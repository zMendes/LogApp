package br.edu.al.leonardomm4.logapp;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;


@Dao
public interface AudioDAO {

    @Query("SELECT * FROM audio")
    List<Audio> getAudioList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAudio(Audio audio);

    @Delete
    void deleteAudio(Audio audio);

    @Query("SELECT * FROM audio WHERE audioName = :name")
    List<Audio> getTags(String name);
}
