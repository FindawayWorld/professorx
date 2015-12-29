package com.findaway.audioengine.sample.login;

/**
 * Created by kkovach on 12/28/15.
 */
public interface LoginListener {

    public void usernameError();

    public void passwordError();

    public void success();
}
