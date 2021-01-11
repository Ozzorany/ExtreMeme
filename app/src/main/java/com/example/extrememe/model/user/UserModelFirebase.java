package com.example.extrememe.model.user;

import android.util.Log;

import com.example.extrememe.model.User;
import com.example.extrememe.services.DatabaseDataLoader;

public class UserModelFirebase {
    private static final String TAG = "UserService";

    public void createUser(User user) {
        DatabaseDataLoader.getDB().collection("users").document(user.getId())
                .set(user)
                .addOnSuccessListener(s -> Log.w(TAG, "Success creating document"))
                .addOnFailureListener(e -> Log.w(TAG, "Error creating document", e));
    }
}
