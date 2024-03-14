package com.example.ratonyelgato;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class JugarActivity extends AppCompatActivity {

   private MediaPlayer sonidoDeslizamiento;

   ImageView[][] casillas = new ImageView[8][8];
   private int turno = 1;
   int casillaRaton = 2;
   int casillaGato = -1;

   private int marcadorRaton = 0;
   private int marcadorGato = 0;

   private int blanco = Color.parseColor("#CDCDCB");
   private int negro = Color.parseColor("#191918");
   private int colorResaltado = Color.parseColor("#D1C574");

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_jugar);

      sonidoDeslizamiento = MediaPlayer.create(this,R.raw.deslizamiento);

      Button btnSalirJugar = findViewById(R.id.btnSalirJugar);

      btnSalirJugar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
            sonidoDeslizamiento.release();
         }

      });

      Button btnReinicar = findViewById(R.id.btnReiniciar);
      btnReinicar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            limpiarTablero();
            colocarFichaRaton(0, blanco, R.drawable.raton);
            colocarFichaGatos(7, blanco, R.drawable.gato, 4);
         }
      });

      GridLayout gridLayout = findViewById(R.id.gridLayout);

      int imgRaton = R.drawable.raton;
      int imgGato = R.drawable.gato;

      //Se genera la tabla
      for(int f = 0; f < 8; f++){
         for(int c = 0; c < 8; c++){
            ImageView tablero = new ImageView(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED,1f);
            tablero.setLayoutParams(params);

            final int finalFila = f;
            final int finalCasilla = c;
            tablero.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  if(turno == 1) {
                     moverRaton(finalFila, finalCasilla);
                  }else {
                     moverGato(finalFila, finalCasilla);
                  }
               }
            });

            casillas[f][c] = tablero;

            // Le doy color a la tabla
            if((f + c) % 2 == 0){
               tablero.setBackgroundColor(blanco);
            }
            else{
               tablero.setBackgroundColor(negro);
            }
            gridLayout.addView(tablero);
         }
      }
      colocarFichaRaton(0, blanco, imgRaton);
      colocarFichaGatos(7, blanco, imgGato, 4);
      casillaGato = -1;
   }

   private void colocarFichaRaton(int fila, int color, int imagenFicha) {
      ImageView casilla = casillas[fila][casillaRaton];
      casilla.setBackgroundColor(color);
      casilla.setImageResource(imagenFicha);
   }

   private void colocarFichaGatos(int fila, int color, int imagenFicha, int cantidad) {
      for (int i = 0; i < cantidad; i++) {
         casillaGato = casillaGato + 2;
         if (casillaGato < 8) {
            ImageView casilla = casillas[fila][casillaGato];
            casilla.setBackgroundColor(color);
            casilla.setImageResource(imagenFicha);
         }
      }
   }

   private int gatoSeleccionadoFila = -1;
   private int gatoSeleccionadoCasilla = -1;

   private void moverGato(int fila, int casilla) {
      quitarResaltado();

      if (gatoSeleccionadoFila == -1 && gatoSeleccionadoCasilla == -1) {
         // Si no hay un gato seleccionado, selecciona el gato en la posición actual
         if (casillas[fila][casilla].getDrawable() != null &&
                 casillas[fila][casilla].getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.gato).getConstantState())) {
            resaltarCasillas(fila, casilla, true);
            gatoSeleccionadoFila = fila;
            gatoSeleccionadoCasilla = casilla;
         }
      } else {
         // Si hay un gato seleccionado, intenta moverlo a la nueva posición
         int nuevaFila = fila;
         int nuevaCasilla = casilla;

         int colorCasilla = ((ColorDrawable) casillas[nuevaFila][nuevaCasilla].getBackground()).getColor();

         // Verificar si la nueva posición está vacía, es una casilla blanca,
         // está una fila por encima y la casilla es [fila-1][casilla-1], [fila-1][casilla+1]
         if (nuevaFila == gatoSeleccionadoFila - 1 &&
                 nuevaCasilla >= 0 && nuevaCasilla < 8 &&
                 casillas[nuevaFila][nuevaCasilla].getDrawable() == null &&
                 colorCasilla == blanco &&
                 (nuevaCasilla == gatoSeleccionadoCasilla - 1 || nuevaCasilla == gatoSeleccionadoCasilla + 1)) {
            // Actualizar la posición del gato en la tabla y reflejar los cambios en la interfaz gráfica
            casillas[gatoSeleccionadoFila][gatoSeleccionadoCasilla].setImageResource(0);  // Limpiar la posición actual
            casillas[nuevaFila][nuevaCasilla].setImageResource(R.drawable.gato);  // Colocar el gato en la nueva posición

            sonidoDeslizamiento.start();
         }

         // Restablecer la selección
         gatoSeleccionadoFila = -1;
         gatoSeleccionadoCasilla = -1;

         turno = 1;
      }
   }

   private int ratonSeleccionadoFila = -1;
   private int ratonSeleccionadoCasilla = -1;
   private void moverRaton(int fila, int casilla) {
      quitarResaltado();

      // Resto del código de la función moverRaton...

      if (ratonSeleccionadoFila == -1 && ratonSeleccionadoCasilla == -1) {
         // Si no hay un raton seleccionado, resalta la casilla en la posición actual
         if (esFichaRaton(fila, casilla)) {
            resaltarCasillas(fila, casilla, false);
            ratonSeleccionadoFila = fila;
            ratonSeleccionadoCasilla = casilla;
         }
      } else {
         // Si hay un raton seleccionado, verifica si hay movimientos disponibles
         if (hayMovimientosDisponibles(ratonSeleccionadoFila, ratonSeleccionadoCasilla, false)) {
            // Realiza el movimiento del ratón a la nueva posición
            realizarMovimientoRaton(fila, casilla);
         } else {
            // Si el ratón está bloqueado por los gatos, mostrar mensaje y terminar el juego
            if (ratonBloqueado()) {
               mostrarMensaje("Los gatos ganan");
            } else {
               // Si no hay movimientos disponibles, mostrar mensaje de empate o realizar alguna acción adicional
               mostrarMensaje("Los gatos ganan");
            }
            return;
         }
      }
   }


   private boolean ratonBloqueado() {
      for (int j = 0; j < 8; j++) {
         if (casillas[7][j].getDrawable() != null &&
                 casillas[7][j].getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.gato).getConstantState())) {
            return true;
         }
      }
      return false;
   }

   private boolean esFichaRaton(int fila, int casilla) {
      return casillas[fila][casilla].getDrawable() != null &&
              casillas[fila][casilla].getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.raton).getConstantState());
   }

   private void realizarMovimientoRaton(int nuevaFila, int nuevaCasilla) {
      int colorCasilla = ((ColorDrawable) casillas[nuevaFila][nuevaCasilla].getBackground()).getColor();

      // Verificar si la nueva posición está vacía, es una casilla blanca,
      // y la casilla es [fila+1][casilla-1], [fila+1][casilla+1], [fila-1][casilla-1] o [fila-1][casilla+1]
      if (((nuevaFila == ratonSeleccionadoFila + 1 && nuevaCasilla == ratonSeleccionadoCasilla - 1) ||
              (nuevaFila == ratonSeleccionadoFila + 1 && nuevaCasilla == ratonSeleccionadoCasilla + 1) ||
              (nuevaFila == ratonSeleccionadoFila - 1 && nuevaCasilla == ratonSeleccionadoCasilla - 1) ||
              (nuevaFila == ratonSeleccionadoFila - 1 && nuevaCasilla == ratonSeleccionadoCasilla + 1)) &&
              nuevaFila >= 0 && nuevaFila < 8 &&
              nuevaCasilla >= 0 && nuevaCasilla < 8 &&
              casillas[nuevaFila][nuevaCasilla].getDrawable() == null &&
              colorCasilla == blanco) {
         // Actualizar la posición del raton en la tabla y reflejar los cambios en la interfaz gráfica
         casillas[ratonSeleccionadoFila][ratonSeleccionadoCasilla].setImageResource(0);  // Limpiar la posición actual
         casillas[nuevaFila][nuevaCasilla].setImageResource(R.drawable.raton);  // Colocar el raton en la nueva posición

         sonidoDeslizamiento.start();

         // Actualizar la posición del ratón
         ratonSeleccionadoFila = nuevaFila;
         ratonSeleccionadoCasilla = nuevaCasilla;
      }

      // Restablecer la selección
      ratonSeleccionadoFila = -1;
      ratonSeleccionadoCasilla = -1;

      turno = 2;

      if (nuevaFila == 7) {
         mostrarMensaje("El ratón gana");
      }
   }

   private boolean hayMovimientosDisponibles(int fila, int casilla, boolean esRaton) {
      // Verificar si el ratón está en la fila 7
      if (esRaton && fila == 7) {
         return true;
      }

      // Verificar si el ratón está en la fila 0 y permitir el movimiento en la columna 0
      if (esRaton && fila == 0) {
         if (casilla == 1 && casillas[1][0].getDrawable() == null && casillas[1][1].getDrawable() == null) {
            return true;
         }
      }

      // Verificar movimientos en las posiciones adyacentes
      for (int i = -1; i <= 1; i++) {
         for (int j = -1; j <= 1; j++) {
            int nuevaFila = fila + i;
            int nuevaCasilla = casilla + j;

            if (nuevaFila >= 0 && nuevaFila < 8 && nuevaCasilla >= 0 && nuevaCasilla < 8 &&
                    (nuevaFila + nuevaCasilla) % 2 == 0 &&
                    (esRaton ? esMovimientoValidoRaton(nuevaFila, nuevaCasilla) : esMovimientoValidoGato(nuevaFila, nuevaCasilla))) {
               // Verificar si es el ratón en la última columna y permitir el movimiento hacia la derecha
               if (esRaton && casilla == 7 && j == 1 && casillas[fila][casilla + 1].getDrawable() == null) {
                  return true;
               }
               // Restricción de movimiento hacia la última columna para los gatos
               if (!esRaton && casilla == 6 && j == 1 && casillas[fila][casilla + 1].getDrawable() == null) {
                  return false;
               }
               return true;
            }
         }
      }
      return false;
   }

   private boolean esMovimientoValidoRaton(int fila, int casilla) {
      int colorCasilla = obtenerColorCasilla(fila, casilla);
      return casillas[fila][casilla].getDrawable() == null && colorCasilla == blanco;
   }

   private boolean esMovimientoValidoGato(int fila, int casilla) {
      int colorCasilla = obtenerColorCasilla(fila, casilla);
      return casillas[fila][casilla].getDrawable() == null && colorCasilla == blanco;
   }

   private int obtenerColorCasilla(int fila, int casilla) {
      return (fila + casilla) % 2 == 0 ? blanco : negro;
   }


   private void resaltarCasillas(int fila, int casilla, boolean esGato) {
      // Obtiene el color de fondo original
      int colorOriginal = ((ColorDrawable) casillas[fila][casilla].getBackground()).getColor();

      // Resalta la casilla seleccionada
      casillas[fila][casilla].setBackgroundColor(colorResaltado);

      // Crea un borde mediante un ShapeDrawable
      GradientDrawable border = new GradientDrawable();
      border.setColor(colorOriginal); // Establece el color de fondo original
      border.setStroke(8, colorResaltado); // Ajusta el ancho del borde

      // Combina el fondo original y el borde en un LayerDrawable
      LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{border});

      // Establece el LayerDrawable como el fondo de la casilla
      casillas[fila][casilla].setBackground(layerDrawable);

      // Resalta las casillas a las que se puede mover el ratón o el gato (solo las casillas blancas)
      for (int i = -1; i <= 1; i++) {
         for (int j = -1; j <= 1; j++) {
            int nuevaFila = fila + i;
            int nuevaCasilla = casilla + j;

            // Verifica si la nueva posición está en el tablero y es una casilla blanca
            // y si es un gato, solo resalta las casillas hacia arriba
            if (nuevaFila >= 0 && nuevaFila < 8 && nuevaCasilla >= 0 && nuevaCasilla < 8 &&
                    (nuevaFila + nuevaCasilla) % 2 == 0 &&
                    casillas[nuevaFila][nuevaCasilla].getDrawable() == null &&
                    (!esGato || i == -1)) {
               // Crea un borde mediante un ShapeDrawable
               GradientDrawable innerBorder = new GradientDrawable();
               innerBorder.setColor(colorOriginal); // Establece el color de fondo original
               innerBorder.setStroke(8, colorResaltado); // Ajusta el ancho del borde

               // Combina el fondo original y el borde
               LayerDrawable innerLayerDrawable = new LayerDrawable(new Drawable[]{innerBorder});

               // Establece el LayerDrawable como el fondo de la casilla
               casillas[nuevaFila][nuevaCasilla].setBackground(innerLayerDrawable);
            }
         }
      }
   }

   private void quitarResaltado() {
      // Restaurar el color original de fondo para todas las casillas
      for (int f = 0; f < 8; f++) {
         for (int c = 0; c < 8; c++) {
            if ((f + c) % 2 == 0) {
               casillas[f][c].setBackgroundColor(blanco);
            } else {
               casillas[f][c].setBackgroundColor(negro);
            }
         }
      }
   }

   private void mostrarMensaje(String mensaje) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(mensaje)
              .setCancelable(false)
              .setPositiveButton("Aceptar", (dialog, id) -> {
                 if(mensaje.contains("ratón gana")){
                     marcadorRaton++;
                 }else if(mensaje.contains("gatos ganan")){
                     marcadorGato++;
                 }
                 TextView txtMarcadorRaton = findViewById(R.id.marcadorRaton);
                 TextView txtMarcadorGato = findViewById(R.id.marcadorGato);
                 txtMarcadorRaton.setText("Ratón: " + marcadorRaton);
                 txtMarcadorGato.setText("|  Gato: " + marcadorGato);
              });
      AlertDialog alert = builder.create();
      alert.show();
   }

   private void limpiarTablero(){
      for (int f = 0; f < 8; f++) {
         for (int c = 0; c < 8; c++) {
            casillas[f][c].setImageResource(0);
         }
      }
   }
}