package view.controllers;

import admin.seguridad.LogConsultaEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import javax.ejb.CreateException;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

@ManagedBean(name = "LogController")
@SessionScoped
public class LogController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private LogConsultaEJB logconsultaEJB;
    @ManagedProperty("#{ContenidoController}")
    private ContenidoController contenidoCtr;
    @ManagedProperty("#{ListavaloresController}")
    private ListavaloresController listValoresCtr;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Funciones funciones;
    private Collection logs;
    private Hashtable log;
    private Hashtable datosRegistro;
    private String condicion;
    private Collection procesos;
    private Collection logErrores;
    private String proceso;
    private Date fecha;
    private String fechaStr;
    private String usuario;
    private String consultaLog;
    private String msgLogCons;
    private String codUsuario;
    private int resLog;
    private FacesMessage message;
    private boolean dfault;
    private String msgRep;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        funciones = new Funciones();
        consultaLog = "";
        proceso = "";
        usuario = datosRegistro.get("CODIGO_USUARIO").toString();
        condicion = "";
        dfault = true;
        buscarTiposProcesos();
        limpiar();
    }

    public void limpiar() {
        fechaStr = "";
        proceso = null;
        codUsuario = "";
    }

    public void busquedaLogs() {
        condicion = "";
        facContex = FacesContext.getCurrentInstance();
        logs = null;
        //if (fecha != null || proceso != null || !codUsuario.isEmpty()) {
        condicion = " WHERE 1=1 ";
        if (fecha != null) {
            fechaStr = funciones.dateToString(fecha);
            condicion += " AND TRUNC(INICIO)= '" + fechaStr + "'";
        }
        if (proceso != null) {
            condicion += " AND PROCESO = '" + proceso + "'";
        }
        if (!codUsuario.isEmpty()) {
            condicion += " AND USUARIO LIKE ('%" + codUsuario.toUpperCase() + "%')";
        }
        /*} else {
            message =
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: ",
                                 "Debe ingresar al menos un parametro de busqueda");
            facContex.addMessage(null, message);
        }*/
        System.out.println("condicion: " + condicion);
        //if (!condicion.isEmpty()) {
        dfault = false;
        try {
            logs = consTablaEJB.consultarTabla(0, 0, "V_LOG_PROCESOS", condicion);
            System.out.println("logs: " + logs);
            Iterator iter = logs.iterator();
            while (iter.hasNext()) {
                Hashtable hTemp = (Hashtable) iter.next();
                String fecha = (String) hTemp.get("INICIO");
                String sFin = (String) hTemp.get("FIN");
                hTemp.replace("INICIO", fecha.substring(0, 16).replace("-", "/"));
                Date inicio = null;
                Date fin = null;
                try {
                    if (!fecha.equals("null")) {
                        inicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fecha);
                    }
                    if (!sFin.equals("null")) {
                        fin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sFin);
                    }
                } catch (Exception e) {
                    System.out.println("Error en conversión fecha: " + e.getMessage());
                }
                String duracion = "0";
                if (inicio != null && fin != null) {
                    long calculo = fin.getTime() - inicio.getTime();
                    if (calculo == 0l) {
                        duracion = "< 1 seg";
                    }
                    if (calculo > 0l) {
                        long horas = (calculo / 3600000l);
                        long minutos = (calculo % 3600000l) / 60000l;
                        long segundos = ((calculo % 3600000l) % 60000l) / 1000l;
                        duracion = horas + "h " + minutos + "m " + segundos + "s";
                    }
                }
                if (fin == null) {
                    duracion = "No ha terminado";
                }
                hTemp.put("DURACION", duracion);
            }
        } catch (Exception e) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: ", "Error aconsultando V_LOG_PROCESOS");
            facContex.addMessage(null, message);
            System.out.println("Error al hacer la busqueda de logs: condicion: " + condicion);
        }
        //}
    }

    private void buscarTiposProcesos() {
        facContex = FacesContext.getCurrentInstance();
        try {
            procesos = consTablaEJB.consultarTabla(0, 0, "V_LISTA_VALORES", " WHERE TIPO_DATO = '20' ");
        } catch (Exception e) {
            message =
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: ",
                                 "LogController|buscarTiposProcesos: Error consultando V_LISTA_VALORES");
            facContex.addMessage(null, message);
            System.out.println("LogController|buscarTiposProcesos: Error");
            e.printStackTrace();
        }
    }

    public void reportesLog() {
        System.out.println("LogController|buscarErroresLog|log: " + log);
        msgRep = "";
        if (log != null) {
            msgRep += log.get("MENSAJE") + " \n ";
            try {
                Integer limite = listValoresCtr.getLimiteRes();
                logErrores = consTablaEJB.consultarTabla(0, 0, "LOG_ERRORES", " WHERE ROWNUM < "+limite+" AND ID_PROCESO = " + log.get("ID_PROCESO"));
            } catch (Exception e) {
                System.out.println("LogController|reportesLog: Error consultando LOG_ERRORES id proceso = " +
                                   log.get("ID_PROCESO"));
                e.printStackTrace();
                msgRep =
                    "LogController|reportesLog: Error consultando LOG_ERRORES id proceso = " + log.get("ID_PROCESO");
            }
        } else {
            msgRep = "LogController|reportesLog: Error log = null";
        }
        PrimeFaces.current().executeScript("PF('dlgvRepLogs').show()");
    }

    public void logConsulta() {
        FacesContext facContex = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facContex.getExternalContext().getRequest();
        String ti = "", ni = "0", tp = "", np = "";
        Integer tb = 2, ttx = 1;
        Long codUsuario = new Long(usuario);
        String dominioNt = (String) datosRegistro.get("DOMINIO_NT");
        String usuarioNt = (String) datosRegistro.get("USUARIO_NT");
        String nombrePc = request.getRemoteAddr();
        String canal = contenidoCtr.getUrl();
        Calendar fechaActual = Calendar.getInstance();
        Hashtable datosLogConsulta = new Hashtable();
        datosLogConsulta.put("USUARIO", codUsuario);
        datosLogConsulta.put("CANAL", canal);
        datosLogConsulta.put("NOMBREPC", nombrePc);
        datosLogConsulta.put("FECHAEJECUCION", fechaActual);
        datosLogConsulta.put("QUERY", consultaLog);
        datosLogConsulta.put("DOMINIORED", dominioNt);
        datosLogConsulta.put("TIPOID", ti);
        datosLogConsulta.put("NUMEROID", ni);
        datosLogConsulta.put("TIPOPRODUCTO", tp);
        datosLogConsulta.put("NUMEROPRODUCTO", np);
        datosLogConsulta.put("TIPOBUSQUEDA", tb);
        datosLogConsulta.put("TIPOTRANSACCION", ttx);
        datosLogConsulta.put("USUARIONT", usuarioNt);
        datosLogConsulta.put("RESULTADOTX", resLog);
        datosLogConsulta.put("DESCRIPCIONRECHAZO", msgLogCons != null ? msgLogCons : "");
        try {
            logconsultaEJB.create(datosLogConsulta);
        } catch (CreateException e) {
            System.out.println("FrmpreinusualController|logConsulta: " + e.getMessage());
        }
        consultaLog = "";
    }
    public void consultaInicial() {
    	fecha=new Date();
    	busquedaLogs();
    	
    }

    public void setLog(Hashtable logIn) {
        log = logIn;
    }

    public Hashtable getLog() {
        return log;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicionIn) {
        condicion = condicionIn;
    }

    public Collection getLogs() {
        return logs;
    }

    public void setLogs(Collection logsIn) {
        logs = logsIn;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String procesoIn) {
        proceso = procesoIn;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fechaIn) {
        fecha = fechaIn;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuarioIn) {
        usuario = usuarioIn;
    }

    public void setProcesos(Collection procesosIn) {
        procesos = procesosIn;
    }

    public Collection getProcesos() {
        return procesos;
    }

    public FacesMessage getMessage() {
        return message;
    }

    public void setMessage(FacesMessage messageIn) {
        message = messageIn;
    }

    public String getMsgRep() {
        return msgRep;
    }

    public void setMsgRep(String msgRepIn) {
        msgRep = msgRepIn;
    }

    public boolean getDfault() {
        return dfault;
    }

    public String getFechaStr() {
        return fechaStr;
    }

    public void setContenidoCtr(ContenidoController contenidoCtr) {
        this.contenidoCtr = contenidoCtr;
    }

    public ContenidoController getContenidoCtr() {
        return contenidoCtr;
    }

    public void setConsultaLog(String consultaLog) {
        this.consultaLog = consultaLog;
    }

    public String getConsultaLog() {
        return consultaLog;
    }

    public void setMsgLogCons(String msgLogCons) {
        this.msgLogCons = msgLogCons;
    }

    public String getMsgLogCons() {
        return msgLogCons;
    }

    public void setResLog(int resLog) {
        this.resLog = resLog;
    }

    public int getResLog() {
        return resLog;
    }

    private String dateToString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        if (fecha != null) {
            return format.format(fecha);
        }
        return "";
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setLogErrores(Collection logErrores) {
        this.logErrores = logErrores;
    }

    public Collection getLogErrores() {
        return logErrores;
    }

    public void setListValoresCtr(ListavaloresController listValoresCtr) {
        this.listValoresCtr = listValoresCtr;
    }

    public ListavaloresController getListValoresCtr() {
        return listValoresCtr;
    }

}
