package com.example.extrememe.model.category;

import android.util.Log;

import com.example.extrememe.model.Category;
import com.example.extrememe.model.localDb.CategoryModelSql;
import com.example.extrememe.services.DatabaseDataLoader;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryModelFirebase {
    private static final String TAG = "CategoryService";
    private CategoryModelSql categoryModelSql = new CategoryModelSql();

    public void getMemeCategories() {
        List<Category> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Category.class));
                        }
                        categoryModelSql.addCategories(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
