package admin;

import admin.zona.ZonasEJB;
import admin.zona.ZonasPK;

import java.rmi.RemoteException;

import java.sql.Timestamp;

import java.util.Hashtable;

import javax.annotation.Resource;

import javax.ejb.EJBException;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;

import javax.rmi.PortableRemoteObject;

import javax.transaction.UserTransaction;

@Stateless(name = "UnidadesNegocioEJB", mappedName = "SARLAFT-EJB-UnidadesNegocioEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class UnidadesNegocioEJBBean implements UnidadesNegocioEJB {

    @Resource
    SessionContext sessionContext;
    private UnidadesNegocio unidadesNegocio;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;

    private ZonasPK zonaFK;
    private static String refZona = "ZonaDAO";

    public Integer create(Hashtable pDatos) {
        Integer codigo = (Integer) pDatos.get("CODIGO");
        String descripcion = (String) pDatos.get("DESCRIPCION");
        Integer codigoZona = (Integer) pDatos.get("CODIGO_ZONA");
        String codigoRegionV = (String) pDatos.get("CODIGO_REGION_V");
        Integer ceo = (Integer) pDatos.get("CEO");
        Integer plazaCritica = (Integer) pDatos.get("PLAZA_CRITICA");
        Integer activa = (Integer) pDatos.get("ACTIVA");
        Integer isMegabanco = (Integer) pDatos.get("IS_MEGABANCO");
        Long usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
        Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
        this.unidadesNegocio = new UnidadesNegocio();
        this.unidadesNegocio.setActiva(activa);
        this.unidadesNegocio.setCeo(ceo);
        this.unidadesNegocio.setCodigo(codigo);
        this.unidadesNegocio.setCodigoRegionV(codigoRegionV);
        this.unidadesNegocio.setCodigoZona(codigoZona);
        this.unidadesNegocio.setDescripcion(descripcion);
        this.unidadesNegocio.setFechaActualizacion(fechaActualizacion);
        this.unidadesNegocio.setPlazaCritica(plazaCritica);
        this.unidadesNegocio.setIsMegabanco(isMegabanco);
        this.unidadesNegocio.setUsuarioActualizacion(usuarioActualizacion);
        this.guardarUnidadesNegocio(this.unidadesNegocio);
        return this.unidadesNegocio.getCodigo();
    }

    public void guardarUnidadesNegocio(UnidadesNegocio unidadesNegocio) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.persist(unidadesNegocio);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("UnidadesNegocioEJB|guardarUnidadesNegocio: transacción null ");
                }
                unidadesNegocio = null;
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("UnidadesNegocioEJB|guardarUnidadesNegocio: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("UnidadesNegocioEJB|guardarUnidadesNegocio: sessionContext null ");
        }
    }

    public void actualizarUnidadesNegocio(UnidadesNegocio unidadesNegocio) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.merge(unidadesNegocio);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("UnidadesNegocioEJB|actualizarUnidadesNegocio: transacción null ");
                }
                unidadesNegocio = null;
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("UnidadesNegocioEJB|actualizarUnidadesNegocio: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("UnidadesNegocioEJB|actualizarUnidadesNegocio: sessionContext null");
        }
    }

    public UnidadesNegocio findByPrimaryKey(Integer primaryKey) {
        Query query;
        UnidadesNegocio unidadesNegocio = null;
        try {
            query = entityManager.createNamedQuery("UnidadesNegocio.findByPrimaryKey", UnidadesNegocio.class);
            query.setParameter("codigo", primaryKey);
            unidadesNegocio = (UnidadesNegocio) query.getSingleResult();
            this.unidadesNegocio = unidadesNegocio;
        } catch (Exception e) {

        }
        return unidadesNegocio;
    }

    public UnidadesNegocio getUnidadesNegocio() {
        return this.unidadesNegocio;
    }

    public void establecerReferencias() throws NamingException, RemoteException {
        Contextos ctx = new Contextos();
        Object objrefZona = (ZonasEJB) ctx.getContext("ZonasEJB");
        this.zonaFK = (ZonasPK) PortableRemoteObject.narrow(objrefZona, ZonasPK.class);
    }
}
