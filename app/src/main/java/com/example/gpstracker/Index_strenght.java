package com.example.gpstracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.PrimitiveIterator;

public class Index_strenght extends AppCompatActivity {
    private TextView text4;
    private TextView textView4;
    private ImageView telo;
    private EditText m2;
    private EditText t2;
    private EditText og2;
    private Button btn2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index_strenght);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        text4 = findViewById(R.id.text4);
        textView4 = findViewById(R.id.textView4);
        telo = findViewById(R.id.telo);
        m2 = findViewById(R.id.m2);
        t2 =  findViewById(R.id.t2);
        og2 = findViewById(R.id.og2);
        btn2= findViewById(R.id.button2);
    }
    public void resultat(){
        int ressul = 0;
        ressul = Integer.parseInt(t2.getText().toString()) - (Integer.parseInt(m2.getText().toString()) + Integer.parseInt(og2.getText().toString()));
        textView4.setText("Ваш результат" + ressul);
    }
}