package view.controllers;

import admin.util.Funciones;

import baseDatos.ConsultaEJB;
import baseDatos.ConsultaTablaEJB;

import java.util.ArrayList;
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

import presentacion.FacadeEJB;

@ManagedBean(name = "ListavaloresController")
@SessionScoped
public class ListavaloresController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    private Funciones funciones;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Collection regiones;
    private Collection listaValores;
    private Collection zonas;
    private Collection tiposDocumento;
    private Collection fuentes;
    private Collection relaciones;
    private Collection tiposTelefono;
    private Collection razonesRetiro;
    private Collection tiposDireccion;
    private Collection tiposEstadoTr;
    private Collection tiposCargo;
    private Collection perfiles;
    private Collection tiposEntidades;
    private Collection productos;
    private Collection productosCritInu;
    private Hashtable datosRegistro;
    private String rangoAnios;
    private Integer limiteRes;
    private String rows;
    private String rowsPerPage;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        funciones = new Funciones();
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        rangoAnios = "2000:2099";
        limiteRes = 100;
        rows = "10";
        rowsPerPage = "10,15,20,50";
        consultaListValores();
    }

    public void consultaListValores() {
        try {
            listaValores = consTablaEJB.consultarTabla(0, 0, "V_LISTA_VALORES", null);
        } catch (Exception e) {
            System.out.println("ListavaloresController|consultaListValores: " + e.getMessage());
        }
    }

    private Collection consultarElemento(String tipoDato) {
        Collection collRetorno = new ArrayList();
        Iterator it = listaValores.iterator();
        while (it.hasNext()) {
            Hashtable ht = (Hashtable) it.next();
            if (ht.get("TIPO_DATO").toString().equals(tipoDato)) {
                collRetorno.add(ht);
            }
        }
        return collRetorno;
    }

    public Collection getRegiones() {
        regiones = consultarElemento("3");
        return regiones;
    }

    public Collection getZonas() {
        zonas = consultarElemento("99");
        return zonas;
    }

    public Collection getFuentes() {
        fuentes = consultarElemento("2");
        return fuentes;
    }

    public Collection getTiposDocumento() {
        tiposDocumento = tiposDocumento == null ? consultarElemento("17") : tiposDocumento;
        return tiposDocumento;
    }

    public Collection getRelaciones() {
        relaciones = relaciones == null ? consultarElemento("12") : relaciones;
        return relaciones;
    }

    public Collection getTiposTelefono() {
        tiposTelefono = tiposTelefono == null ? consultarElemento("22") : tiposTelefono;
        return tiposTelefono;
    }

    public Collection getRazonesRetiro() {
        razonesRetiro = razonesRetiro == null ? consultarElemento("13") : razonesRetiro;
        return razonesRetiro;
    }

    public Collection getTiposDireccion() {
        tiposDireccion = tiposDireccion == null ? consultarElemento("18") : tiposDireccion;
        return tiposDireccion;
    }

    public Collection getTiposEstadoTr() {
        tiposEstadoTr = tiposEstadoTr == null ? consultarElemento("11") : tiposEstadoTr;
        return tiposEstadoTr;
    }

    public Collection getTiposCargo() {
        tiposCargo = tiposCargo == null ? consultarElemento("14") : tiposCargo;
        return tiposCargo;
    }

    public Collection getPerfiles() {
        perfiles = perfiles == null ? consultarElemento("10") : perfiles;
        return perfiles;
    }

    public Collection getTiposEntidades() {
        tiposEntidades = tiposEntidades == null ? consultarElemento("24") : tiposEntidades;
        return tiposEntidades;
    }
    
    public Collection getProductos() {
        productos = productos == null ? consultarElemento("2") : productos;
        return productos;
    }
    public Collection getProductosCritInu() {
        productosCritInu = productosCritInu == null ? consultarElemento("27") : productosCritInu;
        return productosCritInu;
    }

    public String getRangoAnios() {
        return rangoAnios;
    }

    public void setLimiteRes(Integer limiteRes) {
        this.limiteRes = limiteRes;
    }

    public Integer getLimiteRes() {
        return limiteRes;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getRows() {
        return rows;
    }

    public void setRowsPerPage(String rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public String getRowsPerPage() {
        return rowsPerPage;
    }
}
