package com.findaway.audioengine.sample.book;

import com.findaway.audioengine.sample.audiobooks.Content;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by agofman on 2/9/16.
 */
public class BookInteractorImpl implements BookInteractor, Callback<Content> {
    BookService mBookService;
    BookListener mBookListener;

    BookInteractorImpl() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.findawayworld.com/v3/").client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        mBookService = retrofit.create(BookService.class);
    }

    @Override
    public void onResponse(Response<Content> response, Retrofit retrofit) {
        mBookListener.success(response.body());
    }

    @Override
    public void onFailure(Throwable t) {
        mBookListener.error(500, t.getMessage());
    }

    @Override
    public void getContent(String sessionId, String audiobookId, BookListener bookListener) {
        mBookListener = bookListener;
        mBookService.getContent(sessionId, audiobookId).enqueue(this);
    }
}
