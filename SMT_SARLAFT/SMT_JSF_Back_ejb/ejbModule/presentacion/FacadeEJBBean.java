package presentacion;

import admin.Contextos;

import baseDatos.*;
import baseDatos.Consulta;

import entidades.ControlEntidad;

import java.math.*;

import java.sql.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.Date;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import transaccion.*;
import wsproxy.proxy.onbase.com.bancodebogota.WSProcesoOnBasePortClient;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.BinaryType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.CustIdType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.NetworkTrnInfoType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.DocumentAddRqType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service.SetDocumentRequest;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.DocKeywordsType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.DocTypeRefType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.FileInfoType;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Stateless(name = "FacadeEJB", mappedName = "SMT_JSF")
@TransactionManagement(TransactionManagementType.BEAN)
public class FacadeEJBBean implements FacadeEJB {

    @Resource
    private SessionContext sessionContext;

    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;
    @EJB
    private ConsultaTablaEJB consultaTablaEJB;
    @EJB
    private ConsultaEJB consultaEJB;
    @EJB
    private ReporteEJB reporteEJB;
    @EJB
    private TransaccionEJB transaccionEJB;
    @EJB
    private ComentarioEJB comentarioEJB;
    @EJB
    private ComentarioCargoEJB comentarioCargoEJB;
    @EJB
    private FiltroEJB filtroEJB;
    private TablaBD tabla;
    public static String INUSUAL = "I";
    public static String PREINUSUAL = "P";
    public static String NORMAL = "N";
    public static String SOSPECHOSA = "S";
    transient private Connection sesionBD;

    public void setSessionContext(SessionContext ctx) {
    }

    public Collection ejecutarConsultaGeneral(String pCondicion, Consulta pConsultaVisibilidad, int pNumeroResultados) {
        try {

            pCondicion = pCondicion == null ? " " : pCondicion;
            StringBuffer consulta = new StringBuffer(pConsultaVisibilidad.getSeleccion());
            consulta = consulta.append(" " + pConsultaVisibilidad.getTabla());
            consulta = consulta.append(" " + pConsultaVisibilidad.getCondicion());

            if (consulta.toString().indexOf(" WHERE ") != -1) {
                consulta.append(pCondicion);
            } else {

                if (pCondicion.startsWith(" ORDER BY ")) {
                    consulta.append(pCondicion);
                } else {
                    if (pCondicion.startsWith(" AND ")) {
                        consulta.append(" WHERE " + pCondicion.substring(4));
                    } else {
                        if (pCondicion.trim().length() > 0)
                            consulta.append(" WHERE " + pCondicion);
                    }
                }
            }

            if (pConsultaVisibilidad.getAgrupacion() != null &&
                pConsultaVisibilidad.getAgrupacion().trim().length() > 0) {
                consulta = consulta.append(pConsultaVisibilidad.getAgrupacion());
            }
            consulta = consulta.append(" " + pConsultaVisibilidad.getOrden());
            //this.consultaTablaEJB = this.getConsultaTablaEJB();
            //OB ConsultaTablaLocal consultaTablaLocal = this.consultaTablaHome.create();
            System.out.println("consulta: " + consulta);
            return this.consultaTablaEJB.consultarTabla(pNumeroResultados, 0, null, consulta.toString());

        } catch (Exception error) {
            throw new EJBException("FacadeEJB|ejecutarConsultaGeneral : " + error.getMessage());
        }
    }

    public Collection ejecutarConsultaHistorica(int totalRegistros, String pCondicion, Consulta pConsultaVisibilidad) {
        try {
            StringBuffer consulta = new StringBuffer("SELECT * FROM (" + pConsultaVisibilidad.getSeleccion());
            consulta = consulta.append(" " + pConsultaVisibilidad.getTabla());
            consulta = consulta.append(" " + pConsultaVisibilidad.getCondicion());

            if (consulta.toString().indexOf(" WHERE ") != -1) {
                consulta.append(pCondicion);
            } else {

                if (pCondicion.startsWith(" ORDER BY ")) {
                    consulta.append(pCondicion);
                } else {
                    if (pCondicion.startsWith(" AND ")) {
                        consulta.append(" WHERE " + pCondicion.substring(4));
                    } else {
                        if (pCondicion.trim().length() > 0)
                            consulta.append(" WHERE " + pCondicion);
                    }
                }
            }

            if (pConsultaVisibilidad.getAgrupacion() != null &&
                pConsultaVisibilidad.getAgrupacion().trim().length() > 0) {
                consulta = consulta.append(pConsultaVisibilidad.getAgrupacion());
            }

            //this.consultaTablaEJB = this.getConsultaTablaEJB();
            //ConsultaTablaLocal consultaTablaLocal = this.consultaTablaHome.create();
            return this.consultaTablaEJB.consultarTabla(0, totalRegistros, null, consulta.toString());

        } catch (Exception error) {
            throw new EJBException("Facade|ejecutarConsultaHistorica : " + error.getMessage());
        }
    }

    /* private ConsultaTablaEJB getConsultaTablaEJB() throws Exception {
        Contextos ctx = new Contextos();
        ConsultaTablaEJB consultaTablaEJB = (ConsultaTablaEJB) ctx.getContext("ConsultaTablaEJB");
        return consultaTablaEJB;
    } */

    @Override
    public Collection consultarMenu(String pCodigoUsuario) {
        try {
            System.out.println("FacadeEJB|consultarMenu|antes de realizarConsulta|pCodigoUsuario = " + pCodigoUsuario);
            Consulta miConsulta = this.consultaEJB.realizarConsulta(pCodigoUsuario, "2");
            String pConsulta = "SELECT * FROM (" + miConsulta.getSeleccion();
            pConsulta = pConsulta + " " + miConsulta.getTabla();
            pConsulta = pConsulta + " " + miConsulta.getCondicion();
            pConsulta += " ORDER BY ORDEN )";
            Collection resTemp = this.consultaTablaEJB.consultarTabla(0, 0, null, pConsulta);
            return resTemp;
        } catch (Exception error) {
            error.printStackTrace();
            throw new EJBException("Facade|consultaMenu : " + error.getMessage());
        }
    }

    public Collection ejecutarConsulta(String pCondicion, Consulta pConsultaVisibilidad, boolean columnas) {
        try {
            pCondicion = pCondicion == null ? " " : pCondicion;
            StringBuffer consulta = new StringBuffer(pConsultaVisibilidad.getSeleccion());
            consulta = consulta.append(" " + pConsultaVisibilidad.getTabla());
            consulta = consulta.append(" " + pConsultaVisibilidad.getCondicion());

            if (consulta.toString().indexOf(" WHERE ") != -1) {
                consulta.append(pCondicion);
            } else {

                if (pCondicion.startsWith(" ORDER BY ")) {
                    consulta.append(pCondicion);
                } else {
                    if (pCondicion.startsWith(" AND ")) {
                        consulta.append(" WHERE " + pCondicion.substring(4));
                    } else {
                        if (pCondicion.trim().length() > 0)
                            consulta.append(" WHERE " + pCondicion);
                    }
                }
            }

            if (pConsultaVisibilidad.getAgrupacion() != null &&
                pConsultaVisibilidad.getAgrupacion().trim().length() > 0) {
                consulta = consulta.append(pConsultaVisibilidad.getAgrupacion());
            }

            //this.consultaTablaEJB = this.getConsultaTablaEJB();
            //  ConsultaTablaLocal consultaTablaLocal = this.consultaTablaHome.create();
            return this.consultaTablaEJB.consultarTabla(0, 0, null, consulta.toString(), columnas);

        } catch (Exception error) {
            error.printStackTrace();
            throw new EJBException("FacadeEJB|ejecutarConsulta : " + error.getMessage());
        }
    }

