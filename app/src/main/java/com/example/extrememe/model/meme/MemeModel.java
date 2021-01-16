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

        memeModelFirebase.getAllMemes(lastUpdated, result -> {
            Long lastUpdated1 = 0L;

            for (Meme meme: result) {
                memeModelSql.addMeme(meme);

                if(meme.getLastUpdated() > lastUpdated1)
                {
                    lastUpdated1 = meme.getLastUpdated();
                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("lastUpdated", lastUpdated1);
            editor.commit();

            if(listener != null){
                listener.onComplete(result);
            }

            //TODO: CHECK WHAT TO DO WITH THIS IN THE NEXT LESSON
            //memes.setValue(result);
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

                //TODO: CHECK WHAT TO DO WITH THIS IN THE NEXT LESSON
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
        memeModelSql.updateMeme(meme);
        memeModelFirebase.updateMeme(meme, listener);
    }

    public void insertMeme(Meme meme, UpdateMemeListener listener) {
        memeModelFirebase.insertMeme(meme, listener);
    }

    public interface RemoveMemeListener extends Listener<Void> {
    }

    public void removeMeme(Meme meme, RemoveMemeListener listener) {
        memeModelSql.deleteMeme(meme);
        memeModelFirebase.removeMeme(meme.getId(), listener);
    }
}
