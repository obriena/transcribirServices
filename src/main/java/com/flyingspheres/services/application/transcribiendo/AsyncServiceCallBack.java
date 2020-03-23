package com.flyingspheres.services.application.transcribiendo;

import com.flyingspheres.services.application.ModelAdaptor;
import com.flyingspheres.services.application.models.Media;
import com.flyingspheres.services.application.util.DataManager;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCallback;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.mongodb.BasicDBObject;
import org.bson.Document;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aaron O'Brien on 3/22/20.
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
public class AsyncServiceCallBack implements ServiceCallback<SpeechRecognitionResults> {
    String _mediaId;
    String _userId;
    DataManager _dataManager;

    private AsyncServiceCallBack(){}

    public AsyncServiceCallBack(String mongoId, String userId, DataManager dataManager){
        _mediaId = mongoId;
        _userId = userId;
        _dataManager = dataManager;
    }

    @Override
    public void onResponse(Response<SpeechRecognitionResults> response) {
        System.out.println("llamada recibida: " + Calendar.getInstance().getTime().toString());
        SpeechRecognitionResults transcripts = response.getResult();
        int statusCode = response.getStatusCode();
        String statusMessage = response.getStatusMessage();
        System.out.println("Status Code: " + statusCode);
        System.out.println("Status Message: " + statusMessage);

        StringBuffer buffer = new StringBuffer("{ \"results\": [");
        List<SpeechRecognitionResult> resultList = transcripts.getResults();
        String delim = "";
        for (SpeechRecognitionResult result : resultList) {
            buffer.append(delim);
            buffer.append(result.toString());
            delim = ",";
        }
        buffer.append("]}");

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("transcription", buffer.toString());

        BasicDBObject newDbObject = new BasicDBObject();
        newDbObject.append("$set", basicDBObject);

        _dataManager.actualizarDocumento(_userId, _mediaId, newDbObject);
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }
}
