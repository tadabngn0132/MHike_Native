package com.example.mhike_native.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike_native.R;
import com.example.mhike_native.models.Hike;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {
    private List<Hike> hikeList = new ArrayList<>();

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
        holder.tvLength.setText(String.valueOf(hike.getLength_km()));
        holder.tvDifficulty.setText(hike.getDifficulty());
        holder.tvDate.setText(hike.getDate().toString());
        holder.tvParkingAvailable.setText(hike.isParking_available() ? "Yes" : "No");
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
