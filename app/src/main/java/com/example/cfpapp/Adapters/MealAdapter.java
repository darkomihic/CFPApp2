package com.example.cfpapp.Adapters;

import static java.util.Objects.isNull;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cfpapp.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private ArrayList<DocumentSnapshot> mealList;
    private OnItemClickListener clickListener;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public MealAdapter(ArrayList<DocumentSnapshot> workoutList) {
        this.mealList = workoutList;
    }

    private Context context; // Add a Context field

    public MealAdapter(Context context, ArrayList<DocumentSnapshot> workoutList) {
        this.context = context;
        this.mealList = workoutList;
    }

    public MealAdapter(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public MealAdapter(Context context, ArrayList<DocumentSnapshot> workoutList, OnItemClickListener clickListener) {
        this.context = context;
        this.mealList = workoutList;
        this.clickListener = clickListener;
    }







    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        DocumentSnapshot currentWorkout = mealList.get(position);
        holder.workoutNameTextView.setText(currentWorkout.get("name").toString());
        holder.workoutDateTextView.setText("Calories: "+currentWorkout.get("calories").toString()+" Proteins: "+currentWorkout.get("protein").toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*int position = holder.getAdapterPosition(); // Get the position from the parameter
                Log.d("RecyclerView" , "Item clicked"+ isNull(clickListener) + position);
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    DocumentSnapshot item = mealList.get(position);
                    clickListener.onItemClick(position, item);

                    // Use the context to start the new activity
                    Intent intent = new Intent(context, MealActivity.class);
                    intent.putExtra("itemId", item.getId());
                    context.startActivity(intent);
                }*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, DocumentSnapshot item);
    }

    public void updateData(ArrayList<DocumentSnapshot> newData) {
        mealList.clear();
        mealList.addAll(newData);
    }



    public class MealViewHolder extends RecyclerView.ViewHolder {
        public TextView workoutNameTextView;
        public TextView workoutDateTextView;

        public MealViewHolder(View itemView) {
            super(itemView);
            workoutNameTextView = itemView.findViewById(R.id.mealNameTextView);
            workoutDateTextView = itemView.findViewById(R.id.mealDateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("RecyclerView" + isNull(clickListener), "Item clicke d 2" +isNull(clickListener));


                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        Log.d("RecyclerView", "Item clicked at position " + position );
                        if (position != RecyclerView.NO_POSITION) {
                            DocumentSnapshot item = mealList.get(position);
                            clickListener.onItemClick(position, item);

                        }
                    }
                }
            });


        }
    }
}