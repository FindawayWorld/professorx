package com.findaway.audioengine.sample;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kkovach on 12/29/15.
 */
public interface AuthenticationService {

    @GET("authorize")
    Call<AudioEngineSession> getSession(@Query("account_type") String accountType, @Query("account_id") String accountId,
            @Query("consumer_key") String consumerKey);
}
