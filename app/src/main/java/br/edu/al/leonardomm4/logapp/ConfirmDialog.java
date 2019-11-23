package br.edu.al.leonardomm4.logapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmDialog extends AppCompatDialogFragment {

    private final RecordActivity recordActivity;

    public ConfirmDialog(RecordActivity recordActivity){
        this.recordActivity = recordActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_dialog, null);



        builder.setView(view)
                .setMessage("Valide sua entrevista tirando uma foto do local.")
                .setTitle("Validação")
                .setPositiveButton("Ok", ((dialogInterface, i) -> recordActivity.takePicture()));

        return builder.create();
    }
}
