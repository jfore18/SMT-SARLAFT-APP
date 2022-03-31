package view.controllers;

import admin.seguridad.LogConsultaEJB;

import admin.util.Funciones;

import baseDatos.CatalogoBD;
import baseDatos.Consulta;
import baseDatos.ConsultaEJB;

import baseDatos.ConsultaTablaEJB;
import monfox.toolkit.snmp.agent.modules.SnmpV2Mib.SysOREntry;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import java.util.Iterator;

import javax.annotation.Generated;

import javax.annotation.PostConstruct;

import javax.ejb.CreateException;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;

import presentacion.FacadeEJB;

import transaccion.ComentarioEJB;

@ManagedBean(name = "FrmpreinusualController")
@SessionScoped
public class FrmpreinusualController {
	@EJB
	private ConsultaEJB consultaEJB;
	@EJB
	private FacadeEJB facadeEJB;
	@EJB
	private ConsultaTablaEJB consTablaEJB;
	@EJB
	private LogConsultaEJB logconsultaEJB;
	@EJB
	private ComentarioEJB comentarioEJB;
	@ManagedProperty("#{ContenidoController}")
	private ContenidoController contenidoCtr;
	@ManagedProperty("#{ListavaloresController}")
	private ListavaloresController listavaloresCtr;
	@ManagedProperty("#{LogController}")
	private LogController logCtr;
	private Funciones funciones;
	private FacesContext facContex;
	private ExternalContext extContex;
	private HttpSession sesion;
	private FacesMessage fcMsg;
	private Collection regnsConsTr;
	private Collection regiones;
	private Collection zonasConsTr;
	private Collection zonas;
	private Collection unRegs;
	private Collection ftes;
	private Collection tiposFuente;
	private Collection tiposDoc;
	private Collection tiposEstado;
	private Collection trans;
	private Collection criteriosTr;
	private Collection conceptosTr;
	private Collection detallesConcepto;
	private Collection comentarios;
	private Hashtable region;
	private Hashtable zona;
	private Hashtable unReg;
	private Hashtable fte;
	private Hashtable tranSelec;
	private Hashtable detalleFte;
	private Hashtable detalleCLE;
	private Hashtable detalleTran;
	private Hashtable conceptoSel;
	private Hashtable comentarioSel;
	private Hashtable detalleComentario;
	private Hashtable datosRegistro;
	private Hashtable totalReg;
	private Hashtable totalZon;
	private Hashtable totalOfi;
	private Hashtable totalFte;
	private Date fecha;
	private String fechaStr;
	private String nivelConsulta;
	private String rol;
	private String cargo;
	private String tipoCargo;
	private String usuario;
	private String consultaStr;
	private String consultaTran;
	private String fuente;
	private String codTr;
	private String regionStr;
	private String zonaStr;
	private String oficina;
	private String tipDoc;
	private String estadoTr;
	private String estadoD;
	private String identi;
	private String nombre;
	private String coment;
	private String valor;
	private String nroProd;
	private String justFinal;
	private String index;
	private String msg;
	private String tipMsg;
	private String urlVolver;
	private String encabezado;
	private FacesMessage facesMsg;
	private boolean filtro;
	private boolean nueva;
	private boolean fducc;
	private boolean mayorR;
	private boolean puedeComentar;
	private boolean puedeReportar;
	private boolean puedeReportarNormal;
	private boolean disabled;
	private boolean disabled2;
	private boolean continuar;
	private String titulo;
	private int cantClientesPre;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		funciones = new Funciones();
		consultaStr = "";
		datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		cargo = (String) datosRegistro.get("CODIGO_CARGO");
		tipoCargo = (String) datosRegistro.get("CODIGO_TIPO_CARGO");
		rol = (String) datosRegistro.get("CODIGO_PERFIL");
		usuario = (String) datosRegistro.get("CODIGO_USUARIO");
		titulo= "Consulta de transacciones";
		puedeReportar = (rol.equals("2") || rol.equals("5"));
		puedeReportarNormal = (rol.equals("2"));
		puedeComentar = (rol.equals("2") || rol.equals("3") || rol.equals("4") || rol.equals("5"));
	}
	

	/*
	 * Metodo ejecutado en el onload de frmConsultaTr.jsf. Se accede al anterior jsf ingresando por la opcion del menu
	 * consultar transacciones
	 * */
	public void inicioConsTr() {
		iniciarListas();
		sesion.removeAttribute("idRep");
		disabled = false;
		disabled2 = false;
		//System.out.println("Esta es la url anterior:" +contenidoCtr.getUrlAnt());
		if (!contenidoCtr.getUrlAnt().equals("/siscla/tr/detalle/frmDetalleTr.jsf")) {
			trans = null;
		}
		//trans=null;
		if (rol.equals("5")) {
			estadoD = "P";
			mayorR = true;
		}
		if (rol.equals("2") || rol.equals("3")) {
			regionStr = datosRegistro.get("NOMBRE_CORTO_REGION").toString();
			buscaZonas();
			zonaStr = datosRegistro.get("NOMBRE_CORTO_ZONA").toString();
			oficina = rol.equals("2") && !tipoCargo.equals("N") ? datosRegistro.get("NOMBRE_UNIDAD_NEGOCIO").toString()
					: "";
			
			estadoTr = "PRE";
			disabled = true;
			disabled2 = true;
		}
		if (rol.equals("4")) {
			regionStr = datosRegistro.get("NOMBRE_CORTO_REGION").toString();
			buscaZonas();
			disabled = true;
			disabled2 = false;
			mayorR = true;

		}
		puedeReportar = (rol.equals("2") || rol.equals("5"));
		puedeReportarNormal = (rol.equals("2"));
		puedeComentar = (rol.equals("2") || rol.equals("3") || rol.equals("4") || rol.equals("5"));
	}

	private void llenarNombreZonas() {
		Collection listZonas = listavaloresCtr.getZonas();
		Iterator itZonas1 = zonas.iterator();
		while (itZonas1.hasNext()) {
			Hashtable zona1 = (Hashtable) itZonas1.next();
			Iterator itZonas2 = listZonas.iterator();
			while (itZonas2.hasNext()) {
				Hashtable zona2 = (Hashtable) itZonas2.next();
				if (zona1.get("ZONA").equals(zona2.get("NOMBRE_CORTO"))) {
					zona1.put("NOMBRE_ZONA", zona2.get("NOMBRE_LARGO"));
					break;
				}
			}
		}
	}

	private void llenarNombreFuentes() {
		Collection list1 = listavaloresCtr.getFuentes();
		Iterator it1 = ftes.iterator();
		while (it1.hasNext()) {
			Hashtable ht1 = (Hashtable) it1.next();
			Iterator it2 = list1.iterator();
			while (it2.hasNext()) {
				Hashtable ht2 = (Hashtable) it2.next();
				if (ht1.get("FUENTE").equals(ht2.get("NOMBRE_CORTO"))) {
					ht1.put("NOMBRE_FUENTE", ht2.get("NOMBRE_LARGO"));
					break;
				}
			}
		}
	}

	private Hashtable totales(Collection list) {
		Hashtable totalesTemp = new Hashtable();
		int totalHoy = 0;
		int totalAnt = 0;
		if (list != null) {
			Iterator itemp = list.iterator();
			while (itemp.hasNext()) {
				Hashtable htemp = (Hashtable) itemp.next();
				totalHoy += Integer.valueOf(htemp.get("HOY").toString());
				totalAnt += Integer.valueOf(htemp.get("ANTERIORES").toString());
			}
		}
		totalesTemp.put("HOY", totalHoy);
		totalesTemp.put("ANT", totalAnt);
		return totalesTemp;
	}

	/*
	 * Metodo listar
	 * Ejecutado  al dar click en la opcion resumen de transacciones, con nvCons igual a 3
	 * Ejecutado al dar click en la opcion resumen mayor riesgo
	 */
	public void listar(String nvCons) {
		Consulta consulta;
		nivelConsulta = nvCons;
		if (nvCons.equals("3")){
			mayorR=false;
			titulo="Consulta de transacciones preinusuales (OFICINA) ";
		}
		if (nvCons.equals("23")){
			mayorR=true;
			titulo="Consulta de transacciones de mayor riesgo(ANALISTA) ";
		}
		try {
			if (rol.equals("5")) {
				consulta = consultaEJB.realizarConsulta(cargo, nivelConsulta);
				if (nivelConsulta.equals("3") || nivelConsulta.equals("23")) {
					regiones = null;
					if (nivelConsulta.equals("23")) {
						mayorR = true;
						consultaStr = " AND MAYOR_RIESGO = '1'";
					} else {
						mayorR = false;
						consultaStr = "";
					}
					consulta.setSeleccion(consulta.getSeleccion() + ", R.NOMBRE_LARGO  \"NOMBRE_REG\"");
					consulta.setAgrupacion(consulta.getAgrupacion() + ", R.NOMBRE_LARGO");
					regiones = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					System.out.println("regiones: " + regiones);
					
				}
				if (nivelConsulta.equals("4") || nivelConsulta.equals("24")) {
					zonas = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					llenarNombreZonas();
					System.out.println("zonas: " + zonas);
				}
				if (nivelConsulta.equals("5") || nivelConsulta.equals("25")) {
					unRegs = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					System.out.println("unRegs: " + unRegs);
				}
				if (nivelConsulta.equals("6") || nivelConsulta.equals("26")) {
					ftes = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					llenarNombreFuentes();
					System.out.println("ftes: " + ftes);
				}
				
			}
			
			if (rol.equals("2") || rol.equals("3")) {
				String regSel = datosRegistro.get("NOMBRE_CORTO_REGION").toString();
				String zonSel = datosRegistro.get("NOMBRE_CORTO_ZONA").toString();
				String ofiSel = datosRegistro.get("NOMBRE_UNIDAD_NEGOCIO").toString();
				region = new Hashtable();
				zona = new Hashtable();
				region.put("REGION", regSel);
				zona.put("ZONA", zonSel);
				if ((rol.equals("2") &&  !tipoCargo.equals("N") )) {
					unReg = new Hashtable();
					unReg.put("OFICINA", ofiSel);
					consulta = consultaEJB.realizarConsulta(cargo, nivelConsulta);
				    consultaStr = "";
					ftes = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					llenarNombreFuentes();
					encabezado = datosRegistro.get("NOMBRE_REGION") + " | " + datosRegistro.get("NOMBRE_ZONA") + " | "
							+ unReg.get("OFICINA");
				}
				if (rol.equals("3") || tipoCargo.equals("N")) {
					nivelConsulta = nivelConsulta.equals("6") ? "4" : nivelConsulta;
					
					if (tipoCargo.equals("N")) {

						consulta = consultaEJB.realizarConsultaTipoCargo(cargo, nivelConsulta);
					} else {
						consulta = consultaEJB.realizarConsulta(cargo, nivelConsulta);
					}
		
					if (nivelConsulta.equals("3")) {
						consultaStr = "";
						unRegs = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
						System.out.println("unRegs: " + unRegs);
					}
					if (nivelConsulta.equals("4")) {
						ftes = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
						llenarNombreFuentes();
						encabezado = datosRegistro.get("NOMBRE_REGION") + " | " + datosRegistro.get("NOMBRE_ZONA")
								+ " | " + unReg.get("OFICINA");
						System.out.println("ftes: " + ftes);
					}
					encabezado = datosRegistro.get("NOMBRE_REGION") + " | " + datosRegistro.get("NOMBRE_ZONA");
				}
			}
			if (rol.equals("4")) {
				String regSel = datosRegistro.get("NOMBRE_CORTO_REGION").toString();
				region = new Hashtable();
				region.put("REGION", regSel);
				if (nivelConsulta.equals("3")) {
					consultaStr = "";
					consulta = consultaEJB.realizarConsulta(cargo, nivelConsulta);
					zonas = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					llenarNombreZonas();
					encabezado = (String) datosRegistro.get("NOMBRE_REGION");
					System.out.println("zonas: " + zonas);
				}
				if (nivelConsulta.equals("5")) {
					nivelConsulta = "4";
					consulta = consultaEJB.realizarConsulta(cargo, nivelConsulta);
					unRegs = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					encabezado = datosRegistro.get("NOMBRE_REGION") + " | " + datosRegistro.get("NOMBRE_ZONA");
					System.out.println("unRegs: " + unRegs);
				}
				if (nivelConsulta.equals("6")) {
					nivelConsulta = "5";
					consulta = consultaEJB.realizarConsulta(cargo, nivelConsulta);
					ftes = facadeEJB.ejecutarConsulta(consultaStr, consulta, true);
					llenarNombreFuentes();
					encabezado = datosRegistro.get("NOMBRE_REGION") + " | " + datosRegistro.get("NOMBRE_ZONA") + " | "
							+ unReg.get("OFICINA");
					System.out.println("ftes: " + ftes);
				}
			}
			totalReg = totales(regiones);
			totalZon = totales(zonas);
			totalOfi = totales(unRegs);
			totalFte = totales(ftes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * metodo consultar
	 * Se ejecuta al dar click sobre las regiones, zonas, oficinas y fuentes del menu Resumen transacciones
	 * el nivel de consulta se envia desde el jsf, segun si se hace click en region, zona, oficina, fuente
	 */
	public void consultar(String nivCons) {
		nivelConsulta = nivCons;	
		String cons = "";
		if (mayorR) {
			cons = " AND ESTADO_DUCC = 'PRE' AND MAYOR_RIESGO = '1'";
		} else {
			cons = " AND ESTADO_OFICINA = 'PRE'";
		}
		if (nivelConsulta.equals("4") && mayorR) {
			nivelConsulta="24";
		}
		
		if (nivelConsulta.equals("4") || nivelConsulta.equals("24")) {
			consultaStr = " AND NC_REGION = '" + region.get("REGION") + "'" + cons;
			zonas = null;
			unRegs = null;
			ftes = null;
		}
		
		if (nivelConsulta.equals("5") && mayorR) {
			nivelConsulta="25";
		}
		if (nivelConsulta.equals("5") || nivelConsulta.equals("25")) {
			consultaStr = " AND NC_ZONA = '" + zona.get("ZONA") + "'" + cons;
			unRegs = null;
			ftes = null;
		}
		if (nivelConsulta.equals("6") && mayorR) {
			nivelConsulta="26";
		}
		if (nivelConsulta.equals("6") || nivelConsulta.equals("26")) {
			consultaStr = " AND OFICINA = '" + unReg.get("OFICINA") + "'" + cons;
			ftes = null;
		}
		//Nivel 0 cuando selecciona la fuente y va a la consulta de transacciones
		if (nivelConsulta.equals("0")) {
			boolean mrTemp = mayorR;
			iniciarListas();
			limpiarElementos();
			mayorR = mrTemp;
			if (mayorR) {
				estadoD = "P";
			} else {
				estadoTr = "PRE";
			}
	
			
			fuente = (String) fte.get("FUENTE");
			regionStr = (String) region.get("REGION");
			buscaZonas();
			zonaStr = (String) zona.get("ZONA");
			oficina = (String) unReg.get("OFICINA");
			disabled = !rol.equals("5");
			disabled2 = !rol.equals("5");
			buscarTrans();
			contenidoCtr.updateUrl("/siscla/tr/consultaTr.jsf");
		} else {
			listar(nivelConsulta);
		}
	}

	public void iniciarListas() {
		tiposFuente = tiposFuente == null ? listavaloresCtr.getFuentes() : tiposFuente;
		tiposDoc = tiposDoc == null ? listavaloresCtr.getTiposDocumento() : tiposDoc;
		tiposEstado = tiposEstado == null ? listavaloresCtr.getTiposEstadoTr() : tiposEstado;
		regnsConsTr = regnsConsTr == null ? listavaloresCtr.getRegiones() : regnsConsTr;
	}

	public void buscaZonas() {
		try {
			String sql = "SELECT * FROM  V_ZONAS Z LEFT OUTER JOIN V_REGION R ON  Z.CODIGO_REGION_V = R.codigo WHERE DESC_CORTA = '"
					+ regionStr + "'";
			zonasConsTr = consTablaEJB.consultarTabla(0, 0, null, sql);
			System.out.println("zonasConsTr: " + zonasConsTr);
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|buscaZonas: Error consultando zonas");
		}
	}

	public void buscarTrans() {
		fechaStr = dateToString();
		if(rol.equals("5")){
					
			consultaTran = "SELECT * FROM v_transacciones v WHERE " +
					"REGION IN (SELECT CODIGO_REGION_V FROM ANALISTA_REGION WHERE CODIGO_CARGO='"+cargo+"')";

		}
		else {
			consultaTran= "SELECT * FROM V_TRANSACCIONES WHERE 0<1 ";
		}
		
		if (fechaStr != null && !fechaStr.isEmpty()) {
			consultaTran += " AND ( FECHA LIKE '%" + fechaStr + "%' ) ";
		}
		if (fuente != null && !fuente.isEmpty()) {
			consultaTran += " AND ( FUENTE = '" + fuente + "' ) ";
		}
		if (codTr != null && !codTr.isEmpty()) {
			consultaTran += " AND ( CODIGO_TR LIKE '%" + codTr + "%' ) ";
		}
		if (tipDoc != null && !tipDoc.isEmpty()) {
			consultaTran += " AND ( TIPO_IDENTIFICACION = '" + tipDoc + "' ) ";
		}
		if (identi != null && !identi.isEmpty()) {
			consultaTran += " AND ( NUMERO_IDENTIFICACION LIKE '%" + identi + "%' ) ";
		}
		if (nombre != null && !nombre.isEmpty()) {
			consultaTran += " AND ( UPPER(CLIENTE) LIKE '%" + nombre.toUpperCase() + "%' ) ";
		}
		if (estadoTr != null && !estadoTr.isEmpty()) {
			consultaTran += " AND ( ESTADO_OFICINA = '" + estadoTr + "' ) ";
		}
		if (coment != null && !coment.isEmpty()) {
			consultaTran += " AND ( TOTAL_COMENTARIOS = '" + coment + "' ) ";
		}
		if (estadoD != null && !estadoD.isEmpty()) {
			consultaTran += " AND ( CODIGO_ESTADO_DUCC = '" + estadoD + "' ) ";
		}
		if (regionStr != null && !regionStr.isEmpty()) {
			consultaTran += " AND ( NC_REGION = '" + regionStr + "' ) ";
		}
		if (zonaStr != null && !zonaStr.isEmpty()) {
			consultaTran += " AND ( NC_ZONA = '" + zonaStr + "' ) ";
		}
		if (oficina != null && !oficina.isEmpty()) {
			//consultaTran += " AND (UPPER(OFICINA) ='" + oficina.toUpperCase() + "' OR TO_CHAR(CODIGO_OFICINA)='"+ oficina+"')";
			consultaTran += " AND (UPPER(OFICINA)   ='" + oficina.toUpperCase() + "' OR TO_CHAR(CODIGO_OFICINA) IN("+ oficina+"))";
		}
		if (valor != null && !valor.isEmpty()) {
			consultaTran += " AND ( VALOR LIKE '%" + valor + "%' ) ";
		}
		if (nroProd != null && !nroProd.isEmpty()) {
			consultaTran += " AND ( N_PRODUCTO LIKE '%" + nroProd + "%' ) ";
		}
		if (filtro) {
			consultaTran += " AND ( FILTRO_OF = 'X' ) ";
		}
		if (nueva) {
			consultaTran += " AND ( NUEVA = '1' ) ";
		}
		if (fducc) {
			consultaTran += " AND ( FILTRO_DUCC = 'X' ) ";
		}
		if (mayorR) {
			consultaTran += " AND ( MAYOR_RIESGO = '1' ) ";
		}
		// if (!consultaTran.trim().equals("WHERE0<1")) {
		Integer limite = listavaloresCtr.getLimiteRes();
		consultaTran += " AND ROWNUM <= " + limite;
		consultaTran += rol.equals("2") ? " ORDER BY FECHA DESC, VALOR DESC "
				: " ORDER BY FECHA DESC, N_PRODUCTO, MAYOR_RIESGO DESC, VALOR DESC";
		System.out.println("consultaTran: " + consultaTran);
		try {
			trans = null;
			continuar = true;
			Thread thread = new Thread() {
				public void run() {
					try {
						trans = consTablaEJB.consultarTabla(1000, 0, null, consultaTran);
					} catch (Exception e) {
						System.out.println("FrmpreinusualController|buscarTrans:Thread: " + e.getMessage());
					}
				}
			};
			thread.start();
			long contadorIni = System.currentTimeMillis();
			long contadorFin = 0;
			long contadorResta = 0;
			long limit = 15000;
			while (continuar) {
				contadorFin = System.currentTimeMillis();
				contadorResta = contadorFin - contadorIni;
				if (contadorResta > limit && thread.isAlive()) {
					thread.interrupt();
					thread = null;
					trans = null;
					continuar = false;
				} else if (contadorResta < limit && !thread.isAlive()) {
					continuar = false;
				}
			}
			if (trans != null) {
				Iterator iter = trans.iterator();
				while (iter.hasNext()) {
					Hashtable hTemp = (Hashtable) iter.next();
					String fecha = (String) hTemp.get("FECHA");
					fecha = fecha.substring(0, 10).replace("-", "/");
					hTemp.replace("FECHA", fecha);
					String nuevaTemp = ((String) hTemp.get("NUEVA")).equals("1") ? "X" : "";
					hTemp.replace("NUEVA", nuevaTemp);
					String mayrTemp = ((String) hTemp.get("MAYOR_RIESGO")).equals("1") ? "X" : "";
					hTemp.replace("MAYOR_RIESGO", mayrTemp);
					double val = Double.valueOf((String) hTemp.get("VALOR"));
					DecimalFormat format = new DecimalFormat("###,###.##");
					String valor = format.format(val);
					hTemp.replace("VALOR", valor);
				}
			} else {
				if (contadorResta > limit) {
					msg = "La consulta esta tomando mas tiempo de lo habitual.Intente nuevamente";
					fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
					FacesContext.getCurrentInstance().addMessage(null, fcMsg);
				}
			}
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|buscarTrans: " + e.getMessage());
		}
		logCtr.setConsultaLog("SELECT * FROM V_TRANSACCIONES " + consultaTran);
		if (trans != null && !trans.isEmpty()) {
			if (!trans.isEmpty()) {
				logCtr.setResLog(1);
				logCtr.setMsgLogCons(" ");
			} else {
				logCtr.setResLog(2);
				logCtr.setMsgLogCons("No hay transacciones");
			}
		} else {
			logCtr.setResLog(2);
			logCtr.setMsgLogCons("Consulta muy pesada");
		}
		logCtr.logConsulta();
		/*
		 * } else { System.out.println("Ingrese criterios de busqueda: "); }
		 */
		System.out.println("trans: " + trans);
	}

	private void logConsulta() {
		facContex = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) facContex.getExternalContext().getRequest();
		String ti = "", ni = "0", tp = "", np = "";
		Integer tb = 2, ttx = 1;
		String consulta = "SELECT * FROM V_TRANSACCIONES " + consultaTran;
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
		datosLogConsulta.put("QUERY", consulta);
		datosLogConsulta.put("DOMINIORED", dominioNt);
		datosLogConsulta.put("TIPOID", ti);
		datosLogConsulta.put("NUMEROID", ni);
		datosLogConsulta.put("TIPOPRODUCTO", tp);
		datosLogConsulta.put("NUMEROPRODUCTO", np);
		datosLogConsulta.put("TIPOBUSQUEDA", tb);
		datosLogConsulta.put("TIPOTRANSACCION", ttx);
		datosLogConsulta.put("USUARIONT", usuarioNt);
		if (trans != null) {
			if (!trans.isEmpty()) {
				datosLogConsulta.put("RESULTADOTX", 1);
				datosLogConsulta.put("DESCRIPCIONRECHAZO", " ");
			} else {
				datosLogConsulta.put("RESULTADOTX", 2);
				datosLogConsulta.put("DESCRIPCIONRECHAZO", "No hay transacciones");
			}
		} else {
			datosLogConsulta.put("RESULTADOTX", 2);
			datosLogConsulta.put("DESCRIPCIONRECHAZO", "Consulta muy pesada");
		}
		try {
			logconsultaEJB.create(datosLogConsulta);
		} catch (CreateException e) {
			System.out.println("FrmpreinusualController|logConsulta: " + e.getMessage());
		}
	}

	public void ultimasTrans() {
		limpiarElementos();
		if (detalleTran != null) {
			tipDoc = detalleTran.get("TIPO_IDENTIFICACION").toString();
			identi = detalleTran.get("NUMERO_IDENTIFICACION").toString();
			fuente = detalleTran.get("FUENTE").toString();
			nroProd = detalleTran.get("N_PRODUCTO").toString();
			buscarTrans();
		}
	}

	public void limpiarElementos() {
		fecha = null;
		fuente = null;
		codTr = null;
		tipDoc = null;
		identi = null;
		nombre = null;
		estadoTr = null;
		estadoD = null;

		valor = null;
		nroProd = null;
		filtro = false;
		nueva = false;
		fducc = false;
		mayorR = false;
		coment = null;

		if (rol.equals("5")) {
			regionStr = null;
			zonaStr = null;
			oficina = null;
		}
		if (rol.equals("3")) {
			oficina = null;
		}
		if (rol.equals("4")) {
			zonaStr = null;
			oficina = null;
		}
	}

	public void irDetalleTr(Hashtable tranSel) {
		
		tranSelec = tranSel != null ? tranSel : tranSelec;
	
		tranSelec = funciones.quitarNull(tranSelec);
		
		urlVolver = "/siscla/tr/frmConsultaTr.jsf";
		if (tranSelec.get("FECHA_PROCESO") == null) {
			
			consultaDetalleClienteConTxPrein();
			urlVolver = "/siscla/cliente/consultaCliente.jsf";
		}
		consultaDatosTran();
		consultaFte();
		procConceptos();
		Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		cargo = (String) datosRegistro.get("CODIGO_CARGO");
		rol = (String) datosRegistro.get("CODIGO_PERFIL");
		contenidoCtr.updateUrl("/siscla/tr/detalle/frmDetalleTr.jsf");
	}
	
	/*
	 * Metodo ejecutado desde el jsf frmGestion y datosReporte.jsf luego de la creación del reporte y de darle click en aceptar. 
	 * Se crea para saber la pagina de retorno despues de la creacion del reporte
	 * */
	public void regresar() {
	contenidoCtr.updateUrl(urlVolver);
	PrimeFaces.current().ajax().update("panelContenido");
	
	}

	public void irReporteDucc(Hashtable tranSel) {
		tranSelec = tranSel;
		String idTr = tranSelec.get("ID_TRANSACCION").toString();
		String codArc = tranSelec.get("CODIGO_ARCHIVO").toString();
		String fecTr = funciones.formatoFecha(tranSelec.get("FECHA_PROCESO"));
		Collection listReportes = null;
		Hashtable repTemp = null;
		try {
			String consultaRep = " SELECT T.ID FROM (" + " SELECT R.ID ID"
					+ " FROM TRANSACCIONES_REP TR, REPORTE R, CARGOS C" + " WHERE " + " TR.ID_REPORTE = R.ID "
					+ " AND C.CODIGO = R.CODIGO_CARGO " + " AND C.CODIGO_PERFIL_V = '5'";
			if (rol.equals("6") || rol.equals("11") || rol.equals("3") || rol.equals("4")) {
				consultaRep += " AND C.CODIGO_PERFIL_V <> '5'";
			}
			consultaRep += " AND ID_TRANSACCION = " + idTr + " AND CODIGO_ARCHIVO = " + codArc
					+ " AND FECHA_PROCESO = TO_DATE('" + fecTr + "','YYYY/MM/DD')"
					+ " AND CODIGO_CLASE_REPORTE_V IS NOT NULL " + " ORDER BY R.FECHA_ACTUALIZACION DESC ) T"
					+ " WHERE ROWNUM < 2";

			System.out.println("consultaRep: " + consultaRep);

			Collection colRep = null;
			colRep = consTablaEJB.consultarTabla(0, 0, null, consultaRep);
			if (colRep != null) {
				if (colRep.isEmpty()) {
					msg = "La transaccion no tiene reporte asociado";
				} else if (colRep.size() > 1) {
					Iterator it = colRep.iterator();
					String idRepTemp = "";
					while (it.hasNext()) {
						Hashtable tabla = (Hashtable) it.next();
						idRepTemp = (String) tabla.get("ID") + ",";
					}
					msg = "La transaccion tiene más de un reporte asociado, puede consultarlos con los siguientes ID's: "
							+ idRepTemp.substring(0, idRepTemp.length() - 2);
				} else {
					Iterator itRep = colRep.iterator();
					if (itRep.hasNext()) {
						repTemp = (Hashtable) itRep.next();
					}
				}
			}
			if (repTemp != null) {
				String idRep = repTemp.get("ID").toString();
				consultaRep = "SELECT * FROM V_RELACION_REPORTES";
				consultaRep += " WHERE ID_REPORTE = '" + idRep + "'";
				listReportes = consTablaEJB.consultarTabla(0, 0, null, consultaRep);
			}
			if (listReportes != null) {
				Iterator it = listReportes.iterator();
				if (it.hasNext()) {
					repTemp = (Hashtable) it.next();
					if (repTemp.get("ROS") == null || repTemp.get("ROS").toString().equals("null")) {
						repTemp.replace("ROS", "N.A");
					}
					String fechTemp = repTemp.get("FECHA_CREACION").toString();
					repTemp.replace("FECHA_CREACION", fechTemp.substring(0, 10).replace("-", "/"));
				}
				sesion.setAttribute("reporteSel", repTemp);
				contenidoCtr.updateUrl("/siscla/rep/sc/rep.jsf");
			} else {
				fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
				FacesContext.getCurrentInstance().addMessage(null, fcMsg);
			}
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|irReporte: " + e.getMessage());
			msg = "FrmpreinusualController|irReporte: " + e.getMessage();
			fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
	}
	public void irReporteGer(Hashtable tranSel) {
		tranSelec = tranSel;
		String idTr = tranSelec.get("ID_TRANSACCION").toString();
		String codArc = tranSelec.get("CODIGO_ARCHIVO").toString();
		String fecTr = funciones.formatoFecha(tranSelec.get("FECHA_PROCESO"));
		Collection listReportes = null;
		Hashtable repTemp = null;
		try {
			String consultaRep = " SELECT T.ID FROM (" + " SELECT R.ID ID"
					+ " FROM TRANSACCIONES_REP TR, REPORTE R, CARGOS C" + " WHERE " + " TR.ID_REPORTE = R.ID "
					+ " AND C.CODIGO = R.CODIGO_CARGO " + " AND C.CODIGO_PERFIL_V = '2'";
			consultaRep += " AND ID_TRANSACCION = " + idTr + " AND CODIGO_ARCHIVO = " + codArc
					+ " AND FECHA_PROCESO = TO_DATE('" + fecTr + "','YYYY/MM/DD')"
					+ " AND CODIGO_CLASE_REPORTE_V IS NOT NULL " + " ORDER BY R.FECHA_ACTUALIZACION DESC ) T"
					+ " WHERE ROWNUM < 2";

			System.out.println("consultaRep oficina: " + consultaRep);

			Collection colRep = null;
			colRep = consTablaEJB.consultarTabla(0, 0, null, consultaRep);
			if (colRep != null) {
				if (colRep.isEmpty()) {
					msg = "La transaccion no tiene reporte asociado. Inconsitencia en datos";
				} else if (colRep.size() > 1) {
					Iterator it = colRep.iterator();
					String idRepTemp = "";
					while (it.hasNext()) {
						Hashtable tabla = (Hashtable) it.next();
						idRepTemp = (String) tabla.get("ID") + ",";
					}
					msg = "La transaccion tiene más de un reporte asociado, puede consultarlos con los siguientes ID's: "
							+ idRepTemp.substring(0, idRepTemp.length() - 2);
				} else {
					Iterator itRep = colRep.iterator();
					if (itRep.hasNext()) {
						repTemp = (Hashtable) itRep.next();
					}
				}
			}
			if (repTemp != null) {
				String idRep = repTemp.get("ID").toString();
				consultaRep = "SELECT * FROM V_RELACION_REPORTES";
				consultaRep += " WHERE ID_REPORTE = '" + idRep + "'";
				listReportes = consTablaEJB.consultarTabla(0, 0, null, consultaRep);
			}
			if (listReportes != null) {
				Iterator it = listReportes.iterator();
				if (it.hasNext()) {
					repTemp = (Hashtable) it.next();
					if (repTemp.get("ROS") == null || repTemp.get("ROS").toString().equals("null")) {
						repTemp.replace("ROS", "N.A");
					}
					String fechTemp = repTemp.get("FECHA_CREACION").toString();
					repTemp.replace("FECHA_CREACION", fechTemp.substring(0, 10).replace("-", "/"));
				}
				sesion.setAttribute("reporteSel", repTemp);
				contenidoCtr.updateUrl("/siscla/rep/sc/rep.jsf");
			} else {
				fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
				FacesContext.getCurrentInstance().addMessage(null, fcMsg);
			}
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|irReporte: " + e.getMessage());
			msg = "FrmpreinusualController|irReporte: " + e.getMessage();
			fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
	}

	private void consultaDatosTran() {
		System.out.println("Ingresa a consultar datos TRAN");
		index = "0";
		String codigoArchivoTr = (String) tranSelec.get("CODIGO_ARCHIVO");

		String idTr = (String) tranSelec.get("ID_TRANSACCION");

		String fechaProcesoTr = ((String) tranSelec.get("FECHA_PROCESO")).substring(0, 10).replace("-", "/");
		String pCondicion = " WHERE codigo_archivo = " + codigoArchivoTr + " AND fecha_proceso = TO_DATE('"
				+ fechaProcesoTr + "','YYYY/MM/DD')" + " AND id_transaccion = " + idTr;

		Collection resultados = null;
		try {
			resultados = consTablaEJB.consultarTabla(0, 0, "V_DETALLE_TR", pCondicion);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FrmpreinusualController|consultaDatosTran: Error select V_DETALLE_TR");
		}
		Hashtable colTran = null;
		if (resultados != null) {
			Iterator itr = resultados.iterator();
			if (itr.hasNext()) {
				colTran = (Hashtable) itr.next();
				colTran.replace("FECHA", funciones.formatoFecha(colTran.get("FECHA")));
				colTran.replace("FECHA_PROCESO", funciones.formatoFecha(colTran.get("FECHA_PROCESO")));
				colTran.replace("VALOR", funciones.formatoValor(colTran.get("VALOR")));
				String estadoO = ((String) colTran.get("CODIGO_ESTADO_OFICINA")).trim();
				String estadoD = (String) colTran.get("CODIGO_ESTADO_DUCC");
				if (estadoO != null && estadoO.equals("I") && rol.equals("2")) {
					puedeReportar = false;
				} else if (estadoD != null && (!estadoD.equals("N") && !estadoD.equals("P")) && rol.equals("5")) {
					puedeReportar = false;
				} else {
					puedeReportar = true;
				}
				if (estadoO != null && estadoO != null && estadoO.equals("N") && rol.equals("2")) {
					puedeReportarNormal = false;
				} else if (estadoD != null && estadoD.equals("N") && rol.equals("5")) {
					puedeReportarNormal = false;
				}
			}
		}
		detalleTran = colTran;
		System.out.println("detalleTran: " + detalleTran);
	}

	private void consultaFte() {
		String codigoArchivoTr = (String) tranSelec.get("CODIGO_ARCHIVO");
		String idTr = (String) tranSelec.get("ID_TRANSACCION");
		String fechaProcesoTr = ((String) tranSelec.get("FECHA_PROCESO")).substring(0, 10).replace("-", "/");
		String pNroCta = (String) tranSelec.get("N_PRODUCTO");
		String pCondicion = " WHERE codigo_archivo = " + codigoArchivoTr + " AND fecha_proceso = TO_DATE('"
				+ fechaProcesoTr + "','YYYY/MM/DD')" + " AND id_transaccion = " + idTr;
		Collection colDetalleFte = null;
		String vistaFte = "";
		if (tranSelec.get("FUENTE").toString().trim().equals("CA")) {
			vistaFte = "V_DETALLE_TR_CA";
		}
		if (tranSelec.get("FUENTE").toString().trim().equals("CC")) {
			vistaFte = "V_DETALLE_TR_CC";
		}
		if (tranSelec.get("FUENTE").toString().trim().equals("CDT")) {
			vistaFte = "V_DETALLE_TR_CDT";
		}
		try {
			colDetalleFte = consTablaEJB.consultarTabla(0, 0, vistaFte, pCondicion);
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|irDetalleTr: Error select " + vistaFte);
		}
		Hashtable hashFte = null;
		Hashtable cle = null;
		if (colDetalleFte != null) {
			Iterator itr = colDetalleFte.iterator();
			if (itr.hasNext()) { // OB
				hashFte = (Hashtable) itr.next();
				String codigoTransaccion = (String) hashFte.get("CODIGO_TRANSACCION");
				String fechaApertura = ((String) hashFte.get("FECHA_APERTURA")).substring(0, 10).replace("-", "/");
				hashFte.replace("FECHA_APERTURA", fechaApertura);
				if (!tranSelec.get("FUENTE").toString().trim().equals("CDT")) {
					String promedioSemestral = (String) hashFte.get("PROMEDIO_SEMESTRAL");
					String signoPromedio = (String) hashFte.get("SIGNO_PROMEDIO");
					signoPromedio = promedioSemestral.equals("0") ? "" : signoPromedio;
					hashFte.replace("SIGNO_PROMEDIO", signoPromedio);
					String pCondiClem = "WHERE fecha_proceso  = TO_DATE('" + fechaProcesoTr + "','YYYY/MM/DD')"
							+ " AND To_Number(numero_cuenta) = " + pNroCta + " AND codigo_transaccion = '"
							+ codigoTransaccion + "'";
					Collection colDetalleCLE = null;
					try {
						colDetalleCLE = consTablaEJB.consultarTabla(0, 0, "V_DETALLE_TR_CLE", pCondiClem);
					} catch (Exception e) {
						System.out.println("FrmpreinusualController|irDetalleTr: Error select V_DETALLE_TR_CLE");
					}
					if (colDetalleCLE != null) {
						Iterator iter = colDetalleCLE.iterator();
						if (iter.hasNext()) {
							cle = (Hashtable) iter.next();
						}
					}
				} else {
					String idTitular2 = (String) hashFte.get("ID_TITULAR_2");
					String nombreTitular2 = (String) hashFte.get("NOMBRE_TITULAR_2");
					String idTitular3 = (String) hashFte.get("ID_TITULAR_3");
					String nombreTitular3 = (String) hashFte.get("NOMBRE_TITULAR_3");
					String idTitular4 = (String) hashFte.get("ID_TITULAR_4");
					String nombreTitular4 = (String) hashFte.get("NOMBRE_TITULAR_4");
					String nombresTitulares = "";
					String idsTitulares = "";
					nombresTitulares = ((!idTitular2.equals("null") && !idTitular2.equals("0-0") ? idTitular2 + " "
							: "")
							+ (!nombreTitular2.equals("null") && !nombreTitular2.trim().equals("") ? nombreTitular2
									: "")
							+ (!idTitular3.equals("null") && !idTitular3.equals("0-0") ? idTitular3 + " " : "")
							+ (!nombreTitular3.equals("null") && !nombreTitular3.trim().equals("")
									? "; " + nombreTitular3
									: "")
							+ (!idTitular4.equals("null") && !idTitular4.equals("0-0") ? idTitular4 + " " : "")
							+ (!nombreTitular4.equals("null") && !nombreTitular4.trim().equals("")
									? "; " + nombreTitular4
									: ""));
					hashFte.replace("NOMBRE_TITULAR_2", nombresTitulares);
				}
			}
		}
		detalleFte = hashFte;
		detalleCLE = cle;
		// System.out.println("detalleFte: " + detalleFte);
		// System.out.println("detalleCLE: " + detalleCLE);
	}

	public void onTabChangeAction(TabChangeEvent event) {
		String idTab = event.getTab().getId();
		System.out.println("Ingredo al tab");
		System.out.println("idTab: " + idTab);
		if (idTab.equals("tab1")) {
			index = "0";
		}
		if (idTab.equals("tab2")) {
			procCriteriosInu();
			index = "1";
			PrimeFaces.current().ajax().update("frmDetalleTr:tabViewDetalleTr:pnlCritInu");
		}
		if (idTab.equals("tab3")) {
			procConceptos();
			index = "2";
		}
		if (idTab.equals("tab4")) {
			comentarios();
			index = "3";
		}

	}

	private void procCriteriosInu() {
		Consulta detalleTr = consultaEJB.realizarConsulta(cargo, "28");
		String codigoArchivoTr = (String) tranSelec.get("CODIGO_ARCHIVO");
		String idTr = (String) tranSelec.get("ID_TRANSACCION");
		String fechaProcesoTr = ((String) tranSelec.get("FECHA_PROCESO")).substring(0, 10).replace("-", "/");
		String pCondicion = " AND codigo_archivo = " + codigoArchivoTr + " AND fecha_proceso = TO_DATE('"
				+ fechaProcesoTr + "','YYYY/MM/DD')" + " AND id_transaccion = " + idTr;
		try {
			criteriosTr = facadeEJB.ejecutarConsultaGeneral(pCondicion, detalleTr, 0);
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|criteriosInu: " + e.getMessage());
		}
		System.out.println("criteriosTr: " + criteriosTr);
	}

	private void procConceptos() {
		String codigoArchivoTr = (String) tranSelec.get("CODIGO_ARCHIVO");
		String fechaProcesoTr = ((String) tranSelec.get("FECHA_PROCESO")).substring(0, 10).replace("-", "/");
		String idTr = (String) tranSelec.get("ID_TRANSACCION");
		String pCondicion = " AND codigo_archivo = " + codigoArchivoTr + " AND fecha_proceso  = TO_DATE('"
				+ fechaProcesoTr + "','YYYY/MM/DD')" + " AND id_transaccion = " + idTr;
		String pCondicionUsada = " WHERE codigo_archivo = " + codigoArchivoTr + " AND fecha_proceso  = TO_DATE('"
				+ fechaProcesoTr + "','YYYY/MM/DD')" + " AND id = " + idTr;
		Collection resultadosIniciales = null;
		try {
			resultadosIniciales = consTablaEJB.consultarTabla(0, 0, "TRANSACCIONES_CLIENTE", pCondicionUsada);
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|procConceptos: Error select TRANSACCIONES_CLIENTE");
		}
		String tipoId = null;
		String numeroId = null;
		if (resultadosIniciales != null) {
			Iterator itResultados = resultadosIniciales.iterator();
			while (itResultados.hasNext()) {
				Hashtable temporal = (Hashtable) itResultados.next();
				tipoId = (String) temporal.get("TIPO_IDENTIFICACION");
				numeroId = (String) temporal.get("NUMERO_IDENTIFICACION");
			}
		}
		String pCondicionInicial = " AND TIPO_IDENTIFICACION = '" + tipoId + "'" + " AND NUMERO_IDENTIFICACION = '"
				+ numeroId + "'" + " ORDER BY FECHA_CREACION DESC ";
		int rol = Integer.parseInt(this.rol);
		Consulta detalleTr = consultaEJB.realizarConsulta(cargo, "9");
		Collection resultados = null;
		try {
			resultados = facadeEJB.ejecutarConsultaGeneral(pCondicionInicial, detalleTr, 0);
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|procConceptos: Error select V_CONCEPTOS1");
		}
		conceptosTr = resultados;
		System.out.println("conceptosTr: " + conceptosTr);
		//PrimeFaces.current().ajax().update(":frmDetalleTr:tabViewDetalleTr:tablaTran");
	}

	public void comentarios() {
		String codigoArchivoTr = (String) tranSelec.get("CODIGO_ARCHIVO");
		String fechaProcesoTr = ((String) tranSelec.get("FECHA_PROCESO")).substring(0, 10).replace("-", "/");
		String idTr = (String) tranSelec.get("ID_TRANSACCION");
		// String idComentario = request.getParameter("pIdComentario");
		String pCondicion = " AND codigo_archivo = " + codigoArchivoTr + " AND fecha_proceso  = TO_DATE('"
				+ fechaProcesoTr + "','YYYY/MM/DD')" + " AND id_transaccion = " + idTr
				+ " GROUP BY FECHA_CREACION, NOMBRE, PERFIL, ID_COMENTARIO "
				+ " ORDER BY FECHA_CREACION DESC";
		Consulta detalleTr = null;
		try {
			detalleTr = consultaEJB.realizarConsulta(cargo, "7");
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|comentarios: Error consultaEJB.realizarConsulta");
		}
		Collection resultados = null;
		try {
			resultados = facadeEJB.ejecutarConsultaGeneral(pCondicion, detalleTr, 50);
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|comentarios: Error facadeEJB.ejecutarConsultaGeneral");
		}
		comentarios = resultados;
		System.out.println("comentarios: " + comentarios);
		PrimeFaces.current().ajax().update(":frmDetalleTr:tabViewDetalleTr:tablaComent");
	}

	public void detalleConcepto() {
		// System.out.println("conceptoSel: " + conceptoSel);
		String idReporte = (String) conceptoSel.get("1");
		// System.out.println("conceptoSel get 1: " + conceptoSel.get("1"));
		String pCondicion = " WHERE ID_REPORTE = " + idReporte;
		Consulta pConsulta = null;
		try {
			pConsulta = consultaEJB.realizarConsulta(cargo, "27");
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|detalleConcepto: Error select realizarConsulta 27");
		}
		Collection colConcepto = null;
		if (pConsulta != null) {
			try {
				colConcepto = facadeEJB.ejecutarConsultaGeneral(" AND ID = " + idReporte, pConsulta, 0);
			} catch (Exception e) {
				System.out.println("FrmpreinusualController|detalleConcepto: Error select reporte");
			}
		}
		detallesConcepto = colConcepto;
		// System.out.println("detallesConcepto: " + detallesConcepto);
		Collection colAnalisis = null;
		if (colConcepto != null) {
			try {
				colAnalisis = consTablaEJB.consultarTabla(0, 0, "V_ANALISIS_REPORTE", pCondicion);
			} catch (Exception e) {
				System.out.println("FrmpreinusualController|detalleConcepto: Error select V_ANALISIS_REPORTE");
			}
		}
		String justFinalTemp = "";
		if (colAnalisis != null) {
			Iterator it = colAnalisis.iterator();
			while (it.hasNext()) {
				Hashtable analisis = (Hashtable) it.next();
				justFinalTemp = ((String) analisis.get("JUSTIFICACION_FINAL")).equals("null") ? ""
						: justFinalTemp + " \n " + (String) analisis.get("JUSTIFICACION_FINAL");
			}
		}
		justFinal = justFinalTemp;
		PrimeFaces.current().executeScript("PF('dlgwvDetalleConc').show()");
	}

	public void detalleComent() {
		System.out.println("comentarioSel: " + comentarioSel);
		String idComentario = (String) comentarioSel.get("ID");
		String pCondicion = " WHERE id_comentario = " + idComentario;
		Collection colComentario = null;
		Hashtable detComTemp = null;
		try {
			colComentario = consTablaEJB.consultarTabla(0, 0, "V_COMENTARIOS", pCondicion);
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|detalleComent: Error select V_COMENTARIOS");
		}
		if (colComentario != null) {
			Iterator it = colComentario.iterator();
			if (it.hasNext()) {
				detComTemp = (Hashtable) it.next();
				System.out.println("detComTemp.get(FECHA): " + detComTemp.get("FECHA_CREACION"));
				String fechComent = ((String) detComTemp.get("FECHA_CREACION")).substring(0, 16);
				detComTemp.replace("FECHA_CREACION", fechComent);
			}
		}
		detalleComentario = detComTemp;
		PrimeFaces.current().executeScript("PF('dlgwvDetalleComent').show()");
	}

	public void guardarComent() {
		msg = "";
		tipMsg = "";
		try {
			Long idUsuario = new Long(usuario);
			String rolUsu = rol;
			String codigoArchivoTr = (String) tranSelec.get("CODIGO_ARCHIVO");
			String fechaProcesoTr = ((String) tranSelec.get("FECHA_PROCESO")).substring(0, 10);
			String idTr = (String) tranSelec.get("ID_TRANSACCION");
			String comentario = coment;
			java.sql.Date fechProceso = java.sql.Date.valueOf(fechaProcesoTr);
			Timestamp fechCreacion = new Timestamp(System.currentTimeMillis());
			Hashtable h = new Hashtable();
			h.put("ID_TRANSACCION", Integer.valueOf(idTr));
			h.put("FECHA_PROCESO", fechProceso);
			h.put("CODIGO_ARCHIVO", Integer.valueOf(codigoArchivoTr));
			h.put("USUARIO_CREACION", idUsuario);
			h.put("FECHA_CREACION", fechCreacion);
			h.put("COMENTARIO", comentario);
			h.put("PERFIL", rolUsu);
			comentarioEJB.create(h);
			comentarios();
			msg = "Comentario guardado con exito";
			tipMsg = "Ok: ";
			coment = "";
		} catch (Exception e) {
			msg = "No se pudo guardar el comentario";
			tipMsg = "Error: ";
			System.out.println("FrmpreinusualController|guardarComent: " + e.getMessage());
		}
		coment = "";
		facesMsg = new FacesMessage("Resultado proceso: ", tipMsg + msg);
		facContex = FacesContext.getCurrentInstance();
		facContex.addMessage(null, facesMsg);
	}

	/*
	 * Metodo ejecutado en el onload del jsf consultaCliente.jsf. 
	 * El anterior jsf es ejecutado desde la opcion de menu Clientes Preinusuales
	 * */
	public void consuntaClientesConTxPrein() {
		Consulta pConsulta;
		
		if (tipoCargo.equals("N")) {

			 pConsulta = consultaEJB.realizarConsultaTipoCargo(cargo, "30");
		} else {
			 pConsulta = consultaEJB.realizarConsulta(cargo, "30");
		}
	
		try {
			String sOrder = pConsulta.getAgrupacion();
			pConsulta.setAgrupacion(
					sOrder + " ORDER BY TOTAL DESC, T.TIPO_IDENTIFICACION, TO_NUMBER(T.NUMERO_IDENTIFICACION)");
			trans = facadeEJB.ejecutarConsultaGeneral(" ", pConsulta, 1000);
			cantClientesPre= trans.size();
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|consuntaClientesConTxPrein:");
		}
		System.out.println("trans" + trans);
	}

	private void consultaDetalleClienteConTxPrein() {
		String consulta = "";
		String un="";
		String ti = tranSelec.get("TI").toString();
		String ni = tranSelec.get("NI").toString();
		if(tipoCargo.equals("N")) {
		 un = tranSelec.get("OFICINA").toString();
		}else {
		 un = datosRegistro.get("CODIGO_UNIDAD_NEGOCIO").toString();
		}
		if (rol.equals("5")) {
			consulta = "SELECT * FROM ( " + "SELECT v.* " + "FROM v_transacciones v " + ", unidades_negocio un "
					+ ", analista_region ar " + "WHERE v.tipo_identificacion = '" + ti + "' "
					+ "AND v.numero_identificacion = '" + ni + "' " + "AND v.codigo_estado_ducc = 'P' "
					+ "AND v.codigo_oficina = un.codigo " + "AND un.codigo_region_v = ar.codigo_region_v "
					+ "AND ar.codigo_cargo = '" + cargo + "' " + "ORDER BY mayor_riesgo DESC, valor DESC "
					+ ") WHERE ROWNUM < 2 ";
		} else {
			consulta = " SELECT * FROM ( " + " SELECT * FROM v_transacciones " + " WHERE tipo_identificacion = '" + ti
					+ "' " + " AND numero_identificacion = '" + ni + "' " + " AND codigo_estado_oficina = 'P' "
					+ " AND codigo_oficina = '" + un + "'" + " ORDER BY mayor_riesgo DESC, valor DESC "
					+ ") WHERE ROWNUM < 2";
		}
		try {
			Collection miColeccion = consTablaEJB.consultarTabla(0, 0, null, consulta);
			Iterator it = miColeccion.iterator();
			while (it.hasNext()) {
				Hashtable h = (Hashtable) it.next();
				tranSelec.put("FECHA_PROCESO", h.get("FECHA_PROCESO"));
				tranSelec.put("CODIGO_ARCHIVO", h.get("CODIGO_ARCHIVO"));
				tranSelec.put("ID_TRANSACCION", h.get("ID_TRANSACCION"));
				tranSelec.put("FUENTE", h.get("FUENTE"));
			}
		} catch (Exception e) {
			System.out.println("FrmpreinusualController|consultaDetalleClienteConTxPrein: " + e.getMessage());
		}
		System.out.println("esto es urlvolver en consultaDetalleClienteConTxPrein :"+urlVolver);
	}

	public void setComentarios(Collection comentarios) {
		this.comentarios = comentarios;
	}

	public Collection getComentarios() {
		return comentarios;
	}

	public void setRegiones(Collection regiones) {
		this.regiones = regiones;
	}

	public Collection getRegiones() {
		return regiones;
	}

	public void setZonas(Collection zonas) {
		this.zonas = zonas;
	}

	public Collection getZonas() {
		return zonas;
	}

	public void setUnRegs(Collection unRegs) {
		this.unRegs = unRegs;
	}

	public Collection getUnRegs() {
		return unRegs;
	}

	public void setRegion(Hashtable region) {
		this.region = region;
	}

	public Hashtable getRegion() {
		return region;
	}

	public void setZona(Hashtable zona) {
		this.zona = zona;
	}

	public Hashtable getZona() {
		return zona;
	}

	public void setUnReg(Hashtable unReg) {
		this.unReg = unReg;
	}

	public Hashtable getUnReg() {
		return unReg;
	}

	public void setFtes(Collection ftes) {
		this.ftes = ftes;
	}

	public Collection getFtes() {
		return ftes;
	}

	public void setFte(Hashtable fte) {
		this.fte = fte;
	}

	public Hashtable getFte() {
		return fte;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}

	public String getFuente() {
		return fuente;
	}

	public void setCodTr(String codTr) {
		this.codTr = codTr;
	}

	public String getCodTr() {
		return codTr;
	}

	public void setRegionStr(String regionStr) {
		this.regionStr = regionStr;
	}

	public String getRegionStr() {
		return regionStr;
	}

	public void setZonaStr(String zonaStr) {
		this.zonaStr = zonaStr;
	}

	public String getZonaStr() {
		return zonaStr;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getOficina() {
		return oficina;
	}

	public void setTiposFuente(Collection tiposFuente) {
		this.tiposFuente = tiposFuente;
	}

	public Collection getTiposFuente() {
		return tiposFuente;
	}

	public void setTiposDoc(Collection tiposDoc) {
		this.tiposDoc = tiposDoc;
	}

	public Collection getTiposDoc() {
		return tiposDoc;
	}

	public void setTipDoc(String tipDoc) {
		this.tipDoc = tipDoc;
	}

	public String getTipDoc() {
		return tipDoc;
	}

	public void setTiposEstado(Collection tiposEstado) {
		this.tiposEstado = tiposEstado;
	}

	public Collection getTiposEstado() {
		return tiposEstado;
	}

	public void setEstadoTr(String estadoTr) {
		this.estadoTr = estadoTr;
	}

	public String getEstadoTr() {
		return estadoTr;
	}

	public void setEstadoD(String estadoD) {
		this.estadoD = estadoD;
	}

	public String getEstadoD() {
		return estadoD;
	}

	public void setIdenti(String identi) {
		this.identi = identi;
	}

	public String getIdenti() {
		return identi;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setComent(String coment) {
		this.coment = coment;
	}

	public String getComent() {
		return coment;
	}

	public void setFiltro(boolean filtro) {
		this.filtro = filtro;
	}

	public boolean isFiltro() {
		return filtro;
	}

	public void setNueva(boolean nueva) {
		this.nueva = nueva;
	}

	public boolean isNueva() {
		return nueva;
	}

	public void setFducc(boolean fducc) {
		this.fducc = fducc;
	}

	public boolean isFducc() {
		return fducc;
	}

	public void setMayorR(boolean mayorR) {
		this.mayorR = mayorR;
	}

	public boolean isMayorR() {
		return mayorR;
	}

	public void setRegnsConsTr(Collection regnsConsTr) {
		this.regnsConsTr = regnsConsTr;
	}

	public Collection getRegnsConsTr() {
		return regnsConsTr;
	}

	public void setZonasConsTr(Collection zonasConsTr) {
		this.zonasConsTr = zonasConsTr;
	}

	public Collection getZonasConsTr() {
		return zonasConsTr;
	}

	public void setTrans(Collection trans) {
		this.trans = trans;
	}

	public Collection getTrans() {
		return trans;
	}

	public void setTranSelec(Hashtable tranSelec) {
		this.tranSelec = tranSelec;
	}

	public Hashtable getTranSelec() {
		return tranSelec;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setNroProd(String nroProd) {
		this.nroProd = nroProd;
	}

	public String getNroProd() {
		return nroProd;
	}

	public void setContenidoCtr(ContenidoController contenidoCtr) {
		this.contenidoCtr = contenidoCtr;
	}

	public ContenidoController getContenidoCtr() {
		return contenidoCtr;
	}

	public void setdetalleFte(Hashtable detalleFte) {
		this.detalleFte = detalleFte;
	}

	public Hashtable getdetalleFte() {
		return detalleFte;
	}

	public void setDetalleCLE(Hashtable detalleCLE) {
		this.detalleCLE = detalleCLE;
	}

	public Hashtable getDetalleCLE() {
		return detalleCLE;
	}

	public void setDetalleTran(Hashtable detalleTran) {
		this.detalleTran = detalleTran;
	}

	public Hashtable getDetalleTran() {
		return detalleTran;
	}

	public void setCriteriosTr(Collection criteriosTr) {
		this.criteriosTr = criteriosTr;
	}

	public Collection getCriteriosTr() {
		return criteriosTr;
	}

	public void setConceptosTr(Collection conceptosTr) {
		this.conceptosTr = conceptosTr;
	}

	public Collection getConceptosTr() {
		return conceptosTr;
	}

	public void setConceptoSel(Hashtable conceptoSel) {
		this.conceptoSel = conceptoSel;
	}

	public Hashtable getConceptoSel() {
		return conceptoSel;
	}

	public void setJustFinal(String justFinal) {
		this.justFinal = justFinal;
	}

	public String getJustFinal() {
		return justFinal;
	}

	public void setDetallesConcepto(Collection detallesConcepto) {
		this.detallesConcepto = detallesConcepto;
	}

	public Collection getDetallesConcepto() {
		return detallesConcepto;
	}

	public void setComentarioSel(Hashtable comentarioSel) {
		this.comentarioSel = comentarioSel;
	}

	public Hashtable getComentarioSel() {
		return comentarioSel;
	}

	public void setDetalleComentario(Hashtable detalleComentario) {
		this.detalleComentario = detalleComentario;
	}

	public Hashtable getDetalleComentario() {
		return detalleComentario;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getIndex() {
		return index;
	}

	public boolean isPuedeComentar() {
		return puedeComentar;
	}

	public boolean isPuedeReportar() {
		return puedeReportar;
	}

	public boolean isPuedeReportarNormal() {
		return puedeReportarNormal;
	}

	public String getRol() {
		return rol;
	}

	public String getCargo() {
		return cargo;
	}

	public void setUrlVolver(String urlVolver) {
		this.urlVolver = urlVolver;
	}

	public String getUrlVolver() {
		return urlVolver;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setListavaloresCtr(ListavaloresController listavaloresCtr) {
		this.listavaloresCtr = listavaloresCtr;
	}

	public ListavaloresController getListavaloresCtr() {
		return listavaloresCtr;
	}

	public void setLogCtr(LogController logCtr) {
		this.logCtr = logCtr;
	}

	public LogController getLogCtr() {
		return logCtr;
	}

	public void setTotalReg(Hashtable totalReg) {
		this.totalReg = totalReg;
	}

	public Hashtable getTotalReg() {
		return totalReg;
	}

	public void setTotalZon(Hashtable totalZon) {
		this.totalZon = totalZon;
	}

	public Hashtable getTotalZon() {
		return totalZon;
	}

	public void setTotalOfi(Hashtable totalOfi) {
		this.totalOfi = totalOfi;
	}

	public Hashtable getTotalOfi() {
		return totalOfi;
	}

	public void setTotalFte(Hashtable totalFte) {
		this.totalFte = totalFte;
	}

	public Hashtable getTotalFte() {
		return totalFte;
	}

	private String dateToString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		if (fecha != null) {
			return format.format(fecha);
		}
		return null;
	}

	public boolean isDisabled2() {
		return disabled2;
	}

	public void setDisabled2(boolean disabled2) {
		this.disabled2 = disabled2;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getCantClientesPre() {
		return cantClientesPre;
	}

	public void setCantClientesPre(int cantClientesPre) {
		this.cantClientesPre = cantClientesPre;
	}


	public String getTipoCargo() {
		return tipoCargo;
	}


	public void setTipoCargo(String tipoCargo) {
		this.tipoCargo = tipoCargo;
	}
}
