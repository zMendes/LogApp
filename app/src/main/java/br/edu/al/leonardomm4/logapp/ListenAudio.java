package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ListenAudio extends AppCompatActivity {

    Button play;
    MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio);
        play = findViewById(R.id.play);
        mediaPlayer = new MediaPlayer();


        Intent intent = getIntent();

        String id = intent.getStringExtra("fileName");


        String  path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/logApp/" + id;
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        play.setOnClickListener(view -> {
            mediaPlayer.start();
        });
    }
}
