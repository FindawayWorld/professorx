package com.findaway.audioengine.sample.audiobooks;

import java.util.List;

/**
 * Created by agofman on 1/28/16.
 */
public interface AudiobookListener {

    public void success(List<Content> audiobookList);

    public void error(Integer code, String message);
}
