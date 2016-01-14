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

Create a Login class that will be responsible for taking in a LoginRequest, validating the username and password, and returning a HTTP 200 or 401.

``` Java
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
}
```

