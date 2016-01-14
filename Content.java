package com.findaway.audioengine.model;

import com.google.gson.annotations.SerializedName;

import com.findaway.audioengine.exceptions.ChapterNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Content {

    //Content Attributes
    public static final String ID_ATTR = "id";
    public static final String CONTENT_ID_ATTR = "contentId";
    public static final String TITLE_ATTR = "title";
    public static final String SUBTITLE_ATTR = "sub_title";
    public static final String CHAPTERS_ATTR = "chapters";
    public static final String DESCRIPTION_ATTR = "description";
    public static final String ABRIDGEMENT_ATTR = "abridgement";
    public static final String AUTHOR_ATTR = "author";
    public static final String COVER_URL_ATTR = "cover_url";
    public static final String SAMPLE_URL_ATTR = "sample_url";
    public static final String SIZE_ATTR = "actual_size";
    public static final String PUBLISHER_ATTR = "publisher";
    public static final String RUNTIME_ATTR = "runtime";
    public static final String NARRATOR_ATTR = "narrator";
    public static final String GENRE_ATTR = "genre";
    public static final String SECONDARY_GENRE_ATTR = "genre_list";
    public static final String COPYRIGHT_ATTR = "copyright";
    public static final String SERIES_ATTR = "series";
    public static final String GRADE_LEVEL_ATTR = "grade_level";
    public static final String STREET_DATE_ATTR = "street_date";
    public static final String LANGUAGE_ATTR = "language";
    public static final String AWARDS_ATTR = "awards";
    public static final String TIMES_BEST_SELLER_ATTR = "times_bestseller_date";
    public static final String COMMON_CORE_ATTR = "common_core";
    public static final String CHAPTERIZED_ATTR = "chapterized";
    public static final String TITLE_ACQUISITION_STASTUS_ATTR = "title_acquisition_status";
    public static final String BISAC1_ATTR = "BISAC1";
    public static final String BISAC2_ATTR = "BISAC2";
    public static final String BISAC3_ATTR = "BISAC3";
    public static final String METADATA_SIG_ATTR = "metadata_sig";

    @SerializedName(ID_ATTR)
    public String contentId;
    public String title;
    @SerializedName(SUBTITLE_ATTR)
    public String subTitle;
    @SerializedName(BISAC1_ATTR)
    public String bisac1;
    @SerializedName(BISAC2_ATTR)
    public String bisac2;
    @SerializedName(BISAC3_ATTR)
    public String bisac3;
    public List<Chapter> chapters;
    public String description;
    public String abridgement;
    public List<String> author;
    @SerializedName(COVER_URL_ATTR)
    public String coverUrl;
    @SerializedName(SAMPLE_URL_ATTR)
    public String sampleUrl;
    @SerializedName(SIZE_ATTR)
    public Long size;
    public List<String> awards;
    public String publisher;
    public String runtime;
    public List<String> narrator;
    public String genre;
    @SerializedName(SECONDARY_GENRE_ATTR)
    public List<String> secondaryGenre;
    public String copyright;
    public List<String> series;
    @SerializedName(GRADE_LEVEL_ATTR)
    public String gradeLevel;
    @SerializedName(STREET_DATE_ATTR)
    public Date streetDate;
    public String language;
    @SerializedName(TIMES_BEST_SELLER_ATTR)
    public Date timesBestSellerDate;
    @SerializedName(COMMON_CORE_ATTR)
    public boolean commonCore;
    public boolean chapterized;
    @SerializedName(TITLE_ACQUISITION_STASTUS_ATTR)
    public String titleAcquisitionStastus;
    @SerializedName(METADATA_SIG_ATTR)
    public String metadataSignature;

    public Content() {
        author = new ArrayList<String>();
        narrator = new ArrayList<String>();
        chapters = new ArrayList<Chapter>();
        secondaryGenre = new ArrayList<String>();
        series = new ArrayList<String>();
        awards = new ArrayList<String>();
        size = (long) 0;
        chapterized = false;
        commonCore = false;
    }

    public Content(String cId) {
        contentId = cId;
        author = new ArrayList<String>();
        narrator = new ArrayList<String>();
        chapters = new ArrayList<Chapter>();
        secondaryGenre = new ArrayList<String>();
        series = new ArrayList<String>();
        awards = new ArrayList<String>();
        size = (long) 0;
        chapterized = false;
        commonCore = false;
    }

    public String toString() {
        return title;
    }

    public Chapter getChapter(Integer partNumber, Integer chapterNumber) throws ChapterNotFoundException {

        if (partNumber == null && chapterNumber == null) {

            return getFirstChapter();

        } else if (partNumber != null && chapterNumber == null) {

            for (Chapter chapter : chapters) {

                if (chapter.partNumber.equals(partNumber)) {

                    chapter.contentId = contentId;
                    return chapter;
                }
            }

        } else if (partNumber == null) {

            for (Chapter chapter : chapters) {

                if (chapter.chapterNumber.equals(chapterNumber)) {

                    chapter.contentId = contentId;
                    return chapter;
                }
            }

        } else {

            for (Chapter chapter : chapters) {

                if (chapter.partNumber.equals(partNumber) && chapter.chapterNumber.equals(chapterNumber)) {

                    chapter.contentId = contentId;
                    return chapter;
                }
            }
        }

        throw new ChapterNotFoundException("No chapter found for Part " + partNumber + ", Chapter " + chapterNumber + " in book " + contentId);
    }

    public Chapter getFirstChapter() {

        Chapter chapter = chapters.get(0);
        chapter.contentId = contentId;
        return chapter;
    }

    public Chapter getNextChapter(Integer partNumber, Integer chapterNumber) throws ChapterNotFoundException {

        Chapter nextChapter;
        Chapter[] chapterArray = chapters.toArray(new Chapter[chapters.size()]);
        int i = 0;

        if (partNumber == null && chapterNumber == null) {

            Chapter chapter = getFirstChapter();
            return getNextChapter(chapter.partNumber, chapter.chapterNumber);

        } else if (partNumber != null && chapterNumber == null) {

            for (i = 0; i < chapterArray.length; i++) {

                if (chapterArray[i].partNumber.equals(partNumber)) {

                    if (i + 1 < chapterArray.length) {
                        nextChapter = chapterArray[i + 1];
                        nextChapter.contentId = contentId;
                        return nextChapter;
                    } else {
                        throw new ChapterNotFoundException(
                                "No next chapter found for Part " + partNumber + ", Chapter " + chapterNumber + " in book " + contentId);
                    }
                }
            }

        } else if (partNumber == null) {

            for (i = 0; i < chapterArray.length; i++) {

                if (chapterArray[i].chapterNumber.equals(chapterNumber)) {

                    if (i + 1 < chapterArray.length) {
                        nextChapter = chapterArray[i + 1];
                        nextChapter.contentId = contentId;
                        return nextChapter;
                    } else {
                        throw new ChapterNotFoundException(
                                "No next chapter found for Part " + partNumber + ", Chapter " + chapterNumber + " in book " + contentId);
                    }
                }
            }

        } else {

            for (i = 0; i < chapterArray.length; i++) {

                if (chapterArray[i].partNumber.equals(partNumber) && chapterArray[i].chapterNumber.equals(chapterNumber)) {

                    if (i + 1 < chapterArray.length) {
                        nextChapter = chapterArray[i + 1];
                        nextChapter.contentId = contentId;
                        return nextChapter;
                    } else {
                        throw new ChapterNotFoundException(
                                "No next chapter found for Part " + partNumber + ", Chapter " + chapterNumber + " in book " + contentId);
                    }
                }
            }
        }

        throw new ChapterNotFoundException("No chapter found for Part " + partNumber + ", Chapter " + chapterNumber + " in book " + contentId);
    }

    public boolean hasChapters() {

        return chapters != null && chapters.size() > 0;
    }

    public boolean hasNextChapter(Chapter chapter) {

        try {

            getNextChapter(chapter.partNumber, chapter.chapterNumber);
            return true;

        } catch (ChapterNotFoundException cnfe) {

            return false;
        }
    }
}