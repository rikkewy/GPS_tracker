package com.example.gpstracker;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.distanceTV.setText(resultTrainings.get(position).getDistance());
        holder.stepsTV.setText(resultTrainings.get(position).getSteps());
    }

    @Override
    public int getItemCount() {
        return resultTrainings.size();
    }
}
