package br.edu.al.leonardomm4.logapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TagAdapter extends BaseAdapter {

    Activity context;

    List<Audio> audios;

    private static LayoutInflater inflater= null;

    public  TagAdapter(Activity context, List<Audio> audios){
        this.audios = audios;
        this.context = context;
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
        Audio audio     = audios.get(i);
        tag.setText(audio.getTag());
        timestamp.setText(audio.getTimestamp());
        return  itemView;
    }
}
