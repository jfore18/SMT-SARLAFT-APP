package view.controllers;

import admin.PreguntasEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
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

import org.primefaces.PrimeFaces;

@ManagedBean(name = "FrmpreguntasController")
@SessionScoped
public class FrmpreguntasController {

    @EJB
    private PreguntasEJB preguntasEJB;
    @EJB
    ConsultaTablaEJB consultaTablaEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Funciones funciones;
    private Collection listTiposPreguntas;
    private Collection listPreguntas;
    private Hashtable datosRegistro;
    private Hashtable preguntaSel;
    private FacesMessage fcMsg;
    private String usuario;
    private String tipoPregunta;
    private String texto;
    private String fechaAct;
    private String usuarioAct;
    private String condicion;
    private String proceso;
    private String msg;
    private String carDesde;
    private String carHasta;
    private String menor;
    private String mayor;
    private String igual;
    private String displayDesde;
    private String displayHasta;
    private Date desde;
    private Date hasta;
    private Date desde2;
    private Date hasta2;
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

    public void iniciarElementos() {
        listTiposPreguntas = new ArrayList();
        try {
            Hashtable datos = preguntasEJB.consultarTiposPregunta();
            Enumeration llaves = datos.keys();
            while (llaves.hasMoreElements()) {
                String llave = (String) llaves.nextElement();
                String valor = (String) datos.get(llave);
                Hashtable dat = new Hashtable();
                dat.put("VALOR", llave);
                dat.put("LABEL", valor);
                listTiposPreguntas.add(dat);
            }
        } catch (SQLException e) {
            System.out.println("FrmpreguntasController|iniciarElementos: " + e.getMessage());
        }
        igual = "=";
        menor = "<";
        mayor = ">";
        carDesde = "=";
        carHasta = "=";
        limpiar();
    }

    public void limpiar() {
        tipoPregunta = "";
        texto = "";
        desde = null;
        hasta = null;
        fechaAct = "";
        usuarioAct = "";
        listPreguntas = null;
        proceso = "INSERTAR";
        disabled = false;
        displayDesde = "none";
        displayHasta = "none";
    }

