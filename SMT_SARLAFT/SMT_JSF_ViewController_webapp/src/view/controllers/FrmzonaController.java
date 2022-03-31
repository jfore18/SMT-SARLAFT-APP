package view.controllers;

import admin.util.Funciones;

import admin.zona.Zonas;
import admin.zona.ZonasEJB;
import admin.zona.ZonasPK;

import baseDatos.ConsultaTablaEJB;

import java.sql.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import javax.ejb.CreateException;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

@ManagedBean(name = "FrmzonaController")
@SessionScoped
public class FrmzonaController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private ZonasEJB zonasEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Collection zonas;
    private Collection regiones;
    private Hashtable zona;
    private Hashtable region;
    private FacesMessage fcMsg;
    private Funciones funciones;
    private String msg;
    private String fechCreaZona;
    private String codZona;
    private String nombreZona;
    private String nombrelZona;
    private String regionZona;
    private String usuCreaZona;
    private boolean activa;
    private String usuActZona;
    private String fechaActZona;
    private String consulta;
    private Date fecha;
    private boolean read;

    @PostConstruct
    public void init() {
        System.out.println("FrmzonaController|init: ");
        listarRegiones();
        read = false;
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        funciones = new Funciones();
    }

    public void listarRegiones() {
        Collection resultados = null;
        consulta = "SELECT * FROM V_REGION ORDER BY DESC_LARGA";
        try {
            resultados = consTablaEJB.consultarTabla(0, 0, null, consulta);
        } catch (Exception e) {
            System.out.println("FrmzonaController|buscarZonas: Error select V_REGION");
        }
        regiones = resultados;
        System.out.println("regiones: " + regiones);
        consulta = "";
    }

    public void buscarZonas() {
        consulta = " WHERE 0<1";
        if (codZona != null && !codZona.isEmpty()) {
            consulta += " AND (CODIGO_ZONA = '" + codZona + "')";
        }
        if (nombreZona != null && !nombreZona.isEmpty()) {
            nombreZona = nombreZona.toUpperCase();
            consulta += " AND (NOMBRE_CORTO LIKE '%" + nombreZona + "%')";
        }
        if (nombrelZona != null && !nombrelZona.isEmpty()) {
            nombrelZona = nombrelZona.toUpperCase();
            consulta += " AND (NOMBRE_LARGO LIKE '%" + nombrelZona + "%')";
        }
        if (regionZona != null && !regionZona.isEmpty()) {
            consulta += " AND (CODIGO_REGION_V = '" + regionZona + "')";
        }
        if (usuCreaZona != null && !usuCreaZona.isEmpty()) {
            consulta += " AND (USUARIO_CREACION = '" + usuCreaZona + "')";
        }
        if (fechCreaZona != null && !fechCreaZona.isEmpty()) {
            fechCreaZona = funciones.str_AAAAMMDD_str_DDMMAAA(fechCreaZona);
            consulta += " AND FECHA_CREACION LIKE TO_DATE('" + fechCreaZona + "','DD/MM/YYYY')";
        }
        if (fechaActZona != null && !fechaActZona.isEmpty()) {
        	fechaActZona = funciones.str_AAAAMMDD_str_DDMMAAA(fechaActZona);
            consulta += " AND FECHA_ACTUALIZACION LIKE TO_DATE('" + fechaActZona + "','DD/MM/YYYY')";
        }
        if (usuActZona != null && !usuActZona.isEmpty()) {
            consulta += " AND (usuActZona = '" + usuActZona + "')";
        }
        if (activa) {
            consulta += " AND (activa = '1')";
        }
        
        consulta += " ORDER BY NOMBRE_REGION";
        System.out.println("consulta: " + consulta);
        Collection resultados = null;
        try {
            resultados = consTablaEJB.consultarTabla(0, 0, "V_ZONAS", consulta);
            Iterator iter = resultados.iterator();
            while (iter.hasNext()) {
                Hashtable zonaTemp = (Hashtable) iter.next();
                String activa = ((String) zonaTemp.get("ACTIVA")).equals("1") ? "SI" : "NO";
                zonaTemp.replace("ACTIVA", activa);
                
            }
        } catch (Exception e) {
            System.out.println("FrmzonaController|buscarZonas: Error select V_ZONAS");
        }
        zonas = resultados;
        System.out.println("zonas: " + zonas);
    }

    public void actualizar() {
        System.out.println("zona: " + zona);
        if (zona != null) {
            codZona = (String) zona.get("CODIGO_ZONA");
            nombreZona = (String) zona.get("NOMBRE_CORTO");
            nombrelZona = (String) zona.get("NOMBRE_LARGO");
            regionZona = (String) zona.get("CODIGO_REGION_V");
            usuCreaZona = (String) zona.get("USUARIO_CREACION");
            fechCreaZona = ((String) zona.get("FECHA_CREACION")).substring(0, 10).replace("-", "/");
            usuActZona = (String) zona.get("USUARIO_ACTUALIZACION");
            usuActZona = usuActZona.equals("null") ? "": usuActZona;
            fechaActZona =((String) zona.get("FECHA_ACTUALIZACION"));
            fechaActZona = fechaActZona.equals("null") ? "" :((String) zona.get("FECHA_ACTUALIZACION")).substring(0, 10).replace("-", "/");;
            
           // fechaActZona =((String) zona.get("FECHA_ACTUALIZACION")).equals("null") ? " ":((String) zona.get("FECHA_ACTUALIZACION")).substring(0, 10).replace("-", "/");
            activa = ((String) zona.get("ACTIVA")).equals("SI");
            read = true;
        }
    }

    public void limpiar() {
        zona = null;
        zonas = null;
        codZona = "";
        nombreZona = "";
        nombrelZona = "";
        regionZona = "";
        usuCreaZona = "";
        fechCreaZona = "";
        usuActZona = "";
        fechaActZona="";
        activa=false;
        read = false;
    }

    public void guardar() {
        String codigoRegion = regionZona;
        String descripcionCorta = nombreZona.toUpperCase();
        String descripcionLarga = nombrelZona.toUpperCase();
        facContex = FacesContext.getCurrentInstance();
        Hashtable datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        if (codZona != null && !codZona.isEmpty()) {
            int codigoZona = Integer.valueOf(codZona);
            if (descripcionCorta != null && !descripcionCorta.isEmpty()) {
                if (descripcionLarga != null && !descripcionLarga.isEmpty()) {
                    if (codigoRegion != null && !codigoRegion.isEmpty()) {
                        if (read) {
                            //actualizar
                            ZonasPK pkZona = new ZonasPK(codigoZona, codigoRegion);
                            Zonas zonaLocal;
                            try {
                                zonaLocal = zonasEJB.findByPrimaryKey(pkZona);
                                zonaLocal.setNombreCorto(descripcionCorta);
                                zonaLocal.setNombreLargo(descripcionLarga);
                                zonaLocal.setzActiva(activa? new Long(1):new Long(0));
                                zonaLocal.setFechaActualizacion(new Timestamp(System.currentTimeMillis()));
                                zonaLocal.setUsuarioActualizacion(Long.parseLong((String) datosRegistro.get("CODIGO_USUARIO")));
                                zonasEJB.actualizarZonas(zonaLocal);
                                msg = "Actualización exitosa ";
                                fcMsg = new FacesMessage("Ok: ", msg);
                                zona = null;
                                zonas = null;
                                limpiar();
                            } catch (Exception e) {
                                msg = e.getMessage();
                                fcMsg = new FacesMessage("Error actualizar zona: ", msg);
                                System.out.println("FrmzonaController|guardar: Error Update Zonas");
                            }
                        } else {
                            //nueva zona
                           
                            Long usuarioActual = Long.parseLong((String) datosRegistro.get("CODIGO_USUARIO"));
                            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
                            Hashtable pDatos = new Hashtable();
                            pDatos.put("CODIGO", codigoZona);
                            pDatos.put("CODIGO_REGION_V", codigoRegion);
                            pDatos.put("NOMBRE_CORTO", descripcionCorta);
                            pDatos.put("NOMBRE_LARGO", descripcionLarga);
                            pDatos.put("USUARIO_CREACION", usuarioActual);
                            pDatos.put("FECHA_CREACION", fechaActual);
                            pDatos.put("ACTIVA", activa? new Long(1):new Long(0));
                            ZonasPK pkZona = new ZonasPK(codigoZona, codigoRegion);
                            Zonas zonaTemp;
                            try {
                                zonaTemp = zonasEJB.findByPrimaryKey(pkZona);
                                if (zonaTemp == null) {
                                    zonasEJB.create(pDatos);
                                    msg = "Se creó la zona exitosamente";
                                    fcMsg = new FacesMessage("Ok: ", msg);
                                    zona = null;
                                    zonas = null;
                                    read = true;
                                    limpiar();
                                } else {
                                    msg = "La zona ya existe";
                                    fcMsg = new FacesMessage("Error: ", msg);
                                }
                            } catch (Exception e) {
                                msg = e.getMessage();
                                fcMsg = new FacesMessage("Error creando zona: ", msg);
                                e.printStackTrace();
                                System.out.println("FrmzonaController|guardar: Error Create Zonas");
                            }
                        }
                        
                    } else {
                        msg = "Seleccione la región";
                        fcMsg = new FacesMessage("Alerta: ", msg);
                        PrimeFaces.current().focus("frmZona:regionZona");
                    }
                } else {
                    msg = "Ingrese nombre largo de la zona";
                    fcMsg = new FacesMessage("Alerta: ", msg);
                    PrimeFaces.current().focus("frmZona:nombrelZona");
                }
            } else {
                msg = "Ingrese nombre corto de la zona";
                fcMsg = new FacesMessage("Alerta: ", msg);
                PrimeFaces.current().focus("frmZona:nombreZona");
            }
        } else {
            msg = "Ingrese código de la zona";
            fcMsg = new FacesMessage("Alerta: ", msg);
            PrimeFaces.current().focus("frmZona:codZona");
        }
        facContex.addMessage(null, fcMsg);
    }

    public void setZonas(Collection zonas) {
        this.zonas = zonas;
    }

    public Collection getZonas() {
        return zonas;
    }

    public void setZona(Hashtable zona) {
        this.zona = zona;
    }

    public Hashtable getZona() {
        return zona;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setFechCreaZona(String fechCreaZona) {
        this.fechCreaZona = fechCreaZona;
    }

    public String getFechCreaZona() {
        return fechCreaZona;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setRegiones(Collection regiones) {
        this.regiones = regiones;
    }

    public Collection getRegiones() {
        return regiones;
    }

    public void setRegion(Hashtable region) {
        this.region = region;
    }

    public Hashtable getRegion() {
        return region;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombrelZona(String nombrelZona) {
        this.nombrelZona = nombrelZona;
    }

    public String getNombrelZona() {
        return nombrelZona;
    }

    public void setRegionZona(String regionZona) {
        this.regionZona = regionZona;
    }

    public String getRegionZona() {
        return regionZona;
    }

    public void setUsuCreaZona(String usuCreaZona) {
        this.usuCreaZona = usuCreaZona;
    }

    public String getUsuCreaZona() {
        return usuCreaZona;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isRead() {
        return read;
    }

    private String dateToString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        if (fecha != null) {
            return format.format(fecha);
        }
        return null;
    }



	public String getusuActZona() {
		return usuActZona;
	}

	public void setusuActZona(String usuActZona) {
		this.usuActZona = usuActZona;
	}

	public String getfechaActZona() {
		return fechaActZona;
	}

	public void setfechaActZona(String fechaActZona) {
		this.fechaActZona = fechaActZona;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}
}
