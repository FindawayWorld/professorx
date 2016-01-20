#Tutorial 1 - Object Models

When dealing with an audiobook, there are two main objects that you will need to use. Those objects are the
content object and the chapter object. The content object represents an audiobook while the chapter 
object represents one chapter of an audiobook. The data that will fill these objects will come from the api.
More information about using the api can be found in the developer portal here http://developer.audioengine.io/api/v3/patterns.

###Content object
The api returns a JSON which contains data for all of the audiobooks that are available. You can store
all of the audiobook data in a list or array of content objects. 

| Endpoint | Description |
| ---- | --------------- |
| [GET /v3/audiobooks](http://developer.audioengine.io/api/v3/audiobooks) | Return data for all audiobooks |
(This will require an authorised session-key or your api-key in the HTTP header for any request)

``` Java
package com.findaway.audioengine.model;

import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
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
    public Long actual_size; //size of the audiobook in bytes
    public String publisher; //publisher of the audiobook
    public String runtime; //runtime in HH:MM:SS format
    public List<String> narrator; //a list of narrators
    public String genre; //the audiobooks primary genre
    public List<String> genre_list; //a list of other genres 
    public String copyright; //copyright year if available, formatted YYYY 
    public List<String> series; //list of series this book is a part of
    public String grade_level; //text tag such as 'Young Adult'
    public Date street_date; //release date in YYYY-MM-DD format
    public String language; //multiple languages seperated by a slash
    public List<String> awards; //list of awards
    public Date times_bestseller_date; //bestseller date in YYYY-MM-DD format
    public boolean common_core; //true or false
    public boolean chapterized; //true or false
    public String title_acquisition_status; //title status i.e. "Rights Secured"
    public String bisac1; //alphanumberic subject code
    public String bisac2; //alphanumberic subject code
    public String bisac3; //alphanumberic subject code
    public String metadata_sig; //text signature that is updated when any data changes 

}
```


###Chapter object
The api returns a JSON which contains data for all of the chapters in a specific audiobook. You can 
store all of the data returned from the JSON in a list or array of chapter objects. 

| Endpoint | Description |
| ---- | --------------- |
| [GET /v3/audiobooks/:id](http://developer.audioengine.io/api/v3/audiobooks) | Return data for all chapters |
(This will require an authorised session-key or your api-key in the HTTP header for any request)

``` Java
package com.findaway.audioengine.model;

public class Chapter {

    public Long duration; //chapter duration in seconds
    public Integer part_number; //the part number, 0 if audiobook not divided into parts
    public Integer chapter_number; //The chapter number, 0 if introduction

}
```