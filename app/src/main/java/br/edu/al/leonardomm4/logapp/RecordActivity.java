package br.edu.al.leonardomm4.logapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RecordActivity extends AppCompatActivity {

    Button audilog;
    Button tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        audilog = findViewById(R.id.audiolog);
        tag = findViewById(R.id.tag);

        audilog.setOnClickListener(view -> {
            Intent intent = new Intent(this, AudioLogActivity.class);
            startActivity(intent);
        });

        tag.setOnClickListener(view -> {
            openDialog();
        });


    }

    private void openDialog() {
        TagDialog tag = new TagDialog();
        tag.show(getSupportFragmentManager(), "Tag");
    }
}
