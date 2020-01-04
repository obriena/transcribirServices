package com.flyingspheres.services.application.util;

import com.flyingspheres.services.application.rest.FileUploadService;

import javax.inject.Named;
import javax.json.*;
import java.io.InputStream;

/**
 * Created by Aaron O'Brien on 11/20/19.
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

@Named("Archivo")
public class GerenteDeCredenciales implements GerenteSensible{

    private final static String archivoNombre = "/credenciales.txt";
    private static String apiKeyString = null;
    private static String urlString = null;
    private static String mongoPwd = null;
    static {
        InputStream is = FileUploadService.class.getResourceAsStream(archivoNombre);
        if (is != null) {
            JsonReader reader = Json.createReader(is);
            JsonStructure jsonst = reader.read();

            JsonObject object = (JsonObject) jsonst;
            JsonString apiKey = (JsonString) object.get("apikey");
            JsonString url = (JsonString) object.get("url");
            JsonString pwd = (JsonString) object.get("mongoPwd");
            apiKeyString = apiKey.getString();
            urlString = url.getString();
            mongoPwd = pwd.getString();
        }
        else {
            System.out.println("Credenciales es nulo");
        }
    }

    public String getApiKeyString(){
        return apiKeyString;
    }

    public String getMongoPwd(){
        return mongoPwd;
    }

    @Override
    public String getMongoUser() {
        throw new RuntimeException("Not implemented yet");
    }

}
