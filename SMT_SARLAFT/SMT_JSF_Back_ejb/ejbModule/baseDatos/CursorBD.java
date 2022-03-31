/**
 * Clase CursorBD
 *
 * @author Henry Mogollon
 * @version 1.0
 *
 * Esta clase es de libre uso y su comercialización directa o indirectamente
 * esta prohibida.
 * Puede ser distribuida libremente mientras mantenga este encabezado
 * de notificación.
 * 
 */
 
package baseDatos;

import java.sql.*;

import javax.sql.*;

import java.util.*;

import java.rmi.*;

import javax.naming.*;


public class CursorBD {

    private Connection sesion;
    private PreparedStatement sentenciaSQL;
    private ResultSet registros;
    private String usuario;
    private String password;


    public CursorBD() {
    }

    public Collection consultarTabla(int pRegistrosConsulta, int pPosicionInicial, String pTabla,
                                     String pCondicion) throws Exception {
        Collection listaRegistros = new ArrayList();
        StringBuffer txtSql = new StringBuffer();

        try {
            if (pTabla == null) {
                throw new Exception("[CV|Tabla]: El nombre de la Tabla no puede ser nulo");
            }

            this.establecerConexion();


            txtSql.append(" SELECT * \n");
            txtSql.append(" FROM  " + pTabla + "\n");

            if (pCondicion != null) {
                txtSql.append(pCondicion);
            }

            boolean limitarRegistros = (pRegistrosConsulta != 0) ? true : false;

            int numeroRegistro = 0;

            this.sentenciaSQL =
                this.sesion.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT='" + CatalogoBD.patronFormatoFecha +
                                             "'");

            boolean altero = this.sentenciaSQL.execute();
            /*
        Modificación para establecer el lenguaje y el tipo de sort de la base de datos.(Milena)
      */

            boolean alteroLenguaje = this.sentenciaSQL.execute("ALTER SESSION SET NLS_LANGUAGE = 'AMERICAN'");
            boolean alteroSort = this.sentenciaSQL.execute("ALTER SESSION SET NLS_SORT = 'BINARY'");


            this.sentenciaSQL =
                this.sesion.prepareStatement(txtSql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE,
                                             ResultSet.CONCUR_UPDATABLE);

            if (this.sentenciaSQL == null) {
                throw new Exception("[CV|Tabla]: La sintaxis SQL puede estar incorrecta. \n " + txtSql);
            }

            this.registros = this.sentenciaSQL.executeQuery();


            if (this.registros == null) {
                throw new Exception("[CV|Tabla]: No se retornaron registros en la consulta. \n " + txtSql);
            }

            registros.last();

            int totalRegistros = registros.getRow();

            if (totalRegistros < pPosicionInicial) {
                throw new ExcepcionBD("[CV|Tabla]: No hay más registros en la consulta. \n ");

            }

            if (pPosicionInicial == 0) {
                registros.beforeFirst();
            } else {
                registros.absolute(pPosicionInicial);
            }


            ResultSetMetaData metaData = this.registros.getMetaData();


            while (this.registros.next()) {
                if (limitarRegistros) {
                    if (numeroRegistro == pRegistrosConsulta) {
                        break;
                    }

                    numeroRegistro++;
                }

                Hashtable datosRegistro = new Hashtable();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String dato = this.registros.getString(metaData.getColumnName(i));

                    if (dato == null) {
                        dato = "null";
                    }

                    datosRegistro.put(metaData.getColumnName(i).toUpperCase(), dato);
                }

                listaRegistros.add(datosRegistro);
            }

            this.registros.close();
            this.sentenciaSQL.close();
        } catch (Exception error) {
            throw new Exception(error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception error) {
                throw new Exception("[CV|Tabla]: \n" + error.getMessage());
            }
        }

        return listaRegistros;
    }


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @Métodos de Contexto y conexión a Base de Datos
     */


    public void establecerConexion() throws Exception {
        this.cerrarConexion();

        DataSource ds = null;
        this.sesion = null;

        /* Eliminar cuando se implemente en el servidor */
        Hashtable env = new Hashtable();

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory");
        env.put(Context.SECURITY_PRINCIPAL, "admin");
        env.put(Context.SECURITY_CREDENTIALS, "ejbadmin");
        env.put(Context.PROVIDER_URL, "ormi://localhost:11007/default");

        InitialContext ic = new InitialContext(env);
        /* */


        //  InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(CatalogoBD.urlDataSource);
        this.sesion = ds.getConnection(this.usuario, this.password);

    }

    public void cerrarConexion() throws SQLException {
        if (this.sesion != null && !this.sesion.isClosed()) {
            this.sesion.close();
            this.sesion = null;
        }
    }

}
