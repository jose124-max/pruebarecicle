package com.example.pruebarecicle.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pruebarecicle.Models.lugarturistico;
import com.example.pruebarecicle.R;

import java.util.ArrayList;

public class AdaptadorLugarTuristico extends RecyclerView.Adapter<AdaptadorLugarTuristico.LugarTuristicoViewHolder> {

    private Context context;
    private ArrayList<lugarturistico> datos;

    public AdaptadorLugarTuristico(Context context, ArrayList<lugarturistico> datos) {
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public LugarTuristicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lycard, parent, false);
        return new LugarTuristicoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LugarTuristicoViewHolder holder, int position) {
        lugarturistico lugarTuristico = datos.get(position);

        Glide.with(context)
                .load(lugarTuristico.getLogo())
                .into(holder.imageView);

        holder.txtNombre.setText(lugarTuristico.getNombreLugar());
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class LugarTuristicoViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtNombre;

        public LugarTuristicoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imview);
            txtNombre = itemView.findViewById(R.id.txtNombre);
        }
    }
}