package br.edu.al.leonardomm4.logapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.File;
import java.util.Objects;

public class AudioNameDialog extends AppCompatDialogFragment {

    private EditText title;

    private RecordActivity recordActivity;

    public AudioNameDialog(RecordActivity recordActivity){
        this.recordActivity  = recordActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.audio_dialog, null);
        title = view.findViewById(R.id.title);

        builder.setView(view).setTitle("Título").setNegativeButton("Cancel", ((dialogInterface, i) -> {}))
                .setPositiveButton("Ok", ((dialogInterface, i) -> {
                    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logApp" + "/" + title.getText().toString() + ".3gp";
                    recordActivity.mRecorder.setOutputFile(mFileName);
                    recordActivity.mRecorder.stop();
                    recordActivity.mRecorder.release();
                    for (Audio audio: recordActivity.audios) {
                        audio.setAudioName(title.getText().toString());
                        //System.out.println(audio.getAudioName() + "          TESTE     " + audio.getTimestamp());
                        new RecordActivity.InsertTask(recordActivity, audio).execute();
                    }

        }));



        return builder.create();



    }
}
