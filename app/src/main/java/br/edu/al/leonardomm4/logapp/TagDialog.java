package br.edu.al.leonardomm4.logapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class TagDialog extends AppCompatDialogFragment {

    private String selected = "";
    private  RecordActivity recordActivity;

    public TagDialog(RecordActivity recordActivity){
        this.recordActivity = recordActivity;

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_tag, null);

        String[] tags = new String[]{"Pains", "Gains", "Explicação", "Teste"};

        builder.setView(view)
                .setTitle("Tags")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                }).setPositiveButton("ok", (dialogInterface, i) -> {
                    recordActivity.dialogOk(selected);

        });
        builder.setSingleChoiceItems(tags, 0, (a, i) -> selected = tags[i]);
        return builder.create();
    }



}
