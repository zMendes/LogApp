package br.edu.al.leonardomm4.logapp;


import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Activity context;
    List<Audio> audios;
    MediaPlayer mediaPlayer;
    DateFormat formatter;
    ListenAudio listenAudio;

    public RecyclerAdapter(Activity context, List<Audio> audios, MediaPlayer mediaPlayer, ListenAudio listenAudio) {
        this.context = context;
        this.audios = audios;
        this.mediaPlayer = mediaPlayer;
        formatter = new SimpleDateFormat("mm:ss", Locale.US);
        this.listenAudio = listenAudio;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        Audio audio = audios.get(i);

        holder.tag.setText(audio.getTag());

        String text = formatter.format(new Date(Integer.parseInt(audio.getTimestamp())*1000));
        holder.timestamp.setText(text);
        holder.comment.setText(audio.getComment());
        if (audio.getMode().equals("Entrevista")){
            holder.card.setBackgroundColor(Color.parseColor("#03b1fc"));}
        else{
            holder.card.setBackgroundColor(Color.parseColor("#03fc62"));
        }
        holder.itemView.setOnClickListener(view1 ->{

            listenAudio.play.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);


            if (mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(Integer.parseInt(audio.getTimestamp())*1000);
            }
            else {
                mediaPlayer.start();
                listenAudio.changeSeekBar();
                mediaPlayer.seekTo(Integer.parseInt(audio.getTimestamp())*1000);
            }
        });

    }


    @Override
    public int getItemCount() {
        return audios.size();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView tag;
        TextView timestamp;
        TextView comment;
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);
            timestamp = itemView.findViewById(R.id.timestamp);
            comment = itemView.findViewById(R.id.comment);
            card = itemView.findViewById(R.id.card);
        }
    }
}
