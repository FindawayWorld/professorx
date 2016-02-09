package com.findaway.audioengine.sample.audiobooks;
import java.util.List;

/**
 * Created by agofman on 2/2/16.
 */
public class AudiobookPresenterImpl implements AudiobookPresenter, AudiobookListener {

    AudiobookInteractor mAudiobookInteractor;
    AudiobookView mAudiobookView;

    public AudiobookPresenterImpl(AudiobookView audiobookView) {
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
