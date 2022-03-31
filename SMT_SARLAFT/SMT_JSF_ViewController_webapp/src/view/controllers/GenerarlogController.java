package view.controllers;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.annotation.Generated;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

import org.primefaces.event.SelectEvent;

import presentacion.FacadeEJB;

@ManagedBean(name = "GenerarlogController")
@SessionScoped
public class GenerarlogController {

    @EJB
    ConsultaTablaEJB consultaTablaEJB;
    @EJB
    FacadeEJB facadeEJB;
    FacesContext facContex;
    ExternalContext extContex;
    private HttpSession sesion;
    private String fechaProcAct;
    private FacesMessage facesMsg;
    private String msg;
    private String tipMsg;

    @PostConstruct
    void init() {
        msg = "";
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        this.buscarFechaProcAct();
    }

    private void buscarFechaProcAct() {
        try {
            Collection result = consultaTablaEJB.consultarTabla(0, 0, "CONTROL_ENTIDAD", "");
            fechaProcAct = "";
            SimpleDateFormat SDFormat = new SimpleDateFormat("yyyy/MM/dd");
            Iterator iter = result.iterator();
            while (iter.hasNext()) {
                Hashtable item = (Hashtable) iter.next();
                try {
                    String fechaBD = item.get("FECHA_PROCESO").toString().replace("-", "/");
                    Date dateBD = SDFormat.parse(fechaBD);
                    fechaProcAct = SDFormat.format(dateBD.getTime());
                } catch (Exception e) {
                    System.out.println("IniciodiaController|buscarFechaProcAct: No se pudo parsear la fecha de proceso actual");
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("IniciodiaController|buscarFechaProcAct: No se pudo consultar la fecha de proceso actual");
        }
    }

    public void generarArchivoLog() {
        String respuesta = "";
        msg = "";
        tipMsg = "";
        try {
            Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
            String usuario = (String) datosRegistro.get("CODIGO_USUARIO");
            extContex.getApplicationMap().put("bloqueo", "bloqueo");
            String entrada = "(" + usuario + ",?)";
            int[] parametros = { java.sql.Types.NUMERIC };
            respuesta = consultaTablaEJB.ejecutarProcedure("PR_GENERAR_LOG", entrada, parametros);
            respuesta = respuesta.replace('[', ' ').replace(']', ' ').trim();
            Collection cMensaje = new ArrayList();
            try {
                int iRespuesta = Integer.parseInt(respuesta);
                cMensaje = consultaTablaEJB.consultarTabla(0, 0, "MENSAJES", "WHERE CODIGO = '" + respuesta + "'");
                Iterator it = cMensaje.iterator();
                while (it.hasNext()) {
                    Hashtable item = (Hashtable) it.next();
                    msg = (String)item.get("DESCRIPCION");
                }
                tipMsg = "Ok: ";
            } catch (NumberFormatException eformat) {
                msg = respuesta.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ').replace('"', '\'');
                tipMsg = "Info: ";
            } catch (Exception ex) {
                msg =
                    "Error al buscar el mensaje: " + respuesta.replace('\n', ' ').replace('\t', ' ').replace('"', '\'');
                tipMsg = "Info: ";
            }
        } catch (SQLException e) {
            msg = "Error ejecutando procedimiento: " + e.getMessage();
            tipMsg = "Error: ";
        } catch (Exception e) {
            msg = e.getMessage();
            tipMsg = "Error: ";
        } finally {
            extContex.getApplicationMap().remove("bloqueo");
        }
        facesMsg = new FacesMessage("Resultado proceso: ", tipMsg + msg);
        facContex = FacesContext.getCurrentInstance();
        facContex.addMessage(null, facesMsg);
    }

    public void setFechaProcAct(String fechaProcActIn) {
        this.fechaProcAct = fechaProcActIn;
    }

    public String getFechaProcAct() {
        return this.fechaProcAct;
    }
}
