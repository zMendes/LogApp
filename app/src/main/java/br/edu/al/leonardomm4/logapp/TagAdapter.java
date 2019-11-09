package br.edu.al.leonardomm4.logapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TagAdapter extends BaseAdapter {

    Activity context;

    List<Audio> audios;

    MediaPlayer mediaPlayer;

    private static LayoutInflater inflater= null;

    public  TagAdapter(Activity context, List<Audio> audios, MediaPlayer mediaPlayer){
        this.audios = audios;
        this.context = context;
        this.mediaPlayer = mediaPlayer;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return audios.size();
    }

    @Override
    public Object getItem(int i) {
        return audios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView   = (itemView ==null) ? inflater.inflate(R.layout.list_item, null): itemView;
        TextView tag = itemView.findViewById(R.id.tag);
        TextView timestamp = itemView.findViewById(R.id.timestamp);
        TextView comment = itemView.findViewById(R.id.comment);
        Audio audio     = audios.get(i);
        tag.setText(audio.getTag());
        timestamp.setText("00:"+audio.getTimestamp() + " seg");
        comment.setText(audio.getComment());

        if (audio.getMode().equals("Entrevista")){
        itemView.setBackgroundColor(Color.parseColor("#03b1fc"));}
        else{
            itemView.setBackgroundColor(Color.parseColor("#03fc62"));
        }

        itemView.setOnClickListener(view1 ->{
            if (mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(Integer.parseInt(audio.getTimestamp())*1000);
            }
            else {
                mediaPlayer.start();
                mediaPlayer.seekTo(Integer.parseInt(audio.getTimestamp())*1000);
            }
        });

        return  itemView;
    }
}
