package com.example.gpstracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.internal.MapKitBinding;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class TrainingActivity extends AppCompatActivity implements LocListenerInterface, SensorEventListener {
    private TextView tvResDistance, tvTotal, tvVelocity;
    private Location lastLocation;
    private int distance;
    private int total_distance = 0;
    private int rest_distance;
    private ProgressBar pb;
    private int seconds;
    private boolean running;
    TextView stopwatch;
    TextView tempos;
    DBHelperTraining dbHelperTraining;
    private LocationManager locationManager;
    private MyLocListener myLocListener;

    private SensorManager mSensorManager = null;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previewsTotalSteps = 0;
    private TextView steps;

    private MapView mapView;
    private MapObjectCollection mapObjects;
    @NonNull private PlacemarkMapObject Now_Geoposition;
    @NonNull double shirota;
    @NonNull double longg;
    Location lastKnowLoc;
    double lastLongitude;
    double lastLatitude;
    boolean firstTimeChangeLoc = true;

    private DatabaseReference mDatabase;
    public java.util.Map<String, Boolean> stars = new HashMap<>();

    public static int countOfTraining = 1;
    public static String nameOfTraining = "";


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("e3460f52-0812-45df-8e03-9b6b2b641b5c");
        setContentView(R.layout.activity_training);

        init();

        MapKitFactory.initialize(this);
        mapView = findViewById(R.id.mapview);

        /**ОТОБРАЖЕНИЕ ЛОКАЦИИ, СОЗДАНИЕ ТОЧКИ И НАВЕДЕНИЕ НА НЕЕ КАМЕРЫ ПРИ ПЕРВОМ ЗАПУСКЕ ПРИЛОЖЕНИЯ */
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longg = location != null ? location.getLongitude() : 0;
        shirota = location != null ? location.getLatitude() : 0;
        lastKnowLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mapView.getMap().move(
                new CameraPosition(new Point(shirota, longg), 14.0f, 0.0f, 0.0f));
        Now_Geoposition = mapView.getMap().getMapObjects().addPlacemark(
             new Point(shirota, longg));
        stopwatch = findViewById(R.id.chron);
        tempos = findViewById(R.id.tempo);

        steps = findViewById(R.id.steps);
        resetSteps();
        loadData();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        runTimer();

        firstTimeChangeLoc = true;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPref = getSharedPreferences("train", Context.MODE_PRIVATE);
        countOfTraining = Integer.valueOf(sharedPref.getString("count_train", String.valueOf(1)));
    }



    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();

        SharedPreferences sharedPref = getSharedPreferences("train", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("count_train", String.valueOf(countOfTraining));
        editor.apply();
        super.onStop();
    }
    /**ТО ЖЕ САМОЕ ЧТО И ИНТЕРФЕЙС LOCATIONLISTENER, ДОБАВИЛ ЭТОТ ДЛЯ УДОБСТВА, ПОТОМ МОЖНО ВСЁ СДЕЛАТЬ В ОДНОМ МЕТОДЕ */
    private LocationListener locationListener = new LocationListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(@NonNull Location location) {
            lastLatitude = lastKnowLoc.getLatitude();
            lastLongitude = lastKnowLoc.getLongitude();
            if(!firstTimeChangeLoc)drawLine(lastLatitude, lastLongitude, location);

            float d_distance = lastKnowLoc.distanceTo(location);
            if (distance > total_distance) total_distance += (int) d_distance;
            pb.setProgress(total_distance);
            tvTotal.setText(String.valueOf(total_distance));

            String formattedSpeed = "" + Math.round(location.getSpeed() * 3600 / 1000 * 10.0) / 10.0;
            tvVelocity.setText(formattedSpeed);

            lastKnowLoc = location;
            showLocation(location);
            firstTimeChangeLoc = false;
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(@NonNull String provider) {
            showLocation(locationManager.getLastKnownLocation(provider));
        }

    };
    @SuppressLint("MissingPermission")
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("train", Context.MODE_PRIVATE);
        countOfTraining = Integer.valueOf(sharedPref.getString("count_train", String.valueOf(1)));
        /**ОБНОВЛЕНИЕ ЛОКАЦИИ КАЖДЫЕ 10 СЕКУНД
         ИЛИ ПРИ КАЖДОМ ИЗМЕНЕНИИ МЕСТОПОЛОЖЕНИЯ */
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000 * 10, 0, locationListener);
        /**РЕГИСТРАЦИЯ ШАГОВ */
        if (stepSensor == null) {
            Toast.makeText(this, "This device has no sensor", Toast.LENGTH_LONG).show();
        } else {
            mSensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
        locationManager.removeUpdates(locationListener);

        SharedPreferences sharedPref = getSharedPreferences("train", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("count_train", String.valueOf(countOfTraining));
        editor.apply();
    }

    /** ОТОБРАЖЕНИЕ КОРРЕКТНОГО МЕСТОПОЛОЖЕНИЯ ТОЧКИ НА КАРТЕ
     * (РАБОТАЕТ НЕЗАВИСИМО ОТ ТОГО, НАЧАТА ТРЕНИРОВКА ИЛИ НЕТ)
     *
     * ПОДКЛЮЧЕНИЕ К ПРОВАЙДЕРУ GPS ИЛИ NETWORK
     */
    private void showLocation(Location location){
        if(location == null){
            return;
        }
        if(location.getProvider().equals(LocationManager.GPS_PROVIDER)){
            Now_Geoposition.setGeometry(new Point(location.getLatitude(), location.getLongitude()));
        }
        else if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER)){
            Now_Geoposition.setGeometry(new Point(location.getLatitude(), location.getLongitude()));
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
    }

    private void setDistance(String dis) {
        pb.setMax(Integer.parseInt(dis));
        rest_distance = Integer.parseInt(dis);
        distance = Integer.parseInt(dis);
        tvResDistance.setText("/" + dis);
    }

    /** МЕТОД ОТРИСОВКИ ЛИНИИ НА КАРТЕ(В РАЗРАБОТКЕ) */
    public void drawLine(double lastLatitude, double lastLongitude, Location location2){
        Point pointA = new Point(lastLatitude, lastLongitude); // координаты точки A
        Point pointB = new Point(location2.getLatitude(), location2.getLongitude()); // координаты точки B
        mapView.getMap().getMapObjects().addPolyline(new Polyline(new ArrayList<>(Arrays.asList(pointA, pointB))));
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;
                EditText ed = ad.findViewById(R.id.edText);
                if (ed != null) {
                    if (!ed.getText().toString().isEmpty()) setDistance(ed.getText().toString());
                }
            }
        });
        builder.setView(cl);
        builder.show();

    }

    public void onClickDistance(View view) {
        showDialog();
    }
    private void updateDistance(Location loc) {/**ПОЧЕМУ ТО НЕ СЧИТАЕТ ДИСТАНЦИЮ (ИЛИ СЧИТАЕТ НЕПРАВИЛЬНО) */
        //if (lastKnowLoc != null) {
        //    float d_distance = lastKnowLoc.distanceTo(loc);
        //    if (distance > total_distance) total_distance += (int) d_distance;
        //    pb.setProgress(total_distance);
        //}
        //lastKnowLoc = loc;
        //tvTotal.setText(String.valueOf(total_distance));
        //String formattedSpeed = "" + Math.round(loc.getSpeed() * 3600 / 1000 * 10.0) / 10.0;
        //tvVelocity.setText(formattedSpeed);
    }

    public void onClickStart(View view) {running = true;}

    ///////// ОСТАНАВЛИВАЕТ ТРЕНИРОВКУ И ПОКАЗЫВАЕТ ДИАЛОГОВОЕ ОКНО //////////
    public void onClickStop(View view) {
        comp();
        running = false;
        String str =
                "Дистанция: " + String.valueOf(total_distance) + "\n" +
                        "Время: " + stopwatch.getText().toString();
        DialogAftTraining dialog1 = new DialogAftTraining();
        Bundle args = new Bundle();
        args.putString("key", str);
        dialog1.setArguments(args);
        dialog1.show(getSupportFragmentManager(), "custom");

        previewsTotalSteps = totalSteps;
        steps.setText("0");
        savedData();

        stopwatch.setText(R.string.zero_stopwatch);
        tempos.setText("0:00");
        tvTotal.setText("0");
        total_distance = 0;
        seconds = 0;

    }

    public void comp() {
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("steps", totalSteps);
        userInfo.put("dis", total_distance);
        SharedPreferences sharedPref = getSharedPreferences("train", Context.MODE_PRIVATE);
        countOfTraining = Integer.valueOf(sharedPref.getString("count_train", String.valueOf(1)));
        nameOfTraining = "Training" + String.valueOf(countOfTraining);
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(nameOfTraining)
                .setValue(userInfo);
        countOfTraining++;
        SharedPreferences sharedPref1 = getSharedPreferences("train", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putString("count_train", String.valueOf(countOfTraining));
        editor.apply();
    }


    /**МЕТОД НЕИСПРАВНО РАБОТАЕТ, ПЭТОМУ ПОКА ОТКЛЮЧЕН */
    private void checkedTemp(Location loc) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                if (loc.getSpeed() != 0) {
                    float time = 1000 / loc.getSpeed();
                    int min = (int) time / 60;
                    int sec = (int) time % 60;
                    String endTemp = min + ":" + sec;
                    tempos.setText(endTemp);
                }
                lastLocation = loc;
            }
        });
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
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


    public void OnLocationChanged (Location loc){
        if (running) {
            updateDistance(loc);
        }
    }

    /**ВСЕ ДЕЙСТВИЯ С Eventlistener ДЛЯ СЧЕТА И СОХРАНЕНИЯ ШАГОВ */

    private void resetSteps(){
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrainingActivity.this, "Удерживайте, чтобы сбросить шаги", Toast.LENGTH_SHORT).show();
            }
        });

        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                previewsTotalSteps = totalSteps;
                steps.setText("0");
                savedData();
                return true;
            }
        });
    }

    private void savedData(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key1", String.valueOf(previewsTotalSteps));
        editor.apply();
    }

    private  void loadData(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int savedNumber = Integer.valueOf(sharedPref.getString("key1", String.valueOf(0)));
        previewsTotalSteps = savedNumber;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER && running){
            totalSteps = (int) sensorEvent.values[0];
            int currentSteps = totalSteps - previewsTotalSteps;
            steps.setText(String.valueOf(currentSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

}