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


public class IndexMassa extends AppCompatActivity {
    private TextView text;
    private TextView resul;
    private ImageView imt;

    private EditText mas;
    private EditText tal;
    private Button sum_but;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        text = findViewById(R.id.textView);
        resul = findViewById(R.id.res);
        imt = findViewById(R.id.imt);


    }
    public void sum(){
        int res;
        res = Integer.parseInt(mas.getText().toString()) /  (Integer.parseInt(tal.getText().toString())*Integer.parseInt(tal.getText().toString()));
        resul.setText("Ваш ИМТ" + res);

    }
}