package com.findaway.audioengine.sample.login;

/**
 * Created by kkovach on 12/28/15.
 */
public interface LoginInteractor {

    public void login(String username, String password, LoginListener listener);
}
