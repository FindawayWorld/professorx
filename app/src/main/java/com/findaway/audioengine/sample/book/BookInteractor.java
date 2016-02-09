package com.findaway.audioengine.sample.book;

/**
 * Created by agofman on 2/9/16.
 */
public interface BookInteractor {
    public void getContent(String sessionId, String audiobookId, BookListener bookListener);
}