    public Integer crearReporteInusual(Hashtable pDatos, Collection pSoporte, Collection pTxns) {
        System.out.println("===========FacadeEJBBean|crearReporteInusual=================");
        System.out.println("===> FacadeEJBBean|crearReporteInusual|pDatos: " + pDatos);
        System.out.println("===> FacadeEJBBean|crearReporteInusual|pSoporte: " + pSoporte);
        System.out.println("===> FacadeEJBBean|crearReporteInusual|pTxns: " + pTxns);
        try {
            //this.reporteEJB = this.getReporteEJB();

            Hashtable datosReporte = this.generarTipos(pDatos);

            if (pTxns.isEmpty()) {
                datosReporte.put("CODIGO_CLASE_REPORTE_V", "3");
            } else {
                datosReporte.put("CODIGO_CLASE_REPORTE_V", "2");
            }
            //datosReporte.put("CODIGO_TIPO_REPORTE_V","1");
            datosReporte.put("CODIGO_ESTADO_REPORTE_V", "1");
            //ReporeEJB reporteLocal = this.reporteHome.create();
            Hashtable idReporte = this.reporteEJB.crearReporte(datosReporte);
            Integer idRep = (Integer) idReporte.get("ID_REPORTE");
            datosReporte.put("ID_REPORTE", idRep);

            String pPerfil = (String) pDatos.get("PERFIL");
            String pCargo = (String) pDatos.get("CODIGO_CARGO");

            Hashtable pDatosHistorico = new Hashtable();
            /*HistoricoTransaccionLocalHome htHome = this.getHistoricoTransaccionLocalHome();*/
            //TransaccionEJB transaccionEJB = this.getTransaccionEJB();
            //   TransaccionLocal_temp transaccionLocal = transaccionLocalHome.create();

            Iterator iTxns = pTxns.iterator();
            //this.establecerConexion();
            while (iTxns.hasNext()) {
                pDatosHistorico = new Hashtable();
                Hashtable temporal = new Hashtable();
                Hashtable infTxn = new Hashtable();

                Object obj = iTxns.next();

                pDatosHistorico = (Hashtable) obj;

                temporal = this.copiarHash(pDatosHistorico);
                infTxn = this.copiarHash(pDatosHistorico);

                temporal.put("ID_REPORTE", idRep);
                pDatosHistorico.put("CODIGO_ESTADO", this.INUSUAL);
                pDatosHistorico.put("CODIGO_CARGO", pCargo);


                /* Asociar transaccion al reporte */
                this.reporteEJB.adicionarTxn(temporal);

                /* Actualizar estado transaccion */
                if (pPerfil.equals("2")) {
                    infTxn.put("ESTADO_OFICINA", this.INUSUAL);
                } else {
                    infTxn.put("ESTADO_DUCC", this.INUSUAL);
                }
                infTxn.remove("ID_TRANSACCION");
                //infTxn.put("sesionBD", this.sesionBD);
                transaccionEJB.actualizarCampo(infTxn);
                /*if ( sesionBD == null || sesionBD.isClosed()){
          this.establecerConexion();
        }*/
                /* Crear histórico de estados transaccion */
                Timestamp fechaP = (Timestamp) pDatosHistorico.get("FECHA_PROCESO");
                Date fechaProc = new Date(fechaP.getTime());
                pDatosHistorico.remove("FECHA_PROCESO");
                pDatosHistorico.put("FECHA_PROCESO", fechaProc);

                Timestamp fechaA = (Timestamp) pDatosHistorico.get("FECHA_ACTUALIZACION");
                Date fechaAct = new Date(fechaA.getTime());
                pDatosHistorico.remove("FECHA_ACTUALIZACION");
                pDatosHistorico.put("FECHA_ACTUALIZACION", fechaAct);

                this.insertarRegistroHistoricoTransaccion(pDatosHistorico);
            }
            System.out.println("===> FacadeEJBBean|crearReporteInusual|datosReporte: " + datosReporte);
            if (datosReporte.get("TIPO_IDENTIFICACION") != null) {
                this.reporteEJB.adicionarPersona(datosReporte);
            }
            Iterator iSoporte = pSoporte.iterator();
            //IMAif ( sesionBD == null || sesionBD.isClosed()){
            //this.establecerConexion();
            //IMA}
            while (iSoporte.hasNext()) {
                Hashtable temporal = new Hashtable();
                temporal = (Hashtable) iSoporte.next();
                temporal.put("ID_REPORTE", idRep);
                /*IMAif ( sesionBD == null || sesionBD.isClosed()){
          this.establecerConexion();
        }*/
                //temporal.put("sesionBD", this.sesionBD);
                this.reporteEJB.adicionarRespuesta(temporal);
            }

            return idRep;
        } catch (SQLException sqlError) {
            sqlError.printStackTrace(); //ob
            throw new EJBException("FacadeEJB|crearReporteInusualSQL: " + sqlError.getMessage());
        } catch (Exception error) {
            error.printStackTrace(); //ob
            throw new EJBException("FacadeEJB|crearReporteInusual: " + error.getMessage());
        } /*finally{
        try{
            this.cerrarConexion();
        } catch (Exception cerrar){
            throw new EJBException("FacadeEJB|CrearReporteInusual: Error al cerrar la conexión: " + cerrar.getMessage());
        }
    }*/
    }

    /*private ReporteEJB getReporteEJB() throws Exception {
        Contextos contex = new Contextos();
        ReporteEJB reporteEJB = (ReporteEJB) contex.getContext("ReporteEJB");
        return reporteEJB;
    } */

    private HistoricoTransaccionEJB getHistoricoTransaccionEJB() throws Exception {
        Contextos contex = new Contextos();
        HistoricoTransaccionEJB historicoTransaccionEJB =
            (HistoricoTransaccionEJB) contex.getContext("HistoricoTransaccionEJB");
        return historicoTransaccionEJB;
    }

    /* private TransaccionEJB getTransaccionEJB() throws Exception {
        Contextos contex = new Contextos();
        TransaccionEJB transaccionEJB = (TransaccionEJB) contex.getContext("TransaccionEJB");
        return transaccionEJB;
    } */

    private Hashtable copiarHash(Hashtable pTabla) {
        Hashtable copia = new Hashtable();
        Enumeration it = pTabla.keys();
        while (it.hasMoreElements()) {
            String llave = (String) it.nextElement();
            copia.put(llave, pTabla.get(llave));
        }
        return copia;
    }

    /*public Integer crearReporteNormal(Hashtable pDatos, Collection pSoporte, Collection pTxns) {
    try{
      this.reporteHome = this.getReporteHome();
      Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

      String pPerfil = (String) pDatos.get( "PERFIL" );
      String pCargo = (String) pDatos.get( "CODIGO_CARGO" );

      pDatos.put("CODIGO_CLASE_REPORTE_V","1");
      pDatos.put("CODIGO_TIPO_REPORTE_V","3");
      if ( pPerfil.equals("2") ){
        pDatos.put("CODIGO_ESTADO_REPORTE_V","5");
      }
      if ( pPerfil.equals("5") ){
          pDatos.put("CODIGO_ESTADO_REPORTE_V","4");
      }

      ReporteLocal reporteLocal = this.reporteHome.create();
      Hashtable idReporte = reporteLocal.crearReporte(pDatos);
      Integer idRep = (Integer)idReporte.get("ID_REPORTE");

      Hashtable pDatosHistorico = new Hashtable();

      TransaccionLocalHome transaccionLocalHome = this.getTransaccionLocalHome();
      TransaccionLocal transaccionLocal = transaccionLocalHome.create();

      Iterator iTxns = pTxns.iterator();
      this.establecerConexion();
      while ( iTxns.hasNext() ){
        pDatosHistorico = new Hashtable();
        Hashtable temporal = new Hashtable();
        Hashtable infTxn = new Hashtable();

        Object obj = iTxns.next();

        pDatosHistorico = (Hashtable) obj;

        temporal = this.copiarHash(pDatosHistorico);
        infTxn = this.copiarHash(pDatosHistorico);

        temporal.put("ID_REPORTE", idRep);
        temporal.put("sesionBD", this.sesionBD);
        pDatosHistorico.put("CODIGO_ESTADO", this.NORMAL);
        pDatosHistorico.put("CODIGO_CARGO", pCargo);


        reporteLocal.adicionarTxn(temporal);

        if ( sesionBD == null || sesionBD.isClosed()){
          this.establecerConexion();
        }


        if ( pPerfil.equals("2") ){
          infTxn.put("ESTADO_OFICINA", this.NORMAL);
        } else {
          infTxn.put("ESTADO_DUCC", this.NORMAL);
        }
        infTxn.remove("ID_TRANSACCION");
        infTxn.put("sesionBD", this.sesionBD);
        transaccionLocal.actualizarCampo(infTxn);
        if ( sesionBD == null || sesionBD.isClosed()){
          this.establecerConexion();
        }

        Timestamp fechaP = (Timestamp)pDatosHistorico.get("FECHA_PROCESO");
        Date fechaProc = new Date(fechaP.getTime());
        pDatosHistorico.remove("FECHA_PROCESO");
        pDatosHistorico.put("FECHA_PROCESO", fechaProc);

        Timestamp fechaA = (Timestamp)pDatosHistorico.get("FECHA_ACTUALIZACION");
        Date fechaAct = new Date(fechaA.getTime());
        pDatosHistorico.remove("FECHA_ACTUALIZACION");
        pDatosHistorico.put("FECHA_ACTUALIZACION", fechaAct);

        this.insertarRegistroHistoricoTransaccion(pDatosHistorico);
      }

      Iterator iSoporte = pSoporte.iterator();
      if ( sesionBD == null || sesionBD.isClosed()){
          this.establecerConexion();
      }
      while ( iSoporte.hasNext() ){
        Hashtable temporal = new Hashtable();
        temporal = (Hashtable) iSoporte.next();
        temporal.put("ID_REPORTE", idRep);
        if ( sesionBD == null || sesionBD.isClosed()){
          this.establecerConexion();
        }
        temporal.put("sesionBD", this.sesionBD);
        reporteLocal.adicionarRespuesta(temporal);
      }
      return idRep;
    } catch ( Exception ex){
      throw new EJBException( "FacadeEJB|crearReporteNormal: " + ex.getMessage());
    } finally{
        try{
            this.cerrarConexion();
        } catch (Exception cerrar){
            throw new EJBException("FacadeEJB|crearReporteNormal: Error al cerrar la conexión: " + cerrar.getMessage());
        }
    }

  }*/

