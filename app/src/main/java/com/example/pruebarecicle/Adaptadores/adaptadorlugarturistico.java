package com.example.pruebarecicle.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pruebarecicle.Models.lugarturistico;
import com.example.pruebarecicle.R;

import java.util.ArrayList;

public class adaptadorlugarturistico extends ArrayAdapter<lugarturistico> {
    public adaptadorlugarturistico(Context context, ArrayList<lugarturistico> datos) {
        super(context, R.layout.lycard, datos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.lycard, null);

        ImageView imageView = item.findViewById(R.id.imview);
        Glide.with(getContext())
                .load(this.getItem(position).getImagenesGif())
                .into(imageView);

        TextView txtNombre = item.findViewById(R.id.txtNombre);
        txtNombre.setText(getItem(position).getNombreLugar());



        return item;
    }
}
