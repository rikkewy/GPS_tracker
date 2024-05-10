package com.example.gpstracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //// ПРОВЕРКА НА НАЛИЧИЕ В БАЗЕ ДАННЫХ ПОЛЬЗОВАТЕЛЯ //////
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(Main.this, LoginActivity.class));
            //// ЕСЛИ ПОЛЬЗОВАТЕЛЬ НЕ ЗАРЕГИСТРИРОВАН ТО ПЕРЕХОД НА ОКНО АВТОРИЗАЦИИ ////
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.person);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setSelectedItemId(R.id.training);

        setAct();
        bottomNavigationView.getMenu().getItem(1).setChecked(true);///устанавливаем выбранной кнопку home///
    }
    public void setAct() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.training)
                    startActivity(new Intent(Main.this, TrainingActivity.class));
                else if (menuItem.getItemId() == R.id.person) {
                    startActivity(new Intent(Main.this, Health.class));
                }

                return false;
            }
        });
    }
}