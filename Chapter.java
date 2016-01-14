package com.findaway.audioengine.model;

import com.google.gson.annotations.SerializedName;

public class Chapter {

    // Chapter Attributes
    static final String PART_NUMBER_ATTR = "part_number";
    static final String CHAPTER_NUMBER_ATTR = "chapter_number";
    static final String DURATION_ATTR = "duration";
    static final String PATH_ATTR = "path";
    static final String KEY_ATTR = "key";

    public String contentId;
    @SerializedName(PART_NUMBER_ATTR)
    public Integer partNumber;
    @SerializedName(CHAPTER_NUMBER_ATTR)
    public Integer chapterNumber;
    public Long duration;
    public String path;
    public String key;
    public Integer size;
    public DownloadStatus downloadStatus;

    public Chapter() {
        partNumber = 0;
        chapterNumber = 0;
        duration = (long) 0;
        size = 0;
        downloadStatus = DownloadStatus.NOT_DOWNLOADED;
    }

    public Chapter(String id, Integer part, Integer chapter) {
        contentId = id;
        partNumber = part != null ? part : 0;
        chapterNumber = chapter != null ? chapter : 0;
        duration = (long) 0;
        size = 0;
        downloadStatus = DownloadStatus.NOT_DOWNLOADED;
    }

    public String getFriendlyName() {
        String friendlyName;

        if (partNumber == 0 && chapterNumber == 0) {

            friendlyName = "Introduction";

        } else if (partNumber == 0) {

            friendlyName = "Chapter " + chapterNumber;

        } else {

            friendlyName = "Part " + partNumber + ", " + "Chapter " + chapterNumber;
        }

        return friendlyName;
    }

    @Override
    public String toString() {
        return "Content " + contentId + ", Part " + partNumber + ", Chapter " + chapterNumber;
    }
}