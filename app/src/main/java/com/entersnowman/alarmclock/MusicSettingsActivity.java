package com.entersnowman.alarmclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MusicSettingsActivity extends AppCompatActivity {
    Button otherRingtonesButton;
    RadioGroup typeOfRingtone;
    RadioButton defaultRingtone;
    RadioButton otherRingtone;
    SharedPreferences musicPreferences;
    TextView nameOfTrack;
    TextView labelAudio;
    public final static int LIST_OF_RINGTONES = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_settings);
        musicPreferences = getSharedPreferences("listOfMusicPreferences",MODE_PRIVATE);
        otherRingtonesButton = (Button) findViewById(R.id.setRingtoneButton);
        otherRingtonesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        }
        if (resultCode == RESULT_CANCELED) {
            if (!musicPreferences.getBoolean("isOtherRingtone",false)){
                RadioButton radioButton = (RadioButton) findViewById(R.id.defaultRingtone);
                radioButton.setChecked(true);
            }
        }
    }
}
