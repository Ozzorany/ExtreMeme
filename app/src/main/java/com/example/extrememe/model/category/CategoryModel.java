package com.example.extrememe.model.category;

import com.example.extrememe.model.Category;

import java.util.List;

public class CategoryModel {
    public final static CategoryModel instance = new CategoryModel();
    CategoryModelFirebase categoryModelFirebase = new CategoryModelFirebase();

    private CategoryModel() {
    }

    public interface Listener<T> {
        void onComplete(T result);
    }

    public interface GetMemeCategoriesListener extends Listener<List<Category>> {
    }

    public void getMemeCategories(GetMemeCategoriesListener listener) {
        categoryModelFirebase.getMemeCategories(listener);
    }
}
