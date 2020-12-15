package com.example.extrememe.entities;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Category {
    public String id;
    public String name;

    public Category() {
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
