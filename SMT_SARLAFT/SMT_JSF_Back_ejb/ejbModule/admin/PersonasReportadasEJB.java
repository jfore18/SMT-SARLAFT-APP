package admin;

import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalObject;
import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface PersonasReportadasEJB {

    void ingresarPersonaLista(Hashtable pDatos) throws Exception;

    void actualizarDatosPersona(Hashtable pDatos) throws Exception;

    void eliminarPersonaLista(Hashtable pDatos) throws Exception;
}
