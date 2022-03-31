package view.controllers;

import admin.autenticacion.AutenticacionEJB;

import javax.annotation.Generated;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.primefaces.context.PrimeRequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import admin.usuario.UsuarioEJB;

import admin.autenticacion.AutenticacionException;

import admin.autenticacion.ConsultaDominiosException;

import admin.usuario.Usuario;

import java.io.IOException;

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

import org.primefaces.PrimeFaces;

@ManagedBean(name = "LoginController")
@SessionScoped
public class LoginController {

    @EJB
    AutenticacionEJB autenticacionEJB;
    @EJB
    UsuarioEJB usuarioEJB;
    private Usuario usuario;
    Collection<String> dominios;
    FacesContext facContex;
    ExternalContext extContex;
    HttpSession sesion;
    FacesMessage msg;
    PrimeRequestContext primeContext;
    private Hashtable datosRegistro;
    private String registro;
    private Collection perfiles;
    private List<Hashtable> listPerf;

    @PostConstruct
    void init() {
   
        try {
            dominios = autenticacionEJB.getDominios();
            PrimeFaces.current().ajax().update("formLogin:dominio");
        } catch (Exception e) {
            System.out.println("error consultando dominios");
            e.printStackTrace();
        }
    }

    public LoginController() {
        usuario = new Usuario();
        datosRegistro = null;

    }

    @SuppressWarnings("static-access")
	public void autenticarUsuario() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        primeContext = PrimeRequestContext.getCurrentInstance();
        String dominioNT, usuarioNT, password;
        Collection resultado;
        try {
            dominioNT = usuario.getDominioUsuario();
            usuarioNT = usuario.getNombre();
            password = usuario.getPassword();
         
            //autenticacionEJB.autenticar(usuarioNT, password, dominioNT);
            perfiles = usuarioEJB.buscarUsuario(dominioNT, usuarioNT);
            //TEMPORAL
            /*   Iterator iter = perfiles.iterator();
            while(iter.hasNext()){
                datosRegistro = (Hashtable)iter.next();
                if(datosRegistro.get("CODIGO_USUARIO").toString().equals("103125415")){
                //if(datosRegistro.get("CODIGO_USUARIO").toString().equals("1094268598")){
                    break;
                }
            }
            this.btnContinuar();*/
            if (perfiles.size() == 1) {
                Iterator it = perfiles.iterator();
                if (it.hasNext()) {
                    datosRegistro = (Hashtable) it.next();
                }
                this.btnContinuar();
            } else if (perfiles.size() > 1) {
                PrimeFaces.current().ajax().update("formPerfiles:tablaPerfiles");
                PrimeFaces.current().executeScript("PF('dgwvPerfiles').show()");
                primeContext.getCallbackParams().put("logueo", true);
            }
            else if(perfiles.size()==0) {
            	 msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR", "Usuario no existe en SMT-SARLAFT. Verifique usuario y cargo.");
                 facContex.getCurrentInstance().addMessage(null, msg);
                 primeContext.getCallbackParams().put("logueo", false);
            }
        } catch (AutenticacionException ae) {
        	System.out.println("Entro aca web service de autenticacion");
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR", "Datos ingresados incorrectos. Por favor verificar.");
            facContex.getCurrentInstance().addMessage(null, msg);
            primeContext.getCallbackParams().put("logueo", false);
            ae.printStackTrace();
        } catch (Exception e) {
        	System.out.println("Entro aca otro error de autenticacion");
            //e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR", e.getMessage());
            facContex.getCurrentInstance().addMessage(null, msg);
            primeContext.getCallbackParams().put("logueo", false);
        }
    }

    public void btnContinuar() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        if (datosRegistro != null) {
            boolean cargoActivo = ((String) datosRegistro.get("CARGO_ACTIVO")).equals("1") ? true : false;
            boolean usuarioActivo = ((String) datosRegistro.get("USUARIO_ACTIVO")).equals("1") ? true : false;
            if (cargoActivo && usuarioActivo) {
                datosRegistro.put("DOMINIO_NT", usuario.getDominioUsuario());
                datosRegistro.put("USUARIO_NT", usuario.getNombre().toUpperCase());
                sesion.setAttribute("datosRegistro", datosRegistro);
                //System.out.println("==> "+datosRegistro);
                try {
                    extContex.redirect(extContex.getApplicationContextPath() + "/faces/frame.jsf");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        } else {

        }
    }

    public void asignarSelec(Object objSel) {
        if (objSel != null) {
            datosRegistro = (Hashtable) objSel;
        }
    }

    public void terminarSesion(String op) {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        System.out.println("terminarSesion|extContex: " + extContex.getApplicationContextPath());
        try {
            sesion.removeAttribute("datosRegistro");
            sesion.invalidate();
            if (op.equals("0")) {
                extContex.redirect(extContex.getApplicationContextPath() + "/faces/login.jsf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invalidarSesion() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        try {
            sesion.removeAttribute("datosRegistro");
            sesion.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LoginController(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Collection<String> getDominios() {
        return dominios;
    }

    public void setDominios(Collection<String> dominiosIn) {
        dominios = dominiosIn;
    }

    public void setDatosRegistro(Hashtable datosRegistroIn) {
        datosRegistro = datosRegistroIn;
    }

    public Hashtable getDatosRegistro() {
        return datosRegistro;
    }

    public void setRegistro(String registroIn) {
        registro = registroIn;
    }

    public String getRegistro() {
        return registro;
    }

    public Collection getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(Collection perfilesIn) {
        perfiles = perfilesIn;
    }

    public List getListPerf() {
        return listPerf;
    }

    public void setListPerf(List perfilesIn) {
        listPerf = perfilesIn;
    }
}
