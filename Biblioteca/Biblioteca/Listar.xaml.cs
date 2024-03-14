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
    /// Lógica de interacción para Listar.xaml
    /// </summary>
    public partial class Listar : Window
    {
        public Listar()
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
            dgLibrosId.SelectedItem = null;
        }

        private void CargarDatosDataGrid()
        {
            BaseDeDatos bd = new BaseDeDatos();

            DataTable dt = bd.CargarLibros();

            dgLibrosId.ItemsSource = dt.DefaultView;
        }

        private void dgLibrosId_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            BaseDeDatos bd = new BaseDeDatos();

            if (dgLibrosId.SelectedItem != null)
            {
                DataRowView fila = (DataRowView)dgLibrosId.SelectedItem;

                String idSeleccionado = fila["Id"].ToString();

                DataTable dt = bd.ObtenerLibroId(int.Parse(idSeleccionado));

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
