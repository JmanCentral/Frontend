package com.arquitectura.apirest.Frontend;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.arquitectura.apirest.Entidades.Historial;
import com.arquitectura.apirest.Entidades.Pregunta;
import com.arquitectura.apirest.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import Utils.APIS;
import Utils.HistorialService;
import Utils.PreguntaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreguntaActivity extends AppCompatActivity {

    private TextView categoriaText, questionNumber, questionText;
    private TextView op1Text, op2Text, op3Text, op4Text, puntajeText, tiempoText;
    private CardView op1, op2, op3, op4;
    private List<Pregunta> preguntas;
    private int preguntaActual = 0;
    private String categoria, dificultad;
    private PreguntaService preguntaService;
    private HistorialService historialService;
    private Long idUsuario;
    private int puntaje = 0;
    private int ayudas = 0;
    private boolean ayudaUsada = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private CountDownTimer countDownTimer;  // Temporizador
    private long tiempoTotal = 0;
    private long tiempoRestante = 10;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregunta);

        // Inicialización de vistas
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
        puntajeText = findViewById(R.id.txt_puntaje);
        tiempoText = findViewById(R.id.txt_tiempo);

        // Obtener datos desde el intent
        categoria = getIntent().getStringExtra("CATEGORIA_SELECCIONADA");
        dificultad = getIntent().getStringExtra("DIFICULTAD_SELECCIONADA");
        idUsuario = getIntent().getLongExtra("ID", -1L);

        categoriaText.setText(categoria);

        // Configurar Retrofit
        preguntaService = APIS.getPreguntaService();
        historialService = APIS.getHistorialService();
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

        ayudaUsada = false;
        if (preguntaActual < preguntas.size()) {
            // Restablecer colores de las opciones antes de mostrar la nueva pregunta
            op1.setCardBackgroundColor(Color.WHITE);
            op2.setCardBackgroundColor(Color.WHITE);
            op3.setCardBackgroundColor(Color.WHITE);
            op4.setCardBackgroundColor(Color.WHITE);

            Pregunta pregunta = preguntas.get(preguntaActual);
            questionNumber.setText("Pregunta " + (preguntaActual + 1));
            questionText.setText(pregunta.getPregunta());
            op1Text.setText(pregunta.getOp1());
            op2Text.setText(pregunta.getOp2());
            op3Text.setText(pregunta.getOp3());
            op4Text.setText(pregunta.getOp4());
        }
        iniciarTemporizador();
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
            puntaje += 10;
            opcionSeleccionada.setCardBackgroundColor(Color.GREEN);
        } else {
            puntaje -= 5;
            opcionSeleccionada.setCardBackgroundColor(Color.RED);
            mostrarRespuestaCorrecta(respuestaCorrecta);
        }

        // Actualizar puntaje en puntajeText
        puntajeText.setText("Puntaje: " + puntaje);

        new Handler().postDelayed(() -> {
            preguntaActual++;
            if (preguntaActual < preguntas.size()) {
                mostrarPregunta();
            } else {
                if (!historialRegistrado) {
                    registrarHistorial();
                    historialRegistrado = true;
                }
                Toast.makeText(PreguntaActivity.this, "Juego terminado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, 500);
    }

    private void mostrarRespuestaCorrecta(String respuestaCorrecta) {
        if (op1Text.getText().toString().equals(respuestaCorrecta)) {
            op1.setCardBackgroundColor(Color.GREEN);
        } else if (op2Text.getText().toString().equals(respuestaCorrecta)) {
            op2.setCardBackgroundColor(Color.GREEN);
        } else if (op3Text.getText().toString().equals(respuestaCorrecta)) {
            op3.setCardBackgroundColor(Color.GREEN);
        } else if (op4Text.getText().toString().equals(respuestaCorrecta)) {
            op4.setCardBackgroundColor(Color.GREEN);
        }
    }

    private void registrarHistorial() {
        Pregunta pregunta = preguntas.get(preguntaActual - 1);  // Última pregunta respondida
        String fecha = dateFormat.format(new Date());

        // Convertir tiempo total de milisegundos a formato de tiempo (mm:ss)
        long minutos = tiempoTotal / 60;
        long segundos = tiempoTotal % 60;
        String tiempo = String.format("%02d:%02d", minutos, segundos);

        Historial historial = new Historial(
                null, puntaje, fecha, tiempo, ayudas, idUsuario, pregunta.getId_pregunta(),
                "Usuario", categoria, dificultad
        );

        Call<Historial> call = historialService.registrarHistorial(historial);
        call.enqueue(new Callback<Historial>() {
            @Override
            public void onResponse(Call<Historial> call, Response<Historial> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PreguntaActivity.this, "Historial registrado correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreguntaActivity.this, "Error al registrar historial.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Historial> call, Throwable t) {
                Toast.makeText(PreguntaActivity.this, "Error en la conexión con el servidor.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void eliminarunapregunta(View view) {

        if (!ayudaUsada) {  // Verificar que no se haya usado una ayuda en la pregunta actual
            ayudaUsada = true;  // Marcar que la ayuda fue usada
            ayudas++;  // Incrementar el contador de ayudas

            Pregunta pregunta = preguntas.get(preguntaActual);
            String respuestaCorrecta = pregunta.getRespuesta();

            // Crear una lista con las opciones incorrectas
            List<CardView> opcionesIncorrectas = new ArrayList<>();
            if (!op1Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op1);
            if (!op2Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op2);
            if (!op3Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op3);
            if (!op4Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op4);

            // Seleccionar una opción incorrecta al azar
            if (!opcionesIncorrectas.isEmpty()) {
                int randomIndex = new Random().nextInt(opcionesIncorrectas.size());
                opcionesIncorrectas.get(randomIndex).setCardBackgroundColor(Color.RED);
            }

            puntaje -= 3;  // Descontar 3 puntos por usar esta ayuda
            puntajeText.setText("Puntaje: " + puntaje);
        }


    }

    public void eliminardospreguntas(View view) {

        if (!ayudaUsada) {  // Verificar que no se haya usado una ayuda en la pregunta actual
            ayudaUsada = true;  // Marcar que la ayuda fue usada
            ayudas++;  // Incrementar el contador de ayudas

            Pregunta pregunta = preguntas.get(preguntaActual);
            String respuestaCorrecta = pregunta.getRespuesta();

            // Crear una lista con las opciones incorrectas
            List<CardView> opcionesIncorrectas = new ArrayList<>();
            if (!op1Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op1);
            if (!op2Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op2);
            if (!op3Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op3);
            if (!op4Text.getText().toString().equals(respuestaCorrecta)) opcionesIncorrectas.add(op4);

            // Seleccionar dos opciones incorrectas al azar
            if (opcionesIncorrectas.size() >= 2) {
                Collections.shuffle(opcionesIncorrectas);
                opcionesIncorrectas.get(0).setCardBackgroundColor(Color.RED);
                opcionesIncorrectas.get(1).setCardBackgroundColor(Color.RED);
            }

            puntaje -= 5;  // Descontar 5 puntos por usar esta ayuda
            puntajeText.setText("Puntaje: " + puntaje);
        }
    }
    private void iniciarTemporizador() {
        if (countDownTimer != null) {
            countDownTimer.cancel();  // Cancela el temporizador anterior si existe
        }

        tiempoRestante = 10;  // Reinicia el tiempo a 10 segundos
        tiempoText.setText(String.valueOf(tiempoRestante));

        countDownTimer = new CountDownTimer(10000, 1000) {  // 10 segundos, cuenta regresiva cada 1 segundo
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished / 1000;
                tiempoText.setText(String.valueOf(tiempoRestante));
            }

            @Override
            public void onFinish() {
                tiempoRestante = 0;
                tiempoText.setText(String.valueOf(tiempoRestante));
                Toast.makeText(PreguntaActivity.this, "Tiempo alcanzado", Toast.LENGTH_SHORT).show();
                puntaje -= 5;  // Descontar 5 puntos si el tiempo se acaba
                puntajeText.setText("Puntaje: " + puntaje);
                mostrarSiguientePregunta();  // Mostrar la siguiente pregunta cuando el tiempo termina
            }
        }.start();
    }

    private boolean historialRegistrado = false;
    private void mostrarSiguientePregunta() {
        tiempoTotal += (10 - tiempoRestante);  // Sumar el tiempo usado para responder esta pregunta

        if (preguntaActual < preguntas.size() - 1) {
            preguntaActual++;
            mostrarPregunta();  // Mostrar la nueva pregunta
        } else {
            if (!historialRegistrado) {
                registrarHistorial();  // Registrar el historial al terminar todas las preguntas
                historialRegistrado = true;  // Marcar como registrado para evitar duplicados
            }
        }
    }

    public void onFinish() {
        tiempoRestante = 0;
        tiempoText.setText(String.valueOf(tiempoRestante));
        Toast.makeText(PreguntaActivity.this, "Tiempo alcanzado", Toast.LENGTH_SHORT).show();
        puntaje -= 5;  // Descontar 5 puntos si el tiempo se acaba
        puntajeText.setText("Puntaje: " + puntaje);
        mostrarSiguientePregunta();  // Mostrar la siguiente pregunta cuando el tiempo termina
    }

}


