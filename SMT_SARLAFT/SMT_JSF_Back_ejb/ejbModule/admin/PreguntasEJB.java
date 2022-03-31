package admin;

import java.sql.SQLException;

import java.util.Hashtable;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface PreguntasEJB {
    void adicionarPregunta(Hashtable pDatos) throws SQLException;

    void actualizarPregunta(Hashtable pDatos) throws SQLException;

    Hashtable consultarTiposPregunta() throws SQLException;
}
