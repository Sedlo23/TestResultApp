// app/src/main/java/com/example/testresultapp/adapter/StationAdapter.java
package com.example.testresultapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testresultapp.R;
import com.example.testresultapp.database.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {
    private List<StationEntity> stations = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        StationEntity currentStation = stations.get(position);
        holder.textStationName.setText(currentStation.getName());
        holder.textArrivalTime.setText(currentStation.getPlannedArrivalTime());
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public void setStations(List<StationEntity> stations) {
        this.stations = stations;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class StationViewHolder extends RecyclerView.ViewHolder {
        private TextView textStationName;
        private TextView textArrivalTime;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            textStationName = itemView.findViewById(R.id.text_station_name);
            textArrivalTime = itemView.findViewById(R.id.text_arrival_time);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(stations.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(StationEntity station);
    }
}