package com.example.tareasapp;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
   public static void showNotification(Context context, String title, String message) {
      NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      if (notificationManager == null) {
         return;
      }

      // Configurar la notificación
      NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
              .setSmallIcon(R.drawable.logo)
              .setContentTitle(title)
              .setContentText(message)
              .setPriority(NotificationCompat.PRIORITY_DEFAULT)
              .setAutoCancel(true);

      // Mostrar la notificación
      notificationManager.notify(0, builder.build());
   }
}