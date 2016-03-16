# Tutorial 6 - Playing (streaming or downloaded) an audio book.

We ended the last tutorial by successfully downloading an audiobook to your phone.
In this tutorial we will work on the playback of the audio. One of the first things we
need to do is create an area that will show our media player. We do this at the bottom 
of the layout for our book activity. We need to create a frame layout that will hold 
our audio player and we give it an id. 

``` XML
<FrameLayout
    android:id="@+id/player"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
</FrameLayout>
```

Now we need to create the player that will appear in the frame layout when 
we need to play audio. We create a fragment and a layout xml that will go with that fragment.
The layout contains all of the displays and controls a normal media player would have.

``` XML
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:id="@+id/player_fragment"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="wrap_content">

    <LinearLayout
        android:id="@+id/bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:layout_weight="10">

            <TextView
                android:id="@+id/barTitle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="3dp"
                android:text="@string/book_title"/>

            <TextView
                android:id="@+id/barChapter"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="3dp"
                android:text="@string/chapter"/>
        </LinearLayout>

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:orientation="horizontal"
            android:gravity="center"
            android:layout_weight="4">

            <ImageButton
                android:id="@+id/barBack"
                android:src="@drawable/ic_action_back"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:background="@color/black"
                android:contentDescription="@string/play_button"
                android:padding="2dp"/>

            <ImageButton
                android:id="@+id/barPlay"
                android:src="@drawable/ic_action_pause"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:background="@color/black"
                android:contentDescription="@string/play_button"
                android:padding="2dp"/>

            <ImageButton
                android:id="@+id/barForward"
                android:src="@drawable/ic_action_forward"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:background="@color/black"
                android:contentDescription="@string/play_button"
                android:padding="2dp"/>

        </LinearLayout>

    </LinearLayout>

    <LinearLayout
        android:id="@+id/info"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="5dp"
        android:paddingBottom="2dp"
        android:gravity="center|center_horizontal"
        android:orientation="horizontal"
        android:weightSum="1">

        <TextView
            android:id="@+id/played"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="00:00"
            android:textSize="12sp"
            android:padding="2dp"
            android:textStyle="bold"/>

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:gravity="top"
            android:orientation="vertical">

            <SeekBar
                android:id="@+id/seekBar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>

        </LinearLayout>

        <TextView
            android:id="@+id/duration"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="00:00"
            android:textSize="12sp"
            android:padding="2dp"
            android:textStyle="bold"/>

    </LinearLayout>

</LinearLayout>
```

Now that we have a layout and fragment created we can set everything up
to work with playback engine.

### Player Fragment

There are a few things we need to hook up to make all of the parts of the player work.
The first thing we need to do is playback engine. We do this by creating a 
playback engine object and then setting it to a new instance of playback engine.
 
``` Java
private PlaybackEngine mPlaybackEngine;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    try {
        mPlaybackEngine = AudioEngine.getPlaybackEngine();
    
    } catch (AudioEngineException e) {
        Log.e(TAG, "Playback engine error.");
    }
}
```

Now we setup all of the buttons, seekbar, and labels in CreateView. We will also register
all of the listeners for our buttons and seekbar so that we can handle the user action.

``` Java

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_player, container, false);

    playButton = (ImageButton) view.findViewById(R.id.barPlay);
    playButton.setOnClickListener(this);
    backButton = (ImageButton) view.findViewById(R.id.barBack);
    backButton.setOnClickListener(this);
    forwardButton = (ImageButton) view.findViewById(R.id.barForward);
    forwardButton.setOnClickListener(this);

    mTitle = (TextView) view.findViewById(R.id.barTitle);
    mChapter = (TextView) view.findViewById(R.id.barChapter);
    mPlayed = (TextView) view.findViewById(R.id.played);
    mDuration = (TextView) view.findViewById(R.id.duration);
    mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
    mSeekBar.setOnSeekBarChangeListener(this);

    mPlaybackEngine.registerPlaybackListener(this);

    return view;
}
```

