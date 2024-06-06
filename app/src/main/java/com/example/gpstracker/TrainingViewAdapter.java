package com.example.gpstracker;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrainingViewAdapter extends RecyclerView.Adapter<TrainingViewAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<TrainingView> trainings;
    DatabaseReference mDatabase;
    ArrayList<TrainingView> trainingViewArrayList;

    TrainingViewAdapter(Context context, List<TrainingView> trainings) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        trainingViewArrayList = new ArrayList<TrainingView>();
        this.trainings=new ArrayList<TrainingView>();
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(TrainingActivity.nameOfTraining).get().
                addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("myfirebase", "Error getting data", task.getException());
                        }
                        else {
                            GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                            Map<String, Object> value = task.getResult().getValue(genericTypeIndicator);

                            TrainingView trainingView = new TrainingView("jnjknkj", value.get("steps"), "kjh");
                            trainingViewArrayList.add(trainingView);
                            trainings.add(trainingView);
                            Log.v("firebase", "V " + value.get("steps"));
                        }
                    }
                });

        this.trainingViewArrayList.addAll(trainingViewArrayList);
        this.trainings.addAll(trainingViewArrayList);
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

