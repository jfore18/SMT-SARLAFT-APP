package transaccion;

import javax.ejb.EJBLocalObject;

import java.util.Hashtable;

import java.sql.SQLException;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface ReporteEJB {
    Hashtable crearReporte(Hashtable pDatos) throws SQLException;

    void adicionarPersona(Hashtable pDatos) throws SQLException;

    void adicionarTxn(Hashtable pDatos) throws SQLException;

    void adicionarRespuesta(Hashtable pDatos) throws SQLException;

    Hashtable mostrarDatos(Integer pIdReporte);

    Collection mostrarTxn(Integer pId);

    Collection mostrarPersonas(Integer pId);

    Hashtable mostrarRespuestas(Integer pId);

    String actualizarEstado(Hashtable pDatos);

    void marcarRevisado(String pIdReporte) throws SQLException;

    void adicionarPersonaLista(Collection pDatos) throws SQLException;

    Integer actualizarReporte(Hashtable pDatos) throws SQLException;

    boolean limpiarGestion(Integer pIdReporte) throws SQLException;

    boolean limpiarAnalisis(Integer pIdReporte) throws SQLException;

    void actualizarReportesAsociados(Integer pIdReporte) throws SQLException;
}
