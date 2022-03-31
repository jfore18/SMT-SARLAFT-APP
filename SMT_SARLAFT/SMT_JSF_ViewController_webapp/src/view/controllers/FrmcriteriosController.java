package view.controllers;

import admin.CriteriosInusualidad;
import admin.CriteriosInusualidadEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

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

import org.eclipse.persistence.exceptions.DatabaseException;

import presentacion.FacadeEJB;

@ManagedBean(name = "FrmcriteriosController")
@SessionScoped
public class FrmcriteriosController {

	@EJB
	ConsultaTablaEJB consultaTablaEJB;
	@EJB
	CriteriosInusualidadEJB criteriosInusualidadEJB;
	@EJB
	FacadeEJB facadeEJB;
	private FacesContext facContex;
	private ExternalContext extContex;
	private HttpSession sesion;
	private Funciones funciones;
	private Hashtable datosRegistro;
	private FacesMessage fcMsg;
	private Collection listCriterios;
	private Hashtable criterioSel;
	private Hashtable criteriosPerfil;
	private String usuario;
	private String msg;
	private String producto;
	private String descripcion;
	private String idCriterio;
	private String mensaje;
	private String funcion;
	private String proceso;
	private boolean gerenteUnNeg;
	private boolean gerenteZona;
	private boolean directorReg;
	private boolean ducc;
	private boolean consulta;
	private boolean activo;
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
		funcion = "f_clementine";
		limpiar();
	}

	public void buscar() {
		String condicion = " WHERE 0<1";
		if (producto != null) {
			condicion += " AND CODIGO_PRODUCTO_V = '" + producto + "'";
		}
		if (!descripcion.isEmpty()) {
			condicion += " AND UPPER(DESCRIPCION) LIKE '%" + descripcion.toUpperCase() + "%'";
		}
		if (!idCriterio.isEmpty()) {
			condicion += " AND ID LIKE '%" + idCriterio + "%'";
		}
		if (!mensaje.isEmpty()) {
			condicion += " AND UPPER(MENSAJE) LIKE '%" + mensaje.toUpperCase() + "%'";
		}
		if (activo) {
			condicion += " AND ACTIVO = '1'";
		}
		System.out.println("condicion: " + condicion);
		try {
			listCriterios = consultaTablaEJB.consultarTabla(0, 0, "V_CRITERIOS_ADMIN", condicion);
			Iterator it = listCriterios.iterator();
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				ht = funciones.quitarNull(ht);
			}
		} catch (Exception e) {
			System.out.println("FrmcriteriosController|buscar: " + e.getMessage());
		}
		System.out.println("listCriterios: " + listCriterios);
	}

	public void limpiar() {
		producto = null;
		descripcion = "";
		idCriterio = "";
		mensaje = "";
		gerenteUnNeg = false;
		gerenteZona = false;
		directorReg = false;
		ducc = false;
		consulta = false;
		activo = false;
		listCriterios = null;
		criterioSel = null;
		proceso = "INSERTAR";
		disabled = false;
	}

	public void guardar() {
		validar();
		if (continuar) {
			Hashtable pDatos = new Hashtable();
			pDatos.put("ID", idCriterio);
			pDatos.put("CODIGO_PRODUCTO_V", producto);
			pDatos.put("DESCRIPCION", descripcion.toUpperCase());
			pDatos.put("MENSAJE", mensaje);
			pDatos.put("FUNCION", funcion);
			Long activoL = activo ? new Long("1") : new Long("0");
			pDatos.put("ACTIVO", activoL);
			Long usuL = new Long(usuario);
			pDatos.put("USUARIO_CREACION", usuL);
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			pDatos.put("FECHA_CREACION", fechaActual);
			Long isProcesar = new Long(0);
			pDatos.put("PROCESAR_POR_GRUPOS", +isProcesar);
			if (proceso.equals("INSERTAR")) {
				try {
					criteriosInusualidadEJB.create(pDatos);
					msg = "Se creó exitosamente el criterio de inusualidad: " + idCriterio + " - "
							+ descripcion.toUpperCase();
					insertarCriteriosPerfil();
					limpiar();
				} catch (Exception e) {
					System.out.println("FrmcriteriosController|guardar|INSERTAR: " + e.getMessage());
					msg = "INSERTAR: " + e.getMessage();
				}
			}
			if (proceso.equals("ACTUALIZAR")) {
				CriteriosInusualidad ccLocal;
				try {
					Long idCritL = new Long(idCriterio);
					ccLocal = criteriosInusualidadEJB.findByPrimaryKey(idCritL);
					ccLocal.setCodigoProductoV(producto);
					ccLocal.setDescripcion(descripcion.toUpperCase());
					ccLocal.setMensaje(mensaje);
					ccLocal.setFuncion(funcion);
					ccLocal.setActivo(activoL);
					ccLocal.setProcesarPorGrupos(isProcesar);
					criteriosInusualidadEJB.actualizarCriteriosInusualidad(ccLocal);
					insertarCriteriosPerfil();
					msg = "Se actualizó exitosamente el criterio de inusualidad: " + idCriterio + " - "
							+ descripcion.toUpperCase();
					limpiar();

				} catch (Exception e) {
					msg = "FrmcriteriosController|guardar|ACTUALIZAR: " + e.getMessage();
					System.out.println("FrmcriteriosController|guardar|ACTUALIZAR: " + e.getMessage());
				}
			}
		}
		fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
		FacesContext.getCurrentInstance().addMessage(null, fcMsg);
	}

	private boolean validarCampos(String tabla, String campo, String valor) {
		boolean res = false;
		String condicion = " WHERE " + campo + " = '" + valor + "'";
		try {
			Collection colRes = consultaTablaEJB.consultarTabla(0, 0, tabla, condicion);
			if (colRes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("FrmcriteriosController|validarCampos: " + e.getMessage());
		}
		return res;
	}

	private void validar() {
		continuar = true;
		if (producto == null) {
			continuar = false;
			msg = "Debe seleccionar un producto";
		}
		if (continuar && idCriterio.isEmpty()) {
			continuar = false;
			msg = "Debe ingresar el id del criterio";
		} else if (continuar && proceso.equals("INSERTAR")) {
			if (validarCampos("V_CRITERIOS_ADMIN", "ID", idCriterio)) {
				continuar = false;
				msg = "Id del criterio ya existe";
			}
		}
		if (continuar && (!gerenteUnNeg && !gerenteZona && !directorReg && !ducc && !consulta)) {
			continuar = false;
			msg = "Debe seleccionar por lo menos un perfil para asociarlo al criterio";
		}
	}

	public void actualizar() {
		System.out.println("esto es disable:" + disabled);
		producto = criterioSel.get("CODIGO_PRODUCTO_V").toString();
		descripcion = criterioSel.get("DESCRIPCION").toString();
		idCriterio = criterioSel.get("ID").toString();
		mensaje = criterioSel.get("MENSAJE").toString();
		gerenteUnNeg = false;
		gerenteZona = false;
		directorReg = false;
		ducc = false;
		consulta = false;
		constultarCriteriosPerfil();
		activo = criterioSel.get("ACTIVO").toString().equals("1");
		proceso = "ACTUALIZAR";
		disabled = true;
	}

	private void constultarCriteriosPerfil() {
		if (funcion.equals("f_clementine")) {
			String consultaPerfil = " WHERE ID_CRITERIO_INUSUALIDAD = '" + idCriterio + "'";
			Collection perfiles = null;
			try {
				perfiles = consultaTablaEJB.consultarTabla(0, 0, "CRITERIOS_PERFIL", consultaPerfil);
			} catch (Exception e) {
				System.out.println("FrmcriteriosController|constultarCriteriosPerfil: " + e.getMessage());
			}
			System.out.println("perfiles: " + perfiles);
			if (perfiles != null) {
				Iterator ite = perfiles.iterator();
				while (ite.hasNext()) {
					Hashtable temp = (Hashtable) ite.next();
					String perfil = (String) temp.get("CODIGO_PERFIL");
					if (perfil.equals("2")) {
						gerenteUnNeg = true;
					}
					if (perfil.equals("3")) {
						gerenteZona = true;
					}
					if (perfil.equals("4")) {
						directorReg = true;
					}
					if (perfil.equals("5")) {
						ducc = true;
					}
					if (perfil.equals("6")) {
						consulta = true;
					}
				}
			}
		}
	}

	private void insertarCriteriosPerfil() {
		int idCrit = Integer.parseInt(idCriterio);
		if (idCrit > 83) {
			List<String> listPerf = new ArrayList<>();
			if (gerenteUnNeg) {
				listPerf.add("2");
			}
			if (gerenteZona) {
				listPerf.add("3");
			}
			if (directorReg) {
				listPerf.add("4");
			}
			if (ducc) {
				listPerf.add("5");
			}
			if (consulta) {
				listPerf.add("6");
			}
			try {
				facadeEJB.asignarPerfilCriterio(listPerf, idCriterio);
			} catch (SQLException e) {
				System.out.println("FrmcriteriosController|insertarCriteriosPerfil: " + e.getMessage());
			}
		}
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getProducto() {
		return producto;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setIdCriterio(String idCriterio) {
		this.idCriterio = idCriterio;
	}

	public String getIdCriterio() {
		return idCriterio;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setGerenteUnNeg(boolean gerenteUnNeg) {
		this.gerenteUnNeg = gerenteUnNeg;
	}

	public boolean isGerenteUnNeg() {
		return gerenteUnNeg;
	}

	public void setGerenteZona(boolean gerenteZona) {
		this.gerenteZona = gerenteZona;
	}

	public boolean isGerenteZona() {
		return gerenteZona;
	}

	public void setDirectorReg(boolean directorReg) {
		this.directorReg = directorReg;
	}

	public boolean isDirectorReg() {
		return directorReg;
	}

	public void setDucc(boolean ducc) {
		this.ducc = ducc;
	}

	public boolean isDucc() {
		return ducc;
	}

	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}

	public boolean isConsulta() {
		return consulta;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setListCriterios(Collection listCriterios) {
		this.listCriterios = listCriterios;
	}

	public Collection getListCriterios() {
		return listCriterios;
	}

	public void setCriterioSel(Hashtable criterioSel) {
		this.criterioSel = criterioSel;
	}

	public Hashtable getCriterioSel() {
		return criterioSel;
	}

	public void setCriteriosPerfil(Hashtable criteriosPerfil) {
		this.criteriosPerfil = criteriosPerfil;
	}

	public Hashtable getCriteriosPerfil() {
		return criteriosPerfil;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}
}
