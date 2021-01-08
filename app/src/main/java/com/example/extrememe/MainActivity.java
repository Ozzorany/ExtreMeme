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
    Context context = this;
    public BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.mainactivity_navhost);
        NavigationUI.setupActionBarWithNavController(this, navController);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainactivity_navhost);
        NavController navController = navHostFragment.getNavController();
        bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

        if (!LoginService.getInstance(context).isLoggedIn()) {
            bottomNav.getMenu().findItem(R.id.myMemesFragment).setEnabled(false);
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> getSupportActionBar().setTitle(destination.getLabel()));
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