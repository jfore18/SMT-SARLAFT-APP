package admin;

import java.sql.SQLException;

import java.util.Hashtable;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface EntidadExcluidaEJB {
    void crearEntidad(Hashtable pDatos) throws SQLException;

    void actualizarEntidad(Hashtable pDatos) throws SQLException;

    void borrarEntidad(Hashtable pDatos) throws SQLException;
}
