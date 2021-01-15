package com.example.extrememe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;

import java.util.List;

public class MemesViewModel extends ViewModel {
    private MutableLiveData<List<Meme>> data;

    MutableLiveData<List<Meme>> getMemesByUserId(String userId) {
        data = MemeModel.instance.getMemesByUserId(userId);
        return data;
    }
}
