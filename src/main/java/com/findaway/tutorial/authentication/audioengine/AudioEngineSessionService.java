package com.findaway.tutorial.authentication.audioengine;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by kkovach on 1/13/16.
 */
public interface AudioEngineSessionService {

    @POST("sessions")
    Call<AudioEngineSession> getSession(@Header("Api-Key") String apiKey, @Body AudioEngineSessionRequest audioEngineSessionRequest);
}

