package view.controllers;

import admin.ListaValoresEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Collection;
import java.util.Date;
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

@ManagedBean(name = "FrmlistavaloresController")
@SessionScoped
public class FrmlistavaloresController {

    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @EJB
    private ListaValoresEJB listaValoresEJB;
    private Funciones funciones;
    private FacesContext facContex;
    private ExternalContext extContex;
    private FacesMessage fcMsg;
    private HttpSession sesion;
    private Collection listValores;
    private Collection listas;
    private Hashtable ob;
    private Hashtable listValoresSel;
    private Hashtable datosRegistro;
    private Date fecha;
    private String msg;
    private String condicion;
    private String proceso;
    private String lista;
    private boolean continuar;

    @PostConstruct
    public void init() {
        facContex = FacesContext.getCurrentInstance();
        extContex = facContex.getExternalContext();
        sesion = (HttpSession) extContex.getSession(false);
        funciones = new Funciones();
        datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
        iniciarElementos();
    }

    private void iniciarElementos() {
        ob = new Hashtable();
        ob.put("DESCRIPCION_TIPO_DATO", "");
        ob.put("CODIGO", "");
        ob.put("NOMBRE_CORTO", "");
        ob.put("NOMBRE_LARGO", "");
        ob.put("APLICA_GERENTE", false);
        ob.put("ACTIVO", false);
        ob.put("FECHA_ACTUALIZACION", "");
        ob.put("USUARIO_ACTUALIZACION", "");
        proceso = "INSERTAR";
        llenarListas();
        lista = "";
    }

    private void llenarListas() {
        String cons =
            "SELECT TIPO_DATO,DESCRIPCION_TIPO_DATO FROM V_LISTA_VALORES_ADMIN WHERE DESCRIPCION_TIPO_DATO IS NOT NULL GROUP BY TIPO_DATO,DESCRIPCION_TIPO_DATO ORDER BY 1 ASC";
        try {
            listas = consTablaEJB.consultarTabla(0, 0, null, cons);
        } catch (Exception e) {
            System.out.println("FrmlistavaloresController|llenarListas: " + e.getMessage());
        }
        System.out.println("listas: " + listas);
    }

    public void buscarListaValores() {
        condicion = " WHERE 0<1";
        if (ob != null) {
            if (lista != null) {
                condicion += " AND (TIPO_DATO = '" + lista + "')";
            }
            if (ob.get("CODIGO") != null && !ob.get("CODIGO").toString().isEmpty()) {
                condicion += " AND (CODIGO LIKE '%" + ob.get("CODIGO").toString() + "%')";
            }
            if (ob.get("NOMBRE_CORTO") != null && !ob.get("NOMBRE_CORTO").toString().isEmpty()) {
                condicion +=
                    " AND (UPPER(NOMBRE_CORTO) LIKE '%" + ob.get("NOMBRE_CORTO").toString().toUpperCase() + "%')";
            }
            if (ob.get("NOMBRE_LARGO") != null && !ob.get("NOMBRE_LARGO").toString().isEmpty()) {
                condicion +=
                    " AND (UPPER(NOMBRE_LARGO) LIKE '%" + ob.get("NOMBRE_LARGO").toString().toUpperCase() + "%')";
            }
            if (ob.get("APLICA_GERENTE") != null && ob.get("APLICA_GERENTE").equals(true)) {
                condicion += " AND (APLICA_GERENTE = '1')";
            }
            if (ob.get("ACTIVO") != null && ob.get("ACTIVO").equals(true)) {
                condicion += " AND (ACTIVO = '1')";
            }
            /*if (ob.get("FECHA_ACTUALIZACION") != null && !ob.get("FECHA_ACTUALIZACION").toString().isEmpty()) {
                condicion += " AND (FECHA_ACTUALIZACION LIKE '%" + ob.get("FECHA_ACTUALIZACION").toString() + "%')";
            }*/
            if (ob.get("USUARIO_ACTUALIZACION") != null && !ob.get("USUARIO_ACTUALIZACION").toString().isEmpty()) {
                condicion += " AND (USUARIO_ACTUALIZACION LIKE '%" + ob.get("USUARIO_ACTUALIZACION").toString() + "%')";
            }
        }
        //condicion += " ORDER BY DESCRIPCION_TIPO_DATO";
        System.out.println("consulta: " + condicion);
        try {
            listValores = consTablaEJB.consultarTabla(0, 0, "V_LISTA_VALORES_ADMIN", condicion);
            Iterator it = listValores.iterator();
            while (it.hasNext()) {
                Hashtable ht = (Hashtable) it.next();
                ht = funciones.quitarNull(ht);
                ht.replace("FECHA_ACTUALIZACION", funciones.formatoFechaHora(ht.get("FECHA_ACTUALIZACION")));
                String act = ht.get("ACTIVO").toString();
                ht.replace("ACTIVO", act.equals("1"));
                act = ht.get("APLICA_GERENTE").toString();
                ht.replace("APLICA_GERENTE", act.equals("1"));
            }
        } catch (Exception e) {
            System.out.println("FrmlistavaloresController|buscarListaValores: " + e.getMessage());
        }
        System.out.println("listValores: " + listValores);
    }

