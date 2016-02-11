package com.findaway.audioengine.sample.audiobooks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.findaway.audioengine.sample.R;

/**
 * Created by agofman on 2/2/16.
 */
public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView title;
    TextView author;
    ImageView cover;
    TextView id;
    RecyclerViewClickListener mRecyclerViewClickListener;

    @Override
    public void onClick(View v) {
        mRecyclerViewClickListener.recyclerViewListClicked(v, this.getLayoutPosition(), null, null );
    }

    public ListViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);

        mRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);
        title = (TextView)itemView.findViewById(R.id.title);
        author = (TextView)itemView.findViewById(R.id.author);
        cover = (ImageView)itemView.findViewById(R.id.cover);
        id = (TextView)itemView.findViewById(R.id.id);
    }
}