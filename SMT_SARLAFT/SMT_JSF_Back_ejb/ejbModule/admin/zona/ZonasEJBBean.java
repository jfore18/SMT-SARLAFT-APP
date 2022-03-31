package admin.zona;

import admin.usuario.Usuario;

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

@Stateless(name = "ZonasEJB", mappedName = "SARLAFT-EJB-ZonasEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class ZonasEJBBean implements ZonasEJB {

    @Resource
    SessionContext sessionContext;

    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;

    private Zonas zonas;

    private EntityContext context;
    private ZonasPK pk;
    private TablaBD miTabla;
    transient private Connection sesionBD;

    public ZonasPK create(Hashtable pDatos) {
        String codRegion = (String) pDatos.get("CODIGO_REGION_V");
        if (codRegion == null) {
            throw new EJBException("ZonasEJB|Crear: Region nula");
        }
        try {
            this.establecerConexion();
            miTabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", this.sesionBD);
            miTabla.asignarValorColumna("TIPO_DATO", new Integer(3));
            miTabla.asignarValorColumna("CODIGO", codRegion);
            if (!miTabla.buscarPK()) {
                throw new EJBException("ZonasEJB:Crear: Region no existe");
            }
        } catch (Exception e) {
            throw new EJBException("ZonasEJB:Crear " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception ex) {
                System.out.println("ZonasEJB: Create Error al cerrar la conexion " + ex.getMessage());
            }
        }

        this.zonas = new Zonas();
        this.zonas.setCodigo(Integer.parseInt(pDatos.get("CODIGO").toString()));

        this.zonas.setCodigoRegionV(codRegion);

        this.zonas.setNombreCorto((String) pDatos.get("NOMBRE_CORTO"));
        this.zonas.setNombreLargo((String) pDatos.get("NOMBRE_LARGO"));
        this.zonas.setUsuarioCreacion((Long) pDatos.get("USUARIO_CREACION"));
        this.zonas.setFechaCreacion((Timestamp) pDatos.get("FECHA_CREACION"));
        this.zonas.setzActiva((Long) pDatos.get("ACTIVA"));
        this.pk = new ZonasPK(this.zonas.getCodigo(), this.zonas.getCodigoRegionV());
        this.guardarZonas(this.zonas);
        return this.pk;
    }

    public void guardarZonas(Zonas zonas) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.persist(zonas);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("ZonasEJB|guardarZonas: transacción null ");
                }
                zonas = null;
                // e.printStackTrace();
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("ZonasEJB|guardarZonas: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("ZonasEJB|guardarZonas: sessionContext null");
        }
    }

    public Zonas findByPrimaryKey(ZonasPK zonasPK) throws Exception {
        this.zonas = null;
        if (entityManager != null) {
            Query query;
            try {
                query = entityManager.createNamedQuery("Zonas.findByPrimaryKey", Zonas.class);
                query.setParameter("codigo", zonasPK.getCodigo());
                query.setParameter("codigoR", zonasPK.getCodigoRegionV());
                Zonas zonas = (Zonas) query.getSingleResult();
                this.zonas = zonas;
            } catch (Exception e) {
                this.zonas = null;
            }
        } else {
            throw new EJBException("ZonasEJB|findByPrimaryKey: entityManager null ");
        }
        return this.zonas;
    }

    public void actualizarZonas(Zonas zonas) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.merge(zonas);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("ZonasEJB|actualizarZonas: transacción null ");
                }
                zonas = null;
                // e.printStackTrace();
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("ZonasEJB|actualizarZonas: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("ZonasEJB|actualizarZonas: sessionContext null");
        }
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public Zonas getZonas() {
        return this.zonas;
    }

    public void setEntityContext(EntityContext ctx) {
        this.context = ctx;
    }

    public void unsetEntityContext() {
        this.context = null;
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
