#Tutorial 5 - Downloading audio for an audio book.

In order to download a book's audio we will need to do some setup to enable us to download the chapters.
We will need a user interface that will support the downloads as well setting up the sdk which will
allow us to make the actual download calls. 
 
The first thing we will do is setup the user interface so that we have a place for a user to download books.
The first step of that will be to setup a click listener. We will make our library fragment an On click listener.
When one of the books is clicked on, we will create a new activity.

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
```

The book activity needs to be declared in the manifest. This activity will mainly be a container for 
our pager adapter which contain the two fragments the user will see. There are two fragments which will
represent the details screen and the chapter download screen. In this tutorial we will focus on the chapter list
screen as this is where a user will go to download their books.

###ChapterFragment

The chapter fragment will display the list of chapters for the specific audiobook selected. In order to get
all of the chapters for the audiobook we can make a call to the API. The code to do this will be very
similar to the previous tutorial so you can look at that one for more on how to make an api call for the
audiobook data needed. Once we have the list of chapters we can display them in a recycler view. 

In order to download audiobooks we will need access to the Audio Engine SDK. The information about how to install 
and use the sdk can be found in the Audio Engine Developer Portal (http://developer.audioengine.io/sdk/android).
Those are the instructions we will follow in this tutorial. First we need to add the SDK to our project.
We can do this by adding the dependency to our gradle build files. 

``` Java
repositories {

    mavenCentral()

    maven {
        url "http://maven.findawayworld.com/artifactory/libs-release/"
    }
}

dependencies {
    compile 'com.findaway:audio-engine-mobile:6.0.5'
}
```

We now have to add some permissions to the manifest.xml. 

``` XML
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.BROADCAST_STICKY"/>
```

We also need to add the following service and content providers to the manifest.xml. 

``` XML
<service android:name="com.findaway.audioengine.mobile.PlaybackService" />

<provider tools:replace="android:authorities" android:authorities="com.example.audioengine.contentprovider" android:name="com.findaway.audioengine.mobile.AudioEngineProvider"/>
```

The last thing we need to do is create an an Authority class that can be found by AudioEngine. 
This class is needed to support the installation of multiple apps on the same device that use 
Findaway World's AudioEngine. This class must be located in the com.findaway.audioengine package. 
The Authority class should look exactly as follows, where the value of the CONTENT_AUTHORITY field 
matches what you set the authorities value to in your manifest file. This authority field should be 
unique to your application and commonly includes your app package name.

``` Java
package com.findaway.audioengine.sample;

public class Authority {

    public static final String CONTENT_AUTHORITY = "com.example.audioengine.contentprovider";
}
```

Now we have the Audio Engine SDK all set up. The next thing we need to do is enable some on click events
in our chapter fragment so that when a user clicks on one of the chapters we can initiate the download
of that chapter. To start a download we use the download engine object. Also in order to use the download 
engine we need to initialize audio engine and register the fragment as the download listener for download engine. 
We can do this in the onCreate of our chapter fragment. 

```Java
public void onCreate(Bundle savedInstanceState) {
...
    AudioEngine.init(getActivity(), sessionId, LogLevel.WARNING);
    mDownloadEngine = AudioEngine.getDownloadEngine();
    mDownloadEngine.registerDownloadListener(this);
```

In this tutorial we use a custom click listener class so that we can handle the clicks in the chapter fragment.
We set the chapter fragment as the custom listener, and pass it to our view holder where we can set the chapter
fragment as the listener for each chapter view. 

``` Java
    @Override
    public void onClick(View v) {
        mRecyclerViewClickListener.recyclerViewListClicked(v, this.getLayoutPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        mRecyclerViewClickListener.recyclerViewListLongClicked(v, this.getLayoutPosition());
        return true;
    }

    public ChapterViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        mRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        chapter_number = (TextView)itemView.findViewById(R.id.chapter_number);
        part_number = (TextView)itemView.findViewById(R.id.part_number);
        duration = (TextView)itemView.findViewById(R.id.duration);
        download_status = (TextView)itemView.findViewById(R.id.download_status);
        download_progress = (ProgressBar)itemView.findViewById(R.id.progress_bar);
        download_progress.setMax(100);
    }
```

Once this is done, now we can handle our on click events in our fragment. In these events we will use 
the download engine object we created to make the download and delete calls for a single file.

```Java
@Override
public void recyclerViewListClicked(View v, int position) {
    ChapterViewHolder chapterViewHolder = (ChapterViewHolder) mChapterListView.findViewHolderForAdapterPosition(position);
    TextView textView = (TextView) chapterViewHolder.itemView.findViewById(R.id.download_status);
    if (!textView.getText().toString().equals(getString(R.string.downloaded))) {
        mChapter = mChapterContentAdapter.getChapter(position);
        try {
            mDownloadEngine.download(mContentId, mChapter.part_number, mChapter.chapter_number, null, mAccountId, false, false);
        } catch (AudioEngineException ex) {
            Log.e(getTag(), "Problem while downloading content");
        }
    } else {
        Toast.makeText(getActivity(), "File Already Downloaded", Toast.LENGTH_SHORT).show();
    }
}

@Override
public void recyclerViewListLongClicked(View v, int position) {
    ChapterViewHolder chapterViewHolder = (ChapterViewHolder) mChapterListView.findViewHolderForAdapterPosition(position);
    TextView textView = (TextView) chapterViewHolder.itemView.findViewById(R.id.download_status);
    if (textView.getText().toString().equals(getString(R.string.downloaded))) {
        mChapter = mChapterContentAdapter.getChapter(position);
        mDownloadEngine.delete(mContentId, mChapter.part_number, mChapter.chapter_number);
    } else {
        Toast.makeText(getActivity(), "No file found to delete", Toast.LENGTH_SHORT).show();
    }
}
```

We will also create an options menu and some items so that we can handle bulk downloading and deleting 
of chapters. This is just one way to handle it, but there are many ways of doing this. In the on 
create of our chapter fragment we need to set that we have an options menu.

```Java
setHasOptionsMenu(true);
```

Once this is done, this will add a few more methods to the activity lifecycle. We can set the options
items by overriding the on create options menu and then handle the different options selections by
overriding the on options item selected. Once we have identified which option is selected we can make the 
bulk download or bulk delete call.

```Java
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.add("Download All");
    menu.add("Delete All");
    super.onCreateOptionsMenu(menu, inflater);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getTitle() == "Download All") {
        try {
            mDownloadEngine.download(mContentId, mContent.chapters.get(0).part_number, mContent.chapters.get(0).chapter_number, null, mAccountId, true, true);
        } catch (AudioEngineException ex) {
            Log.e(getTag(), "Problem while downloading content");
        }
    } else if (item.getTitle() == "Delete All") {
        mDownloadEngine.delete(mContentId);
    }
    return super.onOptionsItemSelected(item);
}
```

Now we have a view that displays the chapters in the book we chose. We are also able to start a download
or a delete of one or all of the chapters. That last part of this tutorial is to handle the download
events that are returned from the download and delete methods so that the UI can respond to the user. 
We do this by implementing the Audio Engine download listener in our fragment.

```Java
...
public class ChapterFragment extends Fragment implements BookView, DownloadListener, RecyclerViewClickListener {
...
```

This allows us to override three new methods. There are two update methods, and one error method. One
of the update methods returns a download event while the other returns a download progress event. The
download events will receive status's that will notify the overall status of the download like started
or completed. The download progress event will receive the download progress percentage of the chapter
being downloaded. 

``` Java
@Override
public void update(final DownloadProgressEvent downloadProgressEvent) {
    View view = findViewByPartAndChapter(downloadProgressEvent);
    if (view != null) {
        final TextView textView = (TextView) view.findViewById(R.id.download_status);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        ((BookActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(downloadProgressEvent.chapterPercentage + " %");
                progressBar.setProgress(downloadProgressEvent.chapterPercentage);
            }
        });
    }
}

