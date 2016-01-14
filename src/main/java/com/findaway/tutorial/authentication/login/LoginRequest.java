package com.findaway.tutorial.authentication.login;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kkovach on 1/13/16.
 */
public class LoginRequest {

    @JsonProperty
    String username;
    @JsonProperty
    String password;
}
