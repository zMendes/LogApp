package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListenAudio extends AppCompatActivity {

    Button play;
    MediaPlayer mediaPlayer;

    AudioDatabase audioDatabase;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio);
        play = findViewById(R.id.play);
        listView =  findViewById(R.id.lista);

        mediaPlayer = new MediaPlayer();
        audioDatabase = AudioDatabase.getInstance(ListenAudio.this);

        List<Audio> lista = new ArrayList<>();



        Intent intent = getIntent();

        String id = intent.getStringExtra("fileName");

        String audioId=  id.substring(0,id.length()-4);
        System.out.println(audioId+ "id ringht");
        System.out.println(id + "nome do audioTag");
        List<Audio> tags = audioDatabase.dao().getTags(audioId);;
        for (Audio audio: tags){
            lista.add(audio);
            System.out.println("Dentro  do for de audios");
            System.out.println(audio.getAudioName() +" " + audio.getTag()+ "  " + audio.getTimestamp());
        }

        TagAdapter adapter = new TagAdapter(this, lista);

        //ArrayAdapter<   Audio> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);
        listView.setAdapter(adapter); //Set all the file in the list.

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
