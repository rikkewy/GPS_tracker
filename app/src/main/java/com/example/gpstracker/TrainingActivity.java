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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.Locale;

public class TrainingActivity extends AppCompatActivity implements LocListenerInterface {
    private TextView tvResDistance, tvTotal, tvVelocity;
    private Location lastLocation;
    private LocationManager locationManager;
    private MyLocListener myLocListener;
    private int distance;
    private int total_distance;
    private int rest_distance;
    private ProgressBar pb;
    private int seconds;
    private boolean running;
    TextView stopwatch;
    TextView tempos;
    DBHelperTraining DB;
    int id = 1;
    ListView listView;

    SQLiteDatabase db;
    Cursor userCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        stopwatch = findViewById(R.id.chron);
        tempos = findViewById(R.id.tempo);
        init();
        runTimer();
        DB = new DBHelperTraining(this);
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
        tvResDistance.setText("/"+dis);
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
    float d_distance;
    private void updateDistance(Location loc){
        long startTime = System.nanoTime();
        //Код, время выполнения которого нужно измерить
        if (loc.getSpeed() != 0 && lastLocation != null) {
            d_distance = lastLocation.distanceTo(loc);
            if (distance > total_distance) total_distance += (int) d_distance;
            //if (rest_distance > 0) rest_distance -= (int) d_distance;
            pb.setProgress(total_distance);
        }
        lastLocation = loc;
        //tvResDistance.setText(String.valueOf(rest_distance));
        tvTotal.setText(String.valueOf(total_distance));
        String formattedSpeed = new DecimalFormat("#0.0").format(loc.getSpeed()*3600/1000);
        tvVelocity.setText(formattedSpeed);
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        checkedTemp(loc, elapsedTime);
    }

    public void onClickStart(View view) {running = true;}
    ///////// ОСТАНАВЛИВАЕТ ТРЕНИРОВКУ И ПОКАЗЫВАЕТ ДИАЛОГОВОЕ ОКНО //////////
    public void onClickStop(View view){
        running = false;
        String str =
                "Дистанция: " + String.valueOf(total_distance) + "\n" +
                        "Время: " + stopwatch.getText().toString();
        DialogAftTraining dialog1 = new DialogAftTraining();
        Bundle args = new Bundle();
        args.putString("key", str);
        dialog1.setArguments(args);
        dialog1.show(getSupportFragmentManager(), "custom");

        int calories = 0;///// ЭТО НЕ КАЛОРИИ, КАЛОРИИ НЕ СДЕЛАНЫ //////
        Boolean insert = DB.insertData(id, total_distance, tempos.getText().toString(), calories,
                stopwatch.getText().toString());
        if(insert){
            Toast.makeText(TrainingActivity.this, "Тренировка записана", Toast.LENGTH_SHORT).show();
        }
        id++;

        stopwatch.setText(R.string.zero_stopwatch);
        tempos.setText("0:00");
        tvTotal.setText("0");
        seconds=0;
    }


    private void checkedTemp(Location loc, long elapsedTime){
        if (loc.getSpeed() != 0 && lastLocation != null) {
            float metrs = lastLocation.distanceTo(loc);
            float temp = (elapsedTime/60) / (metrs/1000);
            int min = (int) temp;
            int sec = (int)(60 * (temp % (int) temp));
            String endTemp = min + ":" + sec;
            tempos.setText(endTemp);
        }
    }
    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                if (running) {
                    seconds++;
                    stopwatch.setText(time);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^СЕКУНДОМЕР^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\\

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