In order to handle these listeners as well as our playback engine listener we need 
to implement a few things. View.OnClickListener will handle our button clicks, 
PlaybackListener will handle playback engine callbacks, and SeekBar.OnBarChangeListener 
will handle user seeking through the audio track.

``` Java

public class PlayerFragment extends Fragment implements View.OnClickListener, PlaybackListener, SeekBar.OnSeekBarChangeListener {

```

#### PlaybackListener
The PlaybackListener has 3 override methods. The main one used to update the ui is the "update" method
which returns a PlaybackProgressEvent. This event contains the track info and playback info for a 
currently playing track. We need to update the user interface with this data. We can start by saving 
this data to an accessable variable. Then we can create a handler, which will give us access to our ui 
thread. We can add this to our onCreate.

``` Java

uiHandler = new Handler();

```

After we save our current playback event we can then call an update to the ui. We create a Runnable that the 
handler will post to the ui thread. It uses the playback event to update the UI with the latest playback data.

``` Java

@Override
public void update(PlaybackProgressEvent playbackProgressEvent) {
    Log.i(TAG, "Got playback progress event. Duration is " + playbackProgressEvent.duration + ", position is " + playbackProgressEvent.position);
    mPlaybackProgressEvent = playbackProgressEvent;
    uiHandler.post(updatePlayerProgress);
}

private Runnable updatePlayerProgress = new Runnable() {
    public void run() {
        mTitle.setText(mPlaybackProgressEvent.content.title);
        mChapter.setText(String.format(Locale.US, "Chapter %1$d Part %2$d", mPlaybackProgressEvent.chapter.chapterNumber, mPlaybackProgressEvent.chapter.partNumber));
        mSeekBar.setMax(mPlaybackProgressEvent.duration);
        mSeekBar.setProgress(mPlaybackProgressEvent.position);

        Double bufferPercent = mPlaybackProgressEvent.bufferedPercentage.doubleValue() / 100;
        Double buffer = mPlaybackProgressEvent.duration * bufferPercent;
        Integer buff = buffer.intValue();
        Log.i(TAG, buff.toString());
        mSeekBar.setSecondaryProgress(buff);
        mDuration.setText(com.findaway.audioengine.mobile.util.ContentUtils.getTimeString(mPlaybackProgressEvent.duration));
        mPlayed.setText(com.findaway.audioengine.mobile.util.ContentUtils.getTimeString(mPlaybackProgressEvent.position));
    }
};

```

#### OnClick
The onclick override will handle any buttons that are clicked. In this case we have
a play and pause button, and forward and backward buttons. 

``` Java

if (v.getId() == R.id.barPlay) {
    try {
        if (mPlaybackEngine.getState() == PlayerState.PLAYING) {
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
            mPlaybackEngine.pause();
        } else if (mPlaybackEngine.getState() == PlayerState.PAUSED) {
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
            mPlaybackEngine.resume();
        }
    } catch (AudioEngineException ex) {
        Log.e(TAG,  "Failed trying to play or pause.");
    }
} else if (v.getId() == R.id.barBack) {
    mPlaybackEngine.previousChapter();
    playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
} else if (v.getId() == R.id.barForward) {
    mPlaybackEngine.nextChapter();
    playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
}

```

#### OnSeekBarChangeListener
The seekbar will display the current progress through the track to the user. 
If the user wants to change the current play time they can slide the bar to
make the audio move to the new position.

``` Java

@Override
public void onStopTrackingTouch(SeekBar seekBar) {
    mPlaybackEngine.seekTo(mProgress);
    mPlaybackEngine.registerPlaybackListener(this);
}

@Override
public void onStartTrackingTouch(SeekBar seekBar) {
    mPlaybackEngine.unregisterPlaybackListener(this);
}

@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    mProgress = progress;
}

```