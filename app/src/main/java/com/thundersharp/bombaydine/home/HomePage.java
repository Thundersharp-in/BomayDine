package com.thundersharp.bombaydine.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thundersharp.bombaydine.R;

public class HomePage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    View containerl;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.homerrr);
        containerl = findViewById(R.id.containerfrag);

        navController = Navigation.findNavController(this, R.id.containerfrag);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);



    }
}