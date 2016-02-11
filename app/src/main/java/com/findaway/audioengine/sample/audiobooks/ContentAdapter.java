package com.findaway.audioengine.sample.audiobooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findaway.audioengine.sample.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by agofman on 2/2/16.
 */
public class ContentAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private static final String TAG = "ContentAdapter";

    private Context mContext;
    private List<Content> mContent;
    private static RecyclerViewClickListener mRecyclerViewClickListener;

    public ContentAdapter(Context context, List<Content> content, RecyclerViewClickListener recyclerViewClickListener) {

        mContext = context;
        mContent = content;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        Log.d(TAG, "Creating view holder.");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.library_list_item, parent, false);
        return new ListViewHolder(view, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, final int position) {

        Log.d(TAG, "Binding view holder!");

        final Content content = mContent.get(position);
        viewHolder.title.setText(content.title);
        viewHolder.author.setText(content.author.toString());
        viewHolder.id.setText(content.id);
        Picasso.with(mContext).load(mContext.getResources().getString(R.string.MR_IMAGE_COVER_BASE) + content.id + mContext.getResources()
                .getString(R.string.MR_IMAGE_CONTENT_LIST_PARAMS)).into(viewHolder.cover);
    }

    @Override
    public int getItemCount() {

        return mContent.size();
    }

    public void add(Content content) {

        mContent.add(content);
        notifyItemInserted(mContent.size() - 1);
    }

    public String getContentId(int position){
        return mContent.get(position).id;

    }

    public void remove(int position) {

        mContent.remove(position);
        notifyItemRemoved(position);
    }
}
