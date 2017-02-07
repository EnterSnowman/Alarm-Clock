package com.entersnowman.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Calendar;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.entersnowman.alarmclock.ListOfTimesActivity.ALARM_ACTION;

/**
 * Created by Valentin on 18.01.2017.
 */

public class BootReceiver extends BroadcastReceiver {
    public final  static int DAY_IN_MILLIS = 24*60*60*1000;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("time","Boot receive");
            for (Map.Entry<String,Boolean> entry: ((Map<String, Boolean>)context.getSharedPreferences("listOfAlarms",MODE_PRIVATE).getAll()).entrySet()){
                if (entry.getValue()){
                    Intent newIntent  = new Intent(context,AlarmReceiver.class);
                    intent.setAction(ALARM_ACTION+entry.getKey().split(":")[0]+":"+entry.getKey().split(":")[1]);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,newIntent,0);
                    Calendar calendar = Calendar.getInstance();
                    int day = 0;
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    if ((Integer.valueOf(entry.getKey().split(":")[0])<calendar.getTime().getHours())||(Integer.valueOf(entry.getKey().split(":")[0])==calendar.getTime().getHours()&&Integer.valueOf(entry.getKey().split(":")[1])<calendar.getTime().getMinutes()))
                        day = DAY_IN_MILLIS;
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(entry.getKey().split(":")[0]));
                    calendar.set(Calendar.MINUTE, Integer.valueOf(entry.getKey().split(":")[1]));
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()-60*1000+day,AlarmManager.INTERVAL_DAY,pendingIntent);

                }
            }
        }
    }
}
