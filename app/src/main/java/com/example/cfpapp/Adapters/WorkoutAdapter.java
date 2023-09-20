package com.example.cfpapp.Adapters;

import static java.util.Objects.isNull;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cfpapp.R;
import com.example.cfpapp.ExcerciseActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private ArrayList<DocumentSnapshot> workoutList;
    private OnItemClickListener clickListener;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public WorkoutAdapter(ArrayList<DocumentSnapshot> workoutList) {
        this.workoutList = workoutList;
    }

    private Context context; // Add a Context field

    public WorkoutAdapter(Context context, ArrayList<DocumentSnapshot> workoutList) {
        this.context = context;
        this.workoutList = workoutList;
    }

    public WorkoutAdapter(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public WorkoutAdapter(Context context, ArrayList<DocumentSnapshot> workoutList, OnItemClickListener clickListener) {
        this.context = context;
        this.workoutList = workoutList;
        this.clickListener = clickListener;
    }







    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        DocumentSnapshot currentWorkout = workoutList.get(position);
        holder.workoutNameTextView.setText(currentWorkout.get("workoutname").toString());
        holder.workoutDateTextView.setText(currentWorkout.get("date").toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition(); // Get the position from the parameter
                Log.d("RecyclerView" , "Item clicked"+ isNull(clickListener) + position);
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    DocumentSnapshot item = workoutList.get(position);
                    clickListener.onItemClick(position, item);

                    // Use the context to start the new activity
                    Intent intent = new Intent(context, ExcerciseActivity.class);
                    intent.putExtra("itemId", item.getId());
                    context.startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, DocumentSnapshot item);
    }

    public void updateData(ArrayList<DocumentSnapshot> newData) {
        workoutList.clear();
        workoutList.addAll(newData);
    }



    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        public TextView workoutNameTextView;
        public TextView workoutDateTextView;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            workoutNameTextView = itemView.findViewById(R.id.workoutNameTextView);
            workoutDateTextView = itemView.findViewById(R.id.workoutDateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("RecyclerView" + isNull(clickListener), "Item clicke d 2" +isNull(clickListener));


                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        Log.d("RecyclerView", "Item clicked at position " + position );
                        if (position != RecyclerView.NO_POSITION) {
                            DocumentSnapshot item = workoutList.get(position);
                            clickListener.onItemClick(position, item);

                        }
                    }
                }
            });


        }
    }
}

