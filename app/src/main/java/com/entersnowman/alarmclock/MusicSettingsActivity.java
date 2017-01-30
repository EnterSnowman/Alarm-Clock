package com.entersnowman.alarmclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicSettingsActivity extends AppCompatActivity {
    Button otherRingtonesButton;
    RadioGroup typeOfRingtone;
    RadioButton defaultRingtone;
    RadioButton otherRingtone;
    SharedPreferences musicPreferences;
    boolean isPause;
    TextView nameOfTrack;
    TextView labelAudio;
    ImageButton startBtn;
    ImageButton pauseBtn;
    ImageButton stopBtn;
    public final static int LIST_OF_RINGTONES = 1;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_settings);
        musicPreferences = getSharedPreferences("listOfMusicPreferences",MODE_PRIVATE);
        otherRingtonesButton = (Button) findViewById(R.id.setRingtoneButton);
        createMediaPlayer();
        isPause = false;
        startBtn = (ImageButton) findViewById(R.id.start);
        stopBtn = (ImageButton) findViewById(R.id.stop);
        pauseBtn = (ImageButton) findViewById(R.id.pause);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });

        otherRingtonesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPause)
                    stop();
                startActivityForResult(new Intent(MusicSettingsActivity.this, ListOfRingtonesActivity.class),LIST_OF_RINGTONES);
            }
        });
        typeOfRingtone= (RadioGroup) findViewById(R.id.typeOfRingtoneRadioGroup);
        nameOfTrack = (TextView) findViewById(R.id.nameOfTrack);
        labelAudio = (TextView) findViewById(R.id.labelAudio);
        if (!musicPreferences.getBoolean("isOtherRingtone",false))
            nameOfTrack.setText("No track");
        else
            nameOfTrack.setText(musicPreferences.getString("ringtoneAuthor","")+" - "+musicPreferences.getString("ringtoneName",""));
        typeOfRingtone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case  R.id.defaultRingtone:
                    musicPreferences.edit().putString("typeOfRingtone","default").commit();
                        otherRingtonesButton.setEnabled(false);
                        nameOfTrack.setEnabled(false);
                        labelAudio.setEnabled(false);
                        break;
                    case R.id.otherRingtone:
                        musicPreferences.edit().putString("typeOfRingtone","other").commit();
                        if (!musicPreferences.getBoolean("isOtherRingtone", false)) {
                            startActivityForResult(new Intent(MusicSettingsActivity.this, ListOfRingtonesActivity.class),LIST_OF_RINGTONES);
                        }
                        else{
                        otherRingtonesButton.setEnabled(true);
                        nameOfTrack.setEnabled(true);
                        labelAudio.setEnabled(true);
                        }
                        break;
                }
            }
        });
        if (musicPreferences.getString("typeOfRingtone","default").equals("default")){
            RadioButton radioButton = (RadioButton) findViewById(R.id.defaultRingtone);
            musicPreferences.edit().putString("typeOfRingtone","default").commit();
            otherRingtonesButton.setEnabled(false);
            nameOfTrack.setEnabled(false);
            labelAudio.setEnabled(false);
            radioButton.setChecked(true);
        }
        else {
            RadioButton radioButton = (RadioButton) findViewById(R.id.otherRingtone);
            musicPreferences.edit().putString("typeOfRingtone","other").commit();
            nameOfTrack.setEnabled(true);
            labelAudio.setEnabled(true);
            radioButton.setChecked(true);
        }

    }

    public void start(){
        if (isPause){
            mediaPlayer.start();
        }
        else{
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.seekTo(0);
                mp.start();
            }
        });
        }
        startBtn.setEnabled(false);
        pauseBtn.setEnabled(true);
        isPause = false;
    }

    public void pause(){
        isPause = true;
        mediaPlayer.pause();
        pauseBtn.setEnabled(false);
        startBtn.setEnabled(true);
    }

    public void stop(){
        isPause = false;
        mediaPlayer.stop();
        startBtn.setEnabled(true);
    }

    public void createMediaPlayer(){
        if (!musicPreferences.getBoolean("isOtherRingtone",false))
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        else{
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(this, Uri.parse(musicPreferences.getString("ringtonePath","")));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            musicPreferences.edit().putString("ringtonePath",data.getStringExtra("path"))
                    .putString("ringtoneName",data.getStringExtra("name"))
                    .putString("ringtoneAuthor",data.getStringExtra("author")).putBoolean("isOtherRingtone",true).commit();
            nameOfTrack.setText(musicPreferences.getString("ringtoneAuthor","")+" - "+musicPreferences.getString("ringtoneName",""));
            otherRingtonesButton.setEnabled(true);
            nameOfTrack.setEnabled(true);
            labelAudio.setEnabled(true);
            mediaPlayer.release();
            createMediaPlayer();
        }
        if (resultCode == RESULT_CANCELED) {
            if (!musicPreferences.getBoolean("isOtherRingtone",false)){
                RadioButton radioButton = (RadioButton) findViewById(R.id.defaultRingtone);
                radioButton.setChecked(true);
            }
        }
    }
}
