package br.edu.al.leonardomm4.logapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class TagDialog extends AppCompatDialogFragment {

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
            Toast.makeText(getActivity(), "Tag adicionada com sucesso", Toast.LENGTH_SHORT).show();
        });
        builder.setSingleChoiceItems(tags, 0, null);
        return builder.create();
    }

}
