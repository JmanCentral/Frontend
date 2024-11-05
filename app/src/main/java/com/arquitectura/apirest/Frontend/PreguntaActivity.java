package com.arquitectura.apirest.Frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.arquitectura.apirest.DAOS.PreguntaDao;
import com.arquitectura.apirest.Databsae.AppDatabase;
import com.arquitectura.apirest.Entidades.Historial;
import com.arquitectura.apirest.Entidades.Pregunta;
import com.arquitectura.apirest.MusicaApp.MusicService;
import com.arquitectura.apirest.R;
import com.arquitectura.apirest.Room.HistorialRoom;
import com.arquitectura.apirest.Room.PreguntaRoom;

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
    private CountDownTimer countDownTimer;
    private long tiempoInicio;
    private long tiempoRestante = 10;
    private AppDatabase appDatabase;
    SharedPreferences sharedPreferences;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregunta);

        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.putExtra("MUSIC_RESOURCE", R.raw.musica); // Recurso de música diferente
        startService(serviceIntent);

        tiempoInicio = System.currentTimeMillis();
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

        appDatabase = AppDatabase.getDatabase(getApplicationContext());


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

                    guardarPreguntasEnRoom(preguntas);

                    sincronizarPreguntasEnRoom(preguntas);

                    mostrarPregunta();
                } else {
                    Toast.makeText(PreguntaActivity.this, "No se encontraron preguntas en el servidor, cargando desde Room.", Toast.LENGTH_SHORT).show();
                    cargarPreguntasDesdeRoom();
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                // Si falla la conexión con el servidor, cargar las preguntas desde Room
                Toast.makeText(PreguntaActivity.this, "Error al conectar con el servidor, cargando preguntas desde Room.", Toast.LENGTH_SHORT).show();
                cargarPreguntasDesdeRoom();
            }
        });
    }

    private void sincronizarPreguntasEnRoom(List<Pregunta> preguntasDelBackend) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            List<PreguntaRoom> preguntasExistentes = db.preguntaDao().obtenerTodasLasPreguntas(); // Método para obtener todas las preguntas
            Set<String> preguntasDelBackendSet = preguntasDelBackend.stream()
                    .map(Pregunta::getPregunta) // Suponiendo que tienes un método que obtiene el texto de la pregunta
                    .collect(Collectors.toSet());

            for (PreguntaRoom pregunta : preguntasExistentes) {
                if (!preguntasDelBackendSet.contains(pregunta.getPregunta())) {
                    // La pregunta no está en el backend, la eliminamos
                    eliminarPreguntaEnRoom(pregunta);
                }
            }
        }).start();
    }

    // Método para eliminar una pregunta en Room
    private void eliminarPreguntaEnRoom(PreguntaRoom pregunta) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            db.preguntaDao().eliminarPregunta(pregunta); // Usando el método @Delete
        }).start();
    }

    private void cargarPreguntasDesdeRoom() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            List<PreguntaRoom> preguntasRoom = db.preguntaDao().obtenerPreguntasPorCategoriaYDificultad(categoria, dificultad);
            runOnUiThread(() -> {
                if (preguntasRoom != null && !preguntasRoom.isEmpty()) {
                    // Convertir PreguntaRoom a Pregunta para reutilizar en la lógica existente
                    preguntas = convertirPreguntasRoomAPreguntas(preguntasRoom);
                    mostrarPregunta();
                } else {
                    Toast.makeText(PreguntaActivity.this, "No hay preguntas disponibles en Room.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    // Método para guardar preguntas en Room
    private void guardarPreguntasEnRoom(List<Pregunta> preguntas) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            for (Pregunta pregunta : preguntas) {
                // Verificar si la pregunta ya existe en Room
                PreguntaRoom preguntaExistente = db.preguntaDao().obtenerPreguntaPorTextoYCategoria(pregunta.getPregunta(), pregunta.getCategoria());
                if (preguntaExistente == null) {
                    // Si no existe, insertar la nueva pregunta
                    PreguntaRoom nuevaPreguntaRoom = new PreguntaRoom(
                            null,
                            pregunta.getPregunta(),
                            pregunta.getOp1(),
                            pregunta.getOp2(),
                            pregunta.getOp3(),
                            pregunta.getOp4(),
                            pregunta.getRespuesta(),
                            pregunta.getDificultad(),
                            pregunta.getCategoria()
                    );
                    db.preguntaDao().insertarPregunta(nuevaPreguntaRoom);
                } else {
                    preguntaExistente.setOp1(pregunta.getOp1());
                    preguntaExistente.setOp2(pregunta.getOp2());
                    preguntaExistente.setOp3(pregunta.getOp3());
                    preguntaExistente.setOp4(pregunta.getOp4());
                    preguntaExistente.setRespuesta(pregunta.getRespuesta());
                    preguntaExistente.setDificultad(pregunta.getDificultad());
                    db.preguntaDao().actualizarPregunta(preguntaExistente);
                }
            }
        }).start();
    }

    // Convertir PreguntaRoom a Pregunta
    private List<Pregunta> convertirPreguntasRoomAPreguntas(List<PreguntaRoom> preguntasRoom) {
        List<Pregunta> listaPreguntas = new ArrayList<>();
        for (PreguntaRoom preguntaRoom : preguntasRoom) {
            Pregunta pregunta = new Pregunta(
                    preguntaRoom.getId(),
                    preguntaRoom.getPregunta(),
                    preguntaRoom.getOp1(),
                    preguntaRoom.getOp2(),
                    preguntaRoom.getOp3(),
                    preguntaRoom.getOp4(),
                    preguntaRoom.getRespuesta(),
                    preguntaRoom.getDificultad(),
                    preguntaRoom.getCategoria()
            );
            listaPreguntas.add(pregunta);
        }
        return listaPreguntas;
    }





    private void mostrarPregunta() {

        ayudaUsada = false;
        if (preguntaActual < preguntas.size()) {

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
                Intent serviceIntent = new Intent(this, MusicService.class);
                stopService(serviceIntent);
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

        // Aquí se utiliza tiempoRestante que es un entero
        int tiempoTotal = (int) ((System.currentTimeMillis() - tiempoInicio) / 1000);  // El tiempo restante en segundos, que es un entero

        Historial historial = new Historial(
                null, puntaje, fecha, tiempoTotal, ayudas, idUsuario, pregunta.getId_pregunta(),
                "Usuario", categoria, dificultad
        );

        Call<Historial> call = historialService.registrarHistorial(historial);
        call.enqueue(new Callback<Historial>() {
            @Override
            public void onResponse(Call<Historial> call, Response<Historial> response) {
                if (response.isSuccessful()) {
                    guardarHistorialLocalmente(historial);
                    Toast.makeText(PreguntaActivity.this, "Historial registrado correctamente.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Historial> call, Throwable t) {
                guardarHistorialLocalmente(historial);
                Toast.makeText(PreguntaActivity.this, "Historial guardado exitosamente sin internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void guardarHistorialLocalmente(Historial historial) {
        if ((false) || (historial.getFecha() == null) || (historial.getId_usuario() == null) || (historial.getId_pregunta() == null)) {
            Toast.makeText(PreguntaActivity.this, "No se puede guardar: datos incompletos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el username de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Invitado");

        HistorialRoom historialRoom = new HistorialRoom(
                null,
                historial.getPuntaje(),
                historial.getFecha(),
                historial.getTiempo(),
                historial.getAyudas(),
                historial.getId_usuario(),
                historial.getId_pregunta(),
                username,
                historial.getCategoria(),
                historial.getDificultad()
        );


        new Thread(() -> {
            try {
                appDatabase.historialDao().insertHistorial(historialRoom);
                runOnUiThread(() -> {
                    Toast.makeText(PreguntaActivity.this, "Historial guardado localmente.", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                // Registrar el error en el log
                Log.e("PreguntaActivity", "Error al guardar historial", e); // Cambia el TAG según sea necesario

                runOnUiThread(() -> {
                    // Mostrar un Toast con un mensaje genérico al usuario
                    Toast.makeText(PreguntaActivity.this, "Ocurrió un error al guardar el historial.", Toast.LENGTH_LONG).show();
                });
            }
        }).start();

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
                opcionesIncorrectas.get(randomIndex).setCardBackgroundColor(Color.GRAY);
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
                opcionesIncorrectas.get(0).setCardBackgroundColor(Color.GRAY);
                opcionesIncorrectas.get(1).setCardBackgroundColor(Color.GRAY);
            }

            puntaje -= 5;  // Descontar 5 puntos por usar esta ayuda
            puntajeText.setText("Puntaje: " + puntaje);
        }
    }
    private void iniciarTemporizador() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        tiempoRestante = 10;
        tiempoText.setText(String.valueOf(tiempoRestante));

        countDownTimer = new CountDownTimer(tiempoRestante * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = (int) (millisUntilFinished / 1000);
                tiempoText.setText(String.valueOf(tiempoRestante));
            }

            @Override
            public void onFinish() {
                tiempoRestante = 0;
                tiempoText.setText(String.valueOf(tiempoRestante));
                Toast.makeText(PreguntaActivity.this, "Tiempo alcanzado", Toast.LENGTH_SHORT).show();
                puntaje -= 5;
                puntajeText.setText("Puntaje: " + puntaje);
                mostrarSiguientePregunta();
            }
        }.start();
    }



    private boolean historialRegistrado = false;
    private void mostrarSiguientePregunta() {

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


    public void volver1 (View view){

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        historialRegistrado = true;

        // Mostrar mensaje opcional
        Toast.makeText(this, "Has vuelto al menú sin terminar  el historial.", Toast.LENGTH_SHORT).show();

        Intent serviceIntent = new Intent(this, MusicService.class);
        stopService(serviceIntent);
        // Crea un Intent para ir al menú
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

}


