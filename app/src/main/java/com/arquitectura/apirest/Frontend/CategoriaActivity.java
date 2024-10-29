package com.arquitectura.apirest.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arquitectura.apirest.R;

public class CategoriaActivity extends AppCompatActivity {

    private CardView matematicasCard, geografiaCard, literaturaCard, entretenimientoCard, deportesCard;
    private Spinner difficultySpinner;
    private ImageView backBtn;
    private String dificultadSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        // Inicializar CardViews y botón de retroceso
        matematicasCard = findViewById(R.id.matematicas);
        geografiaCard = findViewById(R.id.geografia);
        literaturaCard = findViewById(R.id.literatura);
        entretenimientoCard = findViewById(R.id.entretenimiento);
        deportesCard = findViewById(R.id.deportes);
        backBtn = findViewById(R.id.backBtn);

        // Inicializar el Spinner de dificultad
        difficultySpinner = findViewById(R.id.difficultySpinner);

        // Obtener la dificultad seleccionada en el Spinner
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dificultadSeleccionada = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dificultadSeleccionada = "facil";
            }
        });

        // Listener para el botón de retroceso
        backBtn.setOnClickListener(v -> finish());

        matematicasCard.setOnClickListener(v -> abrirTrivia("matematicas"));
        geografiaCard.setOnClickListener(v -> abrirTrivia("geografia"));
        literaturaCard.setOnClickListener(v -> abrirTrivia("literatura"));
        entretenimientoCard.setOnClickListener(v -> abrirTrivia("entretenimiento"));
        deportesCard.setOnClickListener(v -> abrirTrivia("deportes"));
    }

    private void abrirTrivia(String categoria) {
        Intent intent = new Intent(CategoriaActivity.this, PreguntaActivity.class);
        intent.putExtra("CATEGORIA_SELECCIONADA", categoria);
        intent.putExtra("DIFICULTAD_SELECCIONADA", dificultadSeleccionada);
        startActivity(intent);
    }
}