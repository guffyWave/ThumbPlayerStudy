package com.gufran.thumbplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gufran.thumbplayer.event.PlayerUpdateEvent;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {
    ThumbPlayerView thumbPlayerView, thumbPlayerView2;
    String url1 = "https://sampleswap.org/samples-ghost/MELODIC%20SAMPLES%20and%20LOOPS/GUITARS%20etcetera/969[kb]anthem-5ths.aif.mp3";
    String url2 = "https://sampleswap.org/samples-ghost/MELODIC%20SAMPLES%20and%20LOOPS/guitar%20acoustic%20picking/2070[kb]120_acoustic-guitar-picking2.aif.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThumbPlayerApp.eventBus.register(this);

        thumbPlayerView = (ThumbPlayerView) findViewById(R.id.thumbPlayerView);
        thumbPlayerView2 = (ThumbPlayerView) findViewById(R.id.thumbPlayerView2);
        thumbPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayerService.class);
                i.setAction(PlayerService.ACTION_TOGGLE_PLAYBACK);
                Uri uri = Uri.parse(url1);
                i.setData(uri);
                startService(i);

            }
        });
        thumbPlayerView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayerService.class);
                i.setAction(PlayerService.ACTION_TOGGLE_PLAYBACK);
                Uri uri = Uri.parse(url2);
                i.setData(uri);
                startService(i);
            }
        });
    }

    @Subscribe
    public void playerUpdate(PlayerUpdateEvent playerUpdateEvent) {

        if (playerUpdateEvent.playedURL.equals(url1)) {// first view
            if (playerUpdateEvent.action.equals(PlayerService.BROADCAST_ACTION_PREPARING)) {
                thumbPlayerView.setState(ThumbPlayerView.STATE_PREPARING);
            } else if (playerUpdateEvent.action.equals(PlayerService.BROADCAST_ACTION_PLAYING)) {
                thumbPlayerView.setState(ThumbPlayerView.STATE_PLAYING);
                thumbPlayerView.setProgress(playerUpdateEvent.progress);
            } else {
                thumbPlayerView.setState(ThumbPlayerView.STATE_STOPPED);
            }
        }


        if (playerUpdateEvent.playedURL.equals(url2)) {// second view
            if (playerUpdateEvent.action.equals(PlayerService.BROADCAST_ACTION_PREPARING)) {
                thumbPlayerView2.setState(ThumbPlayerView.STATE_PREPARING);
            } else if (playerUpdateEvent.action.equals(PlayerService.BROADCAST_ACTION_PLAYING)) {
                thumbPlayerView2.setState(ThumbPlayerView.STATE_PLAYING);
                thumbPlayerView2.setProgress(playerUpdateEvent.progress);
            } else {
                thumbPlayerView2.setState(ThumbPlayerView.STATE_STOPPED);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThumbPlayerApp.eventBus.unregister(this);
    }
}
