package admin;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface CriteriosInusualidadEJB {
    Long create(Hashtable pDatos) throws CreateException;

    CriteriosInusualidad findByPrimaryKey(Long primaryKey) throws FinderException;

    void actualizarCriteriosInusualidad(CriteriosInusualidad criteriosInusualidad);

    CriteriosInusualidad getCriteriosInusualidad();

    // Collection findAll() throws FinderException;
}
