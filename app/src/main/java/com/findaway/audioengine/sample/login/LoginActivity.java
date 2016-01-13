package com.findaway.audioengine.sample.login;

import com.findaway.audioengine.sample.MainActivity;
import com.findaway.audioengine.sample.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        presenter = new LoginPresenterImpl(this);
    }

    @Override
    public void showProgress(final boolean show) {

        Log.i(TAG, "Show progress? " + show);

        mLoginFormView.post(new Runnable() {
            @Override
            public void run() {

                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void setUsernameError() {

        username.post(new Runnable() {
            @Override
            public void run() {

                username.setError(getString(R.string.error_invalid_email));
            }
        });
    }

    @Override
    public void setPasswordError() {

        password.post(new Runnable() {
            @Override
            public void run() {

                password.setError(getString(R.string.error_invalid_password));
            }
        });
    }

    @Override
    public void navigateToHome() {

        Log.i(TAG, "Navigating to MainActivity...");

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.email_sign_in_button)
    public void login() {

        Log.i(TAG, "Logging in...");

        presenter.login(username.getText().toString(), password.getText().toString());
    }
}

