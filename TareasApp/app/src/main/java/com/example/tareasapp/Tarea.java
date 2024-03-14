package com.example.tareasapp;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Tarea implements Serializable{

   private String id;
   private String titulo;
   private String descripcion;
   private String grupo;
   private String fecha;
   private String userId;
   private boolean isChecked;

   public Tarea(){}

   public Tarea(String id, String titulo, String descripcion, String grupo, String fecha) {
      this.id = id;
      this.titulo = titulo;
      this.descripcion = descripcion;
      this.grupo = grupo;
      this.fecha = fecha;
      this.isChecked = false;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

   public String getGrupo() {
      return grupo;
   }

   public void setGrupo(String grupo) {
      this.grupo = grupo;
   }

   public String getFecha() { return fecha; }

   public void setFecha(String fecha) {
      this.fecha = fecha;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   @NonNull
   @Override
   public String toString() {
      return titulo;
   }

   public boolean isChecked(){
      return isChecked;
   }

   public void setChecked(boolean checked){
      isChecked = checked;
   }

   @Override
   public boolean equals(Object obj) {
      // Verificar si el objeto es nulo o no es una instancia de Tarea
      if (obj == null || !(obj instanceof Tarea)) {
         return false;
      }
      // Realizar casting del objeto a Tarea
      Tarea otraTarea = (Tarea) obj;
      // Comparar las propiedades de las tareas
      return this.titulo.equals(otraTarea.titulo) &&
              this.descripcion.equals(otraTarea.descripcion) &&
              this.grupo.equals(otraTarea.grupo) &&
              this.fecha.equals(otraTarea.fecha);
   }
}
