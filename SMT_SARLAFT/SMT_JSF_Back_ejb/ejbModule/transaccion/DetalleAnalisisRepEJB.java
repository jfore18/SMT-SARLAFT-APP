package transaccion;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface DetalleAnalisisRepEJB {
    DetalleAnalisisRepPK create(Hashtable pDatos);

    // ProcesoAnalisisLocal findByPrimaryKey(ProcesoAnalisisPK primaryKey) throws FinderException;

    // Collection findAll() throws FinderException;
}
