package com.findaway.audioengine.sample.book;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.Chapter;
import com.findaway.audioengine.sample.audiobooks.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by agofman on 2/9/16.
 */
public class ChapterContentAdapter extends RecyclerView.Adapter<ChapterViewHolder> {

    private List<Chapter> mChapters;
    private static RecyclerViewClickListener mRecyclerViewClickListener;

    public ChapterContentAdapter(List<Chapter> chapters, RecyclerViewClickListener recyclerViewClickListener) {
        mChapters = chapters;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }
    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chapter_list_item, parent, false);
        return new ChapterViewHolder(view, mRecyclerViewClickListener);
    }

    @Override
    public int getItemCount() {
        return mChapters.size();
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        holder.chapter_number.setText("Chapter Number " + mChapters.get(position).chapter_number.toString());
        holder.duration.setText("Duration is : " + mChapters.get(position).duration.toString());
        holder.part_number.setText("Part Number : " + mChapters.get(position).part_number);
    }

    public void setChapters(List<Chapter> chapters)
    {
        mChapters = chapters;
    }
}
