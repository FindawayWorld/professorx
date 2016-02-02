package com.findaway.audioengine.sample.audiobooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findaway.audioengine.sample.R;

import java.util.List;

/**
 * Created by agofman on 2/2/16.
 */
public class ContentAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private static final String TAG = "ContentAdapter";

    private Context mContext;
    private List<Content> mContent;

    public ContentAdapter(Context context, List<Content> content) {

        mContext = context;
        mContent = content;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        Log.d(TAG, "Creating view holder.");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, final int position) {

        Log.d(TAG, "Binding view holder!");

        final Content content = mContent.get(position);
        viewHolder.title.setText(content.toString());
        viewHolder.contentId = content.id;
    }

    @Override
    public int getItemCount() {

        return mContent.size();
    }

    public void add(Content content) {

        mContent.add(content);
        //notifyItemInserted(mContent.size() - 1);
    }

    public void remove(int position) {

        mContent.remove(position);
        //notifyItemRemoved(position);
    }
}
