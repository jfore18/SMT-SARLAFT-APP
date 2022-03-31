package admin;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;

import javax.sql.DataSource;

@Stateless(name = "HistoricoPersonasReportadasEJB", mappedName = "SARLAFT-EJB-HistoricoPersonasReportadasEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class HistoricoPersonasReportadasEJBBean implements HistoricoPersonasReportadasEJB {
    private EntityContext context;
    private Connection sesionBD;
    private TablaBD miTabla;
    private HistoricoPersonasReportadas historicoPersonasReportadas;

    public Long create(Hashtable pDatos) {
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
        String accion = (String) pDatos.get("ACCION");
        this.historicoPersonasReportadas = new HistoricoPersonasReportadas();
        this.historicoPersonasReportadas.setId(this.buscarId());
        this.historicoPersonasReportadas.setAccion(accion);
        this.historicoPersonasReportadas.setAlias(alias);
        this.historicoPersonasReportadas.setApellidosRazonSocial(apellidosRazonSocial);
        this.historicoPersonasReportadas.setCodigoMotivoV(codigoMotivo);
        this.historicoPersonasReportadas.setComentario(comentario);
        this.historicoPersonasReportadas.setFechaActualizacion(fechaActualizacion);
        this.historicoPersonasReportadas.setFechaIngreso(fechaIngreso);
        this.historicoPersonasReportadas.setIdReporte(idReporte);
        this.historicoPersonasReportadas.setNombresRazonComercial(nombresRazonComercial);
        this.historicoPersonasReportadas.setNumeroIdentificacion(numeroIdentificacion);
        this.historicoPersonasReportadas.setRos(ros);
        this.historicoPersonasReportadas.setTipoIdentificacion(tipoIdentificacion);
        this.historicoPersonasReportadas.setTipoReporte(tipoReporte);
        this.historicoPersonasReportadas.setUsuarioActualizacion(usuarioActualizacion);
        return this.historicoPersonasReportadas.getId();
    }

    public Collection findByPrimaryKey(Long primaryKey) {
        return null;
    }


    public void setEntityContext(EntityContext ctx) {
        this.context = ctx;
    }

    public void unsetEntityContext() {
        this.context = null;
    }


    private Long buscarId() {
        Long id = null;
        try {
            this.establecerConexion();
            miTabla = new TablaBD("HISTORICO_PERSONAS_REPORTADAS", "ID", this.sesionBD);
            Integer tId = miTabla.traerIDSecuencia("SEQ_HISTORICO_PERSONAS_REPORT");
            id = new Long(tId.longValue());
        } catch (Exception error) {
            System.out.println("HistoricoPersonasReportadasEJB|buscarId (1): " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("HistoricoPersonasReportadasEJB|buscarId (2): " + e.getMessage());
            }
        }
        return id;
    }

    public void establecerConexion() throws Exception {
        this.cerrarConexion();

        DataSource ds = null;
        this.sesionBD = null;

        InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(CatalogoBD.urlDataSource);
        this.sesionBD = ds.getConnection();
    }

    public void cerrarConexion() throws SQLException {
        if (this.sesionBD != null && !this.sesionBD.isClosed()) {
            this.sesionBD.close();
            this.sesionBD = null;
        }
    }
}
