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

import java.util.Locale;

/**
 * Created by agofman on 3/7/16.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener, PlaybackListener, SeekBar.OnSeekBarChangeListener {

    static String TAG = "Player Fragment";
    ImageButton playButton, backButton, forwardButton;
    private PlaybackEngine mPlaybackEngine;
    TextView mTitle, mChapter, mPlayed, mDuration;
    SeekBar mSeekBar;
    PlaybackProgressEvent mPlaybackProgressEvent;
    private Handler uiHandler;
    int mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mPlaybackEngine = AudioEngine.getPlaybackEngine();

        } catch (AudioEngineException e) {
            Log.e(TAG, "Playback engine error.");
        }
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
                Log.e(TAG,  "Failed trying to play or pause.");
            }
        } else if (v.getId() == R.id.barBack) {
            mPlaybackEngine.previousChapter();
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
        } else if (v.getId() == R.id.barForward) {
            mPlaybackEngine.nextChapter();
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
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
        uiHandler.post(updatePlayerProgress);
    }

    private Runnable updatePlayerProgress = new Runnable() {
        public void run() {
            mTitle.setText(mPlaybackProgressEvent.content.title);
            mChapter.setText(String.format(Locale.US, "Chapter %1$d Part %2$d", mPlaybackProgressEvent.chapter.chapterNumber, mPlaybackProgressEvent.chapter.partNumber));
            mSeekBar.setMax(mPlaybackProgressEvent.duration);
            mSeekBar.setProgress(mPlaybackProgressEvent.position);

            Double bufferPercent = mPlaybackProgressEvent.bufferedPercentage.doubleValue() / 100;
            Double buffer = mPlaybackProgressEvent.duration * bufferPercent;
            Integer buff = buffer.intValue();

            Log.i(TAG, buff.toString());
            mSeekBar.setSecondaryProgress(buff);
            mDuration.setText(com.findaway.audioengine.mobile.util.ContentUtils.getTimeString(mPlaybackProgressEvent.duration));
            mPlayed.setText(com.findaway.audioengine.mobile.util.ContentUtils.getTimeString(mPlaybackProgressEvent.position));
        }
    };

    @Override
    public void update(PlaybackEvent playbackEvent) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mPlaybackEngine.seekTo(mProgress);
        mPlaybackEngine.registerPlaybackListener(this);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mPlaybackEngine.unregisterPlaybackListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProgress = progress;
    }
}