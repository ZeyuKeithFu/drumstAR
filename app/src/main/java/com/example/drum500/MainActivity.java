package com.example.drum500;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    // ***************** Set global variables ***************** //

    // Mediaplayer parameters
    int id;
    String padID;
    int resourceID;
    Map<String, Boolean> stopped = new HashMap() {{
        put("congal", false);
        put("congam", false);
        put("congah", false);
        put("clap", false);
        put("openhihat", false);
        put("tom", false);
        put("snare", false);
        put("closedhihat", false);
        put("kick", false);
        put("cowbell", false);
        put("clave", false);
        put("maraca", false);
    }};

    MediaPlayer drumPlayer;

    // Node repeat parameters
    String[] repeatMap = {"Single", "Double", "Triple", "Quartic"};
    int bpm = 40;
    int repeat = 1;
    double interval = 60 / (double)bpm / (double)repeat * 1000;

    // Record, save
    boolean recording = false;

    // Switches
    boolean nr = false;
    boolean fl = false;

    // UI elements
    Button switchMode;
    Button record;
    SeekBar bpmBar;
    SeekBar repeatBar;
    ToggleButton nodeRepeat;
    ToggleButton fullLevel;
    Button pad1;
    Button pad2;
    Button pad3;
    Button pad4;
    Button pad5;
    Button pad6;
    Button pad7;
    Button pad8;
    Button pad9;
    Button pad10;
    Button pad11;
    Button pad12;
    TextView currentBPM;
    TextView currentRepeat;


    // ***************** Set onTouch method ***************** //

    View.OnTouchListener playSample = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            // Set pressure sensitivity
//            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//            int maxVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            float currentVolumn = event.getPressure() * (float)maxVolumn;
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)currentVolumn, 0);

            // Set node repeat
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                // Test pressure
                Log.i("Info", "pressed");

                id = v.getId();
                padID = v.getResources().getResourceEntryName(id);
                resourceID = getResources().getIdentifier(padID, "raw", getPackageName());
                stopped.put(padID, false);

                if (nr) {

                    new Thread() {
                        public void run() {

                            while (!stopped.get(padID)) {
                                try {
                                    drumPlayer = MediaPlayer.create(v.getContext(), resourceID);
                                    drumPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            mp.reset();
                                            mp.release();
                                        }
                                    });
                                    if (drumPlayer.isPlaying()) {
                                        drumPlayer.reset();
                                        drumPlayer.release();
                                        drumPlayer.start();
                                    } else {
                                        drumPlayer.start();
                                    }
                                    Thread.sleep((long) interval);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                } else {
                    drumPlayer = MediaPlayer.create(v.getContext(), resourceID);
                    drumPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            mp.release();
                        }
                    });
                    if (drumPlayer.isPlaying()) {
                        drumPlayer.reset();
                        drumPlayer.release();
                        drumPlayer.start();
                    } else {
                        drumPlayer.start();
                    }
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                Log.i("Info", "released");
                stopped.put(padID, true);
            }

            return false;
        }
    };


    // ***************** Set onCreate activity ***************** //

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide top bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set UI elements
        switchMode = (Button) findViewById(R.id.modeButton);
        record = (Button) findViewById(R.id.recordButton);
        nodeRepeat = (ToggleButton) findViewById(R.id.repeatButton);
        fullLevel = (ToggleButton) findViewById(R.id.fullButton);
        pad1 = (Button) findViewById(R.id.congal);
        pad2 = (Button) findViewById(R.id.congam);
        pad3 = (Button) findViewById(R.id.congah);
        pad4 = (Button) findViewById(R.id.clap);
        pad5 = (Button) findViewById(R.id.openhihat);
        pad6 = (Button) findViewById(R.id.tom);
        pad7 = (Button) findViewById(R.id.snare);
        pad8 = (Button) findViewById(R.id.closedhihat);
        pad9 = (Button) findViewById(R.id.kick);
        pad10 = (Button) findViewById(R.id.cowbell);
        pad11 = (Button) findViewById(R.id.clave);
        pad12 = (Button) findViewById(R.id.maraca);
        bpmBar = (SeekBar) findViewById(R.id.bpmBar);
        repeatBar = (SeekBar) findViewById(R.id.repeatTimes);
        currentBPM = (TextView) findViewById(R.id.bpmText);
        currentRepeat = (TextView) findViewById(R.id.repeatText);


        // Set bpm bar
        bpmBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bpm = progress + 40;
                interval = 60 / (double)bpm / (double)repeat * 1000;
                currentBPM.setText("BPM : " + Integer.toString(bpm));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        // Set repeat times bar
        repeatBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                repeat = progress + 1;
                interval = 60 / (double)bpm / (double)repeat * 1000;
                currentRepeat.setText("Repeat : " + repeatMap[progress]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        // Set node repeat function
        nodeRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nr = true;
                } else {
                    nr = false;
                }
            }
        });

        pad1.setOnTouchListener(playSample);
        pad2.setOnTouchListener(playSample);
        pad3.setOnTouchListener(playSample);
        pad4.setOnTouchListener(playSample);
        pad5.setOnTouchListener(playSample);
        pad6.setOnTouchListener(playSample);
        pad7.setOnTouchListener(playSample);
        pad8.setOnTouchListener(playSample);
        pad9.setOnTouchListener(playSample);
        pad10.setOnTouchListener(playSample);
        pad11.setOnTouchListener(playSample);
        pad12.setOnTouchListener(playSample);


        // Set Full Level
        fullLevel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fl = true;
                } else {
                    fl = false;
                }
            }
        });


        // Set recording function
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    recording = false;
                    record.setText("●");
                } else {
                    recording = true;
                    record.setText("■");
                }
            }
        });


        // Switch to AR mode
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ArActivity.class));
            }
        });

    }
}