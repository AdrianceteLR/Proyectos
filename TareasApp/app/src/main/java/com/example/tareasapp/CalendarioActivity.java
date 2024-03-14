package com.example.tareasapp;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class CalendarioActivity extends AppCompatActivity{

    private static final int REQUEST_CODE_NUEVA_TAREA = 1;
    private CalendarView calendarView;
    private ListView tareasListView;
    private ArrayList<Tarea> tareaList;
    private ArrayAdapter<Tarea> tareaAdaptador;
    private DatePicker miDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

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
                    Intent intent = new Intent(CalendarioActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_item2) {
                    Intent intent = new Intent(CalendarioActivity.this, CalendarioActivity.class);
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

        // Obtener una referencia al DatePicker
        miDatePicker = findViewById(R.id.miDatePicker);

        tareasListView = findViewById(R.id.tareasListView);

        // Listener para la selección de tareas en la lista
        tareasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener la tarea seleccionada
                Tarea tareaSeleccionada = tareaList.get(position);

                // Obtener la fecha asociada a la tarea en la base de datos
                String fechaTarea = tareaSeleccionada.getFecha(); // Suponiendo que la fecha está almacenada como un String en la tarea

                // Verificar si hay fecha en la tarea o no
                if(fechaTarea != null && !fechaTarea.isEmpty()) {
                    // Parsear la fecha al formato adecuado para el DatePicker
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = null;
                    try {
                        date = dateFormat.parse(fechaTarea);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date != null) {
                        // Configurar el DatePicker para mostrar la fecha de la tarea seleccionada
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        miDatePicker.init(year, month, dayOfMonth, null);
                    }
                }
                else {
                    Toast.makeText(CalendarioActivity.this, "La tarea no tiene fecha", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cargar las tareas desde la base de datos Firestore
        cargarTareasDesdeFirebase();
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

                        // Verificar si el adaptador es nulo antes de notificar cambios
                        if (tareaAdaptador != null) {
                            // Notificar al adaptador que los datos han cambiado
                            tareaAdaptador.notifyDataSetChanged();
                        } else {
                            // Inicializar el adaptador si es nulo
                            tareaAdaptador = new ArrayAdapter<>(CalendarioActivity.this, android.R.layout.simple_list_item_1, tareaList);
                            tareasListView.setAdapter(tareaAdaptador);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Manejar el error si la carga de tareas falla
                        Toast.makeText(CalendarioActivity.this, "Error al cargar las tareas", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(CalendarioActivity.this, LoginActivity.class);
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
