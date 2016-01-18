package com.findaway.audioengine.model;

import com.google.gson.annotations.SerializedName;

public class Chapter {

    //Chapter Attributes
    static final String PART_NUMBER_ATTR = "part_number";
    static final String CHAPTER_NUMBER_ATTR = "chapter_number";
    static final String DURATION_ATTR = "duration";

    public String contentId;
    public Long duration;
    public Integer part_number;
    public Integer chapter_number;

    public Chapter() {
        part_number = 0;
        chapter_number = 0;
        duration = (long) 0;
    }

    public Chapter(String id, Integer part, Integer chapter) {
        contentId = id;
        part_number = part != null ? part : 0;
        chapter_number = chapter != null ? chapter : 0;
        duration = (long) 0;
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