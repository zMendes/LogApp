package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.LinkedList;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    public static String mFileName;
    public MediaRecorder mRecorder;
    ImageView audilog;
    ImageView tag;
    ImageView record;
    Chronometer chrono;
    ImageView change_mode;
    View background;
    String mode = "Entrevista";
    String timestamp;
    LinkedList<Audio> audios = new LinkedList<>();
    String titleStr;
    String lastPath;
    boolean recording = false;
    boolean entrevista = true;
    private AudioDatabase audioDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

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
            Intent intent = new Intent(this, MenuAudioActivity.class);
            startActivity(intent);
        });

        tag.setOnClickListener(view -> openDialog());


        record.setOnClickListener(view -> {

            if (checkPermissions()) {
                //start recording
                if (recording) {
                    mRecorder.stop();
                    mRecorder.release();
                    invert();
                    Toast.makeText(getApplicationContext(), "Recording stopped", Toast.LENGTH_LONG).show();
                    chrono.stop();
                    takePicture();
                    record.setImageResource(R.drawable.ic_fiber_manual_record_red_24dp);
                    for (Audio audio : audios) {
                        new InsertTask(RecordActivity.this, audio).execute();
                    }

                } else {
                    openAudioDialog();
                }
            } else {
                requestPermissions();
            }

        });

        change_mode.setOnClickListener(view -> {
            if (entrevista) {
                entrevista = false;
                mode = "Teste";
                change_mode.setImageResource(R.drawable.ic_autorenew_white_24dp);
                tag.setImageResource(R.drawable.ic_add_circle_outline_blue_24dp);
                background.setBackgroundColor(Color.parseColor("#AAA5A5"));
                Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
            } else {
                mode = "Entrevista";
                entrevista = true;
                change_mode.setImageResource(R.drawable.ic_autorenew_black_24dp);
                tag.setImageResource(R.drawable.ic_add_circle_outline_green_24dp);
                background.setBackgroundColor(Color.parseColor("#F5F2F2"));
                Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
            }


        });


    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) == null) {
            return;
        }


        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file;
        try {
            file = File.createTempFile("image", ".jpg", directory);
        } catch (IOException exception) {
            file = null;
        }
        if (file == null) {
            return;
        }

        lastPath = file.getAbsolutePath();

        Uri uri = FileProvider.getUriForFile(
                this,
                "br.edu.al.leonardomm4.logapp",
                file
        );

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
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
        timestamp = "" + ((SystemClock.elapsedRealtime() - chrono.getBase()) / 1000);

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

    public void dialogOk(String tag, String texto) {
        Toast.makeText(this, " Tag adicionada.", Toast.LENGTH_SHORT).show();

        Audio audio = new Audio(0, titleStr, mode, tag, timestamp, texto, null);
        audios.add(audio);
    }

    public void openAudioDialog() {
        AudioNameDialog audioDialog = new AudioNameDialog(this);

        audioDialog.show(getSupportFragmentManager(), "Nome do audio");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(this, "Nice", Toast.LENGTH_SHORT).show();

            Uri uri = Uri.fromFile(new File(lastPath));

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException exception) {
                bitmap = null;
            }

            if (bitmap != null) {
                System.out.println(bitmap);
                Toast.makeText(this, "Imagem salva", Toast.LENGTH_SHORT).show();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();
                addPicDatabase(bArray);
            }
        }
    }

    private void addPicDatabase(byte[] blob) {
        Audio audio = new Audio(0,titleStr,mode,null,null,null, blob );
        new InsertTask(RecordActivity.this, audio).execute();


    }

    public static class InsertTask extends AsyncTask<Void, Void, Boolean> {

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
}