    public Integer crearReporteNormal(Hashtable pDatos, Collection pSoporte, Collection pTxns) {
        try {
            //this.reporteEJB = this.getReporteEJB();
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

            String pPerfil = (String) pDatos.get("PERFIL");
            String pCargo = (String) pDatos.get("CODIGO_CARGO");

            pDatos.put("CODIGO_CLASE_REPORTE_V", "1");
            pDatos.put("CODIGO_TIPO_REPORTE_V", "3");
            if (pPerfil.equals("2")) {
                pDatos.put("CODIGO_ESTADO_REPORTE_V", "5");
            }
            if (pPerfil.equals("5")) {
                pDatos.put("CODIGO_ESTADO_REPORTE_V", "4");
            }

            //ReporteEJB reporteLocal = this.reporteHome.create();
            Hashtable idReporte = this.reporteEJB.crearReporte(pDatos);
            Integer idRep = (Integer) idReporte.get("ID_REPORTE");

            Hashtable pDatosHistorico = new Hashtable();
            /*HistoricoTransaccionLocalHome htHome = this.getHistoricoTransaccionLocalHome();*/
            //TransaccionEJB transaccionLocalHome = this.getTransaccionEJB();
            //  TransaccionLocal_temp transaccionLocal = transaccionLocalHome.create();

            Iterator iTxns = pTxns.iterator();
            //this.establecerConexion();
            while (iTxns.hasNext()) {
                Hashtable temporal = new Hashtable();

                Object obj = iTxns.next();

                temporal = (Hashtable) obj;

                temporal.put("ID_REPORTE", idRep);
                //temporal.put("sesionBD", this.sesionBD);

                /* Asociar transaccion al reporte */
                this.reporteEJB.adicionarTxn(temporal);


            }

            Iterator iSoporte = pSoporte.iterator();
            /*if ( sesionBD == null || sesionBD.isClosed()){
          this.establecerConexion();
      }*/
            while (iSoporte.hasNext()) {
                Hashtable temporal = new Hashtable();
                temporal = (Hashtable) iSoporte.next();
                temporal.put("ID_REPORTE", idRep);
                //System.out.println("en crear reporte normal, la sesion esta cerrada: " + sesionBD.isClosed());
                /*
        if ( sesionBD == null || sesionBD.isClosed()){
           //System.out.println("en crear reporte normal: Se establece conexion");
          this.establecerConexion();
        }
        temporal.put("sesionBD", this.sesionBD);
        */
                //System.out.println("en crear reporte normal: se invoca adicionar respuesta");
                this.reporteEJB.adicionarRespuesta(temporal);
            }
            /*
      if ( sesionBD == null || sesionBD.isClosed()){
        this.establecerConexion();
      }
      */
            /* Llamar procedimiento almacenado que se encarga del resto
       * de lógica, por
       * optimización de tiempo*/
            /*if (this.consultaTablaEJB == null) {
                this.consultaTablaEJB = this.getConsultaTablaEJB();
            } */

            //ConsultaTablaLocal invocarProceso = this.consultaTablaHome.create();

            int[] respuesta = { Types.VARCHAR };
            String parametroProceso = "(" + idRep + ",'" + pCargo + "',?)";

            String salida = consultaTablaEJB.ejecutarProcedure("PR_REPORTE_NORMAL", parametroProceso, respuesta);

            if (!salida.equals("[null]")) {
                throw new Exception(salida);
            }
            //System.out.println("tercero");
            return idRep;
        } catch (Exception ex) {
            throw new EJBException("FacadeEJB|crearReporteNormal: " + ex.getMessage());
        } /* finally{

        try{
            this.cerrarConexion();
        } catch (Exception cerrar){
            throw new EJBException("FacadeEJB|crearReporteNormal: Error al cerrar la conexión: " + cerrar.getMessage());
        }
    }
    */
    }

    public boolean puedeCrearFiltro(Hashtable pDatos, int pRol) {
        if (pRol != 2 && pRol != 5) {
            return false;
        }
        try {
            //ConsultaTablaEJB consultaLocal = this.getConsultaTablaEJB();
            //  ConsultaTablaLocal consultaLocal = consultaHome.create();
            /*Collection c = consultaLocal.consultarTabla(0,0,"V_CREAR_FILTRO", "WHERE NUMERO_NEGOCIO = '" + pCodigoProducto+ "'" +
                     " AND CODIGO_CLASE_REPORTE = 1");*/
            String miCondicion =
                "SELECT FECHA_PROCESO FROM TRANSACCIONES_CLIENTE " +
                //" WHERE NUMERO_NEGOCIO = '" + pDatos.get("NUMERO_NEGOCIO") + "'" +
                //" AND CODIGO_ARCHIVO = '" + pDatos.get("CODIGO_ARCHIVO") + "'";
                " WHERE TIPO_IDENTIFICACION = '" + pDatos.get("TIPO_IDENTIFICACION") + "'" +
" AND NUMERO_IDENTIFICACION = '" + pDatos.get("NUMERO_IDENTIFICACION") + "'";
            if (pRol == 2) {
                miCondicion += " AND ESTADO_OFICINA = 'N'";
            } else {
                miCondicion += " AND ESTADO_DUCC = 'N'";
            }
            miCondicion += " GROUP BY FECHA_PROCESO";

            Collection c = consultaTablaEJB.consultarTabla(0, 0, null, miCondicion);
            if (c.size() > 2) {
                //if (c.size() == 3 ){
                return true;
                /*} else {
            throw new Exception("Ya existen filtros para este cliente, desea redefinirlos?");
        } */
            } else {
                return false;
            }
        } catch (Exception error) {
            throw new EJBException("FacadeEJB|puedeCrearFiltro: " + error.getMessage());
        }
    }

    public Hashtable traerDatosReporte(String idReporte) {
        try {
            Integer id;
            try {
                id = new Integer(idReporte);
            } catch (Exception e) {
                throw new EJBException("FacadeEJB|traerDatosReporte: ID de Reporte no valido");
            }
            //ReporteEJB reporteHome = this.getReporteEJB();
            ReporteEJB reporteHome = this.reporteEJB;
            // ReporteEJB reporteLocal = reporteHome.create();

            return reporteHome.mostrarDatos(id);

        } catch (Exception exc) {
            throw new EJBException("FacadeEJB|traerDatosReporte: " + exc.getMessage());
        }
    }

