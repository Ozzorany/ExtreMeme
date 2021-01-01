package com.example.extrememe.model.meme;

import com.example.extrememe.model.Meme;

import java.util.List;

public class MemeModel {
    public final static MemeModel instance = new MemeModel();
    MemeModelFirebase memeModelFirebase = new MemeModelFirebase();

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

    public void getMemesByUserId(String id, GetMemesByUserListener listener) {
        memeModelFirebase.getMemesByUserId(id, listener);
    }

    public interface UpdateMemeListener extends Listener<Void> {
    }

    public void updateMeme(Meme meme, UpdateMemeListener listener) {
        memeModelFirebase.updateMeme(meme, listener);
    }

    public interface RemoveMemeListener extends Listener<Void> {
    }

    public void removeMeme(String memeId, RemoveMemeListener listener) {
        memeModelFirebase.removeMeme(memeId, listener);
    }
}
