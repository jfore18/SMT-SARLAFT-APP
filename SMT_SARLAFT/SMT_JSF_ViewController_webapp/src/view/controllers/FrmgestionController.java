package view.controllers;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;
import monfox.toolkit.snmp.agent.modules.SnmpV2Mib.SysOREntry;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

import javax.annotation.Generated;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.naming.InitialContext;

import javax.naming.NamingException;

import javax.servlet.http.HttpSession;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;

import presentacion.FacadeEJB;

import transaccion.ReporteEJB;

@ManagedBean(name = "FrmgestionController")
@SessionScoped
public class FrmgestionController {

	@ManagedProperty("#{FrmpreinusualController}")
	private FrmpreinusualController frmpreCtr;
	@ManagedProperty("#{DatosreporteController}")
	private DatosreporteController datosrepCtr;
	@EJB
	private ConsultaTablaEJB consTablaEJB;
	@EJB
	private FacadeEJB facadeEJB;
	@EJB
	private ReporteEJB reporteEJB;
	private FacesContext facContex;
	private FacesMessage fcMsg;
	private ExternalContext extContex;
	private HttpSession sesion;
	private Funciones funciones;
	private List<Hashtable> transCli;
	private Collection transRepAnt;
	private Collection tiposConsult;
	private Collection preguntasRep;
	private Hashtable datosRegistro;
	private Hashtable datosCliente;
	private String codTipConSel;
	private List<Hashtable> transSel;
	private String index;
	private String idRep;
	private String indag;
	private String otro;
	private String msg;
	private String justIni;
	private String perfil;
	private String cargo;
	private String proceso;
	private String titulo;
	private String cliente;
	private String usuario;
	private boolean repNuevo;
	private boolean clickAnalisis;
	private boolean clickRep;
	private boolean clickHistConc;
	private boolean continuar;
	private boolean navegar;
	private Hashtable respuestas;
	private boolean adjuntar;
	private boolean visualiza;
	private String fileName;

