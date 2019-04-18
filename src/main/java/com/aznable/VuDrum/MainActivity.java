package com.aznable.VuDrum;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    // ************************* Set global variables ************************* //

    // Recording parameters
    MediaRecorder recorder;

    // Requesting permission to RECORD_AUDIO
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_WRITE_PERMISSION = 115;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    // Mediaplayer parameters
    int id;
    int maxVolumn;
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


    // ********************** Set onTouch playing method ********************** //

    View.OnTouchListener playSample = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            // Set node repeat
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                // Set pressure sensitivity
                float pressure = event.getPressure();
                float volumnToPlay = (float) maxVolumn * pressure;
                float volumn = (float) (1 - (Math.log((float) maxVolumn - volumnToPlay) / Math.log((float) maxVolumn)));
                Log.i("Volumn", Float.toString(volumnToPlay));


                // Set drum sound
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
                                    if (!fl)
                                        drumPlayer.setVolume(volumn, volumn);

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
                    if (!fl)
                        drumPlayer.setVolume(volumn, volumn);
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

                stopped.put(padID, true);
            }

            return false;
        }
    };


    // ************************* Set onCreate activity ************************* //

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide top bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Get max volumn
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

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


        // Set pads
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

                    recorder.stop();
                    recorder.release();
                    Toast.makeText(MainActivity.this, "Recording stopped.", Toast.LENGTH_SHORT).show();

                } else {
                    // Ask for permission
                    if(!hasPermissions(getApplicationContext(), permissions)){
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_WRITE_PERMISSION);
                    }
                    recording = true;
                    record.setText("■");

                    // Start recording
                    recorder = new MediaRecorder();

                    recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/drumstAR";
                    File storage_path = new File(file_path);
                    if (!storage_path.exists())
                        storage_path.mkdir();
                    recorder.setOutputFile(file_path + "/demo.3gp");

                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Recording failed!!!Check permission.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    recorder.start();
                    Toast.makeText(MainActivity.this, "Recording start...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Switch to instruction mode
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InstructionActivity.class));
                finish();
            }
        });
    }
}
