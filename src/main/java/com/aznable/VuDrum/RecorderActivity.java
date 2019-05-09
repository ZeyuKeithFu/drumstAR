package com.aznable.VuDrum;

import android.Manifest;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class RecorderActivity extends AppCompatActivity {

    ListView filesList;
    Button back;
    ArrayList<String> tracks;
    ArrayList<String> tracksPath;
    ArrayAdapter arrayAdapter;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recorder);
        // Hide top bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //check for permission
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        }

        // Set UI
        filesList = (ListView) findViewById(R.id.fileList);
        back = (Button) findViewById(R.id.backButton);

        // Back to main
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecorderActivity.this, MainActivity.class));
                finish();
            }
        });

        // Read files
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString() + "/drumstAR";
        Log.i("Files", path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.i("Files", "Size: "+ files.length);

        // Set arrayAdapter
        tracks = new ArrayList<>();
        tracksPath = new ArrayList<>();
        tracks.clear();
        tracksPath.clear();

        for (int i = files.length-1; i >= 0; i--)
        {
            tracks.add(files[i].getName());
            tracksPath.add(path + "/" + files[i].getName());
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tracks);
        filesList.setAdapter(arrayAdapter);

        // Set onItemClick
        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File music = new File(tracksPath.get(position));
                String type = "audio/*";
                Uri musicUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    musicUri = FileProvider.getUriForFile(getApplicationContext(), "com.aznable.VuDrum.provider", music);
                } else {
                    musicUri = Uri.fromFile(music);
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(musicUri, type);
                startActivity(intent);
            }
        });

    }
}
