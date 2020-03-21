package com.flyingspheres.services.application;

import com.flyingspheres.services.application.models.Credentials;
import com.flyingspheres.services.application.models.Media;
import com.flyingspheres.services.application.models.User;
import com.ibm.websphere.crypto.InvalidPasswordEncodingException;
import com.ibm.websphere.crypto.PasswordUtil;
import com.ibm.websphere.crypto.UnsupportedCryptoAlgorithmException;
import org.bson.Document;


public class ModelAdaptor {

    public static Document convertUserToDocument(User user) {
        Document userDocument = new Document();
        userDocument.put("email", user.getEmail());
        userDocument.put("firstName", user.getFirstName());
        userDocument.put("lastName", user.getLastName());
        userDocument.put("role", user.getRole());

        Document credentialDocument = new Document();
        credentialDocument.put("userId", user.getCredentials().getUserId());
        try {
            String encodedPwd = PasswordUtil.encode(user.getCredentials().getPassword());
            credentialDocument.put("password", encodedPwd);
        } catch (InvalidPasswordEncodingException e) {
            e.printStackTrace();
        } catch (UnsupportedCryptoAlgorithmException e) {
            e.printStackTrace();
        }

        userDocument.put("credentials", credentialDocument);

        return userDocument;
    }

    public static User convertDocumentToUser(Document userDoc) {
        Document credDoc = (Document) userDoc.get("credentials");
        Credentials cred = new Credentials();
        cred.setUserId(credDoc.getString("userId"));

        User user = new User();
        user.setRole(userDoc.getString("role"));
        user.setCredentials(cred);
        user.setEmail(userDoc.getString("email"));
        user.setFirstName(userDoc.getString("firstName"));
        user.setLastName(userDoc.getString("lastName"));

        return user;
    }

    public static Document convertMediaToDocument(Media media) {
        Document mediaDocument = new Document();
        mediaDocument.put("userId", media.getUserId());
        mediaDocument.put("notas", media.getNotas());
        mediaDocument.put("transcription", media.getTranscription());
        mediaDocument.put("fileName", media.getFileName());
        mediaDocument.put("mediaData", media.getMediaData());
        mediaDocument.put("mediaId", media.getMediaId());
        mediaDocument.put("idioma", media.getIdioma());

        return mediaDocument;
    }

    public static Media convertDocumentToMediaNoMediaNoTranscript(Document mediaDoc) {
        Media media = new Media();
        media.setUserId(mediaDoc.getString("userId"));
        media.setNotas(mediaDoc.getString("notas"));
        media.setTranscription(mediaDoc.getString("transcription"));
        media.setFileName(mediaDoc.getString("fileName"));
        media.setMediaId(mediaDoc.getString("mediaId"));
        media.setIdioma(mediaDoc.getString("idioma"));
        return media;
    }

    public static Media convertDocumentToMediaNoMedia(Document mediaDoc) {
        Media media = new Media();
        media.setUserId(mediaDoc.getString("userId"));
        media.setNotas(mediaDoc.getString("notas"));
        media.setTranscription(mediaDoc.getString("transcription"));
        media.setFileName(mediaDoc.getString("fileName"));
        media.setMediaId(mediaDoc.getString("mediaId"));
        media.setIdioma(mediaDoc.getString("idioma"));
        return media;
    }

}
