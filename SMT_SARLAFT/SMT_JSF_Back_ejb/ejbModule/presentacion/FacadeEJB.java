package presentacion;

import java.util.Collection;
import java.util.Hashtable;

import baseDatos.Consulta;

import java.sql.SQLException;

import java.util.List;
import java.util.Vector;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.naming.NamingException;

@Remote
public interface FacadeEJB {
    Collection ejecutarConsultaGeneral(String pCondicion, Consulta pConsultaVisibilidad, int pNumeroResultados);

    Collection ejecutarConsultaHistorica(int totalRegistros, String pCondicion, Consulta pConsultaVisibilidad);

    Collection consultarMenu(String pCodigoUsuario);

    Collection ejecutarConsulta(String pCondicion, Consulta pConsultaVisibilidad, boolean columnas);

    Integer crearReporteInusual(Hashtable pDatos, Collection pSoporte, Collection pTxns);

    Integer crearReporteNormal(Hashtable pDatos, Collection pSoporte, Collection pTxns);

    boolean puedeCrearFiltro(Hashtable pDatos, int pRol);

    Hashtable traerDatosReporte(String idReporte);

    void insertarFechaProceso(String pFecha) throws SQLException;

    void inicioDia(String pFecha, String pUsuario) throws SQLException;

    Integer crearReporteAnalista(Hashtable pDatos, Collection pSoporte, Collection pTxns);

    boolean actualizarGestion(Hashtable pDatos, Collection pSoporte, Collection pTxns);

    boolean reportarAnalista(Hashtable pDatos, Collection pSoporte) throws SQLException;

    boolean noReportarAnalista(Hashtable pDatos) throws SQLException;

    boolean asignarRegionAnalista(String[] pCodigoRegion, String pCargoAnalista) throws SQLException;

    boolean actualizarReporte(Hashtable pDatos, Collection pSoporte);

    boolean cargaArchivo(Hashtable pDatos, Collection pSoporte, Collection pTxns, byte[] encoded, String strIdReporte) throws SQLException, NamingException,
    Exception;
    
    void insertarRegistroHistoricoTransaccion(Hashtable pDatos) throws SQLException;

    void desactivarFiltros(Hashtable pDatos) throws SQLException;

    Vector mostrarRegionesAnalista(String pCodigoCargoAnalista) throws SQLException;

    Collection ejecutarConsultaImproved(String pCondicion, Consulta pConsultaVisibilidad);

    void marcarFiltrosCambioUsuario(String pCargo);

    void confirmarFiltro(Hashtable pDatos);

    boolean isConfirmarFiltro(String pCargo);

    void revisarComentario(Hashtable pDatos) throws Exception;

    void crearComentario(Hashtable pDatos) throws Exception;

    String[] traerCargosEnviarComentario(String pCodigoCargo);

    Vector mostrarPerfilesCriterio(String pCodigoCriterio) throws SQLException;

    boolean asignarPerfilCriterio(List<String> pCodigoPerfil, String pIdCriterio) throws SQLException;

    void insertarRegistroConsultaDUCC(Hashtable pDatos) throws SQLException;

    void eliminarRegistroConsultaDUCC(Hashtable pDatos) throws SQLException;

    String traerEquivalencia(Hashtable pDatos) throws SQLException;

    void inactivaFiltroId(Hashtable pDatos, String idF) throws SQLException;

    void aprobarFiltro(Hashtable pDatos) throws SQLException;

    void rechazarFiltro(Hashtable pDatos) throws SQLException;
}
