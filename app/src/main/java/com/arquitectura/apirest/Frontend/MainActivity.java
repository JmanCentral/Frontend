package com.arquitectura.apirest.Frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.arquitectura.apirest.Databsae.AppDatabase;
import com.arquitectura.apirest.Entidades.Usuario;
import com.arquitectura.apirest.MusicaApp.MusicService;
import com.arquitectura.apirest.R;
import com.arquitectura.apirest.Room.UsuarioRoom;

import Utils.APIS;
import Utils.UsuarioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText username;
    EditText password;
    Button loginBtn;
    Button exitBtn;
    TextView signUp;
    UsuarioService usuarioService;
    AppDatabase appDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, MusicService.class);
        startService(serviceIntent);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        exitBtn = findViewById(R.id.exitBtn);
        signUp = findViewById(R.id.signUp);
        usuarioService = APIS.getUsuarioService();

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app_database").build();

        // Verificar si ya existe un usuario registrado
        if (sharedPreferences.contains("username")) {
            String savedUsername = sharedPreferences.getString("username", null);
            if (savedUsername != null) {
                Toast.makeText(this, "Bienvenido de nuevo, " + savedUsername, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("username", savedUsername);
                startActivity(intent);
                finish();
            }
        }
    }

    public void login(View view) {

        String nombreUsuario = username.getText().toString();
        String contrasenaUsuario = password.getText().toString();

        Usuario usuario = new Usuario(null, nombreUsuario, contrasenaUsuario, null , "Novato" , null , null , null , null , null);

        Call<Usuario> call = usuarioService.loginUsuario(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuarioLogueado = response.body();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usuarioLogueado.getUsername());
                    editor.putLong("ID", usuarioLogueado.getId());
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    verificarUsuarioLocalmente(nombreUsuario, contrasenaUsuario);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                verificarUsuarioLocalmente(nombreUsuario, contrasenaUsuario);
            }
        });
    }

    private void verificarUsuarioLocalmente(String username, String password) {
        new Thread(() -> {
            UsuarioRoom usuario = appDatabase.usuarioDao().verificarUsuario(username, password);
            runOnUiThread(() -> {
                if (usuario != null) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usuario.getUsername());
                    editor.putLong("ID", usuario.getId());
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    Toast.makeText(MainActivity.this, "Login exitoso desde la base de datos local", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else {
                    // Si no se encuentra el usuario en Room
                    Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }


    public void registro(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}
