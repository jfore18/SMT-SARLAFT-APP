package view.controllers;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;
import baseDatos.Consulta;
import baseDatos.ConsultaEJB;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import java.util.Date;
import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.naming.InitialContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.primefaces.PrimeFaces;

import presentacion.FacadeEJB;

import transaccion.FiltroEJB;

@ManagedBean(name = "ConsultarfiltrosController")
@SessionScoped
public class ConsultarfiltrosController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private ConsultaEJB consultaEJB;
    @EJB
    private FacadeEJB facadeEJB;
    @EJB
    private FiltroEJB filtroEJB;
    @ManagedProperty("#{LogController}")
    private LogController logCtr;
    @ManagedProperty("#{ListavaloresController}")
    private ListavaloresController listavaloresCtr;
    private Funciones funciones;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private HttpServletRequest request;
    private Collection listEstadosFilt;
    private Collection listClientes;
    private Collection listFiltros;
    private Collection listTiposId;
    private Collection listHistFilt;
    private Collection listRegiones;
    private Collection listZonas;
    private List<Hashtable> listFiltrosSel;
    private Hashtable datosRegistro;
    private Hashtable clienteSel;
    private Hashtable productoSel;
    private FacesMessage fcMsg;
    private String msg;
    private String tipoIdentiFilt;
    private String identiFilt;
    private String nombreFilt;
    private String estadoFilt;
    private String consultaFilt;
    private String codPerfil;
    private String codCargo;
    private String filtro;
    private String justFilt;
    private String descJust;
    private String proceso;
    private String peticion;
    private String region;
    private String zona;
    private String oficina;
    private String condicion;
    private String valor;
    private String nivel;
    private String perfil;
    private Date fechaAprob;
    private boolean continuar;
    private boolean disabled;
    private boolean disabledR;
    private boolean disabledZ;
    private boolean disabledD;
    private String tipo_cargo;
    private boolean confirmaFiltro;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        perfil = (String) datosRegistro.get("CODIGO_PERFIL");
        tipo_cargo=(String) datosRegistro.get("CODIGO_TIPO_CARGO");
        funciones = new Funciones();
        peticion = "";
        fechaAprob = null;
        disabledR=false;
        disabledZ=false;
    }

    /*
     * iniciar. Metodo ejecutados desde frmConsultaFiltrosGral con pet="FILTROS_GRAL"
     * Metodo ejecutado desde ConsultaFiltrosSupervisor con pet="FILTROS SUPERVISOR"
     * Metodo ejecutado desde ConsultaFiltros con pet="FILTROS"
     * */
    
    public void iniciar(String pet) {
        peticion = pet;
        limpiarCampos();
        disabledD=false;
        if (peticion.equals("FILTROS")) {
            nivel = "41";
            //estadoFilt = "";
        }
        if (peticion.equals("FILTROS_GRAL")) {
            nivel = "42";
            llenarListasProcesoFiltrosGral();
            //estadoFilt = "";
            if (perfil.equals("3")) {
                region = datosRegistro.get("CODIGO_REGION").toString();
                consultarZonas();
                zona = datosRegistro.get("CODIGO_ZONA").toString();
                disabledR=true;
                disabledZ=true;
            }
            if (perfil.equals("4")) {
                region = datosRegistro.get("CODIGO_REGION").toString();
                consultarZonas();
                disabledR=true;
                disabledZ=false;
            }
            if (perfil.equals("5")) {
                disabledR=false;
                disabledZ=false;
            }
        }
        if (peticion.equals("FILTROS_SUPERVISOR")) {
        	nivel = "43";
            
            if (perfil.equals("3")) {
                region = datosRegistro.get("CODIGO_REGION").toString();
                consultarZonas();
                zona = datosRegistro.get("CODIGO_ZONA").toString();
                disabledR=true;
                disabledZ=true;
            }
            if (perfil.equals("4")) {
                region = datosRegistro.get("CODIGO_REGION").toString();
                consultarZonas();
                disabledR=true;
                disabledZ=false;
                
            }
            if (perfil.equals("5")) {
                disabledR=false;
                disabledZ=false;
            }
            if(tipo_cargo.equals("B")) 
            {
            	disabledD=true;
            }
        }

        disabled = true;
        if (condicion != null && !condicion.isEmpty() ) {
            disabled = false;
        }
        listClientes = null;
        fechaAprob = null;
        llenarListas();
        if (peticion.equals("FILTROS_SUPERVISOR")) {
        	estadoFilt = "0";
        
        }
  
    }
    /*
     * limpiarCampos. Metodo ecargado de inicializar todos los campos que son criterios de busqueda
     * 
     */
	private void limpiarCampos() {

		tipoIdentiFilt = null;
		identiFilt = null;
		nombreFilt = null;
		estadoFilt = null;
		condicion = null;
		region = null;
		zona = null;
		oficina = null;
		fechaAprob = null;
		valor = null;
		
	}

    private void llenarListas() {
        System.out.println("listEstadosFilt: " + listEstadosFilt);
        if (listEstadosFilt == null || listEstadosFilt.isEmpty()) {
            listEstadosFilt = new ArrayList();
            Hashtable elem = new Hashtable();
            elem.put("CODIGO", "0");
            elem.put("DESCRIPCION", "Pendiente");
            listEstadosFilt.add(elem);
            Hashtable elem2 = new Hashtable();
            elem2.put("CODIGO", "1");
            elem2.put("DESCRIPCION", "Aprobado");
            listEstadosFilt.add(elem2);
            Hashtable elem3 = new Hashtable();
            elem3.put("CODIGO", "2");
            elem3.put("DESCRIPCION", "Inactivo");
            listEstadosFilt.add(elem3);
            Hashtable elem4 = new Hashtable();
            elem4.put("CODIGO", "3");
            elem4.put("DESCRIPCION", "Rechazado");
            listEstadosFilt.add(elem4);
        }
        try {
            listTiposId = consTablaEJB.consultarTabla(0, 0, "V_TIPOS_DOCUMENTO", null);
        } catch (Exception e) {
            System.out.println("ConsultarfiltrosController|llenarListas:V_TIPOS_DOCUMENTO: " + e.getMessage());
        }
    }

    private void llenarListasProcesoFiltrosGral() {
        try {
            listRegiones = consTablaEJB.consultarTabla(0, 0, "V_REGION", null);
        } catch (Exception e) {
            System.out.println("ConsultarfiltrosController|llenarListasProcesoFiltrosGral:V_REGION:" + e.getMessage());
        }
    }

    public void consultarZonas() {
        try {
            String condi = " WHERE CODIGO_REGION_V = '" + region + "'";
            listZonas = consTablaEJB.consultarTabla(0, 0, "V_ZONAS", condi);
        } catch (Exception e) {
            System.out.println("ConsultarfiltrosController|consultarZonas:V_ZONAS:" + e.getMessage());
        }
    }

    public void buscarFiltros() {
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        codCargo = (String) datosRegistro.get("CODIGO_CARGO");
        codPerfil = (String) datosRegistro.get("CODIGO_PERFIL");
        String tipoCargo= (String) datosRegistro.get("CODIGO_TIPO_CARGO");
        
        consultaFilt = "";
        if (peticion.equals("FILTROS")) {
            construirConsultaFiltro();
        }
        if (peticion.equals("FILTROS_GRAL") || peticion.equals("FILTROS_SUPERVISOR")) {
            construirConsultaFiltroGral();
        }
        //if (!consultaFilt.isEmpty()) {
        if (!disabled && valor.isEmpty()) {
            msg = "Debe ingresar el valor para la condición";
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            FacesContext.getCurrentInstance().addMessage(null, fcMsg);
        } else {
            System.out.println("consultaFilt: "+consultaFilt);
            Consulta miConsulta = null;
                       
            if (tipoCargo.equals("N")) {

            	miConsulta = consultaEJB.realizarConsultaTipoCargo(codCargo, nivel);
			} else {
			   miConsulta = consultaEJB.realizarConsulta(codCargo, nivel);
			}
            listClientes = facadeEJB.ejecutarConsultaGeneral(consultaFilt, miConsulta, 100);
        }
        /* } else {
            msg = "Debe ingresar al menos un parametro de busqueda";
            fcMsg = new FacesMessage("Alerta: ", msg);
            FacesContext.getCurrentInstance().addMessage(null, fcMsg);
        }*/
        System.out.println("listClientes: " + listClientes);
    }

    private void construirConsultaFiltro() {
        if (tipoIdentiFilt != null && !tipoIdentiFilt.isEmpty()) {
            consultaFilt += " AND T.TIPO_IDENTIFICACION = '" + tipoIdentiFilt + "'";
        }
        if (identiFilt != null && !identiFilt.isEmpty()) {
            consultaFilt += " AND T.NUMERO_IDENTIFICACION LIKE '%" + identiFilt + "%'";
        }
        if (nombreFilt != null && !nombreFilt.isEmpty()) {
            consultaFilt += " AND UPPER(NOMBRE_RAZON_SOCIAL) LIKE '%" + nombreFilt.toUpperCase() + "%'";
        }
        if (estadoFilt != null && !estadoFilt.isEmpty()) {
            consultaFilt += " AND P.ESTADO = '" + estadoFilt + "'";
        }
        if (oficina != null && !oficina.isEmpty()) {
            consultaFilt +=
                " AND (T.CODIGO_OFICINA= '" + oficina + "')";
        }
    }

    private void construirConsultaFiltroGral() {
        if (tipoIdentiFilt != null && !tipoIdentiFilt.isEmpty()) {
            consultaFilt += " AND V.TIPO_IDENTIFICACION = '" + tipoIdentiFilt + "'";
        }
        if (identiFilt != null && !identiFilt.isEmpty()) {
            consultaFilt += " AND V.NUMERO_IDENTIFICACION LIKE '%" + identiFilt + "%'";
        }
        if (nombreFilt != null && !nombreFilt.isEmpty()) {
            consultaFilt += " AND UPPER(V.NOMBRE_CLIENTE) LIKE '%" + nombreFilt.toUpperCase() + "%'";
        }
        if (estadoFilt != null && !estadoFilt.isEmpty()) {
            consultaFilt += " AND E.ESTADO = '" + estadoFilt + "'";
        }
        if (region != null && !region.isEmpty()) {
            consultaFilt += " AND CODIGO_REGION = '" + region + "'";
        }
        if (zona != null && !zona.isEmpty()) {
            consultaFilt += " AND CODIGO_ZONA = '" + zona + "'";
        }
        if (oficina != null && !oficina.isEmpty()) {
            consultaFilt +=
                " AND (V.CODIGO_UNIDAD_NEGOCIO LIKE '%" + oficina + "%' OR UPPER(V.NOMBRE_UNIDAD_NEGOCIO) LIKE '%" +
                oficina.toUpperCase() + "%')";
        }
		if (valor != null && !valor.isEmpty()) {
			String condi = condicion.equals("MENOR_A") ? "<=" : ">=";
			if (condi.equals("<=")) {
				consultaFilt += " AND V.VALOR_MAXIMO " + condi + " " + valor;
			} else {
				consultaFilt += " AND V.VALOR_MINIMO " + condi + " " + valor;

			}
		}
		if(fechaAprob !=null) {
			String fechaStr;
			fechaStr = funciones.dateToString(fechaAprob);
			consultaFilt +="AND TRUNC(FECHA_SUPERVISOR)='" + fechaStr + "'";
			
		}
    }

    public void habilitaValor() {
        disabled = true;
        if (condicion != null && !condicion.isEmpty() ) {
            disabled = false;
        }
        valor = "";
    }
    /*
     * Metodo ejecutado al seleccionar un cliente en las diferentes pantallas de consultar filtros
     * */
    public void irAfiltros() {
        filtro = "";
        justFilt = "";
        confirmaFiltro=false;
        String nombre = "";
        if (clienteSel != null) {
            if (clienteSel.get("NOMBRE") != null) {
                nombre = clienteSel.get("NOMBRE").toString();
                if (nombre.equals("SIN NOMBRE")) {
                    clienteSel.replace("NOMBRE", "NO HALLADO EN CRM");
                }
            }
            if (clienteSel.get("NOMBRE_CLIENTE") != null) {
                nombre = clienteSel.get("NOMBRE_CLIENTE").toString();
                if (nombre.equals("SIN NOMBRE")) {
                    clienteSel.replace("NOMBRE_CLIENTE", "NO HALLADO EN CRM");
                }
            }
        }
        filtrosCliente();
        if (listFiltros != null && !listFiltros.isEmpty()) {
            logCtr.setResLog(1);
            logCtr.setMsgLogCons(" ");
            if (peticion.equals("FILTROS")) {
                Iterator itFilt = listFiltros.iterator();
                while (itFilt.hasNext()) {
                    Hashtable htFilt = (Hashtable) itFilt.next();
                    String ti = (String) htFilt.get("TIPO_IDENTIFICACION");
                    String ni = (String) htFilt.get("NUMERO_IDENTIFICACION");
                    String tp = (String) htFilt.get("TIPO_PRODUCTO");
                    String cp = (String) htFilt.get("CODIGO_PRODUCTO");
                    String nn = (String) htFilt.get("NUMERO_NEGOCIO");
                    String consultaFiltros =
                        "WHERE ID IN( " + " SELECT MAX(ID) " + " FROM FILTROS " + " WHERE NUMERO_IDENTIFICACION = '" +
                        ni + "'" + " AND TIPO_IDENTIFICACION = '" + ti + "'" + " AND NUMERO_NEGOCIO = '" + nn + "'" +
                        " AND CODIGO_PRODUCTO = '" + cp + "'" + " AND CODIGO_CARGO = '" + codCargo + "'" +
                        " GROUP BY CODIGO_PRODUCTO, NUMERO_NEGOCIO, CODIGO_CARGO)";
                    Collection filtrosExistentes = null;
                    try {
                        filtrosExistentes = consTablaEJB.consultarTabla(0, 0, "V_FILTROS", consultaFiltros);
                    } catch (Exception e) {
                        System.out.println("ConsultarfiltrosController|irAfiltros:V_FILTROS: " + e.getMessage());
                    }
                    String id = "";
                    String just = "";
                    String condicion = "";
                    String vig_desde = "";
                    String confirmar = "";
                    String estado = "-1";
                    String desc_estado = "";
                    if (filtrosExistentes != null) {
                        Iterator itInfoFilt = filtrosExistentes.iterator();
                        while (itInfoFilt.hasNext()) {
                            Hashtable htInfoFilt = (Hashtable) itInfoFilt.next();
                            id = (String) htInfoFilt.get("ID");
                            just = (String) htInfoFilt.get("JUSTIFICACION");
                            condicion = ((String) htInfoFilt.get("CONDICION")).trim();
                            condicion = funciones.condicionFiltro(condicion);
                            vig_desde = (String) htInfoFilt.get("VIGENTE_DESDE");
                            confirmar = (String) htInfoFilt.get("CONFIRMAR");
                            estado = (String) htInfoFilt.get("ESTADO");
                            desc_estado = (String) htInfoFilt.get("DESCRIPCION_ESTADO");
                        }
                    }
                    htFilt.put("ID", id);
                    htFilt.put("JUSTIFICACION", just);
                    htFilt.put("CONDICION", condicion);
                    htFilt.put("VIGENTE_DESDE", vig_desde);
                    htFilt.put("CONFIRMAR", confirmar);
              
                    if (confirmar.equals("1")) {
                    	
                    	confirmaFiltro=true;
                    }
                  
                    htFilt.put("ESTADO", estado);
                    htFilt.put("DESCRIPCION_ESTADO", desc_estado);

                    htFilt = funciones.quitarNull(htFilt);
                }
            } else if (peticion.equals("FILTROS_SUPERVISOR")) {
                Iterator itFilt = listFiltros.iterator();
                while (itFilt.hasNext()) {
                    Hashtable htFilt = (Hashtable) itFilt.next();
                    htFilt = funciones.quitarNull(htFilt);
                    htFilt.replace("FECHA_CREACION", funciones.formatoFecha(htFilt.get("FECHA_CREACION")));
                    htFilt.replace("VALOR_MINIMO", funciones.formatoValor(htFilt.get("VALOR_MINIMO")));
                    htFilt.replace("VALOR_MAXIMO", funciones.formatoValor(htFilt.get("VALOR_MAXIMO")));
                    String filtro = "";
                    if (htFilt.get("OPERADOR").equals("#")) {
                        filtro = "ENTRE " + htFilt.get("VALOR_MINIMO") + " Y " + htFilt.get("VALOR_MAXIMO");
                    } else if (htFilt.get("OPERADOR").equals("<")) {
                        filtro = "MENOR A " + htFilt.get("VALOR_MAXIMO");
                    }
                    htFilt.replace("CONDICION", filtro);
                }
            }
        } else {
            logCtr.setResLog(2);
            logCtr.setMsgLogCons("Cliente no productos");
        }
        logCtr.logConsulta();

        System.out.println("listFiltros: " + listFiltros);
      
    }

    private String buscarCodigoRegion(String valor, String op) {
        try {
            String consulta = "SELECT * FROM V_REGION WHERE DESC_CORTA = '" + valor + "'";
            Collection res = consTablaEJB.consultarTabla(0, 0, null, consulta);
            Iterator it = res.iterator();
            if (it.hasNext()) {
                Hashtable ht = (Hashtable) it.next();
                String codigo = ht.get("CODIGO").toString();
                return codigo;
            }
        } catch (Exception e) {
            System.out.println("ConsultarfiltrosController|buscarCodigo: " + e.getMessage());
        }
        return valor;
    }

    private String buscarCodigoZona(String codReg, String valor, String op) {
        try {
            String consulta =
                "SELECT * FROM ZONAS WHERE NOMBRE_CORTO = '" + valor + "' AND CODIGO_REGION_V = '" + codReg + "'";
            Collection res = consTablaEJB.consultarTabla(0, 0, null, consulta);
            Iterator it = res.iterator();
            if (it.hasNext()) {
                Hashtable ht = (Hashtable) it.next();
                String codigo = ht.get("CODIGO").toString();
                return codigo;
            }
        } catch (Exception e) {
            System.out.println("ConsultarfiltrosController|buscarCodigo: " + e.getMessage());
        }
        return valor;
    }

    private void filtrosCliente() {
        String consulta = null;
        String ti = (String) clienteSel.get("TIPO_IDENTIFICACION");
        String ni = (String) clienteSel.get("NUMERO_IDENTIFICACION");
        String un = (String) clienteSel.get("OFICINA");
        un = un != null && !un.isEmpty() ? un : (String) clienteSel.get("CODIGO_UNIDAD_NEGOCIO");
        String rg = (String) clienteSel.get("REGION");
        rg = buscarCodigoRegion(rg, "1");
        String zn = (String) clienteSel.get("ZONA");
        zn = buscarCodigoZona(rg, zn, "2");
        String cargoUsu = datosRegistro.get("CODIGO_CARGO").toString();
        String codCargo = (String) clienteSel.get("CODIGO_CARGO");
        System.out.println("este es el cargo del filtro:" +codCargo);
        System.out.println("este es el cargo del usuario:" +cargoUsu);
        //codCargo = peticion.equals("FILTROS_GRAL") ? clienteSel.get("CODIGO_CARGO").toString() : codCargo;

        String codPerfil = (String) datosRegistro.get("CODIGO_PERFIL");
        if (peticion.equals("FILTROS")) {
            if (codPerfil.equals("2")) {
                consulta =
                    "SELECT TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION," +
                    "CODIGO_PRODUCTO, NUMERO_NEGOCIO, NOMBRE,tipo_producto FROM(" +
                    "SELECT t.tipo_identificacion TIPO_IDENTIFICACION," +
                    "t.numero_identificacion NUMERO_IDENTIFICACION," + "a.codigo_producto_v CODIGO_PRODUCTO, " +
                    " p.nombre_corto tipo_producto," + "t.numero_negocio NUMERO_NEGOCIO," +
                    " NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre" +
                    " FROM transacciones_cliente t,clientes c,archivos a, lista_valores p" +
                    " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                    " AND c.numero_identificacion(+)  = t.numero_identificacion" +
                    " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                    " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                    ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " +
                    " AND t.codigo_oficina            = " + un + " UNION ALL" +
                    " SELECT t.tipo_identificacion TIPO_IDENTIFICACION," +
                    " t.numero_identificacion NUMERO_IDENTIFICACION," + " a.codigo_producto_v CODIGO_PRODUCTO," +
                    " p.nombre_corto tipo_producto," + " t.numero_negocio NUMERO_NEGOCIO," +
                    " NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre " +
                    " FROM siscla_his.transacciones_cliente t,clientes c,archivos a, lista_valores p" +
                    " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                    " AND c.numero_identificacion(+)  = t.numero_identificacion" +
                    " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                    " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                    ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " +
                    " AND t.codigo_oficina            = " + un + ")";
            } else {
                consulta =
                    "SELECT TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION," +
                    " CODIGO_PRODUCTO, NUMERO_NEGOCIO,NOMBRE,tipo_producto FROM(" +
                    " SELECT t.tipo_identificacion,t.numero_identificacion " +
                    " ,a.codigo_producto_v codigo_producto,p.nombre_corto tipo_producto " +
                    " ,t.numero_negocio,NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre " +
                    " FROM transacciones_cliente t, clientes c, archivos a " +
                    ",lista_valores p, unidades_negocio un, analista_region ar " +
                    " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                    " AND c.numero_identificacion(+)  = t.numero_identificacion " +
                    " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                    " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                    ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " +
                    " AND t.codigo_oficina            = un.codigo " +
                    " AND un.codigo_region_v          = ar.codigo_region_v " + " AND ar.codigo_cargo             = '" +
                    cargoUsu + "'" + " UNION ALL" + " SELECT t.tipo_identificacion,t.numero_identificacion " +
                    " ,a.codigo_producto_v codigo_producto,p.nombre_corto tipo_producto " +
                    " ,t.numero_negocio,NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre " +
                    " FROM siscla_his.transacciones_cliente t, clientes c, archivos a " +
                    ",lista_valores p, unidades_negocio un, analista_region ar " +
                    " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                    " AND c.numero_identificacion(+)  = t.numero_identificacion " +
                    " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                    " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                    ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " +
                    " AND t.codigo_oficina            = un.codigo " +
                    " AND un.codigo_region_v          = ar.codigo_region_v " + " AND ar.codigo_cargo             =  '" +
                    cargoUsu + "'" + ")";
            }
            consulta +=
                " GROUP BY TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION,CODIGO_PRODUCTO,NUMERO_NEGOCIO,NOMBRE,tipo_producto";
        }
        if (peticion.equals("FILTROS_GRAL")) {
            consulta =
                "SELECT T.TIPO_IDENTIFICACION, " + " T.NUMERO_IDENTIFICACION, " +
                " A.CODIGO_PRODUCTO_V CODIGO_PRODUCTO, " + " P.NOMBRE_CORTO TIPO_PRODUCTO, " + " T.NUMERO_NEGOCIO, " +
                " NVL(C.NOMBRE_RAZON_SOCIAL,'NO HALLADO EN CRM') NOMBRE " + " FROM V_FILTROS T, " + " CLIENTES C, " +
                " ARCHIVOS A, " + " LISTA_VALORES P, " + " UNIDADES_NEGOCIO U " +
                " WHERE C.TIPO_IDENTIFICACION (+)= T.TIPO_IDENTIFICACION " +
                " AND C.NUMERO_IDENTIFICACION(+) = T.NUMERO_IDENTIFICACION " + " AND U.CODIGO = '" + un + "'" +
                " AND A.CODIGO = T.CODIGO_PRODUCTO " + " AND P.TIPO_DATO = '2' " +
                " AND P.CODIGO = A.CODIGO_PRODUCTO_V " + " AND T.TIPO_IDENTIFICACION = '" + ti + "'" +
                " AND T.NUMERO_IDENTIFICACION = '" + ni + "'";
            if (codPerfil.equals("3")) {
                consulta += " AND U.CODIGO_REGION_V = " + rg + " AND U.CODIGO_ZONA = " + zn;
            } else if (codPerfil.equals("4")) {
                consulta += " AND U.CODIGO_REGION_V = " + rg;
            } else if (codPerfil.equals("5")) {
                consulta +=
                    " AND U.CODIGO_REGION_V IN ( " + " SELECT CODIGO_REGION_V " + " FROM ANALISTA_REGION " +
                    " WHERE CODIGO_CARGO = '" + cargoUsu + "')";
            }
            consulta +=
                " GROUP BY T.TIPO_IDENTIFICACION, T.NUMERO_IDENTIFICACION," +
                " A.CODIGO_PRODUCTO_V, T.NUMERO_NEGOCIO, C.NOMBRE_RAZON_SOCIAL, P.NOMBRE_CORTO ";
        }
        if (peticion.equals("FILTROS_SUPERVISOR")) {
            if (codPerfil.equals("5")) {
                consulta =
                    "SELECT V.ID ID,V.CODIGO_CARGO CARGO,V.CODIGO_PRODUCTO CODIGO_PRODUCTO,L.NOMBRE_CORTO TIPO_PRODUCTO," +
                    " V.NUMERO_NEGOCIO NUMERO_NEGOCIO,V.VALOR_MINIMO VALOR_MINIMO,V.VALOR_MAXIMO VALOR_MAXIMO,V.OPERADOR OPERADOR,V.CONDICION CONDICION," +
                    " E.ESTADO ESTADO, E.DESCRIPCION_ESTADO, E.FECHA_CREACION FECHA_CREACION, V.JUSTIFICACION JUSTIFICACION, V.NOMBRE_USUARIO_CREACION USUARIO_CREACION " +
                    " FROM V_FILTROS V, V_CLIENTES_FILTRO_ESTADO E, LISTA_VALORES L" + " WHERE" +
                    " V.CODIGO_UNIDAD_NEGOCIO= " + un + " AND L.TIPO_DATO= '2' AND L.CODIGO = V.CODIGO_PRODUCTO" +
                    " AND V.ROL =5 AND V.ID = E.ID" +
                    " AND V.TIPO_IDENTIFICACION = E.TIPO_IDENTIFICACION AND V.NUMERO_IDENTIFICACION = E.NUMERO_IDENTIFICACION" +
                    " AND E.ESTADO = '" + estadoFilt + "' " + " AND E.TIPO_IDENTIFICACION= '" + ti + "' " +
                    " AND E.NUMERO_IDENTIFICACION= '" + ni + "' ";
            } else {
                consulta =
                    "SELECT V.ID ID,V.CODIGO_CARGO CARGO,V.CODIGO_PRODUCTO CODIGO_PRODUCTO,L.NOMBRE_CORTO TIPO_PRODUCTO," +
                    " V.NUMERO_NEGOCIO NUMERO_NEGOCIO,V.VALOR_MINIMO VALOR_MINIMO,V.VALOR_MAXIMO VALOR_MAXIMO,V.OPERADOR OPERADOR,V.CONDICION CONDICION," +
                    " E.ESTADO ESTADO, E.DESCRIPCION_ESTADO, E.FECHA_CREACION FECHA_CREACION, V.JUSTIFICACION JUSTIFICACION, V.NOMBRE_USUARIO_CREACION USUARIO_CREACION " +
                    " FROM V_FILTROS V, V_CLIENTES_FILTRO_ESTADO E, LISTA_VALORES L" + " WHERE" +
                    " V.CODIGO_UNIDAD_NEGOCIO= " + un + " AND L.TIPO_DATO= '2' AND L.CODIGO = V.CODIGO_PRODUCTO" +
                    " AND V.ROL =2 AND V.ID = E.ID" +
                    " AND V.TIPO_IDENTIFICACION = E.TIPO_IDENTIFICACION AND V.NUMERO_IDENTIFICACION = E.NUMERO_IDENTIFICACION" +
                    " AND E.ESTADO = '" + estadoFilt + "' " + " AND E.TIPO_IDENTIFICACION= '" + ti + "' " +
                    " AND E.NUMERO_IDENTIFICACION= '" + ni + "' ";
            }
        }
        Collection lisFiltTemp = null;
        try {
            lisFiltTemp = consTablaEJB.consultarTabla(0, 0, null, consulta);
            logCtr.setConsultaLog(consulta);
        } catch (Exception e) {
            System.out.println("ConsultarfiltrosController|filtrosCliente: " + e.getMessage());
        }
        if (peticion.equals("FILTROS_SUPERVISOR")) {
            listFiltros = lisFiltTemp;
        } else {
            if (lisFiltTemp != null) {
            	
                listFiltros = new ArrayList<Hashtable>();
                Iterator it = lisFiltTemp.iterator();
                while (it.hasNext()) {
                    Hashtable ht = (Hashtable) it.next();
                    String tp = (String) ht.get("TIPO_PRODUCTO");
                    String cp = (String) ht.get("CODIGO_PRODUCTO");
                    String nn = (String) ht.get("NUMERO_NEGOCIO");
                    String consultaFiltros =
                        "WHERE ID IN( " + " SELECT MAX(ID) " + " FROM FILTROS " + " WHERE NUMERO_IDENTIFICACION = '" +
                        ni + "'" + " AND TIPO_IDENTIFICACION = '" + ti + "'" + " AND NUMERO_NEGOCIO = '" + nn + "'" +
                        " AND CODIGO_PRODUCTO = '" + cp + "'" + " AND CODIGO_CARGO = '" + codCargo + "'";
                    consultaFiltros += " GROUP BY CODIGO_PRODUCTO, NUMERO_NEGOCIO, CODIGO_CARGO)";
                    Collection vFiltros = null;
                    try {
                        vFiltros = consTablaEJB.consultarTabla(0, 0, "V_FILTROS", consultaFiltros);
                    } catch (Exception e) {
                        System.out.println("ConsultarfiltrosController|V_FILTROS: " + e.getMessage());
                    }
                    if (vFiltros != null) {
                        Iterator it2 = vFiltros.iterator();
                        if (it2.hasNext()) {
                            Hashtable ht2 = (Hashtable) it2.next();
                            ht2.put("TIPO_PRODUCTO", tp);
                            listFiltros.add(ht2);
                        } else {
                            listFiltros.add(ht);
                        }
                    }
                }
            }
        }
        darFormatoElemsList();
        System.out.println("listFiltros: " + listFiltros);
    }

    private void darFormatoElemsList() {
        if (listFiltros != null) {
            Iterator it2 = listFiltros.iterator();
            while (it2.hasNext()) {
                Hashtable ht2 = (Hashtable) it2.next();
                ht2 = funciones.quitarNull(ht2);
                String filtro = ht2.get("CONDICION") != null ? ht2.get("CONDICION").toString() : "";
                if (!filtro.isEmpty()) {
                    filtro = funciones.condicionFiltro(filtro);
                    ht2.replace("CONDICION", filtro);
                }
                String fechaCrea = ht2.get("FECHA_CREACION") != null ? ht2.get("FECHA_CREACION").toString() : "";
                if (!fechaCrea.isEmpty()) {
                    fechaCrea = funciones.formatoFecha(fechaCrea);
                    ht2.replace("FECHA_CREACION", fechaCrea);
                }
                String fechaVigDes = ht2.get("VIGENTE_DESDE") != null ? ht2.get("VIGENTE_DESDE").toString() : "";
                if (!fechaVigDes.isEmpty()) {
                    fechaVigDes = funciones.formatoFecha(fechaVigDes);
                    ht2.put("F_VIGENTE_DESDE", fechaVigDes);
                }
            }
        }
    }

    public void actualizarFiltro() {
        if (continuar) {
            Hashtable datos = new Hashtable();
            Date fechaHoy = new Date();
            if (!proceso.equals("APROBAR") && !proceso.equals("RECHAZAR")) {
                datos.put("TIPO_IDENTIFICACION", clienteSel.get("TIPO_IDENTIFICACION"));
                datos.put("NUMERO_IDENTIFICACION", clienteSel.get("NUMERO_IDENTIFICACION"));
                datos.put("CONDICION", filtro);
                datos.put("JUSTIFICACION", justFilt);
                datos.put("USUARIO_CREACION", new Long((String) datosRegistro.get("CODIGO_USUARIO")));
                datos.put("FECHA_CREACION", fechaHoy);
                datos.put("USUARIO_CONFIRMACION", new Long((String) datosRegistro.get("CODIGO_USUARIO")));
                datos.put("ESTADO", "0");
                datos.put("CODIGO_CARGO", codCargo);
            }
            if (proceso.equals("APROBAR") || proceso.equals("RECHAZAR")) {
                datos.put("USUARIO_SUPERVISOR", new Long((String) datosRegistro.get("CODIGO_USUARIO")));
                datos.put("FECHA_SUPERVISOR", fechaHoy);
                datos.put("CONCEPTO_SUPERVISOR", justFilt);
            }

            TransactionManager manager = null;
            try {
            	manager = (TransactionManager) new InitialContext().lookup("java:comp/UserTransaction");
                for (Hashtable htFilt : listFiltrosSel) {
                    System.out.println("htFilt: " + htFilt);
                    manager.begin();
                    String codigoProducto = (String) htFilt.get("CODIGO_PRODUCTO");
                    String numeroNegocio = (String) htFilt.get("NUMERO_NEGOCIO");
                    datos.put("CODIGO_PRODUCTO", codigoProducto);
                    datos.put("NUMERO_NEGOCIO", numeroNegocio);
                    String id = (String) htFilt.get("ID");
                    if (proceso.equals("CREAR")) {
                        if (id != null && !id.equals("null") && !id.isEmpty()) {
                            facadeEJB.inactivaFiltroId(datos, id);
                        }
                        filtroEJB.create(datos);
                        msg = "El filtro se creó exitosamente";
                    } else if (proceso.equals("INACTIVAR")) {
                        facadeEJB.inactivaFiltroId(datos, id);
                        msg = "El filtro se inactivó exitosamente";
                    } else if (proceso.equals("CONFIRMAR")) {
                        Integer idInt = new Integer(0);
                        try {
                            idInt = new Integer(id);
                            datos.put("ID", idInt);
                        } catch (Exception ex) {
                            msg = "No se puede confirmar el filtro, no existe";
                            fcMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
                            throw new Exception(msg);
                        }
                        datos.put("USUARIO_CONFIRMACION", new Long((String) datosRegistro.get("CODIGO_USUARIO")));
                        datos.put("FECHA_CONFIRMACION", fechaHoy);
                     
                        facadeEJB.confirmarFiltro(datos);
                     
                        msg = "El filtro se confirmó exitosamente";
                    } else if (proceso.equals("APROBAR")) {
                        datos.put("CODIGO_CARGO", htFilt.get("CARGO"));
                        datos.put("ID", id);
                        facadeEJB.aprobarFiltro(datos);
                        msg = "El filtro se aprobó exitosamente";
                    } else if (proceso.equals("RECHAZAR")) {
                        datos.put("CODIGO_CARGO", htFilt.get("CARGO"));
                        datos.put("ID", id);
                        facadeEJB.rechazarFiltro(datos);
                        msg = "El filtro se rechazó exitosamente";
                    }
                    manager.commit();
                }
                filtro = "";
                justFilt = "";
                listFiltrosSel = null;
                irAfiltros();
                fcMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (manager != null && manager.getTransaction().getStatus() == Status.STATUS_ACTIVE) {
                        manager.rollback();
                    }
                } catch (SystemException se) {
                    System.out.println("ConsultarfiltrosController|actualizarFiltro:: " + se.getMessage());
                }
                msg = e.getMessage();
                fcMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
            }
        } else {
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        }
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);
    }

    public void validaciones(String procesoIn) {
        proceso = procesoIn;
        continuar = true;
        if (listFiltrosSel != null && !listFiltrosSel.isEmpty()) {
            if (proceso.equals("INACTIVAR")) {
                List listEstadosRechazo = Arrays.asList("2", "3", "-1");
                for (Hashtable htFilt : listFiltrosSel) {
                    if (listEstadosRechazo.contains((String) htFilt.get("ESTADO"))) {
                        msg = "Sólo se permite inactivar filtros en estado Pendiente o Aprobado.";
                        continuar = false;
                        break;
                    }
                }
            }
        } else {
            continuar = false;
            msg = "Debe seleccionar al menos un producto de la lista";
        }
        if (continuar && proceso.equals("CREAR")) {
            continuar = false;
            if (!filtro.isEmpty()) {
                String tipoCondi = filtro.trim().substring(0, 1);
                if (tipoCondi.equals("<") || tipoCondi.equals("#")) {
                    String valorCondi = filtro.substring(1, filtro.length());
                    String[] valoresCondi = valorCondi.split(",");
                    int size = valoresCondi.length;
                    if (tipoCondi.equals("<")) {
                        if (valoresCondi[0].isEmpty()) {
                            msg = "Debe ingresar un valor para la condición < (menor que), ej. <100 (menor que 100)";
                        } else if (size > 1) {
                            msg =
                                "Para el operador < (menor que) debe ingresar solo un valor, ej. <100 (menor que 100) ";
                        } else {
                            try {
                                new BigDecimal(valorCondi);
                                continuar = true;
                            } catch (Exception e) {
                                msg = "El valor para la condición debe ser numérico, ej. <100 (menor que 100) ";
                            }
                        }
                    }
                    if (tipoCondi.equals("#")) {
                        if (valoresCondi[0].isEmpty()) {
                            msg =
                                "Debe ingresar los valores para la condición # (rango entre), ej. #1,100 (rango entre 1 y 100) ";
                        }
                        if (size != 2) {
                            msg =
                                "Para el operador # (rango entre) debe ingresar dos valores separados por coma, ej. #1,100 (rango entre 1 y 100) ";
                        } else {
                            try {
                                Integer.parseInt(valoresCondi[0]);
                                Integer.parseInt(valoresCondi[1]);
                                continuar = true;
                            } catch (Exception e) {
                                msg =
                                    "Los valores para la condición debe ser numéricos, ej. #1,100 (rango entre 1 y 100) ";
                            }
                        }
                    }
                } else {
                    msg =
                        "El operador para la condición del filtro es inválido, los operadores validos son < (menor que) y # (rango entre)";
                }
            } else {
                msg =
                    "Debe ingresar la condición del filtro: \n\n < Menor que \n # Rango entre, indicando los valores inferior y superior separados con coma Ej:#1,100 (rango entre 1 y 100)";
            }
        }
        if (continuar && proceso.equals("CREAR")) {
            if (justFilt.isEmpty()) {
                continuar = false;
                msg = "Justificación es un campo obligatorio";
            }
        }
        if (continuar && proceso.equals("RECHAZAR")) {
            if (justFilt.isEmpty()) {
                continuar = false;
                msg = "Concepto supervisor es un campo obligatorio";
            }
        }
        if (continuar && proceso.equals("CONFIRMAR")) {
        	for (Hashtable htFilt : listFiltrosSel) {
                if (htFilt.get("CONFIRMAR").equals("0")) {
                    msg = "El(Los) filtro(s) seleccionado(s) no requiere(n) confirmación. ";
                    continuar = false;
                    break;
                }
            }
        }
        
        if (continuar) {
            PrimeFaces.current().executeScript("PF('dgwvAccionFilt').show()");
        } else {
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            FacesContext.getCurrentInstance().addMessage(null, fcMsg);
        }
    }

    public void updateDescJust(Hashtable filtr) {
        descJust = (String) filtr.get("JUSTIFICACION");
        PrimeFaces.current().ajax().update("frmFiltro:dgJust");
        PrimeFaces.current().executeScript("PF('dgwvJust').show()");
    }

    public void consultaHistoricoFiltros(Hashtable prod) {
        productoSel = prod;
        System.out.println("productoSel: " + productoSel);
        String cod_prod = (String) productoSel.get("CODIGO_PRODUCTO");
        String num_neg = (String) productoSel.get("NUMERO_NEGOCIO");
        String consulta = "";
        String rol= (codPerfil.equals("4") || codPerfil.equals("3") ? "2" : "5" );
        
        if (peticion.equals("FILTROS")) {
            consulta =
                " WHERE CODIGO_PRODUCTO = '" + cod_prod + "'" + " AND NUMERO_NEGOCIO = '" + num_neg + "'" +
                " AND ROL = '" + codPerfil + "'" + " ORDER BY ID DESC";
        }
        if (peticion.equals("FILTROS_GRAL")) {
            String codCargo = clienteSel.get("CODIGO_CARGO").toString();
            consulta =
                " WHERE CODIGO_PRODUCTO = '" + cod_prod + "'" + " AND NUMERO_NEGOCIO = '" + num_neg + "'" +
               	" AND ROL = '" + rol + "'" + " ORDER BY ID DESC";
        }
        if (peticion.equals("FILTROS_SUPERVISOR")) {
           System.out.println("Este es el perfil del usuario:" +codPerfil);
        	        	System.out.println("Este es el perfil para buscar el rol:" +rol);
        	consulta =
                " WHERE CODIGO_PRODUCTO = '" + cod_prod + "'" + " AND NUMERO_NEGOCIO = '" + num_neg + "'" +
                " AND ROL = '" + rol + "'" + " ORDER BY FECHA_CREACION DESC, ID DESC";
        }
        
        try {
            listHistFilt = consTablaEJB.consultarTabla(0, 0, "V_FILTROS", consulta);
        } catch (Exception e) {
            System.out.println("ConsultarfiltrosController|consultaHistoricoFiltros:V_FILTROS: " + e.getMessage());
        }
        if (listHistFilt != null) {
            Iterator itHistFil = listHistFilt.iterator();
            while (itHistFil.hasNext()) {
                Hashtable htHistFilt = (Hashtable) itHistFil.next();
                String condicion = ((String) htHistFilt.get("CONDICION")).trim();
                condicion = funciones.condicionFiltro(condicion);
                htHistFilt.replace("CONDICION", condicion);
                htHistFilt = funciones.quitarNull(htHistFilt);
                htHistFilt.replace("FECHA_CREACION", funciones.formatoFecha(htHistFilt.get("FECHA_CREACION")));
                htHistFilt.replace("VIGENTE_DESDE", funciones.formatoFecha(htHistFilt.get("VIGENTE_DESDE")));
                htHistFilt.replace("VIGENTE_HASTA", funciones.formatoFecha(htHistFilt.get("VIGENTE_HASTA")));
                htHistFilt.replace("FECHA_SUPERVISOR", funciones.formatoFecha(htHistFilt.get("FECHA_SUPERVISOR")));
            }
        }
        System.out.println("listHistFilt: " + listHistFilt);
    }


    public void setListEstadosFilt(Collection listEstadosFilt) {
        this.listEstadosFilt = listEstadosFilt;
    }

    public Collection getListEstadosFilt() {
        return listEstadosFilt;
    }

    public void setIdentiFilt(String identiFilt) {
        this.identiFilt = identiFilt;
    }

    public String getIdentiFilt() {
        return identiFilt;
    }

    public void setNombreFilt(String nombreFilt) {
        this.nombreFilt = nombreFilt;
    }

    public String getNombreFilt() {
        return nombreFilt;
    }

    public void setEstadoFilt(String estadoFilt) {
        this.estadoFilt = estadoFilt;
    }

    public String getEstadoFilt() {
        return estadoFilt;
    }

    public void setListClientes(Collection listClientes) {
        this.listClientes = listClientes;
    }

    public Collection getListClientes() {
        return listClientes;
    }

    public void setListFiltrosSel(List<Hashtable> listFiltrosSel) {
        this.listFiltrosSel = listFiltrosSel;
    }

    public List<Hashtable> getListFiltrosSel() {
        return listFiltrosSel;
    }

    public void setListTiposId(Collection listTiposId) {
        this.listTiposId = listTiposId;
    }

    public Collection getListTiposId() {
        return listTiposId;
    }

    public void setTipoIdentiFilt(String tipoIdentiFilt) {
        this.tipoIdentiFilt = tipoIdentiFilt;
    }

    public String getTipoIdentiFilt() {
        return tipoIdentiFilt;
    }

    public void setListFiltros(Collection listFiltros) {
        this.listFiltros = listFiltros;
    }

    public Collection getListFiltros() {
        return listFiltros;
    }

    public void setClienteSel(Hashtable clienteSel) {
        this.clienteSel = clienteSel;
    }

    public Hashtable getClienteSel() {
        return clienteSel;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setJustFilt(String justFilt) {
        this.justFilt = justFilt;
    }

    public String getJustFilt() {
        return justFilt;
    }

    public void setDescJust(String descJust) {
        this.descJust = descJust;
    }

    public String getDescJust() {
        return descJust;
    }

    public void setListHistFilt(Collection listHistFilt) {
        this.listHistFilt = listHistFilt;
    }

    public Collection getListHistFilt() {
        return listHistFilt;
    }

    public void setProductoSel(Hashtable productoSel) {
        this.productoSel = productoSel;
    }

    public Hashtable getProductoSel() {
        return productoSel;
    }

    public void setPeticion(String peticion) {
        this.peticion = peticion;
    }

    public String getPeticion() {
        return peticion;
    }

    public void setListRegiones(Collection listRegiones) {
        this.listRegiones = listRegiones;
    }

    public Collection getListRegiones() {
        return listRegiones;
    }

    public void setListZonas(Collection listZonas) {
        this.listZonas = listZonas;
    }

    public Collection getListZonas() {
        return listZonas;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getZona() {
        return zona;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getOficina() {
        return oficina;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNivel() {
        return nivel;
    }

    public void setFechaAprob(Date fechaAprob) {
        this.fechaAprob = fechaAprob;
    }

    public Date getFechaAprob() {
        return fechaAprob;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setLogCtr(LogController logCtr) {
        this.logCtr = logCtr;
    }

    public LogController getLogCtr() {
        return logCtr;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setListavaloresCtr(ListavaloresController listavaloresCtr) {
        this.listavaloresCtr = listavaloresCtr;
    }

    public ListavaloresController getListavaloresCtr() {
        return listavaloresCtr;
    }

	public boolean isDisabledR() {
		return disabledR;
	}

	public void setDisabledR(boolean disabledR) {
		this.disabledR = disabledR;
	}

	public boolean isDisabledZ() {
		return disabledZ;
	}

	public void setDisabledZ(boolean disabledZ) {
		this.disabledZ = disabledZ;
	}

	public boolean isDisabledD() {
		return disabledD;
	}

	public void setDisabledD(boolean disabledD) {
		this.disabledD = disabledD;
	}

	public boolean isConfirmaFiltro() {
		return confirmaFiltro;
	}

	public void setConfirmaFiltro(boolean confirmaFiltro) {
		this.confirmaFiltro = confirmaFiltro;
	}
}
