package com.flyingspheres.services.application.rest;


import com.flyingspheres.services.application.models.Media;
import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.transcribiendo.Transcribir;
import com.flyingspheres.services.application.transcribiendo.WatsonAsyncTranscribir;
import com.flyingspheres.services.application.transcribiendo.WatsonTranscribir;
import com.flyingspheres.services.application.util.DataManager;
import com.flyingspheres.services.application.util.GerenteSensible;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Path("fileUpload")
public class FileUploadService {
    @Inject
    DataManager dataManager;

    @Context
    private HttpServletRequest httpRequest;

    @Inject
    private @Named("Entorno")
    GerenteSensible gerenteData;


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json; charset=UTF-8")
    public Response uploadFile(){
        long startTime = System.currentTimeMillis();
        System.out.println("Uploading file: 00:00:00");
        boolean isMultiPart = ServletFileUpload.isMultipartContent(httpRequest);
        String transcripcion = "Transcript failed";
        ServletFileUpload upload = new ServletFileUpload();
        ServerMessage message = new ServerMessage();
        String userId = null;
        String notas = null;
        String fileName = null;
        String language = null;
        try {
            // Parse the request
            FileItemIterator iter = upload.getItemIterator(httpRequest);

            byte[] uploadedBytes = null;
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value =  Streams.asString(stream);
                    if (name.equals("userId")){
                        userId = value;
                    }
                    else if (name.equals("notas")){
                        notas = value;
                    } else if (name.equals("language")) {
                        language = value;
                    }
                } else {
                    System.out.println("File field " + name + " with file name " + item.getName() + " detected.");
                    fileName = item.getName();
                    uploadedBytes = IOUtils.toByteArray(stream);
                }
            }

            long uploadTime = System.currentTimeMillis();
            long elapsedTime = uploadTime - startTime;
            System.out.println("archivo subido: " + (elapsedTime/1000.0));

            ByteArrayInputStream bis = new ByteArrayInputStream(uploadedBytes);
            System.out.println("Conversion completa");

            if (language == null) {
                System.out.println("Language not set in form using default");
                language = "es-ES_BroadbandModel";
            }

            UUID uid = UUID.randomUUID();
            Transcribir transcribir = new WatsonAsyncTranscribir();
            if (transcribir instanceof  WatsonAsyncTranscribir){
                ((WatsonAsyncTranscribir) transcribir).setGerenteData(gerenteData);
                ((WatsonAsyncTranscribir) transcribir).setDataManager(dataManager);
            }

            ServerMessage tranMessage = transcribir.transcribe(language, bis, uid.toString(), userId);

            message.setStatus(true);
            message.setMessage(tranMessage.getMessage());

            long asyncRequest = System.currentTimeMillis();
            try {
                Media media = new Media();
                media.setMediaId(uid.toString());
                media.setUserId(userId);
                media.setNotas(notas);
                media.setFileName(fileName);
                media.setMediaData(uploadedBytes);
                media.setTranscription(tranMessage.getMessage());
                media.setIdioma(language);

                dataManager.guardarMedia(media);
                long mongoSave = System.currentTimeMillis();
                System.out.println("Mongo Save Complete: " + ((mongoSave - asyncRequest)/1000.0) );
                //clearing out the data bytes so we don't send them back to the user.
                media.setMediaData(null);
                message.setPayload(media);

            } catch (Throwable t) {
                message.setStatus(false);
                message.setMessage(t.getMessage());
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Process Complete total time: " + ((System.currentTimeMillis() - startTime)/1000.0));
        return Response.ok(message).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_ENCODING, "UTF-8").build();
    }
}