    public void insertarFechaProceso(String pFecha) throws SQLException {
        try {
            this.establecerConexion();
            String insercion = "UPDATE CONTROL_ENTIDAD SET FECHA_PROCESO = TO_DATE('" + pFecha + "', 'YYYY/MM/DD)";
            Statement sentencia = this.sesionBD.createStatement();
            sentencia.executeUpdate(insercion);
            sentencia.close();
        } catch (Exception error) {
            throw new EJBException("FacadeEJB|insertarFechaProceso" + error.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void establecerConexion() throws Exception {
        this.cerrarConexion();

        DataSource ds = null;
        this.sesionBD = null;

        InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(CatalogoBD.urlDataSource);
        this.sesionBD = ds.getConnection();

    }

    public void cerrarConexion() throws SQLException {
        if (this.sesionBD != null && !this.sesionBD.isClosed()) {
            this.sesionBD.close();
            this.sesionBD = null;
        }
    }

    public void inicioDia(String pFecha, String pUsuario) throws SQLException {
        try {
            this.establecerConexion();
            if (pFecha == null) {
                throw new EJBException("La fecha del día no puede ser nula");
            }
            String consultaID = "SELECT MAX(ID_PROCESO) + 1 ID FROM LOG_PROCESOS ";
            Statement st = this.sesionBD.createStatement();
            ResultSet rs = st.executeQuery(consultaID);
            String idActual = null;
            while (rs.next()) {
                idActual = rs.getString("ID");
            }

            rs.close();
            st.close();

            String pActualizarFecha =
                "UPDATE CONTROL_ENTIDAD " + " SET FECHA_PROCESO = TO_DATE('" + pFecha + "', 'YYYY/MM/DD')";
            String pActualizarNuevas = "UPDATE TRANSACCIONES_CLIENTE " + "SET NUEVA = 0 " + "WHERE NUEVA = 1 ";
            String pInsertarRegistros =
                "INSERT INTO LOG_PROCESOS(ID_PROCESO, CODIGO_PROCESO, FECHA_HORA_INICIO, USUARIO)" +
                " VALUES(SEQ_PROCESO.NEXTVAL, '5', SYSDATE, " + pUsuario + " )";
            //sesionBD.setAutoCommit(false);
            try {
                Statement sRegistro = this.sesionBD.createStatement();
                boolean resInsert = sRegistro.execute(pInsertarRegistros);

                Statement sNueva = this.sesionBD.createStatement();
                int registrosProcesados = sNueva.executeUpdate(pActualizarNuevas);

                Statement sFecha = this.sesionBD.createStatement();
                int sFechaProcesados = sFecha.executeUpdate(pActualizarFecha);

                String borrarTablaCargue = "DELETE CARGUE_TOTAL";
                Statement stBorrarCargueTotal = this.sesionBD.createStatement();
                int resDelet = stBorrarCargueTotal.executeUpdate(borrarTablaCargue);

                String actualizarLog =
                    "UPDATE LOG_PROCESOS " + " SET FECHA_HORA_FIN = SYSDATE, " + " REGISTROS_PROCESADOS = " +
                    registrosProcesados + " WHERE ID_PROCESO = " + idActual;
                Statement stActualizarLog = this.sesionBD.createStatement();
                int resUpdateLogProcesos = stActualizarLog.executeUpdate(actualizarLog);

                sRegistro.close();
                sNueva.close();
                sFecha.close();
                stBorrarCargueTotal.close();
                stActualizarLog.close();
            } catch (Exception e) {
                throw new EJBException("FacadeEJB|inicioDia : " + e.getMessage());
            } finally {
                this.cerrarConexion();
            }
        } catch (Exception error) {
            throw new EJBException("FacadeEJB|inicioDiaException: " + error.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public Integer crearReporteAnalista(Hashtable pDatos, Collection pSoporte, Collection pTxns) {
        try {
            Hashtable datosReporte = this.generarTipos(pDatos);
            if (pTxns.isEmpty()) {
                throw new EJBException("No se puede crear el reporte, no hay transacciones relacionadas");
            }
            Hashtable idReporte = this.reporteEJB.crearReporte(datosReporte);
            Integer idRep = (Integer) idReporte.get("ID_REPORTE");
            datosReporte.put("ID_REPORTE", idRep);
            Iterator iTxns = pTxns.iterator();
            while (iTxns.hasNext()) {
                Hashtable temporal = new Hashtable();
                Hashtable infTxn = new Hashtable();
                Object obj = iTxns.next();
                temporal = (Hashtable) obj;
                infTxn = this.copiarHash(temporal);
                temporal.put("ID_REPORTE", idRep);
                /* Asociar transaccion al reporte */
                this.reporteEJB.adicionarTxn(temporal);
            }
            Iterator iSoporte = pSoporte.iterator();
            while (iSoporte.hasNext()) {
                Hashtable temporal = new Hashtable();
                temporal = (Hashtable) iSoporte.next();
                temporal.put("ID_REPORTE", idRep);
                this.reporteEJB.adicionarRespuesta(temporal);
            }
            return idRep;
        } catch (SQLException sqlError) {
            throw new EJBException("FacadeEJB|crearReporteAnalista:sqlError " + sqlError.getMessage());
        } catch (Exception error) {
            throw new EJBException("FacadeEJB|crearReporteAnalista:error " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception cerrar) {
                throw new EJBException("FacadeEJB|crearReporteAnalista: Error al cerrar la conexión: " +
                                       cerrar.getMessage());
            }
        }
    }

    public boolean actualizarGestion(Hashtable pDatos, Collection pSoporte, Collection pTxns) {
        boolean exito = false;
        try {
            Integer idReporte = (Integer) pDatos.get("ID");
            this.reporteEJB.limpiarGestion(idReporte);
            Hashtable datosReporte = this.generarTipos(pDatos);
            if (pTxns.isEmpty()) {
                throw new EJBException("No se puede crear el reporte, no hay transacciones relacionadas");
            }
            this.reporteEJB.actualizarReporte(datosReporte);
            Iterator iTxns = pTxns.iterator();
            while (iTxns.hasNext()) {
                Hashtable temporal = new Hashtable();
                Hashtable infTxn = new Hashtable();
                Object obj = iTxns.next();
                temporal = (Hashtable) obj;
                infTxn = this.copiarHash(temporal);
                temporal.put("ID_REPORTE", idReporte);
                this.reporteEJB.adicionarTxn(temporal);
            }
            Iterator iSoporte = pSoporte.iterator();
            while (iSoporte.hasNext()) {
                Hashtable temporal = new Hashtable();
                temporal = (Hashtable) iSoporte.next();
                temporal.put("ID_REPORTE", idReporte);
                this.reporteEJB.adicionarRespuesta(temporal);
            }
            exito = true;
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|actualizarGestion: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception cerrar) {
                throw new EJBException("FacadeEJB | actualizarGestion: Error al cerrar la conexión: " +
                                       cerrar.getMessage());
            }
        }
        return exito;
    }

    public boolean reportarAnalista(Hashtable pDatos, Collection pSoporte) throws SQLException {
        System.out.println("==> FacedeEJBBean|reportarAnalista...");
        boolean exito = false;
        try {
            Integer idReporte = (Integer) pDatos.get("ID");
            //this.reporteEJB = this.getReporteEJB();
            // ReporteEJB reporteLocal = this.reporteHome.create();
            Hashtable datosReporte = this.generarTipos(pDatos);

            this.reporteEJB.actualizarReporte(datosReporte);

            String pCargo = (String) pDatos.get("CODIGO_CARGO");
            Collection pTxns = this.reporteEJB.mostrarTxn(idReporte);

            Hashtable pDatosHistorico = new Hashtable();
            /*HistoricoTransaccionLocalHome htHome = this.getHistoricoTransaccionLocalHome();*/
            //TransaccionEJB transaccionLocalHome = this.getTransaccionEJB();
            // TransaccionLocal_temp transaccionLocal = transaccionLocalHome.create();

            this.establecerConexion();

            Iterator iTxns = pTxns.iterator();
            while (iTxns.hasNext()) {

                pDatosHistorico = new Hashtable();
                Hashtable infTxn = new Hashtable();

                Object obj = iTxns.next();

                pDatosHistorico = (Hashtable) obj;

                infTxn = this.copiarHash(pDatosHistorico);


                pDatosHistorico.put("CODIGO_ESTADO", this.INUSUAL);
                pDatosHistorico.put("CODIGO_CARGO", pCargo);

                /* Actualizar estado transaccion */
                infTxn.put("ESTADO_DUCC", this.INUSUAL);
                infTxn.put("ID", new Integer((String) infTxn.get("ID_TRANSACCION")));
                infTxn.put("CODIGO_ARCHIVO", Integer.valueOf((String) infTxn.get("CODIGO_ARCHIVO")));
                //System.out.println("hash: " +infTxn);
                infTxn.remove("ID_TRANSACCION");
                //infTxn.put("sesionBD", this.sesionBD);

                transaccionEJB.actualizarCampo(infTxn);

                /* Crear histórico de estados transaccion */
                Timestamp fechaP = (Timestamp) pDatosHistorico.get("FECHA_PROCESO");
                Date fechaProc = new Date(fechaP.getTime());
                pDatosHistorico.remove("FECHA_PROCESO");
                pDatosHistorico.put("FECHA_PROCESO", fechaProc);

                Timestamp fechaA = new Timestamp(System.currentTimeMillis());
                Date fechaAct = new Date(fechaA.getTime());
                pDatosHistorico.remove("FECHA_ACTUALIZACION");
                pDatosHistorico.put("FECHA_ACTUALIZACION", fechaAct);
                pDatosHistorico.put("ID_TRANSACCION", (Integer) infTxn.get("ID"));
                pDatosHistorico.put("CODIGO_ARCHIVO", (Integer) infTxn.get("CODIGO_ARCHIVO"));

                this.insertarRegistroHistoricoTransaccion(pDatosHistorico);
            }
            Iterator iSoporte = pSoporte.iterator();
            while (iSoporte.hasNext()) {
                Hashtable temporal = new Hashtable();
                temporal = (Hashtable) iSoporte.next();
                temporal.put("ID_REPORTE", idReporte);
                //temporal.put("sesionBD", this.sesionBD);
                this.reporteEJB.adicionarRespuesta(temporal);
            }
            if (datosReporte.get("TIPO_IDENTIFICACION") != null) {
                this.reporteEJB.adicionarPersona(datosReporte);
            }
            exito = true;
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|ReportarAnalista" + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception cerrar) {
                throw new EJBException("FacadeEJB | ReportarAnalista: Error al cerrar la conexión: " +
                                       cerrar.getMessage());
            }
        }
        return exito;
    }

    public boolean noReportarAnalista(Hashtable pDatos) throws SQLException {
        boolean exito = false;
        try {
            Integer idReporte = (Integer) pDatos.get("ID");
            //this.reporteEJB = this.getReporteEJB();
            // ReporteEJB reporteLocal = this.reporteHome.create();
            this.reporteEJB.actualizarReporte(pDatos);

            String pCargo = (String) pDatos.get("CODIGO_CARGO");

            /*Collection pTxns = reporteLocal.mostrarTxn(idReporte);*/


            /*HistoricoTransaccionLocalHome htHome = this.getHistoricoTransaccionLocalHome();*/
            /*TransaccionLocalHome transaccionLocalHome = this.getTransaccionLocalHome();
            TransaccionLocal transaccionLocal = transaccionLocalHome.create();*/
            this.establecerConexion();
            /*** aqui se cambia por el procedimiento*/
            /*            Iterator iTxns = pTxns.iterator();
            while ( iTxns.hasNext() ){

                pDatosHistorico = new Hashtable();
                Hashtable infTxn = new Hashtable();

                Object obj = iTxns.next();

                pDatosHistorico = (Hashtable) obj;

                infTxn = this.copiarHash(pDatosHistorico);


                pDatosHistorico.put("CODIGO_ESTADO", this.NORMAL);
                pDatosHistorico.put("CODIGO_CARGO", pCargo);

                /* Actualizar estado transaccion */
            /*                infTxn.put("ESTADO_DUCC", this.NORMAL);
                infTxn.put("ID", new Integer((String)infTxn.get("ID_TRANSACCION")));
                infTxn.put("CODIGO_ARCHIVO",  Integer.valueOf((String)infTxn.get("CODIGO_ARCHIVO")));
                //System.out.println("hash: " +infTxn);
                infTxn.remove("ID_TRANSACCION");
                infTxn.put("sesionBD", this.sesionBD);

                transaccionLocal.actualizarCampo(infTxn);

                /* Crear histórico de estados transaccion */
            /*                Timestamp fechaP = (Timestamp)pDatosHistorico.get("FECHA_PROCESO");
                Date fechaProc = new Date(fechaP.getTime());
                pDatosHistorico.remove("FECHA_PROCESO");
                pDatosHistorico.put("FECHA_PROCESO", fechaProc);

                Timestamp fechaA = new Timestamp(System.currentTimeMillis());
                Date fechaAct = new Date(fechaA.getTime());
                pDatosHistorico.remove("FECHA_ACTUALIZACION");
                pDatosHistorico.put("FECHA_ACTUALIZACION", fechaAct);
                pDatosHistorico.put("ID_TRANSACCION", (Integer)infTxn.get("ID"));
                pDatosHistorico.put("CODIGO_ARCHIVO",  (Integer)infTxn.get("CODIGO_ARCHIVO"));
                this.insertarRegistroHistoricoTransaccion(pDatosHistorico);
            }*/
            //this.sesionBD.commit();
            /*  if (this.consultaTablaEJB == null) {
                this.consultaTablaEJB = this.getConsultaTablaEJB();
            } */

            //ConsultaTablaLocal invocarProceso = this.consultaTablaHome.create();

            int[] respuesta = { Types.VARCHAR };
            String parametroProceso = "(" + idReporte + ",'" + pCargo + "',?)";

            String salida = this.consultaTablaEJB.ejecutarProcedure("PR_REPORTE_NORMAL", parametroProceso, respuesta);
            //this.sesionBD.commit();

            if (!salida.equals("[null]")) {
                throw new Exception(salida);
            }
            exito = true;
            /* Ingrid Marcela Alonso 2007-02-23
             * Llamado a actualizar el estado de los reportes asociados a
             * gestionado */
            try {
                //this.reporteEJB = this.getReporteEJB();
                //  ReporteEJB reporteLocalAct = this.reporteHome.create();

                this.reporteEJB.actualizarReportesAsociados(idReporte);

            } catch (Exception exc) {
                throw new EJBException("Facade|No reportar: " + exc.getMessage());
            }
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|NoReportarAnalista" + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception cerrar) {
                throw new EJBException("FacadeEJB | NoReportarAnalista: No se cerró la conexion" + cerrar.getMessage());
            }
        }
        return exito;
    }

    public boolean asignarRegionAnalista(String[] pCodigoRegion, String pCargoAnalista) throws SQLException {
        boolean retorno = false;
        try {
            this.establecerConexion();
            Statement sentencia = this.sesionBD.createStatement();
            String borrado = "DELETE ANALISTA_REGION WHERE CODIGO_CARGO = '" + pCargoAnalista + "'";
            sentencia.execute(borrado);
            for (int i = 0; i < pCodigoRegion.length; i++) {
                if (pCodigoRegion[i] == null)
                    continue;
                String insercion =
                    "INSERT INTO ANALISTA_REGION(CODIGO_CARGO, CODIGO_REGION_V) " + " VALUES ( '" + pCargoAnalista +
                    "', '" + pCodigoRegion[i] + "')";
                sentencia.execute(insercion);
            }
            retorno = true;
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|asignarRegionAnalista:" + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
        return retorno;
    }

    public boolean actualizarReporte(Hashtable pDatos, Collection pSoporte) {
        boolean exito = false;
        try {
            Integer idReporte = (Integer) pDatos.get("ID");
            Hashtable datosReporte = this.generarTipos(pDatos);
            this.reporteEJB.actualizarReporte(datosReporte);
            String pCargo = (String) pDatos.get("CODIGO_CARGO");
            Collection pTxns = this.reporteEJB.mostrarTxn(idReporte);
            this.establecerConexion();
            try {
                Statement st = this.sesionBD.createStatement();
                String eliminarRespuestasB = "DELETE RTAS_BOOLEAN_REP " + "WHERE ID_REPORTE = " + idReporte;
                st.execute(eliminarRespuestasB);
                String eliminarRespuestasT = "DELETE RTAS_TEXT_REP " + "WHERE ID_REPORTE = " + idReporte;
                st.execute(eliminarRespuestasT);
                st.close();
            } catch (Exception e) {
                throw new EJBException("FacadeEJB|actualizarReporte(desasociacion) " + e.getMessage());
            }
            Iterator iSoporte = pSoporte.iterator();
            while (iSoporte.hasNext()) {
                Hashtable temporal = new Hashtable();
                temporal = (Hashtable) iSoporte.next();
                temporal.put("ID_REPORTE", idReporte);
                /* temporal.put("sesionBD", this.sesionBD); */
                this.reporteEJB.adicionarRespuesta(temporal);
            }
            if (datosReporte.get("TIPO_IDENTIFICACION") != null) {
                this.reporteEJB.adicionarPersona(datosReporte);
            }
            exito = true;
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|ActualizarReporte" + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception enCerrar) {
                System.out.print("No se cerro la conexion: FACADEEJB|ActualizarReporte" + enCerrar.getMessage());
            }
        }
        return exito;
    }

    public void insertarRegistroHistoricoTransaccion(Hashtable pDatos) throws SQLException {
        try {
            this.establecerConexion();
            this.tabla = new TablaBD("HISTORICO_ESTADOS_TR", "ID", this.sesionBD);
            tabla.asignarValorColumna("ID", tabla.traerIDSecuencia("SEQ_HISTORICO_ESTADO"));
            tabla.asignarValorColumna("CODIGO_ARCHIVO", (Integer) pDatos.get("CODIGO_ARCHIVO"));
            tabla.asignarValorColumna("CODIGO_CARGO", (String) pDatos.get("CODIGO_CARGO"));
            tabla.asignarValorColumna("CODIGO_ESTADO_V", (String) pDatos.get("CODIGO_ESTADO"));
            tabla.asignarValorColumna("FECHA_ACTUALIZACION",
                                      new Timestamp(((Date) pDatos.get("FECHA_ACTUALIZACION")).getTime()));
            tabla.asignarValorColumna("FECHA_PROCESO", new Timestamp(((Date) pDatos.get("FECHA_PROCESO")).getTime()));
            tabla.asignarValorColumna("ID_TRANSACCION", (Integer) pDatos.get("ID_TRANSACCION"));
            tabla.asignarValorColumna("USUARIO_ACTUALIZACION", (Long) pDatos.get("USUARIO_ACTUALIZACION"));
            tabla.insertarDatos();
        } catch (Exception errorInsertando) {
            throw new EJBException("FacadeEJB|insertarRegistroHistoricoTransaccion: Error al insertar en el historico: " +
                                   errorInsertando.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void desactivarFiltros(Hashtable pDatos) throws SQLException {
        try {
            String codigoProducto = (String) pDatos.get("CODIGO_PRODUCTO");
            String numeroNegocio = (String) pDatos.get("NUMERO_NEGOCIO");
            String codigoCargo = (String) pDatos.get("CODIGO_CARGO");

            String consultaFiltros =
                "SELECT ID FROM FILTROS " + " WHERE CODIGO_PRODUCTO = '" + codigoProducto + "' " +
                " AND NUMERO_NEGOCIO = '" + numeroNegocio + "' " + " AND CODIGO_CARGO = '" + codigoCargo + "' " +
                " AND VIGENTE_HASTA IS NULL OR TRUNC(VIGENTE_HASTA) > TRUNC(SYSDATE)";

            this.establecerConexion();
            Statement st = this.sesionBD.createStatement();
            ResultSet rs = st.executeQuery(consultaFiltros);
            while (rs.next()) {
                String idFiltro = rs.getString("ID");
                Filtro filtro = filtroEJB.findByPrimaryKey(new Integer(idFiltro)); //OB
                filtro.setVigenteHasta(new java.sql.Date(System.currentTimeMillis())); //OB
                if (filtro.getConfirmar() != null && filtro.getConfirmar().intValue() == 1) { //OB
                    //if ( filtroLocal.getConfirmar().intValue() ==1 ){
                    filtro.setConfirmar(new Integer(0)); //OB
                    filtro.setFechaConfirmacion(new java.sql.Date(System.currentTimeMillis())); //OB
                    filtro.setUsuarioConfirmacion((Long) pDatos.get("USUARIO_CREACION")); //OB


                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|desactivarFiltros: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    private Hashtable generarTipos(Hashtable pDatos) {
        System.out.println("===========FacadeEJBBean|generarTipos=================");
        Hashtable datosReporte = new Hashtable();
        try {
            Enumeration elementos = pDatos.keys();
            while (elementos.hasMoreElements()) {
                String llave = (String) elementos.nextElement();
                String llaveFinal = "";
                String valorTemporal = "";
                try {
                    valorTemporal = (String) pDatos.get(llave);
                } catch (Exception error) {
                    datosReporte.put(llave, pDatos.get(llave));
                    continue;
                }
                try {
                    if (llave.startsWith("F_")) {
                        try {
                            llaveFinal = llave.substring(2);
                            datosReporte.put(llaveFinal,
                                             Timestamp.valueOf(valorTemporal.replace('/', '-') + " 00:00:0.0"));
                        } catch (Exception er) {
                            throw new EJBException("FacadeEJB|craerReporteInusual: formato de fecha invalido " +
                                                   er.getMessage() + llave);
                        }
                    }
                    if (llave.startsWith("I_")) {
                        try {
                            llaveFinal = llave.substring(2);
                            datosReporte.put(llaveFinal, Integer.valueOf(valorTemporal));
                        } catch (Exception er) {
                            throw new EJBException("FacadeEJB|craerReporteInusual: formato de numero invalido " +
                                                   er.getMessage() + llave);
                        }
                    }
                    if (llave.startsWith("B_")) {
                        try {
                            llaveFinal = llave.substring(2);
                            datosReporte.put(llaveFinal, Boolean.valueOf(valorTemporal));
                        } catch (Exception er) {
                            throw new EJBException("FacadeEJB|craerReporteInusual: formato de booleano invalido " +
                                                   er.getMessage() + llave);
                        }
                    }
                    if (llave.startsWith("G_")) {
                        try {
                            llaveFinal = llave.substring(2);
                            datosReporte.put(llaveFinal, new BigDecimal(valorTemporal));
                        } catch (Exception er) {
                            throw new EJBException("FacadeEJB|craerReporteInusual: formato de BigDecimal invalido " +
                                                   er.getMessage() + llave);
                        }
                    }
                    if (llave.startsWith("N_")) {
                        try {
                            llaveFinal = llave.substring(2);
                            datosReporte.put(llaveFinal, new BigInteger(valorTemporal));
                        } catch (Exception er) {
                            throw new EJBException("FacadeEJB|crearReporteInusual: formato de BigInteger invalido " +
                                                   er.getMessage() + llave);
                        }
                    }
                } catch (Exception u) {
                    System.out.println("Catch de conversion de tipos: " + u.getMessage() + "--" + llave + "--" +
                                       valorTemporal + "--" + llaveFinal);
                }
                if (llaveFinal.equals("")) {
                    datosReporte.put(llave, valorTemporal.toUpperCase());
                }
            }
        } catch (Exception general) {
            System.out.println("Exception en conversion de tipos de datos: " + general.getMessage());
        }
        return datosReporte;
    }

    public Vector mostrarRegionesAnalista(String pCodigoCargoAnalista) throws SQLException {
        Vector regiones = new Vector(0);
        try {
            this.establecerConexion();
            String sConsulta =
                "SELECT CODIGO_REGION_V CODIGO_REGION " + " FROM ANALISTA_REGION " + " WHERE CODIGO_CARGO = ?";
            PreparedStatement consulta = this.sesionBD.prepareStatement(sConsulta);
            consulta.setString(1, pCodigoCargoAnalista);
            ResultSet resultados = consulta.executeQuery();
            while (resultados.next()) {
                regiones.add(resultados.getString("CODIGO_REGION"));
            }
            resultados.close();
            consulta.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|mostrarRegionesAnalista: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
        return regiones;
    }

    /**
     * Este metodo elabora mejor las consultas, se utiliza en la consulta de cliente..
     */
    public Collection ejecutarConsultaImproved(String pCondicion, Consulta pConsultaVisibilidad) {
        try {
            pCondicion = pCondicion == null ? " " : pCondicion;
            String pCondicionWhere = "";
            String pCondicionOrder = "";
            int indiceWhere = pCondicion.indexOf(" WHERE ");
            if (pCondicion.startsWith(" AND ")) {
                indiceWhere = 0;
            }
            int indiceOrder = pCondicion.indexOf(" ORDER BY ");
            if (indiceWhere != -1) {
                pCondicionWhere = " AND " + pCondicion.substring(6);
                if (indiceOrder != -1) {
                    pCondicionWhere = pCondicionWhere.substring(0, indiceOrder);
                }
            }
            if (indiceOrder != -1) {
                pCondicionOrder = pCondicion.substring(indiceOrder);
            }
            StringBuffer consulta = new StringBuffer(pConsultaVisibilidad.getSeleccion());
            consulta = consulta.append(" " + pConsultaVisibilidad.getTabla());
            consulta = consulta.append(" " + pConsultaVisibilidad.getCondicion());

            if (consulta.toString().indexOf(" WHERE ") != -1) {
                consulta = consulta.append(" " + pCondicionWhere);
            } else {
                consulta = consulta.append(" WHERE " + pCondicionWhere.substring(5));
            }
            if (pConsultaVisibilidad.getAgrupacion() != null &&
                pConsultaVisibilidad.getAgrupacion().trim().length() > 0) {
                consulta = consulta.append(pConsultaVisibilidad.getAgrupacion());
            }
            consulta = consulta.append(pCondicionOrder);
            return this.consultaTablaEJB.consultarTabla(0, 0, null, consulta.toString());
        } catch (Exception error) {
            throw new EJBException("Facade|ejecutarConsultaImproved : " + error.getMessage());
        }
    }

    public void marcarFiltrosCambioUsuario(String pCargo) {
        try {
            this.establecerConexion();
            String sUpdate =
                "UPDATE FILTROS " + " SET CONFIRMAR = 1 " + " WHERE CODIGO_CARGO = ? " +
                " AND TRUNC(SYSDATE) BETWEEN TRUNC(VIGENTE_DESDE) AND " +
                " TRUNC(NVL(VIGENTE_HASTA,TO_DATE('2100/12/31','YYYY/MM/DD')))";
            PreparedStatement actualizacion = this.sesionBD.prepareStatement(sUpdate);
            actualizacion.setString(1, pCargo);
            int totalFiltros = actualizacion.executeUpdate();
            actualizacion.close();
        } catch (SQLException e) {
            throw new EJBException("FacadeEJB|marcarFiltrosCambioUsuario: SQL " + e.getMessage());
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|marcarFiltrosCambioUsuario: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception alCerrarConexion) {
                System.out.print("Excepcion al cerrar conexion marcarFiltrosCambioUsuario: ");
                alCerrarConexion.printStackTrace();
            }
        }
    }

    public void confirmarFiltro(Hashtable pDatos) {
   
        Integer pIdFiltro = (Integer) pDatos.get("ID");
        Long pUsuarioConfirmacion = (Long) pDatos.get("USUARIO_CONFIRMACION");
        //Date pFechaConfirmacion = new java.util.Date();
 
        try {
            //Actualizamos los datos de confirmación del filtro.
        
        	
            Filtro filtro = filtroEJB.findByPrimaryKey(pIdFiltro);
            filtro.setFechaConfirmacion(new java.util.Date());
            filtro.setConfirmar(new Integer(0));
            filtro.setUsuarioConfirmacion(pUsuarioConfirmacion);
            filtroEJB.actualizarFiltro2(filtro);
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|confirmarFiltro: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception alCerrarConexion) {
                System.out.print("Excepcion al cerrar conexion confirmarFiltro: ");
                alCerrarConexion.printStackTrace();
            }
        }
    }

    public boolean isConfirmarFiltro(String pCargo) {
        boolean confirmarFiltro = true;
        try {
            this.establecerConexion();
            String consultaConfirmarFiltro =
                "SELECT /*+ first_rows */ " + " ID FROM FILTROS " + " WHERE CODIGO_CARGO = ? " + " AND CONFIRMAR = 1" +
                " AND ESTADO_FILTRO = 1" + " AND ROWNUM < 2 ";
            PreparedStatement consulta = this.sesionBD.prepareStatement(consultaConfirmarFiltro);
            consulta.setString(1, pCargo);
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next()) {
                confirmarFiltro = true;
            } else {
                confirmarFiltro = false;
            }
            resultados.close();
            consulta.close();
        } catch (Exception ex) {
            throw new EJBException("FacadeEJB|isConfirmarFiltro" + ex.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception alCerrarConexion) {
                System.out.print("Excepcion al cerrar conexion isConfirmarFiltro: ");
                alCerrarConexion.printStackTrace();
            }
        }
        return confirmarFiltro;
    }

    public void revisarComentario(Hashtable pDatos) throws Exception {
        Integer pIdComentario = (Integer) pDatos.get("ID_COMENTARIO");
        String pCodigoCargo = (String) pDatos.get("CODIGO_CARGO");
        Timestamp pFechaRevision = (Timestamp) pDatos.get("FECHA_REVISION");
        Long pUsuarioRevision = (Long) pDatos.get("USUARIO_REVISION");
        ComentarioCargoEJBPK pk = new ComentarioCargoEJBPK(pIdComentario, pCodigoCargo);
        ComentarioCargoEJB comentarioCargoHome = this.comentarioCargoEJB;
        ComentarioCargo comCargo = comentarioCargoHome.findByPrimaryKey(pk);
        comCargo.setFechaRevision(pFechaRevision);
        comCargo.setUsuarioRevision(pUsuarioRevision);
    }

    public void crearComentario(Hashtable pDatos) throws Exception {
        // Crear comentario
        ComentarioEJB comentarioHome = this.comentarioEJB;
        Integer idComentario = comentarioHome.create(pDatos);
        Hashtable datos = new Hashtable();
        ComentarioCargoEJB comentarioCargoHome = this.comentarioCargoEJB;

        // OJO DESDE LA PAGINA DE CREACION SE DEBE ENVIAR ESTE CODIGO
        String pCodigoCargo = (String) pDatos.get("CODIGO_CARGO");

        // Traer personas a quienes se debe asociar el comentario
        String[] cargosEnviarComentario = this.traerCargosEnviarComentario(pCodigoCargo);
        // Asociar persona por persona el comentario
        for (int i = 0; i < cargosEnviarComentario.length; i++) {
            datos.clear();
            datos.put("ID_COMENTARIO", idComentario);
            datos.put("CODIGO_CARGO", cargosEnviarComentario[i]);
            comentarioCargoHome.create(datos); //PENDIENTE VERIFICAR
        }
    }

    public String[] traerCargosEnviarComentario(String pCodigoCargo) {
        return null;
    }

    public void insertarRegistroConsultaDUCC(Hashtable pDatos) throws SQLException {
        String tipoIdentificacion;
        String numeroIdentificacion;
        tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
        numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");

        try {
            this.establecerConexion();
            String insercion = "INSERT INTO CONSULTA_DUCC(TIPO_IDENTIFICACION," + "NUMERO_IDENTIFICACION) VALUES (?,?)";
            PreparedStatement sentencia = this.sesionBD.prepareStatement(insercion);
            sentencia.setString(1, tipoIdentificacion);
            sentencia.setString(2, numeroIdentificacion);
            sentencia.execute();
            sentencia.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|insertarRegConDUCC: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void eliminarRegistroConsultaDUCC(Hashtable pDatos) throws SQLException {
        String tipoIdentificacion;
        String numeroIdentificacion;
        tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
        numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");

        try {
            this.establecerConexion();
            String borrado =
                "DELETE CONSULTA_DUCC " + "WHERE TIPO_IDENTIFICACION = ? " + "AND NUMERO_IDENTIFICACION = ? ";
            PreparedStatement sentencia = this.sesionBD.prepareStatement(borrado);
            sentencia.setString(1, tipoIdentificacion);
            sentencia.setString(2, numeroIdentificacion);
            sentencia.execute();
            sentencia.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|eliminarRegConDUCC: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public String traerEquivalencia(Hashtable pDatos) throws SQLException {
        String tipoEquivalencia = (String) pDatos.get("TIPO_EQUIVALENCIA");
        String codigoBD = (String) pDatos.get("CODIGO_BD");
        String equivalencia = "";
        try {
            this.establecerConexion();
            String consultaEquiv =
                " SELECT EQUIVALENCIA " + " FROM EQUIVALENCIAS " + " WHERE TIPO_EQUIVALENCIA = ? " +
                " AND CODIGO_BD = ? ";
            PreparedStatement sentencia = this.sesionBD.prepareStatement(consultaEquiv);
            sentencia.setString(1, tipoEquivalencia);
            sentencia.setString(2, codigoBD);

            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                equivalencia = equivalencia + (String) resultado.getString(1);
            }
            return equivalencia;
        } catch (Exception exc) {
            equivalencia = " ";
            throw new EJBException("Facade:traerEquivalencia: " + exc.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public Vector mostrarPerfilesCriterio(String pCodigoCriterio) throws SQLException {
        Vector perfiles = new Vector(0);
        try {
            this.establecerConexion();
            String sConsulta =
                "SELECT CODIGO_PERFIL " + " FROM CRITERIOS_PERFIL " + " WHERE ID_CRITERIO_INUSUALIDAD = ?";
            PreparedStatement consulta = this.sesionBD.prepareStatement(sConsulta);
            consulta.setString(1, pCodigoCriterio);
            ResultSet resultados = consulta.executeQuery();
            while (resultados.next()) {
                perfiles.add(resultados.getString("CODIGO_PERFIL"));
            }
            resultados.close();
            consulta.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|mostrarPerfilesCriterio: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
        return perfiles;
    }

    public boolean asignarPerfilCriterio(List<String> pCodigoPerfil, String pIdCriterio) throws SQLException {
        boolean retorno = false;
        System.out.println("pCodigoPerfil: " + pCodigoPerfil + " | pIdCriterio: " + pIdCriterio);
        try {
            this.establecerConexion();
            Statement sentencia = this.sesionBD.createStatement();
            String borrado = "DELETE CRITERIOS_PERFIL WHERE ID_CRITERIO_INUSUALIDAD = '" + pIdCriterio + "'";
            System.out.println("borrado: " + borrado);
            boolean resTemp = sentencia.execute(borrado);
            System.out.println("resTemp: " + resTemp);
            for (int i = 0; i < pCodigoPerfil.size(); i++) {
                if (pCodigoPerfil.get(i) == null)
                    continue;
                String insercion =
                    "INSERT INTO CRITERIOS_PERFIL(ID_CRITERIO_INUSUALIDAD, CODIGO_PERFIL) " + " VALUES ( '" +
                    pIdCriterio + "', '" + pCodigoPerfil.get(i) + "')";
                System.out.println("insercion: " + insercion);
                sentencia.execute(insercion);
            }
            retorno = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException("FacadeEJB|asignarPerfilCriterio:" + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
        return retorno;
    }

    /*********************************************************************************
     * Metodo para inactivar el filtro de un producto dado su ID.
     * El estado del filtro debe ser pendiente o aprobado
     *********************************************************************************/
    public void inactivaFiltroId(Hashtable pDatos, String idF) throws SQLException {
        try {
            String codigoProducto = (String) pDatos.get("CODIGO_PRODUCTO");
            String numeroNegocio = (String) pDatos.get("NUMERO_NEGOCIO");
            String codigoCargo = (String) pDatos.get("CODIGO_CARGO");

            String consultaFiltros =
                "SELECT ID,ESTADO_FILTRO FROM FILTROS " + " WHERE ID = '" + idF + "'" + " AND CODIGO_PRODUCTO = '" +
                codigoProducto + "' " + " AND NUMERO_NEGOCIO = '" + numeroNegocio + "' " + " AND CODIGO_CARGO = '" +
                codigoCargo + "' ";

            // System.out.println(consultaFiltros);
            this.establecerConexion();
            Statement st = this.sesionBD.createStatement();
            ResultSet rs = st.executeQuery(consultaFiltros);

            while (rs.next()) {
                String idFiltro = rs.getString("ID");
                Filtro filtroLocal = filtroEJB.findByPrimaryKey(new Integer(idFiltro));
                if (filtroLocal.getEstadoFiltro().equals("0") || filtroLocal.getEstadoFiltro().equals("1")) {
                    filtroLocal.setVigenteHasta(new java.util.Date());
                    filtroLocal.setEstadoFiltro("2");
                    filtroEJB.actualizarFiltro(filtroLocal);
                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|inactivaFiltroId: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void aprobarFiltro(Hashtable pDatos) throws SQLException {
        System.out.println("==> FacadeEJBBean|aprobarFiltro|pDatos: " + pDatos);
        try {
            String idFiltro = (String) pDatos.get("ID");
            String codigoProducto = (String) pDatos.get("CODIGO_PRODUCTO");
            String numeroNegocio = (String) pDatos.get("NUMERO_NEGOCIO");
            String codigoCargo = (String) pDatos.get("CODIGO_CARGO");
            Long usuarioSupervisor = (Long) pDatos.get("USUARIO_SUPERVISOR");
            String conceptoSupervisor = (String) pDatos.get("CONCEPTO_SUPERVISOR");

            String consultaFiltros =
                "SELECT ID,ESTADO_FILTRO FROM FILTROS " + " WHERE ID = '" + idFiltro + "' " +
                " AND CODIGO_PRODUCTO = '" + codigoProducto + "' " + " AND NUMERO_NEGOCIO = '" + numeroNegocio + "' " +
                " AND CODIGO_CARGO = '" + codigoCargo + "' ";
            System.out.println("=> consultaFiltros: " + consultaFiltros);
            this.establecerConexion();
            Statement st = this.sesionBD.createStatement();
            ResultSet rs = st.executeQuery(consultaFiltros);

            while (rs.next()) {
                Filtro filtroLocal = filtroEJB.findByPrimaryKey(new Integer(idFiltro));
                // validamos que el filtro este en estado pendiente
                if (filtroLocal.getEstadoFiltro().equals("0")) {
                    filtroLocal.setVigenteDesde(new java.sql.Date(System.currentTimeMillis()));
                    filtroLocal.setEstadoFiltro("1");
                    filtroLocal.setConceptoSupervisor(conceptoSupervisor);
                    filtroLocal.setUsuarioSupervisor(usuarioSupervisor);
                    filtroLocal.setFechaSupervisor(new java.sql.Date(System.currentTimeMillis()));
                    filtroEJB.aprobarFiltro(filtroLocal);
                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|Aprobar filtros: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void rechazarFiltro(Hashtable pDatos) throws SQLException {
        try {
            String idFiltro = (String) pDatos.get("ID");
            String codigoProducto = (String) pDatos.get("CODIGO_PRODUCTO");
            String numeroNegocio = (String) pDatos.get("NUMERO_NEGOCIO");
            String codigoCargo = (String) pDatos.get("CODIGO_CARGO");
            Long usuarioSupervisor = (Long) pDatos.get("USUARIO_SUPERVISOR");
            String conceptoSupervisor = (String) pDatos.get("CONCEPTO_SUPERVISOR");

            String consultaFiltros =
                "SELECT ID,ESTADO_FILTRO FROM FILTROS " + " WHERE ID = '" + idFiltro + "' " +
                " AND CODIGO_PRODUCTO = '" + codigoProducto + "' " + " AND NUMERO_NEGOCIO = '" + numeroNegocio + "' " +
                " AND CODIGO_CARGO = '" + codigoCargo + "' ";

            this.establecerConexion();
            Statement st = this.sesionBD.createStatement();
            ResultSet rs = st.executeQuery(consultaFiltros);

            while (rs.next()) {
                Filtro filtroLocal = filtroEJB.findByPrimaryKey(new Integer(idFiltro));
                // validamos que el filtro este en estado pendiente
                if (filtroLocal.getEstadoFiltro().equals("0")) {

                    filtroLocal.setEstadoFiltro("3");
                    filtroLocal.setConceptoSupervisor(conceptoSupervisor);
                    filtroLocal.setUsuarioSupervisor(usuarioSupervisor);
                    filtroLocal.setFechaSupervisor(new java.sql.Date(System.currentTimeMillis()));
                    filtroEJB.rechazarFiltro(filtroLocal);
                }

            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw new EJBException("FacadeEJB|Rechazar filtros: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }
    public boolean cargaArchivo(Hashtable pDatos, Collection pSoporte, Collection pTxns, byte[] encoded,String strIdReporte) throws SQLException,
    NamingException,
    Exception {
		// TODO Implement this method
		
		String cargarDocumento = null;
		String estado = null;
		String randomRqUID = null;
		String tipoIdentificacion = null;
		String numeroIdentificacion = null;
		String userName=null;
		Hashtable pDatosHistorico = new Hashtable();
		String codOficina=null; //jfore18
		
		
		WSProcesoOnBasePortClient wSProcesoOnBasePortClient = new WSProcesoOnBasePortClient();  // Instancia Clase Para Invocar el servicio
		
		// LLAMAR VARIABLE EN SESSION PARA NUMERO DE LA OFICINA ,TIPO DE DOCUMENTO Y NUMERO DE IDENTIFICACION :JFORE18

		// oficina
		
		codOficina=pDatos.get("CODIGO_OFICINA").toString();//JFORE18
		System.out.println("codigo de oficina"+ codOficina);
		// NOMBRE USUARIO
		
		userName =  pDatos.get("NOMBRE_USUARIO").toString();
		System.out.println("Esta el nombre"+ userName);

		// NUMERO Y TIPO DE IDENTIFICACION
		numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
		tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
		
		
		// CONSULTA PARA TRAER EL NOMBRE LARGO TIPO DE IDENTIFICACION
		Contextos ctx = new Contextos();
		ConsultaTablaEJB consulta = (ConsultaTablaEJB)ctx.getContext("ConsultaTablaEJB");
		Collection c = consulta.consultarTabla(0,0,null, " SELECT NOMBRE_LARGO FROM LISTA_VALORES WHERE TIPO_DATO='17' AND CODIGO='" + tipoIdentificacion + "' ");
		Iterator it = c.iterator();
		String justificacion = null;
		while ( it.hasNext() ){
		Hashtable h = (Hashtable)it.next();
		justificacion = (String) h.get("NOMBRE_LARGO");
		}
		// CREACION DEL XML: JFORE18
		
		
		// codigo generado de rqUID 
		randomRqUID = UUID.randomUUID().toString();
		
		// Fecha del sistema:JFORE18
		
		Date date = new Date();
		DateFormat dateOnbase = new SimpleDateFormat("yyyyMMdd");
		DateFormat dateDocumento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String fechaDocumento= dateDocumento.format(date);
		String fechaOnbase = dateOnbase.format(date);
		
		
		CustIdType custIdType = new CustIdType();
		custIdType.setCustLoginId(userName);
		
		
		NetworkTrnInfoType networkTrnInfoType= new NetworkTrnInfoType();
		networkTrnInfoType.setNetworkOwner("BPM");
		networkTrnInfoType.setBankId("001");
		
		List<DocKeywordsType> documento = new ArrayList();
		
		DocKeywordsType docKeywordsType = new DocKeywordsType();
		DocKeywordsType docKeywordsType1 = new DocKeywordsType();
		DocKeywordsType docKeywordsType2 = new DocKeywordsType();
		DocKeywordsType docKeywordsType3 = new DocKeywordsType();
		DocKeywordsType docKeywordsType4 = new DocKeywordsType();
		
		docKeywordsType.setKeyName("No. De Reporte");
		docKeywordsType.setKeyValue(strIdReporte);
		documento.add(docKeywordsType);
		
		docKeywordsType1.setKeyName("Tipo de Documento Identificacion");
		docKeywordsType1.setKeyValue(justificacion);
		documento.add(docKeywordsType1);
		
		docKeywordsType2.setKeyName("NO. DE IDENTIFICACION");
		docKeywordsType2.setKeyValue(numeroIdentificacion);
		documento.add(docKeywordsType2);
		
		docKeywordsType3.setKeyName("Fecha");
		docKeywordsType3.setKeyValue(fechaOnbase);
		documento.add(docKeywordsType3);
		
		docKeywordsType4.setKeyName("Codigo de Oficina");
		docKeywordsType4.setKeyValue(codOficina);
		documento.add(docKeywordsType4);
		
		BinaryType binaryType = new BinaryType();
		binaryType.setContentType("101");
		binaryType.setBinData(encoded);
		
		FileInfoType fileInfoType = new FileInfoType();
		fileInfoType.setFilePath("");
		fileInfoType.setFileType(".zip");
		fileInfoType.setFileData(binaryType);
		
		DocTypeRefType DocTypeRefType = new DocTypeRefType();
		DocTypeRefType.setDocTypeName("DUCC - SOPORTES SMT SARLAFT");
		
		
		
		List<FileInfoType> extArchivo = new ArrayList();
		extArchivo.add(fileInfoType);
		
		
		DocumentAddRqType documentAddRqType = new DocumentAddRqType();
		
		documentAddRqType.setRqUID(randomRqUID);
		documentAddRqType.setCustId(custIdType);
		documentAddRqType.setNetworkTrnInfo(networkTrnInfoType);
		documentAddRqType.setClientDt(fechaDocumento);
		documentAddRqType.setCreatedDt(fechaDocumento);
		documentAddRqType.setDocKeywords(documento);
		documentAddRqType.setFileInfo(extArchivo);
		documentAddRqType.setDocTypeRef(DocTypeRefType);
		
		SetDocumentRequest datosDocumento = new SetDocumentRequest();
		datosDocumento.setDocumentAddRq(documentAddRqType);
		
		
		//RESPUESTA DEL SERVICIO:JFORE18
		try {
		wSProcesoOnBasePortClient.getEntregarDocumento(datosDocumento);
		System.out.println("Cargado Exitosamente en OnBase.");
		return true;
		}catch(Exception e){
		System.out.println("este es el error del servicio de OnBase:  "+e.getMessage());
		e.getStackTrace();
		return false;
			}    
		}

}
