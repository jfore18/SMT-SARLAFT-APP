package transaccion;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface HistoricoTransaccionEJB {

    Integer create(Hashtable pDatos) throws CreateException;

    HistoricoTransaccionEJB findByPrimaryKey(Integer primaryKey) throws FinderException;

    Collection findAll() throws FinderException;

}
