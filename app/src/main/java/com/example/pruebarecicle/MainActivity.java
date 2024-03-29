package com.example.pruebarecicle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebarecicle.Adaptadores.AdaptadorLugarTuristico;
import com.example.pruebarecicle.Models.Categorias;
import com.example.pruebarecicle.Models.lugarturistico;
import com.example.pruebarecicle.Models.subcategoria;
import com.example.pruebarecicle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class MainActivity extends AppCompatActivity implements Asynchtask {

    private Spinner spCategorias;
    private Spinner spSubcate;
    Categorias selectedCategoria;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spCategorias = findViewById(R.id.spCateogiras);
        spSubcate = findViewById(R.id.spSubcate);

        recyclerView = (RecyclerView) findViewById(R.id.recturismo);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Map<String, String> datosCategorias = new HashMap<>();
        WebService wsCategorias = new WebService("https://uealecpeterson.net/turismo/categoria/getlistadoCB",
                datosCategorias, MainActivity.this, MainActivity.this);
        wsCategorias.execute("GET");

        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategoria = (Categorias) parentView.getItemAtPosition(position);
                obtenerSubcategorias(selectedCategoria.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        }


        );
        spSubcate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                subcategoria selectedSubCategoria = (subcategoria) parentView.getItemAtPosition(position);
                TextView titulo = findViewById(R.id.textView6);
                titulo.setText(selectedSubCategoria.getDescripcion());
                obtenerlugares(selectedCategoria.getId(),selectedSubCategoria.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        }
        );

    }

    private void obtenerSubcategorias(String idCategoria) {
        Map<String, String> datosSubcategorias = new HashMap<>();

        WebService wsSubcategorias = new WebService("https://uealecpeterson.net/turismo/subcategoria/getlistadoCB/"+idCategoria,
                datosSubcategorias, MainActivity.this, new Asynchtask() {
            @Override
            public void processFinish(String result) {
                try {
                    JSONArray JSONSubcategorias = new JSONArray(result);

                    List<subcategoria> subcategorias = new ArrayList<>();
                    for (int i = 0; i < JSONSubcategorias.length(); i++) {
                        JSONObject subcategoriaJSON = JSONSubcategorias.getJSONObject(i);
                        subcategoria subcategoria = new subcategoria(subcategoriaJSON);
                        subcategorias.add(subcategoria);
                    }

                    ArrayAdapter<subcategoria> adapterSubcategorias = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, subcategorias);
                    spSubcate.setAdapter(adapterSubcategorias);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error al procesar los datos JSON de subcategorías", Toast.LENGTH_SHORT).show();
                }
            }
        });

        wsSubcategorias.execute("GET");
    }

    private void obtenerlugares(String idcat,String idsub) {
        Map<String, String> lugares = new HashMap<>();

        WebService wslugares = new WebService("https://uealecpeterson.net/turismo/lugar_turistico/json_getlistadoGridLT/"+idcat+"/"+idsub,
                lugares, MainActivity.this, new Asynchtask() {
            @Override
            public void processFinish(String result) {
                try {
                    JSONObject jsonData = new JSONObject(result);
                    JSONArray JSONDataArray = jsonData.getJSONArray("data");

                    ArrayList<lugarturistico> lugaresTuristicos = new ArrayList<>();
                    for (int i = 0; i < JSONDataArray.length(); i++) {
                        JSONObject lugarTuristicoJSON = JSONDataArray.getJSONObject(i);
                        lugarturistico lugarTuristico = new lugarturistico(lugarTuristicoJSON);
                        lugaresTuristicos.add(lugarTuristico);
                    }
                    AdaptadorLugarTuristico adapatorl = new AdaptadorLugarTuristico(recyclerView.getContext(), lugaresTuristicos);


                    recyclerView.setAdapter(adapatorl);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error al procesar los datos JSON de subcategorías", Toast.LENGTH_SHORT).show();
                }
            }
        });

        wslugares.execute("GET");
    }

    @Override
    public void processFinish(String result) {
        try {
            JSONArray JSONCategorias = new JSONArray(result);

            List<Categorias> categorias = new ArrayList<>();
            for (int i = 0; i < JSONCategorias.length(); i++) {
                JSONObject categoriaJSON = JSONCategorias.getJSONObject(i);
                String idCategoria = categoriaJSON.getString("id");
                String descripcionCategoria = categoriaJSON.getString("descripcion");
                categorias.add(new Categorias(idCategoria, descripcionCategoria));
            }

            ArrayAdapter<Categorias> adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
            spCategorias.setAdapter(adapterCategorias);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al procesar los datos JSON de categorías", Toast.LENGTH_SHORT).show();
        }
    }
}
