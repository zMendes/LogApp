package br.edu.al.leonardomm4.logapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListenAudio extends AppCompatActivity {

    ImageView play;
    ImageView forward;
    ImageView replay;
    TextView title;
    MediaPlayer mediaPlayer;
    ImageView image;

    AudioDatabase audioDatabase;
    //ListView listView;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;

    RecyclerView recyclerView;


    private TextView maxx;
    private TextView chrono;
    DateFormat formatter = new SimpleDateFormat("mm:ss", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio);
        play = findViewById(R.id.play);
        forward = findViewById(R.id.forward);
        replay = findViewById(R.id.replay);
        title = findViewById(R.id.title);
        seekBar = findViewById(R.id.seek);
        chrono = findViewById(R.id.chrono);
        maxx = findViewById(R.id.max);
        image = findViewById(R.id.pic);

        mediaPlayer = new MediaPlayer();
        audioDatabase = AudioDatabase.getInstance(ListenAudio.this);
        handler = new Handler();


        List<Audio> lista = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);

        Intent intent = getIntent();

        String id = intent.getStringExtra("fileName");

        String audioId=  id.substring(0,id.length()-4);
        System.out.println(audioId+ "id ringht");
        System.out.println(id + "nome do audioTag");
        List<Audio> tags = audioDatabase.dao().getTags(audioId);
        title.setText(audioId);

        for (Audio audio: tags){
            lista.add(audio);
        }


        RecyclerAdapter adapter = new RecyclerAdapter(this, lista, mediaPlayer, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(layoutManager);



            String  path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/logApp/" + id;
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                seekBar.setMax(mediaPlayer.getDuration());
                String max= formatter.format(new Date(mediaPlayer.getDuration()));
                maxx.setText(max);
                chrono.setText("00:00");


        } catch (IOException e) {
            e.printStackTrace();
        }

        play.setOnClickListener(view -> {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            } else {
                mediaPlayer.start();
                changeSeekBar();
                play.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

            }
        });

        forward.setOnClickListener(view -> {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
        });

        replay.setOnClickListener(view -> {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void changeSeekBar() {
        seekBar.setProgress((mediaPlayer.getCurrentPosition()));

        if (mediaPlayer.isPlaying()){
            runnable = () -> {
                changeSeekBar();
                String text = formatter.format(new Date(mediaPlayer.getCurrentPosition()));
                chrono.setText(text);

            };

            handler.postDelayed(runnable, 10);
        } else{
            play.setImageResource(R.drawable.ic_play_arrow_black_24dp);}
    }
}
