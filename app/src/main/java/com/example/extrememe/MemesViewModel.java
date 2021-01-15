package com.example.extrememe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;

import java.util.List;

public class MemesViewModel extends ViewModel {
    private LiveData<List<Meme>> data;

    LiveData<List<Meme>> getMemesByUserId(String userId) {
        data = MemeModel.instance.getMemesByUserId(userId);
        return data;
    }

    LiveData<List<Meme>> getAllMemes() {
        data = MemeModel.instance.getAllMemes();
        return data;
    }
}
