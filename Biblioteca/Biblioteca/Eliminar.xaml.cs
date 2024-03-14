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
    /// Lógica de interacción para Eliminar.xaml
    /// </summary>
    public partial class Eliminar : Window
    {
        public Eliminar()
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
            txtId.Text = String.Empty;
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
        private void btnEliminar_Click(object sender, RoutedEventArgs e)
        {
            MessageBoxResult respuesta = MessageBox.Show("Esta seguro que desea continar", "Mensaje", MessageBoxButton.YesNo);

            if (respuesta == MessageBoxResult.Yes)
            {
                if (!String.IsNullOrWhiteSpace(txtId.Text) && int.TryParse(txtId.Text, out int id))
                {
                    BaseDeDatos bd = new BaseDeDatos();
                    bd.EliminarLibro(id);
                    MessageBox.Show("El libro ha sido borrado correctamente.");
                    CargarDatosDataGrid();
                    LimpiarDatos();
                }
                else
                {
                    MessageBox.Show("El campo ID debe estar rellenado correctamente.");
                }
            }
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
