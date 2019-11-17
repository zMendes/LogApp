package br.edu.al.leonardomm4.logapp;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Audio")
public class Audio implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "audioName")
    private String audioName;

    @ColumnInfo(name = "Mode")
    @Nullable
    private String mode;
    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "timestamp")
    @Nullable
    private String timestamp;
    @ColumnInfo(name = "comment")
    @Nullable
    private String comment;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    @Nullable
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Audio(int id, String audioName, String mode, String tag, String timestamp, String comment, byte[] image) {
        this.id = id;
        this.audioName = audioName;
        this.mode = mode;
        this.tag = tag;
        this.timestamp = timestamp;
        this.comment = comment;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}