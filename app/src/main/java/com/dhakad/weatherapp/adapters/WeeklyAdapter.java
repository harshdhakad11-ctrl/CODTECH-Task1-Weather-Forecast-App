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
import com.dhakad.weatherapp.models.WeeklyModel;

import java.util.ArrayList;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.ViewHolder> {

    private ArrayList<WeeklyModel> list;

    public WeeklyAdapter(ArrayList<WeeklyModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weekly, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeeklyModel model = list.get(position);

        holder.txtDay.setText(model.getDay());

        holder.txtCondition.setText(model.getCondition());

        // Show Max / Min Temperature
        holder.txtTemp.setText(
                model.getMaxTemp() + " / " + model.getMinTemp()
        );

        Glide.with(holder.itemView.getContext())
                .load(model.getIcon())
                .into(holder.imgIcon);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDay, txtCondition, txtTemp;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDay = itemView.findViewById(R.id.txtWeekDay);
            txtCondition = itemView.findViewById(R.id.txtWeekCondition);
            txtTemp = itemView.findViewById(R.id.txtWeekTemp);
            imgIcon = itemView.findViewById(R.id.imgWeekIcon);
        }
    }
}