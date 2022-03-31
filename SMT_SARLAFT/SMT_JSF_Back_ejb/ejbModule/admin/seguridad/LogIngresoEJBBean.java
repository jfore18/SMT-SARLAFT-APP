package admin.seguridad;

import admin.Contextos;

import admin.usuario.Usuario;
import admin.usuario.UsuarioEJB;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.interceptor.AroundInvoke;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

import javax.naming.InitialContext;

import javax.naming.NamingException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;

import javax.sql.DataSource;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

@Stateless(name = "LogIngresoEJB", mappedName = "SMT_JSF")
@TransactionManagement(TransactionManagementType.BEAN)
public class LogIngresoEJBBean implements LogIngresoEJB {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;

    private LogIngreso logIngreso;

    private Connection sesionBD;
    private TablaBD miTabla;

    public Long create(Hashtable pDatos) throws Exception {
        Timestamp fechaIngreso = (Timestamp) pDatos.get("FECHA_LOGIN");
        Timestamp fechaSalida = (Timestamp) pDatos.get("FECHA_LOGOFF");
        Long cedulaUsuario = (Long) pDatos.get("USUARIO");

        Contextos ctx = new Contextos();
        UsuarioEJB usuarioEJB = (UsuarioEJB) ctx.getContext("UsuarioEJB");
        usuarioEJB.findByPrimaryKey(cedulaUsuario);

        Long secuencia = this.buscarId();
        this.logIngreso = new LogIngreso();
        this.logIngreso.setId(secuencia);
        this.logIngreso.setFechaLogin(fechaIngreso);
        this.logIngreso.setFechaLogoff(fechaSalida);
        this.logIngreso.setUsuario(cedulaUsuario);
        this.guardarLogIngreso(this.logIngreso);
        return secuencia;
    }

    public void guardarLogIngreso(LogIngreso logIngreso) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.persist(logIngreso);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("LogIngresoEJB|guardarLogIngreso: transacción null ");
                }
                logIngreso = null;
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("LogIngresoEJB|guardarLogIngreso: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("LogIngresoEJB|guardarLogIngreso: sessionContext null ");
        }
    }

    public void actualizarLogIngresoIdUsuario(LogIngreso logIngreso) {
        UserTransaction transaction = sessionContext.getUserTransaction();
        try {
            transaction.begin();
            entityManager.merge(logIngreso);
            transaction.commit();
        } catch (Exception e) {
            if (transaction == null) {
                throw new EJBException("LogIngresoEJB|actualizarLogIngresoIdUsuario: transacción null ");
            }
            logIngreso = null;
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new EJBException("LogIngresoEJB|actualizarLogIngresoIdUsuario: " + ex.getMessage());
            }
        }
    }

    public void actualizarLogIngresoId(LogIngreso logIngreso) {
        try {
            System.out.println("==>1 " + logIngreso.getId());
            System.out.println("==>2 " + logIngreso.getFechaLogoff());
            SimpleDateFormat SDFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //ob
            String fLogOff = SDFormat.format(logIngreso.getFechaLogoff());
            System.out.println("==>3 " + fLogOff);
            this.establecerConexion();
            String update =
                "UPDATE LOG_INGRESO SET FECHA_LOGOFF = TO_DATE('" + fLogOff +
                "','DD/MM/YYYY HH24:MI:SS') WHERE ID = '" + logIngreso.getId() + "'";
            PreparedStatement sentenciaUpdate = this.sesionBD.prepareStatement(update);
            sentenciaUpdate.executeUpdate();
            sentenciaUpdate.close();
            this.cerrarConexion();
        } catch (Exception e) {
            logIngreso = null;
            e.printStackTrace();
            throw new EJBException("LogIngresoEJB|actualizarLogIngresoId: " + e.getMessage());
        }
    }

    public List<LogIngreso> findAll() {
        return entityManager.createNamedQuery("logingreso.findAll", LogIngreso.class).getResultList();
    }

    public LogIngreso findByPrimaryKey(Long primaryKey) {
        Query query = entityManager.createNamedQuery("logingreso.findByPrimaryKey", LogIngreso.class);
        query.setParameter("IdUsuario", primaryKey);
        LogIngreso logIngreso = (LogIngreso) query.getSingleResult();
        this.logIngreso = logIngreso;
        return logIngreso;
    }

    public LogIngreso findById(Long id) {
        Query query = entityManager.createNamedQuery("logingreso.findById", LogIngreso.class);
        query.setParameter("id", id);
        LogIngreso logIngreso = (LogIngreso) query.getSingleResult();
        this.logIngreso = logIngreso;
        return logIngreso;
    }

    @Override
    public LogIngreso getLogIngreso() {
        return this.logIngreso;
    }

    @Override
    public void setLogIngreso(LogIngreso logIngreso) {
        this.logIngreso = logIngreso;
    }

    private Long buscarId() {
        Long id = null;
        try {
            this.establecerConexion();
        } catch (Exception error) {
            System.out.println("LogIngresoEJB|buscarId (1): " + error.getMessage());
        }
        try {
            //System.out.println("LogIngresoEJB|buscarId try: this.sesionBD = " + this.sesionBD);
            miTabla = new TablaBD("LOG_INGRESO", "ID", this.sesionBD);
        } catch (Exception error) {
            System.out.println("LogIngresoEJB|buscarId (1.1): " + error.getMessage());
        }
        try {
            Integer tId = miTabla.traerIDSecuencia("SEQ_LOG_INGRESO");
            id = new Long(tId.longValue());
        } catch (Exception error) {
            System.out.println("LogIngresoEJB|buscarId (1.2): " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("LogIngresoEJB|buscarId (2): " + e.getMessage());
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
        //System.out.println("LogIngresoEJB|establecerConexion: ds = " + ds);
        this.sesionBD = ds.getConnection();
    }

    private void cerrarConexion() throws SQLException {
        if (this.sesionBD != null && !this.sesionBD.isClosed()) {
            this.sesionBD.close();
            this.sesionBD = null;
        }
    }
}

