package com.example.gpstracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;

public class Main extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    private MapView mapView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// ПРОВЕРКА НА НАЛИЧИЕ В БАЗЕ ДАННЫХ ПОЛЬЗОВАТЕЛЯ //////
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(Main.this, LoginActivity.class));
            //// ЕСЛИ ПОЛЬЗОВАТЕЛЬ НЕ ЗАРЕГИСТРИРОВАН ТО ПЕРЕХОД НА ОКНО АВТОРИЗАЦИИ ////
        }

        MapKitFactory.setApiKey("e3460f52-0812-45df-8e03-9b6b2b641b5c");
        MapKitFactory.initialize(this);
        setContentView(R.layout.main);
        mapView = findViewById(R.id.mapview);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.person);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setSelectedItemId(R.id.training);

        setAct();
        bottomNavigationView.getMenu().getItem(1).setChecked(true);///устанавливаем выбранной кнопку home///
    }
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    public void setAct() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.training)
                    startActivity(new Intent(Main.this, TrainingActivity.class));
                return false;
            }
        });
    }
}