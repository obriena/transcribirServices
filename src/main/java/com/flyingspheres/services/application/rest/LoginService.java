package com.flyingspheres.services.application.rest;

import com.flyingspheres.services.application.models.Credentials;
import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.util.DataManager;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("authenticate")
public class LoginService {
    @Inject
    DataManager dataManager;

    @Inject
    MongoDatabase mongoDb;

    @Resource( lookup = "userDataCollection", name= "userDataCollection")
    String userCollection;

    @GET
    @Produces("application/json; charset=UTF-8")
    public Response validate() {
        ServerMessage message = dataManager.validateMongoConnection();

        return Response.ok(message).build();
    }

    @POST
    @Produces("application/json; charset=UTF-8")
    public Response authenticateCredentials(Credentials credentials){
        List<Document> found = dataManager.validateCredentials(credentials);

        ServerMessage message = new ServerMessage();
        if (found.size() < 1 || found.size() > 1){
            message.setStatus(false);
            message.setMessage("Usuario no encontrado");
        }
        else if (found.size() == 1) {
            message.setStatus(true);
            message.setMessage(found.get(0).toJson());
        }

        return Response.ok(message).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_ENCODING, "UTF-8").build();
    }
}
