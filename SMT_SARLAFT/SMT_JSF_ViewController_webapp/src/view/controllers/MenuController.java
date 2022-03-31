package view.controllers;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import javax.faces.bean.ManagedBean;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import presentacion.FacadeEJB;

@ManagedBean(name = "MenuController")
@SessionScoped
public class MenuController implements Serializable {
    @SuppressWarnings("compatibility:2608067186015683056")
    private static final long serialVersionUID = 1L;

    @EJB
    FacadeEJB facadeEJB;
    @ManagedProperty("#{LoginController}")
    private LoginController loginCtr;
    @ManagedProperty("#{ContenidoController}")
    private ContenidoController contenidoCtr;
    Collection elementos;
    List<Hashtable> listElementos;
    FacesContext facContex;
    ExternalContext extContex;
    HttpSession sesion;
    private String opSel;
    private String opAnt;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        try {
            Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
            if (datosRegistro != null) {
                String codigoCargo = (String) datosRegistro.get("CODIGO_CARGO");
                elementos = facadeEJB.consultarMenu(codigoCargo);
                Iterator it = elementos.iterator();
                String url = "";
               if (it.hasNext()) {
                    Hashtable elem = (Hashtable) it.next();
                    url = (String) elem.get("PAGINA");
                    contenidoCtr.setIniciando(true);
                    contenidoCtr.updateUrl(url);
                    contenidoCtr.agregarElemPila(elem.get("TITLE").toString());
                    opAnt = "0";
                    opSel = elem.get("OPCION").toString();
                }
            } else {
                System.out.println("MenuController|consultaElementosMenu|datosRegistro: null");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("MenuController|consultaElementosMenu|Error consultando menu");
        }

    }

    public void opcSelec(Hashtable opMenuSel) {
        System.out.println("opMenuSel: " + opMenuSel);
        if (opMenuSel != null) {
            opAnt = opSel;
            opSel = opMenuSel.get("OPCION").toString();
            PrimeFaces.current().executeScript("opSelect(" + opSel + "," + opAnt + ");");
            contenidoCtr.iniciarPila(opMenuSel.get("TITLE").toString());
        }
    }

    public Collection getElementos() {
        return elementos;
    }

    public void setElementos(Collection elementosIn) {
        elementos = elementosIn;
    }

    public List<Hashtable> getListElementos() {
        return listElementos;
    }

    public void setlistElementos(List<Hashtable> listElementosIn) {
        listElementos = listElementosIn;
    }

    public void setOpSel(String opSel) {
        this.opSel = opSel;
    }

    public void setOpAnt(String opAnt) {
        this.opAnt = opAnt;
    }

    public String getOpSel() {
        return opSel;
    }

    public String getOpAnt() {
        return opAnt;
    }

    public void setContenidoCtr(ContenidoController contenidoCtr) {
        this.contenidoCtr = contenidoCtr;
    }

    public ContenidoController getContenidoCtr() {
        return contenidoCtr;
    }

    public void setLoginCtr(LoginController loginCtr) {
        this.loginCtr = loginCtr;
    }

    public LoginController getLoginCtr() {
        return loginCtr;
    }
}
