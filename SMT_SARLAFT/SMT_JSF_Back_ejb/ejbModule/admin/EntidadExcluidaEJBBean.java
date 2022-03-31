package admin;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

@Stateless(name = "EntidadExcluidaEJB", mappedName = "SARLAFT-EJB-EntidadExcluidaEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class EntidadExcluidaEJBBean implements EntidadExcluidaEJB {

    @EJB
    HistoricoEntidadesExcluidasEJB historicoEntidadesExcluidasEJB;
    private TablaBD miTabla;
    transient private Connection sesionBD;

    public void crearEntidad(Hashtable pDatos) throws SQLException {
        try {
            boolean insertar = false;
            String tipoEntidad = (String) pDatos.get("TIPO_ENTIDAD");
            String tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
            String numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
            String nombre = (String) pDatos.get("NOMBRE");
            String flagEntidad = null;
            Integer obligarCarga = (Integer) pDatos.get("OBLIGAR_CARGA");

            Long usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
            Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");

            this.establecerConexion();
            miTabla = new TablaBD("ENTIDADES_EXCLUIDAS", "TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION", this.sesionBD);
            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            try {
                if (miTabla.buscarPK()) {
                    miTabla.consultarDatos();
                    String flags = (String) miTabla.traerValorColumna("FLAGS_TIPOS");
                    int posicionFlagEncendido = Integer.parseInt(tipoEntidad);
                    flagEntidad = this.setFlag(flags, posicionFlagEncendido);
                } else {
                    insertar = true;
                    String flags = "00000000";
                    int posicionFlagEncendido = Integer.parseInt(tipoEntidad);
                    flagEntidad = this.setFlag(flags, posicionFlagEncendido);
                }
            } catch (Exception ex) {
                throw new EJBException("EntidadExcluidaEJB|crearEntidad: " + ex.getMessage());
            }
            miTabla.asignarValorColumna("OBLIGAR_CARGA", obligarCarga);
            miTabla.asignarValorColumna("FLAGS_TIPOS", flagEntidad);
            miTabla.asignarValorColumna("NOMBRE", nombre);
            miTabla.asignarValorColumna("FECHA_ACTUALIZACION", fechaActualizacion);

            if (insertar) {
                miTabla.insertarDatos();
                pDatos.put("ACCION", "I" + flagEntidad + obligarCarga);
            } else {
                miTabla.actualizarDatos();
                pDatos.put("ACCION", "IA" + flagEntidad + obligarCarga);
            }

            historicoEntidadesExcluidasEJB.create(pDatos);
        } catch (Exception e) {
            throw new EJBException("EntidadExcluidaEJB|crearEntidad: Fallo " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }

    }

    public void actualizarEntidad(Hashtable pDatos) throws SQLException {
        try {
            String tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
            String numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
            String nombreEntidad = (String) pDatos.get("NOMBRE");
            Integer obligarCarga = (Integer) pDatos.get("OBLIGAR_CARGA");
            Integer obligarCargaAnt = null;
            Long usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
            Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
            String flags = "";

            this.establecerConexion();
            miTabla = new TablaBD("ENTIDADES_EXCLUIDAS", "TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION", this.sesionBD);
            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.consultarDatos();

            Hashtable datosHistorico = new Hashtable();
            datosHistorico.put("TIPO_IDENTIFICACION", (String) miTabla.traerValorColumna("TIPO_IDENTIFICACION"));
            datosHistorico.put("NUMERO_IDENTIFICACION", (String) miTabla.traerValorColumna("NUMERO_IDENTIFICACION"));
            try {
                flags = (String) miTabla.traerValorColumna("FLAGS_TIPOS");
                obligarCargaAnt = (Integer) miTabla.traerValorColumna("OBLIGAR_CARGA");
            } catch (Exception ex) {
            }

            datosHistorico.put("USUARIO_ACTUALIZACION", usuarioActualizacion);
            datosHistorico.put("FECHA_ACTUALIZACION", (Timestamp) pDatos.get("FECHA_ACTUALIZACION"));
            datosHistorico.put("ACCION", "UP" + flags + obligarCargaAnt);
            historicoEntidadesExcluidasEJB.create(datosHistorico);

            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.asignarValorColumna("NOMBRE", nombreEntidad);
            miTabla.asignarValorColumna("OBLIGAR_CARGA", obligarCarga);
            miTabla.asignarValorColumna("FECHA_ACTUALIZACION", fechaActualizacion);

            miTabla.actualizarDatos();

        } catch (Exception e) {
            throw new EJBException("EntidadExcluidaEJB|actualizarEntidad: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void borrarEntidad(Hashtable pDatos) throws SQLException {
        try {
            String tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
            String numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");

            this.establecerConexion();

            miTabla = new TablaBD("ENTIDADES_EXCLUIDAS", "TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION", this.sesionBD);
            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.consultarDatos();
            String flags = "";
            Integer obligarCarga = new Integer(0);
            try {
                flags = (String) miTabla.traerValorColumna("FLAGS_TIPOS");
                obligarCarga = (Integer) miTabla.traerValorColumna("OBLIGAR_CARGA");
            } catch (Exception ex) {
            }

            miTabla.borrarDatos();
            pDatos.put("ACCION", "DEL" + flags + obligarCarga);

            historicoEntidadesExcluidasEJB.create(pDatos);
        } catch (Exception e) {
            throw new EJBException("EntidadExcluidaEJB|eliminarEntidad: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    /**
     * @Métodos de Contexto y conexión a Base de Datos
     */

    private void establecerConexion() throws Exception {
        this.cerrarConexion();

        DataSource ds = null;
        this.sesionBD = null;

        InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(CatalogoBD.urlDataSource);
        this.sesionBD = ds.getConnection();
    }

    private void cerrarConexion() throws SQLException {
        if (this.sesionBD != null && !this.sesionBD.isClosed()) {
            this.sesionBD.close();
            this.sesionBD = null;
        }
    }

    private String setFlag(String flagAnterior, int tipoLista) {
        try {
            StringBuffer sbTemporal = new StringBuffer(flagAnterior);
            sbTemporal = sbTemporal.replace(tipoLista - 1, tipoLista, "1");
            return sbTemporal.toString();
        } catch (Exception ex) {
            throw new EJBException("EntidadExcluidaEJB|setFlag : " + ex.getMessage());
        }
    }


}
