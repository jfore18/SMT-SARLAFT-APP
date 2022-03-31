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

@ManagedBean(name = "FrmttController")
@SessionScoped
public class FrmttController {

    @EJB
    ConsultaTablaEJB consultaTablaEJB;
    @EJB
    TipoTransaccionEJB tipoTransaccionEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Funciones funciones;
    private Collection listTt;
    private Hashtable ttSel;
    private Hashtable datosRegistro;
    private String usuario;
    private String msg;
    private String proceso;
    private String producto;
    private String codigoTx;
    private String descripcion;
    private String mayorRiesgo;
    private String naturaleza;
    private FacesMessage fcMsg;
    private boolean continuar;
    private boolean disabled;

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

    public void buscarTt() {
        String condicion = " WHERE 0<1";
        if (producto != null) {
            condicion += " AND (CODIGO_PRODUCTO_V = '" + producto + "')";
        }
        if (!codigoTx.isEmpty()) {
            condicion += " AND (CODIGO_TRANSACCION LIKE '%" + codigoTx + "%')";
        }
        if (!descripcion.isEmpty()) {
            condicion += " AND (UPPER(DESCRIPCION) LIKE '%" + descripcion.toUpperCase() + "%')";
        }
        if (mayorRiesgo != null) {
            condicion += " AND (MAYOR_RIESGO = '" + mayorRiesgo + "')";
        }
        if (naturaleza != null) {
            condicion += " AND (NATURALEZA = '" + naturaleza + "')";
        }
        System.out.println("condicion: " + condicion);
        try {
            listTt = consultaTablaEJB.consultarTabla(0, 0, "V_TIPOS_TRANSACCION", condicion);
        } catch (Exception e) {
            System.out.println("FrmttController|buscarTt: " + e.getMessage());
        }
        System.out.println("listTt: " + listTt);
    }

    public void guardar() {
        validar();
        if (continuar) {
            Hashtable datos = new Hashtable();
            datos.put("CODIGO_PRODUCTO_V", producto);
            datos.put("CODIGO_TRANSACCION", codigoTx);
            datos.put("DESCRIPCION", descripcion.toUpperCase());
            datos.put("MAYOR_RIESGO", mayorRiesgo.equals("1"));
            datos.put("NATURALEZA", naturaleza);
            if (proceso.equals("INSERTAR")) {
                try {
                    tipoTransaccionEJB.crear(datos);
                    msg = "Se creó exitosamente el tipo de transacción: " + codigoTx + " - " + descripcion.toUpperCase();
                    limpiar();
                } catch (SQLException e) {
                    msg = "" + e.getMessage();
                }
            }
            if (proceso.equals("ACTUALIZAR")) {
                try {
                    tipoTransaccionEJB.actualizar(datos);
                    msg = "Se actualizó exitosamente el tipo de transacción: " + codigoTx + " - " + descripcion.toUpperCase();
                    limpiar();
                } catch (SQLException e) {
                    msg = "" + e.getMessage();
                }
            }
        }
        fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);
    }

    public void actualizar() {
        proceso = "ACTUALIZAR";
        producto = ttSel.get("CODIGO_PRODUCTO_V").toString();
        codigoTx = ttSel.get("CODIGO_TRANSACCION").toString();
        descripcion = ttSel.get("DESCRIPCION").toString();
        mayorRiesgo = ttSel.get("MAYOR_RIESGO").toString();
        naturaleza = ttSel.get("NATURALEZA").toString();
        disabled = true;
    }

    public void limpiar() {
        proceso = "INSERTAR";
        producto = null;
        codigoTx = "";
        descripcion = "";
        mayorRiesgo = null;
        naturaleza = null;
        listTt = null;
        disabled = false;
    }

    private void validar() {
        continuar = true;
        if (producto == null) {
            continuar = false;
            msg = "Debe seleccionar el producto";
        }
        if (continuar && codigoTx.isEmpty()) {
            continuar = false;
            msg = "Debe ingresar el código de transacción";
        }
        if (continuar && descripcion.isEmpty()) {
            continuar = false;
            msg = "Debe ingresar la descripción";
        }
        if (continuar && mayorRiesgo == null) {
            continuar = false;
            msg = "Debe seleccionar mayor riesgo";
        }
        if (continuar && naturaleza == null) {
            continuar = false;
            msg = "Debe seleccionar la naturaleza";
        }
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setCodigoTx(String codigoTx) {
        this.codigoTx = codigoTx;
    }

    public String getCodigoTx() {
        return codigoTx;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setMayorRiesgo(String mayorRiesgo) {
        this.mayorRiesgo = mayorRiesgo;
    }

    public String getMayorRiesgo() {
        return mayorRiesgo;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setListTt(Collection listTt) {
        this.listTt = listTt;
    }

    public Collection getListTt() {
        return listTt;
    }

    public void setTtSel(Hashtable ttSel) {
        this.ttSel = ttSel;
    }

    public Hashtable getTtSel() {
        return ttSel;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
