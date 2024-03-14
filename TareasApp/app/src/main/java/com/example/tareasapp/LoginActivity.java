package com.example.tareasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnIniciarSesion, btnRegistrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtener la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Asignar vistas
        editTextEmail = findViewById(R.id.txtEmail);
        editTextPassword = findViewById(R.id.txtContraseña);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrar = findViewById(R.id.btnRegistrarse);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Verificar si los campos de correo electrónico y contraseña no están vacíos
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Mostrar un mensaje de error si alguno de los campos está vacío
                    Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    // Autenticar al usuario con Firebase Auth
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Inicio de sesión exitoso, redirigir a la actividad principal
                                        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Error en el inicio de sesión, mostrar un mensaje al usuario
                                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión. Verifique sus credenciales.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}