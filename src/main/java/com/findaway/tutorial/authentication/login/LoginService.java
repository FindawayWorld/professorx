package com.findaway.tutorial.authentication.login;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Created by kkovach on 1/12/16.
 */
public class LoginService {

    // Base URI the Grizzly HTTP server will listen on
    public String BASE_URI;

    public HttpServer startServer() {

        String ip = "192.168.0.100";

        BASE_URI = "http://" + ip + ":8080/myapp/";

        System.out.println("LoginService starting at " + BASE_URI);

        // create a resource config that scans for JAX-RS resources and providers
        // in com.findaway.tutorial.authentication package
        final ResourceConfig rc = new ResourceConfig().register(JacksonFeature.class).packages("com.findaway.tutorial.authentication");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {

        LoginService loginService = new LoginService();
        loginService.startServer();
    }
}
