package view.controllers;

import javax.annotation.Generated;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.primefaces.context.PrimeRequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import admin.autenticacion.AutenticacionException;

import admin.autenticacion.ConsultaDominiosException;

import baseDatos.ConsultaTablaEJB;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

import java.util.Stack;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import javax.faces.bean.ViewScoped;

import javax.inject.Named;

import org.primefaces.PrimeFaces;

import presentacion.FacadeEJB;

@ManagedBean(name = "ContenidoController")
@SessionScoped
public class ContenidoController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private FacadeEJB facadeEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private String url;
    private boolean esAnalisisTrans;
    private boolean esProcTrans;
    private boolean iniciando;
    private boolean flagFechaVencidaAdmin;
    private String procsoCrm; //1:genera y 2:obtiene
    private String urlBD;
    private String urlAnt;
    private String navegacion;
    private Stack<String> pila;
    private boolean flagVigenciaFiltro;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        url = "";
        pila = new Stack<String>();
        flagFechaVencidaAdmin = true;
        flagVigenciaFiltro = false;
        validarVigenciaFiltro();
    }

    public void updateUrl(String urlIn) {
        if (urlIn != null) {
            urlAnt = url;
            urlBD = urlIn;
            url = urlIn.replace(".", "-");
            String[] partUrl = url.split("-");
            url = partUrl[0] + ".jsf";
        } else {
            url = "./error.jsf";
        }
        System.out.println("url: " + url);
        if (!iniciando) {
            PrimeFaces.current().ajax().update("panelContenido");
        } else {
            iniciando = false;
        }
    }

    public void validarFechaVencidaAdmin() {
        if (flagFechaVencidaAdmin) {
            String Cantidad = "";
            String condicion =
                "SELECT Count(1) cantidad " + "FROM preguntas_rep " + "WHERE vigente_hasta < Trunc(SYSDATE)";
            Collection consultaFechaPreguntas = null;
            try {
                consultaFechaPreguntas = consTablaEJB.consultarTabla(0, 0, null, condicion);
            } catch (Exception e) {

            }
            if (consultaFechaPreguntas != null) {
                Iterator itr = consultaFechaPreguntas.iterator();
                while (itr.hasNext()) {
                    Hashtable cantidad = (Hashtable) itr.next();
                    Cantidad = (String) cantidad.get("CANTIDAD");
                }
            }
            if (!Cantidad.equals("0")) {
                PrimeFaces.current().executeScript("PF('dgwvVigPreg').show()");
            }
            flagFechaVencidaAdmin = false;
        }
    }

	public void validarVigenciaFiltro() {
	
		Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		String cargo = (String) datosRegistro.get("CODIGO_CARGO");
		String perfil = (String) datosRegistro.get("CODIGO_PERFIL");
		if (perfil.equals("2") || perfil.equals("5")) {
			flagVigenciaFiltro = facadeEJB.isConfirmarFiltro(cargo);
			if (flagVigenciaFiltro) {

				PrimeFaces.current().executeScript("PF('dgwvVigFil').show()");
			}
			System.out.println("Este es el flagvigencia" + flagVigenciaFiltro);
		}
	}

    public void iniciarPila(String nombre) {
        pila.clear();
        pila.push(nombre);
        llenarStrNavegacion();
    }

    public void agregarElemPila(String nombre) {
        pila.push(nombre);
        llenarStrNavegacion();
    }

    public void sacarElemPila() {
        pila.pop();
        llenarStrNavegacion();
    }

    public void sacar2ElemPila() {
        pila.pop();
        pila.pop();
        llenarStrNavegacion();
    }

    public void sacar3ElemPila() {
        pila.pop();
        pila.pop();
        pila.pop();
        llenarStrNavegacion();
    }

    public void sacar4ElemPila() {
        pila.pop();
        pila.pop();
        pila.pop();
        pila.pop();
        llenarStrNavegacion();
    }

    private void llenarStrNavegacion() {
        navegacion = "";
        for (String nombre : pila) {
            navegacion += " > " + nombre;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String urlIn) {
        url = urlIn;
    }

    public boolean getEsAnalisisTrans() {
        return esAnalisisTrans;
    }

    public void setEsAnalisisTrans(boolean esAnalisisTransIn) {
        esAnalisisTrans = esAnalisisTransIn;
    }

    public boolean getEsProcTrans() {
        return esProcTrans;
    }

    public void setEsProcTrans(boolean esProcTransIn) {
        esProcTrans = esProcTransIn;
    }

    public String getProcsoCrm() {
        return procsoCrm;
    }

    public void setProcsoCrm(String procsoCrmIn) {
        procsoCrm = procsoCrmIn;
    }

    public void setUrlBD(String urlBD) {
        this.urlBD = urlBD;
    }

    public String getUrlBD() {
        return urlBD;
    }

    public void setUrlAnt(String urlAnt) {
        this.urlAnt = urlAnt;
    }

    public String getUrlAnt() {
        return urlAnt;
    }

    public void setPila(Stack<String> pila) {
        this.pila = pila;
    }

    public Stack<String> getPila() {
        return pila;
    }

    public void setNavegacion(String navegacion) {
        this.navegacion = navegacion;
    }

    public String getNavegacion() {
        return navegacion;
    }

    public void setIniciando(boolean iniciando) {
        this.iniciando = iniciando;
    }

    public boolean isIniciando() {
        return iniciando;
    }

	public boolean isFlagVigenciaFiltro() {
		return flagVigenciaFiltro;
	}

	public void setFlagVigenciaFiltro(boolean flagVigenciaFiltro) {
		this.flagVigenciaFiltro = flagVigenciaFiltro;
	}
}

