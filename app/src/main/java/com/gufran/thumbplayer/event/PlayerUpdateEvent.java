package com.gufran.thumbplayer.event;

/**
 * Description ...
 *
 * @author Gufran Khurshid
 * @version 1.0
 * @since 6/15/17
 */

public class PlayerUpdateEvent {

    public String action;
    public int progress;
    public String playedURL;

    public PlayerUpdateEvent(String action, int progress, String playedURL) {
        this.action = action;
        this.progress = progress;
        this.playedURL = playedURL;
    }
}
