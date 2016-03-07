package com.findaway.audioengine.sample.login;

import com.google.gson.Gson;

import com.findaway.audioengine.sample.AudioEngineSession;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by kkovach on 12/28/15.
 */
public class LoginInteractorImpl implements LoginInteractor, Callback<AudioEngineSession> {

    private LoginService mLoginService;
    private static final String LIBRARY = "library";
    private static final String RETAIL = "retail";
    private static final String LIBRARY_ACCOUNT = "4444";
    private static final String RETAIL_ACCOUNT = "kevretail";
    private boolean mIsLibrary;
    private AudioEngineSession mSession;
    private LoginListener mLoginListener;

    public LoginInteractorImpl() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://172.29.96.244:8080/myapp/").client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        mLoginService = retrofit.create(LoginService.class);
    }

    @Override
    public void login(final String username, final String password, final LoginListener listener) {

        mLoginListener = listener;

        // Authenticate your user to your system with username and password
        LoginRequest loginRequest = new LoginRequest(username, password);

        mLoginService.login(loginRequest).enqueue(this);
    }

    @Override
    public void onResponse(Response<AudioEngineSession> loginResponse, Retrofit retrofit) {

        if (loginResponse.isSuccess()) {

            mLoginListener.success(loginResponse.body());

        } else {

            mLoginListener.error(loginResponse.code(), loginResponse.message());
        }
    }

    @Override
    public void onFailure(Throwable t) {

        mLoginListener.error(500, t.getMessage());
    }
}
