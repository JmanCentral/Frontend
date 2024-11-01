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

import com.arquitectura.apirest.Entidades.Usuario;
import com.arquitectura.apirest.R;

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        exitBtn = findViewById(R.id.exitBtn);
        signUp = findViewById(R.id.signUp);
        usuarioService = APIS.getUsuarioService();

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

        Usuario usuario = new Usuario(null, nombreUsuario, contrasenaUsuario, null , "Novato");

        Call<Usuario> call = usuarioService.loginUsuario(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuarioLogueado = response.body();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usuarioLogueado.getUsername());
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("username", usuarioLogueado.getUsername());
                    startActivity(intent);
                    finish(); // Finalizar MainActivity
                } else {
                    Toast.makeText(MainActivity.this, "Login fallido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registro(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}
