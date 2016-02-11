package com.findaway.audioengine.sample.book;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findaway.audioengine.DownloadListener;
import com.findaway.audioengine.exceptions.ChapterNotFoundException;
import com.findaway.audioengine.mobile.DownloadEngine;
import com.findaway.audioengine.model.DownloadStatus;
import com.findaway.audioengine.sample.R;
import com.findaway.audioengine.sample.audiobooks.Chapter;
import com.findaway.audioengine.sample.audiobooks.Content;
import com.findaway.audioengine.sample.audiobooks.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by agofman on 2/9/16.
 */
public class ChapterContentAdapter extends RecyclerView.Adapter<ChapterViewHolder> {

    private String TAG = "CHAPTER_CONTENT_ADAPTER";
    private List<Chapter> mChapters;
    private static RecyclerViewClickListener mRecyclerViewClickListener;
    private DownloadEngine mDownloadEngine;
    private DownloadListener mDownloadListener;
    private Content mContent;

    public ChapterContentAdapter(DownloadListener downloadListener,List<Chapter> chapters, RecyclerViewClickListener recyclerViewClickListener, DownloadEngine downloadEngine) {
        mChapters = chapters;
        mRecyclerViewClickListener = recyclerViewClickListener;
        mDownloadEngine = downloadEngine;
        mDownloadListener = downloadListener;
    }
    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chapter_list_item, parent, false);
        return new ChapterViewHolder(view, mRecyclerViewClickListener);
    }

    public Chapter getItem(int position) {
        return mChapters.get(position);
    }

    @Override
    public int getItemCount() {
        return mChapters.size();
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        Chapter chapter = mChapters.get(position);
        Content content = mContent;
        holder.chapter_number.setText("Chapter Number " + chapter.chapter_number.toString());
        holder.duration.setText("Duration is : " + chapter.duration.toString());
        holder.part_number.setText("Part Number : " + chapter.part_number.toString());

        try {
            DownloadStatus status = mDownloadEngine.getStatus(content.id, chapter.part_number, chapter.chapter_number);
            if (status != DownloadStatus.DOWNLOADING) {
                holder.download_status.setText(status.name());
                if (status == DownloadStatus.DOWNLOADED) {
                    holder.download_progress.setProgress(100);
                } else {
                    holder.download_progress.setProgress(0);
                }
            }
        } catch (ChapterNotFoundException e) {
            DownloadStatus status = DownloadStatus.NOT_DOWNLOADED;
            holder.download_status.setText(status.toString());
            holder.download_progress.setProgress(0);
            e.printStackTrace();
        }

        mDownloadEngine.registerDownloadListener(mDownloadListener);
    }

    public void setContent(Content content)
    {
        mContent = content;
        mChapters = content.chapters;
        this.notifyDataSetChanged();
    }
}
