package com.findaway.audioengine.sample.audiobooks;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.findaway.audioengine.sample.book.BookActivity;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agofman on 1/29/16.
 */
public class LibraryFragment extends Fragment implements AudiobookView, RecyclerViewClickListener{

    private static final String TAG = "LibraryFragment";

    private AudiobookPresenter mAudiobookPresenter;
    private SharedPreferences mSharedPreferences;
    private ContentAdapter mContentAdapter;
    private GridLayoutManager mContentLayoutManager;
    private RecyclerView mContentListView;
    private String mSessionId;
    private String mAccountId;
    private String mContentId;

    public LibraryFragment() {
        mAudiobookPresenter = new AudiobookPresenterImpl(this);
    }

    @Override
    public void recyclerViewListClicked(View v, int position, TextView textView, ProgressBar progressBar) {
        mContentId = mContentAdapter.getContentId(position);
        Intent detailsIntent = new Intent(getActivity(), BookActivity.class);
        detailsIntent.putExtra(BookActivity.EXTRA_CONTENT_ID, mContentId);
        detailsIntent.putExtra(BookActivity.EXTRA_SESSION_ID, mSessionId);
        detailsIntent.putExtra(BookActivity.EXTRA_ACCOUNT_ID, mAccountId);
        startActivity(detailsIntent);
    }

    @Override
    public void setAudiobookList(List<Content> audiobookList) {
        for (Content content: audiobookList)
        {
            mContentAdapter.add(content);
        }
    }

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSessionId = mSharedPreferences.getString(LoginActivity.AUDIO_ENGINE_SESSION_KEY, null);
        mAccountId = mSharedPreferences.getString(LoginActivity.AUDIO_ENGINE_ACCOUNT_IDS, null);
        mAudiobookPresenter.getAudiobook(mSessionId, mAccountId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        mContentLayoutManager = new GridLayoutManager(getActivity(), 2);
        mContentListView = (RecyclerView)view.findViewById(R.id.contentgridview);
        mContentListView.setLayoutManager(mContentLayoutManager);

        mContentAdapter = new ContentAdapter(getActivity(), new ArrayList<Content>(), this);
        mContentListView.setAdapter(mContentAdapter);
        return view;
    }

}
