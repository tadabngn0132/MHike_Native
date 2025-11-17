package com.example.mhike_native.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike_native.R;
import com.example.mhike_native.models.Hike;

import org.jspecify.annotations.NonNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {
    private List<Hike> hikeList = new ArrayList<>();

    public interface OnHikeListener {
        void onHikeClicked(long hikeId);
    }

    private OnHikeListener onHikeListener;

    public void setOnClickedHikeListener(OnHikeListener listener) {
        this.onHikeListener = listener;
    }

    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        android.widget.TextView tvHikeName;
        android.widget.TextView tvLocation;
        android.widget.TextView tvLength;
        android.widget.TextView tvDifficulty;
        android.widget.TextView tvDate;
        android.widget.TextView tvParkingAvailable;
        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLength = itemView.findViewById(R.id.tvLength);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvParkingAvailable = itemView.findViewById(R.id.tvParkingAvailable);
        }
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hike, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);
        holder.tvHikeName.setText(hike.getName());
        holder.tvLocation.setText(hike.getLocation());

        String lengthString = hike.getLength_km() + " km";
        holder.tvLength.setText(lengthString);
        holder.tvDifficulty.setText(hike.getDifficulty());
        if (hike.getDifficulty().equalsIgnoreCase("Easy")) {
            holder.tvDifficulty.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        } else if (hike.getDifficulty().equalsIgnoreCase("Moderate")) {
            holder.tvDifficulty.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange));
        } else {
            holder.tvDifficulty.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        }
        holder.tvDate.setText(hike.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        holder.tvParkingAvailable.setText(hike.isParking_available() ? "Yes" : "No");

        holder.itemView.setOnClickListener(v -> {
            if (onHikeListener != null) {
                onHikeListener.onHikeClicked(hike.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public void setHikeList(List<Hike> hikeList) {
        this.hikeList = hikeList;
        notifyDataSetChanged();
    }
}
