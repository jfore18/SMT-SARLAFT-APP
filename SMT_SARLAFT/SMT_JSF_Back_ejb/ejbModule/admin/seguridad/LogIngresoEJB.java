package admin.seguridad;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import javax.naming.NamingException;

@Local
public interface LogIngresoEJB {
    Long create(Hashtable pDatos) throws Exception;

    List<LogIngreso> findAll();

    LogIngreso findByPrimaryKey(Long primaryKey);

    LogIngreso findById(Long id);

    LogIngreso getLogIngreso();

    void setLogIngreso(LogIngreso logIngreso);

    void actualizarLogIngresoIdUsuario(LogIngreso logIngreso);

    void actualizarLogIngresoId(LogIngreso logIngreso);
}
