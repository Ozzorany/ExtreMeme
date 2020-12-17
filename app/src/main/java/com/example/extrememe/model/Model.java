package com.example.extrememe.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    private Model(){
        for(int i=0;i<100;i++) {
            Meme meme = new Meme();
            meme.id = "" + i;
            meme.description = "Meme number " + i;
            meme.imageUrl = "";
            meme.usersLikes = new String[]{};
            data.add(meme);
        }
    }

    List<Meme> data = new LinkedList<Meme>();

    public List<Meme> getAllMemes() {
        return data;
    }
}
