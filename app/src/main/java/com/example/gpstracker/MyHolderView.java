package com.example.gpstracker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolderView extends RecyclerView.ViewHolder {

    TextView distanceTV, stepsTV;
    public MyHolderView(@NonNull View itemView) {
        super(itemView);
        distanceTV = itemView.findViewById(R.id.distance);
        stepsTV = itemView.findViewById(R.id.steps);
    }
}
