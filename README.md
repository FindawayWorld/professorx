#Tutorial 5 - Downloading audio for an audio book.

In order to download an books audio we will need to do some setup to enable us to download chapters.
We will need a user interface that will support the downloads as well setting up the sdk which will
allow us to make the actual download calls.
 
The first thing we need to do is setup a click listener. We'll need to create a custom click listener
 for the recycler view we are using as the onclick only returns a view. 