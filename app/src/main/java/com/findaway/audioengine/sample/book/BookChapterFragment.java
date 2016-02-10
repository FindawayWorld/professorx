package com.findaway.audioengine.sample.book;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.findaway.audioengine.DownloadListener;
import com.findaway.audioengine.config.LogLevel;
import com.findaway.audioengine.exceptions.AudioEngineException;
import com.findaway.audioengine.mobile.AudioEngine;
import com.findaway.audioengine.mobile.DownloadEngine;
import com.findaway.audioengine.model.DownloadError;
import com.findaway.audioengine.model.DownloadEvent;
import com.findaway.audioengine.model.DownloadProgressEvent;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.Chapter;
import com.findaway.audioengine.sample.audiobooks.Content;
import com.findaway.audioengine.sample.audiobooks.RecyclerViewClickListener;
import com.findaway.audioengine.sample.login.LoginActivity;

import java.util.ArrayList;

/**
 * Created by agofman on 2/5/16.
 */
public class BookChapterFragment extends Fragment implements BookView, DownloadListener, RecyclerViewClickListener {

    private BookPresenter mBookPresenter;
    private DownloadEngine mDownloadEngine;
    private ChapterContentAdapter mChapterContentAdapter;

    public BookChapterFragment() {
        mBookPresenter = new BookPresenterImpl(this);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Log.d(getTag(), "test");
        //mDownloadEngine.download();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String contentId = getArguments().getString(BookActivity.EXTRA_CONTENT_ID);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionId = sharedPreferences.getString(LoginActivity.AUDIO_ENGINE_SESSION_KEY, null);
        if (sessionId != null) {
            AudioEngine.init(getActivity(), sessionId, LogLevel.WARNING);
            try {
                mDownloadEngine = AudioEngine.getDownloadEngine();
                mDownloadEngine.registerDownloadListener(this);
            } catch (AudioEngineException e) {
                Log.e(getTag(), "Download engine error.");
            }
            mBookPresenter.getContent(sessionId, contentId);
        }
        else {
            Log.e(getTag(), "Session Id was null. Not getting book chapter list.");
        }
    }

    @Override
    public void setContent(Content content) {
        mChapterContentAdapter.setChapters(content.chapters);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(DownloadEvent downloadEvent) {

    }

    @Override
    public void update(DownloadProgressEvent downloadProgressEvent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void error(DownloadError downloadError) {
        if(downloadError.code.equals(DownloadError.UNAUTHORIZED)) {

            Toast.makeText(getActivity(), "Permission denied. Please check username and password.", Toast.LENGTH_SHORT).show();

        } else if(downloadError.code.equals(DownloadError.FORBIDDEN)) {

            Toast.makeText(getActivity(), "You do not have access to this book.", Toast.LENGTH_SHORT).show();

        } else if(downloadError.code.equals(DownloadError.CONTENT_NOT_FOUND)) {

            Toast.makeText(getActivity(), "Unable to find requested book.", Toast.LENGTH_SHORT).show();

        } else if(downloadError.code.equals(DownloadError.CHAPTER_NOT_FOUND)) {

            Toast.makeText(getActivity(), "Invalid chapter requested.", Toast.LENGTH_SHORT).show();

        } else if(downloadError.code.equals(DownloadError.NETWORK_ERROR)) {

            Toast.makeText(getActivity(), "A network error has occurred. Please check you connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        LinearLayoutManager chapterLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView chapterListView = (RecyclerView)view.findViewById(R.id.chapter_list);
        chapterListView.setLayoutManager(chapterLayoutManager);

        mChapterContentAdapter = new ChapterContentAdapter(new ArrayList<Chapter>(), this);
        chapterListView.setAdapter(mChapterContentAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("Download All");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
