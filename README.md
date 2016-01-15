# Professor X

# Tutorial 2 - Obtaining an Audio Engine Session

In order to do anything useful with Audio Engine you need a session. That session can be used to get a list of your books, download them, as well as play them back. It is not recommened that you get your Audio Engine session for your Android app directly. Instead, you should be communicating with a service (most likely an authentication service) of some kind that is responsible for validting the user of your Android app and creating an Audio Engine session for them.

This tutorial is not specific to Android applications. Instead, it focuses on the service you might create that is responsible for authenticating a user of an Android app, creating an Audio Engine session for them, and then returning that session to then so that it can be used to access the functionality available through Audio Engine.

## Terminolgy

* User - Your end user (customer) who is authenticated to your system and expects to access audio books through your application.
* Consumer - Your end user (customer) from Audio Engine's perspective.
* Account - A container used to group your audio book purchases (licenses). Depending on your business model this account could include one or many Consumers.
* Login Service - The process through which you validate the user to your system.
* Session Service - The process through which you create an Audio Engine session for your authenticated user.

### Authenticating Your User
Depending on if you're creating an application from scratch or adding audio books to an application that already exists you will undoubtedly have some kind of login service that authenticates your user. This service provides a good place to also create an Audio Engine session for your user and return it to your app.

For the purposes of this tutorial we will be creating a RESTful web service using Java [Jersey] (https://jersey.java.net/) & [Grizzly] (https://grizzly.java.net/). There are many, many ways this can be done and we will not go into detail on that here. It is assumed that you are already familiar with these technologies or are working with others who are. This tutorial is based off an [example created by Oracle] (http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/griz_jersey_intro/Grizzly-Jersey-Intro.html).

#### Login Request
Our login service will accept a JSON formatted login request and validate the username and password that make up that request. First, let's create that LoginRequest class.

``` Java
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    @JsonProperty
    String username;
    @JsonProperty
    String password;
}
```

Our web service will accept HTTP POST requests with a JSON body and translate that request into this object. In our example all of this is the responsibility of Jersey & Grizzly and we will not go over the details here. It is however important to understand that this object represents the login request you'll on behalf of your user who's logging into your application.

#### Login
Next, we create a Login class that will be responsible for taking in a the LoginRequest above, validating the username and password, and returning a HTTP 200 or 401. The annotations in this class are part of JAX-RS specification. Again, we are not going into detail about ther here. Just know that they help Jersey & Grizzly route and handle the incoming login request.

``` Java
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

            // If it is a valid username and password we return an HTTP 200
            return Response.status(Response.Status.OK).entity(audioEngineSession).build();

        } else {

            // If the username and password fail validation we return an HTTP 401
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     *
     * @param loginRequest - The login request to validate
     * @return
     */
    private boolean authenticate(LoginRequest loginRequest) {

        // Simplistic validation of username and password
	// This is where each partner would implement their specific user validation and return either true or false
        if (loginRequest.username.equals("foo") && loginRequest.password.equals("password"))
            return true;

        return false;
    }
}
```

#### Audio Engine Session Request
Now that we have our user logged in we're going to concentrate on creating an Audio Engine session for them. Let's create a class that will hold the session creation information for that request. That is a 'consumer_key' (the id of your system user) and a list of 'account_ids' that this session will be valid for.

``` Java
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class AudioEngineSessionRequest {

    @JsonProperty
    public ArrayList<String> account_ids;
    @JsonProperty
    public String consumer_key;

    public AudioEngineSessionRequest() {

        account_ids = new ArrayList<String>();
    }
}
```

#### Audio Engine Session
We also need a class that will hold the session created and returned by Audio Engine.

``` Java
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;


public class AudioEngineSession {

    @JsonProperty
    public ArrayList<String> restrictions;
    @JsonProperty
    public String namespace;
    @JsonProperty
    public String consumer_key;
    @JsonProperty
    public String session_key;
    @JsonProperty
    public ArrayList<String> account_ids;
}
```

#### Accessing the Audio Engine API
Now that we have our session request and session objects we need to actualy make the request. For this we will be utilizing a library called [Retrofit] (http://square.github.io/retrofit/). Retrofit makes it very, very easy to consume a REST API over HTTP. You just create an interface for your URIs and Retrofit will generate the classes necessary to access them. Let's go ahead and create that interface now.

``` Java
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

public interface AudioEngineSessionService {

    @POST("sessions")
    Call<AudioEngineSession> getSession(@Header("Api-Key") String apiKey, @Body AudioEngineSessionRequest audioEngineSessionRequest);
}
```

In our interface you can see where we supply the AudioEngineSessionRequest and AudioEngineSession classes we created above. The URI for our session resource from the Audio Engine API is located at "sessions". You can see that indicated in the @POST annotation on the getSession() method. Finally, a call to get a session requires your Audio Engine API key in a header which should be kept secret and not included in your code. We'll see later where we set this up.



