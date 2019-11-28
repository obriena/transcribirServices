package com.flyingspheres.services.application.rest;

import com.flyingspheres.services.application.ModelAdaptor;
import com.flyingspheres.services.application.models.Media;
import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.util.DataManager;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("media")
public class MediaRetrievalService {
    @Inject
    DataManager dataManager;

    @GET
    @Path("/findAllForUser/{userId}")
    @Produces("application/json; charset=UTF-8")
    public Response retrieveMessagesForUser(@PathParam("userId") String userId){
        List<Media> mediaList = dataManager.recuperarMediaParaElUsuario(userId);

        ServerMessage message = new ServerMessage();
        message.setStatus(true);
        message.setMessage("Retrieved " + mediaList.size() + " items");
        message.setPayload(mediaList);

        return Response.ok(message).build();
    }

    @GET
    @Path("/playFileById/{mediaId}/{userId}")
    @Produces("audio/mp3")
    public Response streamAudioFile(@PathParam("mediaId") String mediaId, @PathParam("userId") String userId){

        Document document = dataManager.recuperarSoloMediaParaElUsuario(userId, mediaId);
        final Binary data = (Binary)document.get("mediaData");
        final byte[] bytes = data.getData();

        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    output.write(bytes);
                } catch (Throwable t) {
                    throw new WebApplicationException(t);
                }
            }
        };

        return Response.ok(stream).build();
    }


}
