package admin;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface UnidadesNegocioEJB {
    Integer create(Hashtable pDatos);

    UnidadesNegocio findByPrimaryKey(Integer primaryKey);

    void actualizarUnidadesNegocio(UnidadesNegocio unidadesNegocio);

    UnidadesNegocio getUnidadesNegocio();
    // Collection findAll() throws FinderException;
}
