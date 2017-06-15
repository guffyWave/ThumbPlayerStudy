package com.gufran.thumbplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<String> dataList = new ArrayList<>();
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setUpToolBar();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        dataList.add("https://sampleswap.org/samples-ghost/MELODIC%20SAMPLES%20and%20LOOPS/GUITARS%20etcetera/969[kb]anthem-5ths.aif.mp3");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");


//        for (int i = 0; i < 20; i++) {
//            dataList.add("Hello " + i);
//        }

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
}
