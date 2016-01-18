package com.findaway.tutorial.authentication.login;

import com.findaway.tutorial.authentication.audioengine.AudioEngineSession;
import com.findaway.tutorial.authentication.audioengine.AudioEngineSessionRequest;
import com.findaway.tutorial.authentication.audioengine.AudioEngineSessionService;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

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
    public Response notAllowed() {

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

            return Response.status(Response.Status.UNAUTHORIZED).build();
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

    /**
     *
     * @param username - The user (consumer) for whom we need to create an Audio Engine session
     * @return An new Audio Engine session
     * @throws IOException
     */
    private AudioEngineSession getAudioEngineSession(String username) throws IOException {

        // This is where we configure Retrofit with the Audio Engine API information so that it can generate our
        // service for us and allow us to access the API
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AudioEngineSessionService.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create()).build();

        // Create that service here
        AudioEngineSessionService audioEngineSessionService = retrofit.create(AudioEngineSessionService.class);

        // Construct our session request
        AudioEngineSessionRequest sessionRequest = new AudioEngineSessionRequest();
        sessionRequest.account_ids = getAccounts(username);
        sessionRequest.consumer_key = username;

        try {

            System.out.println("Using Audio Engine API Key: " + System.getenv("SECRET_AUDIO_ENGINE_API_KEY"));

            // Make call to Audio Engine and return the AudioEngineSession object
            return audioEngineSessionService.getSession(System.getenv("SECRET_AUDIO_ENGINE_API_KEY"), sessionRequest).execute().body();

        } catch (IOException e) {

            // Log and throw exception should we encounter one
            System.out.println("Exception getting Audio Engine session: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get accounts associated with the supplied user
     * @param username - Username for which we need to retrieve accounts.
     * @return List of accounts for user
     */
    private ArrayList<String> getAccounts(String username) {

        ArrayList<String> accounts = new ArrayList<>();

        // This is simplified for the tutorial. Here each partner would use the supplied username to get the
        // proper account to which the user belonged
        if(username.equals("foo"))
            accounts.add("4444");

        return accounts;
    }
}
