package com.arquitectura.apirest.Frontend;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.arquitectura.apirest.R;

import Utils.APIS;
import Utils.HistorialService;

public class HistorialActivity extends AppCompatActivity {

    TextView username, PuntosTotales, fecha, ayudas, Tiempo,  Categoria , Dificultad;
    RecyclerView recyclerView;
    HistorialAdapter historialAdapter;
    HistorialService historialService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial);

        username = findViewById(R.id.username);
        PuntosTotales = findViewById(R.id.overallPoints);
        fecha = findViewById(R.id.Fecha);
        ayudas= findViewById(R.id.Ayudas);
        Tiempo = findViewById(R.id.Tiempo);
        Categoria = findViewById(R.id.Categoria);
        Dificultad = findViewById(R.id.Dificultad);
        recyclerView = findViewById(R.id.recyclerView);
        historialService = APIS.getHistorialService();

    }

}