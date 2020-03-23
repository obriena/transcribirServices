package com.flyingspheres.services.application.transcribiendo;

import com.flyingspheres.services.application.models.ServerMessage;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1p1beta1.*;

import java.io.ByteArrayInputStream;
import java.util.List;

public class GoogleTranscribir implements Transcribir {
    String storageUri = "gs://transcribir_audio/cono.mp3";

    @Override
    public ServerMessage transcribe(String language, ByteArrayInputStream bis, String uid, String userId) {
        ServerMessage message = new ServerMessage();

        try (SpeechClient speechClient = SpeechClient.create()) {

            // Sample rate in Hertz of the audio data sent
            int sampleRateHertz = 44100;

            SpeakerDiarizationConfig speakerDiarizationConfig = SpeakerDiarizationConfig.newBuilder()
                    .setEnableSpeakerDiarization(true)
                    .setMinSpeakerCount(2)
                    .setMaxSpeakerCount(10)
                    .build();

            // Encoding of audio data sent. This sample sets this explicitly.
            // This field is optional for FLAC and WAV audio formats.
            RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.MP3;
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setLanguageCode(language)
                            .setSampleRateHertz(sampleRateHertz)
                            .setEncoding(encoding)
                            .setDiarizationConfig(speakerDiarizationConfig)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(storageUri).build();

            // Use non-blocking call for getting file transcription
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                    speechClient.longRunningRecognizeAsync(config, audio);
            int seconds = 0;
            while (!response.isDone()) {
                System.out.println("Waiting for response: " + ++seconds + " seconds");
                Thread.sleep(1000);
            }

            List<SpeechRecognitionResult> results = response.get().getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s\n", alternative.getTranscript());
                message.setMessage(alternative.getTranscript());
            }

        } catch (Exception exception) {
            System.err.println("Failed to create the client due to: " + exception);
        }
        return message;
    }
}
