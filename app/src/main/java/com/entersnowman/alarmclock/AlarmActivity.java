package com.entersnowman.alarmclock;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
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
    LoopRingTask loopRingTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        stopAlarm = (Button) findViewById(R.id.stopAlarm);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2,0);
        musicPrefs = getSharedPreferences("listOfMusicPreferences",MODE_PRIVATE);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("music", String.valueOf(mediaPlayer.getCurrentPosition()));
                //loopRingTask.cancel(true);
                mediaPlayer.stop();
                mediaPlayer.release();
                stopAlarm.setEnabled(false);
            }
        });
        loopRingTask = new LoopRingTask();
        loopRingTask.execute();

    }

    class LoopRingTask extends AsyncTask<Void,Integer,Void>{

        boolean isUpdate = true;
        Handler handler;
        Runnable runnable;
        @Override
        protected Void doInBackground(Void... params) {
            if (musicPrefs.getBoolean("isOtherRingtone",false))
            {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AlarmActivity.this, Uri.parse(musicPrefs.getString("ringtonePath","")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepareAsync();
                mediaPlayer.setVolume(0.5f,0.5f);
            }
            else {
                mediaPlayer = MediaPlayer.create(AlarmActivity.this, R.raw.alarm);
                mediaPlayer.setVolume(0.5f,0.5f);
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    isUpdate = true;
                    mediaPlayer.seekTo(musicPrefs.getInt("startSeekBar",0));
                    //int end = musicPrefs.getInt("end",mediaPlayer.getDuration());
                    Log.d("music","Start = "+musicPrefs.getInt("startSeekBar",0));
                    Log.d("music","End = "+musicPrefs.getInt("endSeekBar",mediaPlayer.getDuration()));
                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            Log.d("music","CurPos = "+mediaPlayer.getCurrentPosition());
                            publishProgress(mediaPlayer.getCurrentPosition());
                            if (isUpdate)
                                handler.postDelayed(this, 500);
                        }
                    };
                    handler.post(runnable);
                    mediaPlayer.start();
                    Log.d("music","CurPos = "+mediaPlayer.getCurrentPosition());


            };

        });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0]>musicPrefs.getInt("endSeekBar",0)){
                Log.d("music","There will stop");
                isUpdate = false;
                handler.removeCallbacks(runnable);
                mediaPlayer.stop();
                mediaPlayer.prepareAsync();
            }
        }
    }

}
