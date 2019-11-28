package com.flyingspheres.services.application.rest;

import com.flyingspheres.services.application.ModelAdaptor;
import com.flyingspheres.services.application.models.Credentials;
import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.models.User;
import com.flyingspheres.services.application.util.DataManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("register")
public class RegistrationService {
    @Inject
    DataManager dataManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {

        boolean nombreUnico = dataManager.verificarNombreUnico(user.getCredentials().getUserId());

        ServerMessage serverMessage = null;
        if (nombreUnico){
            user.setRole("basic");
            dataManager.guardarUsuarioNuevo(user);
            serverMessage.setStatus(true);
            serverMessage.setMessage("Todo está bien. Usario: " + user.getCredentials().getUserId());
        } else {
            serverMessage.setStatus(false);
            serverMessage.setMessage("El nombre de usuario no esta disponible");
        }

        return Response.ok(serverMessage).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_ENCODING, "UTF-8").build();
    }
}
