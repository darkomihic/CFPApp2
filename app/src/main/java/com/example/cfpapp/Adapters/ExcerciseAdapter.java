package com.example.cfpapp.Adapters;

import static java.util.Objects.isNull;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cfpapp.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExcerciseAdapter extends RecyclerView.Adapter<ExcerciseAdapter.ExcerciseViewHolder> {
    private ArrayList<DocumentSnapshot> excerciseList;
    private ExcerciseAdapter.OnItemClickListener clickListener;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private Context context; // Add a Context field

    public ExcerciseAdapter(Context context, ArrayList<DocumentSnapshot> workoutList, ExcerciseAdapter.OnItemClickListener clickListener) {
        this.context = context;
        this.excerciseList = workoutList;
        this.clickListener = clickListener;
    }



    @NonNull
    @Override
    public ExcerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item_layout, parent, false);
        return new ExcerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcerciseViewHolder holder, int position) {
        DocumentSnapshot currentWorkout = excerciseList.get(position);
        holder.excerciseRepRange.setText(currentWorkout.get("sets").toString());
        String exName = excerciseList.get(position).get("excerciseid").toString();
        holder.excerciseName.setText(exName);

        if(exName.equals("zgib")){
            holder.excerciseImage.setImageResource(R.drawable.pullup);
        }else if(exName.equals("benc")){
            holder.excerciseImage.setImageResource(R.drawable.bench);
        }else if(exName.equals("cucanj")){
            holder.excerciseImage.setImageResource(R.drawable.squat);
        }else if(exName.equals("sklek")){
            holder.excerciseImage.setImageResource(R.drawable.pushup);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition(); // Get the position from the parameter
                Log.d("RecyclerView" , exName);
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    DocumentSnapshot item = excerciseList.get(position);
                    clickListener.onItemClick(position, item);

                    // Use the context to start the new activity
                    //ovde napisati logiku da udje u opis za vezbu ako cu to dodati
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return excerciseList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, DocumentSnapshot item);
    }

    public void updateData(ArrayList<DocumentSnapshot> newData) {
        excerciseList.clear();
        excerciseList.addAll(newData);
    }

    public class ExcerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView excerciseName;
        public TextView excerciseRepRange;
        public ImageView excerciseImage;

        public ExcerciseViewHolder(View itemView) {
            super(itemView);
            excerciseName = itemView.findViewById(R.id.exerciseName);
            excerciseRepRange = itemView.findViewById(R.id.exerciseRepRange);
            excerciseImage = itemView.findViewById(R.id.exerciseImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("RecyclerView" + isNull(clickListener), "Item clicke d 2" +isNull(clickListener));


                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        Log.d("RecyclerView", "Item clicked at position " + position );
                        if (position != RecyclerView.NO_POSITION) {
                            DocumentSnapshot item = excerciseList.get(position);
                            clickListener.onItemClick(position, item);

                        }
                    }
                }
            });


        }
    }
}
