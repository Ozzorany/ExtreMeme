package com.example.extrememe;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.extrememe.services.LoginService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    public BottomNavigationView bottomNav;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        navController = Navigation.findNavController(this, R.id.mainactivity_navhost);
        NavigationUI.setupActionBarWithNavController(this, navController);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainactivity_navhost);
        NavController navController = navHostFragment.getNavController();
        bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> getSupportActionBar().setTitle(destination.getLabel()));

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            boolean isUserLoggedIn = LoginService.getInstance(context).isLoggedIn();

            switch (item.getItemId()) {
                case R.id.myMemesFragment:
                    if (isUserLoggedIn) {
                        Navigation.findNavController(MainActivity.this, R.id.mainactivity_navhost).navigate(R.id.myMemesFragment);
                        return true;
                    }
                    return false;
                case R.id.createMemeFragment:
                    if (isUserLoggedIn) {
                        Navigation.findNavController(MainActivity.this, R.id.mainactivity_navhost).navigate(R.id.createMemeFragment);
                        return true;
                    }
                    return false;
                case R.id.mainFeedFragment:
                    Navigation.findNavController(MainActivity.this, R.id.mainactivity_navhost).navigate(R.id.mainFeedFragment);
                    return true;
            }

            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navController.navigateUp();
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item, navController);
    }
}