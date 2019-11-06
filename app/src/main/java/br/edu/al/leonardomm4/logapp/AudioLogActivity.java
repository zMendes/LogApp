package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioLogActivity extends AppCompatActivity {

    File file;

    AudioDatabase audioDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_log);
        ListView listView = findViewById(R.id.lista);
        List<String> lista = new ArrayList<>();
        audioDatabase = AudioDatabase.getInstance(AudioLogActivity.this);

        File directory = Environment.getExternalStorageDirectory();
        file = new File( directory + "/logApp" );
        File list[] = file.listFiles();

        for (File value : list) {
            lista.add(value.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, lista);
        listView.setAdapter(adapter); //Set all the file in the list.


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ListenAudio.class);
            System.out.println(parent.getItemAtPosition(position).toString());
            intent.putExtra("fileName", parent.getItemAtPosition(position).toString());
            startActivity(intent);
        });

    }



}
