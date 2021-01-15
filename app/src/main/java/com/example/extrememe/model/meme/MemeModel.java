package com.example.extrememe.model.meme;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.extrememe.MemesApplication;
import com.example.extrememe.model.Meme;
import com.example.extrememe.model.localDb.MemeModelSql;

import java.util.List;

public class MemeModel {
    public final static MemeModel instance = new MemeModel();
    MemeModelFirebase memeModelFirebase = new MemeModelFirebase();
    MutableLiveData<List<Meme>> memes;
    MemeModelSql memeModelSql = new MemeModelSql();


    private MemeModel() {
    }

    public interface Listener<T> {
        void onComplete(T result);
    }

    public interface GetAllMemesListener extends Listener<List<Meme>> {
    }

    public void getAllMemes(GetAllMemesListener listener) {
        memeModelFirebase.getAllMemes(listener);
    }

    public interface GetMemesByUserListener extends Listener<List<Meme>> {
    }

    public MutableLiveData<List<Meme>> getMemesByUserId(String userId) {
        if(memes == null){
            memes = memeModelSql.getMemesByUserId(userId);
        }

        return memes;
    }

    public void refreshAllMyMemes(String userId, final Listener listener){
        SharedPreferences sharedPreferences = MemesApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated = sharedPreferences.getLong("lastUpdated", 0);

        memeModelFirebase.getMemesByUserId(userId, lastUpdated, new GetMemesByUserListener() {
            @Override
            public void onComplete(List<Meme> result) {
                Long lastUpdated = 0L;

                for (Meme meme: result) {
                    memeModelSql.addMeme(meme, null);

                    if(meme.getLastUpdated() > lastUpdated)
                    {
                        lastUpdated = meme.getLastUpdated();
                    }
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("lastUpdated", lastUpdated);
                editor.commit();

                memes.setValue(result);
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
