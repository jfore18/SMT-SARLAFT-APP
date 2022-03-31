package transaccion;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.util.Hashtable;

import baseDatos.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import java.sql.*;

import javax.sql.*;

import javax.naming.InitialContext;

import java.sql.SQLException;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Collection;
import java.util.*;

import admin.*;
import admin.seguridad.LogConsultaEJB;
import admin.usuario.UsuarioEJB;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.context.FacesContext;

@Stateless(name = "ReporteEJB", mappedName = "SARLAFT-EJB-ReporteEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class ReporteEJBBean implements ReporteEJB {
    transient private Connection sesionBD;
    static final String PREINUSUAL = "P";
    static final String INUSUAL = "I";
    static final String NORMAL = "N";

    static final String ESTUDIO = "1";
    static final String REVISION = "2";
    static final String REPORTADO = "3";
    static final String NO_REPORTADO = "4";
    static final String GESTIONADO = "5";

    static final String CLASE_NORMAL = "1";
    static final String CLASE_INUSUAL = "2";
    static final String CLASE_CLIENTE = "3";
    
    @EJB
    private HistoricoTransaccionEJB historicoEJB;
    

    public void ejbCreate() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
        try {
            this.cerrarConexion();
        } catch (Exception ex) {

        }

    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
    }

    public Hashtable crearReporte(Hashtable pDatos) throws SQLException {
        try {
            Integer id = null;
            String codigoCargo = null;
            String codigoClaseReporte = null;
            String codigoEstadoReporte = null;
            String codigoTipoReporte = null;
            String justificacionInicial = null;
            String justificacionInicialA = null;
            String justificacionInicialB = null;
            String justificacionInicialC = null;
            Long usuarioCreacion = null;
            Timestamp fechaCreacion = null;
            Long usuarioActualizacion = null;
            Timestamp fechaActualizacion = null;
            String otroDocumentoSoporte = null;
            String otraFuenteInformacion = null;
            String numeroIdentificacion = null;
            String tipoIdentificacion = null;
            this.establecerConexion();

            codigoCargo = (String) pDatos.get("CODIGO_CARGO");
            try {
                TablaBD tabla = new TablaBD("CARGOS", "CODIGO", sesionBD);
                tabla.asignarValorColumna("CODIGO", codigoCargo);
                if (!tabla.buscarPK()) {
                    throw new EJBException("ReporteEJB|CrearReporte: Cargo no existe");
                }
            } catch (Exception e) {
                throw new EJBException("ReporteEJB|CrearReporte: " + e.getMessage());
            }

            codigoClaseReporte = (String) pDatos.get("CODIGO_CLASE_REPORTE_V");

            if (codigoClaseReporte != null) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(7));
                    tabla.asignarValorColumna("CODIGO", codigoClaseReporte);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearReporte: Clase de reporte no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearReporte: " + e.getMessage());
                }
            }

            codigoEstadoReporte = (String) pDatos.get("CODIGO_ESTADO_REPORTE_V");
            //System.out.println("EstadoReporte: " + codigoEstadoReporte);
            if (codigoEstadoReporte != null) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(9));
                    tabla.asignarValorColumna("CODIGO", codigoEstadoReporte);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearReporte: Estado de reporte no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearReporte: " + e.getMessage());
                }
            }
            codigoTipoReporte = (String) pDatos.get("CODIGO_TIPO_REPORTE_V");
            //System.out.println("EstadoReporte: " + codigoTipoReporte);
            if (codigoTipoReporte != null) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(4));
                    tabla.asignarValorColumna("CODIGO", codigoTipoReporte);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearReporte: Tipo de reporte no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearReporte: " + e.getMessage());
                }
            }
            justificacionInicial = (String) pDatos.get("JUSTIFICACION_INICIAL");

            /* manejo de la justificacion inicial para tres columnas varchar2 de 4000*/
            String[] justificaciones = this.partirJustificacionInicial(justificacionInicial);
            justificacionInicialA = justificaciones[0];
            justificacionInicialB = justificaciones[1];
            justificacionInicialC = justificaciones[2];

            usuarioCreacion = (Long) pDatos.get("USUARIO_CREACION");
            fechaCreacion = (Timestamp) pDatos.get("FECHA_CREACION");
            usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
            fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
            numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
            tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");
            //System.out.println("2" );
            TablaBD miTabla = new TablaBD("REPORTE", "ID", sesionBD);
            //System.out.println("3");
            id = miTabla.traerIDSecuencia("SEQ_REPORTE");
            miTabla.asignarValorColumna("ID", id);
            miTabla.asignarValorColumna("CODIGO_CARGO", codigoCargo);
            miTabla.asignarValorColumna("CODIGO_TIPO_REPORTE_V", codigoTipoReporte);
            miTabla.asignarValorColumna("CODIGO_CLASE_REPORTE_V", codigoClaseReporte);
            miTabla.asignarValorColumna("CODIGO_ESTADO_REPORTE_V", codigoEstadoReporte);
            miTabla.asignarValorColumna("JUSTIFICACION_INICIAL", justificacionInicialA);
            miTabla.asignarValorColumna("JUSTIFICACION_INICIAL_B", justificacionInicialB);
            miTabla.asignarValorColumna("JUSTIFICACION_INICIAL_C", justificacionInicialC);

            miTabla.asignarValorColumna("USUARIO_CREACION", usuarioCreacion);
            miTabla.asignarValorColumna("FECHA_CREACION", fechaCreacion);
            miTabla.asignarValorColumna("USUARIO_ACTUALIZACION", usuarioActualizacion);
            miTabla.asignarValorColumna("FECHA_ACTUALIZACION", fechaActualizacion);
            try {
                miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            } catch (Exception er) {
            }
            try {
                miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            } catch (Exception er) {
            }
            try {
                otroDocumentoSoporte = (String) pDatos.get("OTRO_DOCUMENTO_SOPORTE");
                miTabla.asignarValorColumna("OTRO_DOCUMENTO_SOPORTE", otroDocumentoSoporte);
            } catch (Exception er) {
            }
            // LOS SIGUIENTES CAMPOS SON LOS DEL ANALISIS DEL DUCC
            try {
                otraFuenteInformacion = (String) pDatos.get("OTRA_FUENTE_INFORMACION");
                miTabla.asignarValorColumna("OTRA_FUENTE_INFORMACION", otraFuenteInformacion);
            } catch (Exception er) {
            }
            try {
                String codigoTipoConsulta = (String) pDatos.get("CODIGO_TIPO_CONSULTA_V");
                miTabla.asignarValorColumna("CODIGO_TIPO_CONSULTA_V", codigoTipoConsulta);
            } catch (Exception er) {
            }

            try {
                String indagacion = (String) pDatos.get("INDAGACION");
                miTabla.asignarValorColumna("INDAGACION", indagacion);
            } catch (Exception er) {
            }

            try {
                Timestamp fechaAnalisis = (Timestamp) pDatos.get("FECHA_ANALISIS");
                miTabla.asignarValorColumna("FECHA_ANALISIS", fechaAnalisis);
            } catch (Exception er) {
            }
            //System.out.println("4" + miTabla.toString());
            miTabla.insertarDatos();
            Hashtable reporte = new Hashtable();
            reporte.put("ID_REPORTE", id);
            return reporte;
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|crearReporte: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void adicionarPersona(Hashtable pDatos) throws SQLException {
        System.out.println("===========ReporteEJBBean===>adicionarPersona===================");
        try {
            this.establecerConexion();
            boolean clienteYaExiste = false;

            String tipoIdentificacion = null;
            String numeroIdentificacion = null;
            Integer idReporte = null;
            String apellidos_razonSocial = null;
            String nombres_razonComercial = null;
            String direccionEmail = null;
            String tipoRelacionBanco = null;
            String otraRelacion = null;
            boolean vinculadoBanco = false;
            String razonRetiroBanco = null;
            Timestamp fechaInicioVinculacion = null;
            Timestamp fechaFinVinculacion = null;
            BigDecimal ingresosMensuales = null;
            Timestamp fechaIngresosMensuales = null;
            String codigoCIIU = null;
            String descripcionActividadEconomica = null;
            String telefono = null;
            String codigoTipoTelefono = null;
            String fax = null;
            String codigoMunicipio = null;
            String codigoTipoDireccion = null;
            String direccion = null;
            String apellidosRepresentanteLegal = null;
            String nombresRepresentanteLegal = null;
            String tipoIdentificacionRepLegal = null;
            String numeroIdentificacionRepLegal = null;

            apellidos_razonSocial = (String) pDatos.get("APELLIDOS_RAZON_SOCIAL");
            nombres_razonComercial = (String) pDatos.get("NOMBRES_RAZON_COMERCIAL");

            if (apellidos_razonSocial == null && nombres_razonComercial == null ||
                (apellidos_razonSocial.trim().length() == 0 && nombres_razonComercial.trim().length() == 0)) {
                return;
            }

            tipoIdentificacion = ((String) pDatos.get("TIPO_IDENTIFICACION")).trim();
            try {
                TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                tabla.asignarValorColumna("TIPO_DATO", new Integer(17));
                tabla.asignarValorColumna("CODIGO", tipoIdentificacion);
                if (!tabla.buscarPK()) {
                    throw new EJBException("ReporteEJB|CrearPersona: Tipo de identificacion no existe");
                }
            } catch (Exception e) {
                throw new EJBException("ReporteEJB|CrearPersonaTIPOIDENTIFICACION: " + e.getMessage());
            }

            numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");

            idReporte = (Integer) pDatos.get("ID_REPORTE");
            try {
                TablaBD tabla = new TablaBD("REPORTE", "ID", sesionBD);
                tabla.asignarValorColumna("ID", idReporte);
                if (!tabla.buscarPK()) {
                    throw new EJBException("ReporteEJB|CrearPersona: Id de reporte no existe");
                }
            } catch (Exception e) {
                throw new EJBException("ReporteEJB|CrearPersonaREPORTE: " + e.getMessage());
            }

            /*apellidos_razonSocial = (String) pDatos.get("APELLIDOS_RAZON_SOCIAL");
      nombres_razonComercial= (String)pDatos.get("NOMBRES_RAZON_COMERCIAL");*/

            TablaBD miTabla =
                new TablaBD("PERSONAS_REP", "TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION,ID_REPORTE", sesionBD);
            try {
                miTabla.asignarValorColumna("ID_REPORTE", idReporte);
                miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
                miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
                clienteYaExiste = miTabla.buscarPK();
            } catch (Exception e) {
                clienteYaExiste = false;
            }

            try {
                direccionEmail = (String) pDatos.get("DIRECCION_EMAIL");
                miTabla.asignarValorColumna("DIRECCION_EMAIL", direccionEmail);
            } catch (Exception e) {
            }

            tipoRelacionBanco = ((String) pDatos.get("TIPO_RELACION_V"));
            if (tipoRelacionBanco != null) {
                tipoRelacionBanco = ((String) pDatos.get("TIPO_RELACION_V")).trim();
                miTabla.asignarValorColumna("TIPO_RELACION_V", tipoRelacionBanco);
            }

            try {
                otraRelacion = (String) pDatos.get("OTRA_RELACION");
                miTabla.asignarValorColumna("OTRA_RELACION", otraRelacion);
            } catch (Exception e) {
            }

            try {
                vinculadoBanco = ((Boolean) pDatos.get("VINCULADO")).booleanValue();
                miTabla.asignarValorColumna("VINCULADO", vinculadoBanco);
            } catch (Exception e) {
            }

            try {
                razonRetiroBanco = ((String) pDatos.get("RAZON_RETIRO_V")).trim();
                miTabla.asignarValorColumna("RAZON_RETIRO_V", razonRetiroBanco);
            } catch (Exception e) {
            }

            try {
                fechaInicioVinculacion = (Timestamp) pDatos.get("INICIO_VINCULACION");
                miTabla.asignarValorColumna("INICIO_VINCULACION", fechaInicioVinculacion);
            } catch (Exception e) {
            }

            try {
                fechaFinVinculacion = (Timestamp) pDatos.get("FINAL_VINCULACION");
                miTabla.asignarValorColumna("FINAL_VINCULACION", fechaFinVinculacion);
            } catch (Exception e) {
            }

            try {
                ingresosMensuales = (BigDecimal) pDatos.get("INGRESOS_MENSUALES");
                miTabla.asignarValorColumna("INGRESOS_MENSUALES", ingresosMensuales);
            } catch (Exception e) {
            }

            try {
                fechaIngresosMensuales = (Timestamp) pDatos.get("FECHA_INGRESOS");
                miTabla.asignarValorColumna("FECHA_INGRESOS", fechaIngresosMensuales);
            } catch (Exception e) {
            }

            codigoCIIU = (String) pDatos.get("CODIGO_CIIU");
            if (codigoCIIU != null) {
                try {
                    TablaBD tabla = new TablaBD("ACTIVIDAD_ECONOMICA", "CODIGO", sesionBD);
                    tabla.asignarValorColumna("CODIGO", codigoCIIU);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearPersona: Codigo CIIU no existe");
                    }
                    miTabla.asignarValorColumna("CODIGO_CIIU", codigoCIIU);
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearPersonaCIIU: " + e.getMessage());
                }
            }

            descripcionActividadEconomica = (String) pDatos.get("DESCRIPCION_ACTIVIDAD_EC");
            if (descripcionActividadEconomica != null) {
                miTabla.asignarValorColumna("DESCRIPCION_ACTIVIDAD_EC", descripcionActividadEconomica);
            }

            try {
                telefono = (String) pDatos.get("TELEFONO");
                miTabla.asignarValorColumna("TELEFONO", telefono);
            } catch (Exception e) {
            }

            codigoTipoTelefono = (String) pDatos.get("CODIGO_TIPO_TELEFONO_V");

            if (codigoTipoTelefono != null && codigoTipoTelefono.trim().length() > 0) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(22));
                    tabla.asignarValorColumna("CODIGO", codigoTipoTelefono);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearPersona: Codigo tipo telefono no existe");
                    }
                    miTabla.asignarValorColumna("CODIGO_TIPO_TELEFONO_V", codigoTipoTelefono);
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearPersonaTIPOtelefono: " + e.getMessage());
                }
            }

            try {
                fax = (String) pDatos.get("FAX");
                miTabla.asignarValorColumna("FAX", fax);
            } catch (Exception e) {
            }

            try {
                direccion = (String) pDatos.get("DIRECCION");
                miTabla.asignarValorColumna("DIRECCION", direccion);
            } catch (Exception e) {
            }

            codigoTipoDireccion = (String) pDatos.get("CODIGO_TIPO_DIRECCION_V");
            if (codigoTipoDireccion != null && codigoTipoDireccion.trim().length() > 0) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(18));
                    tabla.asignarValorColumna("CODIGO", codigoTipoDireccion);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearPersona: Codigo tipo direccion no existe");
                    }
                    miTabla.asignarValorColumna("CODIGO_TIPO_DIRECCION_V", codigoTipoDireccion);
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearPersonaTIPODIRECCION: " + e.getMessage());
                }
            }

            codigoMunicipio = (String) pDatos.get("CODIGO_MUNICIPIO");
            if (codigoMunicipio != null && codigoMunicipio.trim().length() > 0) {
                try {
                    TablaBD tabla = new TablaBD("MUNICIPIO", "CODIGO", sesionBD);
                    tabla.asignarValorColumna("CODIGO", codigoMunicipio);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearPersona: Codigo municipio no existe");
                    }
                    miTabla.asignarValorColumna("CODIGO_MUNICIPIO", codigoMunicipio);
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearPersonaMUNICIPIO: " + e.getMessage());
                }
            }

            apellidosRepresentanteLegal = (String) pDatos.get("APELLIDOS_REP_LEGAL");
            if (apellidosRepresentanteLegal != null) {

                nombresRepresentanteLegal = (String) pDatos.get("NOMBRES_REP_LEGAL");
                tipoIdentificacionRepLegal = (String) pDatos.get("TI_REP_LEGAL");
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(17));
                    tabla.asignarValorColumna("CODIGO", tipoIdentificacionRepLegal);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|CrearPersona: Tipo de identificacion Representante Legal no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|CrearPersonaREPLEGAL: " + e.getMessage());
                }
                numeroIdentificacionRepLegal = (String) pDatos.get("NI_REP_LEGAL");
                miTabla.asignarValorColumna("APELLIDOS_REP_LEGAL", apellidosRepresentanteLegal);
                miTabla.asignarValorColumna("NOMBRES_REP_LEGAL", nombresRepresentanteLegal);
                miTabla.asignarValorColumna("TI_REP_LEGAL", tipoIdentificacionRepLegal);
                miTabla.asignarValorColumna("NI_REP_LEGAL", numeroIdentificacionRepLegal);
            }

            miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            miTabla.asignarValorColumna("ID_REPORTE", idReporte);
            miTabla.asignarValorColumna("APELLIDOS_RAZON_SOCIAL", apellidos_razonSocial);
            miTabla.asignarValorColumna("NOMBRES_RAZON_COMERCIAL", nombres_razonComercial);
            System.out.println("==> clienteYaExiste: " + clienteYaExiste);
            if (clienteYaExiste) {
                miTabla.actualizarDatos();
            } else {
                this.borrarPersonas(idReporte);
                miTabla.insertarDatos();
            }
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|adicionarPersona: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void adicionarTxn(Hashtable pDatos) throws SQLException {
        try {
            Integer codigoArchivo = null;
            Timestamp fechaProceso = null;
            Integer idTransaccion = null;
            Integer idReporte = null;
            Long usuarioActualizacion = null;
            Timestamp fechaActualizacion = null;
            this.establecerConexion();
            codigoArchivo = (Integer) pDatos.get("CODIGO_ARCHIVO");
            if (codigoArchivo != null) {
                fechaProceso = (Timestamp) pDatos.get("FECHA_PROCESO");
                idTransaccion = (Integer) pDatos.get("ID_TRANSACCION");
            } else {
                throw new EJBException("ReporteEJB:AdicionarTransaccion: Llave de transaccion vacia");
            }
            idReporte = (Integer) pDatos.get("ID_REPORTE");
            if (idReporte != null) {
            } else {
                throw new EJBException("ReporteEJB:AdicionarTransaccion: Id de Reporte vacia");
            }
            try {
                usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
            } catch (Exception r) {
            }
            try {
                fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
            } catch (Exception r) {
            }
            String strInsercion =
                "INSERT INTO TRANSACCIONES_REP (" +
                " CODIGO_ARCHIVO, FECHA_PROCESO, ID_TRANSACCION, ID_REPORTE, USUARIO_ACTUALIZACION, FECHA_ACTUALIZACION ) " +
                " VALUES ( ?, ?, ?, ?, ?, ?)";
            PreparedStatement insercion = sesionBD.prepareStatement(strInsercion);
            insercion.setInt(1, codigoArchivo.intValue());
            insercion.setTimestamp(2, fechaProceso);
            insercion.setInt(3, idTransaccion.intValue());
            insercion.setInt(4, idReporte.intValue());
            insercion.setLong(5, usuarioActualizacion.longValue());
            insercion.setTimestamp(6, fechaActualizacion);
            insercion.execute();
            insercion.close();
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|adicionarTxn: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    /**
     * @Métodos de Contexto y conexión a Base de Datos
     */

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

    public void adicionarRespuesta(Hashtable pDatos) throws SQLException {
        boolean cerrarConex = false;
        try {
            this.cerrarConexion();
            this.establecerConexion();
            Integer idPregunta = (Integer) pDatos.get("ID_PREGUNTA");
            if (idPregunta != null) {

            } else {
                throw new EJBException("ReporteEJB:AdicionarRespuesta: Pregunta Nula");
            }
            Integer idReporte = (Integer) pDatos.get("ID_REPORTE");
            if (idReporte != null) {

            } else {
                throw new EJBException("ReporteEJB:AdicionarRespuesta: Id de Reporte vacia");
            }

            boolean bRespuesta = false;
            String sRespuesta = null;
            TablaBD miTabla = null;

            if (pDatos.containsKey("S_RESPUESTA")) {
                sRespuesta = (String) pDatos.get("S_RESPUESTA");
                miTabla = new TablaBD("RTAS_TEXT_REP", "ID_PREGUNTA,ID_REPORTE", sesionBD);
                miTabla.asignarValorColumna("RESPUESTA", sRespuesta);
            } else {
                if (pDatos.containsKey("B_RESPUESTA")) {
                    try {
                        bRespuesta = Boolean.valueOf((String) pDatos.get("B_RESPUESTA")).booleanValue();
                        miTabla = new TablaBD("RTAS_BOOLEAN_REP", "ID_PREGUNTA,ID_REPORTE", sesionBD);
                        miTabla.asignarValorColumna("RESPUESTA", bRespuesta);
                    } catch (Exception e) {
                        throw new EJBException("ReporteEJB|AdicionarRespuesta1: Respuesta booleana no valida " +
                                               e.getMessage() + " respuesta: " + pDatos.get("B_RESPUESTA"));
                    }
                } else {
                    throw new EJBException("ReporteEJB|AdicionarRespuesta2: Respuesta no valida");
                }
            }
            miTabla.asignarValorColumna("ID_PREGUNTA", idPregunta);
            miTabla.asignarValorColumna("ID_REPORTE", idReporte);
            try {
                if (miTabla.buscarPK()) {
                    miTabla.actualizarDatos();
                } else {
                    miTabla.insertarDatos();
                }
            } catch (Exception error) {
                miTabla.insertarDatos();
            }
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|adicionarRespuesta: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public Hashtable mostrarDatos(Integer pIdReporte) {
        Hashtable resultados = new Hashtable();
        try { //System.out.println("1");
            this.establecerConexion();
            TablaBD tabla = new TablaBD("REPORTE", "ID", this.sesionBD);
            tabla.asignarValorColumna("ID", pIdReporte);
            tabla.consultarDatos();
            //System.out.println("2");
            Hashtable datos = new Hashtable(); //System.out.println("3");
            datos.put("ID", tabla.traerValorColumna("ID")); //System.out.println("4");
            datos.put("CODIGO_CARGO", tabla.traerValorColumna("CODIGO_CARGO")); //System.out.println("5");
            //datos.put( "JUSTIFICACION_INICIAL", tabla.traerValorColumna( "JUSTIFICACION_INICIAL" ) );////System.out.println("6");
            datos.put("JUSTIFICACION_INICIAL", this.traerJustificacion(tabla));
            try {
                datos.put("CODIGO_CLASE_REPORTE_V",
                          tabla.traerValorColumna("CODIGO_CLASE_REPORTE_V")); //System.out.println("7");
            } catch (Exception e) {
            }

            try {
                datos.put("CODIGO_TIPO_REPORTE_V",
                          tabla.traerValorColumna("CODIGO_TIPO_REPORTE_V")); //System.out.println("8");
            } catch (Exception ec) {
            }

            try {
                datos.put("ROS_RELACIONADO", tabla.traerValorColumna("ROS_RELACIONADO")); //System.out.println("9");
            } catch (Exception ec) {
            }

            try {
                datos.put("CODIGO_TIPO_CONSULTA_V",
                          tabla.traerValorColumna("CODIGO_TIPO_CONSULTA_V")); //System.out.println("10");
            } catch (Exception ec) {
            }

            try {
                datos.put("INDAGACION", tabla.traerValorColumna("INDAGACION")); //System.out.println("11");
            } catch (Exception ec) {
            }

            try {
                datos.put("REVISADO", tabla.traerValorColumna("REVISADO")); //System.out.println("12");
            } catch (Exception ec) {
            }

            try {
                datos.put("OTRO_DOCUMENTO_SOPORTE",
                          tabla.traerValorColumna("OTRO_DOCUMENTO_SOPORTE")); //System.out.println("13");
            } catch (Exception ec) {
            }

            try {
                datos.put("OTRA_FUENTE_INFORMACION",
                          tabla.traerValorColumna("OTRA_FUENTE_INFORMACION")); //System.out.println("14");
            } catch (Exception ec) {
            }

            try {
                datos.put("EXISTE_REP_ANALISTA",
                          tabla.traerValorColumna("EXISTE_REP_ANALISTA")); //System.out.println("15");
            } catch (Exception ec) {
            }

            try {
                datos.put("CODIGO_ESTADO_REPORTE_V",
                          tabla.traerValorColumna("CODIGO_ESTADO_REPORTE_V")); //System.out.println("16");
            } catch (Exception e) {
            }
            try {
                datos.put("TIPO_IDENTIFICACION",
                          tabla.traerValorColumna("TIPO_IDENTIFICACION")); //System.out.println("16");
            } catch (Exception e) {
            }
            try {
                datos.put("NUMERO_IDENTIFICACION",
                          tabla.traerValorColumna("NUMERO_IDENTIFICACION")); //System.out.println("16");
            } catch (Exception e) {
            }

            datos.put("USUARIO_CREACION", tabla.traerValorColumna("USUARIO_CREACION")); //System.out.println("17");
            datos.put("FECHA_CREACION", tabla.traerValorColumna("FECHA_CREACION")); //System.out.println("18");
            try {
                datos.put("USUARIO_ACTUALIZACION",
                          tabla.traerValorColumna("USUARIO_ACTUALIZACION")); //System.out.println("19");
                datos.put("FECHA_ACTUALIZACION",
                          tabla.traerValorColumna("FECHA_ACTUALIZACION")); //System.out.println("20");
            } catch (Exception e) {
            }

            return datos;
        } catch (Exception error) {
            throw new EJBException("ReporteEJB|mostrarDatos: " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                throw new EJBException("ReporteEJB|mostrarDatos: No se pudo cerrar la conexion");
            }
        }
    }

    private ConsultaTablaEJB getConsultaTablaEJB() throws Exception {
        Contextos ctx = new Contextos();
        ConsultaTablaEJB consultaTablaEJB = (ConsultaTablaEJB) ctx.getContext("ConsultaTablaEJB");
        return consultaTablaEJB;
    }

    public Collection mostrarTxn(Integer pId) {
        Collection col = new ArrayList();
        try {
            this.establecerConexion();
            String consultaTransaccion =
                "SELECT CODIGO_ARCHIVO, FECHA_PROCESO, ID_TRANSACCION " + " FROM TRANSACCIONES_REP " +
                " WHERE ID_REPORTE = " + pId;
            Statement st = sesionBD.createStatement();
            ResultSet result = st.executeQuery(consultaTransaccion);
            col = new ArrayList();
            while (result.next()) {
                Hashtable txn = new Hashtable();
                txn.put("CODIGO_ARCHIVO", result.getString("CODIGO_ARCHIVO"));
                txn.put("FECHA_PROCESO", result.getTimestamp("FECHA_PROCESO"));
                txn.put("ID_TRANSACCION", result.getString("ID_TRANSACCION"));
                col.add(txn);
            }

            result.close();
            st.close();

            return col;

        } catch (Exception error) {
            throw new EJBException("ReporteEJB|mostrarDatosTxn: " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                throw new EJBException("ReporteEJB|mostrarDatosTxn: No se pudo cerrar la conexion");
            }
        }
    }

    public Collection mostrarPersonas(Integer pId) {
        Collection personas = new ArrayList();
        try {
            this.establecerConexion();
            String consultaPersonas = " WHERE ID_REPORTE = " + pId;
            ConsultaTablaEJB consultaTablaEJB = this.getConsultaTablaEJB();
            //   ConsultaTablaLocal consulta =  consultaLocal.create();

            personas = consultaTablaEJB.consultarTabla(0, 0, "PERSONAS_REP", consultaPersonas);
            return personas;
        } catch (Exception error) {
            throw new EJBException("ReporteEJB|mostrarDatosPersonas: " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                throw new EJBException("ReporteEJB|mostrarDatosPersonas: No se pudo cerrar la conexion");
            }
        }
    }

    public Hashtable mostrarRespuestas(Integer pId) {
        Hashtable resultados = new Hashtable();
        try {
            this.establecerConexion();
            String consultaPreguntas = " WHERE ID_REPORTE = " + pId;
            ConsultaTablaEJB consulta = this.getConsultaTablaEJB();
            //  ConsultaTablaLocal consulta =  consultaLocal.create();
            Collection preguntasBoolean = consulta.consultarTabla(0, 0, "RTAS_BOOLEAN_REP", consultaPreguntas);
            resultados.put("RESPUESTAS_BOOLEANAS", preguntasBoolean);

            Collection preguntasTexto = consulta.consultarTabla(0, 0, "RTAS_TEXT_REP", consultaPreguntas);
            Collection otroDoc =
                consulta.consultarTabla(0, 0, null,
                                        " SELECT OTRO_DOCUMENTO_SOPORTE, " + " OTRA_FUENTE_INFORMACION, " +
                                        " CODIGO_TIPO_CONSULTA_V, " + " INDAGACION FROM REPORTE WHERE ID = " + pId);
            preguntasTexto.addAll(otroDoc);
            resultados.put("RESPUESTAS_TEXTO", preguntasTexto);
            return resultados;
        } catch (Exception error) {
            throw new EJBException("ReporteEJB|mostrarDatosRespuestas: " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                throw new EJBException("ReporteEJB|mostrarDatosRespuestas: No se pudo cerrar la conexion");
            }
        }
    }

    public String actualizarEstado(Hashtable pDatos) {
        try {
            Integer pIdReporte = (Integer) pDatos.get("ID_REPORTE");
            String pNuevoEstado = (String) pDatos.get("CODIGO_ESTADO_REPORTE_V");
            String rosAsignado = "";

            Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
            Date dFechaActualizacion = new Date(fechaActualizacion.getTime());

            Long usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");

            // Estado dos, revision
            if (pNuevoEstado.equals("2")) {
                this.establecerConexion();
                /*String actualizacion = "UPDATE REPORTE " +
                     " SET CODIGO_ESTADO_REPORTE_V = '" + pNuevoEstado + "', " +
                     " FECHA_ACTUALIZACION = TO_DATE('" + + "','YYYY/MM/DD'), " +
                     " USUARIO_ACTUALIZACION = '" + + "'" +
                     " WHERE ID = '" + pIdReporte + "'";*/
                String actualizacion =
                    "UPDATE REPORTE " + " SET CODIGO_ESTADO_REPORTE_V = ? ," + " FECHA_ACTUALIZACION = ? ," +
                    " USUARIO_ACTUALIZACION = ?" + " WHERE ID = ? ";
                //Statement sentencia = sesionBD.createStatement();
                PreparedStatement ps = sesionBD.prepareStatement(actualizacion);
                ps.setString(1, pNuevoEstado);
                ps.setTimestamp(2, fechaActualizacion);
                ps.setLong(3, usuarioActualizacion.longValue());
                ps.setInt(4, pIdReporte.intValue());

                //sentencia.executeUpdate(actualizacion);
                //sentencia.close();
                ps.executeUpdate();
                ps.close();
                this.cerrarConexion();
            } else {
                //Estado tres, reportar
                if (pNuevoEstado.equals("3")) {

                    Collection transacciones = this.mostrarTxn(pIdReporte);

                    this.establecerConexion();
                    Integer ros = null;
                    String actualizacion = "";

                    if (pDatos.get("ROS_RELACIONADO") == "") {
                        TablaBD tabla = new TablaBD("REPORTE", "ID", sesionBD);
                        if (pDatos.containsKey("IS_MEGABANCO")) {
                            ros = tabla.traerIDSecuencia("SEQ_ROS_MEG");
                        } else {
                            ros = tabla.traerIDSecuencia("SEQ_ROS");
                        }
                        rosAsignado = ros.toString();
                    } else {
                        ros = (Integer) pDatos.get("ROS_RELACIONADO");
                    }
                    String codigoTipoReporte = (String) pDatos.get("CODIGO_TIPO_REPORTE_V");
                    actualizacion =
                        "UPDATE REPORTE " + " SET CODIGO_ESTADO_REPORTE_V = ? " + " ,ROS_RELACIONADO = ? " +
                        " ,CODIGO_TIPO_REPORTE_V = ? " + " ,FECHA_ACTUALIZACION = ? " + " ,USUARIO_ACTUALIZACION = ?" +
                        " WHERE ID = ?";
                    PreparedStatement ps = sesionBD.prepareStatement(actualizacion);
                    ps.setString(1, pNuevoEstado);
                    ps.setInt(2, ros.intValue());
                    ps.setString(3, codigoTipoReporte);
                    ps.setTimestamp(4, fechaActualizacion);
                    ps.setLong(5, usuarioActualizacion.longValue());

                    ps.setInt(6, pIdReporte.intValue());

                    int reg = ps.executeUpdate();
                    ps.close();
                    if (transacciones != null) {
                        Iterator it = transacciones.iterator();

                        Statement st = sesionBD.createStatement();
                        while (it.hasNext()) {

                            Hashtable h = (Hashtable) it.next();
                            String idT = (String) h.get("ID_TRANSACCION");
                            Timestamp fechaP = (Timestamp) h.get("FECHA_PROCESO");
                            String codigoA = (String) h.get("CODIGO_ARCHIVO");

                            String act =
                                " UPDATE TRANSACCIONES_CLIENTE " + " SET ESTADO_DUCC = 'S', " +
                                " USUARIO_ACTUALIZACION = '" + usuarioActualizacion + "'," +
                                " FECHA_ACTUALIZACION = SYSDATE " + " WHERE CODIGO_ARCHIVO = '" + codigoA + "'" +
                                " AND FECHA_PROCESO = TO_DATE('" +
                                CatalogoBD.formatoFecha.format((new Date(fechaP.getTime()))) + "', 'YYYY/MM/DD') " +
                                " AND ID = ' " + idT + "'";

                            st.executeUpdate(act);
                            Hashtable datos = this.copiarHash(pDatos);
                            datos.put("ID_TRANSACCION", new Integer(idT));
                            datos.put("FECHA_PROCESO", new java.sql.Date(fechaP.getTime()));
                            datos.put("FECHA_ACTUALIZACION", new java.sql.Date(fechaActualizacion.getTime()));
                            datos.put("CODIGO_ARCHIVO", new Integer(codigoA));
                            datos.put("CODIGO_ESTADO", pNuevoEstado);
    
                            //HistoricoTransaccionEJB historico = this.getHistoricoTransaccionEJB();
                            
                            historicoEJB.create(datos);
                        }
                        st.close();
                        this.cerrarConexion();
                    }

                    if (pDatos.get("ROS_RELACIONADO") == "") {

                        Collection c = new ArrayList();
                        Hashtable h = new Hashtable();
                        h.put("ID_REPORTE", pIdReporte);
                        h.put("USUARIO_ACTUALIZACION", usuarioActualizacion);
                        h.put("FECHA_ACTUALIZACION", new Timestamp(System.currentTimeMillis()));
                        c.add(h);
                        this.establecerConexion();
                        this.adicionarPersonaLista(c);
                    }
                    this.establecerConexion();

                } else {
                    //Estado cuatro, no reportar
                    if (pNuevoEstado.equals("4")) {
                        Collection transacciones = this.mostrarTxn(pIdReporte);
                        this.establecerConexion();
                        String actualizacion =
                            "UPDATE REPORTE " + " SET CODIGO_ESTADO_REPORTE_V = ? " + " , FECHA_ACTUALIZACION = ? " +
                            " , USUARIO_ACTUALIZACION = ? " + " WHERE ID = ?";
                        PreparedStatement ps = sesionBD.prepareStatement(actualizacion);
                        ps.setString(1, pNuevoEstado);
                        ps.setTimestamp(2, fechaActualizacion);
                        ps.setLong(3, usuarioActualizacion.longValue());
                        ps.setInt(4, pIdReporte.intValue());
                        int reg = ps.executeUpdate();
                        ps.close();
                        if (transacciones != null) {
                            Iterator it = transacciones.iterator();
                            while (it.hasNext()) {
                                Hashtable h = (Hashtable) it.next();
                                String idT = (String) h.get("ID_TRANSACCION");
                                Timestamp fechaP = (Timestamp) h.get("FECHA_PROCESO");
                                String codigoA = (String) h.get("CODIGO_ARCHIVO");

                                String act =
                                    " UPDATE TRANSACCIONES_CLIENTE " + " SET ESTADO_DUCC = 'M' " +
                                    " WHERE CODIGO_ARCHIVO = '" + codigoA + "'" + " AND FECHA_PROCESO = TO_DATE('" +
                                    CatalogoBD.formatoFecha.format(new Date(fechaP.getTime())) + "', 'YYYY/MM/DD') " +
                                    " AND ID = ' " + idT + "'";

                                Statement st = sesionBD.createStatement();
                                st.executeUpdate(act);
                                st.close();

                                Hashtable datos = this.copiarHash(pDatos);
                                datos.put("ID_TRANSACCION", new Integer(idT));
                                datos.put("FECHA_PROCESO", new java.sql.Date(fechaP.getTime()));
                                datos.put("FECHA_ACTUALIZACION", new java.sql.Date(fechaActualizacion.getTime()));
                                datos.put("CODIGO_ARCHIVO", new Integer(codigoA));
                                datos.put("CODIGO_ESTADO", pNuevoEstado);
                                
                                
                               // HistoricoTransaccionEJB historico = this.getHistoricoTransaccionEJB();
                                //historico.create(datos);
                                historicoEJB.create(datos);
                            }
                        }
                    }
                }
                this.cerrarReportesCliente(pDatos);
                /* Ingrid Alonso 2007-02-23
                 * Llamado a actualizar el estado de los reportes asociados
                 * a gestionado*/
                this.actualizarReportesAsociados(pIdReporte);
                this.cerrarConexion();
            }
            return rosAsignado;
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|actualizarEstado: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException sql) {
                throw new EJBException("ReporteEJB|actualizarEstado: no se pudo cerrar la conexion");
            }
        }
    }

    private HistoricoTransaccionEJB getHistoricoTransaccionEJB() throws Exception {
         Contextos ctx = new Contextos();
        return (HistoricoTransaccionEJB) ctx.getContext("HistoricoTransaccionEJB");
        
           
    }

    private Hashtable copiarHash(Hashtable pTabla) {
        Hashtable copia = new Hashtable();
        Enumeration it = pTabla.keys();
        while (it.hasMoreElements()) {
            String llave = (String) it.nextElement();
            copia.put(llave, pTabla.get(llave));
        }
        return copia;
    }

    public void marcarRevisado(String pIdReporte) throws SQLException {
        try {
            this.establecerConexion();
            String act = "UPDATE REPORTE " + " SET REVISADO = 1 " + " WHERE ID = " + pIdReporte;
            Statement st = sesionBD.createStatement();
            st.executeUpdate(act);
        } catch (Exception error) {
            throw new EJBException("ReporteEJB|marcarRevisado: " + error.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void adicionarPersonaLista(Collection pDatos) throws SQLException {
        try { //System.out.println("1");
            Iterator it = pDatos.iterator();
            while (it.hasNext()) {
                Hashtable h = (Hashtable) it.next();
                Integer pIdReporte = (Integer) h.get("ID_REPORTE");
                Long usuarioAct = (Long) h.get("USUARIO_ACTUALIZACION");
                Timestamp fechaAct = (Timestamp) h.get("FECHA_ACTUALIZACION");
                Collection personas = this.mostrarPersonas(pIdReporte);
                Iterator itt = personas.iterator();
                //System.out.println("2" + pIdReporte);
                this.establecerConexion();
                boolean existePersonaLista = false;
                while (itt.hasNext()) {
                    Hashtable datosPersona = (Hashtable) itt.next();
                    //TablaBD mTabla = new TablaBD("PERSONAS_REPORTADAS", "TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION,CODIGO_MOTIVO_V", this.sesionBD);
                    String consultaPersona =
                        "SELECT * FROM PERSONAS_REPORTADAS " + " WHERE TIPO_IDENTIFICACION = ?" +
                        " AND NUMERO_IDENTIFICACION = ?" + " AND CODIGO_MOTIVO_V = '16'";
                    //System.out.println("3" + (String)datosPersona.get("TIPO_IDENTIFICACION"));
                    //mTabla.asignarValorColumna("CODIGO_MOTIVO_V","16");
                    //mTabla.asignarValorColumna("TIPO_IDENTIFICACION",(String)datosPersona.get("TIPO_IDENTIFICACION"));
                    //System.out.println("4 " + (String)datosPersona.get("NUMERO_IDENTIFICACION"));
                    //mTabla.asignarValorColumna("NUMERO_IDENTIFICACION",(String)datosPersona.get("NUMERO_IDENTIFICACION"));
                    try {
                        PreparedStatement ps = this.sesionBD.prepareStatement(consultaPersona);
                        ps.setString(1, (String) datosPersona.get("TIPO_IDENTIFICACION"));
                        ps.setString(2, (String) datosPersona.get("NUMERO_IDENTIFICACION"));
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            existePersonaLista = true;
                            //Crear historico por cada persona asociada al reporte
                            HistoricoPersonasReportadasEJB hprl = this.getHistoricoPersonasReportadasEJB();
                            Hashtable datosHistoricoPR = new Hashtable();

                            datosHistoricoPR.put("CODIGO_MOTIVO_V", rs.getString("CODIGO_MOTIVO_V"));
                            //System.out.println("11 a");
                            datosHistoricoPR.put("TIPO_IDENTIFICACION", rs.getString("TIPO_IDENTIFICACION"));
                            //System.out.println("11 b");
                            datosHistoricoPR.put("NUMERO_IDENTIFICACION", rs.getString("NUMERO_IDENTIFICACION"));
                            //System.out.println("12");
                            try {
                                datosHistoricoPR.put("ID_REPORTE", new Integer(rs.getInt("ID_REPORTE")));
                            } catch (Exception e) {
                            }
                            try {
                                datosHistoricoPR.put("APELLIDOS_RAZON_SOCIAL", rs.getString("APELLIDOS_RAZON_SOCIAL"));
                            } catch (Exception e) {
                            }
                            try {
                                datosHistoricoPR.put("NOMBRES_RAZON_COMERCIAL",
                                                     rs.getString("NOMBRES_RAZON_COMERCIAL"));
                            } catch (Exception e) {
                            }
                            try {
                                datosHistoricoPR.put("ALIAS", rs.getString("ALIAS"));
                            } catch (Exception e) {
                            }
                            try {
                                datosHistoricoPR.put("COMENTARIO", rs.getString("COMENTARIO"));
                            } catch (Exception e) {
                            }
                            try {
                                datosHistoricoPR.put("FECHA_INGRESO", rs.getTimestamp("FECHA_INGRESO"));
                            } catch (Exception e) {
                            }
                            try {
                                datosHistoricoPR.put("TIPO_REPORTE", rs.getString("TIPO_REPORTE"));
                            } catch (Exception e) {
                            }
                            try {
                                datosHistoricoPR.put("ROS", rs.getString("ROS"));
                            } catch (Exception e) {
                            }
                            //System.out.println("13");
                            //datosHistoricoPR.put("USUARIO_ACTUALIZACION", (Long)datosPersona.get("USUARIO_ACTUALIZACION"));
                            datosHistoricoPR.put("USUARIO_ACTUALIZACION", usuarioAct);
                            //System.out.println("14");
                            //datosHistoricoPR.put("FECHA_ACTUALIZACION", (Timestamp)datosPersona.get("FECHA_ACTUALIZACION"));
                            datosHistoricoPR.put("FECHA_ACTUALIZACION", fechaAct);
                            //System.out.println("15");
                            datosHistoricoPR.put("ACCION", "ACTUALIZAR");
                            //System.out.println("16");
                            //OB           HistoricoPersonasReportadasLocal local = hprl.create( datosHistoricoPR );
                            //System.out.println("17");
                        }
                        rs.close();
                        ps.close();
                        /*if ( mTabla.buscarPK() ){System.out.println("5");
                            existePersonaLista = true;
                            mTabla.consultarDatos();
                        } else{System.out.println("6");
                            existePersonaLista = false;
                        }*/

                    } catch (Exception noEnLista) {
                        //existePersonaLista = false;
                        throw new EJBException("ReporteEJB|AdicionarPersonaLista: " + noEnLista.getMessage());
                    }
                    /*mTabla.asignarValorColumna("ID_REPORTE",pIdReporte);System.out.println("7: " + (String)datosPersona.get("APELLIDOS_RAZON_SOCIAL"));
                    mTabla.asignarValorColumna("APELLIDOS_RAZON_SOCIAL",(String)datosPersona.get("APELLIDOS_RAZON_SOCIAL"));
                    System.out.println("8: " + (String)datosPersona.get("NOMBRES_RAZON_COMERCIAL"));
                    mTabla.asignarValorColumna("NOMBRES_RAZON_COMERCIAL",(String)datosPersona.get("NOMBRES_RAZON_COMERCIAL"));
                    System.out.println("9: ");
                    mTabla.asignarValorColumna("FECHA_INGRESO",new Timestamp(System.currentTimeMillis()));
                    */
                    if (!existePersonaLista) { //System.out.println("10 ");
                        //mTabla.insertarDatos();
                        //Crear persona en la lista
                        String insercionPersona =
                            "INSERT INTO PERSONAS_REPORTADAS( " +
                            "CODIGO_MOTIVO_V, TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION, " +
                            "ID_REPORTE, APELLIDOS_RAZON_SOCIAL, NOMBRES_RAZON_COMERCIAL, " + "FECHA_INGRESO ) " +
                            " VALUES ( ?, ? , ?, ?, ?, ?, ? )";

                        PreparedStatement psInsercion = this.sesionBD.prepareStatement(insercionPersona);
                        psInsercion.setString(1, "16");
                        psInsercion.setString(2, (String) datosPersona.get("TIPO_IDENTIFICACION"));
                        psInsercion.setString(3, (String) datosPersona.get("NUMERO_IDENTIFICACION"));
                        psInsercion.setInt(4, pIdReporte.intValue());
                        psInsercion.setString(5, (String) datosPersona.get("APELLIDOS_RAZON_SOCIAL"));
                        psInsercion.setString(6, (String) datosPersona.get("NOMBRES_RAZON_COMERCIAL"));
                        psInsercion.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

                        psInsercion.execute();
                        psInsercion.close();
                    } else { //System.out.println("11");
                        //Actualizar registro de personas reportadas
                        /*HistoricoPersonasReportadasHome hprl = this.getHistoricoPersonasReportadasHome();
                        Hashtable datosHistoricoPR = new Hashtable();

                        datosHistoricoPR.put("CODIGO_MOTIVO_V", (String)mTabla.traerValorColumna("CODIGO_MOTIVO_V"));
                        System.out.println("11 a");
                        datosHistoricoPR.put("TIPO_IDENTIFICACION", (String)mTabla.traerValorColumna("TIPO_IDENTIFICACION"));
                        System.out.println("11 b");
                        datosHistoricoPR.put("NUMERO_IDENTIFICACION", (String)mTabla.traerValorColumna("NUMERO_IDENTIFICACION"));
                        System.out.println("12");
                        try{
                            datosHistoricoPR.put("ID_REPORTE", (Integer)mTabla.traerValorColumna("ID_REPORTE"));
                        } catch (Exception e){}
                        try{
                            datosHistoricoPR.put("APELLIDOS_RAZON_SOCIAL", (String)mTabla.traerValorColumna("APELLIDOS_RAZON_SOCIAL"));
                        } catch (Exception e){}
                        try{
                            datosHistoricoPR.put("NOMBRES_RAZON_COMERCIAL", (String)mTabla.traerValorColumna("NOMBRES_RAZON_COMERCIAL"));
                        } catch (Exception e){}
                        try{
                            datosHistoricoPR.put("ALIAS", (String)mTabla.traerValorColumna("ALIAS"));
                        } catch (Exception e){}
                        try{
                            datosHistoricoPR.put("COMENTARIO", (String)mTabla.traerValorColumna("COMENTARIO"));
                        } catch (Exception e){}
                        try{
                            datosHistoricoPR.put("FECHA_INGRESO", (Timestamp)mTabla.traerValorColumna("FECHA_INGRESO"));
                        } catch (Exception e){}
                        try{
                            datosHistoricoPR.put("TIPO_REPORTE", (String)mTabla.traerValorColumna("TIPO_REPORTE"));
                        } catch (Exception e){}
                        try{
                            datosHistoricoPR.put("ROS", (String)mTabla.traerValorColumna("ROS"));
                        } catch (Exception e){}
                        System.out.println("13");
                        //datosHistoricoPR.put("USUARIO_ACTUALIZACION", (Long)datosPersona.get("USUARIO_ACTUALIZACION"));
                        datosHistoricoPR.put("USUARIO_ACTUALIZACION", usuarioAct);
                        System.out.println("14");
                        //datosHistoricoPR.put("FECHA_ACTUALIZACION", (Timestamp)datosPersona.get("FECHA_ACTUALIZACION"));
                        datosHistoricoPR.put("FECHA_ACTUALIZACION", fechaAct);
                        System.out.println("15");
                        datosHistoricoPR.put("ACCION", "ACTUALIZAR");
                        System.out.println("16");
                        HistoricoPersonasReportadasLocal local = hprl.create( datosHistoricoPR );
                        System.out.println("17");
                        mTabla.actualizarDatos();
                        System.out.println("18");*/
                        String actPersona =
                            "UPDATE PERSONAS_REPORTADAS " + " SET APELLIDOS_RAZON_SOCIAL = ?, " +
                            " NOMBRES_RAZON_COMERCIAL = ?, " + " ID_REPORTE = ?, " + " FECHA_INGRESO = ? " +
                            " WHERE TIPO_IDENTIFICACION = ? " + " AND NUMERO_IDENTIFICACION = ? " +
                            " AND CODIGO_MOTIVO_V = '16' ";
                        PreparedStatement psAct = this.sesionBD.prepareStatement(actPersona);
                        psAct.setString(1, (String) datosPersona.get("APELLIDOS_RAZON_SOCIAL"));
                        psAct.setString(2, (String) datosPersona.get("NOMBRES_RAZON_COMERCIAL"));
                        psAct.setInt(3, pIdReporte.intValue());
                        psAct.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        psAct.setString(5, (String) datosPersona.get("TIPO_IDENTIFICACION"));
                        psAct.setString(6, (String) datosPersona.get("NUMERO_IDENTIFICACION"));

                        psAct.executeUpdate();

                        psAct.close();
                    }
                }
                //this.cerrarConexion();
            }
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|AdicionarPersonaLista: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public Integer actualizarReporte(Hashtable pDatos) throws SQLException {
        System.out.println("==> ReporteEJBBean|actualizarReporte...");
        try {
            Integer id = null;
            String codigoCargo = null;
            String codigoClaseReporte = null;
            String codigoEstadoReporte = null;
            String codigoTipoReporte = null;
            String justificacionInicial = null;
            String justificacionInicialA = null;
            String justificacionInicialC = null;
            String justificacionInicialB = null;
            Long usuarioCreacion = null;
            Timestamp fechaCreacion = null;
            Long usuarioActualizacion = null;
            Timestamp fechaActualizacion = null;
            String otroDocumentoSoporte = null;
            String otraFuenteInformacion = null;
            String numeroIdentificacion = null;
            String tipoIdentificacion = null;
            this.establecerConexion();

            TablaBD miTabla = new TablaBD("REPORTE", "ID", sesionBD);
            id = (Integer) pDatos.get("ID");
            miTabla.asignarValorColumna("ID", id);

            miTabla.consultarDatos();

            codigoCargo = (String) pDatos.get("CODIGO_CARGO");
            if (codigoCargo != null) {

                try {
                    TablaBD tabla = new TablaBD("CARGOS", "CODIGO", sesionBD);
                    tabla.asignarValorColumna("CODIGO", codigoCargo);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|ActualizarReporte: Cargo no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|ActualizarReporte 1: " + e.getMessage());
                }
            }

            codigoClaseReporte = (String) pDatos.get("CODIGO_CLASE_REPORTE_V");
            if (codigoClaseReporte != null) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(7));
                    tabla.asignarValorColumna("CODIGO", codigoClaseReporte);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|ActualizarReporte: Clase de reporte no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|ActualizarReporte 2: " + e.getMessage());
                }
                miTabla.asignarValorColumna("CODIGO_CLASE_REPORTE_V", codigoClaseReporte);
            }

            codigoEstadoReporte = (String) pDatos.get("CODIGO_ESTADO_REPORTE_V");
            if (codigoEstadoReporte != null) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(9));
                    tabla.asignarValorColumna("CODIGO", codigoEstadoReporte);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|ActualizarReporte: Estado de reporte no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|ActualizarReporte 3: " + e.getMessage());
                }
                miTabla.asignarValorColumna("CODIGO_ESTADO_REPORTE_V", codigoEstadoReporte);
            }
            codigoTipoReporte = (String) pDatos.get("CODIGO_TIPO_REPORTE_V");
            if (codigoTipoReporte != null) {
                try {
                    TablaBD tabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", sesionBD);
                    tabla.asignarValorColumna("TIPO_DATO", new Integer(4));
                    tabla.asignarValorColumna("CODIGO", codigoTipoReporte);
                    if (!tabla.buscarPK()) {
                        throw new EJBException("ReporteEJB|ActualizarReporte: Tipo de reporte no existe");
                    }
                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|ActualizarReporte 4: " + e.getMessage());
                }
                miTabla.asignarValorColumna("CODIGO_TIPO_REPORTE_V", codigoTipoReporte);
            }
            justificacionInicial = (String) pDatos.get("JUSTIFICACION_INICIAL");
            if (justificacionInicial != null) {
                try {
                    String[] justificaciones = this.partirJustificacionInicial(justificacionInicial);

                    justificacionInicialA = justificaciones[0];
                    justificacionInicialB = justificaciones[1];
                    justificacionInicialC = justificaciones[2];

                } catch (Exception e) {
                    throw new EJBException("ReporteEJB|ActualizarReporte 5 Justificacion: " + e.getMessage());
                }
            }
            usuarioActualizacion = (Long) pDatos.get("USUARIO_ACTUALIZACION");
            fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
            numeroIdentificacion = (String) pDatos.get("NUMERO_IDENTIFICACION");
            tipoIdentificacion = (String) pDatos.get("TIPO_IDENTIFICACION");

            if (justificacionInicial != null) {
                miTabla.asignarValorColumna("JUSTIFICACION_INICIAL", justificacionInicialA);
                miTabla.asignarValorColumna("JUSTIFICACION_INICIAL_B", justificacionInicialB);
                miTabla.asignarValorColumna("JUSTIFICACION_INICIAL_C", justificacionInicialC);
            }

            miTabla.asignarValorColumna("USUARIO_ACTUALIZACION", usuarioActualizacion);
            miTabla.asignarValorColumna("FECHA_ACTUALIZACION", fechaActualizacion);
            if (numeroIdentificacion != null) {
                miTabla.asignarValorColumna("NUMERO_IDENTIFICACION", numeroIdentificacion);
            }

            if (tipoIdentificacion != null) {
                miTabla.asignarValorColumna("TIPO_IDENTIFICACION", tipoIdentificacion);
            }

            try {
                otroDocumentoSoporte = (String) pDatos.get("OTRO_DOCUMENTO_SOPORTE");
                if (otroDocumentoSoporte != null) {
                    miTabla.asignarValorColumna("OTRO_DOCUMENTO_SOPORTE", otroDocumentoSoporte);
                }
            } catch (Exception er) {
            }

            // LOS SIGUIENTES CAMPOS SON LOS DEL ANALISIS DEL DUCC
            try {
                otraFuenteInformacion = (String) pDatos.get("OTRA_FUENTE_INFORMACION");
                if (otraFuenteInformacion != null) {
                    miTabla.asignarValorColumna("OTRA_FUENTE_INFORMACION", otraFuenteInformacion);
                }
            } catch (Exception er) {
            }

            try {
                String codigoTipoConsulta = (String) pDatos.get("CODIGO_TIPO_CONSULTA_V");
                if (codigoTipoConsulta != null) {
                    miTabla.asignarValorColumna("CODIGO_TIPO_CONSULTA_V", codigoTipoConsulta);
                }
            } catch (Exception er) {
            }

            try {
                String indagacion = (String) pDatos.get("INDAGACION");
                if (indagacion != null) {
                    miTabla.asignarValorColumna("INDAGACION", indagacion);
                }
            } catch (Exception er) {
            }

            try {
                Timestamp fechaAnalisis = (Timestamp) pDatos.get("FECHA_ANALISIS");
                if (fechaAnalisis != null) {
                    miTabla.asignarValorColumna("FECHA_ANALISIS", fechaAnalisis);
                }
            } catch (Exception er) {
            }

            miTabla.actualizarDatos();

            //this.sesionBD.commit();
            return id;
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|actualizarReporte: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public boolean limpiarGestion(Integer pIdReporte) throws SQLException {
        boolean exito = false;
        try {
            this.establecerConexion();
            String eliminarTransacciones = "DELETE TRANSACCIONES_REP " + "WHERE ID_REPORTE = " + pIdReporte;
            Statement st = this.sesionBD.createStatement();
            st.execute(eliminarTransacciones);
            String eliminarRespuestasB = "DELETE RTAS_BOOLEAN_REP " + "WHERE ID_REPORTE = " + pIdReporte;
            st.execute(eliminarRespuestasB);
            String eliminarRespuestasT = "DELETE RTAS_TEXT_REP " + "WHERE ID_REPORTE = " + pIdReporte;
            st.execute(eliminarRespuestasT);
            st.close();
            exito = true;
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|LimpiarGestion " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
        return exito;
    }

    public boolean limpiarAnalisis(Integer pIdReporte) throws SQLException {
        boolean exito = false;
        try {
            this.establecerConexion();

            String eliminarRespuestasB =
                "DELETE RTAS_BOOLEAN_REP " + "WHERE ID_REPORTE = " + pIdReporte +
                " AND ID_PREGUNTA IN(SELECT ID FROM PREGUNTAS_REP WHERE TIPO_PREGUNTA_V ='A')";
            Statement st = this.sesionBD.createStatement();
            st.execute(eliminarRespuestasB);

            String eliminarRespuestasT =
                "DELETE RTAS_TEXT_REP " + "WHERE ID_REPORTE = " + pIdReporte +
                " AND ID_PREGUNTA IN(SELECT ID FROM PREGUNTAS_REP WHERE TIPO_PREGUNTA_V ='A')";
            st.execute(eliminarRespuestasT);
            st.close();
            exito = true;
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|LimpiarAnalisis " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
        return exito;
    }

    private void borrarPersonas(Integer pIdReporte) throws SQLException {
        try {
            String eliminarPersonas = "DELETE PERSONAS_REP " + "WHERE ID_REPORTE = " + pIdReporte;
            Statement st = this.sesionBD.createStatement();
            st.execute(eliminarPersonas);
            st.close();
        } catch (Exception e) {
            throw new EJBException("ReporteEJB|LimpiarPersonas " + e.getMessage());
        }
    }

    private HistoricoPersonasReportadasEJB getHistoricoPersonasReportadasEJB() throws NamingException {
        Contextos ctx = new Contextos();
        HistoricoPersonasReportadasEJB hisPersonasReportadasEJB =
            (HistoricoPersonasReportadasEJB) ctx.getContext("HistoricoPersonasReportadasEJB");
        return hisPersonasReportadasEJB;
    }

    private void cerrarReportesCliente(Hashtable pDatos) throws SQLException {

        Integer pIdReporte = (Integer) pDatos.get("ID_REPORTE");
        Timestamp fechaActualizacion = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");

        String consulta =
            "SELECT TIPO_IDENTIFICACION, " + " NUMERO_IDENTIFICACION, " +
            " TO_CHAR(FECHA_CREACION, 'YYYY/MM/DD HH:MI:SS AM') FECHA_CREACION " + " FROM REPORTE " + " WHERE ID = " +
            pIdReporte + " " + " AND CODIGO_CLASE_REPORTE_V = '" + this.CLASE_CLIENTE + "'";

        Statement stConsulta = this.sesionBD.createStatement();
        ResultSet rsConsulta = stConsulta.executeQuery(consulta);

        if (rsConsulta == null) {
            return;
        }

        String tipoIdentificacion = null;
        String numeroIdentificacion = null;
        String fechaCreacion = null;
        int hallaResultados = 0;

        while (rsConsulta.next()) {
            tipoIdentificacion = rsConsulta.getString("TIPO_IDENTIFICACION");
            numeroIdentificacion = rsConsulta.getString("NUMERO_IDENTIFICACION");
            fechaCreacion = rsConsulta.getString("FECHA_CREACION");
            hallaResultados++;
        }
        rsConsulta.close();
        stConsulta.close();
        if (hallaResultados == 0) {

            return;
        }

        String actualizacion =
            "UPDATE REPORTE " + " SET CODIGO_ESTADO_REPORTE_V = ?, " + " FECHA_ACTUALIZACION = ? " +
            " WHERE TIPO_IDENTIFICACION = ? " + " AND NUMERO_IDENTIFICACION = ? " + " AND CODIGO_CLASE_REPORTE_V = ? " +
            " AND CODIGO_ESTADO_REPORTE_V = ? " + " AND FECHA_CREACION < ? ";

        PreparedStatement ps = this.sesionBD.prepareStatement(actualizacion);

        ps.setString(1, this.GESTIONADO);
        ps.setTimestamp(2, fechaActualizacion);
        ps.setString(3, tipoIdentificacion);
        ps.setString(4, numeroIdentificacion);
        ps.setString(5, this.CLASE_CLIENTE);
        ps.setString(6, this.ESTUDIO);
        ps.setTimestamp(7, new Timestamp(new Date(fechaCreacion).getTime()));

        ps.executeUpdate();
        ps.close();

    }

    private String[] partirJustificacionInicial(String pJustificacionInicial) {
        String[] justificaciones = new String[3];
        try {
            if (pJustificacionInicial.length() > 4000) {
                try {
                    justificaciones[1] = pJustificacionInicial.substring(4000, 8000);
                } catch (Exception menorOchoMil) {
                    justificaciones[1] = pJustificacionInicial.substring(4000);
                }
            }
            if (pJustificacionInicial.length() > 8000) {
                try {
                    justificaciones[2] = pJustificacionInicial.substring(8000, 12000);
                } catch (Exception menorDoceMil) {
                    justificaciones[2] = pJustificacionInicial.substring(8000);
                }
            }
            justificaciones[0] = pJustificacionInicial.substring(0, 4000);
        } catch (Exception menorCuatroMil) {
            justificaciones[0] = pJustificacionInicial.substring(0);
            justificaciones[1] = null;
            justificaciones[2] = null;
        }
        return justificaciones;
    }

    private String traerJustificacion(TablaBD pTabla) throws ExcepcionBD {
        String justificacionInicialA = (String) pTabla.traerValorColumna("JUSTIFICACION_INICIAL");
        String justificacionInicialB = (String) pTabla.traerValorColumna("JUSTIFICACION_INICIAL_B");
        String justificacionInicialC = (String) pTabla.traerValorColumna("JUSTIFICACION_INICIAL_C");
        if (justificacionInicialB != null) {
            justificacionInicialA += justificacionInicialB;
            if (justificacionInicialC != null) {
                justificacionInicialA += justificacionInicialC;
            }
        }
        return justificacionInicialA;
    }
    /* Ingrid Marcela Alonso 2007-02-23 nuevo metodo para pasar el estado de
  * los reportes asociados a un reporte ducc a estado Gestionado */
    public void actualizarReportesAsociados(Integer pIdReporte) throws SQLException {
        try {
            if (this.sesionBD == null) {
                this.establecerConexion();
            }

            ConsultaTablaEJB miConsulta = this.getConsultaTablaEJB();
            //    ConsultaTablaLocal miConsulta = consulta.create();
            int[] respuesta = { Types.VARCHAR };
            String parametroProceso = "(" + pIdReporte + ",?)";
            String salida = miConsulta.ejecutarProcedure("PR_CERRAR_REP_REL", parametroProceso, respuesta);
            if (!salida.equals("[null]")) {
                throw new Exception(salida);
            }
        } catch (Exception ex) {
            throw new EJBException("Reporte|actualizarReportesAsociados" + ex.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }
}
