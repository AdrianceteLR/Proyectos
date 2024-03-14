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
    /// Lógica de interacción para Consultar.xaml
    /// </summary>
    public partial class Consultar : Window
    {
        public Consultar()
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
            txtConsultaTitulo.Text = String.Empty;
            rdbId.IsChecked = false;
            rdbTitulo.IsChecked = false;
        }

        private void CargarDatosDataGrid()
        {
            BaseDeDatos bd = new BaseDeDatos();

            DataTable dt = bd.CargarLibros();

            dgLibrosId.ItemsSource = dt.DefaultView;
        }

        private void btnConsultar_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtId.Text) || !String.IsNullOrWhiteSpace(txtConsultaTitulo.Text))
            {
                BaseDeDatos bd = new BaseDeDatos();

                if (rdbId.IsChecked == true && int.TryParse(txtId.Text, out int id))
                {
                    DataTable dti = bd.ObtenerLibroId(id);

                    if (dti.Rows.Count > 0)
                    {
                        DataRow row = dti.Rows[0];

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
                    else
                    {
                        txtId.Text = String.Empty;
                        MessageBox.Show("No se encontró ningún libro con el ID proporcionado.");
                    }
                }

                if (rdbTitulo.IsChecked == true)
                {
                    DataTable dtn = bd.ObtenerLibroNombre(txtConsultaTitulo.Text);

                    if (dtn.Rows.Count > 0)
                    {
                        DataRow row = dtn.Rows[0];

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
                    else
                    {
                        txtConsultaTitulo.Text = String.Empty;
                        MessageBox.Show("No se encontró ningún libro con el Titulo proporcionado.");
                    }
                }
            }
            else
            {
                MessageBox.Show("El campo ID o Titulo debe estar rellando.");
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
