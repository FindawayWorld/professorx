package com.findaway.audioengine.sample.audiobooks;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by agofman on 1/28/16.
 */
public interface AudiobookService {

    @GET("accounts/{id}/audiobook")
    Call<List<Content>> getContentList(@Path("id") String accountId);
}
