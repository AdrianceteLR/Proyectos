using System;
using System.Collections.Generic;
using System.Data;
using System.DirectoryServices.ActiveDirectory;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using MySql.Data.MySqlClient;
using MySql.Data.Types;

namespace Biblioteca
{
    public class BaseDeDatos
    {
        static MySqlConnection conexion = new MySqlConnection();
        String host;
        String puerto;
        String BBDD;
        String usuario;
        String password;
        String CadenaDeConexion;
        MySqlCommand comando = new MySqlCommand();

        public void CrearCadenaDeConexion(String host, String puerto, String BBDD, String usuario, String password)
        {
            this.host = "Server=" + host + ";";
            this.puerto = "Port=" + puerto + ";";
            this.BBDD = "Database=" + BBDD + ";";
            this.usuario = "Uid=" + usuario + ";";
            this.password = "Pwd=" + password + ";";
            CadenaDeConexion = this.host + this.puerto + this.BBDD + this.usuario + this.password;
        }

        public void Conectar()
        {
            try
            {
                conexion.ConnectionString = CadenaDeConexion;
                conexion.Open();

                MessageBox.Show("Conectando Base de Datos", "Mensaje");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al conectar: " + ex.Message);
            }
        }

        public void Desconectar()
        {
            try
            {
                conexion.Close();
                
                MessageBox.Show("Cerrando Base de Datos", "Mensaje");
            }
            catch(Exception ex)
            {
                MessageBox.Show("Error al desconectar: " + ex.Message);
            }
        }
        /*
            txtTitulo.Text = String.Empty;
            txtAutor.Text = String.Empty;
            txtEditorial.Text = String.Empty;
            dpFechaPublicacion.Text = String.Empty;
            txtImagen.Text = String.Empty;
            txtDescripcion.Text = String.Empty;
            txtPrecio.Text = String.Empty;
            txtUnidadesAlmacen.Text = String.Empty;
            chkVenta.IsChecked = false; 
        , String Imagen, String Descripcion,
            String precio, String UnidadesAlmacen, int Venta
         */

        public void InsertarLibro(String titulo, String autor, String editorial, String fechaPublicacion,
            String imagen, String descripcion, String precio, String unidadesAlmacen, int venta)
        {
            String sentenciaSQL = "INSERT INTO catalogo (Titulo, Autor, Editorial, fecha_publicacion," +
                "Imagen, Descripcion, Precio, Unidades, Enventa) VALUES (@Titulo, @Autor, @Editorial, @fechaPublicacion," +
                "@Imagen, @Descripcion, @Precio, @Unidades, @Enventa)";

            comando.Parameters.AddWithValue("@Titulo", titulo);
            comando.Parameters.AddWithValue("@Autor", autor);
            comando.Parameters.AddWithValue("@Editorial", editorial);
            comando.Parameters.AddWithValue("@fechaPublicacion", fechaPublicacion);
            comando.Parameters.AddWithValue("@Imagen", imagen);
            comando.Parameters.AddWithValue("@Descripcion", descripcion);
            comando.Parameters.AddWithValue("@Precio", precio);
            comando.Parameters.AddWithValue("@Unidades", unidadesAlmacen);
            comando.Parameters.AddWithValue("@Enventa", venta);

            String actualizarIdSQL = "ALTER TABLE catalogo AUTO_INCREMENT = 1";
            MySqlCommand actualizarIdSQLCommand = new MySqlCommand(actualizarIdSQL, conexion);

            actualizarIdSQLCommand.ExecuteNonQuery();
            comando.CommandText = sentenciaSQL;
            comando.Connection = conexion;
            comando.ExecuteNonQuery();
        }

        public DataTable CargarLibros()
        {
            DataTable dt = new DataTable();

            string sentenciaSQL = "SELECT Id, Titulo, Autor, Editorial FROM catalogo";

            using (MySqlCommand cmd = new MySqlCommand(sentenciaSQL, conexion))
            {
                using (MySqlDataAdapter adapter = new MySqlDataAdapter(cmd))
                {
                    adapter.FillSchema(dt, SchemaType.Source);
                    adapter.Fill(dt);
                }
            }
            return dt;
        }

        public DataTable ObtenerLibroId(int id)
        {
            DataTable dt = new DataTable();

            String sentenciaSQL = "SELECT * FROM catalogo WHERE Id = @id";

            using (MySqlCommand command = new MySqlCommand(sentenciaSQL, conexion))
            {
                command.Parameters.Add("@id", MySqlDbType.Int32).Value = id;

                using (MySqlDataAdapter adapter = new MySqlDataAdapter(command))
                {
                    adapter.FillSchema(dt, SchemaType.Source);
                    adapter.Fill(dt);
                }
            }
            return dt;
        }

        public DataTable ObtenerLibroNombre(String titulo)
        {
            DataTable dt = new DataTable();

            String sentenciaSQL = "SELECT * FROM catalogo WHERE Titulo = @titulo";

            using (MySqlCommand command = new MySqlCommand(sentenciaSQL, conexion))
            {
                command.Parameters.Add("@titulo", MySqlDbType.VarChar).Value = titulo;

                using (MySqlDataAdapter adapter = new MySqlDataAdapter(command))
                {
                    adapter.FillSchema(dt, SchemaType.Source);
                    adapter.Fill(dt);
                }
            }
            return dt;
        }

        public void EliminarLibro(int id)
        {
            DataTable dt = new DataTable();

            String sentenciaSQL = "DELETE FROM catalogo WHERE Id = @id";
            comando.Parameters.AddWithValue("@id", id);

            comando.CommandText = sentenciaSQL;
            comando.Connection = conexion;
            comando.ExecuteNonQuery();
        }

        public void ActualizarLibro(int id, String titulo, String autor, String editorial, String fechaPublicacion,
            String imagen, String descripcion, String precio, String unidadesAlmacen, int venta)
        {
            String sentenciaSQL = "UPDATE catalogo SET Titulo = @Titulo, Autor = @Autor, Editorial = @Editorial," +
                "fecha_publicacion = @fechaPublicacion, Imagen = @Imagen, Descripcion = @Descripcion," +
                "Precio = @Precio, Unidades = @Unidades, Enventa = @Enventa WHERE Id = @Id";
            comando.Parameters.AddWithValue("@Id", id);
            comando.Parameters.AddWithValue("@Titulo", titulo);
            comando.Parameters.AddWithValue("@Autor", autor);
            comando.Parameters.AddWithValue("@Editorial", editorial);
            comando.Parameters.AddWithValue("@fechaPublicacion", fechaPublicacion);
            comando.Parameters.AddWithValue("@Imagen", imagen);
            comando.Parameters.AddWithValue("@Descripcion", descripcion);
            comando.Parameters.AddWithValue("@Precio", precio);
            comando.Parameters.AddWithValue("@Unidades", unidadesAlmacen);
            comando.Parameters.AddWithValue("@Enventa", venta);

            String actualizarIdSQL = "ALTER TABLE catalogo AUTO_INCREMENT = 1";
            MySqlCommand actualizarIdSQLCommand = new MySqlCommand(actualizarIdSQL, conexion);

            actualizarIdSQLCommand.ExecuteNonQuery();
            comando.CommandText = sentenciaSQL;
            comando.Connection = conexion;
            comando.ExecuteNonQuery();
        }
    }
}
