package com.flyingspheres.services.application.transcribiendo;

import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.util.GerenteSensible;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;

public class WatsonTranscribir implements Transcribir {
    @Inject
    private @Named("Entorno")
    GerenteSensible gerenteData;

    @Override
    public ServerMessage transcribe (String language, ByteArrayInputStream bis) {
        ServerMessage message = new ServerMessage();
        try {
            SpeechToText service = new SpeechToText();
            IamOptions options = new IamOptions.Builder()
                    .apiKey(gerenteData.getApiKeyString())
                    .build();
            service.setIamCredentials(options);
//https://cloud.ibm.com/docs/services/speech-to-text?topic=speech-to-text-models#models

            if (language == null) {
                System.out.println("Language not set in form using default");
                language = "es-ES_BroadbandModel";
            }
            RecognizeOptions recognizeOptions = new RecognizeOptions.Builder()
                    .audio(bis)
                    .model(language)
                    .contentType(HttpMediaType.AUDIO_MP3)
                    .build();
            try {
                System.out.println("llamada watson con la solicitud");
                SpeechRecognitionResults transcript = service.recognize(recognizeOptions).execute().getResult();
                System.out.println(transcript);
                message.setStatus(true);
                message.setMessage(transcript.toString());
            } catch (Throwable t) {
                message.setStatus(false);
                message.setMessage(t.getMessage());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return message;
    }
}
