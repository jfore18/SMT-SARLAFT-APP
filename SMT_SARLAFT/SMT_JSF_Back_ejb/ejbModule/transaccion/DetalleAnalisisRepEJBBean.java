package transaccion;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;

import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.transaction.UserTransaction;

@Stateless(name = "DetalleAnalisisRepEJB", mappedName = "SARLAFT-EJB-DetalleAnalisisRepEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class DetalleAnalisisRepEJBBean implements DetalleAnalisisRepEJB {
    private EntityContext context;
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;
    private DetalleAnalisisRep detalleAnalisisRep;

    public DetalleAnalisisRepPK create(Hashtable pDatos) {
    	System.out.println("Este es el detalle de analisis rep:" +pDatos);
        Long idRep = (Long) pDatos.get("ID");
        Integer acta = (Integer) pDatos.get("NO_ACTA");
        Timestamp fechaAct = (Timestamp) pDatos.get("FECHA_ACTA");
        String justif = (String) pDatos.get("JUSTIFICACION_FINAL");
        String estadoRep = (String) pDatos.get("CODIGO_ESTADO_REPORTE_V");
        Long usuario = (Long) pDatos.get("USUARIO_ACTUALIZACION");
        Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
        Date dFechaActualizacion = new Date(fechaActualizacion.getTime());

        this.detalleAnalisisRep = new DetalleAnalisisRep();
        this.detalleAnalisisRep.setIdReporte(idRep);
        this.detalleAnalisisRep.setNoActa(acta);
        this.detalleAnalisisRep.setFechaActa(dFechaActualizacion);
        this.detalleAnalisisRep.setJustificacionFinal(justif);
        this.detalleAnalisisRep.setCodigoEstadoReporteV(estadoRep);
        this.detalleAnalisisRep.setUsuarioActualizacion(usuario);
        this.detalleAnalisisRep.setFechaActualizacion(dFechaActualizacion);
        DetalleAnalisisRepPK detalleAnalisisRepPK = new DetalleAnalisisRepPK(idRep, acta);
        this.guardarDetalleAnalisisRep(this.detalleAnalisisRep);
        return detalleAnalisisRepPK;
    }

    public void guardarDetalleAnalisisRep(DetalleAnalisisRep detalleAnalisisRep) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.persist(detalleAnalisisRep);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("DetalleAnalisisRepEJB|guardarDetalleAnalisisRep: transacción null ");
                }
                detalleAnalisisRep = null;
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("DetalleAnalisisRepEJB|guardarDetalleAnalisisRep: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("DetalleAnalisisRepEJB|guardarDetalleAnalisisRep: sessionContext null");
        }
    }

}
