package com.example.tareasapp;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TareaAdaptador extends RecyclerView.Adapter<TareaAdaptador.ViewHolderTarea> {

   private Context context;
   private static ArrayList<Tarea> listaTarea;

   public TareaAdaptador(Context context, ArrayList<Tarea> taskList) {
      this.context = context;
      this.listaTarea = taskList;
   }

   @NonNull
   @Override
   public ViewHolderTarea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tarea, parent, false);
      return new ViewHolderTarea(view);
   }

   @SuppressLint("ResourceType")
   @Override
   public void onBindViewHolder(@NonNull ViewHolderTarea holder, int position) {
      Tarea tarea = listaTarea.get(position);
      Log.d(TAG, "Título de la tarea: " + tarea.getTitulo());
      holder.txtTitulo.setText(tarea.getTitulo());
      holder.txtDescripcion.setText(tarea.getDescripcion());

      // Configurar el estado del CheckBox
      holder.miCheckBox.setChecked(tarea.isChecked());

      holder.miCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            tarea.setChecked(isChecked); // Actualizar el estado del CheckBox en la tarea
            if (isChecked) {

               Animation animation = AnimationUtils.loadAnimation(context,R.anim.animacion_borrar);
               holder.itemView.startAnimation(animation);

               // Retrasar la eliminación de la tarea por un segundo
               new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                     // Eliminar la tarea después del retraso
                     listaTarea.remove(tarea);
                     notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado

                     // Eliminar la tarea de la base de datos
                     eliminarTareaDeFirebase(tarea);
                     Toast.makeText(context, "Tarea borrada correctamente.", Toast.LENGTH_SHORT).show();
                  }
               }, 1000);
            }
         }
      });


      holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // Abrir DetalleTareaActivity y pasar los datos de la tarea seleccionada
            Intent intent = new Intent(context, DetalleTareaActivity.class);
            intent.putExtra("tarea_seleccionada", tarea);
            context.startActivity(intent);
         }
      });

      switch (tarea.getGrupo()){
         case "Ocio":
            holder.itemView.findViewById(R.id.linearLayout).setBackgroundResource(R.color.colorVerde);
            break;
         case "Trabajo":
            holder.itemView.findViewById(R.id.linearLayout).setBackgroundResource(R.color.colorAmarillo);
            break;
         case "Deporte":
            holder.itemView.findViewById(R.id.linearLayout).setBackgroundResource(R.color.colorRojo);
            break;
         case "Otros":
            holder.itemView.findViewById(R.id.linearLayout).setBackgroundResource(R.color.colorMorado);
            break;
         default:
            holder.itemView.findViewById(R.id.linearLayout).setBackgroundResource(R.color.colorDefecto);
            break;
      }
   }
   @Override
   public int getItemCount() {
      return listaTarea.size();
   }


   public static class ViewHolderTarea extends RecyclerView.ViewHolder {

      TextView txtTitulo;
      TextView txtDescripcion;
      CheckBox miCheckBox;

      public ViewHolderTarea(@NonNull View itemView) {
         super(itemView);
         txtTitulo = itemView.findViewById(R.id.textViewTitulo);
         txtDescripcion = itemView.findViewById(R.id.textViewDescripcion);
         miCheckBox = itemView.findViewById(R.id.miCheckBox);
      }
   }

   public static void eliminarTareaDeFirebase(Tarea tarea) {
      FirebaseAuth mAuth = FirebaseAuth.getInstance();
      FirebaseUser currentUser = mAuth.getCurrentUser();
      if (currentUser != null) {
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         db.collection("tareas")
                 .whereEqualTo("userId", currentUser.getUid())
                 .whereEqualTo("titulo", tarea.getTitulo())
                 .whereEqualTo("descripcion", tarea.getDescripcion())
                 .whereEqualTo("fecha", tarea.getFecha())
                 .whereEqualTo("grupo", tarea.getGrupo())
                 .get()
                 .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                       for (DocumentSnapshot document : task.getResult()) {
                          db.collection("tareas").document(document.getId())
                                  .delete()
                                  .addOnSuccessListener(aVoid -> Log.d(TAG, "Tarea eliminada de Firebase correctamente"))
                                  .addOnFailureListener(e -> Log.w(TAG, "Error al eliminar tarea de Firebase", e));
                       }
                    } else {
                       Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                    }
                 });
      } else {
         Log.d(TAG, "Usuario actual no encontrado");
      }
   }

}
