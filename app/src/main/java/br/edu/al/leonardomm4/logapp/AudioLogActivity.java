package br.edu.al.leonardomm4.logapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioLogActivity extends AppCompatActivity {

    private List<String> lista;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_log);
        ListView listView = (ListView) findViewById(R.id.lista);
        lista = new ArrayList<String>();

        File directory = Environment.getExternalStorageDirectory();
        file = new File( directory + "/logApp" );
        File list[] = file.listFiles();

        for( int i=0; i< list.length; i++)
        {
            lista.add( list[i].getName() );
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
