package com.findaway.audioengine.sample.audiobooks;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by agofman on 2/5/16.
 */
public interface RecyclerViewClickListener {
    public void recyclerViewListClicked(View v, int position, TextView download_status, ProgressBar download_progress);
}
