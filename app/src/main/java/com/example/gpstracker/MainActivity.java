package com.example.gpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LocListenerInterface {
    private TextView tvResDistance, tvTotal, tvVelocity;
    private Location lastLocation;
    private LocationManager locationManager;
    private MyLocListener myLocListener;
    private int distance;
    private int total_distance;
    private int rest_distance;
    private ProgressBar pb;
    long time_start,time_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        ////// ПРОВЕРКА НА НАЛИЧИЕ В БАЗЕ ДАННЫХ ПОЛЬЗОВАТЕЛЯ //////
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            //// ЕСЛИ ПОЛЬЗОВАТЕЛЬ НЕ ЗАРЕГИСТРИРОВАН ТО ПЕРЕХОД НА ОКНО АВТОРИЗАЦИИ ////
        }
    }

    private void init() {
        tvTotal = findViewById(R.id.tvTotal);
        tvResDistance = findViewById(R.id.tvRest);
        tvVelocity = findViewById(R.id.tvVelocity);
        pb = findViewById(R.id.progressBar);
        pb.setMax(1000);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerInterface(this);
        checkPermissions();

    }
    private  void setDistance(String dis){
        pb.setMax(Integer.parseInt(dis));
        rest_distance = Integer.parseInt(dis);
        distance = Integer.parseInt(dis);
        tvResDistance.setText(dis);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;
                EditText ed =  ad.findViewById(R.id.edText);
                if(ed != null){
                    if(!ed.getText().toString().equals(""))setDistance(ed.getText().toString());

                }
            }
        });
        builder.setView(cl);
        builder.show();

    }
    public void onClickDistance(View view){

        showDialog();
    }
    private void updateDistance(Location loc){
        if (loc.getSpeed()!=0 && lastLocation !=null) {
            float d_distance=lastLocation.distanceTo(loc);
            if(distance > total_distance) total_distance +=(int)d_distance;
            if(rest_distance > 0) rest_distance -= (int)d_distance;
            pb.setProgress(total_distance);
        }
        lastLocation = loc;
        tvResDistance.setText(String.valueOf(rest_distance));
        tvTotal.setText(String.valueOf(total_distance));
        tvVelocity.setText(String.valueOf(loc.getSpeed()));
        long currenttime=(System.nanoTime()-time_start)/1000000000;

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
        private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);

        }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, myLocListener);
            }

        }

    @Override
    public void OnLocationChanged(Location loc) {
        updateDistance(loc);
    }
}