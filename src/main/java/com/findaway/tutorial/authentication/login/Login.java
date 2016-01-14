package com.findaway.tutorial.authentication.login;

import com.findaway.tutorial.authentication.audioengine.AudioEngineSession;
import com.findaway.tutorial.authentication.audioengine.AudioEngineSessionRequest;
import com.findaway.tutorial.authentication.audioengine.AudioEngineSessionService;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by kkovach on 1/13/16.
 */
// Relative path (endpoint) at which our login service resides
@Path("login")
public class Login {

    /**
     *
     * @return Response - response object indicating this GET method is not allowed
     */
    @GET
    public Response unimplemented() {

        return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
    }

    /**
     *
     * @param loginRequest - The request for authentication containing the username and password
     * @return response - The Response object indicating either OK or FORBIDDEN depending on whether or not the loginRequest was valid
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {

        AudioEngineSession audioEngineSession = null;

        if (authenticate(loginRequest)) {

            try {

                audioEngineSession = getAudioEngineSession(loginRequest.username);

            } catch (IOException e) {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }

            return Response.status(Response.Status.OK).entity(audioEngineSession).build();

        } else {

            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     *
     * @param loginRequest - The login request to validate
     * @return
     */
    private boolean authenticate(LoginRequest loginRequest) {

        if (loginRequest.username.equals("foo") && loginRequest.password.equals("password"))
            return true;

        return false;
    }

    private AudioEngineSession getAudioEngineSession(String username) throws IOException {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api-test.findawayworld.com/v3/sessions").client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();

        AudioEngineSessionService audioEngineSessionService = retrofit.create(AudioEngineSessionService.class);

        AudioEngineSessionRequest sessionRequest = new AudioEngineSessionRequest();
        sessionRequest.account_ids.add("4444");
        sessionRequest.consumer_key = username;

        try {

            System.out.println("Using Audio Engine API Key: " + System.getenv("SECRET_AUDIO_ENGINE_API_KEY"));

            return audioEngineSessionService.getSession(System.getenv("SECRET_AUDIO_ENGINE_API_KEY"), sessionRequest).execute().body();

        } catch (IOException e) {

            System.out.println("Exception getting Audio Engine session: " + e.getMessage());
            throw e;
        }
    }
}
