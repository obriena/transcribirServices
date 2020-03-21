package com.flyingspheres.services.application.rest;

import com.flyingspheres.services.application.ModelAdaptor;
import com.flyingspheres.services.application.models.Media;
import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.util.DataManager;
import org.bson.Document;
import org.bson.types.Binary;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Path("media")
public class MediaRetrievalService {
    @Inject
    DataManager dataManager;

    @GET
    @Path("/findAllForUser/{userId}")
    @Produces("application/json; charset=UTF-8")
    public Response retrieveMessagesForUser(@PathParam("userId") String userId){
        long start = System.currentTimeMillis();
        System.out.println("Retrieving all data for user: " + userId);
        List<Media> mediaList = dataManager.recuperarMediaParaElUsuario(userId);
        long dataRetrieved = System.currentTimeMillis();
        System.out.println("Data Retrieved: " + ((dataRetrieved - start)/1000.0));

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

    @GET
    @Path("/retrieveTranscript/{mediaId}/{userId}")
    @Produces("application/json; charset=UTF-8")
    public Response retrieveTranscript(@PathParam("mediaId") String mediaId, @PathParam("userId") String userId) {
        long start = System.currentTimeMillis();
        System.out.println("Retrieving media record for user: " + userId);
        Document document = dataManager.recuperarSoloMediaParaElUsuario(userId, mediaId);
        long dataRetrieved = System.currentTimeMillis();
        System.out.println("Data Retrieved: " + ((dataRetrieved - start)/1000.0));

        Media media = ModelAdaptor.convertDocumentToMediaNoMedia(document);

        ServerMessage message = new ServerMessage();
        message.setStatus(true);
        message.setMessage("Retrieved One Message");
        message.setPayload(media);

        return Response.ok(message).build();
    }


}
