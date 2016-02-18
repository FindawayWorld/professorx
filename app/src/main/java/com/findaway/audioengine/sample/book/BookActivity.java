package com.findaway.audioengine.sample.book;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.findaway.audioengine.sample.R;

/**
 * Created by agofman on 2/8/16.
 */
public class BookActivity extends AppCompatActivity {
    public static final String EXTRA_CONTENT_ID = "EXTRA_CONTENT_ID";
    public static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";
    public static final String EXTRA_ACCOUNT_ID = "EXTRA_ACCOUNT_ID";
    private String mContentId, mSessionId, mAccountId;
    private BookPagerAdapter mCustomPagerAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mToolbar = (Toolbar)findViewById(R.id.book_toolbar);
        setSupportActionBar(mToolbar);

        mContentId = getIntent().getStringExtra(EXTRA_CONTENT_ID);
        mSessionId = getIntent().getStringExtra(EXTRA_SESSION_ID);
        mAccountId = getIntent().getStringExtra(EXTRA_ACCOUNT_ID);

        ViewPager viewPager = (ViewPager)findViewById(R.id.book_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });

        mCustomPagerAdapter = new BookPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mCustomPagerAdapter);
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
            args.putString(EXTRA_SESSION_ID, mSessionId);
            args.putString(EXTRA_ACCOUNT_ID, mAccountId);
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
