package com.flyingspheres.services.application.util;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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
public class NetworkManager {
    /**
     * Returns an open http response.
     * Consumer must close the response.
     * Here is an example how to check the response status
     *
     * <code>
     *       int responseCode = response.getStatus();
     *       response.close();
     * </code>
     *
     * @param url
     * @return
     */
    public static Response makeGetRequest(String url) {
        Client client = ClientBuilder.newClient();
        Invocation.Builder invoBuild = client.target(url).request();
        Response response = invoBuild.get();

        return response;
    }

    public static Response makePostRequestWithJsonBody(String url, String postbody, Map<String, String> headers) {
    /*
    client.target(baseUri)
                     .path(event.getMediaId().toString())
                     .path(person.getMediaId().toString())
                     .request(MediaType.APPLICATION_XML)
                     .post(Entity.xml(userResponse.getLabel()));
     */
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);

        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
        for (String key : headers.keySet()) {
            builder = builder.header(key, headers.get(key));
        }
        Response response = builder.post(Entity.json(postbody));

        return response;
    }

    public static Response makePostRequestWithNoBody(String urlString) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(urlString);

        Invocation.Builder builder = target.request(MediaType.TEXT_PLAIN);

        Response response = builder.post(Entity.text(""));

        return response;
    }
}
