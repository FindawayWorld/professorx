package com.findaway.audioengine.sample.book;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.findaway.audioengine.config.LogLevel;
import com.findaway.audioengine.mobile.AudioEngine;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.login.LoginActivity;

/**
 * Created by agofman on 2/8/16.
 */
public class BookActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    public static final String EXTRA_CONTENT_ID = "EXTRA_CONTENT_ID";
    private String mContentId;
    private ViewPager mViewPager;
    private BookPagerAdapter mCustomPagerAdapter;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mToolbar.setTitle(mCustomPagerAdapter.getPageTitle(position).toString());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mToolbar = (Toolbar)findViewById(R.id.book_toolbar);
        setSupportActionBar(mToolbar);

        mContentId = getIntent().getStringExtra(EXTRA_CONTENT_ID);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sessionId = mSharedPreferences.getString(LoginActivity.AUDIO_ENGINE_SESSION_KEY, null);

        mViewPager = (ViewPager)findViewById(R.id.book_pager);
        mViewPager.addOnPageChangeListener(this);

        mCustomPagerAdapter = new BookPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    class BookPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        public BookPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;
            if (position == 0) {
                fragment = new BookDetailsFragment();
            } else {
                fragment = new BookChapterFragment();
            }
            Bundle args = new Bundle();
            args.putString(EXTRA_CONTENT_ID, mContentId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Details";
            } else {
                return "Chapters";
            }
        }
    }
}
