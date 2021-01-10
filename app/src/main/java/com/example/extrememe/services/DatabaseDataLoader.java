package com.example.extrememe.services;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DatabaseDataLoader {
    private static FirebaseFirestore fireStoreDB;

    private DatabaseDataLoader() {
    }

    public static FirebaseFirestore getDB() {
        if (fireStoreDB == null) {
            fireStoreDB = FirebaseFirestore.getInstance();
        }

        return fireStoreDB;
    }
}
