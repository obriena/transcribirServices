package com.flyingspheres.services.application.rest;

import com.flyingspheres.services.application.models.Credentials;
import com.flyingspheres.services.application.models.ServerMessage;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
@Path("callback")
public class TranscribirCallback {

    @GET
    @Path("/results")
    public Response receiveCallback(@HeaderParam ("X-Callback-Signature") String callbackSignature, @QueryParam("challenge_string") String challengeString){
        /*
            you can calculate an HMAC-SHA1 signature of the challenge string by using the secret as the key
         */
        System.out.println("TranscribirCallback: Received a callback with signature: " + callbackSignature);
        System.out.println("TranscribirCallback: Received a challengeString of: " + challengeString);
        ServerMessage message = new ServerMessage();
        return Response.ok(challengeString).build();
    }
}
