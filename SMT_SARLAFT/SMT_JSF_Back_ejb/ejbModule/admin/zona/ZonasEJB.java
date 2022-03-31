package admin.zona;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface ZonasEJB {
    ZonasPK create(Hashtable pDatos) throws CreateException;

    Zonas findByPrimaryKey(ZonasPK zonasPK) throws Exception;

    void actualizarZonas(Zonas zonas);

    void setZonas(Zonas zonas);

    Zonas getZonas();
}
