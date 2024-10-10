package com.example.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Weather;
import com.example.btl_libary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {
    private List<Weather.Forecast.Forecastday.Hour> hourlyData;

    public HourlyAdapter(List<Weather.Forecast.Forecastday.Hour> hourlyData) {
        this.hourlyData = hourlyData;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        Weather.Forecast.Forecastday.Hour hour = hourlyData.get(position);
        if (hour != null) {
            String time = hour.getTime().split(" ")[1];
            holder.hourTextView.setText(time);
            String iconUrl = "https:" + hour.getCondition().getIcon();
            Picasso.get().load(iconUrl).into(holder.weatherIcon);
            holder.temperatureTextView.setText(String.format("%s°C", hour.getTemp_c()));
        }
    }

    @Override
    public int getItemCount() {
        return hourlyData != null ? hourlyData.size() : 0;
    }

    public static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView hourTextView;
        TextView temperatureTextView;
        ImageView weatherIcon;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            hourTextView = itemView.findViewById(R.id.timeTextView);
            temperatureTextView = itemView.findViewById(R.id.tempTextView);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
        }
    }
}
