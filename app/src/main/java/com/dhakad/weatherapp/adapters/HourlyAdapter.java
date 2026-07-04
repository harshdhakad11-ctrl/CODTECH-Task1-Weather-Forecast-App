package com.dhakad.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dhakad.weatherapp.R;
import com.dhakad.weatherapp.models.HourlyModel;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    private ArrayList<HourlyModel> list;

    public HourlyAdapter(ArrayList<HourlyModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HourlyModel model = list.get(position);

        holder.txtTime.setText(model.getTime());

        holder.txtTemp.setText(model.getTemp());

        Glide.with(holder.itemView.getContext())
                .load(model.getIcon())
                .into(holder.imgIcon);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTime, txtTemp;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTime = itemView.findViewById(R.id.txtHourTime);
            txtTemp = itemView.findViewById(R.id.txtHourTemp);
            imgIcon = itemView.findViewById(R.id.imgHourIcon);

        }
    }
}