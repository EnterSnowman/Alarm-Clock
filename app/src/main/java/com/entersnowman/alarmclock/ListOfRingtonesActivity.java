package com.entersnowman.alarmclock;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ListOfRingtonesActivity extends AppCompatActivity {
    RecyclerView listOfRingtones;
    ArrayList<Track> tracks;
    RingtoneAdapter ringtoneAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_ringtones);
        listOfRingtones = (RecyclerView) findViewById(R.id.listOfRingtones);
        tracks = new ArrayList<Track>();
        setTracks();
        ringtoneAdapter = new RingtoneAdapter(tracks,this,this);
        listOfRingtones.setLayoutManager(new LinearLayoutManager(this));
        listOfRingtones.setAdapter(ringtoneAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_close);

    }

    private void setTracks(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        tracks = new ArrayList<Track>();

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast() ){
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    Track track = new Track(path,artist,title);

                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {
                        tracks.add(track);
                    }
                }

            }

            // print to see list of mp3 files
            for( Track file : tracks) {
                Log.i("TAG", file.getPath());
            }

        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
