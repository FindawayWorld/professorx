package com.findaway.tutorial.authentication.audioengine;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by kkovach on 1/13/16.
 */
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
