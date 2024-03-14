// PrincipalActivity.java

package com.example.tareasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NUEVA_TAREA = 1;

    private RecyclerView recyclerViewTasks;
    private TareaAdaptador tareaAdaptador;
    private ArrayList<Tarea> tareaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tareaList = new ArrayList<>();

        // Inflar el fragmento de la barra de herramientas
        MenuFragment menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.toolbar_container, menuFragment)
                .commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Manejar los clics en los elementos del menú
                int id = item.getItemId();
                if (id == R.id.menu_item1) {
                    // No es necesario iniciar la actividad actual de nuevo
                    return true;
                } else if (id == R.id.menu_item2) {
                    Intent intent = new Intent(PrincipalActivity.this, CalendarioActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_item3) {
                    // Mostrar un AlertDialog para confirmar el cierre de sesión
                    mostrarDialogoCerrarSesion();
                    return true;
                }
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.añadirTareas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, NuevaTareaActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NUEVA_TAREA);
            }
        });

        // Configurar el RecyclerView
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        tareaAdaptador = new TareaAdaptador(this, tareaList);
        recyclerViewTasks.setAdapter(tareaAdaptador);

        // Cargar las tareas desde la base de datos Firestore
        cargarTareasDesdeFirebase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NUEVA_TAREA && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("nueva_tarea")) {
                // Obtener la nueva tarea de los extras del intent
                Tarea nuevaTarea = (Tarea) data.getSerializableExtra("nueva_tarea");

                // Agregar la nueva tarea a la lista de tareas
                tareaList.add(nuevaTarea);

                // Notificar al adaptador que los datos han cambiado
                tareaAdaptador.notifyDataSetChanged();
            }
        }
    }

    private void cargarTareasDesdeFirebase() {
        // Obtener el ID del usuario actualmente autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Obtener una referencia a la colección "tareas" en Firebase Firestore
            CollectionReference tareasRef = FirebaseFirestore.getInstance().collection("tareas");

            // Realizar una consulta para obtener solo las tareas del usuario actual
            tareasRef.whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        // Limpiar la lista de tareas
                        tareaList.clear();
                        // Iterar sobre los documentos obtenidos
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Convertir cada documento en un objeto Tarea y agregarlo a la lista
                            Tarea tarea = document.toObject(Tarea.class);
                            tareaList.add(tarea);
                        }
                        // Notificar al adaptador que los datos han cambiado
                        tareaAdaptador.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        // Manejar el error si la carga de tareas falla
                        Toast.makeText(PrincipalActivity.this, "Error al cargar las tareas", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void mostrarDialogoCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cerrar sesión
                FirebaseAuth.getInstance().signOut();
                // Redirigir al usuario a la pantalla de inicio de sesión
                Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                // Limpiar las actividades anteriores en la pila y empezar una nueva actividad de inicio de sesión
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // Asegurarse de que el diálogo se cierre antes de salir
                dialogInterface.dismiss();
                // Añadir un return para evitar que el código siguiente se ejecute
                return;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // No hacer nada, simplemente cerrar el diálogo
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
