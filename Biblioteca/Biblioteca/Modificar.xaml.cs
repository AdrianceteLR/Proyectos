using MySql.Data.MySqlClient;
using MySql.Data.Types;
using System;
using System.Collections.Generic;
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
    /// Lógica de interacción para Modificar.xaml
    /// </summary>
    public partial class Modificar : Window
    {
        public Modificar()
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

        private void CargarDatosDataGrid()
        {
            BaseDeDatos bd = new BaseDeDatos();

            DataTable dt = bd.CargarLibros();

            dgLibrosId.ItemsSource = dt.DefaultView;
        }

        private void btnBuscar_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtId.Text) && int.TryParse(txtId.Text, out int id))
            {
                BaseDeDatos bd = new BaseDeDatos();
                DataTable dt = bd.ObtenerLibroId(id);

                if (dt.Rows.Count > 0)
                {
                    DataRow row = dt.Rows[0];

                    txtTitulo.Text = row["Titulo"].ToString();
                    txtAutor.Text = row["Autor"].ToString();
                    txtEditorial.Text = row["Editorial"].ToString();
                    DateTime fecha = (DateTime)row["fecha_publicacion"];
                        dpFechaPublicacion.Text = fecha.ToString();
                    txtImagen.Text = row["Imagen"].ToString();
                    txtDescripcion.Text = row["descripcion"].ToString();
                    txtPrecio.Text = row["precio"].ToString();
                    txtUnidadesAlmacen.Text = row["unidades"].ToString();
                    Boolean venta = (Boolean)row["enventa"];
                        chkVenta.IsChecked = venta;

                    gpbDatosLibro.IsEnabled = true;
                }
                else
                {
                    txtId.Text = String.Empty;
                    MessageBox.Show("No se encontró ningún libro con el ID proporcionado.");
                }
            }
            else
            {
                MessageBox.Show("El campo ID debe estar rellenado correctamente.");
            }
            
        }

        private void btnModificar_Click(object sender, RoutedEventArgs e)
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
                    if (String.IsNullOrWhiteSpace(txtUnidadesAlmacen.Text) || int.TryParse(txtUnidadesAlmacen.Text, out int almacen))
                    {
                        BaseDeDatos bd = new BaseDeDatos();
                        bd.ActualizarLibro(int.Parse(txtId.Text),txtTitulo.Text, txtAutor.Text, txtEditorial.Text, fecha, txtImagen.Text, txtDescripcion.Text, txtPrecio.Text, txtUnidadesAlmacen.Text, venta);
                        
                        MessageBox.Show("Libro actualizado correctamente");

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

                CargarDatosDataGrid();
                LimpiarDatos();
            }
            else
            {
                MessageBox.Show("Los campos Título y Autor deben estar rellenados.");
            }
        }

        private void btnLimpiar_Click(object sender, RoutedEventArgs e)
        {
            txtId.Text = String.Empty;
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
