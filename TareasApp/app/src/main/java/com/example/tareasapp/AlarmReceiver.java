package com.example.tareasapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent){
      // Aquí se muestra la notificación cuando se activa la alarma
      NotificationHelper.showNotification(context, "¡Es hora de tu tarea!", "No olvides completar tu tarea.");
   }
}
