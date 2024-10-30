package com.arquitectura.apirest.Frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arquitectura.apirest.R;

public class MenuActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView helloUserName;
    CardView startQuiz;
    CardView rules;
    CardView history;
    CardView editPassword;
    CardView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        helloUserName = findViewById(R.id.helloUserName);

        startQuiz = (CardView) findViewById(R.id.startQuiz);
        rules = (CardView) findViewById(R.id.rules);
        history = (CardView) findViewById(R.id.history);
        editPassword = (CardView) findViewById(R.id.editPassword);
        logout = (CardView) findViewById(R.id.logout);

       String username = sharedPreferences.getString("username", "Invitado");
       helloUserName.setText(getString(R.string.hello_user_name, username));
       Long ID = getIntent().getLongExtra("ID", -1);
        if (ID == -1) {
            // Manejar el error aquí (por ejemplo, finalizar la actividad o mostrar un mensaje)
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show();
            finish(); // Termina la actividad si el ID es inválido
        }

    }

    public void startQuiz(View view) {

        Intent intent = new Intent(this, CategoriaActivity.class);
        Long ID = sharedPreferences.getLong("ID", -1); // Cambia el valor predeterminado a -1

        if (ID == -1) {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show();
            return; // No continuar si el ID no es válido
        }

        intent.putExtra("ID", ID);
        String username = sharedPreferences.getString("username", "Invitado");
        intent.putExtra("username", username);
        startActivity(intent);

    }

    public void reglas(View view) {


        Intent intent = new Intent(this, ReglasActivity.class);
        startActivity(intent);
    }

    public void historial(View view) {


        Intent intent = new Intent(this, HistorialActivity.class);
        startActivity(intent);

    }
    public void editarContrasena(View view) {


        Intent intent = new Intent(this, EditarContraActivity.class);
        startActivity(intent);
    }
    public void salir(View view) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finalizar la actividad actual
    }


}