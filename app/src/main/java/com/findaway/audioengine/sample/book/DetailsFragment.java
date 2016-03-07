package com.findaway.audioengine.sample.book;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.findaway.audioengine.exceptions.AudioEngineException;
import com.findaway.audioengine.mobile.AudioEngine;
import com.findaway.audioengine.mobile.DownloadEngine;
import com.findaway.audioengine.mobile.PlaybackEngine;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.Content;
import com.squareup.picasso.Picasso;

/**
 * Created by agofman on 2/8/16.
 */
public class DetailsFragment extends Fragment implements BookView, View.OnClickListener {

    private BookPresenter mBookPresenter;
    private String mContentId, mAccountId;
    private DownloadEngine mDownloadEngine;
    private PlaybackEngine mPlaybackEngine;
    Button playButton, downloadButton;
    private Content mContent;
    TextView description, genre, narrator, publisher, size, runtime, title, author;
    ImageView cover;
    private FrameLayout mPlayerLayout;

    public DetailsFragment() {
        mBookPresenter = new BookPresenterImpl(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String sessionId = getArguments().getString(BookActivity.EXTRA_SESSION_ID);
        mContentId = getArguments().getString(BookActivity.EXTRA_CONTENT_ID);
        mAccountId = getArguments().getString(BookActivity.EXTRA_ACCOUNT_ID);
        if (sessionId != null) {
            try {
                mDownloadEngine = AudioEngine.getDownloadEngine();
                mPlaybackEngine = AudioEngine.getPlaybackEngine();
            } catch (AudioEngineException e) {
                Log.e(getTag(), "Download engine error.");
            }
            mBookPresenter.getContent(sessionId, mContentId);
        } else {
            Log.e(getTag(), "Session Id was null. Not getting book chapter list.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        mPlayerLayout = (FrameLayout) getActivity().findViewById(R.id.player);

        cover = (ImageView) view.findViewById(R.id.cover);
        title = (TextView) view.findViewById(R.id.title);
        author = (TextView) view.findViewById(R.id.author);
        description = (TextView) view.findViewById(R.id.description);
        genre = (TextView) view.findViewById(R.id.genre);
        narrator = (TextView) view.findViewById(R.id.narrator);
        publisher = (TextView) view.findViewById(R.id.publisher);
        size = (TextView) view.findViewById(R.id.size);
        runtime = (TextView) view.findViewById(R.id.runtime);
        playButton = (Button) view.findViewById(R.id.play);
        playButton.setOnClickListener(this);
        downloadButton = (Button) view.findViewById(R.id.download);
        downloadButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void setContent(Content content) {
        mContent = content;
        Picasso.with(getActivity()).load(getActivity().getResources().getString(R.string.MR_IMAGE_COVER_BASE) + mContent.id).into(cover);
        title.setText(mContent.title);
        author.setText(mContent.author.get(0));
        description.setText(Html.fromHtml(mContent.description));
        genre.setText(mContent.genre);
        narrator.setText(mContent.narrator.get(0));
        publisher.setText(mContent.publisher);
        size.setText(mContent.actual_size);
        runtime.setText(mContent.runtime);
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.download) {
            try {
                mDownloadEngine.download(mContentId, mContent.chapters.get(0).part_number, mContent.chapters.get(0).chapter_number, null, mAccountId, true, true);
            } catch (AudioEngineException ex) {
                Log.e(getTag(), "Problem while downloading content");
            }
        } else if (v.getId() == R.id.play) {
            if (playButton.getText().equals("Play")) {
                mPlaybackEngine.play(mAccountId, null, mContentId, mContent.chapters.get(0).part_number, mContent.chapters.get(0).chapter_number, 0);
                mPlayerLayout.setVisibility(View.VISIBLE);
                playButton.setText("Stop");
            } else if ( playButton.getText().equals("Stop")) {
                mPlaybackEngine.stop();
                mPlayerLayout.setVisibility(View.GONE);
                playButton.setText("Play");
            }
        }
    }
}
