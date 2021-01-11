package com.example.extrememe.model.user;

import com.example.extrememe.model.User;

public class UserModel {
    public final static UserModel instance = new UserModel();
    UserModelFirebase userModelFirebase = new UserModelFirebase();

    private UserModel() {
    }

    public void createUser(User user) {
        userModelFirebase.createUser(user);
    }
}
