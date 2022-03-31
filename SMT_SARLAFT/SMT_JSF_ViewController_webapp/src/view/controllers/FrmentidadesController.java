package view.controllers;

import admin.EntidadExcluidaEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;
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

@ManagedBean(name = "FrmentidadesController")
@SessionScoped
public class FrmentidadesController {

    @EJB
    ConsultaTablaEJB consultaTablaEJB;
    @EJB
    EntidadExcluidaEJB entidadExcluidaEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Funciones funciones;
    private Collection listEntidades;
    private Collection listTiposEntidad;
    private Hashtable datosRegistro;
    private Hashtable entidadSel;
    private FacesMessage fcMsg;
    private String usuario;
    private String tipoEntidad;
    private String tipoId;
    private String numeroId;
    private String nombre;
    private String fechaAct;
    private String cargaObl;
    private String condicion;
    private String proceso;
    private String msg;
    private boolean disabled;
    private boolean continuar;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        usuario = datosRegistro.get("CODIGO_USUARIO").toString();
        funciones = new Funciones();
        iniciarElementos();
        disabled = false;
    }

    private void iniciarElementos() {
        tipoEntidad = "";
        tipoId = "";
        numeroId = "";
        nombre = "";
        cargaObl = "";
        fechaAct = "";
        proceso = "INSERTAR";
    }

    private String flagsTipos() {
        int tipoInt = Integer.parseInt(tipoEntidad);
        String flagTipo = "";
        for (int i = 1; i <= 8; i++) {
            if (i == tipoInt) {
                flagTipo += "1";
            } else {
                flagTipo += "0";
            }
        }
        return flagTipo;
    }

    public void buscarEntidades() {
        condicion = " WHERE 0<1";
        if (tipoEntidad != null) {
            String flagTipo = flagsTipos();
            condicion += "AND FLAGS_TIPOS = '" + flagTipo + "'";
        }
        if (tipoId != null) {
            condicion += " AND TIPO_IDENTIFICACION = '" + tipoId + "'";
        }
        if (numeroId != null && !numeroId.isEmpty()) {
            condicion += " AND NUMERO_IDENTIFICACION LIKE '%" + numeroId + "%'";
        }
        if (nombre != null && !nombre.isEmpty()) {
            condicion += " AND UPPER(NOMBRE) LIKE '%" + nombre.toUpperCase() + "%'";
        }
        if (cargaObl != null) {
            condicion += " AND OBLIGAR_CARGA = '" + cargaObl + "'";
        }
        System.out.println("condicion: " + condicion);
        try {
            listEntidades = consultaTablaEJB.consultarTabla(0, 0, "ENTIDADES_EXCLUIDAS", condicion);
            Iterator it = listEntidades.iterator();
            while (it.hasNext()) {
                Hashtable ht = (Hashtable) it.next();
                ht = funciones.quitarNull(ht);
                ht.replace("FECHA_ACTUALIZACION", funciones.formatoFechaHora(ht.get("FECHA_ACTUALIZACION")));
            }
        } catch (Exception e) {
            System.out.println("FrmentidadesController|buscarEntidades: " + e.getMessage());
        }
        System.out.println("listEntidades: " + listEntidades);
    }

    public void limpiar() {
        disabled = false;
        iniciarElementos();
        listEntidades = null;
    }

    public void guardar() {
        validar();
        if (continuar) {
            Long usuarioActual = new Long(usuario);
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
            Hashtable datos = new Hashtable();
            datos.put("TIPO_IDENTIFICACION", tipoId);
            datos.put("NUMERO_IDENTIFICACION", numeroId);
            datos.put("USUARIO_ACTUALIZACION", usuarioActual);
            datos.put("FECHA_ACTUALIZACION", fechaActual);
            if (proceso.equals("INSERTAR")) {
                datos.put("TIPO_ENTIDAD", tipoEntidad);
                datos.put("NOMBRE", nombre.toUpperCase());
                datos.put("OBLIGAR_CARGA", new Integer(cargaObl));
                try {
                    entidadExcluidaEJB.crearEntidad(datos);
                    msg = "Se creó la entidad en la lista exitosamente";
                    limpiar();
                } catch (SQLException e) {
                    msg = "" + e.getMessage();
                    System.out.println("FrmentidadesController|guardar|INSERTAR: " + e.getMessage());
                }
            }
            if (proceso.equals("ACTUALIZAR")) {
                datos.put("TIPO_ENTIDAD", tipoEntidad);
                datos.put("NOMBRE", nombre.toUpperCase());
                datos.put("OBLIGAR_CARGA", new Integer(cargaObl));
                try {
                    entidadExcluidaEJB.actualizarEntidad(datos);
                    msg = "Se actualizó la entidad exitosamente";
                    limpiar();
                } catch (SQLException e) {
                    msg = "" + e.getMessage();
                    System.out.println("FrmentidadesController|guardar|ACTUALIZAR: " + e.getMessage());
                }
            }
            if (proceso.equals("ELIMINAR")) {
                try {
                    entidadExcluidaEJB.borrarEntidad(datos);
                    msg = "Se borró la entidad exitosamente";
                    limpiar();
                } catch (SQLException e) {
                    msg = "" + e.getMessage();
                    System.out.println("FrmentidadesController|guardar|ELIMINAR: " + e.getMessage());
                }
            }
        }
        fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);
    }
    
    public void actualizar() {
        if (entidadSel != null) {
            String flagTipo = entidadSel.get("FLAGS_TIPOS").toString();
            tipoEntidad = String.valueOf(flagTipo.indexOf("1")+1);
            tipoId = entidadSel.get("TIPO_IDENTIFICACION").toString();
            numeroId = entidadSel.get("NUMERO_IDENTIFICACION").toString();
            nombre = entidadSel.get("NOMBRE").toString();
            cargaObl = entidadSel.get("OBLIGAR_CARGA").toString();
            fechaAct = entidadSel.get("FECHA_ACTUALIZACION").toString();
        }
        disabled = true;
        proceso = "ACTUALIZAR";
    }

    public void eliminar() {
        proceso = "ELIMINAR";
        guardar();
    }

    private void validar() {
        continuar = true;
        continuar = true;
        if (!proceso.equals("ELIMINAR")) {
            if (tipoEntidad == null) {
                msg = "Debe seleccionar un tipo de entidad";
                continuar = false;
            }
        }
        if (continuar && tipoId == null) {
            msg = "Debe seleccionar un tipo de identificación";
            continuar = false;
        }
        if (continuar && numeroId.isEmpty()) {
            msg = "Debe ingresar el número de identificación";
            continuar = false;
        }
        if (continuar && nombre.isEmpty()) {
            msg = "Debe ingresar el nombre de la entidad";
            continuar = false;
        }
        if (continuar && cargaObl == null) {
            msg = "Debe seleccionar un valor para obligar carga";
            continuar = false;
        }
    }

    public void setListEntidades(Collection listEntidades) {
        this.listEntidades = listEntidades;
    }

    public Collection getListEntidades() {
        return listEntidades;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setListTiposEntidad(Collection listTiposEntidad) {
        this.listTiposEntidad = listTiposEntidad;
    }

    public Collection getListTiposEntidad() {
        return listTiposEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setFechaAct(String fechaAct) {
        this.fechaAct = fechaAct;
    }

    public String getFechaAct() {
        return fechaAct;
    }

    public void setCargaObl(String cargaObl) {
        this.cargaObl = cargaObl;
    }

    public String getCargaObl() {
        return cargaObl;
    }

    public void setEntidadSel(Hashtable entidadSel) {
        this.entidadSel = entidadSel;
    }

    public Hashtable getEntidadSel() {
        return entidadSel;
    }
}
