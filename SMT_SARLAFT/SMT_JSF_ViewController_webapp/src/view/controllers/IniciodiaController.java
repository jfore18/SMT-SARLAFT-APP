package view.controllers;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

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

@ManagedBean(name="IniciodiaController")
@SessionScoped
public class IniciodiaController {

    @EJB
    ConsultaTablaEJB consultaTablaEJB;
    @EJB
    FacadeEJB facadeEJB;
    FacesContext facContex;
    ExternalContext extContex;
    private HttpSession sesion;
    private String fechaProcAct;
    private Date fechaNue;
    private String fechaProcNue;
    private FacesMessage facesMsg;
    private String msg;
    
    @PostConstruct
    void init() {
        System.out.println("IniciodiaController|init()");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        fechaProcNue = format.format(new Date().getTime());
        msg = "";
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        this.buscarFechaProcAct();
    }

    public void setFechaProcAct(String fechaProcActIn) {
        this.fechaProcAct = fechaProcActIn;
    }

    public String getFechaProcAct() {
        return this.fechaProcAct;
    }

    public void setFechaProcNue(String fechaProcNueIn) {
        this.fechaProcNue = fechaProcNueIn;
    }

    public String getFechaProcNue() {
        return this.fechaProcNue;
    }

    public void setFechaNue(Date fechaNueIn) {
        this.fechaNue = fechaNueIn;
    }

    public Date getFechaNue() {
        return this.fechaNue;
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

    public void cambiarFechaProceso(){
        Hashtable datosRegistro = (Hashtable)sesion.getAttribute("datosRegistro");
        String usuario = (String)datosRegistro.get("CODIGO_USUARIO");
        if(usuario != null){
            try {
                fechaProcNue = this.dateToString();
                facadeEJB.inicioDia(fechaProcNue, usuario);
                fechaProcNue = "";
                this.buscarFechaProcAct();
                msg = "La fecha fue cambiada con exito, la nueva fecha de proceso es: "+fechaProcAct;
                facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", msg);
                facContex.getCurrentInstance().addMessage(null, facesMsg);
                //PrimeFaces.current().ajax().update(":frmInicioDia:txtFechProcAct");
                System.out.println("IniciodiaController|cambiarFechaProceso: exito");
            } catch (Exception e) {
                msg = "No fue posible cambiar la fecha de proceso: "+e.getMessage();
                facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", msg);
                facContex.getCurrentInstance().addMessage(null, facesMsg);
                System.out.println("IniciodiaController|cambiarFechaProceso: No se pudo cambiar la fecha");
            }
        }else{
                
        }
    }
    
    public void onDateSelect(){
        fechaProcNue = dateToString();
    }
    private String dateToString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        if (fechaNue != null) {
            return format.format(fechaNue);
        }
        return format.format(new Date().getTime());
    }
}
