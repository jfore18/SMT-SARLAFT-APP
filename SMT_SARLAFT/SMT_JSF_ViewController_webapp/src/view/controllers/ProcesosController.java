package view.controllers;

import baseDatos.ConsultaTablaEJB;

import java.io.Serializable;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Generated;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import javax.faces.bean.ViewScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.inject.Named;

import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "ProcesosController")
@RequestScoped
public class ProcesosController {
    @EJB
    private ConsultaTablaEJB consultaTablaEJB;
    @ManagedProperty("#{ContenidoController}")
    private ContenidoController contenidoCtr;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private String msg;
    private String tipMsg;
    private String proceso;
    private FacesMessage facesMsg;

    @PostConstruct
    public void init() {
        System.out.println("GeneracrmController|init: ");
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
    }



    public void procesos(String tipoProceso) {
        Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        String usuario = (String) datosRegistro.get("CODIGO_USUARIO");
        String entrada;
        String respuesta = "";
        msg = "";
        try {
            extContex.getApplicationMap().put("bloqueo", "bloqueo");
            if (tipoProceso.equals("GENERA")) {
                entrada = "(" + usuario + ")";
                respuesta =
                    consultaTablaEJB.ejecutarProcedure("PK_INTERFAZ_CRM.P_CREAR_ARCHIVO_CLIENTES", entrada, null);
            }
            if (tipoProceso.equals("OBTIENE")) {
                entrada = "('ICL','4'," + usuario + ",?)";
                int[] parametros = { java.sql.Types.VARCHAR };
                respuesta =
                    consultaTablaEJB.ejecutarProcedure("PK_INTERFAZ_CRM.P_OBTENER_DATOS_CLIENTES", entrada, parametros);
            }
            if (tipoProceso.equals("DEPURAR")) {
                entrada = "(?," + usuario + ")";
                int[] parametros = { java.sql.Types.VARCHAR };
                respuesta = consultaTablaEJB.ejecutarProcedure("PK_DEPURACION.DEPURAR", entrada, parametros);
            }
            respuesta = respuesta.replace('[', ' ').replace(']', ' ');
            if (respuesta.trim().length() == 0) {
                respuesta = "Se ejecutó el proceso exitosamente";
            }
            msg = respuesta;
            tipMsg = "Ok: ";
            System.out.println("ProcesosController|procesos: "+respuesta);
        } catch (SQLException e) {
            msg = "Error ejecutando procedimiento: " + e.getMessage();
            tipMsg = "Error: ";
            System.out.println("ProcesosController|procesos: "+e.getMessage());
        } catch (Exception e) {
            msg = e.getMessage();
            tipMsg = "Error: ";
            System.out.println("ProcesosController|procesos: "+e.getMessage());
        } finally {
            extContex.getApplicationMap().remove("bloqueo");
        }
        facesMsg = new FacesMessage("Resultado proceso: ", tipMsg + msg);
        facContex = FacesContext.getCurrentInstance();
        facContex.addMessage(null, facesMsg);

    }

    public void setMsg(String msgIn) {
        msg = msgIn;
    }

    public String getMsg() {
        return msg;
    }

    public void setContenidoCtr(ContenidoController contenidoCtr) {
        this.contenidoCtr = contenidoCtr;
    }

    public ContenidoController getContenidoCtr() {
        return contenidoCtr;
    }
}
