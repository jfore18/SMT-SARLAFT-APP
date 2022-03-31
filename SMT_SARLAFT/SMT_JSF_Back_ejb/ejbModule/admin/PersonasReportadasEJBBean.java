package admin;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

@Stateless(name = "PersonasReportadasEJB", mappedName = "SARLAFT-EJB-PersonasReportadasEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class PersonasReportadasEJBBean implements PersonasReportadasEJB {

    @EJB
    private HistoricoPersonasReportadasEJB historicoPerRepEJB;
    private TablaBD miTabla;
    transient private Connection sesionBD;

    public void ingresarPersonaLista(Hashtable pDatos) throws Exception {
        try {
            String codigoMotivo = (String) pDatos.get("CODIGO_MOTIVO_V");
            String tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
            String numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
            if (tipoIdentificacion.equals("S") && numeroIdentificacion == null) {
                numeroIdentificacion = traerSiguienteID();
                pDatos.put("NUMERO_IDENTIFICACION", numeroIdentificacion);
            }
            Integer idReporte = (Integer) pDatos.get("ID_REPORTE");
            String apellidosRazonSocial = (String) pDatos.get("APELLIDOS_RAZON_SOCIAL");
            String nombresRazonComercial = (String) pDatos.get("NOMBRES_RAZON_COMERCIAL");
            String alias = (String) pDatos.get("ALIAS");
            String comentario = (String) pDatos.get("COMENTARIO");
            Timestamp fechaIngreso = (Timestamp) pDatos.get("FECHA_INGRESO");
            String tipoReporte = (String) pDatos.get("TIPO_REPORTE");
            String ros = (String) pDatos.get("ROS");
            Long usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
            Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
            this.establecerConexion();
            miTabla =
                new TablaBD("PERSONAS_REPORTADAS", "CODIGO_MOTIVO_V,TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION",
                            this.sesionBD);
            miTabla.asignarValorColumna("CODIGO_MOTIVO_V", codigoMotivo);
            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.asignarValorColumna("ID_REPORTE", idReporte);
            miTabla.asignarValorColumna("APELLIDOS_RAZON_SOCIAL", apellidosRazonSocial);
            miTabla.asignarValorColumna("NOMBRES_RAZON_COMERCIAL", nombresRazonComercial);
            miTabla.asignarValorColumna("ALIAS", alias);
            miTabla.asignarValorColumna("COMENTARIO", comentario);
            miTabla.asignarValorColumna("FECHA_INGRESO", fechaIngreso);
            miTabla.asignarValorColumna("TIPO_REPORTE", tipoReporte);
            miTabla.asignarValorColumna("ROS", ros);
            miTabla.insertarDatos();
        } catch (Exception e) {
            throw new EJBException("PersonasReportadasEJB|ingresarPersonaLista: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void actualizarDatosPersona(Hashtable pDatos) throws Exception {
        try {
            String codigoMotivo = (String) pDatos.get("CODIGO_MOTIVO_V");
            String tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
            String numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
            Integer idReporte = (Integer) pDatos.get("ID_REPORTE");
            String apellidosRazonSocial = (String) pDatos.get("APELLIDOS_RAZON_SOCIAL");
            String nombresRazonComercial = (String) pDatos.get("NOMBRES_RAZON_COMERCIAL");
            String alias = (String) pDatos.get("ALIAS");
            String comentario = (String) pDatos.get("COMENTARIO");
            Timestamp fechaIngreso = (Timestamp) pDatos.get("FECHA_INGRESO");
            String tipoReporte = (String) pDatos.get("TIPO_REPORTE");
            String ros = (String) pDatos.get("ROS");
            Long usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
            Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
            this.establecerConexion();
            miTabla =
                new TablaBD("PERSONAS_REPORTADAS", "CODIGO_MOTIVO_V,TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION",
                            this.sesionBD);
            miTabla.asignarValorColumna("CODIGO_MOTIVO_V", codigoMotivo);
            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.consultarDatos();

            Hashtable datosHistorico = new Hashtable();
            datosHistorico.put("CODIGO_MOTIVO_V", (String) miTabla.traerValorColumna("CODIGO_MOTIVO_V"));
            datosHistorico.put("TIPO_IDENTIFICACION", (String) miTabla.traerValorColumna("TIPO_IDENTIFICACION"));
            datosHistorico.put("NUMERO_IDENTIFICACION", (String) miTabla.traerValorColumna("NUMERO_IDENTIFICACION"));
            try {
                datosHistorico.put("ID_REPORTE", (Integer) miTabla.traerValorColumna("ID_REPORTE"));
            } catch (Exception ex) {
            }
            try {
                datosHistorico.put("APELLIDOS_RAZON_SOCIAL",
                                   (String) miTabla.traerValorColumna("APELLIDOS_RAZON_SOCIAL"));
            } catch (Exception ex) {
            }
            try {
                datosHistorico.put("NOMBRES_RAZON_COMERCIAL",
                                   (String) miTabla.traerValorColumna("NOMBRES_RAZON_COMERCIAL"));
            } catch (Exception ex) {
            }
            try {
                datosHistorico.put("ALIAS", (String) miTabla.traerValorColumna("ALIAS"));
            } catch (Exception ex) {
            }
            try {
                datosHistorico.put("COMENTARIO", (String) miTabla.traerValorColumna("COMENTARIO"));
            } catch (Exception ex) {
            }
            try {
                datosHistorico.put("FECHA_INGRESO", (Timestamp) miTabla.traerValorColumna("FECHA_INGRESO"));
            } catch (Exception ex) {
            }
            try {
                datosHistorico.put("TIPO_REPORTE", (String) miTabla.traerValorColumna("TIPO_REPORTE"));
            } catch (Exception ex) {
            }
            try {
                datosHistorico.put("ROS", (String) miTabla.traerValorColumna("ROS"));
            } catch (Exception ex) {
            }
            datosHistorico.put("USUARIO_ACTUALIZACION", (Long) pDatos.get("USUARIO_ACTUALIZACION"));
            datosHistorico.put("FECHA_ACTUALIZACION", (Timestamp) pDatos.get("FECHA_ACTUALIZACION"));
            datosHistorico.put("ACCION", "MODIFICACION");
            historicoPerRepEJB.create(datosHistorico);

            miTabla.asignarValorColumna("CODIGO_MOTIVO_V", codigoMotivo);
            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.asignarValorColumna("ID_REPORTE", idReporte);
            miTabla.asignarValorColumna("APELLIDOS_RAZON_SOCIAL", apellidosRazonSocial);
            miTabla.asignarValorColumna("NOMBRES_RAZON_COMERCIAL", nombresRazonComercial);
            miTabla.asignarValorColumna("ALIAS", alias);
            miTabla.asignarValorColumna("COMENTARIO", comentario);
            miTabla.asignarValorColumna("FECHA_INGRESO", fechaIngreso);
            miTabla.asignarValorColumna("TIPO_REPORTE", tipoReporte);
            miTabla.asignarValorColumna("ROS", ros);
            miTabla.actualizarDatos();
        } catch (Exception e) {
            throw new EJBException("PersonasReportadasEJB|actualizarDatosPersona: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void eliminarPersonaLista(Hashtable pDatos) throws Exception {
        try {
            String codigoMotivo = (String) pDatos.get("CODIGO_MOTIVO_V");
            String tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
            String numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
            this.establecerConexion();
            miTabla =
                new TablaBD("PERSONAS_REPORTADAS", "CODIGO_MOTIVO_V,TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION",
                            this.sesionBD);
            miTabla.asignarValorColumna("CODIGO_MOTIVO_V", codigoMotivo);
            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.consultarDatos();
            try {
                pDatos.put("ID_REPORTE", (Integer) miTabla.traerValorColumna("ID_REPORTE"));
            } catch (Exception ex) {
            }
            try {
                pDatos.put("APELLIDOS_RAZON_SOCIAL", (String) miTabla.traerValorColumna("APELLIDOS_RAZON_SOCIAL"));
            } catch (Exception ex) {
            }
            try {
                pDatos.put("NOMBRES_RAZON_COMERCIAL", (String) miTabla.traerValorColumna("NOMBRES_RAZON_COMERCIAL"));
            } catch (Exception ex) {
            }
            try {
                pDatos.put("ALIAS", (String) miTabla.traerValorColumna("ALIAS"));
            } catch (Exception ex) {
            }
            try {
                pDatos.put("COMENTARIO", (String) miTabla.traerValorColumna("COMENTARIO"));
            } catch (Exception ex) {
            }
            try {
                pDatos.put("FECHA_INGRESO", (Timestamp) miTabla.traerValorColumna("FECHA_INGRESO"));
            } catch (Exception ex) {
            }
            try {
                pDatos.put("TIPO_REPORTE", (String) miTabla.traerValorColumna("TIPO_REPORTE"));
            } catch (Exception ex) {
            }
            try {
                pDatos.put("ROS", (String) miTabla.traerValorColumna("ROS"));
            } catch (Exception ex) {
            }
            miTabla.borrarDatos();
            pDatos.put("ACCION", "ELIMINACION");
            historicoPerRepEJB.create(pDatos);
        } catch (Exception e) {
            throw new EJBException("PersonasReportadasEJB|eliminarPersonaLista: " + e.getMessage());
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

    private String traerSiguienteID() throws Exception {
        try {
            this.establecerConexion();
            String consulta =
                "SELECT MAX(NUMERO_IDENTIFICACION) + 1 ID_S FROM PERSONAS_REPORTADAS " +
                " WHERE TIPO_IDENTIFICACION = 'S'";
            Statement st = this.sesionBD.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            String id = null;
            while (rs.next()) {
                id = rs.getString("ID_S");
            }
            rs.close();
            st.close();
            return id;
        } catch (Exception e) {
            throw new EJBException("PersonasReportadasEJB|traerSiguienteID: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }
}
