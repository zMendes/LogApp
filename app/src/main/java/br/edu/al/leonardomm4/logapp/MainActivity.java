package br.edu.al.leonardomm4.logapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button audio_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio_button = findViewById(R.id.audio);

        audio_button.setOnClickListener(view ->{
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        });
    }
}
