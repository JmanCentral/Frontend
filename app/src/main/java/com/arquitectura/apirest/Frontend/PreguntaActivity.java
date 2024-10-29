package com.arquitectura.apirest.Frontend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arquitectura.apirest.Entidades.Pregunta;
import com.arquitectura.apirest.R;

import java.util.List;

import Utils.APIS;
import Utils.PreguntaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreguntaActivity extends AppCompatActivity {

    private TextView categoriaText, questionNumber, questionText;
    private TextView op1Text, op2Text, op3Text, op4Text;
    private CardView op1, op2, op3, op4;
    private List<Pregunta> preguntas;
    private int preguntaActual = 0;
    private String categoria, dificultad;
    private PreguntaService preguntaService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregunta);

        categoriaText = findViewById(R.id.categoria);
        questionNumber = findViewById(R.id.questionNumber);
        questionText = findViewById(R.id.questionText);
        op1Text = findViewById(R.id.op1Text);
        op2Text = findViewById(R.id.op2Text);
        op3Text = findViewById(R.id.op3Text);
        op4Text = findViewById(R.id.op4Text);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);

        categoria = getIntent().getStringExtra("CATEGORIA_SELECCIONADA");
        dificultad = getIntent().getStringExtra("DIFICULTAD_SELECCIONADA");
        categoriaText.setText(categoria);

        // Configurar Retrofit
        preguntaService = APIS.getPreguntaService();
        cargarPreguntas();

        // Configurar eventos de clic para las opciones
        configurarClickOpciones();

    }

    private void cargarPreguntas() {
        Call<List<Pregunta>> call = preguntaService.obtenerPreguntasPorCategoriaYDificultad(categoria, dificultad);
        call.enqueue(new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    preguntas = response.body();
                    mostrarPregunta();
                } else {
                    Toast.makeText(PreguntaActivity.this, "No se encontraron preguntas.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                Toast.makeText(PreguntaActivity.this, "Error al cargar preguntas.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void mostrarPregunta() {
        if (preguntaActual < preguntas.size()) {
            Pregunta pregunta = preguntas.get(preguntaActual);
            questionNumber.setText("Pregunta " + (preguntaActual + 1));
            questionText.setText(pregunta.getPregunta());
            op1Text.setText(pregunta.getOp1());
            op2Text.setText(pregunta.getOp2());
            op3Text.setText(pregunta.getOp3());
            op4Text.setText(pregunta.getOp4());
        }
    }

    private void configurarClickOpciones() {
        View.OnClickListener listener = view -> {
            CardView opcionSeleccionada = (CardView) view;
            verificarRespuesta(opcionSeleccionada);
        };

        op1.setOnClickListener(listener);
        op2.setOnClickListener(listener);
        op3.setOnClickListener(listener);
        op4.setOnClickListener(listener);
    }

    @SuppressLint("NonConstantResourceId")
    private void verificarRespuesta(CardView opcionSeleccionada) {
        Pregunta pregunta = preguntas.get(preguntaActual);
        String respuestaCorrecta = pregunta.getRespuesta();
        String respuestaUsuario = "";

        if (opcionSeleccionada == op1) {
            respuestaUsuario = op1Text.getText().toString();
        } else if (opcionSeleccionada == op2) {
            respuestaUsuario = op2Text.getText().toString();
        } else if (opcionSeleccionada == op3) {
            respuestaUsuario = op3Text.getText().toString();
        } else if (opcionSeleccionada == op4) {
            respuestaUsuario = op4Text.getText().toString();
        }

        if (respuestaUsuario.equals(respuestaCorrecta)) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrecto. La respuesta correcta es: " + respuestaCorrecta, Toast.LENGTH_SHORT).show();
        }

        // Cargar la siguiente pregunta
        preguntaActual++;
        if (preguntaActual < preguntas.size()) {
            mostrarPregunta();
        } else {
            Toast.makeText(this, "Has terminado el cuestionario.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}