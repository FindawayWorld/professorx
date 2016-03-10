package com.findaway.audioengine.sample.book;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.findaway.audioengine.PlaybackListener;
import com.findaway.audioengine.exceptions.AudioEngineException;
import com.findaway.audioengine.mobile.AudioEngine;
import com.findaway.audioengine.mobile.PlaybackEngine;
import com.findaway.audioengine.mobile.PlayerState;
import com.findaway.audioengine.model.PlaybackError;
import com.findaway.audioengine.model.PlaybackEvent;
import com.findaway.audioengine.model.PlaybackProgressEvent;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.Chapter;
import com.findaway.audioengine.sample.audiobooks.Content;

/**
 * Created by agofman on 3/7/16.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener, PlaybackListener, BookView, SeekBar.OnSeekBarChangeListener {

    static String TAG = "Player Fragment";
    ImageButton playButton, backButton, forwardButton;
    private PlaybackEngine mPlaybackEngine;
    private String mSessionId, mContentId, mAccountId;
    TextView mTitle, mChapter, mPlayed, mDuration;
    SeekBar mSeekBar;
    PlaybackProgressEvent mPlaybackProgressEvent;
    PlaybackEvent mPlaybackEvent;
    private Handler uiHandler;
    private Content mContent;
    int mSeekTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BookPresenter bookPresenter = new BookPresenterImpl(this);

        mSessionId = getArguments().getString(BookActivity.EXTRA_SESSION_ID);
        mContentId = getArguments().getString(BookActivity.EXTRA_CONTENT_ID);
        mAccountId = getArguments().getString(BookActivity.EXTRA_ACCOUNT_ID);

        try {
            mPlaybackEngine = AudioEngine.getPlaybackEngine();

        } catch (AudioEngineException e) {
            Log.e(TAG, "Playback engine error.");
        }

        bookPresenter.getContent(mSessionId, mContentId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        uiHandler = new Handler();

        playButton = (ImageButton) view.findViewById(R.id.barPlay);
        playButton.setOnClickListener(this);

        backButton = (ImageButton) view.findViewById(R.id.barBack);
        backButton.setOnClickListener(this);

        forwardButton = (ImageButton) view.findViewById(R.id.barForward);
        forwardButton.setOnClickListener(this);

        mTitle = (TextView) view.findViewById(R.id.barTitle);
        mChapter = (TextView) view.findViewById(R.id.barChapter);
        mPlayed = (TextView) view.findViewById(R.id.played);
        mDuration = (TextView) view.findViewById(R.id.duration);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);

        if (mPlaybackProgressEvent != null) {
            uiHandler.post(updatePlayer);
        }
        mPlaybackEngine.registerPlaybackListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlaybackEngine.unregisterPlaybackListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.barPlay) {
            try {
                if (mPlaybackEngine.getState() == PlayerState.PLAYING) {
                    playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
                    mPlaybackEngine.pause();
                } else if (mPlaybackEngine.getState() == PlayerState.PAUSED) {
                    playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
                    mPlaybackEngine.resume();
                }
            } catch (AudioEngineException ex) {

            }
        } else if (v.getId() == R.id.barBack) {
            try {
                int currChapterNumber = mPlaybackEngine.getCurrentChapter();
                int currPartNumber = mPlaybackEngine.getCurrentPart();
                Chapter prevChapter = null;
                for (Chapter chapter : mContent.chapters) {
                    if (chapter.chapter_number == currChapterNumber && chapter.part_number == currPartNumber) {
                        int index = mContent.chapters.indexOf(chapter);
                        prevChapter = mContent.chapters.get(index - 1);
                    }
                }
                mPlaybackEngine.play(mAccountId, null, mContentId, prevChapter.part_number, prevChapter.chapter_number, 0);
            } catch (AudioEngineException ex) {

            }

        } else if (v.getId() == R.id.barForward) {
            try {
                int currChapterNumber = mPlaybackEngine.getCurrentChapter();
                int currPartNumber = mPlaybackEngine.getCurrentPart();
                Chapter nextChapter = null;
                for (Chapter chapter : mContent.chapters) {
                    if (chapter.chapter_number == currChapterNumber && chapter.part_number == currPartNumber) {
                        int index = mContent.chapters.indexOf(chapter);
                        try {
                            nextChapter = mContent.chapters.get(index + 1);
                            mPlaybackEngine.play(mAccountId, null, mContentId, nextChapter.part_number, nextChapter.chapter_number, 0);
                        }
                        catch (Exception ex) {
                            Toast.makeText(getActivity(), "This is the last chapter", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            } catch (AudioEngineException ex) {

            }
        }
    }

    @Override
    public void error(PlaybackError playbackError) {
        Log.e(TAG, "Playback failed: " + playbackError.message);

        Log.e(TAG, "Playback failed: " + playbackError.code + " - " + playbackError.message);

        if (playbackError.code.equals(PlaybackError.CONTENT_NOT_FOUND)) {
            Toast.makeText(getActivity(), "Playback error: " + playbackError.code + " - Content not found.", Toast.LENGTH_SHORT).show();
        } else if (playbackError.code.equals(PlaybackError.CHAPTER_NOT_FOUND)) {
            Toast.makeText(getActivity(), "Playback error: " + playbackError.code + " - Chapter not found.", Toast.LENGTH_SHORT).show();
        } else if (playbackError.code.equals(PlaybackError.UNAUTHORIZED)) {
            Toast.makeText(getActivity(), "Playback error: " + playbackError.code + " - User does not have permission. Check credentials.", Toast.LENGTH_SHORT).show();
        } else if (playbackError.code.equals(PlaybackError.FORBIDDEN)) {
            Toast.makeText(getActivity(), "Playback error: " + playbackError.code + " - Account does not have permission.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Playback error: " + playbackError.code + " - Not curently handled in sample app.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(PlaybackProgressEvent playbackProgressEvent) {
        Log.i(TAG, "Got playback progress event. Duration is " + playbackProgressEvent.duration + ", position is " + playbackProgressEvent.position);
            mPlaybackProgressEvent = playbackProgressEvent;
            uiHandler.post(updatePlayer);
    }

    private Runnable updatePlayer = new Runnable() {
        public void run() {
            mTitle.setText(mPlaybackProgressEvent.content.title);
            mChapter.setText(String.format("Chapter %1$d Part %2$d", mPlaybackProgressEvent.chapter.chapterNumber, mPlaybackProgressEvent.chapter.partNumber));
            mSeekBar.setMax(mPlaybackProgressEvent.duration);
            mSeekBar.setProgress(mPlaybackProgressEvent.position);
            mSeekBar.setSecondaryProgress(mPlaybackProgressEvent.duration * (mPlaybackProgressEvent.bufferedPercentage / 100));
            mDuration.setText(com.findaway.audioengine.mobile.util.ContentUtils.getTimeString(mPlaybackProgressEvent.duration));
            mPlayed.setText(com.findaway.audioengine.mobile.util.ContentUtils.getTimeString(mPlaybackProgressEvent.position));
        }
    };

    @Override
    public void update(PlaybackEvent playbackEvent) {
        mPlaybackEvent = playbackEvent;
    }

    @Override
    public void setContent(Content content) {
        mContent = content;
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mPlaybackEngine.seekTo(mSeekTo);
        mPlaybackEngine.registerPlaybackListener(this);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mPlaybackEngine.unregisterPlaybackListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mSeekTo = progress;
    }
}
