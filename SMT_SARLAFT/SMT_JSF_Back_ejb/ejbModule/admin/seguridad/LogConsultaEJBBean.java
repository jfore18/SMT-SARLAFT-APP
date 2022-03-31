package admin.seguridad;

import admin.Contextos;

import admin.usuario.UsuarioEJB;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.Date;

import java.sql.SQLException;

import java.util.Calendar;
import java.util.Hashtable;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.sql.DataSource;

import javax.transaction.UserTransaction;

@Stateless(name = "LogConsultaEJB", mappedName = "SMT_JSF")
@TransactionManagement(TransactionManagementType.BEAN)
public class LogConsultaEJBBean implements LogConsultaEJB {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;
    @EJB
    private UsuarioEJB usuarioEJB;
    private EntityContext _context;
    private Connection sesionBD;
    private TablaBD miTabla;
    private LogConsulta logConsulta;

    public Long create(Hashtable pDatos) {
        Long usuario = (Long) pDatos.get("USUARIO");
        String canal = (String) pDatos.get("CANAL");
        String nombrePc = (String) pDatos.get("NOMBREPC");
        Calendar fechaEjecucion = (Calendar) pDatos.get("FECHAEJECUCION");
        String query = (String) pDatos.get("QUERY");
        String dominioRed = (String) pDatos.get("DOMINIORED");
        String tipoId = (String) pDatos.get("TIPOID");
        Long numeroId = Long.valueOf((String) pDatos.get("NUMEROID")).longValue();
        //Long numeroId = new Long(0);
        String tipoProducto = (String) pDatos.get("TIPOPRODUCTO");
        String numeroProducto = (String) pDatos.get("NUMEROPRODUCTO");
        int tipoBusqueda = ((Integer) pDatos.get("TIPOBUSQUEDA")).intValue();
        int tipoTransaccion = ((Integer) pDatos.get("TIPOTRANSACCION")).intValue();
        int resultadoTx = ((Integer) pDatos.get("RESULTADOTX")).intValue();
        String descripcionRechazo = (String) pDatos.get("DESCRIPCIONRECHAZO");
        String usuarioNT = (String) pDatos.get("USUARIONT");
        try {
            usuarioEJB.findByPrimaryKey(usuario);
        } catch (Exception noHallado) {
            throw new EJBException("LogConsulta|create(): usuario no existe");
        }
        Long secuencia = this.buscarId();
        this.logConsulta = new LogConsulta();
        this.logConsulta.setConsecutivo(secuencia);
        this.logConsulta.setUsuario(usuario);
        this.logConsulta.setCanal(canal);
        this.logConsulta.setNombrePc(nombrePc);
        this.logConsulta.setFechaEjecucion(fechaEjecucion);
        this.logConsulta.setFechaEnvio(Calendar.getInstance());
        this.logConsulta.setDominioRed(dominioRed);
        this.logConsulta.setTipoId(tipoId);
        this.logConsulta.setNumeroId(numeroId);
        this.logConsulta.setTipoProducto(tipoProducto);
        this.logConsulta.setNumeroProducto(numeroProducto);
        this.logConsulta.setTipoBusqueda(tipoBusqueda);
        this.logConsulta.setTipoTransaccion(tipoTransaccion);
        this.logConsulta.setResultadoTx(resultadoTx);
        this.logConsulta.setDescripcionRechazo(descripcionRechazo);
        this.logConsulta.setUsuarioNt(usuarioNT);
        if (query.length() > 4000)
            query = query.substring(0, 4000);
        this.logConsulta.setQuery(query);
        this.guardarLogConsulta(this.logConsulta);
        return secuencia;
    }

    public void guardarLogConsulta(LogConsulta logConsulta) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.merge(logConsulta);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("LogConsultaEJB|guardarLogConsulta: transacción null ");
                }
                logConsulta = null;
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("LogConsultaEJB|guardarLogConsulta: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("LogConsultaEJB|guardarLogConsulta: sessionContext null ");
        }
    }

    public LogConsulta getLogConsulta() {
        return this.logConsulta;
    }

    public void setLogConsulta(LogConsulta logConsulta) {
        this.logConsulta = logConsulta;
    }

    private UsuarioEJB getUsuarioEJB() throws NamingException {
        Contextos ctx = new Contextos();
        return (UsuarioEJB) ctx.getContext("UsuarioEJB");
    }

    private Long buscarId() {
        Long id = null;
        try {
            this.establecerConexion();
            miTabla = new TablaBD("LOG_CONSULTAS", "CONSECUTIVO", this.sesionBD);
            Integer tId = miTabla.traerIDSecuencia("SEQ_LOG_CONSULTAS");
            id = new Long(tId.longValue());
        } catch (Exception error) {
            System.out.println("LogConsultaEJB|buscarId (1): " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("LogConsultaEJB|buscarId (2): " + e.getMessage());
            }
        }
        return id;
    }

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

}
