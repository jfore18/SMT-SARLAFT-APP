package view.controllers;

import admin.util.Funciones;

import baseDatos.CatalogoBD;
import baseDatos.Consulta;
import baseDatos.ConsultaEJB;
import baseDatos.ConsultaTablaEJB;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import java.util.Iterator;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import presentacion.FacadeEJB;

@ManagedBean(name = "IndestController")
@SessionScoped
public class IndestController {

	@ManagedProperty("#{ContenidoController}")
	private ContenidoController contenidoCtr;
	@EJB
	private ConsultaTablaEJB consTablaEJB;
	@EJB
	private ConsultaEJB consultaEJB;
	@EJB
	private FacadeEJB facadeEJB;
	private Consulta miConsulta;
	private Funciones funciones;
	private FacesContext facContex;
	private ExternalContext extContex;
	private HttpSession sesion;
	private Collection<Hashtable> listIndicadores;
	private Hashtable indicadorSel;
	private Hashtable datosRegistro;
	private Hashtable totales;
	private int nivel;
	private String perfil;
	private String tipoCargo;
	private String fechaCorte;
	private String condicion;
	private String codRegion;
	private String codZona;
	private String codOficina;
	private String encabezado;
	private String nombreRegion;
	private String nombreZona;
	private String nombreOficina;
	private String nombreProceso;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		perfil = datosRegistro.get("CODIGO_PERFIL").toString();
		tipoCargo = (String) datosRegistro.get("CODIGO_TIPO_CARGO");
		funciones = new Funciones();
		condicion = "";
	}

	public void iniciarNivel() {
		nombreRegion = "";
		encabezado = "";
		nombreRegion = "";
		nombreZona = "";
		nombreOficina = "";
		// Gerente de zona
		if (perfil.equals("3")) {
			nombreRegion = datosRegistro.get("NOMBRE_REGION").toString();
			nombreZona = datosRegistro.get("NOMBRE_ZONA").toString();
			encabezado = nombreRegion + " | " + nombreZona;
		}
		// Director de region
		else if (perfil.equals("4")) {
			nombreRegion = datosRegistro.get("NOMBRE_REGION").toString();
			encabezado = nombreRegion;
		}
		// Gerente unidad de negocio
		else if (perfil.equals("2") && !tipoCargo.equals("N")) {
			nombreRegion = datosRegistro.get("NOMBRE_REGION").toString();
			nombreZona = datosRegistro.get("NOMBRE_ZONA").toString();
			nombreOficina = datosRegistro.get("NOMBRE_UNIDAD_NEGOCIO").toString();
			encabezado = nombreRegion + " | " + nombreZona + " | " + nombreOficina;
		}
		// Gerente de Distrito
		else if (perfil.equals("2") && tipoCargo.equals("N")) {
			nombreRegion = datosRegistro.get("NOMBRE_REGION").toString();
			nombreZona = datosRegistro.get("NOMBRE_ZONA").toString();
			encabezado = nombreRegion + " | " + nombreZona;
		}

		condicion = "";
		codRegion = "";
		codZona = "";
		codOficina = "";
		indicadorSel = null;
		if (contenidoCtr.getUrlBD().contains("MR")) {
			nivel = 19;
			nombreProceso = "Indicadores y Estadísticas (ANALISTA)";
		} else {
			nombreProceso = "Indicadores y estadísticas (OFICINA)";
			nivel = 15;
		}
		consultaIndicacores();
	}

	public void consultaIndicacores() {
		String codCargo = (String) datosRegistro.get("CODIGO_CARGO");
		String tipoCargo = (String) datosRegistro.get("CODIGO_TIPO_CARGO");

		fechaCorte = CatalogoBD.formatoFecha.format(new Date());
		String strConsultaFechaInd = "SELECT TO_CHAR(FECHA_ACTUALIZACION, 'YYYY/MM/DD HH:MI AM') FECHA_ACTUALIZACION FROM LISTA_VALORES "
				+ " WHERE TIPO_DATO = 20 AND CODIGO = 99";
		try {
			listIndicadores = consTablaEJB.consultarTabla(0, 0, null, strConsultaFechaInd);
		} catch (Exception e) {
			System.out.println("IndestController|consultaIndicacores:LISTA_VALORES: " + e.getMessage());
		}
		if (listIndicadores != null) {
			Iterator itRes = listIndicadores.iterator();
			while (itRes.hasNext()) {
				Hashtable res = (Hashtable) itRes.next();
				fechaCorte = (String) res.get("FECHA_ACTUALIZACION");
			}
		}
		if (tipoCargo.equals("N")) {
			miConsulta = consultaEJB.realizarConsultaTipoCargo(codCargo, String.valueOf(nivel));
		} else {
			miConsulta = consultaEJB.realizarConsulta(codCargo, String.valueOf(nivel));
		}

		miConsulta.setOrden(" ORDER BY 1 ASC");
		listIndicadores = facadeEJB.ejecutarConsultaGeneral(condicion, miConsulta, 0);
		Iterator itInd = listIndicadores.iterator();
		float total1 = 0, total2 = 0, total3 = 0, total4 = 0, total5 = 0, total6 = 0;
		while (itInd.hasNext()) {
			Hashtable ind = (Hashtable) itInd.next();
			ind = funciones.quitarNull(ind);
			Float porcentaje = null;
			float dividendo = Float.parseFloat((String) ind.get("GESTIONADAS"));
			float divisor = Float.parseFloat((String) ind.get("RECIBIDAS"));
			if (divisor != 0) {
				porcentaje = new Float(dividendo / divisor);
			} else {
				porcentaje = new Float(divisor);
			}
			ind.put("PORCENTAJE_GESTIONADAS", porcentaje);
			ind.put("MENOR_TRES", ind.get("<3"));
			ind.put("ENTRE_TRES_Y_CINCO", ind.get("3-5"));
			ind.put("MAYOR_CINCO", ind.get(">5"));
			total1 += Float.parseFloat((String) ind.get("RECIBIDAS"));
			total2 += Float.parseFloat((String) ind.get("GESTIONADAS"));
			total4 += Float.parseFloat((String) ind.get("MENOR_TRES"));
			total5 += Float.parseFloat((String) ind.get("ENTRE_TRES_Y_CINCO"));
			total6 += Float.parseFloat((String) ind.get("MAYOR_CINCO"));
			ind.replace("PORCENTAJE_GESTIONADAS", funciones.formatoPorcentaje(ind.get("PORCENTAJE_GESTIONADAS")));
			ind.replace("RECIBIDAS", funciones.formatoMiles(ind.get("RECIBIDAS")));
			ind.replace("GESTIONADAS", funciones.formatoMiles(ind.get("GESTIONADAS")));
			ind.replace("MENOR_TRES", funciones.formatoMiles(ind.get("MENOR_TRES")));
			ind.replace("ENTRE_TRES_Y_CINCO", funciones.formatoMiles(ind.get("ENTRE_TRES_Y_CINCO")));
			ind.replace("MAYOR_CINCO", funciones.formatoMiles(ind.get("MAYOR_CINCO")));
		}
		totales = new Hashtable();
		totales.put("TOTAL_RECIBIDAS", total1);
		totales.put("TOTAL_GESTIONADAS", total2);
		total3 = total1 > 0 ? (total2 / total1) : 0;
		totales.put("TOTAL_PORCENTAJE_GESTIONADAS", total3);
		totales.put("TOTAL_MENOR_TRES", total4);
		totales.put("TOTAL_ENTRE_TRES_Y_CINCO", total5);
		totales.put("TOTAL_MAYOR_CINCO", total6);
		totales.replace("TOTAL_RECIBIDAS", funciones.formatoMiles(totales.get("TOTAL_RECIBIDAS")));
		totales.replace("TOTAL_GESTIONADAS", funciones.formatoMiles(totales.get("TOTAL_GESTIONADAS")));
		String porcGest = funciones.formatoPorcentaje(totales.get("TOTAL_PORCENTAJE_GESTIONADAS"));
		totales.replace("TOTAL_PORCENTAJE_GESTIONADAS", porcGest);
		totales.replace("TOTAL_MENOR_TRES", funciones.formatoMiles(totales.get("TOTAL_MENOR_TRES")));
		totales.replace("TOTAL_ENTRE_TRES_Y_CINCO", funciones.formatoMiles(totales.get("TOTAL_ENTRE_TRES_Y_CINCO")));
		totales.replace("TOTAL_MAYOR_CINCO", funciones.formatoMiles(totales.get("TOTAL_MAYOR_CINCO")));
		System.out.println("listIndicadores: " + listIndicadores);
		System.out.println("totales: " + totales);
	}

	public void avanzar() {
		if (perfil.equals("5")) {
			if (nivel != 18 && nivel != 22) {
				// indicadorSel = indSel;
				System.out.println("indicadorSel: " + indicadorSel);
				if (nivel == 15 || nivel == 19) {
					nombreRegion = (String) indicadorSel.get("REGION");
					encabezado = nombreRegion;
					codRegion = (String) indicadorSel.get("CODIGO_REGION");
					condicion = " CODIGO_REGION = '" + codRegion + "'";
				} else if (nivel == 16 || nivel == 20) {
					nombreZona = (String) indicadorSel.get("ZONA");
					encabezado += " | " + nombreZona;
					codZona = (String) indicadorSel.get("CODIGO_ZONA,CODIGO_REGION");
					condicion = " (CODIGO_ZONA,CODIGO_REGION) = " + codZona;
				} else if (nivel == 17 || nivel == 21) {
					nombreOficina = (String) indicadorSel.get("OFICINA");
					encabezado += " | " + nombreOficina;
					codOficina = (String) indicadorSel.get("CODIGO_OFICINA");
					condicion += " AND CODIGO_OFICINA = '" + codOficina + "'";
				}
				nivel++;
				consultaIndicacores();
			}
		}
		if (perfil.equals("3") || (perfil.equals("2") && tipoCargo.equals("N"))) {
			if (nivel == 15) {
				nombreOficina = (String) indicadorSel.get("OFICINA");
				encabezado += " | " + nombreOficina;
				codOficina = (String) indicadorSel.get("CODIGO_OFICINA");
				condicion += " AND CODIGO_OFICINA = '" + codOficina + "'";
				nivel++;
				consultaIndicacores();
			}
		}
		if (perfil.equals("4")) {

			if (nivel == 15) {
				nombreZona = (String) indicadorSel.get("ZONA");
				encabezado += " | " + nombreZona;
				codZona = (String) indicadorSel.get("CODIGO_ZONA,CODIGO_REGION");
				condicion = " (CODIGO_ZONA,CODIGO_REGION) = " + codZona;
				nivel++;
			} else if (nivel == 16) {
				nombreOficina = (String) indicadorSel.get("OFICINA");
				encabezado += " | " + nombreOficina;
				codOficina = (String) indicadorSel.get("CODIGO_OFICINA");
				condicion += " AND CODIGO_OFICINA = '" + codOficina + "'";
				nivel++;
			}
			
			consultaIndicacores();
		}

	}

	public void volver() {
		if (perfil.equals("5")) {
			nivel--;
			if (nivel == 15 || nivel == 19) {
				encabezado = "";
				condicion = "";
			} else if (nivel == 16 || nivel == 20) {
				encabezado = nombreRegion;
				condicion = " CODIGO_REGION = '" + codRegion + "'";
			} else if (nivel == 17 || nivel == 21) {
				encabezado = nombreRegion + " | " + nombreZona;
				condicion = " (CODIGO_ZONA,CODIGO_REGION) = " + codZona;
			}
			consultaIndicacores();
		}
		if (perfil.equals("3") || (perfil.equals("2") && tipoCargo.equals("N"))) {
			encabezado = nombreRegion + " | " + nombreZona;
			condicion = "";
			nivel--;
			consultaIndicacores();
		}

		if (perfil.equals("4")) {
			nivel--;

			if (nivel == 15) {
				encabezado = nombreRegion;
				condicion = "";
			}

			if (nivel == 16) {
				encabezado = nombreRegion + " | " + nombreZona;
				condicion = " (CODIGO_ZONA,CODIGO_REGION) = " + codZona;
			}
			consultaIndicacores();
		}

	}

	public void setListIndicadores(Collection listIndicadores) {
		this.listIndicadores = listIndicadores;
	}

	public Collection getListIndicadores() {
		return listIndicadores;
	}

	public void setIndicadorSel(Hashtable indicadorSel) {
		this.indicadorSel = indicadorSel;
	}

	public Hashtable getIndicadorSel() {
		return indicadorSel;
	}

	public void setMiConsulta(Consulta miConsulta) {
		this.miConsulta = miConsulta;
	}

	public Consulta getMiConsulta() {
		return miConsulta;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getNivel() {
		return nivel;
	}

	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	public String getFechaCorte() {
		return fechaCorte;
	}

	public void setTotales(Hashtable totales) {
		this.totales = totales;
	}

	public Hashtable getTotales() {
		return totales;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setContenidoCtr(ContenidoController contenidoCtr) {
		this.contenidoCtr = contenidoCtr;
	}

	public ContenidoController getContenidoCtr() {
		return contenidoCtr;
	}

	public void setNombreProceso(String nombreProceso) {
		this.nombreProceso = nombreProceso;
	}

	public String getNombreProceso() {
		return nombreProceso;
	}
}
