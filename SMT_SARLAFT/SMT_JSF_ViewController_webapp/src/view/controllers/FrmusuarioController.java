package view.controllers;

import admin.usuario.Usuario;
import admin.usuario.UsuarioEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.Hashtable;

import java.util.Iterator;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

@ManagedBean(name = "FrmusuarioController")
@SessionScoped
public class FrmusuarioController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private UsuarioEJB usuarioEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Funciones funciones;
    private FacesMessage fcMsg;
    private Hashtable datosRegistro;
    private Hashtable usuarioSel;
    private Collection listUsuarios;
    private String cedula;
    private String nombre;
    private String condicion;
    private String msg;
    private boolean activo;
    private boolean continuar;
    private boolean disabled;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        funciones = new Funciones();
        limpiar();
    }

    public void buscar() {
        condicion = " WHERE 0<1";
        if (!cedula.isEmpty()) {
            condicion += " AND (CEDULA LIKE '%" + cedula + "%')";
        }
        if (!nombre.isEmpty()) {
            condicion += " AND (NOMBRE LIKE '%" + nombre + "%')";
        }
        if (activo) {
            condicion += " AND (ACTIVO = '1')";
        }
        System.out.println("condicion: " + condicion);
        try {
            listUsuarios = consTablaEJB.consultarTabla(0, 0, "USUARIO", condicion);
            Iterator it = listUsuarios.iterator();
            while (it.hasNext()) {
                Hashtable ht = (Hashtable) it.next();
                ht = funciones.quitarNull(ht);
            }
        } catch (Exception e) {
            System.out.println("FrmcargoController|buscarCargos" + e.getMessage());
        }
        System.out.println("listUsuarios: " + listUsuarios);
    }

    public void limpiar() {
        cedula = "";
        nombre = "";
        activo = false;
        listUsuarios = null;
        usuarioSel = null;
        disabled = false;
    }

    public void actualizar() {
        cedula = usuarioSel.get("CEDULA").toString();
        nombre = usuarioSel.get("NOMBRE").toString();
        activo = usuarioSel.get("ACTIVO").equals("1");
        disabled = true;
    }

    private void validar() {
        continuar = true;

    }

    public void guardar() {
        msg = "";
        String tipoMsg = "";
        try {
            Long usuarioActual = new Long(datosRegistro.get("CODIGO_USUARIO").toString());
            Long codigoUsuario = new Long(cedula);
            Usuario usuarioTemp;
            usuarioTemp = usuarioEJB.findByPrimaryKey(codigoUsuario);
            Integer activoInt = activo ? 1 : 0;
            usuarioTemp.setActivo(activoInt);
            usuarioTemp.setFechaActualizacion(new Timestamp(System.currentTimeMillis()));
            usuarioTemp.setUsuarioActualizacion(usuarioActual);
            usuarioEJB.actualizarUsuario(usuarioTemp);
            msg = "Se actualizó el usuario con éxito ";
            tipoMsg = "exito";
        } catch (Exception e) {
            msg = "No se actualizó el usuario: " + e.getMessage();
            tipoMsg = "error";
        }
        sesion.setAttribute("msg", msg);
        sesion.setAttribute("tipoMsg", tipoMsg);
        limpiar();
        /*fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);*/
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setUsuarioSel(Hashtable usuarioSel) {
        this.usuarioSel = usuarioSel;
    }

    public Hashtable getUsuarioSel() {
        return usuarioSel;
    }

    public void setListUsuarios(Collection listUsuarios) {
        this.listUsuarios = listUsuarios;
    }

    public Collection getListUsuarios() {
        return listUsuarios;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
