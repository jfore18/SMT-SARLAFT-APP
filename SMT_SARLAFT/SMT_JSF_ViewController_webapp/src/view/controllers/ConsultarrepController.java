package view.controllers;

import admin.util.Funciones;
import baseDatos.Consulta;
import baseDatos.ConsultaEJB;
import baseDatos.ConsultaTablaEJB;
import presentacion.FacadeEJB;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.sql.Timestamp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import java.util.Enumeration;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.context.PrimeRequestContext;

import transaccion.ReporteEJB;

@ManagedBean(name = "ConsultarrepController")
@SessionScoped
public class ConsultarrepController {

	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	@EJB
	private ConsultaTablaEJB consTablaEJB;
	@EJB
	private ReporteEJB reporteEJB;
	@ManagedProperty("#{DatosreporteController}")
	private DatosreporteController datosreporteCtr;
	@ManagedProperty("#{FrmgestionController}")
	private FrmgestionController frmgestionCtr;
	@ManagedProperty("#{ListavaloresController}")
	private ListavaloresController listavaloresCtr;
	@EJB
	private ConsultaEJB consultaEJB;
	@EJB
	private FacadeEJB facadeEJB;
	private Funciones funciones;
	private FacesContext facContex;
	private ExternalContext extContex;
	private HttpSession sesion;
	private FacesMessage fcMsg;
	private Consulta miConsulta;
	private Date fechaRep;
	private String fechaStr;
	private String numeroRep;
	private String ros;
	private String estadoRep;
	private String claseRep;
	private String perfil;
	private String region;
	private String zonaT;
	private String oficina;
	private String tipoId;
	private String numeroId;
	private String consultaRep;
	private String msg;
	private String justificacionInicial;
	private String tab1Index;
	private String tab2Index;
	private String labelNombresRazonComercial;
	private String labelApellidosRazonSocial;
	private String codPerfilCreacionRep;
	private String idRep;
	private String otroDoc;
	private String proceso;
	private String cargo;
	private boolean tabAnalRepActivada;
	private boolean tabReporteActivada;
	private boolean tabPerRelReporteActivada;
	private boolean tabDocSopReporteActivada;
	private Hashtable reporteSel;
	private Hashtable clienteRep;
	private Hashtable tranRep;
	private Hashtable datSoporte;
	private Hashtable respuestas;
	private Hashtable respuestasAnal;
	private Hashtable datosRegistro;
	private Collection listTransRep;
	private Collection listEstados;
	private Collection listClases;
	private Collection listPerfiles;
	private Collection listRegiones;
	private Collection listZonas;
	private Collection listTiposId;
	private Collection listReportes;
	private Collection listPregRespRep;
	private Collection listDocsAnalisis;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		cargo = datosRegistro.get("CODIGO_CARGO").toString();
		funciones = new Funciones();
		llenarListas();
	}

	public void limpiarCampos() {
		fechaRep = null;
		numeroRep = "";
		ros = "";
		estadoRep = "";
		claseRep = "";
		perfil = "";
		region = "";
		zonaT = "";
		oficina = "";
		tipoId = "";
		numeroId = "";
		listReportes = null;

	}

	private void llenarListas() {
		try {
			listEstados = consTablaEJB.consultarTabla(0, 0, "V_ESTADOS_REPORTE", null);
		} catch (Exception e) {
			System.out.println("ConsultarrepController|llenarListas:V_ESTADOS_REPORTE: " + e.getMessage());
		}
		try {
			listClases = consTablaEJB.consultarTabla(0, 0, "V_CLASES_REPORTE", null);
		} catch (Exception e) {
			System.out.println("ConsultarrepController|llenarListas:V_CLASES_REPORTE: " + e.getMessage());
		}
		try {
			listPerfiles = consTablaEJB.consultarTabla(0, 0, "V_PERFILES", null);
		} catch (Exception e) {
			System.out.println("ConsultarrepController|llenarListas:V_PERFILES: " + e.getMessage());
		}
		try {
			listRegiones = consTablaEJB.consultarTabla(0, 0, "V_REGION", null);
		} catch (Exception e) {
			System.out.println("ConsultarrepController|llenarListas:V_REGION: " + e.getMessage());
		}
		try {
			listTiposId = consTablaEJB.consultarTabla(0, 0, "V_TIPOS_DOCUMENTO", null);
		} catch (Exception e) {
			System.out.println("ConsultarrepController|llenarListas:V_TIPOS_DOCUMENTO: " + e.getMessage());
		}
	}

	public void buscarZonas() {
		try {
			String consulta = "select * from V_ZONAS WHERE CODIGO_REGION_V = '" + region + "'";
			listZonas = consTablaEJB.consultarTabla(0, 0, null, consulta);
			System.out.println("Lista de zonas:" + listZonas);
		} catch (Exception e) {
			System.out.println("ConsultarrepController|buscarZonas:V_ZONAS: " + e.getMessage());
		}
	}

	public void buscarReportes() {
		fechaStr = funciones.dateToString(fechaRep);
		miConsulta = consultaEJB.realizarConsulta(cargo, "10");
		consultaRep = "AND 1>0";

		/*
		 * consultaRep = "SELECT * FROM V_RELACION_REPORTES V,ANALISTA_REGION R " +
		 * "WHERE ((V.CODIGO_REGION=R.CODIGO_REGION_V AND R.CODIGO_CARGO='"+cargo+"')" +
		 * " OR V.CODIGO_CARGO='"+cargo+"')";
		 */

		if (fechaStr != null && !fechaStr.isEmpty()) {
			consultaRep += " AND FECHA_CREACION LIKE '%" + fechaStr + "%'";
		}
		if (numeroRep != null && !numeroRep.isEmpty()) {
			consultaRep += " AND ID_REPORTE LIKE '%" + numeroRep + "%'";
		}
		if (ros != null && !ros.isEmpty()) {
			consultaRep += " AND ROS LIKE '%" + ros + "%'";
		}
		if (estadoRep != null && !estadoRep.isEmpty()) {
			consultaRep += " AND CODIGO_ESTADO = '" + estadoRep + "'";
		}
		if (claseRep != null && !claseRep.isEmpty()) {
			consultaRep += " AND CODIGO_CLASE = '" + claseRep + "'";
		}
		if (perfil != null && !perfil.isEmpty()) {
			consultaRep += " AND CODIGO_PERFIL = '" + perfil + "'";
		}
		if (region != null && !region.isEmpty()) {
			consultaRep += " AND CODIGO_REGION = '" + region + "'";
		}

		if (zonaT != null && !zonaT.isEmpty()) {
			consultaRep += " AND CODIGO_ZONA = '" + zonaT + "'";

		}
		if (oficina != null && !oficina.isEmpty()) {
			consultaRep += " AND (CODIGO_OFICINA LIKE '%" + oficina + "%' OR OFICINA LIKE '%" + oficina.toUpperCase()
					+ "%')";
		}
		if (tipoId != null && !tipoId.isEmpty()) {
			consultaRep += " AND TIPO_IDENTIFICACION = '" + tipoId + "'";
		}
		if (numeroId != null && !numeroId.isEmpty()) {
			consultaRep += " AND NUMERO_IDENTIFICACION = '" + numeroId + "'";
		}
		Integer limite = listavaloresCtr.getLimiteRes();
		consultaRep += " AND ROWNUM <= " + limite;
		if (!consultaRep.equals("SELECT * FROM V_RELACION_REPORTES WHERE 1>0")) {
			try {
				listReportes = facadeEJB.ejecutarConsultaGeneral(consultaRep, miConsulta, 0);

				if (listReportes != null) {
					System.out.println("lista diferente de null");
					Iterator it = listReportes.iterator();
					while (it.hasNext()) {
						Hashtable repTemp = (Hashtable) it.next();
						String codigo_estado = (String) repTemp.get("1");
						String codigo_clase = (String) repTemp.get("2");
						String idReporte = (String) repTemp.get("NUMERO");
						String cargo = (String) repTemp.get("CARGO");
						String fecha = (String) repTemp.get("FECHA");
						String clase = (String) repTemp.get("CLASE");
						repTemp.put("CODIGO_ESTADO", codigo_estado);
						repTemp.put("CODIGO_CLASE", codigo_clase);
						repTemp.put("ID_REPORTE", idReporte);
						repTemp.put("CODIGO_CARGO", cargo);
						repTemp.put("FECHA_CREACION", fecha);
						repTemp.put("CLASE_REPORTE", clase);
					}
				}
			} catch (Exception e) {
				System.out.println("ConsultarrepController|buscarReportes:V_RELACION_REPORTES: " + e.getMessage());
			}
		} else {
			msg = "Debe ingresar al menos un parametro de busqueda";
			fcMsg = new FacesMessage("Alerta: ", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
		// System.out.println("listReportes: "+listReportes);
	}

	private void limpiar() {
		clienteRep = null;
		tab1Index = "0";
		tab2Index = "0";
		tabAnalRepActivada = false;
		tabReporteActivada = false;
		tabPerRelReporteActivada = false;
		tabDocSopReporteActivada = false;
		respuestasAnal = null;
		listDocsAnalisis = null;
		listPregRespRep = null;
		otroDoc = null;
	}

	public void consultarDatosRep(String procesoIn) {
		System.out.println("1");
		proceso = procesoIn;
		limpiar();
		listTransRep = null;
		System.out.println("2");
		if (proceso.equals("ConsultarRepTr")) {
			reporteSel = (Hashtable) sesion.getAttribute("reporteSel");
		}
		if (reporteSel != null) {
			System.out.println(reporteSel);
			reporteSel = funciones.quitarNull(reporteSel);
			System.out.println("3");
			idRep = reporteSel.get("ID_REPORTE").toString();
			System.out.println("4");
			String ob;
			ob = buscarValor(reporteSel, "ID_REPORTE", "REPORTE", "ID", "CODIGO_TIPO_REPORTE_V");
			reporteSel.put("TIPO_REPORTE", ob);
			ob = buscarValor(reporteSel, "TIPO_REPORTE", "V_TIPOS_REPORTE", "CODIGO", "DESCRIPCION");
			reporteSel.replace("TIPO_REPORTE", ob.isEmpty() ? "SIN INFORMACIÓN" : ob);
			ob = buscarValor(reporteSel, "ID_REPORTE", "REPORTE", "ID", "CODIGO_CARGO");
			ob = buscarValor(reporteSel, "CODIGO_CARGO", "V_USUARIOS", "CODIGO_CARGO", "CODIGO_PERFIL");
			codPerfilCreacionRep = ob;
			System.out.println("reporteSel: " + reporteSel);
			Hashtable datRep = null;
			if (idRep != null) {
				datRep = reporteEJB.mostrarDatos(new Integer(idRep));
			}
			if (datRep != null) {
				justificacionInicial = datRep.get("JUSTIFICACION_INICIAL").toString();
			}
			Collection cliente = null;
			if (idRep != null) {
				cliente = reporteEJB.mostrarPersonas(new Integer(idRep));
			}
			System.out.println("cliente: " + cliente);
			if (cliente != null) {
				Iterator it = cliente.iterator();
				if (it.hasNext()) {
					clienteRep = (Hashtable) it.next();
				}
			}
			System.out.println("clienteRep: " + clienteRep);
			// if (clienteRep == null || proceso.equals("ConsultarRepTr")) {
			listTransRep = consultaInfoTrans(idRep, "1");
			// }
			if (listTransRep != null) {
				Iterator itInfoTran = listTransRep.iterator();
				while (itInfoTran.hasNext()) {
					Hashtable infoTran = (Hashtable) itInfoTran.next();
					infoTran = funciones.quitarNull(infoTran);
					infoTran.replace("FECHA", funciones.formatoFecha(infoTran.get("FECHA")));
					infoTran.replace("VALOR", funciones.formatoValor(infoTran.get("VALOR")));
					infoTran.replace("MAYOR_RIESGO", funciones.mayorR(infoTran.get("MAYOR_RIESGO")));
				}
			}
			if (idRep != null) {
				datosreporteCtr.setIdRep(idRep);
				datosreporteCtr.iniciarProcesoAnalisis();
			}

			System.out.println("listTransRep: " + listTransRep);
		}

	}

	public void actualizarReporte(String idReporte) {
		idRep = idReporte;
		sesion.setAttribute("idRep", idRep);
		listTransRep = reporteEJB.mostrarTxn(new Integer(idRep));
		sesion.setAttribute("listTransRep", listTransRep);
		// System.out.println("listTransRep: " + listTransRep);
		String consulta = " WHERE (ID_TRANSACCION, CODIGO_ARCHIVO, FECHA_PROCESO) IN ( ";
		Iterator it = listTransRep.iterator();
		while (it.hasNext()) {
			Hashtable transaccion = (Hashtable) it.next();
			String archivo = (String) transaccion.get("CODIGO_ARCHIVO");
			String sFecha = funciones.formatoFecha(transaccion.get("FECHA_PROCESO"));
			String id = (String) transaccion.get("ID_TRANSACCION");
			consulta += "(" + id + ", " + archivo + " , TO_DATE('" + sFecha + "','YYYY/MM/DD')),";
		}
		consulta = consulta.substring(0, consulta.lastIndexOf(",")) + ") ORDER BY MAYOR_RIESGO DESC, VALOR DESC";
		System.out.println("consulta: " + consulta);
		try {
			listTransRep = consTablaEJB.consultarTabla(0, 0, "V_TRANSACCIONES", consulta);
			Iterator it2 = listTransRep.iterator();
			while (it2.hasNext()) {
				Hashtable tx = (Hashtable) it2.next();
				String fechaTemp = funciones.formatoFecha(tx.get("FECHA"));
				tx.replace("FECHA", fechaTemp);
				String valorTemp = funciones.formatoValor(tx.get("VALOR"));
				tx.replace("VALOR", valorTemp);
				String mayRTemp = tx.get("MAYOR_RIESGO").equals("1") ? "SI" : "NO";
				tx.replace("MAYOR_RIESGO", mayRTemp);
			}
		} catch (Exception e) {
			System.out.println("ConsultarrepController|actualizarReporte: " + e.getMessage());
		}
		// System.out.println("listTransRep: " + listTransRep);
	}

	public void onTab1Change(TabChangeEvent event) {
		String idTab = event.getTab().getId();
		if (idTab.equals("tabTrRelRep")) {
			tab1Index = "0";
		}
		if (idTab.equals("tabRepRep")) {
			tab1Index = listTransRep != null ? "1" : "0";
			if (!tabReporteActivada) {
				tabReporteActivada = true;
			}
		}
		if (idTab.equals("tabAnalRep")) {
			tab1Index = listTransRep != null ? "2" : "1";
			if (!tabAnalRepActivada) {
				tabAnalRepActivada = true;
				consultarDatosAnalisis();
				PrimeFaces.current().ajax().update("tabRep:pgAnalisisRep");
			}
		}
		if (idTab.equals("tabAnalisis")) {
			datosreporteCtr.setIdRep(idRep);
			datosreporteCtr.iniciarProcesoAnalisis();
			PrimeFaces.current().ajax().update("tabRep:pgConsultarAnalisis");
		}
	}

	public void onTab2Change(TabChangeEvent event) {
		String idTab = event.getTab().getId();
		if (idTab.equals("tabEncReporte")) {
			tab2Index = "0";
		}
		if (idTab.equals("tabPerRelReporte")) {
			tab2Index = "1";
			if (!tabPerRelReporteActivada) {
				tabPerRelReporteActivada = true;
				consultarDatosCliente();
				PrimeFaces.current().ajax().update("tabRep:tabReporte:pgClienteRep");
			}
		}
		if (idTab.equals("tabDocSopReporte")) {
			tab2Index = "2";
			if (!tabDocSopReporteActivada) {
				tabDocSopReporteActivada = true;
				consultarDatosSoporte();
				PrimeFaces.current().ajax().update("tabRep:tabReporte:pgDocSopRep");
			}
		}
	}

	public void consultarDatosCliente() {
		tranRep = null;
		labelNombresRazonComercial = "Nombres:";
		labelApellidosRazonSocial = "Apellidos:";
		String ob;
		if (clienteRep != null && !clienteRep.isEmpty()) {
			clienteRep = funciones.quitarNull(clienteRep);
			List<String> tipDocCmparar = Arrays.asList("N");
			if (tipDocCmparar.contains(clienteRep.get("TIPO_IDENTIFICACION"))) {
				labelNombresRazonComercial = "Razon comercial:";
				labelApellidosRazonSocial = "Razon social:";
			}
			ob = buscarValor(clienteRep, "TIPO_RELACION_V", "V_TIPOS_RELACION", "CODIGO", "NOMBRE_LARGO");
			clienteRep.replace("TIPO_RELACION_V", ob);
			ob = buscarValor(clienteRep, "RAZON_RETIRO_V", "V_RAZONES_RETIRO", "CODIGO", "DESCRIPCION");
			clienteRep.replace("RAZON_RETIRO_V", ob);
			ob = buscarValor(clienteRep, "CODIGO_TIPO_TELEFONO_V", "V_TIPOS_TELEFONO", "CODIGO", "NOMBRE_LARGO");
			clienteRep.replace("CODIGO_TIPO_TELEFONO_V", ob);
			ob = buscarValor(clienteRep, "CODIGO_TIPO_DIRECCION_V", "V_TIPOS_DIRECCION", "CODIGO", "NOMBRE_LARGO");
			clienteRep.replace("CODIGO_TIPO_DIRECCION_V", ob);
			ob = buscarValor(clienteRep, "CODIGO_CIIU", "ACTIVIDAD_ECONOMICA", "CODIGO", "DESCRIPCION");
			clienteRep.replace("CODIGO_CIIU", clienteRep.get("CODIGO_CIIU") + "-" + ob);
			ob = buscarValor(clienteRep, "TIPO_IDENTIFICACION", "V_TIPOS_DOCUMENTO", "CODIGO", "NOMBRE_LARGO");
			clienteRep.replace("TIPO_IDENTIFICACION", ob);
			clienteRep.replace("FECHA_INGRESOS", funciones.formatoFecha(clienteRep.get("FECHA_INGRESOS")));
			clienteRep.replace("INICIO_VINCULACION", funciones.formatoFecha(clienteRep.get("INICIO_VINCULACION")));
			clienteRep.replace("FINAL_VINCULACION", funciones.formatoFecha(clienteRep.get("FINAL_VINCULACION")));
			ob = buscarValor(clienteRep, "CODIGO_MUNICIPIO", "MUNICIPIO", "CODIGO", "MUNICIPIO");
			clienteRep.replace("CODIGO_MUNICIPIO", ob);
		} else {
			Collection cTransTemp = consultaInfoTrans(idRep, "2");
			if (cTransTemp != null) {
				Iterator itTrans2 = cTransTemp.iterator();
				while (itTrans2.hasNext()) {
					tranRep = (Hashtable) itTrans2.next();
				}
			}
			if (tranRep != null) {
				tranRep = funciones.quitarNull(tranRep);
				ob = buscarValor(tranRep, "TIPO_IDENTIFICACION", "V_TIPOS_DOCUMENTO", "CODIGO", "NOMBRE_LARGO");
				tranRep.replace("TIPO_IDENTIFICACION", ob);
			}
		}
		// System.out.println("clienteRep: "+clienteRep);
	}

	private Collection consultaInfoTrans(String idRep, String codConsulta) {
		Collection datosTrans = null;
		Collection cTransTemp = null;
		datosTrans = reporteEJB.mostrarTxn(new Integer(idRep));
		if (datosTrans != null) {
			String consulta = null;
			if (codConsulta.equals("1")) {
				consulta = " WHERE (ID_TRANSACCION, CODIGO_ARCHIVO, FECHA_PROCESO) IN ( ";
			}
			Hashtable tranTemp = null;
			Iterator itTrans = datosTrans.iterator();
			while (itTrans.hasNext()) {
				tranTemp = (Hashtable) itTrans.next();
				String codArch = tranTemp.get("CODIGO_ARCHIVO").toString();
				String fechProc = funciones
						.dateToString(new Date(((Timestamp) tranTemp.get("FECHA_PROCESO")).getTime()));
				String codTran = tranTemp.get("ID_TRANSACCION").toString();
				if (codConsulta.equals("1")) {
					consulta += "(" + codTran + ", " + codArch + " , TO_DATE('" + fechProc + "','YYYY/MM/DD')),";
				}
				if (codConsulta.equals("2")) {
					consulta = " WHERE CODIGO_ARCHIVO = " + codArch + " AND FECHA_PROCESO = TO_DATE('" + fechProc
							+ "','YYYY/MM/DD')" + " AND ID_TRANSACCION = " + codTran;
				}
			}
			if (codConsulta.equals("1")) {
				consulta = consulta.substring(0, consulta.lastIndexOf(","))
						+ ") ORDER BY MAYOR_RIESGO DESC, VALOR DESC";
			}
			if (tranTemp != null) {
				try {
					cTransTemp = consTablaEJB.consultarTabla(0, 0, "V_TRANSACCIONES", consulta);
				} catch (Exception e) {
					System.out
							.println("ConsultarrepController|consultarDatosCliente:V_TRANSACCIONES: " + e.getMessage());
				}
			}
		}
		return cTransTemp;
	}

	public void consultarDatosSoporte() {
		datosreporteCtr.setIdRep(idRep);
		datosreporteCtr.setClaseRep(reporteSel.get("CLASE_REPORTE").toString());
		datosreporteCtr.consultarDocSoporte();
		listPregRespRep = datosreporteCtr.getDocsSoporte();
		respuestas = datosreporteCtr.getRespuestas();
		otroDoc = respuestas != null && respuestas.get("OTRO_DOCUMENTO_SOPORTE") != null
				&& respuestas.get("OTRO_DOCUMENTO_SOPORTE") != "null"
						? respuestas.get("OTRO_DOCUMENTO_SOPORTE").toString()
						: "NINGUNO";
	}

	private void consultarDatosAnalisis() {
		String ob;
		frmgestionCtr.setIdRep(idRep);
		frmgestionCtr.preguntasRep();
		listDocsAnalisis = frmgestionCtr.getPreguntasRep();
		respuestasAnal = frmgestionCtr.getRespuestas();
		ob = buscarValor(respuestasAnal, "CODIGO_TIPO_CONSULTA_V", "V_TIPOS_CONSULTA", "CODIGO", "DESCRIPCION");
		respuestasAnal.put("TIPO_CONSULTA", ob);
	}

	public void generarPDF() {

		HttpServletResponse response;
		PdfWriter write;
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FacesContext context = FacesContext.getCurrentInstance();
		response = (HttpServletResponse) context.getExternalContext().getResponse();
		String tipoIdPDF;
		String numeroIdPDF;
		String nombrePDF;

		try {
			tipoIdPDF="";
			numeroIdPDF="";
			nombrePDF="";
			write = PdfWriter.getInstance(document, baos);
			document.open();
			document.setPageSize(PageSize.LETTER);
			document.setMargins(50, 50, 50, 25);
			document.open();
			Chunk tituloEncabezado = new Chunk("REPORTE DE OPERACIONES", new Font(Font.UNDEFINED, 14, Font.ITALIC));
			Paragraph titulo = new Paragraph(tituloEncabezado.setUnderline(0.2f, -2f));
			titulo.setAlignment(Element.ALIGN_CENTER);
			document.add(titulo);
			Font font = new Font(Font.UNDEFINED, 10, Font.BOLD);
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			PdfPCell celda = new PdfPCell();
			celda.setBorder(PdfPCell.NO_BORDER);
			PdfPCell celda3 = new PdfPCell();
			celda3.setColspan(3);
			celda3.setBorder(PdfPCell.NO_BORDER);
			celda.setPhrase(new Phrase("No. Interno", font));
			table.addCell(celda);
			celda.setPhrase(new Phrase(idRep.toString(), font));
			table.addCell(celda);
			celda.setPhrase(new Phrase("Fecha", font));
			table.addCell(celda);
			celda.setPhrase(new Phrase(reporteSel.get("FECHA_CREACION").toString(), font));
			table.addCell(celda);
			celda.setPhrase(new Phrase("Creado Por", font));
			table.addCell(celda);
			celda3.setPhrase(new Phrase(reporteSel.get("USUARIO").toString(), font));
			table.addCell(celda3);
			celda.setPhrase(new Phrase("Perfil", font));
			table.addCell(celda);
			celda3.setPhrase((new Phrase(reporteSel.get("PERFIL").toString(), font)));
			table.addCell(celda3);
			celda.setPhrase(new Phrase("Unidad de Negocio", font));
			table.addCell(celda);
			celda3.setPhrase(new Phrase(reporteSel.get("OFICINA").toString(), font));
			table.addCell(celda3);
			celda.setPhrase(new Phrase("Clase de Reporte", font));
			table.addCell(celda);
			celda.setPhrase(new Phrase(reporteSel.get("CLASE_REPORTE").toString(), font));
			table.addCell(celda);
			celda.setPhrase(new Phrase("Tipo", font));
			table.addCell(celda);
			celda.setPhrase(new Phrase(reporteSel.get("TIPO_REPORTE").toString(), font));
			table.addCell(celda);
			celda.setPhrase(new Phrase("Estado", font));
			table.addCell(celda);
			celda.setPhrase(new Phrase(reporteSel.get("ESTADO").toString(), font));
			table.addCell(celda);
			celda.setPhrase(new Phrase("ROS", font));
			table.addCell(celda);
			celda.setPhrase(new Phrase(reporteSel.get("ROS").toString(), font));
			table.addCell(celda);
			document.add(table);
			Chunk tituloOperaciones = new Chunk("INFORMACIÓN DE LA OPERACIÓN",
					new Font(Font.UNDEFINED, 14, Font.ITALIC));
			document.add(new Paragraph(tituloOperaciones.setUnderline(0.2f, -2f)));

			if (listTransRep != null && !listTransRep.isEmpty()) {
				Font fontTrans = new Font(Font.UNDEFINED, 10);

				float[] anchos = { 18f, 10f, 17f, 35f, 20f, 5f, 10f, 18f };

				PdfPTable tablaTrans = new PdfPTable(8);
				tablaTrans.setWidthPercentage(100);
				tablaTrans.setSpacingBefore(5f);
				tablaTrans.setSpacingAfter(5f);
				tablaTrans.setWidths(anchos);
				PdfPCell celdaTrans = new PdfPCell();
				celdaTrans.setPhrase(new Phrase("Fecha", fontTrans));
				tablaTrans.addCell(celdaTrans);
				celdaTrans.setPhrase(new Phrase("Fuente", fontTrans));
				tablaTrans.addCell(celdaTrans);
				celdaTrans.setPhrase(new Phrase("Producto", fontTrans));
				tablaTrans.addCell(celdaTrans);
				celdaTrans.setPhrase(new Phrase("Tipo", fontTrans));
				tablaTrans.addCell(celdaTrans);
				celdaTrans.setPhrase(new Phrase("Valor", fontTrans));
				tablaTrans.addCell(celdaTrans);
				celdaTrans.setPhrase(new Phrase("+R", fontTrans));
				tablaTrans.addCell(celdaTrans);
				celdaTrans.setPhrase(new Phrase("Estado", fontTrans));
				tablaTrans.addCell(celdaTrans);
				celdaTrans.setPhrase(new Phrase("Estado DUCC", fontTrans));
				tablaTrans.addCell(celdaTrans);
				Iterator it = listTransRep.iterator();
				while (it.hasNext()) {
					
					Hashtable tran = (Hashtable) it.next();
					celdaTrans.setPhrase(new Phrase(tran.get("FECHA").toString(), fontTrans));
					tablaTrans.addCell(celdaTrans);
					celdaTrans.setPhrase(new Phrase(tran.get("FUENTE").toString(), fontTrans));
					tablaTrans.addCell(celdaTrans);
					celdaTrans.setPhrase(new Phrase(tran.get("N_PRODUCTO").toString(), fontTrans));
					tablaTrans.addCell(celdaTrans);
					celdaTrans.setPhrase(
							new Phrase(tran.get("CODIGO_TR") + " - " + tran.get("DESCRIPCION_TIPO_TR"), fontTrans));
					tablaTrans.addCell(celdaTrans);
					celdaTrans.setPhrase(new Phrase(tran.get("VALOR").toString(), fontTrans));
					celdaTrans.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tablaTrans.addCell(celdaTrans);
					celdaTrans.setPhrase(new Phrase(tran.get("MAYOR_RIESGO").toString(), fontTrans));
					celdaTrans.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					tablaTrans.addCell(celdaTrans);
					celdaTrans.setPhrase(new Phrase(tran.get("ESTADO_OFICINA").toString(), fontTrans));
					tablaTrans.addCell(celdaTrans);
					celdaTrans.setPhrase(new Phrase(tran.get("ESTADO_DUCC").toString(), fontTrans));
					tablaTrans.addCell(celdaTrans);
					tipoIdPDF=tran.get("TIPO_IDENTIFICACION").toString();
					numeroIdPDF=tran.get("NUMERO_IDENTIFICACION").toString();
					nombrePDF=tran.get("CLIENTE").toString();
					
				}
				document.add(tablaTrans);
			} else {
				document.add(new Paragraph("No hay transacciones relacionadas"));
			}
			Chunk tituloDescripcion = new Chunk("DESCRIPCIÓN", new Font(Font.UNDEFINED, 14, Font.ITALIC));
			document.add(new Paragraph(tituloDescripcion.setUnderline(0.2f, -2f)));
			String[] justificacion = justificacionInicial.split("\\\\r\\\\n");
			for (String linea : justificacion) {
				linea = linea.replaceAll("\\\\t", "\t");
				if (linea.length() == 0) {
					document.add(Chunk.NEWLINE);
				}
				Paragraph parrafo = new Paragraph(linea);
				parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(parrafo);
			}
			Chunk tituloPersonasRelacionadas = new Chunk("PERSONAS IMPLICADAS");
			document.add(new Paragraph(tituloPersonasRelacionadas.setUnderline(0.2f, -2f)));

			
			if (clienteRep != null && !clienteRep.isEmpty()) {
				PdfPTable tablePer = new PdfPTable(8);
				tablePer.setWidthPercentage(100);
				tablePer.setSpacingBefore(10f);
				tablePer.setSpacingAfter(10f);
				tablePer.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

				PdfPCell celdaP = new PdfPCell();
				celdaP.setBorder(PdfPCell.NO_BORDER);
				PdfPCell celdaP1 = new PdfPCell();
				celdaP1.setBorder(PdfPCell.NO_BORDER);

			celdaP.setPhrase(new Phrase("Tipo Identificación:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("TIPO_IDENTIFICACION").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Número Identificación:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("NUMERO_IDENTIFICACION").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Nombre/Razón Social:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("NOMBRES_RAZON_COMERCIAL").toString() + " "
					+ clienteRep.get("APELLIDOS_RAZON_SOCIAL").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Teléfono:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(
					clienteRep.get("TELEFONO").toString().equals("null") ? " " : clienteRep.get("TELEFONO").toString(),
					font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("FAX:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(
					clienteRep.get("FAX").toString().equals("null") ? " " : clienteRep.get("FAX").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Dirección:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("DIRECCION").toString().equals("null") ? " "
					: clienteRep.get("DIRECCION").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Municipio:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("CODIGO_MUNICIPIO").toString().equals("null") ? " "
					: clienteRep.get("CODIGO_MUNICIPIO").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Correo Electrónico:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("DIRECCION_EMAIL").toString().equals("null") ? " "
					: clienteRep.get("DIRECCION_EMAIL").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Relación:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("TIPO_RELACION_V").toString().equals("null") ? " "
					: clienteRep.get("TIPO_RELACION_V").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Inicio Vinculación:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("INICIO_VINCULACION").toString().equals("null") ? " "
					: clienteRep.get("INICIO_VINCULACION").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Fin Vinculación:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("FINAL_VINCULACION").toString().equals("null") ? " "
					: clienteRep.get("FINAL_VINCULACION").toString(), font));
			tablePer.addCell(celdaP1);
			// celdaP.setPhrase(new Phrase("Razón retiro:", font));
			// tablePer.addCell(celdaP);
			// celdaP1.setPhrase(new
			// Phrase(clienteRep.get("RAZON_RETIRO").toString(),font));
			// tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Ingreso mensual :", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("INGRESOS_MENSUALES").toString().equals("null") ? " "
					: clienteRep.get("INGRESOS_MENSUALES").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Fecha ingreso:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("FECHA_INGRESOS").toString().equals("null") ? " "
					: clienteRep.get("FECHA_INGRESOS").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("CIIU:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("CODIGO_CIIU").toString().equals("null") ? " "
					: clienteRep.get("CODIGO_CIIU").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Actividad Economica:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("DESCRIPCION_ACTIVIDAD_EC").toString(), font));
			tablePer.addCell(celdaP1);
			celdaP.setPhrase(new Phrase("Representante legal:", font));
			tablePer.addCell(celdaP);
			celdaP1.setPhrase(new Phrase(clienteRep.get("TI_REP_LEGAL").toString().equals("null") ? " "
					: clienteRep.get("TI_REP_LEGAL").toString() + " - " + clienteRep.get("NI_REP_LEGAL").toString()
							+ " - " + clienteRep.get("NOMBRES_REP_LEGAL").toString(),
					font));
			tablePer.addCell(celdaP1);
			document.add(tablePer);
	
			}
			else {
				
				PdfPTable tablePer2 = new PdfPTable(6);
				tablePer2.setWidthPercentage(100);
				tablePer2.setSpacingBefore(10f);
				tablePer2.setSpacingAfter(10f);
				tablePer2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

				PdfPCell celdaP2 = new PdfPCell();
				celdaP2.setBorder(PdfPCell.NO_BORDER);
				PdfPCell celdaP3 = new PdfPCell();
				celdaP3.setBorder(PdfPCell.NO_BORDER);
				
				celdaP2.setPhrase(new Phrase("Tipo Identificación:", font));
				tablePer2.addCell(celdaP2);
				celdaP3.setPhrase(new Phrase(tipoIdPDF, font));
				tablePer2.addCell(celdaP3);
				celdaP2.setPhrase(new Phrase("Número Identificación:", font));
				tablePer2.addCell(celdaP2);
				celdaP3.setPhrase(new Phrase(numeroIdPDF, font));
				tablePer2.addCell(celdaP3);
				celdaP2.setPhrase(new Phrase("Nombre/Razón Social:", font));
				tablePer2.addCell(celdaP2);
				celdaP3.setPhrase(new Phrase(nombrePDF, font));
				tablePer2.addCell(celdaP3);
				document.add(tablePer2);
				
			}
			
			Chunk tituloProcesoAnalisis = new Chunk("PROCESO DE ANÁLISIS");
			document.add(new Paragraph(tituloProcesoAnalisis.setUnderline(0.2f, -2f)));
			PdfPTable tableAnalisis = new PdfPTable(6);
			tableAnalisis.setWidthPercentage(100);
			tableAnalisis.setSpacingBefore(10f);
			tableAnalisis.setSpacingAfter(10f);
			tableAnalisis.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

			PdfPCell celdaA = new PdfPCell();
			celdaA.setBorder(PdfPCell.NO_BORDER);
			PdfPCell celdaB = new PdfPCell();
			celdaB.setBorder(PdfPCell.NO_BORDER);

			celdaA.setPhrase(new Phrase("Estado:", font));
			tableAnalisis.addCell(celdaA);
			celdaB.setPhrase(new Phrase(reporteSel.get("ESTADO").toString(), font));
			tableAnalisis.addCell(celdaB);

			Collection analisisTmp = datosreporteCtr.getListAnalisis();
			Iterator it = analisisTmp.iterator();
			String fecha = "";
			String numActa = "";
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				fecha = (String) ht.get("FECHA_ACTA");
				numActa = (String) ht.get("NO_ACTA");
			}

			celdaA.setPhrase(new Phrase("Acta:", font));
			tableAnalisis.addCell(celdaA);
			celdaB.setPhrase(new Phrase(numActa));
			tableAnalisis.addCell(celdaB);

			celdaA.setPhrase(new Phrase("Fecha Acta:", font));
			tableAnalisis.addCell(celdaA);
			celdaB.setPhrase(new Phrase(fecha));
			tableAnalisis.addCell(celdaB);
			document.add(tableAnalisis);

		} catch (Exception e) {
			e.printStackTrace();
		}
		document.close();
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment;filename=/reporte_" + idRep.toString() + ".pdf");
		response.setContentLength(baos.size());
		try {
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
		} catch (IOException ioe) {
		}
		context.responseComplete();
		// PrimeFaces.current().ajax().update("frmImprimirRep:reportViewer");

	}

	private String buscarValor(Hashtable objeto, String valor, String tabla, String campo, String campRet) {
		String valTemp = null;
		try {
			valTemp = objeto.get(valor).toString();
			valTemp = valTemp.equals("null") ? "" : valTemp;
			if (valTemp != null && !valTemp.isEmpty()) {
				String consRelTemp = " WHERE " + campo + " = '" + objeto.get(valor).toString() + "' ";
				Collection colResulCons = null;
				try {
					colResulCons = consTablaEJB.consultarTabla(0, 0, tabla, consRelTemp);
				} catch (Exception e) {
					System.out.println("ConsultarrepController|buscarValor:" + tabla + ": " + e.getMessage());
				}
				if (colResulCons != null) {
					Iterator itReul = colResulCons.iterator();
					if (itReul.hasNext()) {
						Hashtable htResul = (Hashtable) itReul.next();
						return htResul.get(campRet).toString();
					}
				}
			}
		} catch (Exception e) {
			valTemp = "Sin información";
		}
		return valTemp;
	}

	public void setFechaRep(Date fechaRep) {
		this.fechaRep = fechaRep;
	}

	public Date getFechaRep() {
		return fechaRep;
	}

	public void setNumeroRep(String numeroRep) {
		this.numeroRep = numeroRep;
	}

	public String getNumeroRep() {
		return numeroRep;
	}

	public void setRos(String ros) {
		this.ros = ros;
	}

	public String getRos() {
		return ros;
	}

	public void setEstadoRep(String estadoRep) {
		this.estadoRep = estadoRep;
	}

	public String getEstadoRep() {
		return estadoRep;
	}

	public void setClaseRep(String claseRep) {
		this.claseRep = claseRep;
	}

	public String getClaseRep() {
		return claseRep;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion() {
		return region;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getOficina() {
		return oficina;
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

	public void setListEstados(Collection listEstados) {
		this.listEstados = listEstados;
	}

	public Collection getListEstados() {
		return listEstados;
	}

	public void setListClases(Collection listClases) {
		this.listClases = listClases;
	}

	public Collection getListClases() {
		return listClases;
	}

	public void setListPerfiles(Collection listPerfiles) {
		this.listPerfiles = listPerfiles;
	}

	public Collection getListPerfiles() {
		return listPerfiles;
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

	public void setListTiposId(Collection listTiposId) {
		this.listTiposId = listTiposId;
	}

	public Collection getListTiposId() {
		return listTiposId;
	}

	public void setListReportes(Collection listReportes) {
		this.listReportes = listReportes;
	}

	public Collection getListReportes() {
		return listReportes;
	}

	public void setReporteSel(Hashtable reporteSel) {
		this.reporteSel = reporteSel;
	}

	public Hashtable getReporteSel() {
		return reporteSel;
	}

	public void setJustificacionInicial(String justificacionInicial) {
		this.justificacionInicial = justificacionInicial;
	}

	public String getJustificacionInicial() {
		return justificacionInicial;
	}

	public void setTab1Index(String tab1Index) {
		this.tab1Index = tab1Index;
	}

	public String getTab1Index() {
		return tab1Index;
	}

	public void setTab2Index(String tab2Index) {
		this.tab2Index = tab2Index;
	}

	public String getTab2Index() {
		return tab2Index;
	}

	public void setTabAnalRepActivada(boolean tabAnalRepActivada) {
		this.tabAnalRepActivada = tabAnalRepActivada;
	}

	public boolean isTabAnalRepActivada() {
		return tabAnalRepActivada;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setClienteRep(Hashtable clienteRep) {
		this.clienteRep = clienteRep;
	}

	public Hashtable getClienteRep() {
		return clienteRep;
	}

	public void setTabPerRelReporteActivada(boolean tabPerRelReporteActivada) {
		this.tabPerRelReporteActivada = tabPerRelReporteActivada;
	}

	public boolean isTabPerRelReporteActivada() {
		return tabPerRelReporteActivada;
	}

	public void setTabDocSopReporteActivada(boolean tabDocSopReporteActivada) {
		this.tabDocSopReporteActivada = tabDocSopReporteActivada;
	}

	public boolean isTabDocSopReporteActivada() {
		return tabDocSopReporteActivada;
	}

	public void setTranRep(Hashtable tranRep) {
		this.tranRep = tranRep;
	}

	public Hashtable getTranRep() {
		return tranRep;
	}

	public void setListTransRep(Collection listTransRep) {
		this.listTransRep = listTransRep;
	}

	public Collection getListTransRep() {
		return listTransRep;
	}

	public void setLabelNombresRazonComercial(String labelNombresRazonComercial) {
		this.labelNombresRazonComercial = labelNombresRazonComercial;
	}

	public String getLabelNombresRazonComercial() {
		return labelNombresRazonComercial;
	}

	public void setLabelApellidosRazonSocial(String labelApellidosRazonSocial) {
		this.labelApellidosRazonSocial = labelApellidosRazonSocial;
	}

	public String getLabelApellidosRazonSocial() {
		return labelApellidosRazonSocial;
	}

	public void setTabReporteActivada(boolean tabReporteActivada) {
		this.tabReporteActivada = tabReporteActivada;
	}

	public boolean isTabReporteActivada() {
		return tabReporteActivada;
	}

	public void setDatSoporte(Hashtable datSoporte) {
		this.datSoporte = datSoporte;
	}

	public Hashtable getDatSoporte() {
		return datSoporte;
	}

	public void setCodPerfilCreacionRep(String codPerfilCreacionRep) {
		this.codPerfilCreacionRep = codPerfilCreacionRep;
	}

	public String getCodPerfilCreacionRep() {
		return codPerfilCreacionRep;
	}

	public void setListPregRespRep(Collection listPregRespRep) {
		this.listPregRespRep = listPregRespRep;
	}

	public Collection getListPregRespRep() {
		return listPregRespRep;
	}

	public void setDatosreporteCtr(DatosreporteController datosreporteCtr) {
		this.datosreporteCtr = datosreporteCtr;
	}

	public DatosreporteController getDatosreporteCtr() {
		return datosreporteCtr;
	}

	public void setOtroDoc(String otroDoc) {
		this.otroDoc = otroDoc;
	}

	public String getOtroDoc() {
		return otroDoc;
	}

	public void setRespuestas(Hashtable respuestas) {
		this.respuestas = respuestas;
	}

	public Hashtable getRespuestas() {
		return respuestas;
	}

	public void setFrmgestionCtr(FrmgestionController frmgestionCtr) {
		this.frmgestionCtr = frmgestionCtr;
	}

	public FrmgestionController getFrmgestionCtr() {
		return frmgestionCtr;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}

	public void setListDocsAnalisis(Collection listDocsAnalisis) {
		this.listDocsAnalisis = listDocsAnalisis;
	}

	public Collection getListDocsAnalisis() {
		return listDocsAnalisis;
	}

	public void setRespuestasAnal(Hashtable respuestasAnal) {
		this.respuestasAnal = respuestasAnal;
	}

	public Hashtable getRespuestasAnal() {
		return respuestasAnal;
	}

	public void setListavaloresCtr(ListavaloresController listavaloresCtr) {
		this.listavaloresCtr = listavaloresCtr;
	}

	public ListavaloresController getListavaloresCtr() {
		return listavaloresCtr;
	}

	public String getZonaT() {
		return zonaT;
	}

	public void setZonaT(String zonaT) {
		this.zonaT = zonaT;
	}

}
