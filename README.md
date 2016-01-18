#Tutorial 1 - Object Models

##Content Object

The content object represents any piece of content that a user has access to through Audio Engine.
Your audio library will be a collection of these content objects. The content object contains all of the
audio book information needed for that specific piece of content as well as methods to access the
chapter objects it contains. The content object can be created with a content id or without a value.

###Methods

getChapter(Integer partNumber, Integer chapterNumber) - Returns a specific chapter object.

getFirstChapter() - Returns the first chapter object of the content.

getNextChapter(Integer partNumber, Integer chapterNumber) - Returns the next chapter object.

hasChapters() - Returns the number of chapter objects for this content.

hasNextChapter(Chapter chapter) - Returns weather or not there is a next chapter object.

###Properties
id                          - (string) id of audiobook (5 digits)
title                       - (string) title of the audiobook
sub_title                   - (string) optional subtitle
chapters                    - (array) a list of chapter data (see below)
description                 - (string) paragraph audiobook summary
abridgement                 - (string) "Abridged" or "Unabridged"
author                      - (array/strings) list of authors
cover_url                   - (string) url of the audiobook cover image
sample_url                  - (string) url of an audio sample for the audiobook
actual_size                 - (Long) size of audiobook in bytes
publisher                   - (string) publisher of audiobook
runtime                     - (string) runtime in HH:MM:SS format
narrator                    - (array/strings) a list of narrators
genre                       - (string) a text tag for the book's primary genre
genre_list                  - (array/strings) a list of other genre tags
copyright                   - (string) copyright year if available YYYY format
series                      - (array/strings) list of series this book is a part of
grade_level                 - (string) text tag such as 'Young Adult'
street_date                 - (string) YYYY-MM-DD format
language                    - (string) Multiple languages separated by slash / "
awards                      - (array/strings) list of awards
times_bestseller_date"      - (string) YYYY-MM-DD format
common_core                 - (true or false)
chapterized                 - (true or false)
title_acquisition_status"   - (string) i.e. "Rights Secured"
bisac1                      - (string) Alphanumeric subject code
bisac2                      - (string) Alphanumeric subject code (2 of 3)
bisac3                      - (string) Alphanumeric subject code (3 of 3)
metadata_sig                - (string) text signature that is updated when any of the above data changes

###How to get a content object
To get the content object a call can be made to the android api.
####GET /v3/audiobooks
    - Returns a JSON formatted document containing a list of audiobook objects as described above.
(This will require an authorised session-key or your api-key in the HTTP header for any request)

##Chapter Object

The chapter object represents a specific chapter in a piece of content and contains information
specific to that chapter. There can be multiple chapter objects contained within each content
object. Chapter object can be created using id, part, and chapter, or without a value.

###Methods

getFriendlyName() - Returns formatted part and chapter information.

###Properties
duration            - (number) in seconds
part_number         - (number) Zero if not divided into parts
chapter_number      - (number) Zero if "Introduction"

####How to get chapter object
To get a chapter object a call can be made to the android api.

####GET /v3/audiobooks/{id}
    - Returns JSON formatted document containing an audiobook object.
(This will require an authorised session-key or your api-key in the HTTP header for any request)

