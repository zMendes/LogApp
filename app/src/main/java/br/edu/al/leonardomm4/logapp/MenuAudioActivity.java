package br.edu.al.leonardomm4.logapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAudioActivity extends AppCompatActivity {

    TextView audios;
    TextView videos;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_audio);

        audios = findViewById(R.id.textView1);
        videos = findViewById(R.id.textView2);
        menu = findViewById(R.id.audios);

        menu.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        });

        audios.setOnClickListener(view -> {
            Intent intent = new Intent(this, AudioLogActivity.class);
            startActivity(intent);
        });

        videos.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        });
    }
}

