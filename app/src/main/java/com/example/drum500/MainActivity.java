package com.example.drum500;

import android.app.ActionBar;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    String padID;

    public void playSample(View view) {

        int id = view.getId();

        padID = view.getResources().getResourceEntryName(id);
        int resourceID = getResources().getIdentifier(padID, "raw", getPackageName());

        MediaPlayer phraseSpeaker = MediaPlayer.create(this, resourceID);
        phraseSpeaker.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View main = findViewById(R.id.freeView);
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        main.setSystemUiVisibility(uiOptions);

    }
}
