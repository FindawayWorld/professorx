# Professor X

Professor X is a set of tutorials intended to help partners in developing an Android audio book application or add audio books to an already developed Android application. It is broken into what we consider the major steps needed to acomplish this task. Those steps are as follows...

1. Understanding Audio Engine object models.
2. Obtaining an Audio Engine session (which is required to interact with the different components of Audio Engine).
3. Application login and Audio Engine sessions.
4. Getting a list of audio books for your account.
5. Downloading audio for an audio book.
6. Playing (streaming or downloaded) an audio book.


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



##Chapter Object

The chapter object represents a specific chapter in a piece of content and contains information
specific to that chapter. There can be multiple chapter objects contained within each content
object. Chapter object can be created using id, part, and chapter, or without a value.

###Methods

getFriendlyName() - Returns formatted part and chapter information.

