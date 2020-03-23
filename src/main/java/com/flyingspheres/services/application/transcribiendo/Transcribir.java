package com.flyingspheres.services.application.transcribiendo;

import com.flyingspheres.services.application.models.ServerMessage;

import java.io.ByteArrayInputStream;


public interface Transcribir {
    public ServerMessage transcribe (String language, ByteArrayInputStream bis, String uid, String userId);
}
