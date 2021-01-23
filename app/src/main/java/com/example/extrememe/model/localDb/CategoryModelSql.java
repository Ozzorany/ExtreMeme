package com.example.extrememe.model.localDb;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.extrememe.model.Category;

import java.util.List;

public class CategoryModelSql {
    public LiveData<List<Category>> getAllCategories() {
        return AppLocalDb.db.categoryDao().getAllCategories();
    }

    public void addCategories(List<Category> categories) {
        new InsertCategoriesAsyncTask(AppLocalDb.db.categoryDao()).execute(categories);
    }

    private static class InsertCategoriesAsyncTask extends AsyncTask<List<Category>, Void, Void> {
        private CategoryDao categoryDao;

        private InsertCategoriesAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(List<Category>... categories) {
            categoryDao.deleteAll();
            categoryDao.insertAll(categories[0]);
            return null;
        }
    }
}
