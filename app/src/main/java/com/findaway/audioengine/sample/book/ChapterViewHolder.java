package com.findaway.audioengine.sample.book;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.RecyclerViewClickListener;

/**
 * Created by agofman on 2/9/16.
 */
public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView chapter_number;
    TextView part_number;
    TextView duration;
    TextView download_status;

    RecyclerViewClickListener mRecyclerViewClickListener;

    @Override
    public void onClick(View v) {
        mRecyclerViewClickListener.recyclerViewListClicked(v, this.getLayoutPosition());
    }

    public ChapterViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        mRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);

        chapter_number = (TextView)itemView.findViewById(R.id.chapter_number);
        part_number = (TextView)itemView.findViewById(R.id.part_number);
        duration = (TextView)itemView.findViewById(R.id.duration);
        download_status = (TextView)itemView.findViewById(R.id.download_status);
    }
}
