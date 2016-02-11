package com.findaway.audioengine.sample.book;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.ArrayList;

/**
 * Created by agofman on 2/5/16.
 */
public class BookChapterFragment extends Fragment implements BookView, DownloadListener, RecyclerViewClickListener {

    private BookPresenter mBookPresenter;
    private DownloadEngine mDownloadEngine;
    private ChapterContentAdapter mChapterContentAdapter;
    private String mContentId, mAccountId, mSessionId;
    private ProgressBar mDownloadProgress;
    private TextView mDownloadStatus;
    private Chapter mChapter;
    private Context mContext;

    public BookChapterFragment() {
        mBookPresenter = new BookPresenterImpl(this);
    }

    @Override
    public void recyclerViewListClicked(View v, int position, TextView download_status, ProgressBar download_progress) {
        mChapter =  mChapterContentAdapter.getItem(position);
        mDownloadProgress = download_progress;
        mDownloadStatus = download_status;
        try {
            mDownloadEngine.download(mContentId, mChapter.part_number, mChapter.chapter_number, null, mAccountId, false, false);
        }
        catch (AudioEngineException ex)
        {
            Log.e(getTag(), "Problem while downloading content");
        }
    }

    @Override
    public void recyclerViewListLongClicked(View v, int position, TextView download_status, ProgressBar download_progress) {
        if (download_status.getText() == "DOWNLOADED") {
            mChapter = mChapterContentAdapter.getItem(position);
            mDownloadProgress = download_progress;
            mDownloadStatus = download_status;
            mDownloadEngine.delete(mContentId, mChapter.part_number, mChapter.chapter_number);
        }
        else {
            Toast.makeText(getActivity(), "No file found to delete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        mContentId = getArguments().getString(BookActivity.EXTRA_CONTENT_ID);
        mSessionId = getArguments().getString(BookActivity.EXTRA_SESSION_ID);
        mAccountId = getArguments().getString(BookActivity.EXTRA_ACCOUNT_ID);
        if (mSessionId != null) {
            try {
                AudioEngine.init(getActivity(), mSessionId, LogLevel.WARNING);
                mDownloadEngine = AudioEngine.getDownloadEngine();
                mDownloadEngine.registerDownloadListener(this);
            } catch (AudioEngineException e) {
                Log.e(getTag(), "Download engine error.");
            }
            mBookPresenter.getContent(mSessionId, mContentId);
        }
        else {
            Log.e(getTag(), "Session Id was null. Not getting book chapter list.");
        }
    }

    @Override
    public void setContent(Content content) {
        mChapterContentAdapter.setContent(content);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(DownloadEvent downloadEvent) {
        if (downloadEvent.code == DownloadEvent.CHAPTER_DOWNLOAD_COMPLETED && downloadEvent.chapter != null) {

            if (downloadEvent.chapter.contentId.equals(mContentId) && downloadEvent.chapter.partNumber == mChapter.part_number
                    && downloadEvent.chapter.chapterNumber == mChapter.chapter_number) {

                Log.d(getTag(), "Download complete for " + downloadEvent.chapter + ". This is " + mContentId + ", " + mChapter.part_number + ", " + mChapter.chapter_number);
                ((BookActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mDownloadStatus.setText("DOWNLOADED");
                        mDownloadProgress.setProgress(100);
                    }
                });
            }

        } else if (downloadEvent.code == DownloadEvent.DELETE_COMPLETE && downloadEvent.chapter != null) {

            if (downloadEvent.chapter.contentId.equals(mContentId) && downloadEvent.chapter.partNumber == mChapter.part_number
                    && downloadEvent.chapter.chapterNumber == mChapter.chapter_number) {

                Log.d(getTag(), "Delete complete for " + downloadEvent.chapter + ". This is " + mContentId + ", " + mChapter.part_number + ", " + mChapter.chapter_number);
                ((BookActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mDownloadStatus.setText("NOT_DOWNLOADED");
                        mDownloadProgress.setProgress(0);
                    }
                });
            }
        }
    }

    @Override
    public void update(final DownloadProgressEvent downloadProgressEvent) {
        if (downloadProgressEvent.chapter != null && downloadProgressEvent.chapter.contentId.equals(mContentId) && downloadProgressEvent.chapter.partNumber == mChapter.part_number
                && downloadProgressEvent.chapter.chapterNumber == mChapter.chapter_number) {

            ((BookActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadStatus.setText(downloadProgressEvent.chapterPercentage + " %");
                    mDownloadProgress.setProgress(downloadProgressEvent.chapterPercentage);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "Download All" ) {
            try {
                mDownloadEngine.download(mContentId, 0, 0, null, mAccountId, true, false);
            }
            catch (AudioEngineException ex)
            {
                Log.e(getTag(), "Problem while downloading content");
            }
        } else if (item.getTitle() == "Delete All") {
            mDownloadEngine.delete(mContentId);
        }
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

        mChapterContentAdapter = new ChapterContentAdapter(this, new ArrayList<Chapter>(), this, mDownloadEngine);
        chapterListView.setAdapter(mChapterContentAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("Download All");
        menu.add("Delete All");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
