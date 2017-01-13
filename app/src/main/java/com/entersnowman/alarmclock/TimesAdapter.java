package com.entersnowman.alarmclock;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Valentin on 13.01.2017.
 */

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.TimeHolder> {

    private ArrayList<Alarm> alarms;
    @Override
    public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item,parent,false);
        TimeHolder timeHolder = new TimeHolder(v);
        return timeHolder;
    }

    public TimesAdapter(ArrayList<Alarm> alarms){
        this.alarms = alarms;
    }

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    @Override
    public void onBindViewHolder(TimeHolder holder, int position) {
        Log.d("1","Added "+position+" "+alarms.get(position).getHour()+":"+alarms.get(position).getMinute());
        String minute;
        if(alarms.get(position).getMinute()<10)
        minute = "0"+Integer.toString(alarms.get(position).getMinute());
        else
        minute = Integer.toString(alarms.get(position).getMinute());
        holder.timeOfAlarm.setText(alarms.get(position).getHour()+":"+minute);
        holder.isOn.setChecked(alarms.get(position).isOn());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public static class TimeHolder extends RecyclerView.ViewHolder{
        public CheckBox isOn;
        public TextView timeOfAlarm;
        public TimeHolder(View itemView) {
            super(itemView);
            isOn = (CheckBox) itemView.findViewById(R.id.isAlarmOn);
            timeOfAlarm = (TextView) itemView.findViewById(R.id.timeOfAlarm);
        }
    }
}
