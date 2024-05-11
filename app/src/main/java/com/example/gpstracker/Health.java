package com.example.gpstracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Health extends AppCompatActivity {
    private TextView massa;
    private TextView proportion;
    private TextView weight;
    private TextView strenght;
    private Button btn_massa;
    private Button btn_proportion;
    private Button btn_weight;
    private Button btn_strenght;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health);
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //  v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //  return insets;
        // });
        massa = findViewById(R.id.massa);
        proportion = findViewById(R.id.proportion);
        weight = findViewById(R.id.weight);
        strenght = findViewById(R.id.strength);
        btn_massa = findViewById(R.id.btn_massa);
        btn_proportion = findViewById(R.id.btn_proportion);
        btn_weight = findViewById(R.id.btn_weight);
        btn_strenght = findViewById(R.id.btn_strength);
        btn_massa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switcher = new Intent(Health.this, IndexMassa.class);
                Health.this.startActivity(switcher);
            }
        });
        btn_proportion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switcher = new Intent(Health.this, IndexPropartion.class);
                Health.this.startActivity(switcher);
            }
        });
        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switcher = new Intent(Health.this, Index_Weight_height.class);
                Health.this.startActivity(switcher);
            }
        });
        btn_strenght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switcher = new Intent(Health.this, Index_strenght.class);
                Health.this.startActivity(switcher);
            }
        });
    }
}