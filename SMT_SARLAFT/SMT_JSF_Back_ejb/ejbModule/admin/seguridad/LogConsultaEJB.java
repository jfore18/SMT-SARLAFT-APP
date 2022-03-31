package admin.seguridad;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.Remote;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.Local;

@Local
public interface LogConsultaEJB {
    Long create(Hashtable pDatos) throws CreateException;

    //LogConsulta findByPrimaryKey(Long primaryKey) throws FinderException;

    //Collection findAll() throws FinderException;

    LogConsulta getLogConsulta();

    void setLogConsulta(LogConsulta logConsulta);
}
