package com.example.gpstracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Index_Weight_height extends AppCompatActivity {
    private TextView text3;
    private TextView resul_imt;
    private ImageView weigt1;
    private EditText m1;
    private EditText t1;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index_weight_height);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        text3 = findViewById(R.id.text3);
        resul_imt = findViewById(R.id.res2);
        weigt1 = findViewById(R.id.weight1);
        m1 = findViewById(R.id.m1);
        t1= findViewById(R.id.t1);
        button = findViewById(R.id.btn1);
    }
        public void sum(){
            int res;
            res = Integer.parseInt(m1.getText().toString()) /  Integer.parseInt(t1.getText().toString());
            resul_imt.setText("Ваш ИМТ" + res);
    }
}