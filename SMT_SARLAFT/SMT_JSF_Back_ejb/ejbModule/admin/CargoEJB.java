package admin;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.Local;
import javax.ejb.Remote;

import javax.naming.NamingException;

@Local
public interface CargoEJB {
    String create(Hashtable pDatos) throws CreateException;

    Cargo findByPrimaryKey(String primaryKey) throws FinderException;

    Collection findAll() throws FinderException;

    void actualizarCargo(Cargo cargo);

    void establecerUsuario(Long pCedula) throws Exception;

    void setCargo(Cargo cargo);

    Cargo getCargo();

}
