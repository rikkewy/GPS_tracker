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
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Main extends AppCompatActivity implements LocListenerInterface{

    BottomNavigationView bottomNavigationView;
    private LocationManager locationManager;
    private MyLocListener myLocListener;
    ArrayList<TrainingView> trainingViewArrayList = new ArrayList<TrainingView>();
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    TrainingViewAdapter adapter;
    TextView text;
    boolean firstExist = true;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerInterface(this);
        checkPermissions();

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

        setInitialData();
        text=findViewById(R.id.textViewEx);
        RecyclerView recyclerView = findViewById(R.id.list);
        // создаем адаптер
        adapter = new TrainingViewAdapter(this, trainingViewArrayList);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        //database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(!firstExist)readDataFromDb();
        firstExist = false;

    }
    public void onResume(){
        readDataFromDb();
        super.onResume();
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(TrainingActivity.nameOfTraining).get().
                addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("myfirebase", "Error getting data", task.getException());
                        }
                        else {
                            GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                            Map<String, Object> value = task.getResult().getValue(genericTypeIndicator);

                            TrainingView trainingView = new TrainingView("jnjknkj", value.get("steps"), "kjh");
                            trainingViewArrayList.add(trainingView);
                            Log.v("firebase", "V " + value.get("steps"));
                        }
                    }
                });
    }


    public void readDataFromDb(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                    //Map<String, Object> value = snapshot.child("users")
                    //        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(genericTypeIndicator);
                    ////Log.v("mytext", "Value " + value);
                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(TrainingActivity.nameOfTraining).get().
                        addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("myfirebase", "Error getting data", task.getException());
                        }
                        else {
                            GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                            Map<String, Object> value = task.getResult().getValue(genericTypeIndicator);
                            TrainingView trainingView = new TrainingView("jnjknkj", value.get("steps"), "kjh");
                            trainingViewArrayList.add(trainingView);

                            Log.v("firebase", "V " + value.get("steps"));
                        }
                    }
                });
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("mytext", "No Complite", error.toException());
            }
        });
    }

    private void setInitialData(){

        //trainingViewArrayList.add(new TrainingView("jj", 0, ""));
        //states.add(new State ("Аргентина", "Буэнос-Айрес", R.drawable.argentina));
        //states.add(new State ("Колумбия", "Богота", R.drawable.columbia));
        //states.add(new State ("Уругвай", "Монтевидео", R.drawable.uruguai));
        //states.add(new State ("Чили", "Сантьяго", R.drawable.chile));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == RESULT_OK) {
            checkPermissions();

        } else {
            Toast.makeText(this, "No GPS permissions", Toast.LENGTH_SHORT).show();

        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, myLocListener);
        }

    }
    public void OnLocationChanged (Location loc){}
}