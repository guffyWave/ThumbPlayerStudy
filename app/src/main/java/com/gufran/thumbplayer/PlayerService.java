package com.gufran.thumbplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Description ...
 *
 * @author Gufran Khurshid
 * @version 1.0
 * @since 6/12/17
 */

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    enum State {
        Preparing,
        Playing,
        Stopped,
    }

    private static final String TAG = "PLAYER_SERVICE";
    MediaPlayer mPlayer = null;
    static PlayerService.State mState = State.Preparing;
    String currentlyPlayedURL = "";

    //Actions
    public static final String ACTION_TOGGLE_PLAYBACK = BuildConfig.APPLICATION_ID + ".musicplayer.action.TOGGLE_PLAYBACK";
    public static final String ACTION_PLAY = BuildConfig.APPLICATION_ID + ".musicplayer.action.PLAY";
    public static final String ACTION_STOP = BuildConfig.APPLICATION_ID + ".musicplayer.action.STOP";


    void createMediaPlayerIfNeeded() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            // mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);// mPlayer will acquire a wake-lock while playing.
        } else
            mPlayer.reset();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        String requestedURL = intent.getData().toString();
        if (action.equals(ACTION_PLAY)) {
            currentlyPlayedURL = requestedURL;
            processPlayRequest(currentlyPlayedURL);
        } else if (action.equals(ACTION_TOGGLE_PLAYBACK)) {
            if (requestedURL.equals(currentlyPlayedURL)) {
                currentlyPlayedURL = requestedURL;
                processTogglePlaybackRequest(currentlyPlayedURL);
            } else {
                currentlyPlayedURL = requestedURL;
                processPlayRequest(currentlyPlayedURL);
            }
        } else if (action.equals(ACTION_STOP)) processStopRequest();
        return START_NOT_STICKY;
    }

    void processTogglePlaybackRequest(String urlToPlay) {
        if (mState == State.Stopped || mState == State.Preparing) {
            processPlayRequest(urlToPlay);
        } else {
            processStopRequest();
        }
    }

    void processPlayRequest(String urlToPlay) {
        playStream(urlToPlay);
    }

    void playStream(String manualUrl) {
        mState = PlayerService.State.Stopped;
        relaxResources(false); // release everything except MediaPlayer
        try {
            createMediaPlayerIfNeeded();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(manualUrl);
            mState = PlayerService.State.Preparing;
            mPlayer.prepareAsync();
        } catch (IOException ex) {
            Log.e(TAG, "IOException playing URL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void processStopRequest() {
        if (mState == PlayerService.State.Playing) {
            mState = PlayerService.State.Stopped;
            relaxResources(true);
            stopSelf();
        }
    }

    void relaxResources(boolean releaseMediaPlayer) {
        //  stopForeground(true); // stop being a foreground service
        if (releaseMediaPlayer && mPlayer != null) {// stop and release the Media Player, if it's available
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        // we can also release the Wifi lock, if we're holding it
        //   if (mWifiLock.isHeld()) mWifiLock.release();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        playStream(null);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mState = PlayerService.State.Playing;
        mPlayer.setVolume(1.0f, 1.0f); // up the vol to max
        if (!mPlayer.isPlaying()) mPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(getApplicationContext(), "Media mPlayer error! Resetting.",
                Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: what=" + String.valueOf(what) + ", extra=" + String.valueOf(extra));
        mState = PlayerService.State.Stopped;
        relaxResources(true);
        return true; // we handled the error
    }

    @Override
    public void onDestroy() {
        mState = PlayerService.State.Stopped;
        relaxResources(true);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
