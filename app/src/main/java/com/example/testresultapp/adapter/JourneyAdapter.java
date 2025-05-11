// app/src/main/java/com/example/testresultapp/adapter/JourneyAdapter.java
package com.example.testresultapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testresultapp.R;
import com.example.testresultapp.database.entity.TrainJourneyEntity;

import java.util.ArrayList;
import java.util.List;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.JourneyViewHolder> {
    private List<TrainJourneyEntity> journeys = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journey, parent, false);
        return new JourneyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyViewHolder holder, int position) {
        TrainJourneyEntity currentJourney = journeys.get(position);
        holder.textTrainNumber.setText(currentJourney.getTrainNumber());
        holder.textDate.setText(currentJourney.getDate());
    }

    @Override
    public int getItemCount() {
        return journeys.size();
    }

    public void setJourneys(List<TrainJourneyEntity> journeys) {
        this.journeys = journeys;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class JourneyViewHolder extends RecyclerView.ViewHolder {
        private TextView textTrainNumber;
        private TextView textDate;

        public JourneyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTrainNumber = itemView.findViewById(R.id.text_train_number);
            textDate = itemView.findViewById(R.id.text_date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(journeys.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TrainJourneyEntity journey);
    }
}