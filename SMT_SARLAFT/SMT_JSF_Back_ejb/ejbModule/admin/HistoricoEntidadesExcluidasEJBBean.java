package admin;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Hashtable;

import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;

import javax.sql.DataSource;

import javax.transaction.UserTransaction;

@Stateless(name = "HistoricoEntidadesExcluidasEJB", mappedName = "SARLAFT-EJB-HistoricoEntidadesExcluidasEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class HistoricoEntidadesExcluidasEJBBean implements HistoricoEntidadesExcluidasEJB {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;
    private HistoricoEntidadesExcluidas historicoEntidadesExcluidas;
    private EntityContext context;
    private Connection sesionBD;
    private TablaBD miTabla;

    public Long create(Hashtable pDatos) {

        String tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
        String numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
        Long usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
        Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
        String accion = (String) pDatos.get("ACCION");

        this.historicoEntidadesExcluidas = new HistoricoEntidadesExcluidas();
        this.historicoEntidadesExcluidas.setId(this.buscarId());
        this.historicoEntidadesExcluidas.setTipoIdentificacion(tipoIdentificacion);
        this.historicoEntidadesExcluidas.setNumeroIdentificacion(numeroIdentificacion);
        this.historicoEntidadesExcluidas.setAccion(accion);
        this.historicoEntidadesExcluidas.setUsuarioActualizacion(usuarioActualizacion);
        this.historicoEntidadesExcluidas.setFechaActualizacion(fechaActualizacion);
        this.guardarHistoricoEntidadesExcluidas(this.historicoEntidadesExcluidas);
        return this.historicoEntidadesExcluidas.getId();
    }

    public void guardarHistoricoEntidadesExcluidas(HistoricoEntidadesExcluidas historicoEntidadesExcluidas) {
        UserTransaction transaction = sessionContext.getUserTransaction();
        try {
            transaction.begin();
            entityManager.persist(historicoEntidadesExcluidas);
            transaction.commit();
        } catch (Exception e) {
            if (transaction == null) {
                throw new EJBException("HistoricoEntidadesExcluidasEJB|guardarHistoricoEntidadesExcluidas: transacción null ");
            }
            historicoEntidadesExcluidas = null;
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new EJBException("HistoricoEntidadesExcluidasEJB|guardarHistoricoEntidadesExcluidas: " +
                                       ex.getMessage());
            }
        }
    }

    public void actualizarHistoricoEntidadesExcluidas(HistoricoEntidadesExcluidas historicoEntidadesExcluidas) {
        UserTransaction transaction = sessionContext.getUserTransaction();
        try {
            transaction.begin();
            entityManager.merge(historicoEntidadesExcluidas);
            transaction.commit();
        } catch (Exception e) {
            if (transaction == null) {
                throw new EJBException("HistoricoEntidadesExcluidasEJB|actualizarHistoricoEntidadesExcluidasEJB: transacción null ");
            }
            historicoEntidadesExcluidas = null;
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new EJBException("HistoricoEntidadesExcluidasEJB|actualizarHistoricoEntidadesExcluidasEJB: " +
                                       ex.getMessage());
            }
        }
    }

    public HistoricoEntidadesExcluidas findByPrimaryKey(String primaryKey) throws FinderException {
        Query query;
        query =
            entityManager.createNamedQuery("HistoricoEntidadesExcluidas.findByPrimaryKey",
                                           HistoricoEntidadesExcluidas.class);
        query.setParameter("id", primaryKey);
        HistoricoEntidadesExcluidas historicoEntidadesExcluidas = (HistoricoEntidadesExcluidas) query.getSingleResult();
        this.historicoEntidadesExcluidas = historicoEntidadesExcluidas;
        return historicoEntidadesExcluidas;
    }

    private Long buscarId() {
        Long id = null;
        try {
            this.establecerConexion();
            miTabla = new TablaBD("HISTORICO_ENTIDADES_EXCLUIDAS", "ID", this.sesionBD);
            Integer tId = miTabla.traerIDSecuencia("SEQ_HISTORICO_ENTIDADES");
            id = new Long(tId.longValue());
        } catch (Exception error) {
            System.out.println("HistoricoEntidadesExcluidasEJB|buscarId (1): " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("HistoricoEntidadesExcluidasEJB|buscarId (2): " + e.getMessage());
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
