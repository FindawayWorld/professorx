#Tutorial 3 - Application Login and Audio Engine Sessions

Now that you have a service setup to validate your users and return a session for your app, we will 
learn how to get and use that session in your app. You will start by designing a login screen that will 
accept a users credentials. The credentials include username and password. When the user clicks the 
login button the authentication will start. 

###LoginActivity
In your login activity, you will need to declare the LoginPresenter that is used in the login process.
This is done at the top of the class. You will use this during the onCreate method.

``` Java

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = "LoginActivity";

    private LoginPresenter presenter;
    
```

In your onCreate method you will need to create a new instance of the LoginPresenterImpl object and 
assign it to the LoginPresenter. 

``` Java
@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        presenter = new LoginPresenterImpl(this);
    }
```

The LoginPresenterImpl object contains the methods used to login as well as handling the login process
and errors that could occur during the process. The constructor of the LoginPresenterImpl creates a 
new instance of the LoginInteractorImpl object.  

``` Java
package com.findaway.audioengine.sample.login;

import com.findaway.audioengine.sample.AudioEngineSession;

public class LoginPresenterImpl implements LoginPresenter, LoginListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginView loginView) {

        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void login(String username, String password) {

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
    public void success(AudioEngineSession audioEngineSession) {

        System.out.println("Got key " + audioEngineSession.sessionKey + ". Presenting success!");
        System.out.println("Hiding progress...");
        loginView.showProgress(false);
        System.out.println("Navigating to home...");
        loginView.navigateToHome();
    }

    @Override
    public void error(Integer code, String message) {

        System.out.println("Login error: " + code + " - " + message);
    }
}
```

The LoginInteractorImpl object will handle making the calls to your authentication service. It will 
get the response back from the service and pass that response back to the LoginPresenterImpl object.

``` Java
package com.findaway.audioengine.sample.login;

import com.google.gson.Gson;

import com.findaway.audioengine.sample.AudioEngineSession;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

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

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://172.29.96.209:8080/myapp/").client(okHttpClient)
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
```

Now that you have a LoginPresenter created, you can use it to login. The LoginActivity contains a 
method that will handle the user clicking the login button. This is where you will actually use your
presenter and you will call the login method, passing in the users credentials from the form.
 
``` Java
@OnClick(R.id.email_sign_in_button)
    public void login() {

        Log.i(TAG, "Logging in...");

        presenter.login(username.getText().toString(), password.getText().toString());
    }
```



