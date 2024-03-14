package com.example.tareasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetalleTareaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout linear = findViewById(R.id.linearCabecera);

        // Obtener los datos de la tarea enviados desde PrincipalActivity
        Tarea tarea = (Tarea) getIntent().getSerializableExtra("tarea_seleccionada");

        // Mostrar los detalles de la tarea en los TextViews
        TextView tituloTextView = findViewById(R.id.tituloTextView);
        TextView descripcionTextView = findViewById(R.id.descripcionTextView);
        TextView grupoTextView = findViewById(R.id.grupoTextView);
        TextView fechaTextView = findViewById(R.id.fechaTextView);

        switch (tarea.getGrupo()){
            case "Ocio":
                linear.setBackgroundResource(R.color.colorVerde);
                grupoTextView.setBackgroundResource(R.color.colorVerde);
                break;
            case "Trabajo":
                linear.setBackgroundResource(R.color.colorAmarillo);
                grupoTextView.setBackgroundResource(R.color.colorAmarillo);
                break;
            case "Deporte":
                linear.setBackgroundResource(R.color.colorRojo);
                grupoTextView.setBackgroundResource(R.color.colorRojo);
                break;
            case "Otros":
                linear.setBackgroundResource(R.color.colorMorado);
                grupoTextView.setBackgroundResource(R.color.colorMorado);
                break;
            default:
                linear.setBackgroundResource(R.color.colorDefecto);
                grupoTextView.setBackgroundResource(R.color.colorDefecto);
                break;
        }

        tituloTextView.setText(tarea.getTitulo());
        descripcionTextView.setText(tarea.getDescripcion());
        grupoTextView.setText("Grupo: " + tarea.getGrupo());
        fechaTextView.setText("Fecha: " + tarea.getFecha());
    }
}