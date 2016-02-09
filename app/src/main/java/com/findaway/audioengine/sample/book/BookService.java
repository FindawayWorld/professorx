package com.findaway.audioengine.sample.book;

import com.findaway.audioengine.sample.audiobooks.Content;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by agofman on 2/9/16.
 */
public interface BookService {

    @GET("audiobooks/{id}")
    Call<Content> getContent(@Header("Session-Key") String sessionId, @Path("id") String audiobookId);
}
