package com.entersnowman.alarmclock;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ListOfTimesActivity extends AppCompatActivity {
    final  static int TIME_DIALOG = 1;
    SharedPreferences sharedPreferences;
    ArrayList<Alarm> alarms;
    RecyclerView recyclerView;
    TimesAdapter timesAdapter;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_times);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        alarms = new ArrayList<Alarm>();
        recyclerView = (RecyclerView) findViewById(R.id.listOfTimes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        sharedPreferences = getSharedPreferences("listOfAlarms",MODE_PRIVATE);
        Map<String,Boolean> tmp = (Map<String, Boolean>) sharedPreferences.getAll();
        for (Map.Entry<String,Boolean> entry: tmp.entrySet()){
            alarms.add(new Alarm(entry.getValue(),Integer.valueOf(entry.getKey().split(":")[0]),Integer.valueOf(entry.getKey().split(":")[1])));
        }

        timesAdapter = new TimesAdapter(alarms,this);
        recyclerView.setAdapter(timesAdapter);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    protected Dialog onCreateDialog(int id){
        if (id==TIME_DIALOG){
            TimePickerDialog timePickerDialog =new TimePickerDialog(this,alarmCallBack, Calendar.getInstance().getTime().getHours(),Calendar.getInstance().getTime().getMinutes(),true);
            return timePickerDialog;
        }
        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener alarmCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Integer.toString(hourOfDay)+":"+Integer.toString(minute),true);
            editor.commit();
            Intent intent  = new Intent(ListOfTimesActivity.this,AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ListOfTimesActivity.this,0,intent,0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()-60*1000,AlarmManager.INTERVAL_DAY,pendingIntent);
            alarms = new ArrayList<Alarm>();
            Map<String,Boolean> tmp = (Map<String, Boolean>) sharedPreferences.getAll();
            for (Map.Entry<String,Boolean> entry: tmp.entrySet()){
                alarms.add(new Alarm(entry.getValue(),Integer.valueOf(entry.getKey().split(":")[0]),Integer.valueOf(entry.getKey().split(":")[1])));
            }
            timesAdapter.setAlarms(alarms);
            timesAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_of_times, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_alarm) {
            showDialog(TIME_DIALOG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
