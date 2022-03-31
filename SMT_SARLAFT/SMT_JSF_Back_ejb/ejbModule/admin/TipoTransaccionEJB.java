package admin;

import java.sql.SQLException;

import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface TipoTransaccionEJB {

    void crear(Hashtable pDatos) throws SQLException;

    void actualizar(Hashtable pDatos) throws SQLException;
}
