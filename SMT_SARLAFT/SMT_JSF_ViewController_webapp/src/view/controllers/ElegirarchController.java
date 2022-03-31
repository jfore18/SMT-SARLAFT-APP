package view.controllers;

import admin.util.Funciones;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import baseDatos.ConsultaTablaEJB;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Date;
import java.util.Hashtable;

import java.util.Iterator;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import presentacion.FacadeEJB;

@ManagedBean(name = "ElegirarchController")
@SessionScoped
public class ElegirarchController {

	@EJB
	private ConsultaTablaEJB consultaTablaEJB;
	@ManagedProperty("#{ContenidoController}")
	private ContenidoController contenidoCtr;
	private Funciones funciones;
	private MenuModel menuModel;
	private FacesContext facContex;
	private ExternalContext extContex;
	private HttpSession sesion;
	private Collection result;
	private boolean esAnalisis;
	private String fechaStr;
	private Date fecha;
	private String msgProceso;
	private String msgTituloDlg;
	private String codTipArch;
	private String codProduct;
	private String codArch;
	private String total;
	private String nombreItem;
	private String respuesta;
	private String entrada;
	private Integer progreso;
	private Thread thread;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		funciones = new Funciones();
		fecha = new Date();
		fecha = new Date(fecha.getTime() + TimeUnit.DAYS.toMillis(-1));
		fechaStr = funciones.dateToString(fecha);
		progreso = 0;
	}

	public void menuArch() {
		String urlBD = contenidoCtr.getUrlBD();
		esAnalisis = urlBD.contains("?analizar=true");
		System.out.println("esAnalisis????: " + esAnalisis);
		Integer numero;
		menuModel = new DefaultMenuModel();
		try {
			result = consultaTablaEJB.consultarTabla(0, 0, "V_ARCHIVOS_CARGUE", "ORDER BY CODIGO_ARCHIVO");
			DefaultSubMenu submenu = new DefaultSubMenu();
			if (esAnalisis) {
				submenu.setLabel("Análisis de Transacciones (Aplicación de filtros y criterios)");
			} else {
				submenu.setLabel("Cargue de Transacciones");

			}

			Iterator itr = result.iterator();
			boolean permitirCarga = true;
			numero=0;
			while (itr.hasNext()) {
				Hashtable arch = (Hashtable) itr.next();
				numero++;
				String p4 = numero.toString()+". " + arch.get("NOMBRE_TIPO_ARCHIVO") + " - " + arch.get("NOMBRE_PRODUCTO");
				String p1 = (String) arch.get("CODIGO_TIPO_ARCHIVO");
				String p2 = (String) arch.get("CODIGO_PRODUCTO");
				String p3 = (String) arch.get("CODIGO_ARCHIVO");
				String p5 = null;
				if (!esAnalisis) {
					String miConsulta = " SELECT COUNT(*) TOTAL " + " FROM TRANSACCIONES_CLIENTE "
							+ " WHERE FECHA_PROCESO = (SELECT FECHA_PROCESO FROM CONTROL_ENTIDAD)"
							+ " AND CODIGO_ARCHIVO = " + p3;
					Collection resultadosCruce = consultaTablaEJB.consultarTabla(0, 0, null, miConsulta);
					Iterator it = resultadosCruce.iterator();
					while (it.hasNext()) {
						p5 = (String) ((Hashtable) it.next()).get("TOTAL");
						p5 = p5.equals("0") ? null : "cruce";
					}
					permitirCarga = true;
					String tieneGestion = "SELECT 1 RESULTADO FROM TRANSACCIONES_REP ";
					String tieneComentarios = "SELECT 1 RESULTADO FROM COMENTARIOS ";
					String parteFinal = " WHERE FECHA_PROCESO = (SELECT FECHA_PROCESO FROM CONTROL_ENTIDAD)"
							+ " AND CODIGO_ARCHIVO = " + p3;
					tieneGestion += parteFinal;
					tieneComentarios += parteFinal;
					Collection cGestion = consultaTablaEJB.consultarTabla(0, 0, null, tieneGestion);
					Iterator iGestion = cGestion.iterator();
					while (iGestion.hasNext()) {
						permitirCarga = false;
						iGestion.next();
					}
					if (permitirCarga) {
						Collection cComentarios = consultaTablaEJB.consultarTabla(0, 0, null, tieneComentarios);
						Iterator iComentarios = cComentarios.iterator();
						while (iComentarios.hasNext()) {
							permitirCarga = false;
							iComentarios.next();
						}
					}
				}
				DefaultMenuItem item = new DefaultMenuItem(p4);
				if (permitirCarga) {
					item.setCommand("#{ElegirarchController.preProcesoArch1('" + p1 + "','" + p2 + "','" + p3 + "','"
							+ p4 + "','" + p5 + "')}");
				} else {
					item.setDisabled(true);
				}
				submenu.addElement(item);
			}
			menuModel.addElement(submenu);
		} catch (Exception e) {
			System.out.println("ElegirarchController|menuArch: " + e.getMessage());
		}
	}

	public void preProcesoArch1(String p1, String p2, String p3, String p4, String p5) {
		codTipArch = p1;
		codProduct = p2;
		codArch = p3;
		nombreItem = p4;
		total = p5;
		if (esAnalisis) {
			preProcesoArch3();
		} else {
			if (total != null && !total.equals("null")) {
				msgTituloDlg = "Cargue de archivos";
				msgProceso = "Ya existe un cargue para este archivo con la fecha de proceso actual, "
						+ "\n está seguro de querer volver a cargar este archivo? ";
				PrimeFaces.current().ajax().update(":frmElegArch:dlgConfirmReprocArch");
				PrimeFaces.current().executeScript("PF('wvdlgConfirmReprocArch').show()");
			} else {
				preProcesoArch2();
			}
		}
	}

	public void preProcesoArch2() {
		msgTituloDlg = "Fecha proceso archivo";
		msgProceso = "Qué fecha de archivo desea cargar? (YYYY/MM/DD): ";
		PrimeFaces.current().ajax().update(":frmElegArch:dlgGener1");
		PrimeFaces.current().executeScript("PF('wvdlgGener1').show()");
	}

	public void preProcesoArch3() {
		if (esAnalisis) {
			msgTituloDlg = "Confirmar analisis archivo";
			msgProceso = "Seguro quiere analizar las transacciones del archivo " + nombreItem;
		} else {
			System.out.println("fechaSelec: " + fecha);
			fechaStr = funciones.dateToString(fecha);
			System.out.println("fechaStr: " + fechaStr);
			msgTituloDlg = "Confirmar carga archivo";
			msgProceso = "Seguro quiere cargar las transacciones del archivo " + nombreItem + " con fecha "
					+ fechaStr + " ? ";
		}
		PrimeFaces.current().ajax().update(":frmElegArch:dlgConfirmProcArch");
		PrimeFaces.current().executeScript("PF('dlgwvConfirmProcArch').show()");
	}

	public void procesoArch() {
		Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		String usuario = (String) datosRegistro.get("CODIGO_USUARIO");
		int[] parametros = { java.sql.Types.NUMERIC };
		msgTituloDlg="";
		String fechaArchivo = "_" + fechaStr.substring(8, 10) + fechaStr.substring(5, 7) + fechaStr.substring(2, 4);
		entrada = "(" + codTipArch + "," + codProduct + ",'" + fechaArchivo + "'," + usuario + ",?)";
		String procedure;
		if (esAnalisis) {
			entrada = "(" + codArch + "," + usuario + ",?)";
			msgTituloDlg = "Resultado proceso análisis de archivo";
			procedure = "PK_LLAMAR_PROCESOS_BATCH.P_ANALIZAR_TRANSACCIONES";
		} else {
			msgTituloDlg = "Resultado proceso carga de archivo";
			procedure = "PK_LLAMAR_PROCESOS_BATCH.P_CARGAR_TRANSACCIONES";
		}
		thread = new Thread() {
			public void run() {
				try {
					respuesta = consultaTablaEJB.ejecutarProcedure(procedure, entrada, parametros);
				} catch (Exception e) {
					System.out.println("FrmpreinusualController|buscarTrans:Thread: " + e.getMessage());
				}
			}
		};
		thread.start();
		progreso = 0;
	}

	public void posProcesoArch() {
		progreso = 0;
		try {
			respuesta = respuesta.replace('[', ' ').replace(']', ' ').trim();
			respuesta = respuesta.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ').replace('"', '\'');
			try {
				int iRespuesta = Integer.parseInt(respuesta);
				Collection cMensaje = new ArrayList();
				cMensaje = consultaTablaEJB.consultarTabla(0, 0, "MENSAJES", "WHERE CODIGO = '" + respuesta + "'");
				Iterator it = cMensaje.iterator();
				while (it.hasNext()) {
					Hashtable item = (Hashtable) it.next();
					respuesta = (String) item.get("DESCRIPCION");
				}
			} catch (NumberFormatException eformat) {
				respuesta = respuesta.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ').replace('"', '\'');
			} catch (Exception ex) {
				respuesta = "Error al buscar el mensaje: "
						+ respuesta.replace('\n', ' ').replace('\t', ' ').replace('"', '\'');
			}
			msgProceso = respuesta;
		} catch (Exception e) {
			System.out.println("Entro a la excepcion para mostrar el mensaje" +esAnalisis);
			if (esAnalisis) {
				msgTituloDlg = "Resultado proceso analisis archivo";
			} else {
				msgTituloDlg = "Resultado proceso carga archivo";
			}
			msgProceso = e.getMessage();
		}
		PrimeFaces.current().executeScript("PF('wvdlgProgreso').hide()");
		PrimeFaces.current().ajax().update(":frmElegArch:dlgProcArch");
		PrimeFaces.current().executeScript("PF('dlgwvProcArch').show()");
	}

	private Integer updateProgress(Integer progreso) {
		System.out.println("en el hilo" +msgTituloDlg);
		if (progreso == null) {
			progreso = 0;
		} else if (thread != null) {
			if (thread.isAlive()) {
				progreso = progreso + (int) (Math.random() * 35);
				if (progreso >= 95) {
					progreso = 95;
				}
			} else {
				progreso = 100;
			}
		}
		return progreso;
	}

	public Collection getResult() {
		return result;
	}

	public void setResult(Collection resultIn) {
		result = resultIn;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModelIn) {
		menuModel = menuModelIn;
	}

	public void setContenidoCtr(ContenidoController contenidoCtr) {
		this.contenidoCtr = contenidoCtr;
	}

	public ContenidoController getContenidoCtr() {
		return contenidoCtr;
	}

	public String getFechaStr() {
		return fechaStr;
	}

	public void setFechaStr(String fechaStrIn) {
		fechaStr = fechaStrIn;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fechaIn) {
		fecha = fechaIn;
	}

	public void setMsgProceso(String msgProcesoIn) {
		msgProceso = msgProcesoIn;
	}

	public String getMsgProceso() {
		return msgProceso;
	}

	public void setMsgTituloDlg(String msgTituloDlgIn) {
		msgTituloDlg = msgTituloDlgIn;
	}

	public String getMsgTituloDlg() {
		return msgTituloDlg;
	}

	public void setCodTipArch(String codTipArchIn) {
		codTipArch = codTipArchIn;
	}

	public String getCodTipArch() {
		return codTipArch;
	}

	public void setCodProduct(String codProductIn) {
		codProduct = codProductIn;
	}

	public String getCodProduct() {
		return codProduct;
	}

	public void setNombreItem(String nombreItemIn) {
		nombreItem = nombreItemIn;
	}

	public String getNombreItem() {
		return nombreItem;
	}

	public void setCodArch(String codArchIn) {
		codArch = codArchIn;
	}

	public String getCodArch() {
		return codArch;
	}

	public void setProgreso(Integer progreso) {
		this.progreso = progreso;
	}

	public Integer getProgreso() {
		progreso = updateProgress(progreso);
		return progreso;
	}

}
