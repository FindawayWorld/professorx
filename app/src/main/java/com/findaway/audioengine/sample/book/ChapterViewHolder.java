package com.findaway.audioengine.sample.book;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.RecyclerViewClickListener;

/**
 * Created by agofman on 2/9/16.
 */
public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    TextView chapter_number;
    TextView part_number;
    TextView duration;
    TextView download_status;
    RecyclerViewClickListener mRecyclerViewClickListener;
    ProgressBar download_progress;

    @Override
    public void onClick(View v) {
        mRecyclerViewClickListener.recyclerViewListClicked(v, this.getLayoutPosition(), download_status, download_progress);
    }

    @Override
    public boolean onLongClick(View v) {
        mRecyclerViewClickListener.recyclerViewListLongClicked(v, this.getLayoutPosition(), download_status, download_progress);
        return true;
    }

    public ChapterViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        mRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        chapter_number = (TextView)itemView.findViewById(R.id.chapter_number);
        part_number = (TextView)itemView.findViewById(R.id.part_number);
        duration = (TextView)itemView.findViewById(R.id.duration);
        download_status = (TextView)itemView.findViewById(R.id.download_status);
        download_progress = (ProgressBar)itemView.findViewById(R.id.progress_bar);
        download_progress.setMax(100);
    }
}
