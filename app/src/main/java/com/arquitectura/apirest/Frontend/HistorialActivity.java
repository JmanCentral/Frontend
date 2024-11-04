package com.arquitectura.apirest.Frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arquitectura.apirest.Databsae.AppDatabase;
import com.arquitectura.apirest.Entidades.Historial;
import com.arquitectura.apirest.R;
import com.arquitectura.apirest.Room.HistorialRoom;

import java.util.ArrayList;
import java.util.List;

import Utils.APIS;
import Utils.HistorialService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistorialAdapter historialAdapter;
    private List<Historial> historialList = new ArrayList<>(); // Lista para almacenar el historial
    private ProgressBar progressBar; // Para indicar la carga
    private TextView noHistorial; // Mensaje cuando no hay historial
    private HistorialService historialService;
    private AppDatabase appDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial); // Asegúrate de que el nombre del layout es correcto
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar); // Asegúrate de tener un ProgressBar en el layout
        noHistorial = findViewById(R.id.noHistorial); // Para mostrar un mensaje si no hay historial
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historialAdapter = new HistorialAdapter(this, historialList);
        recyclerView.setAdapter(historialAdapter);
        appDatabase = AppDatabase.getDatabase(getApplicationContext());

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        obtenerHistorialDesdeApi(username);
    }
    private void obtenerHistorialDesdeApi(String username) {
        // Mostrar la barra de progreso
        progressBar.setVisibility(View.VISIBLE);

        // Inicializar el servicio
        historialService = APIS.getHistorialService();

        // Hacer la petición
        Call<List<Historial>> call = historialService.obtenerHistorialPorUsername(username);

        call.enqueue(new Callback<List<Historial>>() {
            @Override
            public void onResponse(Call<List<Historial>> call, Response<List<Historial>> response) {
                // Ocultar la barra de progreso
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Actualizar la lista con los datos recibidos
                    historialList.clear();
                    historialList.addAll(response.body());
                    historialAdapter.notifyDataSetChanged();

                    if (historialList.isEmpty()) {
                        noHistorial.setVisibility(View.VISIBLE); // Mostrar mensaje si la lista está vacía
                    } else {
                        noHistorial.setVisibility(View.GONE); // Ocultar el mensaje si hay datos
                    }
                } else {
                    noHistorial.setText("No se pudo obtener el historial.");
                    noHistorial.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Historial>> call, Throwable t) {
                // Intentar cargar desde la base de datos local
                cargarHistorialDesdeRoom(username);
            }
        });
    }
    private void cargarHistorialDesdeRoom(String username) {
        // Mostrar la barra de progreso
        progressBar.setVisibility(View.VISIBLE);

        // Intentar cargar desde la base de datos local
        new Thread(() -> {
            List<HistorialRoom> historialLocal = appDatabase.historialDao().obtenerHistorialPorUsuario(username);
            runOnUiThread(() -> {
                if (historialLocal != null && !historialLocal.isEmpty()) {
                    // Limpiar la lista actual
                    historialList.clear();

                    // Convertir de HistorialRoom a Historial
                    for (HistorialRoom historialRoom : historialLocal) {
                        Historial historial = new Historial();
                        historial.setId(historialRoom.getId());
                        historial.setPuntaje(historialRoom.getPuntaje());
                        historial.setTiempo(historialRoom.getTiempo());
                        historial.setAyudas(historialRoom.getAyudas());
                        historial.setCategoria(historialRoom.getCategoria());
                        historial.setDificultad(historialRoom.getDificultad());
                        historial.setId_pregunta(historialRoom.getId_pregunta());
                        historial.setId_usuario(historialRoom.getId_usuario());
                        historial.setUsername(historialRoom.getUsername());
                        historial.setFecha(historialRoom.getFecha());
                        // Agregar a la lista de historial
                        historialList.add(historial);
                    }

                    // Notificar al adaptador sobre los cambios
                    historialAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    noHistorial.setVisibility(View.GONE); // Ocultar mensaje de no historial
                } else {
                    // Si no hay datos locales, mostrar un mensaje
                    progressBar.setVisibility(View.GONE);
                    noHistorial.setText("No hay historial disponible.");
                    noHistorial.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    public void regresar(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}


