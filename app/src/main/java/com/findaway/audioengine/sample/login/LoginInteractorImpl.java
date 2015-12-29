package com.findaway.audioengine.sample.login;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by kkovach on 12/28/15.
 */
public class LoginInteractorImpl implements LoginInteractor {

    private ScheduledExecutorService mScheduledExecutorService;

    private MockLogin mMockLogin;

    public LoginInteractorImpl() {

        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void login(final String username, final String password, final LoginListener listener) {

        mMockLogin = new MockLogin(username, password, listener);
        System.out.println("Scheduling mock login for 15 seconds...");
        mScheduledExecutorService.schedule(mMockLogin, 15, TimeUnit.SECONDS);
    }

    private class MockLogin implements Runnable {

        private final String mUsername, mPassword;
        private final LoginListener mListener;

        MockLogin(String username, String password, LoginListener listener) {

            mUsername = username;
            mPassword = password;
            mListener = listener;
        }

        @Override
        public void run() {

            System.out.println("Running mock login...");

            boolean error = false;
            if (mUsername == null || mUsername.length() == 0) {
                System.out.println("Username error!");
                mListener.usernameError();
                error = true;
            }
            if (mPassword == null || mPassword.length() == 0) {
                System.out.println("Password error!");
                mListener.passwordError();
                error = true;
            }
            if (!error) {
                System.out.println("Success!");
                mListener.success();
            }
        }
    }
}
