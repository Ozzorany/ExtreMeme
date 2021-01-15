package com.example.extrememe.model.localDb;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.extrememe.model.Meme;

import java.util.List;

@Dao
public interface MemeDao {

    @Query("select * from Meme")
    List<Meme> getAllMemes();

    @Query("select * from Meme where userId = :userId")
    MutableLiveData<List<Meme>> getAllMemesByUserId(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Meme... memes);

    @Delete
    void delete(Meme meme);
}
