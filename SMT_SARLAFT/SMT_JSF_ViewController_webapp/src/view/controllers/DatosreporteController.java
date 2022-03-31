package view.controllers;

import admin.seguridad.LogConsultaEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import com.proycomp.forbbog.genericos.principal4ora817.Message;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import javax.ejb.CreateException;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import javax.naming.InitialContext;

import javax.naming.NamingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;

import presentacion.FacadeEJB;

import transaccion.ReporteEJB;
import transaccion.DetalleAnalisisRepEJB;

@ManagedBean(name = "DatosreporteController")
@SessionScoped
public class DatosreporteController {
	@EJB
	private FacadeEJB facadeEJB;
	@EJB
	private ConsultaTablaEJB consTablaEJB;
	@EJB
	private ReporteEJB reporteEJB;
	@EJB
	private DetalleAnalisisRepEJB detalleAnaliEJB;
	@ManagedProperty("#{FrmpreinusualController}")
	private FrmpreinusualController frmpreinusualCtr;
	@ManagedProperty("#{ListavaloresController}")
	private ListavaloresController listavaloresCtr;
	@ManagedProperty("#{LogController}")
	private LogController logCtr;
	private Funciones funciones;
	private FacesContext facContex;
	private ExternalContext extContex;
	private HttpSession sesion;
	private FacesMessage fcMsg;
	private Date fechaInicioVinculacion;
	private Date fechaFinVinculacion;
	private Date fechaIngresosMensual;
	private String rol;
	private String cargo;
	private String usuario;
	private String tabIndex;
	private String descripcion;
	private String tipoId;
	private String labelNombre;
	private String labelApellidos;
	private String valorNombre;
	private String valorApellidos;
	private String email;
	private String relacion;
	private String otraRelacion;
	private String ciiu;
	private String actividadEconomica;
	private String tipoTelefono;
	private String fax;
	private String tipoDireccion;
	private String municipio;
	private String direccion;
	private String tipoIdReprLegal;
	private String nombreReprLegal;
	private String apellidosReprLegal;
	private String otro;
	private String msg;
	private String proceso;
	private String idRep;
	private String claseRep;
	private String razonRetiro;
	private String displayRepLeg;
	private String txtBuscMun;
	private String txtBuscCiiu;
	private String fechaReporte;
	private String numeroActa;
	private String fechaActa;
	private String rosRelacionado;
	private String estadoReporte;
	private String tipoReporte;
	private String justReporte;
	private String detalleJust;
	private String decision_RosRel;
	private String disabledEstRep;
	private String disabledROS;
	private String displayTipRep;
	private String displayDescRep;
	private String numeroIdReprLegal;
	private String numeroId;
	private String ingresosMensuales;
	private String telefono;
	private boolean tabPerRelActivada;
	private boolean tabDocSopActivada;
	private boolean continuar;
	private boolean disabledRazRet;
	private boolean flagCU;
	private boolean tabReporte;
	private boolean tabAnalisis;
	private boolean tabProcAnalis;
	private boolean readFechActa;
	private Collection tiposDoc;
	private Collection actdsEcon;
	private Collection actdsEconFiltr;
	private Collection razonesRetiro;
	private Collection municipios;
	private Collection estadosReporte;
	private Collection tiposReporte;
	private Collection listAnalisis;
	private Collection numRos;
	private List docsSoporte;
	private Hashtable actEcoSel;
	private Hashtable respuestas;
	private Hashtable municipioSel;
	private Hashtable datosRegistro;
	private Hashtable rosSel;

