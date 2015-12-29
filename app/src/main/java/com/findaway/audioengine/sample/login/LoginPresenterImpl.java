package com.findaway.audioengine.sample.login;

/**
 * Created by kkovach on 12/28/15.
 */
public class LoginPresenterImpl implements LoginPresenter, LoginListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginView loginView) {

        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentials(String username, String password) {

        loginView.showProgress(true);
        loginInteractor.login(username, password, this);
    }

    @Override
    public void usernameError() {

        loginView.setUsernameError();
        loginView.showProgress(false);
    }

    @Override
    public void passwordError() {

        loginView.setPasswordError();
        loginView.showProgress(false);
    }

    @Override
    public void success() {

        System.out.println("Presenting success!");
        System.out.println("Hiding progress...");
        loginView.showProgress(false);
        System.out.println("Navigating to home...");
        loginView.navigateToHome();
    }
}
