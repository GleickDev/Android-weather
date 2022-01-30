package com.gleickapps.weather.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gleickapps.weather.R;
import com.gleickapps.weather.model.City;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<City> citiesList;
    private ItemClickListener mClickListener;

    public RecyclerAdapter(ArrayList<City> citiesList) {
        this.citiesList = citiesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCityName;


        public MyViewHolder(@NonNull View view) {
            super(view);
            tvCityName = view.findViewById(R.id.tv_city_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String name = citiesList.get(position).getCityName();
        holder.tvCityName.setText(name);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    // Permite que eventos de cliques sejam capturados
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // A atividade pai implementará este método para responder a eventos de clique
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
