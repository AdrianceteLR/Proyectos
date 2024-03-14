using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Biblioteca
{
    /// <summary>
    /// Lógica de interacción para Añadir.xaml
    /// </summary>
    public partial class Añadir : Window
    {
        public Añadir()
        {
            InitializeComponent();
            CargarDatosDataGrid(); 
        }

        private void LimpiarDatos()
        {
            txtTitulo.Text = String.Empty;
            txtAutor.Text = String.Empty;
            txtEditorial.Text = String.Empty;
            dpFechaPublicacion.Text = String.Empty;
            txtImagen.Text = String.Empty;
            txtDescripcion.Text = String.Empty;
            txtPrecio.Text = String.Empty;
            txtUnidadesAlmacen.Text = String.Empty;
            chkVenta.IsChecked = false;
        }

        private void btnAñadir_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtTitulo.Text) && !String.IsNullOrWhiteSpace(txtAutor.Text))
            {
                int venta;
                if (chkVenta.IsChecked == true)
                {
                    venta = 1;
                }
                else
                {
                    venta = 0; 
                }

                DateTime fechaPublicacion = dpFechaPublicacion.SelectedDate ?? DateTime.Now;
                String fecha = fechaPublicacion.ToString("yyyy/MM/dd");

                if (String.IsNullOrWhiteSpace(txtPrecio.Text) || int.TryParse(txtPrecio.Text, out int precio))
                {
                    if(String.IsNullOrWhiteSpace(txtUnidadesAlmacen.Text) || int.TryParse(txtUnidadesAlmacen.Text, out int almacen))
                    {
                        BaseDeDatos bd = new BaseDeDatos();
                        bd.InsertarLibro(txtTitulo.Text, txtAutor.Text, txtEditorial.Text, fecha, txtImagen.Text, txtDescripcion.Text, txtPrecio.Text, txtUnidadesAlmacen.Text, venta);
                        MessageBox.Show("Libro añadido correctamente");

                        CargarDatosDataGrid();
                        LimpiarDatos();
                    }
                    else
                    {
                        MessageBox.Show("El campo Unidades Almacen debe estar en blanco o ser un numero");
                    }
                }
                else
                {
                    MessageBox.Show("El campo Precio debe estar en blanco o ser un numero");
                }
            }
            else
            {
                MessageBox.Show("Los campos Título y Autor deben estar rellenados.");
            }
        }

        private void CargarDatosDataGrid()
        {
            BaseDeDatos bd = new BaseDeDatos();

            DataTable dt = bd.CargarLibros();
 
            dgLibros.ItemsSource = dt.DefaultView;
        }

        private void btnLimpiar_Click(object sender, RoutedEventArgs e)
        {
            LimpiarDatos();
        }

        private void btnSalir_Click(object sender, RoutedEventArgs e)
        {
            BaseDeDatos bd = new BaseDeDatos();

            bd.Desconectar();
            this.Close();
        }
    }

}
