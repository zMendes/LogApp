package br.edu.al.leonardomm4.logapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    ImageView audilog;
    ImageView tag;
    ImageView record;
    Chronometer chrono;

    private MediaRecorder mRecorder;
    private static String mFileName;
    boolean recording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        record = findViewById(R.id.record);
        audilog = findViewById(R.id.audiolog);
        tag = findViewById(R.id.tag);

        chrono = findViewById(R.id.chronometer);
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator   + "logApp");
        directory.mkdirs();

        mFileName= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator   + "logApp"+ "/" + UUID.randomUUID().toString() +"teskt.3gp";



        audilog.setOnClickListener(view -> {
            Intent intent = new Intent(this, AudioLogActivity.class);
            startActivity(intent);
        });

        tag.setOnClickListener(view -> {
            openDialog();
        });


        record.setOnClickListener(view -> {
            if (checkPermissions()){
                //start recording
                if (recording){
                    mRecorder.stop();
                    mRecorder.release();
                    invert();
                    Toast.makeText(getApplicationContext(), "Recording stopped", Toast.LENGTH_LONG).show();
                    chrono.stop();
                    record.setBackgroundColor(Color.parseColor("#000000"));

                }
                else {
                    mRecorder = new MediaRecorder();
                    chrono.setBase(SystemClock.elapsedRealtime());
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile(mFileName);
                    try {
                        mRecorder.prepare();
                        mRecorder.start();
                        chrono.start();
                        record.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    } catch (IOException e) {
                        Log.e("AudioRecording", "prepare() failed");
                    }
                    Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                    invert();
                }
            }
            else{
                requestPermissions();
            }

        });





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void openDialog() {
        TagDialog tag = new TagDialog();
        tag.show(getSupportFragmentManager(), "Tag");
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(RecordActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void invert(){
        recording = !recording;
    }
}
