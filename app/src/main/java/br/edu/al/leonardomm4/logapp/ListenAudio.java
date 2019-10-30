package br.edu.al.leonardomm4.logapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ListenAudio extends AppCompatActivity {

    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio);
        text = findViewById(R.id.oi);

        Intent intent = getIntent();


        String id = intent.getStringExtra("fileName");

        text.setText(id);
    }
}
