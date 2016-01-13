package com.findaway.audioengine.sample.login;

import com.findaway.audioengine.sample.AudioEngineSession;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by kkovach on 12/29/15.
 */
public interface LoginService {

    @POST("login")
    Call<AudioEngineSession> login(@Body LoginRequest loginRequest);
}