	// jfore18
	byte[] encoded = null;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		cargo = (String) datosRegistro.get("CODIGO_CARGO");
		rol = (String) datosRegistro.get("CODIGO_PERFIL");
		usuario = (String) datosRegistro.get("CODIGO_USUARIO");
		funciones = new Funciones();
		tabIndex = "0";
		labelNombre = "Nombre";
		labelApellidos = "Apellidos";
		tiposDoc = listavaloresCtr.getTiposDocumento();
		tabPerRelActivada = false;
		tabDocSopActivada = false;
		sesion.setAttribute("tabDocSopActivada", tabDocSopActivada);
		valorNombre = "";
		proceso = "ReportarCliente";
		flagCU = false;
		tiposReporte = null;
		numRos=null;
		justReporte="";
		listAnalisis=null;
		
	}

	private void consultarDatosReporte() {
		if (idRep != null && !idRep.isEmpty()) {
			try {
				String consultaRep = "SELECT * FROM REPORTE WHERE ID = '" + idRep + "'";
				System.out.println("consultaRep: " + consultaRep);
				Collection colRep = consTablaEJB.consultarTabla(0, 0, null, consultaRep);
				Iterator itRep = colRep.iterator();
				if (itRep.hasNext()) {
					Hashtable htRep = (Hashtable) itRep.next();
					funciones.quitarNull(htRep);
					try {
						tipoId = htRep.get("TIPO_IDENTIFICACION").toString();
					} catch (Exception e) {
						tipoId = "";
					}
					try {
						numeroId = htRep.get("NUMERO_IDENTIFICACION").toString();
					} catch (Exception e) {

					}
					try {
						descripcion = htRep.get("JUSTIFICACION_INICIAL").toString();
					} catch (Exception e) {
						descripcion = "";
					}
					try {
						otro = htRep.get("OTRO_DOCUMENTO_SOPORTE").toString();
					} catch (Exception e) {
						descripcion = "";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void iniciarReporte(String proc) {
		proceso = proc;
		sesion.setAttribute("procesoRep", proceso);
		tabPerRelActivada = false;
		tabDocSopActivada = false;
		tabIndex = "0";
		docsSoporte = null;
		otro = "";
		tipoId = "";
		numeroId = "";
		valorNombre = "";
		valorApellidos = "";
		email = "";
		relacion = "";
		otraRelacion = "";
		fechaInicioVinculacion = null;
		fechaFinVinculacion = null;
		razonRetiro = "";
		ingresosMensuales = "";
		fechaIngresosMensual = null;
		actEcoSel = null;
		tipoTelefono = "";
		telefono = "";
		fax = "";
		tipoDireccion = "";
		municipioSel = null;
		municipio = "";
		direccion = "";
		tipoIdReprLegal = "";
		numeroIdReprLegal = "";
		nombreReprLegal = "";
		apellidosReprLegal = "";
		displayRepLeg = "none";
		actividadEconomica = "";
		disabledRazRet = true;
		idRep = proceso.equals("ReportarCliente") ? null : (String) sesion.getAttribute("idRep");
		descripcion = "";
		if (proceso.equals("ActualizarReporte")) {
			consultarDatosReporte();
		}
		flagCU = false;
		disabledEstRep = "true";
		disabledROS="true";
		fechaReporte = "";
		numeroActa = "";
		fechaActa = "";
		estadosReporte = null;
		tabReporte = false;
		tabAnalisis = false;
		tabProcAnalis = false;
		displayDescRep = "none";
		displayTipRep = "none";
		decision_RosRel = "";
		readFechActa = false;
		rosRelacionado = "";
		estadoReporte=null;
		tipoReporte=null;
		justReporte="";
	}

	public void onTabChange(TabChangeEvent event) {
		String idTab = event.getTab().getId();
		if (idTab.equals("tabEnc")) {
			tabIndex = "0";
		}
		if (idTab.equals("tabPerRel")) {
			tabIndex = "1";
			if (!tabPerRelActivada) {
				tabPerRelActivada = true;
				if (proceso.equals("RealizarGestion_ReportarCliente")) {
					iniciarElementosFormularioCliente();
					PrimeFaces.current().ajax().update("frmReportar:tabViewReporte:pnlgrdCliente");
					if (displayRepLeg == "block") {
						PrimeFaces.current().ajax().update("frmReportar:tabViewReporte:pnlgrdRepresentanteLegal");

					}

				}
				if (proceso.equals("ActualizarReporte")) {
					iniciarElementosFormularioCliente();
					PrimeFaces.current().ajax().update("frmActRep:tabViewActReporte:tabViewReporte:pnlgrdCliente");

					if (displayRepLeg == "block") {
						PrimeFaces.current().ajax()
								.update("frmActRep:tabViewActReporte:tabViewReporte:pnlgrdRepresentanteLegal");
					}
				}
				PrimeFaces.current().ajax().update("frmCabecera:messages");
			}
		}
		if (idTab.equals("tabDocSop")) {
			tabIndex = proceso.equals("ReportarTrans") ? "1" : "2";
			if (!tabDocSopActivada) {
				tabDocSopActivada = true;
				sesion.setAttribute("tabDocSopActivada", tabDocSopActivada);
				if (!proceso.equals("ReportarCliente")) {
					idRep = sesion.getAttribute("idRep") != null ? sesion.getAttribute("idRep").toString() : null;
				}
				consultarDocSoporte();
				if (proceso.equals("ReportarTrans")) {
					PrimeFaces.current().ajax().update("frmGestion:tabViewGestion:tabViewReporte:pgDocsSoporte");
				} else if (proceso.equals("ActualizarReporte")) {
					PrimeFaces.current().ajax().update("frmActRep:tabViewActReporte:tabViewReporte:pgDocsSoporte");
				} else {
					PrimeFaces.current().ajax().update("frmReportar:tabViewReporte:pgDocsSoporte");
				}
			}
		}
	}

	public void iniciarElementosFormularioCliente() {
		Hashtable detalleTran = getFrmpreinusualCtr().getDetalleTran();
		try {
			tipoId = (String) detalleTran.get("TIPO_IDENTIFICACION");
		} catch (Exception e) {
			tipoId = "";
		}
		try {
			numeroId = (String) detalleTran.get("NUMERO_IDENTIFICACION");
		} catch (Exception e) {
			numeroId = "";
		}
		int idReporte = Integer.valueOf(sesion.getAttribute("idRep").toString().trim());
		boolean existeCliente = false;
		Collection cliente = reporteEJB.mostrarPersonas(new Integer(idReporte));
		Iterator itCliente = cliente.iterator();
		while (itCliente.hasNext()) {
			existeCliente = true;
			Hashtable datosCliente = (Hashtable) itCliente.next();
			datosCliente = funciones.quitarNull(datosCliente);
			tipoId = (String) datosCliente.get("TIPO_IDENTIFICACION");
			String numId = (String) datosCliente.get("NUMERO_IDENTIFICACION");
			numeroId = numId;
			valorNombre = (String) datosCliente.get("NOMBRES_RAZON_COMERCIAL");
			valorApellidos = (String) datosCliente.get("APELLIDOS_RAZON_SOCIAL");
			String codMunicipio = (String) datosCliente.get("CODIGO_MUNICIPIO");
			if (codMunicipio != null && !codMunicipio.isEmpty()) {
				Collection cMun = null;
				try {
					String condicion = " WHERE CODIGO = '" + codMunicipio + "'";
					cMun = consTablaEJB.consultarTabla(0, 0, "MUNICIPIO", condicion);
				} catch (Exception e) {
					System.out.println("DatosreporteController|iniciarElementosFormularioCliente: " + e.getMessage());
				}
				if (cMun != null) {
					Iterator iMun = cMun.iterator();
					while (iMun.hasNext()) {
						Hashtable hMun = (Hashtable) iMun.next();
						municipio = (String) hMun.get("MUNICIPIO") + "-" + (String) hMun.get("DEPARTAMENTO");
						municipioSel = hMun;
					}
				}
			}
			email = (String) datosCliente.get("DIRECCION_EMAIL");
			relacion = (String) datosCliente.get("TIPO_RELACION_V");
			otraRelacion = (String) datosCliente.get("OTRA_RELACION");
			String fechaIniVincula = (String) datosCliente.get("INICIO_VINCULACION");
			fechaIniVincula = funciones.formatoFecha(fechaIniVincula);
			fechaInicioVinculacion = !fechaIniVincula.isEmpty() ? (Date) funciones.stringToDate(fechaIniVincula) : null;
			String fechaFinVincula = (String) datosCliente.get("FINAL_VINCULACION");
			fechaFinVincula = funciones.formatoFecha(fechaFinVincula);
			fechaFinVinculacion = !fechaFinVincula.isEmpty() ? (Date) funciones.stringToDate(fechaFinVincula) : null;
			razonRetiro = (String) datosCliente.get("RAZON_RETIRO_V");
			String ingresosMens = (String) datosCliente.get("INGRESOS_MENSUALES");
			ingresosMensuales = ingresosMens;
			String fechaIngreso = (String) datosCliente.get("FECHA_INGRESOS");
			fechaIngreso = funciones.formatoFecha(fechaIngreso);
			fechaIngresosMensual = !fechaIngreso.isEmpty() ? (Date) funciones.stringToDate(fechaIngreso) : null;
			ciiu = (String) datosCliente.get("CODIGO_CIIU");
			if (ciiu != null && !ciiu.isEmpty()) {
				Collection cCiiu = null;
				try {
					String condicion = " WHERE CODIGO = '" + ciiu + "'";
					cCiiu = consTablaEJB.consultarTabla(0, 0, "ACTIVIDAD_ECONOMICA", condicion);
				} catch (Exception e) {
					System.out.println("DatosreporteController|iniciarElementosFormularioCliente: " + e.getMessage());
				}
				if (cCiiu != null) {
					Iterator iCiiu = cCiiu.iterator();
					while (iCiiu.hasNext()) {
						Hashtable hCiiu = (Hashtable) iCiiu.next();
						actEcoSel = hCiiu;
					}
				}
			}
			actividadEconomica = (String) datosCliente.get("DESCRIPCION_ACTIVIDAD_EC");
			tipoTelefono = (String) datosCliente.get("CODIGO_TIPO_TELEFONO_V");
			String telefon = (String) datosCliente.get("TELEFONO");
			telefono = telefon != null && !telefon.isEmpty() ? telefon : null;
			fax = (String) datosCliente.get("FAX");
			direccion = (String) datosCliente.get("DIRECCION");
			tipoDireccion = (String) datosCliente.get("CODIGO_TIPO_DIRECCION_V");
			if (tipoId.equals("N")) {
				try {
					apellidosReprLegal = (String) datosCliente.get("APELLIDOS_REP_LEGAL");
				} catch (Exception e) {
				}
				try {
					nombreReprLegal = (String) datosCliente.get("NOMBRES_REP_LEGAL");
				} catch (Exception e) {
				}
				try {
					tipoIdReprLegal = (String) datosCliente.get("TI_REP_LEGAL");
				} catch (Exception e) {
				}
				try {
					numeroIdReprLegal = (String) datosCliente.get("NI_REP_LEGAL");
				} catch (Exception e) {
				}
				displayRepLeg = "block";
			}
		}
		if (!existeCliente && !proceso.equals("ActualizarReporte")) {
			consultarClienteCRM("2");
		}
		validarDisplayRazonSocial();
	}

	public void consultarDocSoporte() {
		respuestas = new Hashtable();
		if (idRep != null) {
			Hashtable respuestasReporte = reporteEJB.mostrarRespuestas(new Integer(idRep.trim()));
			Collection respuestasB = (Collection) respuestasReporte.get("RESPUESTAS_BOOLEANAS");
			Collection respuestasS = (Collection) respuestasReporte.get("RESPUESTAS_TEXTO");
			if (!respuestasB.isEmpty()) {
				Iterator itR = respuestasB.iterator();
				while (itR.hasNext()) {
					Hashtable h = (Hashtable) itR.next();
					String codigoPregunta = (String) h.get("ID_PREGUNTA");
					String respuesta = (String) h.get("RESPUESTA");
					respuestas.put(codigoPregunta, respuesta);
				}
			}
			if (!respuestasS.isEmpty()) {
				Iterator itR = respuestasS.iterator();
				while (itR.hasNext()) {
					Hashtable h = (Hashtable) itR.next();
					try {
						String codigoPregunta = (String) h.get("ID_PREGUNTA");
						String respuesta = (String) h.get("RESPUESTA");
						respuestas.put(codigoPregunta, respuesta);
					} catch (Exception er) {
						String otrod = (String) h.get("OTRO_DOCUMENTO_SOPORTE");
						if (otrod != null && !otrod.equals("null")) {
							respuestas.put("OTRO_DOCUMENTO_SOPORTE", otrod);
						}
						String indag = (String) h.get("INDAGACION");
						if (indag != null && !indag.equals("null")) {
							respuestas.put("INDAGACION", indag);
						}
						String tipCons = (String) h.get("CODIGO_TIPO_CONSULTA_V");
						if (tipCons != null && !tipCons.equals("null")) {
							respuestas.put("CODIGO_TIPO_CONSULTA_V", tipCons);
						}
					}
				}
			}
		}
		docsSoporte = new ArrayList();
		String inConsulta = "('R','S','O')";
		if (claseRep != null && claseRep.equals("NORMAL")) {
			inConsulta = "('N')";
		}
		String consulta = " WHERE TIPO_PREGUNTA_V IN " + inConsulta
				+ " AND TRUNC(SYSDATE) BETWEEN TRUNC(VIGENTE_DESDE) AND TRUNC(VIGENTE_HASTA) ORDER BY ID";
		Collection c = null;
		try {
			c = consTablaEJB.consultarTabla(0, 0, "PREGUNTAS_REP", consulta);
		} catch (Exception e) {
			System.out.println("DatosreporteController|consultarDocSoporte: " + e.getMessage());
		}
		List listTemp = new ArrayList();
		Iterator iter = c.iterator();
		while (iter.hasNext()) {
			Hashtable docTemp = (Hashtable) iter.next();
			listTemp.add(docTemp);
		}
		List listConceptosTemp = new ArrayList();
		for (int k = 0; k < listTemp.size(); k++) {
			Hashtable hashTemp = (Hashtable) listTemp.get(k);
			String conceptoTemp = (String) hashTemp.get("DESCRIPCION");
			if (!listConceptosTemp.contains(conceptoTemp)) {
				List listTemp2 = new ArrayList();
				hashTemp.put("CHECKED", false);
				if (respuestas.containsKey(hashTemp.get("ID"))
						&& ((String) respuestas.get(hashTemp.get("ID"))).equals("1")) {
					hashTemp.replace("CHECKED", true);
				}
				listTemp2.add(hashTemp);
				for (int i = k + 1; i < listTemp.size(); i++) {
					Hashtable hashTemp2 = (Hashtable) listTemp.get(i);
					String conceptoTemp2 = (String) hashTemp2.get("DESCRIPCION");
					if (conceptoTemp2.endsWith(conceptoTemp)) {
						hashTemp2.put("DISABLED", true);
						if ((Boolean) hashTemp.get("CHECKED")) {
							hashTemp2.replace("DISABLED", false);
						}
						hashTemp2.put("VALOR", "");
						if (respuestas.containsKey(hashTemp2.get("ID"))) {
							hashTemp2.replace("VALOR", respuestas.get(hashTemp2.get("ID")));
						}
						listTemp2.add(hashTemp2);
						listConceptosTemp.add(conceptoTemp2);
					}
				}
				docsSoporte.add(listTemp2);
			}
		}
		System.out.println("docsSoporte: " + docsSoporte);
	}

	// jfore18
	public void reportar() {
		validaciones();
		if (continuar) {
			Hashtable datos = new Hashtable();
			String nombreUsuario = (String) datosRegistro.get("USUARIO_NT"); // jfore18
			datos.put("NOMBRE_USUARIO", nombreUsuario);// JFORE18
			datos.put("JUSTIFICACION_INICIAL", descripcion);
			datos.put("OTRO_DOCUMENTO_SOPORTE", otro);
			datos.put("TIPO_IDENTIFICACION", tipoId);
			datos.put("NUMERO_IDENTIFICACION", numeroId);
			datos.put("NOMBRES_RAZON_COMERCIAL", valorNombre);
			datos.put("APELLIDOS_RAZON_SOCIAL", valorApellidos);
			datos.put("DIRECCION_EMAIL", email != null ? email : "");
			datos.put("TIPO_RELACION_V", relacion != null ? relacion : "");
			datos.put("OTRA_RELACION", otraRelacion != null ? otraRelacion : "");
			String fecha = fechaInicioVinculacion != null ? funciones.dateToString(fechaInicioVinculacion) : "";
			datos.put("F_INICIO_VINCULACION", fecha);
			fecha = fechaFinVinculacion != null ? funciones.dateToString(fechaFinVinculacion) : "";
			datos.put("F_FINAL_VINCULACION", fecha);
			datos.put("RAZON_RETIRO_V", razonRetiro != null ? razonRetiro : "");
			datos.put("G_INGRESOS_MENSUALES", ingresosMensuales != null ? ingresosMensuales : "");
			fecha = fechaIngresosMensual != null ? funciones.dateToString(fechaIngresosMensual) : "";
			datos.put("F_FECHA_INGRESOS", fecha);	
			if (actEcoSel != null && !actEcoSel.isEmpty()) {
            	ciiu = actEcoSel.get("CODIGO").toString() ;
            	datos.put("CODIGO_CIIU", ciiu);
            	ciiu = actEcoSel.get("DESCRIPCION").toString();
                datos.put("CIIU", ciiu);
               
            }
			
			datos.put("DESCRIPCION_ACTIVIDAD_EC", actividadEconomica != null ? actividadEconomica : "");
			datos.put("CODIGO_TIPO_TELEFONO_V", tipoTelefono != null ? tipoTelefono : "");
			datos.put("TELEFONO", telefono != null ? telefono : "");
			datos.put("FAX", fax != null ? fax : "");
			datos.put("CODIGO_TIPO_DIRECCION_V", tipoDireccion != null ? tipoDireccion : "");
			String codOficina = (String) datosRegistro.get("CODIGO_UNIDAD_NEGOCIO"); // jfore18
			datos.put("CODIGO_OFICINA", codOficina);// JFORE18
			municipio = municipioSel != null && municipioSel.get("CODIGO") != null
					? municipioSel.get("CODIGO").toString()
					: "";
			datos.put("CODIGO_MUNICIPIO", municipio);
			datos.put("DIRECCION", direccion != null ? direccion : "");
			if (tipoId.equals("N") && tipoIdReprLegal != null && !tipoIdReprLegal.isEmpty()) {
				datos.put("TI_REP_LEGAL", tipoIdReprLegal != null ? tipoIdReprLegal : "");
				datos.put("NI_REP_LEGAL", numeroIdReprLegal != null ? numeroIdReprLegal : "");
				datos.put("NOMBRES_REP_LEGAL", nombreReprLegal != null ? nombreReprLegal : "");
				datos.put("APELLIDOS_REP_LEGAL", apellidosReprLegal != null ? apellidosReprLegal : "");

			}
			String codCargo = (String) datosRegistro.get("CODIGO_CARGO");
			String codPerfil = (String) datosRegistro.get("CODIGO_PERFIL");
			Long codUsuario = new Long((String) datosRegistro.get("CODIGO_USUARIO"));
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			if (!proceso.equals("ActualizarReporte")) {
				datos.put("USUARIO_CREACION", codUsuario);
				datos.put("FECHA_CREACION", fechaActual);

			}
			datos.put("USUARIO_ACTUALIZACION", codUsuario);
			datos.put("FECHA_ACTUALIZACION", fechaActual);
			datos.put("CODIGO_CARGO", codCargo);
			datos.put("PERFIL", codPerfil);
			Collection datosSoporte = new ArrayList();

			for (int t = 0; t < docsSoporte.size(); t++) {
				List listDocSopTemp = (List) docsSoporte.get(t);
				for (int i = 0; i < listDocSopTemp.size(); i++) {
					Hashtable soporte = new Hashtable();
					Hashtable docSopTemp = (Hashtable) listDocSopTemp.get(i);
					if (t == 0) {
						datos.put("ID_PREGUNTA", docSopTemp.get("ID"));
						datos.put("B_RESPUESTA", docSopTemp.get("CHECKED"));
					}
					int idPregTemp = Integer.valueOf(docSopTemp.get("ID").toString());
					if (docSopTemp.get("TIPO_PREGUNTA_V") != null
							&& docSopTemp.get("TIPO_PREGUNTA_V").toString().equals("S")) {
						soporte.put("ID_PREGUNTA", idPregTemp);
						soporte.put("B_RESPUESTA", ((Boolean) docSopTemp.get("CHECKED")) == true ? "true" : "false");
						datosSoporte.add(soporte);
					} else {
						if (docSopTemp.get("DISABLED") != null && !(Boolean) docSopTemp.get("DISABLED")) {
							soporte.put("ID_PREGUNTA", idPregTemp);
							if (docSopTemp.get("DESCRIPCION").toString().contains("FECHA")) {
								soporte.put("S_RESPUESTA", funciones.dateToString2(docSopTemp.get("VALOR")));
							} else {
								soporte.put("S_RESPUESTA", (String) docSopTemp.get("VALOR"));
							}
							datosSoporte.add(soporte);
						}
					}
				}
			}
			TransactionManager manager = null;
			Collection txnsAsociadas = new ArrayList();
			txnsAsociadas.add(datos);
			try {
				manager = (TransactionManager) new InitialContext().lookup("java:comp/UserTransaction");
				manager.begin();

				if (idRep == null) {
					int idRepI = facadeEJB.crearReporteInusual(datos, datosSoporte, new ArrayList());
					idRep = idRepI + "";
					continuar = true; // idRepI != 0;
				} else {
					datos.put("ID", Integer.valueOf(idRep));
					datos.put("ID_REPORTE", Integer.valueOf(idRep));
					if (proceso.equals("ActualizarReporte")) {
						continuar = facadeEJB.actualizarReporte(datos, datosSoporte);
					} else {
						datos.put("CODIGO_CLASE_REPORTE_V", "2");
						datos.put("CODIGO_ESTADO_REPORTE_V", "1");
						continuar = facadeEJB.reportarAnalista(datos, datosSoporte);
					}
				}
				if (continuar) {
					manager.commit();
					String msg1 = null;
					if (proceso.equals("ActualizarReporte")) {
						msg = "El reporte se actualizó exitosamente";
					} else {
						String strIdReporte = idRep.toString();// JFORE18
						if (encoded != null) {

							System.out.println("llegue al if de cargar archivo");
							boolean mensajesArchivo = facadeEJB.cargaArchivo(datos, datosSoporte, txnsAsociadas,
									encoded, strIdReporte);
							if (mensajesArchivo == true) {
								msg1 = " y Archivo cargado correctamente.";

							} else {
								msg1 = " y el archivo no pudo ser cargado correctamente.";

							}
							msg = "El reporte se creó exitosamente con el número: " + idRep + msg1;

						} else {
							msg = "El reporte se creó exitosamente con el número: " + idRep;
						}
						sesion.removeAttribute("idRep");
					}
					
				}
				encoded = null;
			} catch (Exception e) {
				continuar = false;
				msg = "DatosreporteController|reportar: " + e.getMessage();
				try {
					msg = msg.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ');
				} catch (Exception e_e1) {
					msg += "\n Ex2: " + e_e1.getMessage();
				}
				try {
					if (manager != null && manager.getStatus() == Status.STATUS_ACTIVE) {
						manager.rollback();
					}
				} catch (Exception e_e2) {
				}
			}
			/*
			 * String elementoUpdate = sesion.getAttribute("elemPadre").toString();
			 * PrimeFaces.current().ajax().update(elementoUpdate + ":dgReporte");
			 */
			PrimeFaces.current().executeScript("PF('dgwvReporte').show()");
		} else {
			fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
	}

	private void validaciones() {
		continuar = false;
		if (tabPerRelActivada && tabDocSopActivada) {
			if (descripcion != null && !descripcion.isEmpty()) {
				validarFrmCliente();
				if (continuar) {
					continuar = validarDocSoporte();
					if (!continuar) {
						tabIndex = "2";
						msg = "Si selecciona un documento debe gestionar los campos correspondientes";
					}
				}
			} else {
				msg = "Descripción de la operación es obligatorio";
				tabIndex = "0";
			}
		} else {
			msg = "Revise todas las pestañas de esta pantalla";
		}
	}

	private void validarTiNi() {
		if (tipoId == null || tipoId.isEmpty()) {
			msg = "Tipo identificación es obligatorio";
			continuar = false;
		} else if (numeroId == null || numeroId.isEmpty()) {
			msg = "Número identificación es obligatorio";
			continuar = false;
		}
	}

	private void validarFrmCliente() {
		continuar = true;
		validarTiNi();
		if (continuar) {
			
		if(!tipoId.equals("N")){	
			if (valorApellidos == null || valorApellidos.isEmpty()) {
				msg = labelApellidos + " es obligatorio";
				continuar = false;
			} else if ((valorNombre == null || valorNombre.isEmpty()) && !flagCU) {
				msg = labelNombre + " es obligatorio";
				continuar = false;
			}
		}
		else{
			if (valorApellidos == null || valorApellidos.isEmpty()) {
				msg = labelApellidos + " es obligatorio";
				continuar = false;
			}
		}
			
			// Se valida la actividad economica y el CIIU como obligatorio unicamente en la
			// gestión del analista DUCC
			if (!proceso.equals("ReportarCliente")) {
				if (actEcoSel == null || actEcoSel.get("DESCRIPCION") == null
						|| actEcoSel.get("DESCRIPCION").toString().isEmpty()) {
					msg = "CIIU es obligatorio";
					continuar = false;
				} else if ((actividadEconomica == null || actividadEconomica.isEmpty()) && !flagCU) {
					msg = "Actividad economica es obligatorio";
					continuar = false;
				}
			}

			if ((email != null && !email.isEmpty()) && !flagCU) {
				Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				Matcher mather = pattern.matcher(email);
				if (!mather.find()) {
					msg = "El correo ingresado no es válido";
					continuar = false;
				}
			}
		}
		if (!continuar) {
			tabIndex = "1";
		}
	}

	public void consultarCU() {
		continuar = true;
		validarTiNi();
		if (continuar) {
			consultarClienteCRM("1");
		} else {
			fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
	}

	private void consultarClienteCRM(String proceso) {
		String servidor = null;
		try {
			servidor = (String) (new javax.naming.InitialContext().lookup("java:comp/env/CRM"));
			Message mensajeCliente = new Message();
			mensajeCliente.addField("AA_TIPO_DOC", tipoId);
			mensajeCliente.addField("AA_NIT", numeroId);
			mensajeCliente.setTarget(servidor);
			mensajeCliente.setRequestingNode("BB_SIPLA");
			mensajeCliente.setMessageName("BB_SPCLIEGETPSREQ");
			mensajeCliente.setDestinationNode("PSFT_CR");
			mensajeCliente.setRootTag("TRANSACCION");
			String respuestaServidor = null;
			String estadoTrama = null;
			try {
				mensajeCliente.send();
				respuestaServidor = mensajeCliente.getField("MESSAGE_NBR");
				estadoTrama = mensajeCliente.getField("MESSAGE_TEXT");
			} catch (Exception e) {
				logCtr.setResLog(2);
				logCtr.setMsgLogCons("Error en comunicacion con CRM");
				msg = "No se pudo establecer comunicación con CRM. No se puede verificar si el cliente existe.";
			}
			if (respuestaServidor != null && (respuestaServidor.equals("1") || respuestaServidor.equals("10"))) {
				logCtr.setResLog(2);
				logCtr.setMsgLogCons("No se encontro el cliente en CRM");
				msg = "No se encontró el cliente en CRM.";
			}
			if (respuestaServidor != null && respuestaServidor.equals("0")) {
				logCtr.setResLog(1);
				logCtr.setMsgLogCons(" ");
				valorNombre = mensajeCliente.getField("BB_NAME1");
				valorNombre = valorNombre != null ? valorNombre : "";
				valorNombre = valorNombre.length() > 40 ? valorNombre.substring(0, 40) : valorNombre;
				valorApellidos = mensajeCliente.getField("BB_NAME");
				valorApellidos = valorApellidos != null ? valorApellidos : "";
				valorApellidos = valorApellidos.length() > 40 ? valorApellidos.substring(0, 40) : valorApellidos;
				String codigoCIIU = mensajeCliente.getField("AA_COD_CIIU");
				String CIIU = "";
				if (codigoCIIU != null && !codigoCIIU.equals("null")) {
					String condicion = " WHERE CODIGO = '" + codigoCIIU + "'";
					Collection cCiiu = consTablaEJB.consultarTabla(0, 0, "ACTIVIDAD_ECONOMICA", condicion);
					Iterator iCiiu = cCiiu.iterator();
					while (iCiiu.hasNext()) {
						Hashtable hCiiu = (Hashtable) iCiiu.next();
						CIIU = (String) hCiiu.get("DESCRIPCION");
					}
					if (CIIU == null) {
						CIIU = "";
						codigoCIIU = "";
					}
				}
				actEcoSel = new Hashtable();
				actEcoSel.put("CODIGO", codigoCIIU);
				actEcoSel.put("DESCRIPCION", CIIU);
				System.out.println("CIIU*: " + actEcoSel);
				email = mensajeCliente.getField("EMAIL_ADDR");
				email = email != null && email.length() > 30 ? email.substring(0, 30) : email;
				ingresosMensuales = mensajeCliente.getField("BB_ING_MENSUAL");
				String fechaIngresos = mensajeCliente.getField("BB_FEC_CORTE_CLI");
				if (fechaIngresos != null) {
					try {
						String anio = fechaIngresos.substring(0, 4);
						String mes = fechaIngresos.substring(4, 6);
						String dia = fechaIngresos.substring(6, 8);
						fechaIngresos = anio + "/" + mes + "/" + dia;
						fechaIngresosMensual = (Date) funciones.stringToDate(fechaIngresos);
					} catch (Exception e) {
						fechaIngresos = "";
					}
				}
				String inicioVinculacion = mensajeCliente.getField("BB_FECHA_VINC");
				if (inicioVinculacion != null) {
					try {
						String anio = inicioVinculacion.substring(0, 4);
						String mes = inicioVinculacion.substring(4, 6);
						String dia = inicioVinculacion.substring(6, 8);
						inicioVinculacion = anio + "/" + mes + "/" + dia;
						fechaInicioVinculacion = (Date) funciones.stringToDate(inicioVinculacion);
					} catch (Exception e) {
						inicioVinculacion = "";
					}
				}
				tipoTelefono = mensajeCliente.getField("BB_PURPOSE_TEL1");
				if (tipoTelefono != null && tipoTelefono.equals("20002")) {
					tipoTelefono = "44";
				}
				String tel = mensajeCliente.getField("BB_TELEFONO");
				tel = tel != null && tel.length() > 30 ? tel.substring(0, 30) : tel;
				telefono = tel;
				fax = mensajeCliente.getField("NUM_FAX");
				fax = fax != null && fax.length() > 30 ? fax.substring(0, 30) : fax;
				tipoDireccion = mensajeCliente.getField("BB_PURPOSE_DIR1");
				direccion = mensajeCliente.getField("ADDRESS1");
				direccion = direccion != null && direccion.length() > 40 ? direccion.substring(0, 40) : direccion;
				String codMunicipio = mensajeCliente.getField("CITY");
				if (codMunicipio != null && !codMunicipio.equals("null")) {
					String condicion = " WHERE CODIGO = '" + codMunicipio + "'";
					Collection cMun = consTablaEJB.consultarTabla(0, 0, "MUNICIPIO", condicion);
					Iterator iMun = cMun.iterator();
					while (iMun.hasNext()) {
						Hashtable hMun = (Hashtable) iMun.next();
						municipio = (String) hMun.get("MUNICIPIO") + "" + (String) hMun.get("DEPARTAMENTO");
					}
				}
				if (tipoId.equals("N")) {
					tipoIdReprLegal = mensajeCliente.getField("AA_TIPO_DOC");
					String numIdRepLeg = mensajeCliente.getField("AA_NIT");
					numeroIdReprLegal = numIdRepLeg != null ? numIdRepLeg : null;
					nombreReprLegal = mensajeCliente.getField("NAME2");
					apellidosReprLegal = mensajeCliente.getField("NAME3");
					displayRepLeg = "block";
				}
				if (proceso.equals("1")) {
					logCtr.setConsultaLog(
							"Consulta a CRM | Tipo Identificacion: " + tipoId + ", Numero identificacion: " + numeroId);
				} else if (proceso.equals("2")) {
					StringBuilder sb = new StringBuilder();
					sb.append("<?xml version=\"1.0\"?>");
					sb.append("<RESPUESTA class=\"R\">");
					sb.append("<MESSAGE_NBR>" + mensajeCliente.getField("MESSAGE_NBR") + "</MESSAGE_NBR>");
					sb.append("<MESSAGE_TEXT>" + mensajeCliente.getField("MESSAGE_TEXT") + "</MESSAGE_TEXT>");
					sb.append("<AA_NIT>" + mensajeCliente.getField("AA_NIT") + "</AA_NIT>");
					sb.append("<AA_TIPO_DOC>" + mensajeCliente.getField("AA_TIPO_DOC") + "</AA_TIPO_DOC>");
					sb.append(
							"<BB_FEC_CORTE_CLI>" + mensajeCliente.getField("BB_FEC_CORTE_CLI") + "</BB_FEC_CORTE_CLI>");
					sb.append("<BB_FECHA_VINC>" + mensajeCliente.getField("BB_FECHA_VINC") + "</BB_FECHA_VINC>");
					sb.append("<BB_ING_MENSUAL>" + mensajeCliente.getField("BB_ING_MENSUAL") + "</BB_ING_MENSUAL>");
					sb.append("<BB_NAME>" + mensajeCliente.getField("BB_NAME") + "</BB_NAME>");
					sb.append("<BB_NAME1>" + mensajeCliente.getField("BB_NAME1") + "</BB_NAME1>");
					sb.append("<NAME2>" + mensajeCliente.getField("NAME2") + "</NAME2>");
					sb.append("<NAME3>" + mensajeCliente.getField("NAME3") + "</NAME3>");
					sb.append("<BB_PURPOSE_DIR1>" + mensajeCliente.getField("BB_PURPOSE_DIR1") + "</BB_PURPOSE_DIR1>");
					sb.append("<ADDRESS1>" + mensajeCliente.getField("ADDRESS1") + "</ADDRESS1>");
					sb.append("<CITY>" + mensajeCliente.getField("CITY") + "</CITY>");
					sb.append("<BB_PURPOSE_TEL1>" + mensajeCliente.getField("BB_PURPOSE_TEL1") + "</BB_PURPOSE_TEL1>");
					sb.append("<BB_TELEFONO>" + mensajeCliente.getField("BB_TELEFONO") + "</BB_TELEFONO>");
					sb.append("<EMAIL_ADDR>" + mensajeCliente.getField("EMAIL_ADDR") + "</EMAIL_ADDR>");
					sb.append("<NUM_FAX>" + mensajeCliente.getField("NUM_FAX") + "</NUM_FAX>");
					sb.append("<AA_COD_CIIU>" + mensajeCliente.getField("AA_COD_CIIU") + "</AA_COD_CIIU>");
					sb.append("</RESPUESTA>");
					logCtr.setConsultaLog("Respuesta Consulta de CRM | " + sb.toString());
				}
				flagCU = true;
				logCtr.logConsulta();
			} else {
				fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
				FacesContext.getCurrentInstance().addMessage(null, fcMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DatosreporteController|consultarClienteCRM: " + e.getMessage());
		}
	}

	public void consultaCiiu() {
		String condicion = "";
		if (txtBuscCiiu != null && !txtBuscCiiu.isEmpty()) {
			txtBuscCiiu = txtBuscCiiu.toUpperCase();
			condicion += " WHERE DESCRIPCION LIKE '%" + txtBuscCiiu + "%'";
		}
		try {
			actdsEcon = consTablaEJB.consultarTabla(0, 0, "ACTIVIDAD_ECONOMICA", condicion);
		} catch (Exception e) {
			System.out.println("DatosreporteController|consultaCiiu: " + e.getMessage());
		}
		// PrimeFaces.current().ajax().update("tabViewReporte:dgRefCIUU");
		// PrimeFaces.current().executeScript("PF('dgwvRefCIUU').show()");
	}

	public void consultarMunicipios() {
		if (txtBuscMun != null && !txtBuscMun.isEmpty()) {
			txtBuscMun = txtBuscMun.toUpperCase();
			String condicion = " WHERE CENTRO_POBLADO LIKE '%" + txtBuscMun + "%'";
			condicion += " OR DEPARTAMENTO LIKE '%" + txtBuscMun + "%'";
			condicion += " OR MUNICIPIO LIKE '%" + txtBuscMun + "%'";
			try {
				municipios = consTablaEJB.consultarTabla(0, 0, "MUNICIPIO", condicion);
			} catch (Exception e) {
				System.out.println("DatosreporteController|consultarMunicipios: " + e.getMessage());
			}
		} else {

		}
	}

	public void validarDisabled(Object id) {
		for (int t = 0; t < docsSoporte.size(); t++) {
			List<Hashtable> listDocSopTemp = (List<Hashtable>) docsSoporte.get(t);
			Hashtable docSopTemp = listDocSopTemp.get(0);
			if (listDocSopTemp.size() > 1 && docSopTemp.get("ID").equals(id)) {
				for (int k = 1; k < listDocSopTemp.size(); k++) {
					docSopTemp = listDocSopTemp.get(k);
					boolean disabledTemp = Boolean.valueOf(docSopTemp.get("DISABLED").toString());
					docSopTemp.replace("DISABLED", !disabledTemp);
				}
			}
		}
	}

	public boolean validarDocSoporte() {
		boolean validacion = true;
		for (int t = 0; t < docsSoporte.size(); t++) {
			List<Hashtable> listDocSopTemp = (List<Hashtable>) docsSoporte.get(t);
			Hashtable docSopTemp = listDocSopTemp.get(0);
			boolean checked = (Boolean) docSopTemp.get("CHECKED");
			if (listDocSopTemp.size() > 1 && checked) {
				for (int k = 1; k < listDocSopTemp.size(); k++) {
					docSopTemp = listDocSopTemp.get(k);
					if (docSopTemp.get("VALOR") == null || docSopTemp.get("VALOR").toString().isEmpty()) {
						return false;
					}
				}
			}
		}
		return validacion;
	}

	public void validarRazonRetiro() {
		if (fechaFinVinculacion != null) {
			disabledRazRet = false;
		} else {
			disabledRazRet = true;
		}
	}

	public void validarDisplayRazonSocial() {
		if (tipoId != null && tipoId.equals("N")) {
			displayRepLeg = "block";
			labelApellidos = "Razon comercial:";
			labelNombre = "Razon social:";
			
		} else {
			displayRepLeg = "none";
			labelNombre = "Nombres:";
			labelApellidos= "Apellidos:";
		}
		
		
		
	}

	public void onTabActReporteChange(TabChangeEvent event) {
		String idTab = event.getTab().getId();
		if (idTab.equals("tabTr")) {
			tabIndex = "0";
		}
		if (idTab.equals("tabReporte")) {
			Collection listTransRep = (Collection) sesion.getAttribute("listTransRep");
			tabIndex = listTransRep != null && !listTransRep.isEmpty() ? "1" : "0";
			if (!tabReporte) {
				tabReporte = true;

			}
		}
		if (idTab.equals("tabAnalisis")) {
			Collection listTransRep = (Collection) sesion.getAttribute("listTransRep");
			tabIndex = listTransRep != null && !listTransRep.isEmpty() ? "2" : "1";
			if (!tabAnalisis) {
				tabAnalisis = true;

			}
		}
		if (idTab.equals("tabProcAnalis")) {
			Collection listTransRep = (Collection) sesion.getAttribute("listTransRep");
			tabIndex = listTransRep != null && !listTransRep.isEmpty() ? "3" : "1";
			if (!tabProcAnalis) {
				tabProcAnalis = true;
				disabledEstRep = "true";
				disabledROS="true";
				iniciarProcesoAnalisis();
			}
		}
	}

	public void iniciarProcesoAnalisis() {
		consultarEstadosReporte();
		listAnalisis=null;
		String sConsulta = "SELECT " + " D.NO_ACTA, " + " D.FECHA_ACTA, " + "R.ID,"
				+ " D.CODIGO_ESTADO_REPORTE_V CODIGO_ESTADO_R," + " E.NOMBRE_LARGO ESTADO_R, "
				+ " R.ROS_RELACIONADO ROS, " + " T.NOMBRE_LARGO TIPO_R, " + " D.JUSTIFICACION_FINAL "
				+ " FROM REPORTE R, " + " DETALLE_ANALISIS_REP D,    " + " PERSONAS_REP P , " + " LISTA_VALORES E, "
				+ " LISTA_VALORES T  " + " WHERE " + " P.ID_REPORTE(+) = R.ID " + " AND D.ID_REPORTE = R.ID "
				+ " AND E.TIPO_DATO = '9' " + " AND E.CODIGO = D.CODIGO_ESTADO_REPORTE_V "
				+ " AND T.TIPO_DATO(+) = '4' " + " AND T.CODIGO(+) = R.CODIGO_TIPO_REPORTE_V " + " AND R.ID = " + idRep
				+ " ORDER BY R.ID, D.FECHA_ACTUALIZACION";
		Collection resultados = null;
		try {
			listAnalisis = consTablaEJB.consultarTabla(0, 0, null, sConsulta);
			Iterator it = listAnalisis.iterator();
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				ht = funciones.quitarNull(ht);
				ht.replace("FECHA_ACTA", funciones.formatoFecha(ht.get("FECHA_ACTA")));
			}					
			System.out.println("Lista de analisis" +listAnalisis);
		} catch (Exception e) {
			System.out.println("DatosreporteController|iniciarProcesoAnalisis|ex1: " + e.getMessage());
			e.printStackTrace();
		}
		Collection c = null;
		fechaReporte = "";
		try {
			c = consTablaEJB.consultarTabla(0, 0, null, " SELECT FECHA_CREACION FROM REPORTE WHERE ID = " + idRep);
			Iterator itr = c.iterator();
			while (itr.hasNext()) {
				fechaReporte = (String) ((Hashtable) itr.next()).get("FECHA_CREACION");
				fechaReporte = funciones.formatoFecha(fechaReporte);
				System.out.println("fechaReporte: " + fechaReporte);
			}
		} catch (Exception e) {
			System.out.println("DatosreporteController|iniciarProcesoAnalisis|ex2: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void validarDisplayTipoDescRep() {
		System.out.println("estadoReporte: " + estadoReporte);
		if (estadoReporte != null) {
			if (estadoReporte.equals("3")) {
				displayDescRep = "none";
				displayTipRep = "block";
				consultarTiposReporte();

			} else if (estadoReporte.equals("4")) {
				displayDescRep = "block";
				displayTipRep = "none";
			} else {
				displayDescRep = "none";
				displayTipRep = "none";
			}
		} else {
			displayDescRep = "none";
			displayTipRep = "none";
		}
	}

	private void consultarEstadosReporte() {
		estadosReporte = null;
		String consultaEstRep = "SELECT * FROM LISTA_VALORES WHERE TIPO_DATO = 9 AND APLICA_GERENTE = 1";
		try {
			estadosReporte = consTablaEJB.consultarTabla(0, 0, null, consultaEstRep);
		} catch (Exception e) {
			System.out.println("DatosreporteController|iniciarProcesoAnalisis|ex3: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void consultarTiposReporte() {
		tiposReporte = null;
		String consultaTipRep = "SELECT * FROM LISTA_VALORES WHERE TIPO_DATO = 4";
		try {
			tiposReporte = consTablaEJB.consultarTabla(0, 0, null, consultaTipRep);
		} catch (Exception e) {
			System.out.println("DatosreporteController|iniciarProcesoAnalisis|ex3: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void validarActa() {
		System.out.println("numeroActa: " + numeroActa);
		try {
			String sConsulta = "SELECT FECHA_ACTA, NO_ACTA FROM DETALLE_ANALISIS_REP " + " WHERE NO_ACTA = "
					+ numeroActa;
			Collection cActa = consTablaEJB.consultarTabla(0, 0, null, sConsulta);
			Iterator it = cActa.iterator();
			msg = null;
			String actaFinal;
			fechaActa = "";
			while (it.hasNext()) {
				Hashtable h = (Hashtable) it.next();
				actaFinal = (String) h.get("NO_ACTA");
				fechaActa = (String) h.get("FECHA_ACTA");
				fechaActa = funciones.formatoFecha(fechaActa);
				msg = "Ya existe un acta con este número, se cargará la fecha registrada en el sistema";
				readFechActa = true;
			}
			if (msg == null) {
				msg = "No esta registrada en el sistema un acta con ese número, por favor ingrese la fecha de la misma.";
				readFechActa = false;
			}
		} catch (Exception e) {
			System.out.println("DatosreporteController|validarActa: " + e.getMessage());
		}
	}

	public void validarFechaActa() {
		System.out.println("fechaActa: " + fechaActa);
		if (fechaActa != null && !fechaActa.isEmpty()) {
			Date fechaActaD = (Date) funciones.stringToDate(fechaActa);
			Date fechaRepD = (Date) funciones.stringToDate(fechaReporte);
			if (fechaActaD.before(fechaRepD)) {
				fechaActa = "";
				msg = "La fecha del acta no puede ser menor que la fecha del reporte";
				readFechActa = false;
				PrimeFaces.current().ajax().update("frmActRep:tabViewActReporte:dgGen");
				PrimeFaces.current().executeScript("PF('dgwvGen').show()");
			}
		}
	}

	public void disabledEstadoReporte() {
		System.out.println("disabledEstRep: " + disabledEstRep);
		System.out.println("desicion ros: " + decision_RosRel);
		
		if (decision_RosRel.equals("Decision")){
			disabledROS="true";
			disabledEstRep = "false";
			rosRelacionado="";
			
		}
		if (decision_RosRel.equals("RosRel")) {
			estadoReporte=null;
			tipoReporte=null;
			justReporte="";
			disabledEstRep = "true";
			disabledROS="false";
		}
		
	/*	if (disabledEstRep.equals("true")) {
			consultarEstadosReporte();
			disabledEstRep = "false";
		} else {
			estadosReporte = null;
			disabledEstRep = "true";
		}*/
		
		
		
	}

	public void consultaRos() {
		System.out.println("consultaRos...");
		String sConsulta = "SELECT " + " D.NO_ACTA, " + " D.FECHA_ACTA, " + " D.CODIGO_ESTADO_REPORTE_V, "
				+ " R.ROS_RELACIONADO, " + " R.CODIGO_TIPO_REPORTE_V " + " FROM REPORTE R, "
				+ " DETALLE_ANALISIS_REP D " + " WHERE " + " D.ID_REPORTE = R.ID " + " AND R.TIPO_IDENTIFICACION = '"
				+ tipoId + "'" + " AND R.NUMERO_IDENTIFICACION = '" + numeroId + "'"
				+ " AND D.CODIGO_ESTADO_REPORTE_V = '3'";
		System.out.println("===> sConsulta: " + sConsulta);
		numRos = null;
		try {
			numRos = consTablaEJB.consultarTabla(0, 0, null, sConsulta);
		} catch (Exception e) {
		}
		System.out.println("===> numRos: " + numRos);
	}

	public void enviarProcesoAnalisis() {
		boolean valida=validarCampos();
		if (valida) {
			TransactionManager manager = null;
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			Hashtable datos = new Hashtable();
			Integer idRepI = Integer.valueOf(idRep);
			datos.put("ID", new Long(idRep));
			datos.put("ID_REPORTE", idRepI);
			datos.put("NO_ACTA", Integer.valueOf(numeroActa));
			datos.put("FECHA_ACTA", fechaActual);
			datos.put("USUARIO_ACTUALIZACION", new Long(usuario));
			
			datos.put("FECHA_ACTUALIZACION", fechaActual);
			datos.put("CODIGO_TIPO_REPORTE_V", tipoReporte != null ? tipoReporte : "");
			datos.put("JUSTIFICACION_FINAL", justReporte != null ? justReporte : "");
			datos.put("ROS_RELACIONADO",rosRelacionado!= ""? new Integer(rosRelacionado):"");
			datos.put("CODIGO_ESTADO_REPORTE_V", estadoReporte != null ? estadoReporte : "");
			
			try {
				manager = (TransactionManager) new InitialContext().lookup("java:comp/UserTransaction");
				manager.begin();
				detalleAnaliEJB.create(datos);
				datos.put("CODIGO_CARGO", cargo);
				String rosAsignado = reporteEJB.actualizarEstado(datos);
				rosAsignado = rosAsignado.equals("") ? "" : ". Se asignó el ROS: " + rosAsignado;
				manager.commit();
				msg = "Se registró el proceso de análisis exitosamente" + rosAsignado;
				
			} catch (Exception e) {
				msg = "Se presentó un error: " + e.getMessage();
				try {
					msg = msg.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ');
					if ((msg.toLowerCase()).indexOf("already") != -1
							|| (msg.toLowerCase()).indexOf("ora-00001") != -1) {
						msg = "Acta anteriormente asociada a este reporte. No se puede actualizar el proceso de análisis";
					}
				} catch (Exception otra) {
				}
				try {
					if (manager != null && manager.getStatus() == Status.STATUS_ACTIVE) {
						manager.rollback();
					}
				} catch (SystemException ee) {
				}
			}
			PrimeFaces.current().ajax().update("frmActRep:tabViewActReporte:dgAnalis");
			PrimeFaces.current().executeScript("PF('dgwvAnalisis').show()");
		} else {
			continuar = false;
			fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
		
	}

	private boolean validarCampos() {

		if ((numeroActa == null) || (numeroActa.equals(""))) {
			msg = "Numero de acta es obligatorio ";
			return false;
		}
		if ((fechaActa == null) || (fechaActa.equals(""))) {
			msg = "Fecha de acta es  obligatorio ";
			return false;
		}
		if(decision_RosRel==null) {
			msg = "Debe seleccionar una opción: Decisión o ROS relacionado";
	
			return false;
		}	
			
		if ((decision_RosRel.equals("Decision")) && (estadoReporte==null)) {
			msg = "Seleccione un valor correcto en Decisión";
			return false;
		}
		if ((decision_RosRel.equals("Decision")) && (estadoReporte.equals("3"))) {
			if (tipoReporte==null){
			msg = "Seleccione un valor correcto para el tipo de reporte";
			return false;
			}
		}
		if ((decision_RosRel.equals("Decision")) && (estadoReporte.equals("4"))) {
				
			if (justReporte.equals("")){
			msg = "Debe ingresar las justificación";
			return false;
			}
		}
		if ((decision_RosRel.equals("RosRel")) && (rosRelacionado==null)) {
			msg = "Si selecciona la opción de ROS debe seleccionar un valor de ROS válido";
			return false;
		}
		

		return true;

	}
	public void actualizarAnalisis() {
		rosRelacionado=(String)rosSel.get("ROS_RELACIONADO") ;
		fechaActa=(String)rosSel.get("FECHA_ACTA");
		numeroActa=(String)rosSel.get("NO_ACTA");
	}
	
	public void visualizaDescJust(Hashtable analisis) {
		detalleJust = (String) analisis.get("JUSTIFICACION_FINAL");
		PrimeFaces.current().ajax().update("frmActRep:tabViewActReporte:dgJust");
		PrimeFaces.current().executeScript("PF('dgwvJust').show()");
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setFechaInicioVinculacion(Date fechaInicioVinculacion) {
		this.fechaInicioVinculacion = fechaInicioVinculacion;
	}

	public Date getFechaInicioVinculacion() {
		return fechaInicioVinculacion;
	}

	public void setFechaFinVinculacion(Date fechaFinVinculacion) {
		this.fechaFinVinculacion = fechaFinVinculacion;
	}

	public Date getFechaFinVinculacion() {
		return fechaFinVinculacion;
	}

	public void setFechaIngresosMensual(Date fechaIngresosMensual) {
		this.fechaIngresosMensual = fechaIngresosMensual;
	}

	public Date getFechaIngresosMensual() {
		return fechaIngresosMensual;
	}

	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	public String getTipoId() {
		return tipoId;
	}

	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	public String getNumeroId() {
		return numeroId;
	}

	public void setLabelNombre(String labelNombre) {
		this.labelNombre = labelNombre;
	}

	public String getLabelNombre() {
		return labelNombre;
	}

	public void setLabelApellidos(String labelApellidos) {
		this.labelApellidos = labelApellidos;
	}

	public String getLabelApellidos() {
		return labelApellidos;
	}

	public void setValorNombre(String valorNombre) {
		this.valorNombre = valorNombre;
	}

	public String getValorNombre() {
		return valorNombre;
	}

	public void setValorApellidos(String valorApellidos) {
		this.valorApellidos = valorApellidos;
	}

	public String getValorApellidos() {
		return valorApellidos;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setRelacion(String relacion) {
		this.relacion = relacion;
	}

	public String getRelacion() {
		return relacion;
	}

	public void setCiiu(String ciiu) {
		this.ciiu = ciiu;
	}

	public String getCiiu() {
		return ciiu;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setTipoTelefono(String tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}

	public String getTipoTelefono() {
		return tipoTelefono;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setTipoDireccion(String tipoDireccion) {
		this.tipoDireccion = tipoDireccion;
	}

	public String getTipoDireccion() {
		return tipoDireccion;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setTipoIdReprLegal(String tipoIdReprLegal) {
		this.tipoIdReprLegal = tipoIdReprLegal;
	}

	public String getTipoIdReprLegal() {
		return tipoIdReprLegal;
	}

	public void setNumeroIdReprLegal(String numeroIdReprLegal) {
		this.numeroIdReprLegal = numeroIdReprLegal;
	}

	public String getNumeroIdReprLegal() {
		return numeroIdReprLegal;
	}

	public void setNombreReprLegal(String nombreReprLegal) {
		this.nombreReprLegal = nombreReprLegal;
	}

	public String getNombreReprLegal() {
		return nombreReprLegal;
	}

	public void setApellidosReprLegal(String apellidosReprLegal) {
		this.apellidosReprLegal = apellidosReprLegal;
	}

	public String getApellidosReprLegal() {
		return apellidosReprLegal;
	}

	public void setIngresosMensuales(String ingresosMensuales) {
		this.ingresosMensuales = ingresosMensuales;
	}

	public String getIngresosMensuales() {
		return ingresosMensuales;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTiposDoc(Collection tiposDoc) {
		this.tiposDoc = tiposDoc;
	}

	public Collection getTiposDoc() {
		return tiposDoc;
	}

	public void setFrmpreinusualCtr(FrmpreinusualController frmpreinusualCtr) {
		this.frmpreinusualCtr = frmpreinusualCtr;
	}

	public FrmpreinusualController getFrmpreinusualCtr() {
		return frmpreinusualCtr;
	}

	public void setOtraRelacion(String otraRelacion) {
		this.otraRelacion = otraRelacion;
	}

	public String getOtraRelacion() {
		return otraRelacion;
	}

	public void setDocsSoporte(List docsSoporte) {
		this.docsSoporte = docsSoporte;
	}

	public List getDocsSoporte() {
		return docsSoporte;
	}

	public void setOtro(String otro) {
		this.otro = otro;
	}

	public String getOtro() {
		return otro;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setActdsEcon(Collection actdsEcon) {
		this.actdsEcon = actdsEcon;
	}

	public Collection getActdsEcon() {
		return actdsEcon;
	}

	public void setActEcoSel(Hashtable actEcoSel) {
		this.actEcoSel = actEcoSel;
	}

	public Hashtable getActEcoSel() {
		return actEcoSel;
	}

	public void setActdsEconFiltr(Collection actdsEconFiltr) {
		this.actdsEconFiltr = actdsEconFiltr;
	}

	public Collection getActdsEconFiltr() {
		return actdsEconFiltr;
	}

	public void setIdRep(String idRep) {
		this.idRep = idRep;
	}

	public String getIdRep() {
		return idRep;
	}

	public void setRespuestas(Hashtable respuestas) {
		this.respuestas = respuestas;
	}

	public Hashtable getRespuestas() {
		return respuestas;
	}

	public void setClaseRep(String claseRep) {
		this.claseRep = claseRep;
	}

	public String getClaseRep() {
		return claseRep;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}

	public void setTabDocSopActivada(boolean tabDocSopActivada) {
		this.tabDocSopActivada = tabDocSopActivada;
	}

	public boolean isTabDocSopActivada() {
		return tabDocSopActivada;
	}

	public void setRazonRetiro(String razonRetiro) {
		this.razonRetiro = razonRetiro;
	}

	public String getRazonRetiro() {
		return razonRetiro;
	}

	public void setRazonesRetiro(Collection razonesRetiro) {
		this.razonesRetiro = razonesRetiro;
	}

	public Collection getRazonesRetiro() {
		return razonesRetiro;
	}

	public void setListavaloresCtr(ListavaloresController listavaloresCtr) {
		this.listavaloresCtr = listavaloresCtr;
	}

	public ListavaloresController getListavaloresCtr() {
		return listavaloresCtr;
	}

	public void setDisplayRepLeg(String displayRepLeg) {
		this.displayRepLeg = displayRepLeg;
	}

	public String getDisplayRepLeg() {
		return displayRepLeg;
	}

	public void setDisabledRazRet(boolean disabledRazRet) {
		this.disabledRazRet = disabledRazRet;
	}

	public boolean isDisabledRazRet() {
		return disabledRazRet;
	}

	public void setTxtBuscMun(String txtBuscMun) {
		this.txtBuscMun = txtBuscMun;
	}

	public String getTxtBuscMun() {
		return txtBuscMun;
	}

	public void setMunicipios(Collection municipios) {
		this.municipios = municipios;
	}

	public Collection getMunicipios() {
		return municipios;
	}

	public void setMunicipioSel(Hashtable municipioSel) {
		this.municipioSel = municipioSel;
	}

	public Hashtable getMunicipioSel() {
		return municipioSel;
	}

	public void setTxtBuscCiiu(String txtBuscCiiu) {
		this.txtBuscCiiu = txtBuscCiiu;
	}

	public String getTxtBuscCiiu() {
		return txtBuscCiiu;
	}

	public void setContinuar(boolean continuar) {
		this.continuar = continuar;
	}

	public boolean isContinuar() {
		return continuar;
	}

	public void setLogCtr(LogController logCtr) {
		this.logCtr = logCtr;
	}

	public LogController getLogCtr() {
		return logCtr;
	}

	public void setFechaReporte(String fechaReporte) {
		this.fechaReporte = fechaReporte;
	}

	public String getFechaReporte() {
		return fechaReporte;
	}

	public void setNumeroActa(String numeroActa) {
		this.numeroActa = numeroActa;
	}

	public String getNumeroActa() {
		return numeroActa;
	}

	public void setFechaActa(String fechaActa) {
		this.fechaActa = fechaActa;
	}

	public String getFechaActa() {
		return fechaActa;
	}

	public void setRosRelacionado(String rosRelacionado) {
		this.rosRelacionado = rosRelacionado;
	}

	public String getRosRelacionado() {
		return rosRelacionado;
	}

	public void setEstadoReporte(String estadoReporte) {
		this.estadoReporte = estadoReporte;
	}

	public String getEstadoReporte() {
		return estadoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setJustReporte(String justReporte) {
		this.justReporte = justReporte;
	}

	public String getJustReporte() {
		return justReporte;
	}

	public void setDecision_RosRel(String decision_RosRel) {
		this.decision_RosRel = decision_RosRel;
	}

	public String getDecision_RosRel() {
		return decision_RosRel;
	}

	public void setEstadosReporte(Collection estadosReporte) {
		this.estadosReporte = estadosReporte;
	}

	public Collection getEstadosReporte() {
		return estadosReporte;
	}

	public void setDisabledEstRep(String disabledEstRep) {
		this.disabledEstRep = disabledEstRep;
	}

	public String getDisabledEstRep() {
		return disabledEstRep;
	}

	public void setDisplayTipRep(String displayTipRep) {
		this.displayTipRep = displayTipRep;
	}

	public String getDisplayTipRep() {
		return displayTipRep;
	}

	public void setDisplayDescRep(String displayDescRep) {
		this.displayDescRep = displayDescRep;
	}

	public String getDisplayDescRep() {
		return displayDescRep;
	}

	public void setReadFechActa(boolean readFechActa) {
		this.readFechActa = readFechActa;
	}

	public boolean isReadFechActa() {
		return readFechActa;
	}

	public void archivo(byte[] encoded2) {
		encoded = encoded2;
	}

	public Collection getTiposReporte() {
		return tiposReporte;
	}

	public void setTiposReporte(Collection tiposReporte) {
		this.tiposReporte = tiposReporte;
	}

	public Collection getNumRos() {
		return numRos;
	}

	public void setNumRos(Collection numRos) {
		this.numRos = numRos;
	}

	public Hashtable getRosSel() {
		return rosSel;
	}

	public void setRosSel(Hashtable rosSel) {
		this.rosSel = rosSel;
	}

	public String getDisabledROS() {
		return disabledROS;
	}

	public void setDisabledROS(String disabledROS) {
		this.disabledROS = disabledROS;
	}

	public Collection getListAnalisis() {
		return listAnalisis;
	}

	public void setListAnalisis(Collection listAnalisis) {
		this.listAnalisis = listAnalisis;
	}

	public String getDetalleJust() {
		return detalleJust;
	}

	public void setDetalleJust(String detalleJust) {
		this.detalleJust = detalleJust;
	}
}
