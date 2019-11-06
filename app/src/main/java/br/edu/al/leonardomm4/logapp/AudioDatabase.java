package br.edu.al.leonardomm4.logapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Audio.class}, exportSchema = false, version = 1)
public abstract class AudioDatabase extends RoomDatabase {
    private String DB_NAME = "audio.db";

    public abstract AudioDAO dao();

    private static  AudioDatabase audioDB;

    public static AudioDatabase getInstance(Context context){
        if (audioDB ==null){
            audioDB = buildDatabaseInstance(context);
        }
        return audioDB;
    }
    private static AudioDatabase buildDatabaseInstance(Context context){
        return Room.databaseBuilder(context,AudioDatabase.class, "audio.db").allowMainThreadQueries().build();
    }

    public void cleanUp(){
        audioDB = null;
    }
    }
