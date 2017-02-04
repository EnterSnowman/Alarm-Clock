package com.entersnowman.alarmclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import java.io.IOException;


public class MusicSettingsActivity extends AppCompatActivity {
    Button otherRingtonesButton;
    RadioGroup typeOfRingtone;
    SharedPreferences musicPreferences;
    CrystalRangeSeekbar crystalRangeSeekbar;
    int mediaPlayerDuration;
    int correctDuration;
    boolean isPause;
    TextView nameOfTrack;
    TextView labelAudio;
    TextView leftRange;
    TextView rightRange;
    ImageButton startBtn;
    ImageButton pauseBtn;
    ImageButton stopBtn;
    public final static int LIST_OF_RINGTONES = 1;
    MediaPlayer mediaPlayer;
    int startSeekBar;
    int endSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_settings);
        musicPreferences = getSharedPreferences("listOfMusicPreferences",MODE_PRIVATE);
        otherRingtonesButton = (Button) findViewById(R.id.setRingtoneButton);
        leftRange = (TextView) findViewById(R.id.leftRange);
        rightRange = (TextView) findViewById(R.id.rightRange);
        startSeekBar = musicPreferences.getInt("startSeekBar",0);
        endSeekBar = musicPreferences.getInt("endSeekBar",1000);
        Log.d("music","startSeekBar = "+startSeekBar+" , "+"endSeekBar = "+endSeekBar);
        crystalRangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeOfPlaying);
        crystalRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                Log.d("music","value changed "+ millisToMinutesAndSeconds(minValue.intValue())+" "+millisToMinutesAndSeconds(maxValue.intValue()));
                leftRange.setText( millisToMinutesAndSeconds(minValue.intValue()));
                rightRange.setText(millisToMinutesAndSeconds(maxValue.intValue()));
                musicPreferences.edit().putInt("start",(int)(((float)minValue.intValue()/correctDuration)*mediaPlayerDuration))
                        .putInt("end",(int)(((float)maxValue.intValue()/correctDuration)*mediaPlayerDuration))
                        .putInt("startSeekBar",minValue.intValue())
                        .putInt("endSeekBar",maxValue.intValue())
                        .commit();
            }
        });
        createMediaPlayer();
        setRangesOfSeekBar();
        Log.d("music", String.valueOf(mediaPlayer.getDuration()));
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
                    musicPreferences.edit().putBoolean("isOtherRingtone",false).commit();
                        createMediaPlayer();
                        otherRingtonesButton.setEnabled(false);
                        nameOfTrack.setEnabled(false);
                        labelAudio.setEnabled(false);
                        break;
                    case R.id.otherRingtone:
                        musicPreferences.edit().putBoolean("isOtherRingtone",true).commit();
                        if (!musicPreferences.getBoolean("isOtherRingtone", false)) {
                            startActivityForResult(new Intent(MusicSettingsActivity.this, ListOfRingtonesActivity.class),LIST_OF_RINGTONES);
                        }
                        else{
                        otherRingtonesButton.setEnabled(true);
                        nameOfTrack.setEnabled(true);
                        labelAudio.setEnabled(true);
                            createMediaPlayer();
                        }
                        break;
                }
            }
        });
        if (!musicPreferences.getBoolean("isOtherRingtone",false)){
            RadioButton radioButton = (RadioButton) findViewById(R.id.defaultRingtone);
            otherRingtonesButton.setEnabled(false);
            nameOfTrack.setEnabled(false);
            labelAudio.setEnabled(false);
            radioButton.setChecked(true);
        }
        else {
            RadioButton radioButton = (RadioButton) findViewById(R.id.otherRingtone);
            nameOfTrack.setEnabled(true);
            labelAudio.setEnabled(true);
            radioButton.setChecked(true);
        }

    }



    public void start(){
        mediaPlayer.seekTo(musicPreferences.getInt("startSeekBar",0));
        mediaPlayer.start();
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
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(musicPreferences.getInt("startSeekBar",0));
            }
        });
        startBtn.setEnabled(true);
        pauseBtn.setEnabled(false);
    }

    public void createMediaPlayer(){
        if (!musicPreferences.getBoolean("isOtherRingtone",false))
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        else{
            if (mediaPlayer!=null)
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(this, Uri.parse(musicPreferences.getString("ringtonePath","")));
                mediaPlayer.prepareAsync();
                Log.d("music","SetDataSource "+Uri.parse(musicPreferences.getString("ringtonePath","")));
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
            startSeekBar = 0;
            Uri uri = Uri.parse(musicPreferences.getString("ringtonePath",""));
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(getApplicationContext(),uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int millSecond = Integer.parseInt(durationStr);
            endSeekBar = millSecond;
            mmr.release();
            setRangesOfSeekBar();

        }
        if (resultCode == RESULT_CANCELED) {
            if (!musicPreferences.getBoolean("isOtherRingtone",false)){
                RadioButton radioButton = (RadioButton) findViewById(R.id.defaultRingtone);
                radioButton.setChecked(true);
            }
        }
    }

    public String millisToMinutesAndSeconds(int millis){
        int seconds = (int) (millis / 1000) % 60 ;
        int minutes = (int) ((millis / (1000*60)) % 60);
        String zero;
        if (seconds < 10)
            zero ="0";
        else
            zero  ="";
        return Integer.toString(minutes)+":"+zero+Integer.toString(seconds);
    }

    public void setRangesOfSeekBar(){
        if (musicPreferences.contains("ringtonePath")) {
            Log.d("music", "setRanges");
            Uri uri = Uri.parse(musicPreferences.getString("ringtonePath", ""));
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(getApplicationContext(), uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int millSecond = Integer.parseInt(durationStr);
            mmr.release();
            mediaPlayerDuration = mediaPlayer.getDuration();
            correctDuration = millSecond;
            Log.d("music", "Dur = " + String.valueOf(millSecond));
            Log.d("music", "setMinMax");
            crystalRangeSeekbar.setMinValue(0);
            crystalRangeSeekbar.setMaxValue(millSecond);
            Log.d("music", "setSelectedMinMax");
            crystalRangeSeekbar.setMinStartValue(startSeekBar).setMaxStartValue(endSeekBar).apply();
            leftRange.setText(millisToMinutesAndSeconds(crystalRangeSeekbar.getSelectedMinValue().intValue()));
            rightRange.setText(millisToMinutesAndSeconds(crystalRangeSeekbar.getSelectedMaxValue().intValue()));
        }
    }
}
