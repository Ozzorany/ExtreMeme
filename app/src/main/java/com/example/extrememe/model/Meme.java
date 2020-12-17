package com.example.extrememe.model;

public class Meme {
    String id;
    String imageUrl;
    String description;
    String[] usersLikes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getUsersLikes() {
        return usersLikes;
    }

    public void setUsersLikes(String[] usersLikes) {
        this.usersLikes = usersLikes;
    }
}
