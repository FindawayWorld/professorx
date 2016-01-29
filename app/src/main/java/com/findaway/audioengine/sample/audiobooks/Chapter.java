package com.findaway.audioengine.sample.audiobooks;

/**
 * Created by agofman on 1/27/16.
 */
public class Chapter {
    public Long duration; //chapter duration in seconds
    public Integer part_number; //the part number, 0 if audiobook not divided into parts
    public Integer chapter_number; //The chapter number, 0 if introduction
}