package com.flyingspheres.services.application.util;

import com.flyingspheres.services.application.ModelAdaptor;
import com.flyingspheres.services.application.models.Credentials;
import com.flyingspheres.services.application.models.Media;
import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.models.User;
import com.ibm.websphere.crypto.PasswordUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Binary;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron O'Brien on 11/26/19.
 * <p>
 * This SOFTWARE PRODUCT is provided by THE PROVIDER "as is" and "with all faults."
 * <p>
 * THE PROVIDER makes no representations or warranties of any kind concerning the safety, suitability, lack of viruses,
 * inaccuracies, typographical errors, or other harmful components of this SOFTWARE PRODUCT. There are inherent dangers
 * in the use of any software, and you are solely responsible for determining whether this SOFTWARE PRODUCT is compatible
 * with your equipment and other software installed on your equipment. You are also solely responsible for the protection
 * of your equipment and backup of your data, and THE PROVIDER will not be liable for any damages you may suffer in
 * connection with using, modifying, or distributing this SOFTWARE PRODUCT.
 */
public class DataManager {
    @Inject
    MongoDatabase mongoDb;

    @Resource( lookup = "userDataCollection", name= "userDataCollection")
    String userCollection;

    @Resource( lookup = "mediaDataCollection", name= "mediaDataCollection")
    String mediaCollection;

    public ServerMessage validateMongoConnection() {
        ServerMessage message = new ServerMessage();
        StringBuffer buffer = new StringBuffer();
        buffer.append("Null Check for DB: ");
        buffer.append(mongoDb == null);
        message.setStatus(mongoDb==null);
        if (mongoDb != null) {
            buffer.append(" collections: ");
            buffer.append("DB Name: ");
            buffer.append(mongoDb.getName());
            message.setMessage(buffer.toString());
            System.out.println(buffer.toString());
        } else {
            message.setMessage("DB Injection failed.  DB Object is null.");
        }
        return message;
    }

    public List<Document> validateCredentials(Credentials credentials) {

        List<Document> found = new ArrayList<Document>();
        try {
            Document filterUser = new Document();
            Document credFilter = new Document();
            credFilter.put("userId", credentials.getUserId());
            credFilter.put("password", PasswordUtil.encode(credentials.getPassword()));
            filterUser.put("credentials", credFilter);

            FindIterable<Document> result = mongoDb.getCollection(userCollection).find().filter(filterUser);

            for (Document doc : result) {
                found.add(doc);
            }
        } catch (Throwable t){
            t.printStackTrace();
        }
        return found;
    }

    public boolean verificarNombreUnico(String userId) {
        List<Document> found = new ArrayList<Document>();
        try {
            Document filterUser = new Document();
            Document credFilter = new Document();
            credFilter.put("userId", userId);
            filterUser.put("credentials", credFilter);

            FindIterable<Document> result = mongoDb.getCollection(userCollection).find().filter(filterUser);

            for (Document doc : result) {
                found.add(doc);
            }
        } catch (Throwable t){
            t.printStackTrace();
        }
        return found.size() == 0;
    }

    public void guardarUsuarioNuevo(User user) {
        MongoCollection<Document> collection = mongoDb.getCollection(userCollection);
        collection.insertOne(ModelAdaptor.convertUserToDocument(user));
    }

    public List<Media> recuperarMediaParaElUsuario(String userId) {
        //TODO using just the user id to retrieve documents is a security risk
        Document mediaFilter = new Document();
        mediaFilter.put("userId", userId);

        FindIterable<Document> result = mongoDb.getCollection(mediaCollection).find().filter(mediaFilter);
        List<Media> mediaList = new ArrayList<>();
        for (Document doc : result) {
            mediaList.add(ModelAdaptor.convertDocumentToMedia(doc));
        }
        return mediaList;
    }

    public Document recuperarSoloMediaParaElUsuario(String userId, String mediaId) {
        Document mediaFilter = new Document();
        mediaFilter.put("userId", userId);
        mediaFilter.put("mediaId", mediaId);

        FindIterable<Document> result = mongoDb.getCollection(mediaCollection).find().filter(mediaFilter);
        Document document = null;
        for (Document doc : result) {
            document = doc;
            break;
        }
        return document;

    }

    public void guardarMedia(Media media) {
        MongoCollection<Document> collection = mongoDb.getCollection(mediaCollection);
        collection.insertOne(ModelAdaptor.convertDocumentToMedia(media));
    }
}
