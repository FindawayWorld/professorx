package com.findaway.audioengine.sample.audiobooks;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agofman on 1/29/16.
 */
public class LibraryFragment extends Fragment implements AudiobookView {

    private AudiobookPresenter mAudiobookPresenter;
    private SharedPreferences mSharedPreferences;
    private ContentAdapter mContentAdapter;
    private GridLayoutManager mContentLayoutManager;
    RecyclerView mContentListView;

    public LibraryFragment() {
        mAudiobookPresenter = new AudiobookPresenterImpl(this);
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
        String accountId = mSharedPreferences.getString(LoginActivity.AUDIO_ENGINE_ACCOUNT_IDS, null);
        String sessionId = mSharedPreferences.getString(LoginActivity.AUDIO_ENGINE_SESSION_KEY, null);
        mAudiobookPresenter.getAudiobook(sessionId, accountId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);

        mContentLayoutManager = new GridLayoutManager(getActivity(), 2);
        mContentListView = (RecyclerView)view.findViewById(R.id.gridview);
        mContentListView.setLayoutManager(mContentLayoutManager);

        mContentAdapter = new ContentAdapter(getActivity(), new ArrayList<Content>());
        mContentListView.setAdapter(mContentAdapter);

        return view;
    }

}
