package com.example.extrememe.model.localDb;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.extrememe.MemesApplication;
import com.example.extrememe.model.Meme;

@Database(entities = {Meme.class}, version = 3)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract MemeDao memeDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MemesApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

