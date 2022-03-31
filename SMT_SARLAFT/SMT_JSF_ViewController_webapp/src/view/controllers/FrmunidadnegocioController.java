package view.controllers;

import admin.UnidadesNegocio;

import admin.UnidadesNegocioEJB;

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

import javax.ejb.FinderException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

@ManagedBean(name = "FrmunidadnegocioController")
@SessionScoped
public class FrmunidadnegocioController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private UnidadesNegocioEJB unEJB;
    private FacesContext facContex;
    private ExternalContext extContex;
    private HttpSession sesion;
    private Collection udsn;
    private Collection zonas;
    private Hashtable un;
    private FacesMessage fcMsg;
    private String msg;
    private String codUN;
    private String nombreUN;
    private String regionUN;
    private String zonaUN;
    private String consulta;
    private boolean read;
    private boolean ceo;
    private boolean plzCrit;
    private boolean activa;
    private boolean megabco;

    @PostConstruct
    public void init() {
        System.out.println("FrmunidadnegocioController|init: ");
        read = false;
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
    }

    public void buscarZonas() {
        System.out.println("regionUN: " + regionUN);
        try {
            String sql = "SELECT * FROM V_ZONAS WHERE CODIGO_REGION_V = '" + regionUN + "'";
            zonas = consTablaEJB.consultarTabla(0, 0, null, sql);
        } catch (Exception e) {
            System.out.println("FrmunidadnegocioController|buscarZonas: Error consultando zonas");
        }
        System.out.println("zonas: " + zonas);
    }

    public void buscarUN() {
        consulta = " WHERE 0<1";
        if (codUN != null && !codUN.isEmpty()) {
            consulta += " AND (CODIGO_UNIDAD_NEGOCIO LIKE '%" + codUN + "%')";
        }
        if (nombreUN != null && !nombreUN.isEmpty()) {
            nombreUN = nombreUN.toUpperCase();
            consulta += " AND (NOMBRE_UNIDAD_NEGOCIO LIKE '%" + nombreUN + "%')";
        }
        if (regionUN != null && !regionUN.isEmpty()) {
            consulta += " AND (CODIGO_REGION_V = '" + regionUN + "')";
        }
        if (zonaUN != null && !zonaUN.isEmpty()) {
            consulta += " AND (CODIGO_ZONA = '" + zonaUN + "')";
        }
        if (ceo) {
            consulta += " AND (CEO = '1')";
        }
        if (plzCrit) {
            consulta += " AND (PLAZA_CRITICA = '1')";
        }
        if (activa) {
            consulta += " AND (ACTIVA = '1')";
        }
        if (megabco) {
            consulta += " AND (IS_MEGABANCO = '1')";
        }
        consulta += " ORDER BY CODIGO_UNIDAD_NEGOCIO";
        System.out.println("consulta: " + consulta);
        Collection resultados = null;
        try {
            resultados = consTablaEJB.consultarTabla(0, 0, "V_UN", consulta);
            Iterator iter = resultados.iterator();
            while (iter.hasNext()) {
                Hashtable unRegTemp = (Hashtable) iter.next();
                String activa = ((String) unRegTemp.get("ACTIVA")).equals("1") ? "SI" : "NO";
                unRegTemp.replace("ACTIVA", activa);
                String mb = ((String) unRegTemp.get("IS_MEGABANCO")).equals("1") ? "SI" : "NO";
                unRegTemp.replace("IS_MEGABANCO", mb);
                String pc = ((String) unRegTemp.get("PLAZA_CRITICA")).equals("1") ? "SI" : "NO";
                unRegTemp.replace("PLAZA_CRITICA", pc);
            }
        } catch (Exception e) {
            System.out.println("FrmunidadnegocioController|buscarUN: Error select V_UN");
        }
        udsn = resultados;
        System.out.println("udsn: " + udsn);
    }

    public void actualizar() {
        System.out.println("un: " + un);
        if (un != null) {
            codUN = (String) un.get("CODIGO_UNIDAD_NEGOCIO");
            nombreUN = (String) un.get("NOMBRE_UNIDAD_NEGOCIO");
            regionUN = (String) un.get("CODIGO_REGION_V");
            buscarZonas();
            zonaUN = (String) un.get("CODIGO_ZONA");
            ceo = ((String) un.get("CEO")).equals("1");
            plzCrit = ((String) un.get("PLAZA_CRITICA")).equals("SI");
            activa = ((String) un.get("ACTIVA")).equals("SI");
            megabco = ((String) un.get("IS_MEGABANCO")).equals("SI");
            read = true;
        }
    }

    public void limpiar() {
        un = null;
        udsn = null;
        codUN = "";
        nombreUN = "";
        regionUN = "";
        zonaUN = "";
        ceo = false;
        plzCrit = false;
        activa = false;
        megabco = false;
        read = false;
    }

    public void guardar() {
        facContex = FacesContext.getCurrentInstance();
        String nom = nombreUN.toUpperCase();
        String reg = regionUN;
        String zon = zonaUN;
        Integer ceoi = ceo ? new Integer(1) : new Integer(0);
        Integer act = activa ? new Integer(1) : new Integer(0);
        Integer pc = plzCrit ? new Integer(1) : new Integer(0);
        Integer mb = megabco ? new Integer(1) : new Integer(0);
        Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
        if (codUN != null && !codUN.isEmpty()) {
            int cod = Integer.valueOf(codUN);
            if (nom != null && !nom.isEmpty()) {
                if (reg != null && !reg.isEmpty()) {
                    if (zon != null && !zon.isEmpty()) {
                        Hashtable usuario = (Hashtable) sesion.getAttribute("datosRegistro");
                        Long codUsu = Long.parseLong((String) usuario.get("CODIGO_USUARIO"));
                        int zoni = Integer.valueOf(zon);
                        UnidadesNegocio unTemp = null;
                        if (read) {
                            //actualizar
                            try {
                                unTemp = unEJB.findByPrimaryKey(cod);
                                unTemp.setDescripcion(nom);
                                unTemp.setFechaActualizacion(fechaActual);
                                unTemp.setUsuarioActualizacion(codUsu);
                                unTemp.setActiva(act);
                                unTemp.setIsMegabanco(mb);
                                unTemp.setCeo(ceoi);
                                unTemp.setCodigoZona(zoni);
                                unTemp.setCodigoRegionV(reg);
                                unTemp.setPlazaCritica(pc);
                                unEJB.actualizarUnidadesNegocio(unTemp);
                                msg = "Actualización exitosa ";
                                fcMsg = new FacesMessage("Ok: ", msg);
                                un = null;
                                udsn = null;
                            } catch (Exception e) {
                                msg = e.getMessage();
                                fcMsg = new FacesMessage("Error actualizar unidad negocio: ", msg);
                                System.out.println("FrmunidadnegocioController|guardar: Error Update unid negocio");
                            }
                        } else {
                            //nueva un
                            Hashtable pDatos = new Hashtable();
                            pDatos.put("CODIGO", cod);
                            pDatos.put("CODIGO_ZONA", zoni);
                            pDatos.put("CODIGO_REGION_V", reg);
                            pDatos.put("DESCRIPCION", nom);
                            pDatos.put("CEO", ceoi);
                            pDatos.put("PLAZA_CRITICA", pc);
                            pDatos.put("ACTIVA", act);
                            pDatos.put("IS_MEGABANCO", mb);
                            pDatos.put("USUARIO_ACTUALIZACION", codUsu);
                            pDatos.put("FECHA_ACTUALIZACION", fechaActual);
                            try {
                                unTemp = unEJB.findByPrimaryKey(cod);
                                if (unTemp == null) {
                                    unEJB.create(pDatos);
                                    msg = "Se creó la unidad de negocio exitosamente";
                                    fcMsg = new FacesMessage("Ok: ", msg);
                                    un = null;
                                    udsn = null;
                                    read = true;
                                } else {
                                    msg = "La unidad de negocio ya existe";
                                    fcMsg = new FacesMessage("Aviso: ", msg);
                                }
                            } catch (Exception e) {
                                msg = e.getMessage();
                                fcMsg = new FacesMessage("Error creando unidad negocio: ", msg);
                                e.printStackTrace();
                                System.out.println("FrmzonaController|guardar: Error Create unid negocio");
                            }
                        }
                        limpiar();
                    } else {
                        msg = "Seleccione la zona";
                        fcMsg = new FacesMessage("Alerta: ", msg);
                        PrimeFaces.current().focus("frmUN:zonaUN");
                    }
                } else {
                    msg = "Seleccione la región";
                    fcMsg = new FacesMessage("Alerta: ", msg);
                    PrimeFaces.current().focus("frmUN:regionUN");
                }
            } else {
                msg = "Ingrese nombre de la unidad de negocio";
                fcMsg = new FacesMessage("Alerta: ", msg);
                PrimeFaces.current().focus("frmUN:nombreUN");
            }
        } else {
            msg = "Ingrese código de la unidad de negocio";
            fcMsg = new FacesMessage("Alerta: ", msg);
            PrimeFaces.current().focus("frmUN:codUN");
        }
        facContex.addMessage(null, fcMsg);
    }


    public void setUdsn(Collection udsn) {
        this.udsn = udsn;
    }

    public Collection getUdsn() {
        return udsn;
    }

    public void setZonas(Collection zonas) {
        this.zonas = zonas;
    }

    public Collection getZonas() {
        return zonas;
    }

    public void setUn(Hashtable un) {
        this.un = un;
    }

    public Hashtable getUn() {
        return un;
    }

    public void setCodUN(String codUN) {
        this.codUN = codUN;
    }

    public String getCodUN() {
        return codUN;
    }

    public void setNombreUN(String nombreUN) {
        this.nombreUN = nombreUN;
    }

    public String getNombreUN() {
        return nombreUN;
    }

    public void setRegionUN(String regionUN) {
        this.regionUN = regionUN;
    }

    public String getRegionUN() {
        return regionUN;
    }

    public void setZonaUN(String zonaUN) {
        this.zonaUN = zonaUN;
    }

    public String getZonaUN() {
        return zonaUN;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isRead() {
        return read;
    }

    public void setCeo(boolean ceo) {
        this.ceo = ceo;
    }

    public boolean isCeo() {
        return ceo;
    }

    public void setPlzCrit(boolean plzCrit) {
        this.plzCrit = plzCrit;
    }

    public boolean isPlzCrit() {
        return plzCrit;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setMegabco(boolean megabco) {
        this.megabco = megabco;
    }

    public boolean isMegabco() {
        return megabco;
    }
}
