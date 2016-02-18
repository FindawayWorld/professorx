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
    private View.OnClickListener mOnClickListener;

    @Override
    public void onClick(View v) {
        mOnClickListener.onClick(v);
    }

    public ListViewHolder(View itemView, View.OnClickListener onClickListener) {
        super(itemView);

        mOnClickListener = onClickListener;
        itemView.setOnClickListener(this);
        title = (TextView)itemView.findViewById(R.id.title);
        author = (TextView)itemView.findViewById(R.id.author);
        cover = (ImageView)itemView.findViewById(R.id.cover);
        id = (TextView)itemView.findViewById(R.id.id);
    }
}