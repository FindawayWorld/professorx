package com.findaway.audioengine.sample.audiobooks;

/**
 * Created by agofman on 1/27/16.
 */
import java.lang.String;
import java.util.List;

public class Content {
    public String id;  //this is used as the identifier for the audiobook
    public String title; //the title of the audiobook
    public String sub_title; //the subtitle of the audiobook which is optional
    public List<Chapter> chapters; //list of chapter data(seperate api call)
    public String description; //a breif paragraph about the audiobook
    public String abridgement; //"Abridged" or "Unabridged"
    public List<String> author; //a list of authors
    public String cover_url; //the url where you can find the audiobook cover image
    public String sample_url; //the url where you can find the sample audio
    public String actual_size; //size of the audiobook in bytes
    public String publisher; //publisher of the audiobook
    public String runtime; //runtime in HH:MM:SS format
    public List<String> narrator; //a list of narrators
    public String genre; //the audiobooks primary genre
    public List<String> genre_list; //a list of other genres
    public String copyright; //copyright year if available, formatted YYYY
    public List<String> series; //list of series this book is a part of
    public String grade_level; //text tag such as 'Young Adult'
    public String street_date; //release date in YYYY-MM-DD format
    public String language; //multiple languages seperated by a slash
    public List<String> awards; //list of awards
    public String times_bestseller_date; //bestseller date in YYYY-MM-DD format
    public boolean common_core; //true or false
    public boolean chapterized; //true or false
    public String title_acquisition_status; //title status i.e. "Rights Secured"
    public String bisac1; //alphanumberic subject code
    public String bisac2; //alphanumberic subject code
    public String bisac3; //alphanumberic subject code
    public String metadata_sig; //text signature that is updated when any data changes
}