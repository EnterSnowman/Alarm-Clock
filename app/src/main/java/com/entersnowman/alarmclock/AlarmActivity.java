package com.entersnowman.alarmclock;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
    SharedPreferences musicPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        stopAlarm = (Button) findViewById(R.id.stopAlarm);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
        musicPrefs = getSharedPreferences("listOfMusicPreferences",MODE_PRIVATE);
        if (musicPrefs.getBoolean("isOtherRingtone",false))
        {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(this, Uri.parse(musicPrefs.getString("ringtonePath","")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setVolume(0.5f,0.5f);
        }
        else {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            mediaPlayer.setVolume(0.5f,0.5f);
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("music", String.valueOf(mediaPlayer.getCurrentPosition()));
                mediaPlayer.stop();
                mediaPlayer.release();
                stopAlarm.setEnabled(false);
            }
        });
    }

}
