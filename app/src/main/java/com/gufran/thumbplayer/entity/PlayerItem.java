package com.gufran.thumbplayer.entity;

import com.gufran.thumbplayer.ThumbPlayerView;

/**
 * Description ...
 *
 * @author Gufran Khurshid
 * @version 1.0
 * @since 6/16/17
 */

public class PlayerItem {
    public String name;
    public String mediaURL;
    public int state;
    public int progress;

    public PlayerItem(String name, String mediaURL) {
        this.name = name;
        this.mediaURL = mediaURL;
        this.state = ThumbPlayerView.STATE_STOPPED;
        this.progress = 0;
    }
}
