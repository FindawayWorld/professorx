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

        InputStreamReader in = new InputStreamReader(System.in);
        int selection = 0;
        String ip;

        // Print selection question
        printIpSelection();

        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        selection = scanner.nextInt();

        ip = getIpSelection(selection);

        BASE_URI = "http://" + ip + ":8080/myapp/";

        System.out.println("LoginService starting at " + BASE_URI);

        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.rest package
        final ResourceConfig rc = new ResourceConfig().register(JacksonFeature.class).packages("com.findaway.tutorial.authentication");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    private void printIpSelection() {

        int i;

        try {

            System.out.println("Select IP address for test auth server:");  // often returns "127.0.0.1"
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            i = 0;

            for (; n.hasMoreElements(); ) {

                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();

                for (; a.hasMoreElements(); ) {

                    InetAddress addr = a.nextElement();
                    System.out.println(i + 1 + ".)  " + addr.getHostAddress());
                    i++;
                }
            }

        } catch (SocketException e) {

            e.printStackTrace();
        }
    }

    private String getIpSelection(int i) {

        int j = 0;

        try {

            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();

            for (; n.hasMoreElements(); ) {

                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();

                for (; a.hasMoreElements(); ) {

                    InetAddress addr = a.nextElement();
                    if (j + 1 == i)
                        return addr.getHostAddress();
                    else
                        j++;
                }
            }

        } catch (SocketException e) {

            e.printStackTrace();
        }

        throw new IllegalArgumentException("Selection not valid.");
    }

    public static void main(String[] args) throws IOException {

        LoginService loginService = new LoginService();
        loginService.startServer();
    }
}