@Override
public void update(DownloadEvent downloadEvent) {
    if (downloadEvent.code.equals(DownloadEvent.CHAPTER_DOWNLOAD_COMPLETED) && downloadEvent.chapter != null) {
        View view = findViewByPartAndChapter(downloadEvent);
        if (view != null) {
            final TextView statusView = (TextView) view.findViewById(R.id.download_status);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            ((BookActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusView.setText(getText(R.string.downloaded));
                    progressBar.setProgress(100);
                }
            });
        }
    } else if (downloadEvent.code.equals(DownloadEvent.DELETE_COMPLETE) && downloadEvent.chapter != null) {
        View view = findViewByPartAndChapter(downloadEvent);
        if (view != null) {
            final TextView statusView = (TextView) view.findViewById(R.id.download_status);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            ((BookActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusView.setText(getText(R.string.not_downloaded));
                    progressBar.setProgress(0);
                }
            });
        }
    } else if (downloadEvent.code.equals(DownloadEvent.DOWNLOAD_STARTED) && downloadEvent.chapter != null) {
        View view = findViewByPartAndChapter(downloadEvent);
        if (view != null) {
            final TextView statusView = (TextView) view.findViewById(R.id.download_status);
            ((BookActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusView.setText(getText(R.string.downloading).toString());
                }
            });
        }
    } else if (downloadEvent.code.equals(DownloadEvent.DELETE_COMPLETE) && downloadEvent.chapter == null) {
        int startPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        int endPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        for (int x = startPosition; x <= endPosition; x++) {
            View view = mChapterListView.findViewHolderForAdapterPosition(x).itemView.findViewById(R.id.chapter_list_view);
            if (view != null) {
                final TextView statusView = (TextView) view.findViewById(R.id.download_status);
                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                ((BookActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusView.setText(getText(R.string.not_downloaded));
                        progressBar.setProgress(0);
                    }
                });
            }
        }
    }
}

@Override
public void error(DownloadError downloadError) {
    if (downloadError.code.equals(DownloadError.UNAUTHORIZED)) {

        Toast.makeText(getActivity(), "Permission denied. Please check username and password.", Toast.LENGTH_SHORT).show();

    } else if (downloadError.code.equals(DownloadError.FORBIDDEN)) {

        Toast.makeText(getActivity(), "You do not have access to this book.", Toast.LENGTH_SHORT).show();

    } else if (downloadError.code.equals(DownloadError.CONTENT_NOT_FOUND)) {

        Toast.makeText(getActivity(), "Unable to find requested book.", Toast.LENGTH_SHORT).show();

    } else if (downloadError.code.equals(DownloadError.CHAPTER_NOT_FOUND)) {

        Toast.makeText(getActivity(), "Invalid chapter requested.", Toast.LENGTH_SHORT).show();

    } else if (downloadError.code.equals(DownloadError.NETWORK_ERROR)) {

        Toast.makeText(getActivity(), "A network error has occurred. Please check you connection and try again.", Toast.LENGTH_SHORT).show();
    }
}
```