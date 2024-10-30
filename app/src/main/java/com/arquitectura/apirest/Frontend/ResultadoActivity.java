package com.arquitectura.apirest.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.arquitectura.apirest.Frontend.PreguntaActivity;
import com.arquitectura.apirest.R;

public class ResultadoActivity extends AppCompatActivity {

    // Declaración de las vistas
    private TextView categoria, correct, incorrect, puntaje, fecha, tiempo;
    private Button startAgainBtn;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado); // Asegúrate de que el nombre del layout sea correcto

        // Enlazar vistas
        categoria = findViewById(R.id.categoria);
        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        puntaje = findViewById(R.id.puntaje);
        fecha = findViewById(R.id.fecha);
        tiempo = findViewById(R.id.tiempo);
        startAgainBtn = findViewById(R.id.startAgainBtn);
        backBtn = findViewById(R.id.backBtn);

        // Recibir datos pasados a través de un Intent (opcional)
        Intent intent = getIntent();
        if (intent != null) {
            String categoriaText = intent.getStringExtra("categoria");
            int correctas = intent.getIntExtra("correctas", 0);
            int incorrectas = intent.getIntExtra("incorrectas", 0);
            int puntajeText = intent.getIntExtra("puntaje", 0);
            String fechaText = intent.getStringExtra("fecha");
            String tiempoText = intent.getStringExtra("tiempo");

            // Mostrar los datos recibidos
            categoria.setText(categoriaText);
            correct.setText(String.valueOf(correctas));
            incorrect.setText(String.valueOf(incorrectas));
            puntaje.setText(String.valueOf(puntajeText));
            fecha.setText(fechaText);
            tiempo.setText(tiempoText);
        }

        startAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para empezar de nuevo
                Intent restartIntent = new Intent(ResultadoActivity.this, PreguntaActivity.class);
                startActivity(restartIntent);
                finish(); // Cierra la actividad actual para evitar volver atrás
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Vuelve a la actividad anterior
            }
        });
    }
}
