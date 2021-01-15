package com.example.extrememe.model.localDb;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;

import java.util.List;

public class MemeModelSql {
    public void getAllMemes(final MemeModel.GetAllMemesListener listener){
        class MyAsyncTask extends AsyncTask {
            List<Meme> data;
            @Override
            protected Object doInBackground(Object[] objects) {
                data = AppLocalDb.db.memeDao().getAllMemes();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(data);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public MutableLiveData<List<Meme>> getMemesByUserId(String userId){
        return AppLocalDb.db.memeDao().getAllMemesByUserId(userId);
    }

    public void addMeme(final Meme meme, final MemeModel.UpdateMemeListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.memeDao().insertAll(meme);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete(null);
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
