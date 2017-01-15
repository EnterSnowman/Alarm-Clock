package com.entersnowman.alarmclock;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity{
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    Button stopAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        stopAlarm = (Button) findViewById(R.id.stopAlarm);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setVolume(0.5f,0.5f);
        mediaPlayer.start();
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("music", String.valueOf(mediaPlayer.getCurrentPosition()));
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

}