    public void actualizar() {
        ob = listValoresSel;
        lista= (String) listValoresSel.get("TIPO_DATO");
        proceso = "ACTUALIZAR";
    }

    public void limpiar() {
        iniciarElementos();
        listValores = null;
    }

    public void guardar() {
        continuar = true;
        validar();
        if (continuar) {
            System.out.println("ob: " + ob);
            Hashtable datos = new Hashtable();
            datos.put("TIPO_DATO", new Integer(lista));
            datos.put("CODIGO", ob.get("CODIGO").toString());
            datos.put("NOMBRE_CORTO", ob.get("NOMBRE_CORTO").toString());
            datos.put("NOMBRE_LARGO", ob.get("NOMBRE_LARGO").toString());
            int act = ob.get("ACTIVO").equals(true) ? 1 : 0;
            datos.put("ACTIVO", act);
            String ger = ob.get("APLICA_GERENTE").equals(true) ? "1" : "0";
            datos.put("APLICA_GERENTE", ger);
            datos.put("FECHA_ACTUALIZACION", new Timestamp(System.currentTimeMillis()));
            String usuario = datosRegistro.get("CODIGO_USUARIO").toString();
            datos.put("USUARIO_ACTUALIZACION", new Long(usuario));
            String descTipList = ob.get("DESCRIPCION_TIPO_DATO").toString();
            if (proceso.equals("INSERTAR")) {
                try {
                    listaValoresEJB.crearValor(datos);
                    msg = "Se creó el valor en la lista " + descTipList + " exitosamente";
                    limpiar();
                } catch (SQLException e) {
                    msg = "" + e.getMessage();
                }
            }
            if (proceso.equals("ACTUALIZAR")) {
                try {
                    listaValoresEJB.actualizarValor(datos);
                    msg = "Se actualizó el valor en la lista " + descTipList + " exitosamente";
                    limpiar();
                } catch (SQLException e) {
                    msg = "" + e.getMessage();
                }
            }
        }
        fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        FacesContext.getCurrentInstance().addMessage(null, fcMsg);
    }

    private void validar() {
        System.out.println("ob: " + ob);
        if (ob != null) {
            if (lista == null) {
                msg = "Lista es obligatorio";
                continuar = false;
            }
            if (continuar && ob.get("CODIGO").toString().isEmpty()) {
                msg = "Codigo es obligatorio";
                continuar = false;
            } else if (continuar && proceso.equals("INSERTAR")) {
                if (validarDuplicidad("CODIGO", ob.get("CODIGO").toString())) {
                    msg = "Codigo ya existe";
                    continuar = false;
                }
            }
            if (continuar && ob.get("NOMBRE_CORTO").toString().isEmpty()) {
                msg = "Nombre corto es obligatorio";
                continuar = false;
            } else if (continuar && proceso.equals("INSERTAR")) {
                if (validarDuplicidad("NOMBRE_CORTO", ob.get("NOMBRE_CORTO").toString())) {
                    msg = "Nombre corto ya existe";
                    continuar = false;
                }
            }
            if (continuar && ob.get("NOMBRE_LARGO").toString().isEmpty()) {
                msg = "Nombre largo es obligatorio";
                continuar = false;
            }
        }
    }

    private boolean validarDuplicidad(String campo, String valor) {
        boolean res = false;
        String cons = "SELECT * FROM V_LISTA_VALORES_ADMIN WHERE " + campo + " = '" + valor + "'";
        System.out.println("cons: " + cons);
        Collection colRes;
        try {
            colRes = consTablaEJB.consultarTabla(0, 0, null, cons);
            if (colRes != null && colRes.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("FrmlistavaloresController|llenarListas: " + e.getMessage());
        }
        return res;
    }

    public void setOb(Hashtable ob) {
        this.ob = ob;
    }

    public Hashtable getOb() {
        return ob;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setListValores(Collection listValores) {
        this.listValores = listValores;
    }

    public Collection getListValores() {
        return listValores;
    }

    public void setListValoresSel(Hashtable listValoresSel) {
        this.listValoresSel = listValoresSel;
    }

    public Hashtable getListValoresSel() {
        return listValoresSel;
    }

    public void setListas(Collection listas) {
        this.listas = listas;
    }

    public Collection getListas() {
        return listas;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public String getLista() {
        return lista;
    }
}
