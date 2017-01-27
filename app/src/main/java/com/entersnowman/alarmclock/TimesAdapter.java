package com.entersnowman.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Valentin on 13.01.2017.
 */

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.TimeHolder> {

    private ArrayList<Alarm> alarms;
    private Context context;
    @Override
    public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item,parent,false);
        TimeHolder timeHolder = new TimeHolder(v);
        return timeHolder;
    }

    public TimesAdapter(ArrayList<Alarm> alarms, Context context){
        this.alarms = alarms;
        this.context = context;
    }

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    @Override
    public void onBindViewHolder(final TimeHolder holder, final int position) {
        Log.d("1","Added "+position+" "+alarms.get(position).getHour()+":"+alarms.get(position).getMinute());
        String minute;
        if(alarms.get(position).getMinute()<10)
        minute = "0"+Integer.toString(alarms.get(position).getMinute());
        else
        minute = Integer.toString(alarms.get(position).getMinute());
        holder.timeOfAlarm.setText(alarms.get(position).getHour()+":"+minute);
        holder.isOn.setChecked(alarms.get(position).isOn());
        holder.deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("listOfAlarms",MODE_PRIVATE).edit();
                editor.remove(Integer.toString(alarms.get(position).getHour())+":"+Integer.toString(alarms.get(position).getMinute()));
                editor.commit();
                alarms.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public static class TimeHolder extends RecyclerView.ViewHolder{
        public CheckBox isOn;
        public TextView timeOfAlarm;
        public ImageView deleteAlarmButton;
        public TimeHolder(View itemView) {
            super(itemView);
            isOn = (CheckBox) itemView.findViewById(R.id.isAlarmOn);
            timeOfAlarm = (TextView) itemView.findViewById(R.id.timeOfAlarm);
            deleteAlarmButton = (ImageView) itemView.findViewById(R.id.deleteAlarm);
        }
    }
}
