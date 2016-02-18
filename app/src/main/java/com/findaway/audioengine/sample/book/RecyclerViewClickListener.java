package com.findaway.audioengine.sample.book;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by agofman on 2/5/16.
 */
public interface RecyclerViewClickListener {
    public void recyclerViewListClicked(View v, int position);

    public void recyclerViewListLongClicked(View v, int position);
}
