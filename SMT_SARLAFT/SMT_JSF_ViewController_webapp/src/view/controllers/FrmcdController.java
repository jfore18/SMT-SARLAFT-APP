package view.controllers;

import admin.TipoTransaccionEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;

import java.util.Collection;
import java.util.Hashtable;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import presentacion.FacadeEJB;

@ManagedBean(name = "FrmcdController")
@SessionScoped
public class FrmcdController {

    @EJB
    ConsultaTablaEJB consultaTablaEJB;
    @EJB
    FacadeEJB facadeEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Funciones funciones;
    private Collection listCd;
    private Hashtable cdSel;
    private Hashtable datosRegistro;
    private String usuario;
    private String msg;
    private String tipoId;
    private String numeroId;
    private String proceso;
    private FacesMessage fcMsg;
    private boolean continuar;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        usuario = datosRegistro.get("CODIGO_USUARIO").toString();
        funciones = new Funciones();
        limpiar();
    }

    public void buscarCd() {
        String condicion = " WHERE 0<1";
        if (tipoId != null) {
            condicion += " AND (TIPO_IDENTIFICACION = '" + tipoId + "')";
        }
        if (!numeroId.isEmpty()) {
            condicion += " AND (NUMERO_IDENTIFICACION LIKE '%" + numeroId + "%')";
        }
        System.out.println("condicion: " + condicion);
        try {
            listCd = consultaTablaEJB.consultarTabla(0, 0, "CONSULTA_DUCC", condicion);
        } catch (Exception e) {
            System.out.println("FrmcdController|buscarCd: " + e.getMessage());
        }
        System.out.println("listCd: " + listCd);
    }

    public void limpiar() {
        tipoId = null;
        numeroId = "";
        listCd = null;
        proceso = "INSERTAR";
    }

    public void actualizar() {
        tipoId = cdSel.get("TIPO_IDENTIFICACION").toString();
        numeroId = cdSel.get("NUMERO_IDENTIFICACION").toString();
        proceso = "ELIMINAR";
    }

    private void validar() {
        continuar = true;
        if (tipoId == null) {
            continuar = false;
            msg = "Debe seleccionar el tipo de identificación";
        }
        if (continuar && numeroId.isEmpty()) {
            continuar = false;
            msg = "Debe ingresar el número de identificación";
        }
    }

    public void guardar() {
        validar();
        if (continuar) {
            Hashtable datos = new Hashtable();
            datos.put("TIPO_IDENTIFICACION", tipoId);
            datos.put("NUMERO_IDENTIFICACION", numeroId);
            if (proceso.equals("INSERTAR")) {
                try {
                    facadeEJB.insertarRegistroConsultaDUCC(datos);
                    msg = "El registro fue creado existosamente";
                    limpiar();
                } catch (SQLException e) {
                    msg = ""+e.getMessage();
                }
            }
            if (proceso.equals("ELIMINAR")) {
                try {
                    facadeEJB.eliminarRegistroConsultaDUCC(datos);
                    msg = "El registro fue borrado existosamente";
                    limpiar();
                } catch (SQLException e) {
                    msg = ""+e.getMessage();
                }
            }
        }
        fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);
    }

    public void setListCd(Collection listCd) {
        this.listCd = listCd;
    }

    public Collection getListCd() {
        return listCd;
    }

    public void setCdSel(Hashtable cdSel) {
        this.cdSel = cdSel;
    }

    public Hashtable getCdSel() {
        return cdSel;
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

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getProceso() {
        return proceso;
    }
}
