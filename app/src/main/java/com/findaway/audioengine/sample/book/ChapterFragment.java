package com.findaway.audioengine.sample.book;

import android.content.Context;
import android.nfc.Tag;
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
import com.findaway.audioengine.exceptions.AudioEngineException;
import com.findaway.audioengine.mobile.AudioEngine;
import com.findaway.audioengine.mobile.DownloadEngine;
import com.findaway.audioengine.model.DownloadError;
import com.findaway.audioengine.model.DownloadEvent;
import com.findaway.audioengine.model.DownloadProgressEvent;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.Chapter;
import com.findaway.audioengine.sample.audiobooks.Content;

import java.util.ArrayList;

/**
 * Created by agofman on 2/5/16.
 */
public class ChapterFragment extends Fragment implements BookView, DownloadListener, RecyclerViewClickListener {

    static String TAG = "Chapter Fragment";
    private BookPresenter mBookPresenter;
    private DownloadEngine mDownloadEngine;
    private ChapterContentAdapter mChapterContentAdapter;
    private String mContentId, mAccountId;
    private Chapter mChapter;
    private Context mContext;
    private Content mContent;
    private RecyclerView mChapterListView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void recyclerViewListClicked(View v, int position) {
        ChapterViewHolder chapterViewHolder = (ChapterViewHolder) mChapterListView.findViewHolderForAdapterPosition(position);
        TextView textView = (TextView) chapterViewHolder.itemView.findViewById(R.id.download_status);
        if (!textView.getText().toString().equals(getString(R.string.downloaded))) {
            mChapter = mChapterContentAdapter.getChapter(position);
            try {
                mDownloadEngine.download(mContentId, mChapter.part_number, mChapter.chapter_number, null, mAccountId, false, false);
            } catch (AudioEngineException ex) {
                Log.e(getTag(), "Problem while downloading content");
            }
        } else {
            Toast.makeText(getActivity(), "File Already Downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void recyclerViewListLongClicked(View v, int position) {
        ChapterViewHolder chapterViewHolder = (ChapterViewHolder) mChapterListView.findViewHolderForAdapterPosition(position);
        TextView textView = (TextView) chapterViewHolder.itemView.findViewById(R.id.download_status);
        if (textView.getText().toString().equals(getString(R.string.downloaded))) {
            mChapter = mChapterContentAdapter.getChapter(position);
            mDownloadEngine.delete(mContentId, mChapter.part_number, mChapter.chapter_number);
        } else {
            Toast.makeText(getActivity(), "No file found to delete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookPresenter = new BookPresenterImpl(this);
        setHasOptionsMenu(true);
        mContext = getActivity();
        mContentId = getArguments().getString(BookActivity.EXTRA_CONTENT_ID);
        String sessionId = getArguments().getString(BookActivity.EXTRA_SESSION_ID);
        mAccountId = getArguments().getString(BookActivity.EXTRA_ACCOUNT_ID);
        if (sessionId != null) {
            try {
                mDownloadEngine = AudioEngine.getDownloadEngine();

            } catch (AudioEngineException e) {
                Log.e(getTag(), "Download engine error.");
            }
            mBookPresenter.getContent(sessionId, mContentId);
        } else {
            Log.e(getTag(), "Session Id was null. Not getting book chapter list.");
        }
    }

    @Override
    public void setContent(Content content) {
        mChapterContentAdapter.setContent(content);
        mContent = content;
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void update(final DownloadProgressEvent downloadProgressEvent) {
        View view = findViewByPartAndChapter(downloadProgressEvent);
        if (view != null) {
            final TextView textView = (TextView) view.findViewById(R.id.download_status);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            ((BookActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(downloadProgressEvent.chapterPercentage + " %");
                    progressBar.setProgress(downloadProgressEvent.chapterPercentage);
                }
            });
        }
    }

    @Override
    public void update(DownloadEvent downloadEvent) {
        if (downloadEvent.code.equals(DownloadEvent.CHAPTER_DOWNLOAD_COMPLETED) && downloadEvent.chapter != null) {
            View view = findViewByPartAndChapter(downloadEvent);
            if (view != null) {
                final TextView statusView = (TextView) view.findViewById(R.id.download_status);
                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                ((BookActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusView.setText(getText(R.string.downloaded));
                        progressBar.setProgress(100);
                    }
                });
            }
        } else if (downloadEvent.code.equals(DownloadEvent.DELETE_COMPLETE) && downloadEvent.chapter != null) {
            View view = findViewByPartAndChapter(downloadEvent);
            if (view != null) {
                final TextView statusView = (TextView) view.findViewById(R.id.download_status);
                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                ((BookActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusView.setText(getText(R.string.not_downloaded));
                        progressBar.setProgress(0);
                    }
                });
            }
        } else if (downloadEvent.code.equals(DownloadEvent.DOWNLOAD_STARTED) && downloadEvent.chapter != null) {
            View view = findViewByPartAndChapter(downloadEvent);
            if (view != null) {
                final TextView statusView = (TextView) view.findViewById(R.id.download_status);
                ((BookActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusView.setText(getText(R.string.downloading).toString());
                    }
                });
            }
        } else if (downloadEvent.code.equals(DownloadEvent.DELETE_COMPLETE) && downloadEvent.chapter == null) {
            int startPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
            int endPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            for (int x = startPosition; x <= endPosition; x++) {
                View view = mChapterListView.findViewHolderForAdapterPosition(x).itemView.findViewById(R.id.chapter_list_view);
                if (view != null) {
                    final TextView statusView = (TextView) view.findViewById(R.id.download_status);
                    final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                    ((BookActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusView.setText(getText(R.string.not_downloaded));
                            progressBar.setProgress(0);
                        }
                    });
                }
            }
        }
    }

