package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView audio_button;
    ImageView audio_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio_button = findViewById(R.id.audio);
        audio_log = findViewById(R.id.audios);

        audio_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        });

        audio_log.setOnClickListener(view -> {
            Intent intent = new Intent(this, AudioLogActivity.class);
            startActivity(intent);
        });
    }
}
