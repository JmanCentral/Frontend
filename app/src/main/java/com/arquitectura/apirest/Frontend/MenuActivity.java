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

    }

    public void startQuiz(View view) {
        // Iniciar la actividad QuizActivity
        Intent intent = new Intent(this, CategoriaActivity.class);
        startActivity(intent);

    }

    public void reglas(View view) {
        // Iniciar la actividad RulesActivity
        Intent intent = new Intent(this, ReglasActivity.class);
        startActivity(intent);
    }

    public void historial(View view) {
        // Iniciar la actividad HistoryActivity
        Intent intent = new Intent(this, HistorialActivity.class);
        startActivity(intent);

    }
    public void editarContrasena(View view) {
        // Iniciar la actividad EditPasswordActivity
        Intent intent = new Intent(this, EditarContraActivity.class);
        startActivity(intent);
    }
    public void salir(View view) {
        // Limpiar SharedPreferences al salir
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Mostrar un mensaje de confirmación
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        // Redirigir a MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finalizar la actividad actual
    }


}