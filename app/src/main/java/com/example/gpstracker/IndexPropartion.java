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

public class IndexPropartion extends AppCompatActivity {
    private TextView text1;
    private ImageView prop;
    private TextView res1;
    private EditText chest;
    private EditText tall1;
    private Button btn;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index_propartion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        text1 = findViewById(R.id.text_propor);
        prop = findViewById(R.id.propt);
        res1 = findViewById(R.id.res1);
        chest = findViewById(R.id.og);
        tall1 = findViewById(R.id.bt_tall);
        btn = findViewById(R.id.btn_propsum);
    }
    public void mean(){
        int result1;
        result1 =   Integer.parseInt(chest.getText().toString()) /  Integer.parseInt(tall1.getText().toString()) *100;
        res1.setText("Ваш результат" + result1);
    }
}