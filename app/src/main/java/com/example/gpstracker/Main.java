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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Main extends AppCompatActivity implements LocListenerInterface{

    BottomNavigationView bottomNavigationView;
    private LocationManager locationManager;
    private MyLocListener myLocListener;
    private DatabaseReference mDatabase;
    List<ResultTraining> resultTrainings;
    RecyclerView recyclerView;
    boolean firstExist = true;
    String date_str, time_str, bestSpeed_str, temp_str, dis_str, steps_str;
    MyAdapter adapter;
    ImageView person, home, training;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(Main.this, LoginActivity.class));
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerInterface(this);
        checkPermissions();


        //bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //bottomNavigationView.setSelectedItemId(R.id.person);
        //bottomNavigationView.setSelectedItemId(R.id.home);
        //bottomNavigationView.setSelectedItemId(R.id.training);
        //setAct();
        //bottomNavigationView.getMenu().getItem(1).setChecked(true);

        resultTrainings = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(getApplicationContext(), resultTrainings);
        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(!firstExist)loadDataFromDb();
        firstExist= false;

        person = findViewById(R.id.image_person);
        home = findViewById(R.id.image_home);
        training = findViewById(R.id.image_training);

        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_image));
                startActivity(new Intent(Main.this, Health.class));
            }
        });
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_image));
                startActivity(new Intent(Main.this, TrainingActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!firstExist){
            loadDataFromDb();
            firstExist=false;
        }
    }

    private void loadDataFromDb() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().
                    addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("myfirebase", "Error getting data", task.getException());
                            } else {
                                resultTrainings.clear(); // Очистка списка перед добавлением новых данных
                                GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                };
                                Map<String, Object> value = task.getResult().getValue(genericTypeIndicator);
                                if (value != null && !value.isEmpty()) {
                                    for (int i = value.size(); i > 0; i--) {
                                        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Training" + i).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful() && task.getResult().getValue()!=null) {
                                                    String s2 = task.getResult().getValue().toString();
                                                    Log.v("value", "Training: " + s2);
                                                    String[] s1 = s2.split(",");

                                                    int serch = s1[0].lastIndexOf("=");
                                                    s2 = s1[0].substring(serch + 1);
                                                    bestSpeed_str = s2;

                                                    serch = s1[1].lastIndexOf("=");
                                                    s2 = s1[1].substring(serch + 1, s1[1].length());
                                                    date_str = s2;

                                                    serch = s1[2].lastIndexOf("=");
                                                    s2 = s1[2].substring(serch + 1, s1[2].length());
                                                    temp_str = s2;

                                                    serch = s1[3].lastIndexOf("=");
                                                    s2 = s1[3].substring(serch + 1, s1[3].length());
                                                    time_str = s2;

                                                    serch = s1[4].lastIndexOf("=");
                                                    s2 = s1[4].substring(serch + 1, s1[4].length());
                                                    steps_str = s2;

                                                    serch = s1[5].lastIndexOf("=");
                                                    s2 = s1[5].substring(serch + 1, s1[5].length() - 1);
                                                    dis_str = s2;
                                                    if (bestSpeed_str != null || time_str != null || dis_str != null || steps_str != null || date_str != null) {
                                                        resultTrainings.add(new ResultTraining(date_str, time_str, dis_str, bestSpeed_str, temp_str, steps_str));
                                                    }
                                                } else {
                                                    Log.e("myfirebase", "Error getting data", task.getException());
                                                }
                                                adapter.notifyDataSetChanged(); // Уведомление адаптера об изменениях в данных
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
        }
    }

    public void setAct() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.training) {
                    startActivity(new Intent(Main.this, TrainingActivity.class));
                } else if (menuItem.getItemId() == R.id.person) {
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