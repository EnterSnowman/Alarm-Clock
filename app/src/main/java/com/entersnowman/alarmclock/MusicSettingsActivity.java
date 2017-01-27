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

import java.util.ArrayList;
import java.util.List;

public class MusicSettingsActivity extends AppCompatActivity {
    Button otherRingtonesButton;
    RadioGroup typeOfRingtone;
    RadioButton defaultRingtone;
    RadioButton otherRingtone;
    SharedPreferences musicPreferences;
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
        typeOfRingtone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case  R.id.defaultRingtone:
                    musicPreferences.edit().putString("typeOfRingtone","default").commit();
                        break;
                    case R.id.otherRingtone:
                        musicPreferences.edit().putString("typeOfRingtone","other").commit();
                        break;
                }
            }
        });
        if (musicPreferences.getString("typeOfRingtone","default").equals("default")){
            RadioButton radioButton = (RadioButton) findViewById(R.id.defaultRingtone);
            musicPreferences.edit().putString("typeOfRingtone","default").commit();
            radioButton.setChecked(true);
        }
        else {
            RadioButton radioButton = (RadioButton) findViewById(R.id.otherRingtone);
            musicPreferences.edit().putString("typeOfRingtone","other").commit();
            radioButton.setChecked(true);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
