package com.entersnowman.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Valentin on 13.01.2017.
 */

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.TimeHolder> {

    private ArrayList<Alarm> alarms;
    private Context context;
    AlarmManager alarmManager;
    @Override
    public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item,parent,false);
        TimeHolder timeHolder = new TimeHolder(v);
        return timeHolder;
    }

    public TimesAdapter(ArrayList<Alarm> alarms, Context context){
        this.alarms = alarms;
        this.context = context;
        alarmManager = (AlarmManager) this.context.getSystemService(ALARM_SERVICE);
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
        holder.isOn.setOnCheckedChangeListener(null);
        holder.isOn.setChecked(alarms.get(position).isOn());
        holder.isOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Intent canceledAlarm = new Intent(context,AlarmReceiver.class);
                    canceledAlarm.setAction(ListOfTimesActivity.ALARM_ACTION+Integer.toString(alarms.get(position).getHour())+":"+Integer.toString(alarms.get(position).getMinute()));
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,canceledAlarm,0);
                    Calendar calendar = Calendar.getInstance();
                    int day = 0;
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    if ((alarms.get(position).getHour()<calendar.getTime().getHours())||(alarms.get(position).getHour()==calendar.getTime().getHours()&&alarms.get(position).getMinute()<calendar.getTime().getMinutes()))
                        day = 24*60*60*1000;
                    calendar.set(Calendar.HOUR_OF_DAY, alarms.get(position).getHour());
                    calendar.set(Calendar.MINUTE, alarms.get(position).getMinute());
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()-60*1000+day,AlarmManager.INTERVAL_DAY,pendingIntent);
                    SharedPreferences.Editor editor = context.getSharedPreferences("listOfAlarms",MODE_PRIVATE).edit();
                    editor.putBoolean(Integer.toString(alarms.get(position).getHour())+":"+Integer.toString(alarms.get(position).getMinute()),b);
                    editor.commit();
                    alarms.get(position).setOn(b);
                    notifyDataSetChanged();
                }
                else {
                    Intent canceledAlarm = new Intent(context,AlarmReceiver.class);
                    canceledAlarm.setAction(ListOfTimesActivity.ALARM_ACTION+Integer.toString(alarms.get(position).getHour())+":"+Integer.toString(alarms.get(position).getMinute()));
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,canceledAlarm,0);
                    alarmManager.cancel(pendingIntent);
                    SharedPreferences.Editor editor = context.getSharedPreferences("listOfAlarms",MODE_PRIVATE).edit();
                    editor.putBoolean(Integer.toString(alarms.get(position).getHour())+":"+Integer.toString(alarms.get(position).getMinute()),b);
                    editor.commit();
                    alarms.get(position).setOn(b);
                    notifyDataSetChanged();
                }
            }
        });
        holder.deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent canceledAlarm = new Intent(context,AlarmReceiver.class);
                canceledAlarm.setAction(ListOfTimesActivity.ALARM_ACTION+Integer.toString(alarms.get(position).getHour())+":"+Integer.toString(alarms.get(position).getMinute()));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,canceledAlarm,0);
                alarmManager.cancel(pendingIntent);
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
