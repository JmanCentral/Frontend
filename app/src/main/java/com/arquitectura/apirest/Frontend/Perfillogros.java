package com.arquitectura.apirest.Frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arquitectura.apirest.DAOS.DificultadCategoria;
import com.arquitectura.apirest.Databsae.AppDatabase;
import com.arquitectura.apirest.Entidades.Usuario;
import com.arquitectura.apirest.R;
import com.arquitectura.apirest.Room.UsuarioRoom;

import java.util.List;

import Utils.APIS;
import Utils.UsuarioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfillogros extends AppCompatActivity {

    TextView usernameTextView, emailTextView, nivelTextView, logro1TextView, logro2TextView , logro3TextView, logro4TextView, logro5TextView;
    SharedPreferences sharedPreferences;
    AppDatabase appDatabase;
    UsuarioService usuarioService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfillogros);

        // Referencias a los TextViews
        usernameTextView = findViewById(R.id.username1);
        emailTextView = findViewById(R.id.email);
        nivelTextView = findViewById(R.id.Nivel);
        logro1TextView = findViewById(R.id.Logro1);
        logro2TextView = findViewById(R.id.Logro2);
        logro3TextView = findViewById(R.id.Logro3);
        logro4TextView = findViewById(R.id.Logro4);
        logro5TextView = findViewById(R.id.Logro5);

        usuarioService = APIS.getUsuarioService();
        appDatabase = AppDatabase.getDatabase(getApplicationContext());

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");


        modificarUsuario(username);
        obtenerUsuario(username);
    }

    private void obtenerUsuario(String username) {
        ImageView miImagen = findViewById(R.id.miImagen);
        ImageView miImagen2 = findViewById(R.id.miImagen2);
        ImageView miImagen3 = findViewById(R.id.miImagen3);
        ImageView miImagen4 = findViewById(R.id.miImagen4);
        ImageView miImagen5 = findViewById(R.id.miImagen5);
        ImageView miImagen6 = findViewById(R.id.miImagen6);

        Call<Usuario> call = usuarioService.verificarUsuarioExistente(username);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();
                    // Actualizar la UI con los datos del usuario
                    usernameTextView.setText(usuario.getUsername());
                    emailTextView.setText(usuario.getEmail());
                    nivelTextView.setText(usuario.getNivel());
                    logro1TextView.setText(usuario.getLogro1());
                    logro2TextView.setText(usuario.getLogro2());
                    logro3TextView.setText(usuario.getLogro3());
                    logro4TextView.setText(usuario.getLogro4());
                    logro5TextView.setText(usuario.getLogro5());
                    miImagen.setImageResource(R.drawable.taza);
                    miImagen2.setImageResource(R.drawable.medalla);
                    miImagen3.setImageResource(R.drawable.medalla);
                    miImagen4.setImageResource(R.drawable.medalla);
                    miImagen5.setImageResource(R.drawable.medalla);
                    miImagen6.setImageResource(R.drawable.medalla);

                } else {

                    Toast.makeText(Perfillogros.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(Perfillogros.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modificarUsuario(String username) {

        // Llamada para actualizar usuario en el servicio
        Call<Usuario> call = usuarioService.actualizarNivel(username); // Pasar el usuario actualizado
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Perfillogros.this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Perfillogros.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }



    private void actualizarUsuarioPorHistorialEnRoom(String username) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            // Obtener el usuario de Room
            UsuarioRoom usuario = db.usuarioDao().obtenerUsuarioPorUsername(username);

            if (usuario != null) {
                // Obtener datos de los historiales de usuario
                Integer puntajeTotal = db.historialDao().getTotalPuntosDelUsuario(username);
                Integer totalAyudas = db.historialDao().getTotalAyudasDelUsuario(username);
                Integer tiempoTotal = db.historialDao().getTiempoTotalDelUsuario(username);
                List<DificultadCategoria> dificultadesYCategorias = db.historialDao().obtenerDificultadYCategoriaPorUsuario(username);

                // Actualizar nivel del usuario
                if (puntajeTotal != null && puntajeTotal > 100) {
                    usuario.setNivel("Experto");
                } else {
                    usuario.setNivel("Novato");
                }

                // Actualizar logros
                if (tiempoTotal != null && tiempoTotal < 120) {
                    usuario.setLogro1("Muy Rapido");
                }

                if (totalAyudas != null) {
                    if (totalAyudas == 0) {
                        usuario.setLogro2("Invencible");
                    } else if (totalAyudas < 30) {
                        usuario.setLogro2("Loser");
                    }
                }

                // Verificar logros por categorías y dificultades específicas
                if (verificarLogroPorCategoriaYDificultad(dificultadesYCategorias, "matematicas", "dificil")) {
                    usuario.setLogro3("Aprende a las malas");
                }
                if (verificarLogroPorCategoriaYDificultad(dificultadesYCategorias, "geografia", "dificil")) {
                    usuario.setLogro4("Conocedor");
                }
                if (verificarLogroPorCategoriaYDificultad(dificultadesYCategorias, "literatura", "dificil")) {
                    usuario.setLogro5("Cosas de neruda");
                }

                // Actualizar el usuario en Room
                db.usuarioDao().actualizarUsuario(usuario);

                runOnUiThread(() -> Toast.makeText(Perfillogros.this, "Usuario actualizado en Room", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(Perfillogros.this, "Usuario no encontrado en Room", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Método auxiliar para verificar logros por categoría y dificultad
    private boolean verificarLogroPorCategoriaYDificultad(List<DificultadCategoria> lista, String categoriaObjetivo, String dificultadObjetivo) {
        for (DificultadCategoria item : lista) {
            if (item.getDificultad().equals(dificultadObjetivo) && item.getCategoria().equals(categoriaObjetivo)) {
                return true;
            }
        }
        return false;
    }



    public void regresar2(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}