    public void buscarPreguntas() {
        condicion = " WHERE 0<1";
        if (tipoPregunta != null && !tipoPregunta.isEmpty()) {
            condicion += " AND TIPO_PREGUNTA = '" + tipoPregunta + "'";
        }
        if (texto != null && !texto.isEmpty()) {
            condicion += " AND UPPER(PREGUNTA) LIKE '%" + texto.toUpperCase() + "%'";
        }
        if (desde != null) {
            String fechaDesde = funciones.dateToString(desde);
            if (carDesde.equals("=") || carDesde.equals("<") || carDesde.equals(">")) {
                condicion += " AND VIGENTE_DESDE " + carDesde + " '" + fechaDesde + "'";
            } else {
                String fechaDesde2 = funciones.dateToString(desde2);
                condicion += " AND VIGENTE_DESDE > '" + fechaDesde + "' AND VIGENTE_DESDE < '" + fechaDesde2 + "'";
            }
        }
        if (hasta != null) {
            String fechaHasta = funciones.dateToString(hasta);
            if (carHasta.equals("=") || carHasta.equals("<") || carHasta.equals(">")) {
                condicion += " AND VIGENTE_HASTA " + carHasta + " '" + fechaHasta + "'";
            } else {
                String fechaHasta2 = funciones.dateToString(hasta2);
                condicion += " AND VIGENTE_HASTA > '" + fechaHasta + "' AND VIGENTE_HASTA < '" + fechaHasta2 + "'";
            }
        }
        try {
            listPreguntas = consultaTablaEJB.consultarTabla(0, 0, "V_PREGUNTAS", condicion);
            Iterator it = listPreguntas.iterator();
            while (it.hasNext()) {
                Hashtable ht = (Hashtable) it.next();
                ht = funciones.quitarNull(ht);
                ht.replace("VIGENTE_DESDE", funciones.formatoFecha(ht.get("VIGENTE_DESDE")));
                ht.replace("VIGENTE_HASTA", funciones.formatoFecha(ht.get("VIGENTE_HASTA")));
                ht.replace("FECHA_ACTUALIZACION", funciones.formatoFechaHora(ht.get("FECHA_ACTUALIZACION")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FrmpreguntasController|buscarPreguntas: " + e.getMessage());
        }
        System.out.println("listPreguntas: " + listPreguntas);
    }


    public void guardar() {
        continuar = true;
        validar();
        if (continuar) {
            Hashtable datos = new Hashtable();
            datos.put("DESCRIPCION", texto.toUpperCase());
            String vigDes = funciones.dateToString(desde).replace("/", "-") + " 00:00:00.0";
            datos.put("VIGENTE_DESDE", Timestamp.valueOf(vigDes));
            String vigHas = funciones.dateToString(hasta).replace("/", "-") + " 00:00:00.0";
            datos.put("VIGENTE_HASTA", Timestamp.valueOf(vigHas));
            datos.put("CODIGO_PERFIL_V", "*");
            datos.put("TIPO_PREGUNTA_V", tipoPregunta);
            Long usuarioActual = new Long(usuario);
            datos.put("USUARIO_ACTUALIZACION", usuarioActual);
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
            datos.put("FECHA_ACTUALIZACION", fechaActual);
            if (proceso.equals("INSERTAR")) {
                try {
                    preguntasEJB.adicionarPregunta(datos);
                    msg = "Se creo la pregunta exitosamente";
                    limpiar();
                } catch (SQLException e) {
                    System.out.println("FrmpreguntasController|guardar|insertar " + e.getMessage());
                }
            }
            if (proceso.equals("ACTUALIZAR")) {
                String idPregunta = preguntaSel.get("ID_PREGUNTA").toString();
                datos.put("ID", new Integer(idPregunta));
                try {
                    preguntasEJB.actualizarPregunta(datos);
                    msg = "Se actualizó la pregunta exitosamente";
                    limpiar();
                } catch (SQLException e) {
                    System.out.println("FrmpreguntasController|guardar|actualizar " + e.getMessage());
                }
            }
        }
        fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);
    }

    public void actualizar() {
        if (preguntaSel != null) {
            tipoPregunta = preguntaSel.get("TIPO_PREGUNTA").toString();
            texto = preguntaSel.get("PREGUNTA").toString();
            String desd = preguntaSel.get("VIGENTE_DESDE").toString();
            String hast = preguntaSel.get("VIGENTE_HASTA").toString();
            desde = (Date) funciones.stringToDate(desd);
            hasta = (Date) funciones.stringToDate(hast);
            fechaAct = preguntaSel.get("FECHA_ACTUALIZACION").toString();
            usuarioAct = preguntaSel.get("USUARIO_ACTUALIZACION").toString();
        }
        proceso = "ACTUALIZAR";
        disabled = true;
    }

    private void validar() {
        continuar = true;
        if (tipoPregunta == null) {
            msg = "Debe seleccionar un tipo de pregunta";
            continuar = false;
        }
        if (continuar && texto.isEmpty()) {
            msg = "Debe ingresar el texto de la pregunta";
            continuar = false;
        }
        if (continuar && desde == null) {
            msg = "Debe ingresar la fecha vigente desde";
            continuar = false;
        } else if (continuar && carDesde.equals("#")) {
            if (desde2 == null) {
                msg = "Debe ingresar la segunda fecha vigente desde";
                continuar = false;
            }
        }
        if (continuar && hasta == null) {
            msg = "Debe ingresar la fecha vigente hasta";
            continuar = false;
        } else if (continuar && carHasta.equals("#")) {
            if (hasta2 == null) {
                msg = "Debe ingresar la segunda fecha vigente hasta";
                continuar = false;
            }
        }
    }

    public void asiganrCaracter(String campo, String caracter) {
        if (campo.equals("Desde")) {
            this.carDesde = caracter;
            if (this.carDesde.equals("#")) {
                displayDesde = "block";
            } else {
                displayDesde = "none";
            }
        }
        if (campo.equals("Hasta")) {
            this.carHasta = caracter;
            if (this.carHasta.equals("#")) {
                displayHasta = "block";
            } else {
                displayHasta = "none";
            }
        }
        PrimeFaces.current().ajax().update("frmPreguntas");
    }

    public void setListTiposPreguntas(Collection listTiposPreguntas) {
        this.listTiposPreguntas = listTiposPreguntas;
    }

    public Collection getListTiposPreguntas() {
        return listTiposPreguntas;
    }

    public void setTipoPregunta(String tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getDesde() {
        return desde;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setFechaAct(String fechaAct) {
        this.fechaAct = fechaAct;
    }

    public String getFechaAct() {
        return fechaAct;
    }

    public void setUsuarioAct(String usuarioAct) {
        this.usuarioAct = usuarioAct;
    }

    public String getUsuarioAct() {
        return usuarioAct;
    }

    public void setListPreguntas(Collection listPreguntas) {
        this.listPreguntas = listPreguntas;
    }

    public Collection getListPreguntas() {
        return listPreguntas;
    }

    public void setPreguntaSel(Hashtable preguntaSel) {
        this.preguntaSel = preguntaSel;
    }

    public Hashtable getPreguntaSel() {
        return preguntaSel;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getMenor() {
        return menor;
    }

    public String getMayor() {
        return mayor;
    }

    public String getIgual() {
        return igual;
    }

    public void setDesde2(Date desde2) {
        this.desde2 = desde2;
    }

    public Date getDesde2() {
        return desde2;
    }

    public void setHasta2(Date hasta2) {
        this.hasta2 = hasta2;
    }

    public Date getHasta2() {
        return hasta2;
    }

    public void setDisplayDesde(String displayDesde) {
        this.displayDesde = displayDesde;
    }

    public String getDisplayDesde() {
        return displayDesde;
    }

    public void setDisplayHasta(String displayHasta) {
        this.displayHasta = displayHasta;
    }

    public String getDisplayHasta() {
        return displayHasta;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setCarDesde(String carDesde) {
        this.carDesde = carDesde;
    }

    public String getCarDesde() {
        return carDesde;
    }

    public void setCarHasta(String carHasta) {
        this.carHasta = carHasta;
    }

    public String getCarHasta() {
        return carHasta;
    }
}
