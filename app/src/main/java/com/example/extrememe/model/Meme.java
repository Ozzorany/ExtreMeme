package com.example.extrememe.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Meme implements Serializable {
    @PrimaryKey
    @NonNull
    String id;
    String userId;
    String imageUrl;
    String description;
    @TypeConverters(ListTypeConverter.class)
    List<String> usersLikes;
    @TypeConverters(ListTypeConverter.class)
    List<String> categories;
    Long lastUpdated;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("userId", userId);
        result.put("imageUrl", imageUrl);
        result.put("description", description);
        result.put("usersLikes", usersLikes);
        result.put("categories", categories);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public void fromMap( Map<String, Object> map){
        id = (String)map.get("id");
        userId = (String)map.get("userId");
        imageUrl = (String)map.get("imageUrl");
        description = (String)map.get("description");
        usersLikes = (List<String>)map.get("usersLikes");
        categories = (List<String>)map.get("categories");
        Timestamp timestamp = (Timestamp)map.get("lastUpdated");
        //lastUpdated = timestamp.getSeconds();
        lastUpdated = timestamp.toDate().getTime();
    }

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

    public List<String> getUsersLikes() {
        return usersLikes;
    }

    public void setUsersLikes(List<String> usersLikes) {
        this.usersLikes = usersLikes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
