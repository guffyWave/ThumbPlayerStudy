package com.gufran.thumbplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    ThumbPlayerView thumbPlayerView, thumbPlayerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbPlayerView = (ThumbPlayerView) findViewById(R.id.thumbPlayerView);
        thumbPlayerView2 = (ThumbPlayerView) findViewById(R.id.thumbPlayerView2);
        thumbPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayerService.class);
                i.setAction(PlayerService.ACTION_TOGGLE_PLAYBACK);
                Uri uri = Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3");
                i.setData(uri);
                startService(i);
            }
        });
        thumbPlayerView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayerService.class);
                i.setAction(PlayerService.ACTION_TOGGLE_PLAYBACK);
                Uri uri = Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3");
                i.setData(uri);
                startService(i);
            }
        });

    }
}
