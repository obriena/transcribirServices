package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.json.*;
import java.io.InputStream;

/**
 * Created by Aaron O'Brien on 9/2/19.
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
public class CrearCurlMandoPrueba {
    private final String audioFile = "pruebaArchivo_2019_05_06_1700.mp3";
    private final String mando = "curl -X POST -u \"apikey:{apikey}\" " +
            "--header \"Content-Type: audio/mp3\" " +
            "--data-binary @./{audioFile}  \"{url}/v1/recognize?model=es-ES_BroadbandModel\"";


    private final String archivoNombre = "/credenciales.txt";
    private String apiKeyString = null;
    private String urlString = null;


    public void cargarCredenciales() {
        InputStream is = this.getClass().getResourceAsStream(archivoNombre);
        Assert.assertNotNull(is);

        JsonReader reader = Json.createReader(is);
        JsonStructure jsonst = reader.read();

        JsonObject object = (JsonObject) jsonst;
        JsonString apiKey = (JsonString) object.get("apikey");
        JsonString url = (JsonString) object.get("url");
        apiKeyString = apiKey.getString();
        urlString = url.getString();
    }


    public void crearMandoPrueba (){
        Assert.assertNotNull(apiKeyString);
        String curlMando = mando.replace("{apikey}", apiKeyString);
        curlMando = curlMando.replace("{url}", urlString);
        curlMando = curlMando.replace("{audioFile} ", audioFile);
        System.out.println(curlMando);

    }
}
