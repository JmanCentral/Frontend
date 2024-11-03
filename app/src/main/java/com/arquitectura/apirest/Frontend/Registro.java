package com.arquitectura.apirest.Frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.arquitectura.apirest.Databsae.AppDatabase;
import com.arquitectura.apirest.Entidades.Usuario;
import com.arquitectura.apirest.R;
import com.arquitectura.apirest.Room.UsuarioRoom;

import Utils.APIS;
import Utils.UsuarioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    EditText username, email, password;
    UsuarioService usuarioService;
    SharedPreferences sharedPreferences;
    AppDatabase appDatabase;

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

        // Inicializar la base de datos Room
        appDatabase = AppDatabase.getDatabase(this);
    }

    public void registrarse(View view) {
        String nombreUsuario = username.getText().toString();
        String correo = email.getText().toString();
        String contrasena = password.getText().toString();

        // Validaciones de entrada
        if (!isValidEmail(correo)) {
            Toast.makeText(this, "Correo no válido. debe tener un formato válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidUsername(nombreUsuario)) {
            Toast.makeText(this, "Nombre de usuario no válido debe tener entre 4 y 15 caracteres", Toast.LENGTH_SHORT).show();
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

                    guardarUsuarioLocalmente(nuevoUsuario);
                    // Registro exitoso, redirigir a MenuActivity
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro.this, MenuActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    private void guardarUsuarioLocalmente(Usuario nuevoUsuario) {
        UsuarioRoom usuarioRoom = new UsuarioRoom(null, nuevoUsuario.getUsername(), nuevoUsuario.getPassword(), nuevoUsuario.getEmail(), "Novato");

        new Thread(() -> {
            appDatabase.usuarioDao().insertUsuario(usuarioRoom);
            runOnUiThread(() -> {
                Toast.makeText(Registro.this, "Usuario guardado localmente", Toast.LENGTH_SHORT).show();
            });
        }).start();
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
