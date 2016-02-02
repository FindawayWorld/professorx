package com.findaway.audioengine.sample;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kkovach on 12/29/15.
 */
public class AudioEngineSession {

    @SerializedName("session_key")
    public String sessionKey;

    @SerializedName("account_ids")
    public ArrayList<String> account_ids;

    @SerializedName("consumer_key")
    public String consumer_key;
}
