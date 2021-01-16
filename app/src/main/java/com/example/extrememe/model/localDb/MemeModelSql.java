package com.example.extrememe.model.localDb;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.extrememe.model.Meme;

import java.util.List;

public class MemeModelSql {
    public LiveData<List<Meme>> getAllMemes() {
        return AppLocalDb.db.memeDao().getAllMemes();
    }

    public LiveData<List<Meme>> getMemesByUserId(String userId) {
        return AppLocalDb.db.memeDao().getAllMemesByUserId(userId);
    }

    public void addMeme(Meme meme) {
        new InsertMemeAsyncTask(AppLocalDb.db.memeDao()).execute(meme);
    }

    public void deleteMeme(Meme meme) {
        new DeleteMemeAsyncTask(AppLocalDb.db.memeDao()).execute(meme);
    }

    public void updateMeme(Meme meme){
        new UpdateMemeAsyncTask(AppLocalDb.db.memeDao()).execute(meme);
    }


    private static class InsertMemeAsyncTask extends AsyncTask<Meme, Void, Void> {
        private MemeDao memeDao;

        private InsertMemeAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Meme... memes) {
            memeDao.insertAll(memes[0]);
            return null;
        }
    }

    private static class DeleteMemeAsyncTask extends AsyncTask<Meme, Void, Void> {
        private MemeDao memeDao;

        private DeleteMemeAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Meme... memes) {
            memeDao.delete(memes[0]);
            return null;
        }
    }

    private static class UpdateMemeAsyncTask extends AsyncTask<Meme, Void, Void> {
        private MemeDao memeDao;

        private UpdateMemeAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Meme... memes) {
            memeDao.updateMemes(memes[0]);
            return null;
        }
    }
}


