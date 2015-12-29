package com.findaway.audioengine.sample.login;

/**
 * Created by kkovach on 12/28/15.
 */
public interface LoginView {

    public void showProgress(boolean show);

    public void setUsernameError();

    public void setPasswordError();

    public void navigateToHome();
}
