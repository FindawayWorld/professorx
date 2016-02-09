package com.findaway.audioengine.sample.book;

import com.findaway.audioengine.sample.audiobooks.Content;

/**
 * Created by agofman on 2/9/16.
 */
public class BookPresenterImpl implements BookPresenter, BookListener {

    BookInteractor mBookInteractor;
    BookView mBookView;

    public BookPresenterImpl(BookView bookView){
        mBookView = bookView;
        mBookInteractor = new BookInteractorImpl();
    }

    @Override
    public void getContent(String sessionId, String audiobookId) {
        mBookInteractor.getContent(sessionId, audiobookId, this);
    }

    @Override
    public void success(Content content) {
        mBookView.setContent(content);
    }

    @Override
    public void error(Integer code, String message) {

    }
}
