package com.findaway.audioengine.sample.login;

import com.findaway.audioengine.sample.AudioEngineSession;

/**
 * Created by kkovach on 12/28/15.
 */
public interface LoginListener {

    public void usernameError();

    public void passwordError();

    public void success(AudioEngineSession audioEngineSession);

    public void error(Integer code, String message);
}
