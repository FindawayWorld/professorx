package com.findaway.audioengine.sample.book;

import com.findaway.audioengine.sample.audiobooks.Content;

/**
 * Created by agofman on 2/9/16.
 */
public interface BookListener {

    public void success(Content content);

    public void error(Integer code, String message);
}
