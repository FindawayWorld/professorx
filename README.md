#Tutorial 4 - Getting a list of audio books for your account.

In the last tutorial we successfully grabbed the session key from the api. We are now ready to use 
that session key to get a list of audiobook that are accessible by your account. We ended up in the 
"Navigate to Home" method which starts the main activity. The first thing we'll need to do is save 
the data we got back from our api call so that we can use it throughout the app. To do this we'll 
save the values to shared preferences. We start by declaring keys for the values and declaring a 
shared preferences object. 
  
``` Java
public class LoginActivity extends AppCompatActivity implements LoginView {

    public static final String AUDIO_ENGINE_SESSION_KEY = "AUDIO_ENGINE_SESSION_KEY";
    public static final String AUDIO_ENGINE_CONSUMER_KEY = "AUDIO_ENGINE_CONSUMER_KEY";
    public static final String AUDIO_ENGINE_ACCOUNT_IDS = "AUDIO_ENGINE_ACCOUNT_IDS";
    
    private SharedPreferences mSharedPreferences;

```

The shared preferences is set during the onCreate for the login activity.

``` Java
mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
```

Once you have received all of the data from the API and have made it into the Navigate to home method, 
we can save the data to shared preferences.

``` Java
@Override
public void navigateToHome(AudioEngineSession audioEngineSession) {

    Log.i(TAG, "Navigating to MainActivity...");
    mSharedPreferences.edit().putString(AUDIO_ENGINE_SESSION_KEY, audioEngineSession.sessionKey).apply();
    mSharedPreferences.edit().putString(AUDIO_ENGINE_ACCOUNT_IDS, audioEngineSession.account_ids.get(0)).apply();
    mSharedPreferences.edit().putString(AUDIO_ENGINE_CONSUMER_KEY, audioEngineSession.consumer_key).apply();
    startActivity(new Intent(this, MainActivity.class));
    finish();
}
```

In this tutorial we will use a fragment to display the audiobook list. Once the main activity is 
started, we will use it to open our library fragment. Our library fragment will display the list of 
books in a recycler view. The same interactor and presenter implementation will be used as the one 
used for the login process. During the constructor of the library fragment we will setup our audiobook presenter.

``` Java
public LibraryFragment() {
    mAudiobookPresenter = new AudiobookPresenterImpl(this);
}
```

In order to use this presenter we need to grab the session key and account id we saved in our login 
activity. We do this by declaring the shared preference object at the top of the library fragment 
class. We then load the data we need from shared preferences into variables during the onCreate. Once
we have the value we need we can pass them to the getAudiobooks method on the mAudiobookPresenter object
to start the process of getting the book list. 

```Java
public class LibraryFragment extends Fragment implements AudiobookView {
    private SharedPreferences mSharedPreferences;
    ...
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String accountId = mSharedPreferences.getString(LoginActivity.AUDIO_ENGINE_ACCOUNT_IDS, null);
        String sessionId = mSharedPreferences.getString(LoginActivity.AUDIO_ENGINE_SESSION_KEY, null);
        mAudiobookPresenter.getAudiobook(sessionId, accountId);
    }
```

###AudiobookPresenterImpl
The presenters job is the handle the interaction between the view and the interactor. It passes all
of the required data between the objects and helps them communicate. 
``` Java
package com.findaway.audioengine.sample.audiobooks;
import java.util.List;

public class AudiobookPresenterImpl implements AudiobookPresenter, AudiobookListener {

    AudiobookInteractor mAudiobookInteractor;
    AudiobookView mAudiobookView;

    public AudiobookPresenterImpl(AudiobookView audiobookView)
    {
        mAudiobookView = audiobookView;
        mAudiobookInteractor = new AudiobookInteractorImpl();
    }

    @Override
    public void getAudiobook(String sessionId, String accountId) {

        mAudiobookInteractor.getContentList(sessionId, accountId, this);
    }

    @Override
    public void success(List<Content> audiobookList) {
        mAudiobookView.setAudiobookList(audiobookList);
    }

    @Override
    public void error(Integer code, String message) {

    }
}
```

###AudiobookInteractorImpl
The job of the interactor is to handle your api calls and execute business logic. Once the interactor
gets a result it can communicate it back to the presenter. 
``` Java
package com.findaway.audioengine.sample.audiobooks;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AudiobookInteractorImpl implements AudiobookInteractor, Callback<List<Content>> {

    private AudiobookService mAudiobookService;
    private AudiobookListener mAudiobookListener;

    public AudiobookInteractorImpl() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.findawayworld.com/v3/").client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        mAudiobookService = retrofit.create(AudiobookService.class);
    }

    @Override
    public void onResponse(Response<List<Content>> response, Retrofit retrofit) {
        mAudiobookListener.success(response.body());
    }

    @Override
    public void onFailure(Throwable t) {
        mAudiobookListener.error(500, t.getMessage());
    }

    @Override
    public void getContentList(String sessionId, String accountId, AudiobookListener audiobookListener) {
        mAudiobookListener = audiobookListener;
        mAudiobookService.getContentList(sessionId, accountId).enqueue(this);
    }
}
```

The process makes the api call, and returns a list of content objects(discussed in tutorial 1). When
this process completes it passes the list of content objects back to the view to handle displaying
the list of audiobooks. The setAudiobookList method adds each of the content objects to a content 
adapter. The content adapter handles how each content object will display on the screen.

``` Java
@Override
public void setAudiobookList(List<Content> audiobookList) {
    for (Content content: audiobookList)
    {
        mContentAdapter.add(content);
    }
}
```
 
Once this is all put together you will have a recycler view with all of the audiobook you have access
to from your account.