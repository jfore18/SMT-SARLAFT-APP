package admin;

import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.Remote;
import javax.ejb.FinderException;
import javax.ejb.Local;

@Local
public interface HistoricoEntidadesExcluidasEJB {
    Long create(Hashtable pDatos) throws CreateException;

    void actualizarHistoricoEntidadesExcluidas(HistoricoEntidadesExcluidas historicoEntidadesExcluidas);

    HistoricoEntidadesExcluidas findByPrimaryKey(String primaryKey) throws FinderException;
    // Collection findAll() throws FinderException;
}
