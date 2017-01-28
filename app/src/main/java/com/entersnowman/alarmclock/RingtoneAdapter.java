package com.entersnowman.alarmclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Valentin on 27.01.2017.
 */

public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.RingtoneHolder> {
    ArrayList<Track> tracks;
    Context context;
    Activity activity;
    public  RingtoneAdapter(ArrayList<Track> tracks, Context context,Activity activity){
        this.tracks = tracks;
        this.context = context;
        this.activity  =activity;
    }
    @Override
    public RingtoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        RingtoneAdapter.RingtoneHolder timeHolder = new   RingtoneAdapter.RingtoneHolder(v);
        return timeHolder;
    }

    @Override
    public void onBindViewHolder(RingtoneHolder holder, final int position) {
        holder.name.setText(tracks.get(position).getName());
        holder.artist.setText(tracks.get(position).getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent();
                newIntent.putExtra("name",tracks.get(position).getName());
                newIntent.putExtra("author",tracks.get(position).getAuthor());
                newIntent.putExtra("path",tracks.get(position).getPath());
                activity.setResult(Activity.RESULT_OK,newIntent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class RingtoneHolder extends RecyclerView.ViewHolder {
        TextView artist;
        TextView name;
        public RingtoneHolder(View itemView) {
            super(itemView);
            artist = (TextView) itemView.findViewById(R.id.artist);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
