// NuevaTareaActivity.java

package com.example.tareasapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NuevaTareaActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 100;
    private static String tareaId;
    private static String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        EditText editTextTitulo = findViewById(R.id.editTextTitulo);
        EditText editTextDescripcion = findViewById(R.id.editTextDescripcion);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Spinner spinnerGrupo = findViewById(R.id.spinnerGrupo);

        configurarSpinner();

        EditText editTextFecha = findViewById(R.id.editTextfecha);

        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        final int añoActual = calendar.get(Calendar.YEAR);
        final int mesActual = calendar.get(Calendar.MONTH);
        final int diaActual = calendar.get(Calendar.DAY_OF_MONTH);

        // Configurar el OnClickListener para abrir el DatePickerDialog
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NuevaTareaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Actualizar el texto del EditText con la fecha seleccionada
                                String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                editTextFecha.setText(fechaSeleccionada);
                            }
                        }, añoActual, mesActual, diaActual);
                datePickerDialog.show();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los datos de entrada del usuario
                String titulo = editTextTitulo.getText().toString();
                String descripcion = editTextDescripcion.getText().toString();
                String grupo = spinnerGrupo.getSelectedItem().toString();
                String fecha = editTextFecha.getText().toString();

                // Verificar que el título no esté vacío
                if (!titulo.isEmpty()) {
                    // Generar un ID único para la tarea
                    String tareaId = FirebaseFirestore.getInstance().collection("tareas").document().getId();

                    // Crear una nueva instancia de Tarea con los datos ingresados
                    Tarea nuevaTarea = new Tarea(tareaId, titulo, descripcion, grupo, fecha);

                    // Devolver la nueva tarea a la actividad principal
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("nueva_tarea", nuevaTarea);
                    setResult(Activity.RESULT_OK, resultIntent);
                    guardarTarea(titulo, descripcion, grupo, fecha);
                    finish();
                } else {
                    // Mostrar un mensaje de error si el título está vacío
                    Toast.makeText(NuevaTareaActivity.this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configurarSpinner() {
        Spinner spinnerGrupo = findViewById(R.id.spinnerGrupo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grupos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapter);
    }

    private void guardarTarea(String titulo, String descripcion, String grupo, String fecha) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference tareasRef = db.collection("tareas");

            Map<String, Object> nuevaTarea = new HashMap<>();
            nuevaTarea.put("titulo", titulo);
            nuevaTarea.put("descripcion", descripcion);
            nuevaTarea.put("grupo", grupo);
            nuevaTarea.put("fecha", fecha);
            nuevaTarea.put("userId", userId);

            tareasRef.add(nuevaTarea)
                    .addOnSuccessListener(documentReference -> {
                        // Crear una nueva instancia de Tarea con los datos ingresados
                        tareaId = documentReference.getId();
                        Tarea nuevaTareaObj = new Tarea(titulo, descripcion, grupo, fecha, userId);

                        // Configurar la alarma para esta tarea
                        configurarAlarmaParaTarea(NuevaTareaActivity.this, tareaId, fecha);

                        // Crear un intent para devolver la nueva tarea a PrincipalActivity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("nueva_tarea", nuevaTareaObj);
                        setResult(Activity.RESULT_OK, resultIntent);

                        // Mostrar un mensaje de éxito y cerrar la actividad
                        Toast.makeText(NuevaTareaActivity.this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Mostrar un mensaje de error si la tarea no se pudo guardar
                        Toast.makeText(NuevaTareaActivity.this, "Error al guardar la tarea: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public static void configurarAlarmaParaTarea(Context context, String tareaId, String fecha) {
        // Parsear la fecha y obtener el año, mes y día
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(fecha);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Configurar una intent para el BroadcastReceiver
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Configurar AlarmManager para que active la alarma en la fecha especificada
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                // Configurar la alarma para que se active en el día especificado a las 00:00 horas
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // Puedes utilizar alarmManager.setExact() o alarmManager.set() dependiendo de tus necesidades
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Verificar si la solicitud de permisos es para notificaciones
        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            // Verificar si los permisos fueron otorgados
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron otorgados
                configurarAlarmaParaTarea(NuevaTareaActivity.this, tareaId, fecha); // Llamar a la función para configurar la alarma
            } else {
                // Los permisos no fueron otorgados
            }
        }
    }

}
