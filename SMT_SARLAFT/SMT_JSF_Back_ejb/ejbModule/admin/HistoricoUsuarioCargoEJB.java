package admin;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface HistoricoUsuarioCargoEJB {
    Long create(Hashtable pDatos) throws CreateException;

    HistoricoUsuarioCargo findByPrimaryKey(Long primaryKey) throws FinderException;

    Collection findAll() throws FinderException;

    void setHistoricoUsuarioCargo(HistoricoUsuarioCargo historicoUsuarioCargo);

    HistoricoUsuarioCargo getHistoricoUsuarioCargo();
}