    public View findViewByPartAndChapter(DownloadEvent downloadEvent) {
        int startPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        int endPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        View view = null;
        Integer chapterNumber, partNumber, deChapterNumber, dePartNumber;
        String deContentId;
        for (int x = startPosition; x <= endPosition; x++) {
            try {
                view = mChapterListView.findViewHolderForAdapterPosition(x).itemView.findViewById(R.id.chapter_list_view);
            }
            catch (NullPointerException ex) {
                Log.e(TAG, "Couldn't find view.");
            }
            if (view != null) {
                TextView chapterNumberView = (TextView) view.findViewById(R.id.chapter_number);
                TextView partNumberView = (TextView) view.findViewById(R.id.part_number);
                deChapterNumber = downloadEvent.chapter.chapterNumber;
                dePartNumber = downloadEvent.chapter.partNumber;
                deContentId = downloadEvent.chapter.contentId;
                chapterNumber = Integer.parseInt(chapterNumberView.getText().subSequence(15, chapterNumberView.getText().length()).toString());
                partNumber = Integer.parseInt(partNumberView.getText().subSequence(14, partNumberView.getText().length()).toString());
                if (partNumber.equals(dePartNumber) && chapterNumber.equals(deChapterNumber) && deContentId.equals(mContentId)) {
                    return view;
                }
            }
        }
        return null;
    }

    public View findViewByPartAndChapter(DownloadProgressEvent downloadProgressEvent) {
        int startPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        int endPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        View view = null;
        Integer chapterNumber, partNumber, deChapterNumber, dePartNumber;
        String deContentId;
        for (int x = startPosition; x <= endPosition; x++) {
            try {
                view = mChapterListView.findViewHolderForAdapterPosition(x).itemView.findViewById(R.id.chapter_list_view);
            }
            catch (NullPointerException ex) {
                Log.e(TAG, "Couldn't find view.");
            }
            if (view != null) {
                TextView chapterNumberView = (TextView) view.findViewById(R.id.chapter_number);
                TextView partNumberView = (TextView) view.findViewById(R.id.part_number);
                deChapterNumber = downloadProgressEvent.chapter.chapterNumber;
                dePartNumber = downloadProgressEvent.chapter.partNumber;
                deContentId = downloadProgressEvent.chapter.contentId;
                chapterNumber = Integer.parseInt(chapterNumberView.getText().subSequence(15, chapterNumberView.getText().length()).toString());
                partNumber = Integer.parseInt(partNumberView.getText().subSequence(14, partNumberView.getText().length()).toString());
                if (partNumber.equals(dePartNumber) && chapterNumber.equals(deChapterNumber) && deContentId.equals(mContentId)) {
                    return view;
                }
            }
        }
        return null;
    }


    @Override
    public void onPause() {
        super.onPause();
        mDownloadEngine.unregisterDownloadListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDownloadEngine.registerDownloadListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "Download All") {
            try {
                mDownloadEngine.download(mContentId, mContent.chapters.get(0).part_number, mContent.chapters.get(0).chapter_number, null, mAccountId, true, true);
            } catch (AudioEngineException ex) {
                Log.e(getTag(), "Problem while downloading content");
            }
        } else if (item.getTitle() == "Delete All") {
            mDownloadEngine.delete(mContentId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void error(DownloadError downloadError) {
        if (downloadError.code.equals(DownloadError.UNAUTHORIZED)) {

            Toast.makeText(getActivity(), "Permission denied. Please check username and password.", Toast.LENGTH_SHORT).show();

        } else if (downloadError.code.equals(DownloadError.FORBIDDEN)) {

            Toast.makeText(getActivity(), "You do not have access to this book.", Toast.LENGTH_SHORT).show();

        } else if (downloadError.code.equals(DownloadError.CONTENT_NOT_FOUND)) {

            Toast.makeText(getActivity(), "Unable to find requested book.", Toast.LENGTH_SHORT).show();

        } else if (downloadError.code.equals(DownloadError.CHAPTER_NOT_FOUND)) {

            Toast.makeText(getActivity(), "Invalid chapter requested.", Toast.LENGTH_SHORT).show();

        } else if (downloadError.code.equals(DownloadError.NETWORK_ERROR)) {

            Toast.makeText(getActivity(), "A network error has occurred. Please check you connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mChapterListView = (RecyclerView) view.findViewById(R.id.chapter_list);
        mChapterListView.setLayoutManager(mLinearLayoutManager);

        mChapterContentAdapter = new ChapterContentAdapter(new ArrayList<Chapter>(), this, mDownloadEngine);
        mChapterListView.setAdapter(mChapterContentAdapter);
        mDownloadEngine.registerDownloadListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDownloadEngine.unregisterDownloadListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("Download All");
        menu.add("Delete All");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
