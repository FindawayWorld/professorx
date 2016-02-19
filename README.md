#Tutorial 5 - Downloading audio for an audio book.

In order to download a book's audio we will need to do some setup to enable us to download the chapters.
We will need a user interface that will support the downloads as well setting up the sdk which will
allow us to make the actual download calls. 
 
The first thing we will do is setup the user interface so that we have a place for a user to download books.
The first step of that will be to setup a click listener. When one of the books is clicked on, we will
create a new activity.

``` Java
...
public class LibraryFragment extends Fragment implements AudiobookView, View.OnClickListener {
...
@Override
public void onClick(View v) {
    TextView contentIdView = (TextView) v.findViewById(R.id.id);
    String contentId = contentIdView.getText().toString();
    Intent detailsIntent = new Intent(getActivity(), BookActivity.class);
    detailsIntent.putExtra(BookActivity.EXTRA_CONTENT_ID, contentId);
    detailsIntent.putExtra(BookActivity.EXTRA_SESSION_ID, mSessionId);
    detailsIntent.putExtra(BookActivity.EXTRA_ACCOUNT_ID, mAccountId);
    startActivity(detailsIntent);
}
...
````
The book activity needs to be declared in the manifest. This activity will mainly be a container for 
our pager adapter which contain the two fragments the user will see. There are two fragments which will
represent the details view and the chapter list view. In this tutorial we will focus on the chapter list
view as this is where a user will go to download their books.

###ChapterFragment

The chapter fragment will display the list of chapters for the specific audiobook selected. In order to get
all of the chapters for the audiobook we can make a call to the API. The code to do this will be very
similar to the previous tutorial so you can look at that one for more on how to make an api call. 