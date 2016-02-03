package com.findaway.audioengine.sample.audiobooks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.findaway.audioengine.sample.R;

/**
 * Created by agofman on 2/2/16.
 */
public class ListViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView author;

    public ListViewHolder(View itemView) {
        super(itemView);

        View view = itemView;
        title = (TextView)view.findViewById(R.id.title);
        author = (TextView)view.findViewById(R.id.author);

    }

}