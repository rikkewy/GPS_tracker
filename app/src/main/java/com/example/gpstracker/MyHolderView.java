package com.example.gpstracker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolderView extends RecyclerView.ViewHolder {

    TextView dateOfTrainingTV, timeOfTrainingTV, distanceTV, bestSpeedTV, tempOfTrainingTV, stepsTV;
    public MyHolderView(@NonNull View itemView) {
        super(itemView);
        dateOfTrainingTV = itemView.findViewById(R.id.date_of_training);
        timeOfTrainingTV = itemView.findViewById(R.id.time_of_training);
        distanceTV = itemView.findViewById(R.id.distance);
        bestSpeedTV = itemView.findViewById(R.id.best_speed);
        tempOfTrainingTV = itemView.findViewById(R.id.temp_of_training);
        stepsTV = itemView.findViewById(R.id.steps);
    }
}
