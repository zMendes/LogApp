package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
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


    private AudioDatabase audioDatabase;
    private Audio audio;


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

        tag = findViewById(R.id.tag);
        change_mode = findViewById(R.id.change_mode);


        chrono = findViewById(R.id.chronometer);
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logApp");
        directory.mkdirs();

        Date date = new Date();
        audioDatabase = AudioDatabase.getInstance(RecordActivity.this);
        String tag_string = "";


        audilog.setOnClickListener(view -> {
            Intent intent = new Intent(this, AudioLogActivity.class);
            startActivity(intent);
        });

        tag.setOnClickListener(view -> {
            openDialog();
        });


        record.setOnClickListener(view -> {
            if (title.getText().toString().isEmpty()) {
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logApp" + "/" + date.toString() + "audio.3gp";
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
            if (entrevista==true){
                entrevista = false;
                change_mode.setImageResource(R.drawable.ic_autorenew_green_24dp);
            }
            else{
                entrevista = true;
                change_mode.setImageResource(R.drawable.ic_autorenew_black_24dp);
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

    private void setResult(Audio audio, int flag) {
        setResult(flag, new Intent().putExtra("audio", audio));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<RecordActivity> activityReference;
        private Audio audio;

        // only retain a weak reference to the activity
        InsertTask(RecordActivity context, Audio audio) {
            activityReference = new WeakReference<>(context);
            this.audio = audio;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().audioDatabase.dao().insertAudio(audio);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(audio, 1);
            }
        }

    }


    public void dialogOk(String tag) {
        Toast.makeText(this, tag + chrono.getText(), Toast.LENGTH_SHORT).show();
        audio = new Audio(0, title.getText().toString(), "Entrevista", tag, chrono.getText().toString());
        new InsertTask(RecordActivity.this, audio).execute();
    }

}

