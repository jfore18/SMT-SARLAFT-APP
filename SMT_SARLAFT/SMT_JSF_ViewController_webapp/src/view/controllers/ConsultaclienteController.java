package view.controllers;

import admin.util.Funciones;

import baseDatos.Consulta;
import baseDatos.ConsultaEJB;
import baseDatos.ConsultaTablaEJB;

import java.util.Collection;

import java.util.Hashtable;

import java.util.Iterator;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

import presentacion.FacadeEJB;

@ManagedBean(name = "ConsultaclienteController")
@SessionScoped
public class ConsultaclienteController {

	@EJB
	ConsultaEJB consultaEJB;
	@EJB
	FacadeEJB facadeEJB;
	@EJB
	private ConsultaTablaEJB consTablaEJB;
	private Funciones funciones;
	private FacesContext facContex;
	private ExternalContext extContex;
	private Consulta miConsulta;
	private HttpSession sesion;
	private Collection listCliente;
	private Hashtable clienteSel;
	private Hashtable datosRegistro;
	private int nivel;
	private int totalCli;
	private String condicion;
	private String encabezado;
	private String nombreProceso;
	private String nombreRegion;
	private String codRegion;
	private String nombreZona;
	private String codZona;
	private String perfil;
	private String tipoCargo;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		funciones = new Funciones();
		datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		nivel = 31;
	}

	public void iniciar(String proceso) {
		if (proceso.equals("CLI_PRE")) {
			nivel = 31;
			nombreProceso = "Clientes con transacciones preinusuales";
		}
		if (proceso.equals("CLI_MR")) {
			nivel = 34;
			nombreProceso = "Clientes con transacciones preinusuales de mayor riesgo";
		}
		if (proceso.equals("CLI_FIL")) {
			nivel = 38;
			nombreProceso = "Clientes con filtros aprobados";
		}
		condicion = "";
		llenarLista();
	}

	public void llenarLista() {
		String cargo = datosRegistro.get("CODIGO_CARGO").toString();
		perfil = datosRegistro.get("CODIGO_PERFIL").toString();
		miConsulta = consultaEJB.realizarConsulta(cargo, String.valueOf(nivel));
		listCliente = facadeEJB.ejecutarConsultaGeneral(condicion, miConsulta, 100);
		if (listCliente != null) {
			Iterator itemp = listCliente.iterator();
			int total = 0;
			while (itemp.hasNext()) {
				Hashtable htemp = (Hashtable) itemp.next();
				total += Integer.valueOf(htemp.get("TOTAL").toString());
			}
			totalCli = total;
		}
		System.out.println("lista: " + listCliente);
		System.out.println("Este es el nivel:" + nivel);
		if (nivel == 38 || nivel == 31) {
			if (perfil.equals("3")) {
				nombreRegion = datosRegistro.get("NOMBRE_REGION").toString();
				nombreZona = datosRegistro.get("NOMBRE_ZONA").toString();
				encabezado = nombreRegion + " | " + nombreZona;
			} else if (perfil.equals("4")) {
				nombreRegion = datosRegistro.get("NOMBRE_REGION").toString();
				encabezado = nombreRegion;
			} else {
				encabezado = "";
			}
		}
	}

	public void procesar(String accion) {
		if (accion.equals("AVANZAR")) {

			if (nivel != 33 && nivel != 36 && nivel != 40) {
				if (nivel == 31 || nivel == 34 || nivel == 38) {
					if (perfil.equals("5")) {
						nombreRegion = (String) clienteSel.get("REGION");
						encabezado = nombreRegion;
						codRegion = (String) clienteSel.get("Z.CODIGO_REGION_V");
						condicion = " AND Z.CODIGO_REGION_V = " + codRegion;
					}
					if (perfil.equals("4")) {
						nombreZona = (String) clienteSel.get("ZONA");
						encabezado += " | " + nombreZona;
						codZona = (String) clienteSel.get("U.CODIGO_ZONA");
						condicion = " AND (U.CODIGO_ZONA) = " + codZona;
					}
					nivel++;
				} else if (nivel == 32 || nivel == 35 || nivel == 39) {
					if (perfil.equals("5")) {
						nombreZona = (String) clienteSel.get("ZONA");
						encabezado += " | " + nombreZona;
						codZona = (String) clienteSel.get("CODIGO_ZONA,CODIGO_REGION_V");
						condicion = " AND (CODIGO_ZONA,CODIGO_REGION_V) = " + codZona;
						nivel++;
					}

				}
				llenarLista();
			}
		}
		if (accion.equals("VOLVER")) {
			nivel--;
			if (nivel == 31 || nivel == 34 || nivel == 38) {
				if (perfil.equals("5")) {
					encabezado = "";
					condicion = "";
				}
				if (perfil.equals("4")) {
					encabezado = nombreRegion;
					condicion = "";
				}
			}
			if (nivel == 32 || nivel == 35 || nivel == 39) {
				encabezado = nombreRegion;
				condicion = " AND Z.CODIGO_REGION_V = " + codRegion;
			}
			llenarLista();
		}

	}

	public void setListCliente(Collection listCliente) {
		this.listCliente = listCliente;
	}

	public Collection getListCliente() {
		return listCliente;
	}

	public void setClienteSel(Hashtable clienteSel) {
		this.clienteSel = clienteSel;
	}

	public Hashtable getClienteSel() {
		return clienteSel;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setNombreProceso(String nombreProceso) {
		this.nombreProceso = nombreProceso;
	}

	public String getNombreProceso() {
		return nombreProceso;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getNivel() {
		return nivel;
	}

	public void setMiConsulta(Consulta miConsulta) {
		this.miConsulta = miConsulta;
	}

	public Consulta getMiConsulta() {
		return miConsulta;
	}

	public void setTotalCli(int totalCli) {
		this.totalCli = totalCli;
	}

	public int getTotalCli() {
		return totalCli;
	}
}
