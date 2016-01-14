package com.findaway.tutorial.authentication.audioengine;

import java.util.ArrayList;

/**
 * Created by kkovach on 1/13/16.
 */
public class AudioEngineSessionRequest {

    public ArrayList<String> account_ids;
    public String consumer_key;

    public AudioEngineSessionRequest() {

        account_ids = new ArrayList<String>();
    }
}
