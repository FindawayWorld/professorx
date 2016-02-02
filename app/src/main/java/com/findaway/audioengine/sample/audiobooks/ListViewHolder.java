package com.findaway.audioengine.sample.audiobooks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.findaway.audioengine.sample.MainActivity;


/**
 * Created by agofman on 2/2/16.
 */
public class ListViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ListViewHolder.class.getSimpleName();

    MainActivity activity;

    TextView title;
    String contentId;

    public ListViewHolder(View itemView) {

        super(itemView);
    }

}