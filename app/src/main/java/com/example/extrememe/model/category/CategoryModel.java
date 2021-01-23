package com.example.extrememe.model.category;

import androidx.lifecycle.LiveData;

import com.example.extrememe.model.Category;
import com.example.extrememe.model.localDb.CategoryModelSql;

import java.util.List;

public class CategoryModel {
    public final static CategoryModel instance = new CategoryModel();
    CategoryModelFirebase categoryModelFirebase = new CategoryModelFirebase();
    private CategoryModelSql categoryModelSql = new CategoryModelSql();

    private CategoryModel() {
    }

    public LiveData<List<Category>> getLocalCategories() {
        this.getRemoteCategories();

        return categoryModelSql.getAllCategories();
    }

    public void getRemoteCategories() {
        categoryModelFirebase.getMemeCategories();
    }
}
