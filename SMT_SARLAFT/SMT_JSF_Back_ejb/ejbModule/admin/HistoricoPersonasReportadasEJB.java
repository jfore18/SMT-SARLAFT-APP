package admin;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface HistoricoPersonasReportadasEJB {
    Long create(Hashtable pDatos) throws CreateException;

    Collection findByPrimaryKey(Long primaryKey) throws FinderException;

    //  Collection findAll() throws FinderException, RemoteException;
}
