package com.example.extrememe.model.meme;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.example.extrememe.MemesApplication;
import com.example.extrememe.model.Meme;
import com.example.extrememe.model.localDb.MemeModelSql;

import java.util.List;

public class MemeModel {
    public final static MemeModel instance = new MemeModel();
    MemeModelFirebase memeModelFirebase = new MemeModelFirebase();
    LiveData<List<Meme>> memes;
    LiveData<List<Meme>> myMemes;
    MemeModelSql memeModelSql = new MemeModelSql();


    private MemeModel() {
    }

    public interface Listener<T> {
        void onComplete(T result);
    }

    public interface GetAllMemesListener extends Listener<List<Meme>> {
    }

    public LiveData<List<Meme>> getAllMemes() {
        if(memes == null){
            memes = memeModelSql.getAllMemes();
        }

        return memes;
    }

    public void refreshAllMemes(final Listener listener){
        SharedPreferences sharedPreferences = MemesApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated = sharedPreferences.getLong("lastUpdated", 0);

        memeModelFirebase.getAllMemes(lastUpdated, new GetAllMemesListener() {
            @Override
            public void onComplete(List<Meme> result) {
                Long lastUpdated = 0L;

                for (Meme meme: result) {
                    memeModelSql.addMeme(meme);

                    if(meme.getLastUpdated() > lastUpdated)
                    {
                        lastUpdated = meme.getLastUpdated();
                    }
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("lastUpdated", lastUpdated);
                editor.commit();

                if(listener != null){
                    listener.onComplete(result);
                }

                //memes.setValue(result);
            }
        });
    }

    public interface GetMemesByUserListener extends Listener<List<Meme>> {
    }

    public LiveData<List<Meme>> getMemesByUserId(String userId) {
        if(myMemes == null){
            myMemes = memeModelSql.getMemesByUserId(userId);
        }

        return myMemes;
    }

    public void refreshAllMyMemes(String userId, final Listener listener){
        SharedPreferences sharedPreferences = MemesApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated = sharedPreferences.getLong("lastUpdated", 0);

        memeModelFirebase.getMemesByUserId(userId, lastUpdated, new GetMemesByUserListener() {
            @Override
            public void onComplete(List<Meme> result) {
                Long lastUpdated = 0L;

                for (Meme meme: result) {
                    memeModelSql.addMeme(meme);

                    if(meme.getLastUpdated() > lastUpdated)
                    {
                        lastUpdated = meme.getLastUpdated();
                    }
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("lastUpdated", lastUpdated);
                editor.apply();

                //memes.setValue(result);
                if(listener != null){
                    listener.onComplete(myMemes.getValue());
                }
            }
        });
    }

    public interface UpdateMemeListener extends Listener<Void> {
    }

    public void updateMeme(Meme meme, UpdateMemeListener listener) {
        memeModelFirebase.updateMeme(meme, listener);
    }

    public void insertMeme(Meme meme, UpdateMemeListener listener) {
        memeModelFirebase.insertMeme(meme, listener);
    }

    public interface RemoveMemeListener extends Listener<Void> {
    }

    public void removeMeme(String memeId, RemoveMemeListener listener) {
        memeModelFirebase.removeMeme(memeId, listener);
    }
}
