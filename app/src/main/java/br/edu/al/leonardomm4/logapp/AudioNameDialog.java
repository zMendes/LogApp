package br.edu.al.leonardomm4.logapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.File;
import java.io.IOException;
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
                    recordActivity.titleStr = title.getText().toString();
                    recordActivity.mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logApp" + "/" + recordActivity.titleStr+ ".3gp";
                    recordActivity.mRecorder = new MediaRecorder();
                    recordActivity.chrono.setBase(SystemClock.elapsedRealtime());
                    recordActivity.mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recordActivity.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recordActivity.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recordActivity.mRecorder.setOutputFile(recordActivity.mFileName);
                    try {
                        recordActivity.mRecorder.prepare();
                        recordActivity.mRecorder.start();
                        recordActivity.chrono.start();
                        recordActivity.record.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
                    } catch (IOException e) {
                        Log.e("AudioRecording", "prepare() failed");
                    }
                    Toast.makeText(recordActivity.getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                    recordActivity.invert();


                }));



        return builder.create();



    }
}
