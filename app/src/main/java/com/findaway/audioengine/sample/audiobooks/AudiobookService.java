package com.findaway.audioengine.sample.audiobooks;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by agofman on 1/28/16.
 */
public interface AudiobookService {

    @GET("accounts/{id}/audiobooks")
    Call<List<Content>> getContentList(@Header("Session-Key") String sessionId, @Path("id") String accountId);

}
