package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    EditText title;
    ImageView audilog;
    ImageView tag;
    ImageView record;
    Chronometer chrono;
    ImageView change_mode;
    View background;
    String mode = "Entrevista";
    String timestamp;
    LinkedList<Audio> audios = new LinkedList<>();

    private AudioDatabase audioDatabase;


    private MediaRecorder mRecorder;
    private static String mFileName;
    boolean recording = false;
    boolean entrevista = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        title = findViewById(R.id.title);
        record = findViewById(R.id.record);
        audilog = findViewById(R.id.audiolog);
        background = findViewById(R.id.background);

        tag = findViewById(R.id.tag);
        change_mode = findViewById(R.id.change_mode);


        chrono = findViewById(R.id.chronometer);
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logApp");
        directory.mkdirs();

        Date date = new Date();
        audioDatabase = AudioDatabase.getInstance(RecordActivity.this);


        audilog.setOnClickListener(view -> {
            Intent intent = new Intent(this, AudioLogActivity.class);
            startActivity(intent);
        });

        tag.setOnClickListener(view -> openDialog());



        record.setOnClickListener(view -> {
            if (title.getText().toString().isEmpty()) {
                UUID uuid = UUID.randomUUID();
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logApp" + "/" +  uuid.toString()+ "audio.3gp";
            } else {
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logApp" + "/" + title.getText().toString() + ".3gp";
            }
            if (checkPermissions()) {
                //start recording
                if (recording) {
                    mRecorder.stop();
                    mRecorder.release();
                    invert();
                    Toast.makeText(getApplicationContext(), "Recording stopped", Toast.LENGTH_LONG).show();
                    chrono.stop();
                    record.setImageResource(R.drawable.ic_fiber_manual_record_red_24dp);
                    for (Audio audio: audios) {
                        new InsertTask(RecordActivity.this, audio).execute();
                    }

                } else {

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
                        record.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
                    } catch (IOException e) {
                        Log.e("AudioRecording", "prepare() failed");
                    }
                    Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                    invert();
                }
            } else {
                requestPermissions();
            }

        });

        change_mode.setOnClickListener(view ->{
            if (entrevista){
                entrevista = false;
                mode = "Teste";
                change_mode.setImageResource(R.drawable.ic_autorenew_white_24dp);
                tag.setImageResource(R.drawable.ic_add_circle_outline_blue_24dp);
                background.setBackgroundColor(Color.parseColor("#AAA5A5"));
                Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
            }
            else{
                mode = "Entrevista";
                entrevista = true;
                change_mode.setImageResource(R.drawable.ic_autorenew_black_24dp);
                tag.setImageResource(R.drawable.ic_add_circle_outline_green_24dp);
                background.setBackgroundColor(Color.parseColor("#F5F2F2"));
                Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
            }


                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    private void openDialog() {
        TagDialog tag = new TagDialog(this);
        timestamp = ""+ ((SystemClock.elapsedRealtime()- chrono.getBase())/1000);

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

    public void invert() {
        recording = !recording;
    }


    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<RecordActivity> activityReference;
        private Audio audio;

        InsertTask(RecordActivity context, Audio audio) {
            activityReference = new WeakReference<>(context);
            this.audio = audio;
        }

        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().audioDatabase.dao().insertAudio(audio);
            return true;
        }

    }


    public void dialogOk(String tag, String texto) {
        Toast.makeText(this, " Tag adicionada.", Toast.LENGTH_SHORT).show();

        Audio audio = new Audio(0, title.getText().toString(), mode, tag, timestamp, texto);
        audios.add(audio);
    }

}

