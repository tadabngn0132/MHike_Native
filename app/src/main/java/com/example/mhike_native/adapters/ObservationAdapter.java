package com.example.mhike_native.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike_native.R;
import com.example.mhike_native.models.Observation;

import org.jspecify.annotations.NonNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    private List<Observation> observationList = new ArrayList<>();

    public interface OnObservationListener {
        void onObservationClicked(long observationId, long hikeId);
    }

    private OnObservationListener onObservationListener;

    public void setOnClickedObservationListener(OnObservationListener listener) {
        this.onObservationListener = listener;
    }

    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        android.widget.TextView tvObservationName;
        android.widget.TextView tvObservationTimestamp;
        android.widget.TextView tvComments;
        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvObservationName = itemView.findViewById(R.id.tvObservationName);
            tvObservationTimestamp = itemView.findViewById(R.id.tvObservationTimestamp);
            tvComments = itemView.findViewById(R.id.tvComments);
        }
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observationList.get(position);
        holder.tvObservationName.setText(observation.getTitle());
        holder.tvObservationTimestamp.setText(observation.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")));
        holder.tvComments.setText(observation.getComments());

        holder.itemView.setOnClickListener(v -> {
            if (onObservationListener != null) {
                onObservationListener.onObservationClicked(observation.getId(), observation.getHike_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public void setObservationList(List<Observation> observations) {
        this.observationList = observations;
        notifyDataSetChanged();
    }
}
