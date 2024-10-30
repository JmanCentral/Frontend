package com.arquitectura.apirest.Frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.arquitectura.apirest.Entidades.Usuario;
import com.arquitectura.apirest.R;

import Utils.APIS;
import Utils.UsuarioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    EditText username, email, password;
    UsuarioService usuarioService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        // Inicializar campos y servicio
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        usuarioService = APIS.getUsuarioService();

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
    }

    public void registrarse(View view) {
        String nombreUsuario = username.getText().toString();
        String correo = email.getText().toString();
        String contrasena = password.getText().toString();

        // Validaciones de entrada
        if (!isValidEmail(correo)) {
            Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidUsername(nombreUsuario)) {
            Toast.makeText(this, "Nombre de usuario no válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidPassword(contrasena)) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, incluir una letra, un número y un carácter especial", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Usuario
        Usuario nuevoUsuario = new Usuario(null, nombreUsuario, contrasena, correo, "Novato");

        // Llamada al servicio de registro
        Call<Usuario> call = usuarioService.registrarUsuario(nuevoUsuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuarioRegistrado = response.body();

                    // Guardar el nombre de usuario en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usuarioRegistrado.getUsername());
                    editor.putLong("ID", usuarioRegistrado.getId());
                    editor.apply();

                    // Registro exitoso, redirigir a MenuActivity
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro.this, MenuActivity.class);
                    intent.putExtra("ID", usuarioRegistrado.getId());
                    intent.putExtra("username", usuarioRegistrado.getUsername());

                    startActivity(intent);
                    finish();
                } else {
                    // Error en el registro
                    Toast.makeText(Registro.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                // Error de conexión
                Toast.makeText(Registro.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validaciones
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[A-Za-z0-9]{4,15}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    public void volver(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
