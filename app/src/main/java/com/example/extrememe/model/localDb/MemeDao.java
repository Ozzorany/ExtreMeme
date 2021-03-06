package com.example.extrememe.model.localDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.extrememe.model.Meme;

import java.util.List;

@Dao
public interface MemeDao {

    @Query("select * from Meme")
    LiveData<List<Meme>> getAllMemes();

    @Query("select * from Meme where userId = :userId")
    LiveData<List<Meme>> getAllMemesByUserId(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Meme... memes);

    @Update
    void updateMemes(Meme... memes);

    @Delete
    void delete(Meme meme);
}
