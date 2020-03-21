package com.flyingspheres.services.application.transcribiendo;

import com.flyingspheres.services.application.models.ServerMessage;
import org.junit.Test;

public class TestGoogleTranscribir {

    @Test
    public void testGoogleTranscribir(){
        Transcribir transcribir = new GoogleTranscribir();
        ServerMessage message = transcribir.transcribe("es-ES", null);
        System.out.println(message.getMessage());
    }
}
