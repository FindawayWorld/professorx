package com.findaway.audioengine.sample.audiobooks;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by agofman on 1/28/16.
 */
public class AudiobookInteractorImpl implements  AudiobookInteractor, Callback<List<Content>> {

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
    public void getContentList(String accountId) {
        mAudiobookService.getContentList(accountId).enqueue(this);
    }
}
