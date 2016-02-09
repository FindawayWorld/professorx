package com.findaway.audioengine.sample.book;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.findaway.audioengine.sample.R;

/**
 * Created by agofman on 2/8/16.
 */
public class BookActivity extends AppCompatActivity {
    public static final String EXTRA_CONTENT_ID = "EXTRA_CONTENT_ID";
    String mContentId;
    ViewPager mViewPager;
    BookPagerAdapter mCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mContentId = getIntent().getStringExtra(EXTRA_CONTENT_ID);
        mViewPager = (ViewPager)findViewById(R.id.book_pager);
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
