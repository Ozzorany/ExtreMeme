package com.example.extrememe.services;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DatabaseDataLoader {
    private static FirebaseFirestore fireStoreDB;

    private DatabaseDataLoader() {
    }

    public static FirebaseFirestore getDB() {
        if(fireStoreDB == null) {
            fireStoreDB = FirebaseFirestore.getInstance();
        }

        return fireStoreDB;
    }
}
