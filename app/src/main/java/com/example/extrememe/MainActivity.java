package com.example.extrememe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.extrememe.services.CategoryService;
import com.example.extrememe.services.DatabaseDataLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CategoryService().getMemeCategories();
    }
}