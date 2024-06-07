package com.example.gpstracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Health extends AppCompatActivity {
    private TextView resul_imt;
    private TextView resul_prop;
    private TextView resul_strenght;
    private TextView resul_weight_height;
    private TextView rost;
    private TextView wes;
    private TextView long_of_step;
    private TextView gender;
    private TextView circ_of_chest;
    private TextView selection;
    private Button btnResult;
    private Spinner spinner;
    private  String[] genders = { "М", "Ж"};
    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView_health);

        bottomNavigationView.setSelectedItemId(R.id.person);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setSelectedItemId(R.id.training);

        setAct();
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        bottomNavigationView.getMenu().getItem(1).setChecked(false);

        btnResult = findViewById(R.id.btn_result);
        rost = findViewById(R.id.your_height);
        wes = findViewById(R.id.your_weight);
        long_of_step = findViewById(R.id.your_step);
        gender = findViewById(R.id.your_gender);
        circ_of_chest = findViewById(R.id.your_chest);
        resul_imt = findViewById(R.id.res);
        resul_prop =  findViewById(R.id.resul_prop);
        resul_strenght = findViewById(R.id.resul_strenght);
        resul_weight_height = findViewById(R.id.resul_weight_height);


        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection.setText(String.valueOf(spinner.getSelectedItem()));
                SharedPreferences sharedPref5 = getSharedPreferences("myPref5", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref5.edit();
                editor.putInt("genders", spinner.getSelectedItemPosition());
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        selection = findViewById(R.id.selection);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genders);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResul();
            }
        });

        rost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRost();

            }
        });
        wes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWes();

            }
        });
        long_of_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_long_of_step();

            }
        });
        circ_of_chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCirc();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences("myPref1", Context.MODE_PRIVATE);
        String rost2 = sharedPref.getString("rost", "");
        if (!rost2.equals("") ) {
            rost.setText(rost2);
        }
        SharedPreferences sharedPref1 = getSharedPreferences("myPref2", Context.MODE_PRIVATE);
        String wes2 = sharedPref1.getString("wes", "");
        if (!wes2.equals("")) {
            wes.setText(wes2);
        }
        SharedPreferences sharedPref2 = getSharedPreferences("myPref4", Context.MODE_PRIVATE);
        String circ2 =sharedPref2.getString("circ", "" );
        if(!circ2.equals("")) {
            circ_of_chest.setText(circ2);
        }
        SharedPreferences sharedPref3 = getSharedPreferences("myPref3", Context.MODE_PRIVATE);
        String step1 =sharedPref3.getString("step", "" );
        if(!step1.equals("")) {
            long_of_step.setText(step1);
        }

        SharedPreferences sharedPref5 = getSharedPreferences("myPref5", Context.MODE_PRIVATE);
        int gender =sharedPref5.getInt("genders", 0 );
        if(gender!=0) spinner.setSelection(gender);
    }
    public void onPause(){
        super.onPause();
        //SharedPreferences sharedPref5 = getSharedPreferences("myPref5", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref5.edit();
        //editor.putInt("genders", spinner.getSelectedItemPosition());
        //editor.apply();
    }

    public void setResul(){
        int rost_mean;
        int wes_mean;
        int circ_mean;
        double res1 = 0;
        double res2 = 0;
        double res3 = 0;
        double res4 = 0;

         SharedPreferences sharedPref = getSharedPreferences("myPref1", Context.MODE_PRIVATE);
         int rost2 = Integer.valueOf(sharedPref.getString("rost", String.valueOf(0)));
         rost_mean = rost2;

         SharedPreferences sharedPref1 = getSharedPreferences("myPref2", Context.MODE_PRIVATE);
         int wes2 = Integer.valueOf(sharedPref1.getString("wes", String.valueOf(0)));
         wes_mean =wes2;

         SharedPreferences sharedPref2 = getSharedPreferences("myPref4", Context.MODE_PRIVATE);
         int circ2 = Integer.valueOf(sharedPref2.getString("circ", String.valueOf(0)));
         circ_mean = circ2;

         if(rost_mean != 0 && wes_mean !=0) {
             res1 = (int) (wes_mean /  (rost_mean *rost_mean / 10000.0));
             resul_imt.setText("Ваш ИМТ:" + " "  + res1);
             res4 = wes_mean * 1000 / rost_mean;
             resul_weight_height.setText("Ваш результат" + " "+ res4);
         }

         if (rost_mean != 0 && circ_mean!= 0){
             res2 = (int)(( ((double)circ_mean /  rost_mean) )* 100.0);
             resul_prop.setText("Ваш результат:" + " " +  res2);
         }

         if(rost_mean != 0 && wes_mean != 0 && circ_mean  != 0){
             res3 = rost_mean - (wes_mean + circ_mean);
             resul_strenght.setText("Ваш результат:" + " " + res3);
         }
   }
    public void setAct() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.training)
                    startActivity(new Intent(Health.this, TrainingActivity.class));
                else if (menuItem.getItemId() == R.id.home) {
                    startActivity(new Intent(Health.this, Main.class));
                }

                return false;
            }
        });
    }
    /** ДИОЛОГ С РОСТОМ */
        public void showDialogRost() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Рост");

            ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_rost, null);
            builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog ad = (AlertDialog) dialog;
                    EditText ed = ad.findViewById(R.id.rost1);
                    if (ed != null && Integer.parseInt(ed.getText().toString()) > 140 && Integer.parseInt(ed.getText().toString()) < 250) {
                        SharedPreferences sharedPref = getSharedPreferences("myPref1", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("rost", ed.getText().toString());
                        editor.apply();

                        rost.setText(String.valueOf(ed.getText().toString()));
                    }
                }
            });
            builder.setView(cl);
            builder.show();
        }

    /** ДИОЛОГ С ВЕСОМ */
    public void showDialogWes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вес");

        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.diolog_wes, null);
        builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;
                EditText ed = ad.findViewById(R.id.wes1);
                if (ed != null && Integer.parseInt(ed.getText().toString()) > 20 && Integer.parseInt(ed.getText().toString()) < 200) {
                    SharedPreferences sharedPref = getSharedPreferences("myPref2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("wes", String.valueOf(ed.getText().toString()));
                    editor.apply();
                    wes.setText(String.valueOf(ed.getText().toString()));
                }
            }
        });
        builder.setView(cl);
        builder.show();
    }

    /** ДИОЛОГ С РАЗМЕРОМ ШАГА */
    public void showDialog_long_of_step() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Длина шага");

        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_long_of_step, null);
        builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;
                EditText ed = ad.findViewById(R.id.longStep);
                if (ed != null && Integer.parseInt(ed.getText().toString()) < 100 && Integer.parseInt(ed.getText().toString()) > 30) {
                    SharedPreferences sharedPref = getSharedPreferences("myPref3", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("step", String.valueOf(ed.getText().toString()));
                    editor.apply();
                    long_of_step.setText(String.valueOf(ed.getText().toString()));
                }
            }
        });
        builder.setView(cl);
        builder.show();
    }

    /** ДИОЛОГ С ОБХВАТОМ ГРУДИ */
    public void showDialogCirc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Обхват груди");

        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_circ, null);
        builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;
                EditText ed = ad.findViewById(R.id.circ);
                if (ed != null && Integer.parseInt(ed.getText().toString()) < 200) {
                    SharedPreferences sharedPref = getSharedPreferences("myPref4", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("circ", String.valueOf(ed.getText().toString()));
                    editor.apply();
                    circ_of_chest.setText(String.valueOf(ed.getText().toString()));
                }
            }
        });
        builder.setView(cl);
        builder.show();
    }

}

