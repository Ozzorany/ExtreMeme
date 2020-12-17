package com.example.extrememe.services;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DatabaseDataLoader {
    private static FirebaseFirestore fireStoreDB;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference storageRef ;


    private DatabaseDataLoader() {
    }

    public static FirebaseFirestore getDB() {
        if(fireStoreDB == null) {
            fireStoreDB = FirebaseFirestore.getInstance();
        }

        return fireStoreDB;
    }

    public static StorageReference getStorageRef(String url) {
        if(storageRef == null) {
            storageRef = storage.getReferenceFromUrl(url);
        }

        return storageRef;
    }
}
