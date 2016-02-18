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
import android.widget.TextView;

import com.findaway.audioengine.sample.book.BookActivity;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agofman on 1/29/16.
 */
public class LibraryFragment extends Fragment implements AudiobookView, View.OnClickListener {

    private AudiobookPresenter mAudiobookPresenter;
    private ContentAdapter mContentAdapter;
    private String mSessionId;
    private String mAccountId;

    public LibraryFragment() {
        mAudiobookPresenter = new AudiobookPresenterImpl(this);
    }

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        TextView contentIdView = (TextView) v.findViewById(R.id.id);
        String contentId = contentIdView.getText().toString();
        Intent detailsIntent = new Intent(getActivity(), BookActivity.class);
        detailsIntent.putExtra(BookActivity.EXTRA_CONTENT_ID, contentId);
        detailsIntent.putExtra(BookActivity.EXTRA_SESSION_ID, mSessionId);
        detailsIntent.putExtra(BookActivity.EXTRA_ACCOUNT_ID, mAccountId);
        startActivity(detailsIntent);
    }

    @Override
    public void setAudiobookList(List<Content> audiobookList) {
        for (Content content : audiobookList) {
            mContentAdapter.add(content);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSessionId = sharedPreferences.getString(LoginActivity.AUDIO_ENGINE_SESSION_KEY, null);
        mAccountId = sharedPreferences.getString(LoginActivity.AUDIO_ENGINE_ACCOUNT_IDS, null);
        mAudiobookPresenter.getAudiobook(mSessionId, mAccountId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.contentgridview);
        recyclerView.setLayoutManager(gridLayoutManager);

        mContentAdapter = new ContentAdapter(getActivity(), new ArrayList<Content>(), this);
        recyclerView.setAdapter(mContentAdapter);
        return view;
    }
}
