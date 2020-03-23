package com.flyingspheres.services.application.transcribiendo;

import com.flyingspheres.services.application.models.ServerMessage;
import com.flyingspheres.services.application.util.DataManager;
import com.flyingspheres.services.application.util.GerenteSensible;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.http.ServiceCallback;
import com.ibm.cloud.sdk.core.security.AuthenticatorConfig;
import com.ibm.cloud.sdk.core.security.basicauth.BasicAuthConfig;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;

import javax.annotation.RegEx;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Aaron O'Brien on 3/21/20.
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
public class WatsonAsyncTranscribir implements Transcribir {
    private GerenteSensible gerenteData;
    private DataManager dataManager;

    /**
     *
     * @param language
     * @param bis
     * @param uuid
     * @param userId
     * @return
     */
    @Override
    public ServerMessage transcribe(String language, ByteArrayInputStream bis, String uuid, String userId) {
        ServerMessage message = new ServerMessage();
        try {
            String apiString = getGerenteData().getApiKeyString();
            AuthenticatorConfig authenticatorConfig = new BasicAuthConfig.Builder()
                    .username("apikey")
                    .password(apiString)
                    .build();
            SpeechToText service = new SpeechToText(authenticatorConfig);

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
                System.out.println("llamada watson con la solicitud" + Calendar.getInstance().getTime().toString());

                ServiceCallback<SpeechRecognitionResults> callback = new AsyncServiceCallBack(uuid, userId, dataManager);

                service.recognize(recognizeOptions).enqueue(callback);

                message.setStatus(true);
                message.setMessage("{ \"results\": [{  \"final\": true,  \"alternatives\": [    {      \"transcript\": \"Transcripción Presentada\"}]}]}");
            } catch (Throwable t) {
                message.setStatus(false);
                message.setMessage(t.getMessage());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return message;
    }

    public GerenteSensible getGerenteData() {
        return gerenteData;
    }

    public void setGerenteData(GerenteSensible gerenteData) {
        this.gerenteData = gerenteData;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}