	UploadedFile uploadedFile;
	byte[] encoded;
	private byte[] Archivo;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		usuario = datosRegistro.get("CODIGO_USUARIO").toString();
		perfil = datosRegistro.get("CODIGO_PERFIL").toString();
		cargo = datosRegistro.get("CODIGO_CARGO").toString();
		index = "0";
		repNuevo = false;
		clickAnalisis = false;
		transSel = new ArrayList<Hashtable>();
		indag = "";
		funciones = new Funciones();
		continuar = false;
		proceso = "";
		adjuntar = false;

	}

	public void ejecutarProceso(String procesoIn) {
		proceso = procesoIn;

		sesion.setAttribute("procesoRep", proceso);
		datosCliente = frmpreCtr.getDetalleTran();
		if (proceso.equals("RealizarGestion")) {
			titulo = "Realizar gestión";
			//idRep = null;
			consTrns();
			if (idRep  == null){
				indag = "";
				codTipConSel = "";		
			}
			tiposConsulta();
			preguntasRep();

		} else if (proceso.equals("Reportar")) {
			datosrepCtr.iniciarReporte("ReportarTrans");
			titulo = "Reportar transacción";
			adjuntar = false;
			visualiza = false;
			consTrns();
		} else if (proceso.equals("NoReportar")) {
			titulo = "No reportar transacción ";
			consDocsSoporte();
			adjuntar = false;
			visualiza = false;
			consTrns();
		}
		cliente = "Cliente: " + datosCliente.get("CLIENTE") + ". Identificación: "
				+ datosCliente.get("TIPO_IDENTIFICACION") + " - " + datosCliente.get("NUMERO_IDENTIFICACION");
		
		clickAnalisis = false;
		clickRep = false;
		clickHistConc = false;
		index = "0";
		otro = "";
		justIni = "";
		continuar = false;

	}

	public void consTrns() {
		idRep = null;
		sesion.removeAttribute("idRep");
		this.transSel = new ArrayList<Hashtable>();
		String tipId = (String) frmpreCtr.getDetalleTran().get("TIPO_IDENTIFICACION");
		String numId = (String) frmpreCtr.getDetalleTran().get("NUMERO_IDENTIFICACION");
		String consTrn = "";
		if (idRep == null) {
			String consultaRep = " SELECT R.ID ID FROM REPORTE R WHERE R.TIPO_IDENTIFICACION = '" + tipId + "'"
					+ " AND R.NUMERO_IDENTIFICACION = '" + numId + "' AND R.CODIGO_CARGO = '" + cargo
					+ "' AND R.CODIGO_CLASE_REPORTE_V IS NULL AND ROWNUM < 2";
			Collection colRep = null;
			try {
				colRep = consTablaEJB.consultarTabla(0, 0, null, consultaRep);
			} catch (Exception e) {
				System.out.println("FrmgestionController|consTrns: Error select REPORTE");
			}
			if (colRep.isEmpty()) {
				repNuevo = true;
			} else {
				repNuevo = false;
				Iterator it = colRep.iterator();
				String idReporte = "";
				while (it.hasNext()) {
					Hashtable hash = (Hashtable) it.next();
					idReporte = (String) hash.get("ID");
				}
				idRep = idReporte;
				sesion.setAttribute("idRep", idRep);
			}
		}
		if (idRep != null) {
			transRepAnt = reporteEJB.mostrarTxn(new Integer(idRep));
		}
		if (perfil.equals("5")) {
			String codEstId = (String) frmpreCtr.getDetalleTran().get("CODIGO_ESTADO_DUCC");
			consTrn = "SELECT * FROM V_TRANSACCIONES WHERE ROWNUM <= 500 AND TIPO_IDENTIFICACION='" + tipId
					+ "' AND NUMERO_IDENTIFICACION='" + numId + "' AND CODIGO_ESTADO_DUCC ='" + codEstId
					+ "' ORDER BY N_PRODUCTO, FECHA DESC, MAYOR_RIESGO DESC, TO_NUMBER(V_TRANSACCIONES.VALOR) DESC ";
		} else if (perfil.equals("2")) {
			String estadoOfi = (String) frmpreCtr.getDetalleTran().get("ESTADO_OFICINA");
			String codOfi = (String) frmpreCtr.getDetalleTran().get("CODIGO_OFICINA");
			consTrn = "SELECT * FROM V_TRANSACCIONES WHERE ROWNUM <= 500 AND TIPO_IDENTIFICACION='" + tipId
					+ "' AND NUMERO_IDENTIFICACION='" + numId + "' AND ESTADO_OFICINA ='" + estadoOfi
					+ "' AND CODIGO_OFICINA = '" + codOfi
					+ "' ORDER BY MAYOR_RIESGO DESC, TO_NUMBER(V_TRANSACCIONES.VALOR) DESC ";
		}
		Collection colTran = null;
		try {
			colTran = consTablaEJB.consultarTabla(0, 0, null, consTrn);
		} catch (Exception e) {
			System.out.println("FrmgestionController|consTrns: Error select V_TRANSACCIONES");
		}
		if (colTran != null) {
			transCli = new ArrayList<Hashtable>();
			Hashtable tranTemp = null;
			Iterator itr = colTran.iterator();
			while (itr.hasNext()) { // OB
				tranTemp = (Hashtable) itr.next();
				tranTemp.replace("FECHA", funciones.formatoFecha(tranTemp.get("FECHA")));
				double val = Double.valueOf((String) tranTemp.get("VALOR"));
				DecimalFormat format = new DecimalFormat("###,###.##");
				String valor = format.format(val);
				tranTemp.replace("VALOR", valor);
				transCli.add(tranTemp);
				if (transRepAnt != null && !transRepAnt.isEmpty()) {
					Hashtable temp = new Hashtable();
					temp.put("CODIGO_ARCHIVO", tranTemp.get("CODIGO_ARCHIVO"));
					temp.put("ID_TRANSACCION", tranTemp.get("ID_TRANSACCION"));
					temp.put("FECHA_PROCESO", tranTemp.get("FECHA_PROCESO"));
					Iterator itTemp = transRepAnt.iterator();
					while (itTemp.hasNext()) {
						Hashtable tranAntTemp = (Hashtable) itTemp.next();
						if (tranAntTemp.get("ID_TRANSACCION").toString()
								.equals(tranTemp.get("ID_TRANSACCION").toString())
								&& tranAntTemp.get("CODIGO_ARCHIVO").toString()
										.equals(tranTemp.get("CODIGO_ARCHIVO").toString())
								&& tranAntTemp.get("FECHA_PROCESO").toString()
										.equals(tranTemp.get("FECHA_PROCESO").toString())) {
							this.transSel.add(tranTemp);
						}
					}
				}
				tranTemp.replace("FECHA_PROCESO", funciones.formatoFecha(tranTemp.get("FECHA_PROCESO")));
				if (transSel.isEmpty()) {
					if (frmpreCtr.getDetalleTran().get("ID_TRANSACCION").toString()
							.equals(tranTemp.get("ID_TRANSACCION").toString())
							&& frmpreCtr.getDetalleTran().get("CODIGO_ARCHIVO").toString()
									.equals(tranTemp.get("CODIGO_ARCHIVO").toString())
							&& frmpreCtr.getDetalleTran().get("FECHA_PROCESO").toString()
									.equals(tranTemp.get("FECHA_PROCESO").toString())) {
						this.transSel.add(tranTemp);
					}
				}
			}
		}

	}

	public void iniciarAnalisis() {
		idRep = sesion.getAttribute("idRep").toString();
		tiposConsulta();
		preguntasRep();
	}

	public void onTabChange(TabChangeEvent event) {
		System.out.println("Entro a onTabChange");
		String idTab = event.getTab().getId();
		if (idTab.equals("tabTrRel")) {
			index = "0";
		}
		if (idTab.equals("tabAnal")) {
			index = "1";
			System.out.println("entro a tab anal" + clickAnalisis);
			if (!clickAnalisis) {
				System.out.println(clickAnalisis);
				clickAnalisis = true;
				tiposConsulta();
				preguntasRep();
				//iniciarAnalisis();
			}
		}
		if (idTab.equals("tabReporte")) {
			clickRep = true;
			index = "1";
		}
		if (idTab.equals("tabSoporte")) {
			clickRep = true;
			index = "1";
		}
		if (idTab.equals("tabHistConcep")) {
			clickHistConc = true;
			index = "2";
		}
		System.out.println("Salir onTabChange 1 " + clickRep + "index: " + index);
		System.out.println("Salir onTabChange 2 " + clickAnalisis);

	}

	public void tiposConsulta() {
		Collection colTipCons = null;
		String cons = "SELECT * FROM V_TIPOS_CONSULTA";
		try {
			colTipCons = consTablaEJB.consultarTabla(0, 0, null, cons);
		} catch (Exception e) {
			System.out.println("FrmgestionController|tiposConsulta: Error select V_TIPOS_CONSULTA");
		}
		tiposConsult = colTipCons;

	}

	public void preguntasRep() {
		validarRespuestas();
		String consPreg = " WHERE TIPO_PREGUNTA_V ='A' AND TRUNC(SYSDATE) BETWEEN TRUNC(VIGENTE_DESDE) AND TRUNC(VIGENTE_HASTA) ORDER BY ID";
		Collection colPregRep = null;
		try {
			colPregRep = consTablaEJB.consultarTabla(0, 0, "PREGUNTAS_REP", consPreg);
		} catch (Exception e) {
			System.out.println("FrmgestionController|preguntasRep: Error select PREGUNTAS_REP");
		}
		if (colPregRep != null) {
			Iterator itPregRep = colPregRep.iterator();
			while (itPregRep.hasNext()) {
				boolean chk = false;
				Hashtable pregRep = (Hashtable) itPregRep.next();
				String desc = ((String) pregRep.get("DESCRIPCION")).replace("&nbsp;&nbsp;&nbsp;", "");
				pregRep.replace("DESCRIPCION", desc);
				if (respuestas.containsKey(pregRep.get("ID"))
						&& ((String) respuestas.get(pregRep.get("ID"))).equals("1")) {
					chk = true;
				}
				pregRep.put("CHECKED", chk);
			}
		}
		codTipConSel = null;
		indag = "";
		otro = "";
		if (respuestas.containsKey("CODIGO_TIPO_CONSULTA_V")) {
			codTipConSel = respuestas.get("CODIGO_TIPO_CONSULTA_V").toString();
		}
		if (respuestas.containsKey("INDAGACION")) {
			indag = respuestas.get("INDAGACION").toString();
		}
		if (respuestas.containsKey("OTRA_FUENTE_INFORMACION")) {
			otro = respuestas.get("OTRA_FUENTE_INFORMACION").toString();
		}
		preguntasRep = colPregRep;
	}

	public void consDocsSoporte() {
		String condicion = " WHERE TIPO_PREGUNTA_V IN ('N') AND TRUNC(SYSDATE) BETWEEN TRUNC(VIGENTE_DESDE) AND TRUNC(VIGENTE_HASTA) ORDER BY ID";
		Collection colDocsSop = null;
		try {
			colDocsSop = consTablaEJB.consultarTabla(0, 0, "PREGUNTAS_REP", condicion);
		} catch (Exception e) {
			System.out.println("FrmgestionController|preguntasRep: Error select PREGUNTAS_REP");
		}
		if (colDocsSop != null) {
			Iterator itDocsSop = colDocsSop.iterator();
			while (itDocsSop.hasNext()) {
				Hashtable docSop = (Hashtable) itDocsSop.next();
				docSop.put("RESPUESTA", "NULL");
			}
		}
		preguntasRep = colDocsSop;
		System.out.println("preguntasRep: " + preguntasRep);
	}

	private void validarRespuestas() {
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
						String otraf = (String) h.get("OTRA_FUENTE_INFORMACION");
						if (!otraf.equals("null")) {
							respuestas.put("OTRA_FUENTE_INFORMACION", otraf);
						}
						String indagacion = (String) h.get("INDAGACION");
						if (!indagacion.equals("null")) {
							respuestas.put("INDAGACION", indagacion);
						}
						String tipoConsulta = (String) h.get("CODIGO_TIPO_CONSULTA_V");
						if (!tipoConsulta.equals("null")) {
							respuestas.put("CODIGO_TIPO_CONSULTA_V", tipoConsulta);
						}
					}
				}
			}
		}
	}

	public void noReportar(String procesoIn) {
		validarRegGestion();
		if (continuar) {
			Hashtable datos = new Hashtable();
			Integer idRepI = Integer.valueOf(idRep);
			datos.put("ID", idRepI);
			datos.put("ID_REPORTE", idRepI);
			datos.put("CODIGO_ESTADO_REPORTE_V", "4");
			datos.put("CODIGO_TIPO_REPORTE_V", "3");
			datos.put("CODIGO_CLASE_REPORTE_V", "1");
			datos.put("JUSTIFICACION_INICIAL", indag != null ? indag : "");
			datos.put("USUARIO_ACTUALIZACION", new Long(usuario));
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			datos.put("FECHA_ACTUALIZACION", fechaActual);
			datos.put("CODIGO_CARGO", cargo);
			TransactionManager manager = null;
			try {
				manager = (TransactionManager) new InitialContext().lookup("java:comp/UserTransaction");
				manager.begin();
				if (facadeEJB.noReportarAnalista(datos)) {
					manager.commit();
					msg = "La(s) transacción(es) fue(ron) revisadas exitosamente con el consecutivo:" + idRep;
				}
			} catch (Exception e) {
				msg = "Se presentó un error: " + e.getMessage();
				try {
					msg = msg.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ');
				} catch (Exception otra) {
				}
				try {
					if (manager != null && manager.getStatus() == Status.STATUS_ACTIVE) {
						manager.rollback();
					}
				} catch (SystemException ee) {
				}
			}
			continuar = false;
			PrimeFaces.current().ajax().update("frmGestion:dgRegGest");
			PrimeFaces.current().executeScript("PF('dgwvRegGest').show()");
		} else {
			fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
	}

	private void validarRegGestion() {
		continuar = true;
		if (clickAnalisis) {
			if (transSel == null || transSel.isEmpty()) {
				msg = "No ha asociado transacciones ";
				index = "0";
				continuar = false;
			} else {
				if (indag.trim().isEmpty()) {
					msg = "El campo indagación es obligatorio";
					index = "1";
					continuar = false;
				}
			}
		} else {
			msg = "Revise todas las pestañas de esta pantalla";
			continuar = false;
		}
	}

	private void validarReportar() {

		msg = null;
		continuar = true;
		justIni = datosrepCtr.getDescripcion();

		boolean tabDocSopActivada = datosrepCtr.isTabDocSopActivada();

		if (transSel != null && !transSel.isEmpty()) {
			if (tabDocSopActivada) {
				if (!justIni.isEmpty()) {
					if (!datosrepCtr.validarDocSoporte()) {
						msg = "Si selecciona un documento debe gestionar los campos correspondientes";
						index = "1";
						datosrepCtr.setTabIndex("1");
						continuar = false;
					}
				} else {
					msg = "Descripción de la operación es un dato obligatorio";
					index = "1";
					datosrepCtr.setTabIndex("0");
					continuar = false;
				}
			} else {
				index = "1";
				msg = "Revise todas las pestañas del reporte";
				continuar = false;
			}
		} else {
			msg = "No ha relacionado transacciones";
			index = "0";
			continuar = false;
		}

	}

	private void validarNoReportar() {
		continuar = true;
		if (transSel != null && !transSel.isEmpty()) {
			if (!justIni.isEmpty()) {
				Iterator itPregResp = preguntasRep.iterator();
				boolean respondioTodas = true;
				while (itPregResp.hasNext()) {
					Hashtable pregRepTemp = (Hashtable) itPregResp.next();
					if (pregRepTemp.get("RESPUESTA") == null
							|| pregRepTemp.get("RESPUESTA").toString().equals("NULL")) {
						respondioTodas = false;
						break;
					}
				}
				if (!respondioTodas) {
					msg = "Por favor conteste todas las peguntas";
					index = "1";
					continuar = false;
				}
			} else {
				msg = "Observaciones es un dato obligatorio";
				index = "1";
				continuar = false;
			}
		} else {
			msg = "No ha asociado transacciones";
			index = "0";
			continuar = false;
		}
	}

	public void regGestion() {

		facContex = FacesContext.getCurrentInstance();
		perfil = datosRegistro.get("CODIGO_PERFIL").toString();

		if (perfil.equals("5")) {
			validarRegGestion();
			if (continuar) {
				registrarGestion();
			} else {
				fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
				FacesContext.getCurrentInstance().addMessage(null, fcMsg);
			}
		} else if (perfil.equals("2") || perfil.equals("3")) {

			if (clickRep) {

				if (proceso.equals("Reportar")) {

					validarReportar();
					if (continuar) {
						registrarGestion();
					} else {
						fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
						FacesContext.getCurrentInstance().addMessage(null, fcMsg);
					}
				} else if (proceso.equals("NoReportar")) {
					validarNoReportar();
					if (continuar) {
						registrarGestion();
					} else {
						fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
						FacesContext.getCurrentInstance().addMessage(null, fcMsg);
					}
				}
			} else {
				msg = "Revise todas las pestañas de esta pantalla";
				fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
				facContex.addMessage(null, fcMsg);
			}
		}
	}

	public void cargaArchivo(FileUploadEvent event) {

		uploadedFile = event.getFile();
		fileName = uploadedFile.getFileName();
		System.out.println("cargue el archivo" + fileName);
		String contentType = uploadedFile.getContentType();
		byte[] contents = uploadedFile.getContents();
		encoded = contents;
		datosrepCtr.archivo(encoded);
	}

	// jfore18
	public void registrarGestion() {
		String codPerfil = (String) datosRegistro.get("CODIGO_PERFIL");
		preguntasRep = proceso.equals("Reportar") ? datosrepCtr.getDocsSoporte() : preguntasRep;
		Hashtable datosPregRep = new Hashtable();
		if (proceso.equals("Reportar")) {
			datosPregRep = (Hashtable) ((ArrayList) preguntasRep.iterator().next()).get(0);
			otro = datosrepCtr.getOtro();
		} else {
			datosPregRep = (Hashtable) preguntasRep.iterator().next();
		}
		Hashtable datosTx = (Hashtable) transSel.iterator().next();
		String tipIdent = (String) datosTx.get("TIPO_IDENTIFICACION");
		String numIdent = (String) datosTx.get("NUMERO_IDENTIFICACION");
		String idTran = (String) datosTx.get("ID_TRANSACCION");
		String codArch = (String) datosTx.get("CODIGO_ARCHIVO");
		String fechProc = (String) datosTx.get("FECHA_PROCESO");

		boolean chk = datosPregRep.get("CHECKED") != null && datosPregRep.get("CHECKED").equals(true);
		String idPreg = (String) datosPregRep.get("ID");
		String codCargo = (String) datosRegistro.get("CODIGO_CARGO");
		String nombreUsuario = (String) datosRegistro.get("USUARIO_NT"); // jfore18
		String codOficina = (String) datosRegistro.get("CODIGO_UNIDAD_NEGOCIO"); // jfore18
		Long codUsuario = new Long((String) datosRegistro.get("CODIGO_USUARIO"));
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		String codTopCons = codTipConSel != null ? codTipConSel : "";
		Hashtable datos = new Hashtable();
		datos.put("B_RESPUESTA", chk);
		datos.put("CODIGO_TIPO_CONSULTA_V", codTopCons);
		datos.put("F_FECHA_PROCESO", fechProc);
		datos.put("ID_PREGUNTA", idPreg);
		datos.put("INDAGACION", indag);
		datos.put("I_CODIGO_ARCHIVO", codArch);
		datos.put("I_ID_TRANSACCION", idTran);
		datos.put("JUSTIFICACION_INICIAL", justIni != null ? justIni : "");
		datos.put("NUMERO_IDENTIFICACION", numIdent);
		datos.put("OTRA_FUENTE_INFORMACION", otro);
		datos.put("TIPO_IDENTIFICACION", tipIdent);
		datos.put("NOMBRE_USUARIO", nombreUsuario);// JFORE18
		datos.put("CODIGO_OFICINA", codOficina);// JFORE18
		// pendiente en esta parte validar si se requieren otros datos
		datos.put("USUARIO_CREACION", codUsuario);
		datos.put("USUARIO_ACTUALIZACION", codUsuario);
		datos.put("FECHA_CREACION", fechaActual);
		datos.put("FECHA_ACTUALIZACION", fechaActual);
		datos.put("CODIGO_CARGO", codCargo);
		datos.put("PERFIL", codPerfil);

		Collection datosSoporte = new ArrayList();
		Iterator itPregResp = preguntasRep.iterator();
		if (codPerfil.equals("5")) {
			while (itPregResp.hasNext()) {
				Hashtable soporte = new Hashtable();
				Hashtable pregRepTemp = (Hashtable) itPregResp.next();
				soporte.put("ID_PREGUNTA", Integer.valueOf((String) pregRepTemp.get("ID")));
				soporte.put("B_RESPUESTA", ((Boolean) pregRepTemp.get("CHECKED")) == true ? "true" : "false");
				datosSoporte.add(soporte);
			}
		} else if (codPerfil.equals("2")) {
			if (proceso.equals("Reportar")) {

				while (itPregResp.hasNext()) {

					List<Hashtable> elemts = (List<Hashtable>) itPregResp.next();
					for (Hashtable pregRepTemp : elemts) {

						Hashtable soporte = new Hashtable();
						if (pregRepTemp.get("TIPO_PREGUNTA_V") != null
								&& pregRepTemp.get("TIPO_PREGUNTA_V").toString().equals("S")) {

							soporte.put("ID_PREGUNTA", Integer.valueOf((String) pregRepTemp.get("ID")));
							soporte.put("B_RESPUESTA",
									((Boolean) pregRepTemp.get("CHECKED")) == true ? "true" : "false");
							datosSoporte.add(soporte);
						} else {
							if (!(Boolean) pregRepTemp.get("DISABLED")) {

								soporte.put("ID_PREGUNTA", Integer.valueOf((String) pregRepTemp.get("ID")));
								if (pregRepTemp.get("DESCRIPCION").toString().contains("FECHA")) {
									soporte.put("S_RESPUESTA", funciones.dateToString2(pregRepTemp.get("VALOR")));
								} else {
									soporte.put("S_RESPUESTA", pregRepTemp.get("VALOR"));
								}
								datosSoporte.add(soporte);
							}
						}
					}
				}

			} else if (proceso.equals("NoReportar")) {
				while (itPregResp.hasNext()) {
					Hashtable soporte = new Hashtable();
					Hashtable pregRepTemp = (Hashtable) itPregResp.next();
					soporte.put("ID_PREGUNTA", Integer.valueOf((String) pregRepTemp.get("ID")));
					soporte.put("B_RESPUESTA", pregRepTemp.get("RESPUESTA"));
					datosSoporte.add(soporte);
				}
			}
		}
		Collection txnsAsociadas = new ArrayList();
		Iterator itTransSel = transSel.iterator();
		while (itTransSel.hasNext()) {
			Hashtable tranTemp1 = (Hashtable) itTransSel.next();
			System.out.println("tranTemp1: " + tranTemp1);
			Hashtable tranTemp = new Hashtable();
			tranTemp.put("ID_TRANSACCION", Integer.valueOf((String) tranTemp1.get("ID_TRANSACCION")));
			tranTemp.put("ID", Integer.valueOf((String) tranTemp1.get("ID_TRANSACCION")));
			String fechaProc = ((String) tranTemp1.get("FECHA_PROCESO")).replace('/', '-') + " 00:00:00.0";
			tranTemp.put("FECHA_PROCESO", Timestamp.valueOf(fechaProc));
			tranTemp.put("CODIGO_ARCHIVO", Integer.valueOf((String) tranTemp1.get("CODIGO_ARCHIVO")));
			tranTemp.put("USUARIO_ACTUALIZACION", new Long((String) datosRegistro.get("CODIGO_USUARIO")));
			tranTemp.put("FECHA_ACTUALIZACION", fechaActual);

			txnsAsociadas.add(tranTemp);
		}
		TransactionManager manager = null;
		String strIdReporte = null;
		try {
			manager = (TransactionManager) new InitialContext().lookup("java:comp/UserTransaction");
			Integer idReporte = 0;
			manager.begin();

			perfil = datosRegistro.get("CODIGO_PERFIL").toString();
			if (perfil.equals("5")) {
				if (idRep == null) {
					idReporte = facadeEJB.crearReporteAnalista(datos, datosSoporte, txnsAsociadas);
					manager.commit();
					msg = "La gestion se creó exitosamente con el consecutivo: " + idReporte;
					idRep = String.valueOf(idReporte);
					sesion.setAttribute("idRep", idRep);
					continuar = true;
				} else {
					continuar = false;
					datos.put("ID", Integer.valueOf(idRep));
					if (facadeEJB.actualizarGestion(datos, datosSoporte, txnsAsociadas)) {
						manager.commit();
		
						msg = "La gestion fue actualizada exitosamente";
					} else {
						if (manager != null && manager.getStatus() == Status.STATUS_ACTIVE) {
							manager.rollback();
						}
						msg = "Se presentó un error al actualizar la gestion";
					}
				}
			} else if (perfil.equals("2")) {
				System.out.println("llegue al if de cargar archivo");
				String msg1 = null;
				continuar = false;
				if (proceso.equals("Reportar")) {
					idReporte = facadeEJB.crearReporteInusual(datos, datosSoporte, txnsAsociadas);
					manager.commit();
					strIdReporte = idReporte.toString();// JFORE18

					if (uploadedFile != null) {

						boolean mensajesArchivo = facadeEJB.cargaArchivo(datos, datosSoporte, txnsAsociadas, encoded,
								strIdReporte);
						if (mensajesArchivo == true) {
							msg1 = " y Archivo cargado correctamente.";

						} else {
							msg1 = " y el archivo no pudo ser cargado correctamente.";
						}
						msg = "El reporte se creó exitosamente con el número: " + idReporte + msg1;
						

					} else {
						msg = "El reporte se creó exitosamente con el número: " + idReporte;
						

					}

				} else if (proceso.equals("NoReportar")) {
					String msg2 = null;
					idReporte = facadeEJB.crearReporteNormal(datos, datosSoporte, txnsAsociadas);
					manager.commit();
					strIdReporte = idReporte.toString();// JFORE18
					if (uploadedFile != null) {
						System.out.println("llegue al if de cargar archivo");

						boolean mensajesArchivo = facadeEJB.cargaArchivo(datos, datosSoporte, txnsAsociadas, encoded,
								strIdReporte);
						if (mensajesArchivo == true) {
							msg2 = " y Archivo cargado correctamente.";

						} else {
							msg2 = " y el archivo no pudo ser cargado correctamente.";

						}
						msg = "La(s) transacción(es) fue(ron) revisadas exitosamente con el consecutivo: " + idReporte
								+ msg2;
					} else {
						msg = "La(s) transacción(es) fue(ron) revisadas exitosamente con el consecutivo: " + idReporte;
					}
				}

			}
			uploadedFile = null;
			visualiza = false;
			adjuntar = false;
		} catch (Exception error) {
			msg = "Se presentó un error: " + error.getMessage();
			try {
				msg = msg.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ');
			} catch (Exception otra) {
			}

			try {
				if (manager != null && manager.getStatus() == Status.STATUS_ACTIVE) {
					manager.rollback();
				}
			} catch (SystemException e) {
			}
		}
		PrimeFaces.current().ajax().update("frmGestion:dgRegGest");
		PrimeFaces.current().executeScript("PF('dgwvRegGest').show()");
	}

	public void actualizarAnalisis() {
		try {
			idRep = sesion.getAttribute("idRep").toString();
			TransactionManager manager = (TransactionManager) new InitialContext().lookup("java:comp/UserTransaction");
			Integer idReporte = Integer.valueOf(idRep);
			manager.begin();
			if (reporteEJB.limpiarAnalisis(idReporte)) {
				Hashtable datos = new Hashtable();
				datos.put("ID", idReporte);
				datos.put("USUARIO_ACTUALIZACION", new Long(usuario));
				datos.put("FECHA_ACTUALIZACION", new Timestamp(System.currentTimeMillis()));
				String codTopCons = codTipConSel != null ? codTipConSel : "";
				datos.put("CODIGO_TIPO_CONSULTA_V", codTopCons);
				datos.put("INDAGACION", indag);
				datos.put("OTRA_FUENTE_INFORMACION", otro);
				Iterator itPregResp = preguntasRep.iterator();
				while (itPregResp.hasNext()) {
					Hashtable soporte = new Hashtable();
					Hashtable pregRepTemp = (Hashtable) itPregResp.next();
					soporte.put("ID_REPORTE", idReporte);
					soporte.put("ID_PREGUNTA", Integer.valueOf((String) pregRepTemp.get("ID")));
					soporte.put("B_RESPUESTA", ((Boolean) pregRepTemp.get("CHECKED")) == true ? "true" : "false");
					reporteEJB.adicionarRespuesta(soporte);
				}
				reporteEJB.actualizarReporte(datos);
				manager.commit();
				msg = "Se registró el análisis del reporte " + idReporte + " exitosamente";
			} else {
				if (manager != null && manager.getStatus() == Status.STATUS_ACTIVE) {
					manager.rollback();
				}
				msg = "No se pudo actualizar el análisis ";
			}
		} catch (Exception e) {
			msg = "Ocurrio un error: " + e.getMessage();
			e.printStackTrace();
			System.out.println("FrmgestionController|actualizarAnalisis: " + e.getMessage());
		}
		PrimeFaces.current().ajax().update("frmActRep:tabViewActReporte:dgActAnal");
		PrimeFaces.current().executeScript("PF('dgwvActAnal').show()");
	}

	public void iniciarDocumento() {
		adjuntar = false;
		visualiza = false;

	}

	public void habilitarCargue() {
		visualiza = adjuntar ? true : false;
		fileName = null;
	}

	public byte[] getArchivos() {
		Archivo = encoded;
		return Archivo;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getIndex() {
		return index;
	}

	public String getIdRep() {
		return idRep;
	}

	public void setIdRep(String idRep) {
		this.idRep = idRep;
	}

	public void setTransCli(List transCli) {
		this.transCli = transCli;
	}

	public List getTransCli() {
		return transCli;
	}

	public void setTransSel(List<Hashtable> transSel) {
		this.transSel = transSel;
	}

	public List<Hashtable> getTransSel() {
		return transSel;
	}

	public void setFrmpreCtr(FrmpreinusualController frmpreCtr) {
		this.frmpreCtr = frmpreCtr;
	}

	public FrmpreinusualController getFrmpreCtr() {
		return frmpreCtr;
	}

	public void setTiposConsult(Collection tiposConsult) {
		this.tiposConsult = tiposConsult;
	}

	public Collection getTiposConsult() {
		return tiposConsult;
	}

	public void setPreguntasRep(Collection preguntasRep) {
		this.preguntasRep = preguntasRep;
	}

	public Collection getPreguntasRep() {
		return preguntasRep;
	}

	public void setCodTipConSel(String codTipConSel) {
		this.codTipConSel = codTipConSel;
	}

	public String getCodTipConSel() {
		return codTipConSel;
	}

	public void setIndag(String indag) {
		this.indag = indag;
	}

	public String getIndag() {
		return indag;
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

	public void setRespuestas(Hashtable respuestas) {
		this.respuestas = respuestas;
	}

	public Hashtable getRespuestas() {
		return respuestas;
	}

	public void setJustIni(String justIni) {
		this.justIni = justIni;
	}

	public String getJustIni() {
		return justIni;
	}

	public void setDatosrepCtr(DatosreporteController datosrepCtr) {
		this.datosrepCtr = datosrepCtr;
	}

	public DatosreporteController getDatosrepCtr() {
		return datosrepCtr;
	}

	public void setContinuar(boolean continuar) {
		this.continuar = continuar;
	}

	public boolean isContinuar() {
		return continuar;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getCliente() {
		return cliente;
	}

	public void setDatosCliente(Hashtable datosCliente) {
		this.datosCliente = datosCliente;
	}

	public Hashtable getDatosCliente() {
		return datosCliente;
	}

	public boolean isAdjuntar() {
		return adjuntar;
	}

	public void setAdjuntar(boolean adjuntar) {
		this.adjuntar = adjuntar;
	}

	public boolean isVisualiza() {
		return visualiza;
	}

	public void setVisualiza(boolean visualiza) {
		this.visualiza = visualiza;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


}
