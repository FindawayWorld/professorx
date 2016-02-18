package com.findaway.audioengine.sample.audiobooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    private static View.OnClickListener mOnClickListener;
    private Context mContext;
    private List<Content> mContent;

    public ContentAdapter(Context context, List<Content> content, View.OnClickListener onClickListener) {
        mContext = context;
        mContent = content;
        mOnClickListener = onClickListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.library_list_item, parent, false);
        return new ListViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, final int position) {
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
}
