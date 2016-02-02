package com.findaway.audioengine.sample.audiobooks;

import com.findaway.audioengine.sample.login.LoginListener;

/**
 * Created by agofman on 1/28/16.
 */
public interface AudiobookInteractor {

    public void getContentList(String sessionId, String accountId, AudiobookListener loginListener);
}
