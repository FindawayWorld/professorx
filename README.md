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

    public String id;  //this will be used as the identifier for the audiobook
    public String title;
    public String sub_title;
    public List<Chapter> chapters;
    public String description;
    public String abridgement;
    public List<String> author;
    public String cover_url;
    public String sample_url;
    public Long actual_size;
    public String publisher;
    public String runtime;
    public List<String> narrator;
    public String genre;
    public List<String> genre_list;
    public String copyright;
    public List<String> series;
    public String grade_level;
    public Date street_date;
    public String language;
    public List<String> awards;
    public Date times_bestseller_date;
    public boolean common_core;
    public boolean chapterized;
    public String title_acquisition_status;
    public String bisac1;
    public String bisac2;
    public String bisac3;
    public String metadata_sig;

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

    public Long duration;
    public Integer part_number;
    public Integer chapter_number;

}
```