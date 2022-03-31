package transaccion;

import java.sql.SQLException;

import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface TransaccionEJB {
    void actualizarCampo(Hashtable pDatos) throws SQLException;
}
