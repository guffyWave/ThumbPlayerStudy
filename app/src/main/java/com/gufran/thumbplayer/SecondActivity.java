package com.gufran.thumbplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gufran.thumbplayer.entity.PlayerItem;
import com.gufran.thumbplayer.event.PlayerUpdateEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<PlayerItem> dataList = new ArrayList<>();
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ThumbPlayerApp.eventBus.register(this);

        setUpToolBar();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        dataList.add(new PlayerItem("One", "https://sampleswap.org/samples-ghost/MELODIC%20SAMPLES%20and%20LOOPS/GUITARS%20etcetera/969[kb]anthem-5ths.aif.mp3"));
        dataList.add(new PlayerItem("Two", "https://sampleswap.org/samples-ghost/REMIXABLE%20COLLECTIONS/Mike%20360%20Beatbox%20Flute/1701[kb]mike-360-buzz-not-pretty.aif.mp3"));
        dataList.add(new PlayerItem("Three", "https://sampleswap.org/samples-ghost/REMIXABLE%20COLLECTIONS/Mike%20360%20Beatbox%20Flute/2589[kb]mike-360-flutebeat-intro.aif.mp3"));
        dataList.add(new PlayerItem("Four", "https://sampleswap.org/samples-ghost/REMIXABLE%20COLLECTIONS/Mike%20360%20Beatbox%20Flute/999[kb]mike-360-play-one-last-song.aif.mp3"));
        dataList.add(new PlayerItem("Five", "https://sampleswap.org/samples-ghost/PUBLIC%20DOMAIN%20MUSIC/2237[kb]Skamba-Kankliah-ir-Trimintia.mp3.mp3"));
        dataList.add(new PlayerItem("Six", "https://sampleswap.org/samples-ghost/PUBLIC%20DOMAIN%20MUSIC/1168[kb]Pharoahs-Army-Got-Drowned.mp3.mp3"));
        dataList.add(new PlayerItem("Seven", "https://sampleswap.org/samples-ghost/PUBLIC%20DOMAIN%20MUSIC/2861[kb]Nesmeyana_track-3.mp3.mp3"));
        dataList.add(new PlayerItem("Eight", "https://sampleswap.org/samples-ghost/PUBLIC%20DOMAIN%20MUSIC/7006[kb]Mississippi-John-Hurt_Louis-Collins.mp3.mp3"));
        dataList.add(new PlayerItem("Nine", "https://sampleswap.org/samples-ghost/PUBLIC%20DOMAIN%20MUSIC/2475[kb]Mappari-Martha-by-Friedrich-von-Flotow.mp3.mp3"));
        dataList.add(new PlayerItem("Ten", "https://sampleswap.org/samples-ghost/PUBLIC%20DOMAIN%20MUSIC/1550[kb]La-Paloma-by-Banda-de-Zapadores-de-Mexico.mp3.mp3"));
        dataList.add(new PlayerItem("Eleven", "https://sampleswap.org/samples-ghost/PUBLIC%20DOMAIN%20MUSIC/1618[kb]Just-Because-She-Made-Dem-Goo-Goo-Eyes.mp3.mp3"));


        adapter = new SimpleAdapter(SecondActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.notifyDataSetChanged();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Subscribe
    public void playerUpdate(PlayerUpdateEvent playerUpdateEvent) {

        stopAllPlayerItems();

        int broadcastResultPosition = playerUpdateEvent.position;

        PlayerItem playerItem = dataList.get(broadcastResultPosition);

        if (playerUpdateEvent.action.equals(PlayerService.BROADCAST_ACTION_PREPARING)) {
            playerItem.state = ThumbPlayerView.STATE_PREPARING;
        } else if (playerUpdateEvent.action.equals(PlayerService.BROADCAST_ACTION_PLAYING)) {
            playerItem.state = ThumbPlayerView.STATE_PLAYING;
            playerItem.progress = playerUpdateEvent.progress;
        } else if (playerUpdateEvent.action.equals(PlayerService.BROADCAST_ACTION_STOPPED)) {
            playerItem.state = ThumbPlayerView.STATE_STOPPED;
        } else {
            playerItem.state = ThumbPlayerView.STATE_ERROR;
        }

        adapter.notifyDataSetChanged();
    }

    private void stopAllPlayerItems() {
        for (PlayerItem pi :
                dataList) {
            pi.state = ThumbPlayerView.STATE_STOPPED;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThumbPlayerApp.eventBus.unregister(this);
    }
}
