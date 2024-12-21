package com.example.gpstracker;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyHolderView> {

    Context context;
    List<ResultTraining> resultTrainings;

    public MyAdapter(Context context, List<ResultTraining> resultTrainings) {
        this.context = context;
        this.resultTrainings = resultTrainings;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolderView(LayoutInflater.from(context).inflate(R.layout.list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView holder, int position) {
        holder.dateOfTrainingTV.setText("Дата тренировки: " + resultTrainings.get(position).getDateOfTraining());
        holder.timeOfTrainingTV.setText("Время тренировки: " + resultTrainings.get(position).getTimeOfTraining());
        holder.distanceTV.setText("Дистанция: " + resultTrainings.get(position).getDistance());
        holder.bestSpeedTV.setText("Лучшая скорость: " +resultTrainings.get(position).getBestSpeed());
        holder.tempOfTrainingTV.setText("Темп: " + resultTrainings.get(position).getTempOfTraining());
        holder.stepsTV.setText("Шаги: " + resultTrainings.get(position).getSteps());

        //holder.itemView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Toast.makeText(context.getApplicationContext(), "Намите и удерживайте, чтобы удалить тренировку", Toast.LENGTH_LONG).show();
        //    }
        //});
        //holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        //    @Override
        //    public boolean onLongClick(View v) {
        //        int actualPosition = holder.getAdapterPosition();
        //        removeItem(holder.getAdapterPosition(), actualPosition);
//
        //        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
        //                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Training"+TrainingActivity.countOfTraining);
        //        mPostReference.removeValue();
//
//
        //        TrainingActivity.countOfTraining--;
        //        return false;
        //    }
        //});
    }
    private void removeItem(int position, int actualPosition) {
        resultTrainings.remove(actualPosition);
        notifyItemRemoved(actualPosition);
        notifyItemRangeChanged(actualPosition, resultTrainings.size());
    }

    @Override
    public int getItemCount() {
        return resultTrainings.size();
    }
}
