using System;
using System.Windows;
using System.Windows.Threading;

namespace Biblioteca
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private DispatcherTimer timer;
        public MainWindow()
        {
            InitializeComponent();

            timer = new DispatcherTimer();
            timer.Interval = TimeSpan.FromSeconds(1);
            timer.Tick += mostrarFecha;
            timer.Start();

        }

        private void mostrarFecha(object sender, EventArgs e)
        {
            lblFecha.Content = "Hoy es " + DateTime.Now.ToString("D");
        }

        private void btnAñadir_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtHost.Text) && !String.IsNullOrWhiteSpace(txtPuerto.Text) && !String.IsNullOrWhiteSpace(txtBBDD.Text) && !String.IsNullOrWhiteSpace(txtUsuario.Text) && !String.IsNullOrWhiteSpace(pwdPassword.Password))
            {
                BaseDeDatos bd = new BaseDeDatos();
                bd.CrearCadenaDeConexion(txtHost.Text, txtPuerto.Text, txtBBDD.Text, txtUsuario.Text, pwdPassword.Password);
                bd.Conectar();

                Añadir añadir = new Añadir();
                añadir.Show();
            }
            else
            {
                MessageBox.Show("Por favor, introduce todos los datos de la base de datos");
            }
        }


        private void btnModificar_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtHost.Text) && !String.IsNullOrWhiteSpace(txtPuerto.Text) && !String.IsNullOrWhiteSpace(txtBBDD.Text) && !String.IsNullOrWhiteSpace(txtUsuario.Text) && !String.IsNullOrWhiteSpace(pwdPassword.Password))
            {
                BaseDeDatos bd = new BaseDeDatos();
                bd.CrearCadenaDeConexion(txtHost.Text, txtPuerto.Text, txtBBDD.Text, txtUsuario.Text, pwdPassword.Password);
                bd.Conectar();

                Modificar modificar = new Modificar();
                modificar.Show();
            }
            else
            {
                MessageBox.Show("Por favor, introduce todos los datos de la base de datos");
            }
        }

        private void btnConsultar_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtHost.Text) && !String.IsNullOrWhiteSpace(txtPuerto.Text) && !String.IsNullOrWhiteSpace(txtBBDD.Text) && !String.IsNullOrWhiteSpace(txtUsuario.Text) && !String.IsNullOrWhiteSpace(pwdPassword.Password))
            {
                BaseDeDatos bd = new BaseDeDatos();
                bd.CrearCadenaDeConexion(txtHost.Text, txtPuerto.Text, txtBBDD.Text, txtUsuario.Text, pwdPassword.Password);
                bd.Conectar();

                Consultar consultar = new Consultar();
                consultar.Show();
            }
            else
            {
                MessageBox.Show("Por favor, introduce todos los datos de la base de datos");
            }
        }

        private void btnEliminar_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtHost.Text) && !String.IsNullOrWhiteSpace(txtPuerto.Text) && !String.IsNullOrWhiteSpace(txtBBDD.Text) && !String.IsNullOrWhiteSpace(txtUsuario.Text) && !String.IsNullOrWhiteSpace(pwdPassword.Password))
            {
                BaseDeDatos bd = new BaseDeDatos();
                bd.CrearCadenaDeConexion(txtHost.Text, txtPuerto.Text, txtBBDD.Text, txtUsuario.Text, pwdPassword.Password);
                bd.Conectar();

                Eliminar eliminar = new Eliminar();
                eliminar.Show();
            }
            else
            {
                MessageBox.Show("Por favor, introduce todos los datos de la base de datos");
            }
        }

        private void btnListar_Click(object sender, RoutedEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(txtHost.Text) && !String.IsNullOrWhiteSpace(txtPuerto.Text) && !String.IsNullOrWhiteSpace(txtBBDD.Text) && !String.IsNullOrWhiteSpace(txtUsuario.Text) && !String.IsNullOrWhiteSpace(pwdPassword.Password))
            {
                BaseDeDatos bd = new BaseDeDatos();
                bd.CrearCadenaDeConexion(txtHost.Text, txtPuerto.Text, txtBBDD.Text, txtUsuario.Text, pwdPassword.Password);
                bd.Conectar();

                Listar listar = new Listar();
                listar.Show();
            }
            else
            {
                MessageBox.Show("Por favor, introduce todos los datos de la base de datos");
            }
        }

        private void btnSalir_Click(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown();
        }

        private void btnLimpiar_Click(object sender, RoutedEventArgs e)
        {
            txtHost.Text = String.Empty;
            txtPuerto.Text = String.Empty;
            txtBBDD.Text = String.Empty;
            txtUsuario.Text = String.Empty;
            pwdPassword.Password = String.Empty;
        }
    }
}
