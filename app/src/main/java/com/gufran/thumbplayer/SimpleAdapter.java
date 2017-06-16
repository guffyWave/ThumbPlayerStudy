package com.gufran.thumbplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gufran.thumbplayer.entity.PlayerItem;
import com.gufran.thumbplayer.event.PlayerUpdateEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Description ...
 *
 * @author Gufran Khurshid
 * @version 1.0
 * @since 5/15/17
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ItemHolder> {
    Context context;
    private LayoutInflater layoutInflater;
    ArrayList<PlayerItem> dataList;

    public SimpleAdapter(Context context, ArrayList<PlayerItem> dataList) {
        layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public SimpleAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.layout_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleAdapter.ItemHolder holder, final int position) {
        final PlayerItem playerItem = dataList.get(position);
        holder.textItemName.setText(playerItem.name);
        holder.thumbPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayerService.class);
                i.setAction(PlayerService.ACTION_TOGGLE_PLAYBACK);
                Uri uri = Uri.parse(playerItem.mediaURL);
                i.setData(uri);
                i.putExtra("POSITION", position);
                context.startService(i);
            }
        });

        if (playerItem.state == ThumbPlayerView.STATE_STOPPED) {
            holder.thumbPlayerView.setState(ThumbPlayerView.STATE_STOPPED);
            holder.thumbPlayerView.setClickable(true);
        } else if (playerItem.state == ThumbPlayerView.STATE_PLAYING) {
            holder.thumbPlayerView.setState(ThumbPlayerView.STATE_PLAYING);
            holder.thumbPlayerView.setProgress(playerItem.progress);
            holder.thumbPlayerView.setClickable(true);
        } else if (playerItem.state == ThumbPlayerView.STATE_PREPARING) {
            holder.thumbPlayerView.setState(ThumbPlayerView.STATE_PREPARING);
            holder.thumbPlayerView.setClickable(false);
        } else {
            holder.thumbPlayerView.setState(ThumbPlayerView.STATE_ERROR);
            holder.thumbPlayerView.setClickable(true);
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        ThumbPlayerView thumbPlayerView;

        public ItemHolder(View itemView) {
            super(itemView);
            textItemName = (TextView) itemView.findViewById(R.id.item_name);
            thumbPlayerView = (ThumbPlayerView) itemView.findViewById(R.id.thumbPlayerView);
        }
    }


}
