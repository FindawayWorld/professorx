#Tutorial 3 - Application Login and Audio Engine Sessions

Now that you have a service setup to validate your users and return a session for you app, we will 
learn how to get and use that session in your app. You will start by designing a login screen that will 
accept a users credentials. The credentials include username and password. When the user clicks the 
login button the authentication will start. 

###LoginActivity
In your login activity, you will need to declare the LoginPresenter that is used in the login process.
You will set this during the onCreate method.

``` Java

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = "LoginActivity";

    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.email)
    AutoCompleteTextView username;
    @Bind(R.id.password)
    EditText password;
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



