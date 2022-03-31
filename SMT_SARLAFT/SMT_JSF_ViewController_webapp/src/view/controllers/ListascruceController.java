package view.controllers;

import admin.PersonasReportadasEJB;

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

@ManagedBean(name = "ListascruceController")
@SessionScoped
public class ListascruceController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private PersonasReportadasEJB personasRepEJB;
    private Funciones funciones;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Collection listMotivos;
    private Collection listTiposDoc;
    private Collection listTiposRep;
    private Collection listasCruce;
    private Hashtable listaSel;
    private FacesMessage fcMsg;
    private String msg;
    private String motivoSel;
    private String tipoDocSel;
    private String tipoRepSel;
    private String numeroDoc;
    private String nombres;
    private String apellidos;
    private String alias;
    private String fechaIngreso;
    private String comentario;
    private String numeroRep;
    private String ros;
    private String condicion;
    private String proceso;
    private boolean disabled;
    private boolean disabled2;
    private boolean continuar;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        funciones = new Funciones();
        iniciarListas();
    }

    public void inicio() {
        disabled = false;
        disabled2 = false;
        limpiar("0");
    }

    private void iniciarListas() {
        try {
            listMotivos = consTablaEJB.consultarTabla(0, 0, "V_TIPOS_MOTIVO", null);
        } catch (Exception e) {
            System.out.println("ListascruceController|iniciarListas:V_TIPOS_MOTIVO: " + e.getMessage());
        }
        System.out.println("listMotivos: " + listMotivos);
        try {
            listTiposDoc = consTablaEJB.consultarTabla(0, 0, "V_TIPOS_DOCUMENTO", null);
        } catch (Exception e) {
            System.out.println("ListascruceController|iniciarListas:V_TIPOS_DOCUMENTO: " + e.getMessage());
        }
        System.out.println("listTiposDoc: " + listTiposDoc);
        try {
            listTiposRep = consTablaEJB.consultarTabla(0, 0, "V_TIPOS_REPORTE", null);
        } catch (Exception e) {
            System.out.println("ListascruceController|iniciarListas:V_TIPOS_REPORTE: " + e.getMessage());
        }
        System.out.println("listTiposRep: " + listTiposRep);
    }

    public void buscarListasCruce() {
        condicion = " WHERE 0<1";
        if (motivoSel != null && !motivoSel.isEmpty()) {
            condicion += " AND CODIGO_MOTIVO_V = '" + motivoSel + "'";
        }
        if (tipoDocSel != null && !tipoDocSel.isEmpty()) {
            condicion += " AND TIPO_IDENTIFICACION = '" + tipoDocSel + "'";
        }
        if (tipoRepSel != null && !tipoRepSel.isEmpty()) {
            condicion += " AND TIPO_REPORTE = '" + tipoRepSel + "'";
        }
        if (numeroDoc != null && !numeroDoc.isEmpty()) {
            condicion += " AND NUMERO_IDENTIFICACION LIKE '" + numeroDoc + "'";
        }
        if (nombres != null && !nombres.isEmpty()) {
            condicion += " AND UPPER(NOMBRES_RAZON_COMERCIAL) LIKE '%" + nombres.toUpperCase() + "%'";
        }
        if (apellidos != null && !apellidos.isEmpty()) {
            condicion += " AND UPPER(APELLIDOS_RAZON_SOCIAL) LIKE '%" + apellidos.toUpperCase() + "%'";
        }
        if (alias != null && !alias.isEmpty()) {
            condicion += " AND UPPER(ALIAS) LIKE '%" + alias.toUpperCase() + "%'";
        }
        if (comentario != null && !comentario.isEmpty()) {
            condicion += " AND UPPER(COMENTARIO) LIKE '%" + comentario.toUpperCase() + "%'";
        }
        if (ros != null && !ros.isEmpty()) {
            condicion += " AND ROS LIKE '%" + ros + "%'";
        }
        condicion+=" ORDER BY TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION ASC";
        try {
            listasCruce = consTablaEJB.consultarTabla(0, 0, "V_LISTAS_CRUCE", condicion);
        } catch (Exception e) {
            System.out.println("cc|buscarListasCruce:V_LISTAS_CRUCE: " + e.getMessage());
        }
        if (listasCruce != null) {
            Iterator iter = listasCruce.iterator();
            while (iter.hasNext()) {
                Hashtable list = (Hashtable) iter.next();
                list = funciones.quitarNull(list);
                list.replace("FECHA_INGRESO", funciones.formatoFechaHora(list.get("FECHA_INGRESO")));
            }
        }
        System.out.println("listasCruce: " + listasCruce);
    }

    public void limpiar(String op) {
        motivoSel = "";
        tipoDocSel = "";
        tipoRepSel = "";
        numeroDoc = "";
        nombres = "";
        apellidos = "";
        alias = "";
        fechaIngreso = "";
        comentario = "";
        numeroRep = "";
        ros = "";
        proceso = op.equals("2") ? "ACTUALIZAR" : "INSERTAR";
        listasCruce = op.equals("1") ? null : listasCruce;
        listaSel = op.equals("1") ? null : listaSel;
        disabled = op.equals("2");
        disabled2 = !op.equals("0");
    }

    public void actualizar() {
        System.out.println("listaSel: " + listaSel);
        if (listaSel != null) {
            limpiar("2");
           // disabled2 = false;
            funciones.quitarNull(listaSel);
            motivoSel = listaSel.get("CODIGO_MOTIVO_V").toString();
            tipoDocSel = listaSel.get("TIPO_IDENTIFICACION").toString();
            tipoRepSel = listaSel.get("TIPO_REPORTE").toString();
            numeroDoc = listaSel.get("NUMERO_IDENTIFICACION").toString();
            nombres = listaSel.get("NOMBRES_RAZON_COMERCIAL").toString();
            apellidos = listaSel.get("APELLIDOS_RAZON_SOCIAL").toString();
            alias = listaSel.get("ALIAS").toString();
            fechaIngreso = listaSel.get("FECHA_INGRESO").toString();
            comentario = listaSel.get("COMENTARIO").toString();
            numeroRep = listaSel.get("ID_REPORTE").toString();
            ros = listaSel.get("ROS").toString();
        }
    }

    public void eliminar() {
        proceso = "ELIMINAR";
        continuar = true;
        guardar();
    }

    private void validar() {
        continuar = true;
        if (motivoSel == null || motivoSel.isEmpty()) {
            msg = "Debe seleccionar un motivo: ";
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            continuar = false;
        } else if (tipoDocSel == null || tipoDocSel.isEmpty()) {
            msg = "Debe seleccionar un tipo de documento: ";
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            continuar = false;
        } else if (numeroDoc == null || numeroDoc.isEmpty()) {
            msg = "Debe escribir el número de documento: ";
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            continuar = false;
        } else if (apellidos == null || apellidos.isEmpty()) {
            msg = "Debe escribir el apellido: ";
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            continuar = false;
        } else if (nombres == null || nombres.isEmpty()) {
            msg = "Debe escribir el nombre: ";
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            continuar = false;
        } else if (comentario == null || comentario.isEmpty()) {
            msg = "Debe escribir un comentario: ";
            fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
            continuar = false;
        }
    }

    public void guardar() {
        if (!proceso.equals("ELIMINAR")) {
            validar();
        }
        if (continuar) {
            Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
            Long codUsu = Long.parseLong((String) datosRegistro.get("CODIGO_USUARIO"));
            Hashtable tabla = new Hashtable();
            tabla.put("CODIGO_MOTIVO_V", motivoSel);
            tabla.put("TIPO_IDENTIFICACION", tipoDocSel);
            tabla.put("NUMERO_IDENTIFICACION", numeroDoc);
            try {
                tabla.put("ID_REPORTE", new Integer(numeroRep));
            } catch (Exception e) {
                System.out.println("ListascruceController|guardar:ID_REPORTE: " + e.getMessage());
            }
            tabla.put("APELLIDOS_RAZON_SOCIAL", apellidos.toUpperCase());
            tabla.put("NOMBRES_RAZON_COMERCIAL", nombres.toUpperCase());
            tabla.put("ALIAS", alias != null && !alias.isEmpty() ? alias.toUpperCase() : "");
            tabla.put("COMENTARIO", comentario.toUpperCase());
            tabla.put("TIPO_REPORTE", tipoRepSel != null ? tipoRepSel : "");
            tabla.put("ROS", ros != null ? ros : "");
            tabla.put("USUARIO_ACTUALIZACION", codUsu);
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
            tabla.put("FECHA_ACTUALIZACION", fechaActual);
            if (proceso.equals("INSERTAR")) {
                tabla.put("FECHA_INGRESO", fechaActual);
                try {
                    personasRepEJB.ingresarPersonaLista(tabla);
                    msg = "Se creó la persona en la lista exitosamente";
                    fcMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
                    limpiar("1");
                } catch (Exception e) {
                    System.out.println("ListascruceController|guardar:INSERTAR: " + e.getMessage());
                    msg = "Se presentó un error en la creacion: " + e.getMessage();
                    fcMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
                }
            } else if (proceso.equals("ACTUALIZAR")) {
                Timestamp fechaIngr = Timestamp.valueOf(fechaIngreso.replace("/", "-") + ":00.0");
                tabla.put("FECHA_INGRESO", fechaIngr);
                try {
                    personasRepEJB.actualizarDatosPersona(tabla);
                    msg = "Se actualizó la persona exitosamente";
                    fcMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
                    limpiar("1");
                } catch (Exception e) {
                    System.out.println("ListascruceController|guardar:ACTUALIZAR: " + e.getMessage());
                    msg = "Se presento un error al intentar actualizar: " + e.getMessage();
                    fcMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
                }
            } else if (proceso.equals("ELIMINAR")) {
                try {
                    personasRepEJB.eliminarPersonaLista(tabla);
                    msg = "Se eliminó la persona de la lista exitosamente";
                    fcMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
                    limpiar("1");
                } catch (Exception e) {
                    System.out.println("ListascruceController|guardar:ELIMINAR: " + e.getMessage());
                    msg = "Se presentó un error al intentar eliminar la persona de la lista: " + e.getMessage();
                    fcMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
                }
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);
    }

    public void setListMotivos(Collection listMotivos) {
        this.listMotivos = listMotivos;
    }

    public Collection getListMotivos() {
        return listMotivos;
    }

    public void setListTiposDoc(Collection listTiposDoc) {
        this.listTiposDoc = listTiposDoc;
    }

    public Collection getListTiposDoc() {
        return listTiposDoc;
    }

    public void setListTiposRep(Collection listTiposRep) {
        this.listTiposRep = listTiposRep;
    }

    public Collection getListTiposRep() {
        return listTiposRep;
    }

    public void setMotivoSel(String motivoSel) {
        this.motivoSel = motivoSel;
    }

    public String getMotivoSel() {
        return motivoSel;
    }

    public void setTipoDocSel(String tipoDocSel) {
        this.tipoDocSel = tipoDocSel;
    }

    public String getTipoDocSel() {
        return tipoDocSel;
    }

    public void setTipoRepSel(String tipoRepSel) {
        this.tipoRepSel = tipoRepSel;
    }

    public String getTipoRepSel() {
        return tipoRepSel;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNombres() {
        return nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setNumeroRep(String numeroRep) {
        this.numeroRep = numeroRep;
    }

    public String getNumeroRep() {
        return numeroRep;
    }

    public void setRos(String ros) {
        this.ros = ros;
    }

    public String getRos() {
        return ros;
    }

    public void setListasCruce(Collection listasCruce) {
        this.listasCruce = listasCruce;
    }

    public Collection getListasCruce() {
        return listasCruce;
    }

    public void setListaSel(Hashtable listaSel) {
        this.listaSel = listaSel;
    }

    public Hashtable getListaSel() {
        return listaSel;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled2(boolean disabled2) {
        this.disabled2 = disabled2;
    }

    public boolean isDisabled2() {
        return disabled2;
    }
}
