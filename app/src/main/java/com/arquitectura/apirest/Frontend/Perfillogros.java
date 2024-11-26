package com.arquitectura.apirest.Frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arquitectura.apirest.Databsae.AppDatabase;
import com.arquitectura.apirest.Entidades.Usuario;
import com.arquitectura.apirest.R;

import Utils.APIS;
import Utils.UsuarioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfillogros extends AppCompatActivity {

    TextView usernameTextView, emailTextView, nivelTextView, logro1TextView, logro2TextView;
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
                    miImagen.setImageResource(R.drawable.medalla);
                    miImagen2.setImageResource(R.drawable.medalla);
                    miImagen3.setImageResource(R.drawable.medalla);

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
}




