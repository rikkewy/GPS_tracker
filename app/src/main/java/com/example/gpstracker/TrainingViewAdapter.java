package com.example.gpstracker;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrainingViewAdapter extends RecyclerView.Adapter<TrainingViewAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<TrainingView> trainings;

    TrainingViewAdapter(Context context, List<TrainingView> trainings) {
        this.trainings = trainings;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public TrainingViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_training, parent, false);
        return new ViewHolder(view);
    }

    //public void set(TrainingView trainingView){
    //    this.trainings.add(trainingView);
    //    notifyDataSetChanged();
    //}

    @Override
    public void onBindViewHolder(TrainingViewAdapter.ViewHolder holder, int position) {
        TrainingView trainingView = trainings.get(position);
        holder.dateOfTrainingEt.setText(String.valueOf(trainingView.getDateOfTraining()));
        holder.distanceOfTrainingEt.setText(String.valueOf(trainingView.getDistanceOfTraining()));
        holder.timeOfTrainingEt.setText(String.valueOf(trainingView.getTimeOfTraining()));
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView dateOfTrainingEt,distanceOfTrainingEt, timeOfTrainingEt;
        ViewHolder(View view){
            super(view);
            dateOfTrainingEt = view.findViewById(R.id.date_of_training);
            distanceOfTrainingEt = view.findViewById(R.id.distance_of_training);
            timeOfTrainingEt = view.findViewById(R.id.time_of_training);
        }
    }
}

