package com.example.extrememe.model.localDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.extrememe.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("select * from Category")
    LiveData<List<Category>> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    @Query("DELETE FROM Category")
    void deleteAll();
}
