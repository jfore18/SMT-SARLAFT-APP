package view.controllers;

import admin.Cargo;
import admin.CargoEJB;

import admin.usuario.Usuario;
import admin.usuario.UsuarioEJB;

import admin.util.Funciones;

import baseDatos.ConsultaTablaEJB;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

import presentacion.FacadeEJB;

@ManagedBean(name = "FrmcargoController")
@SessionScoped
public class FrmcargoController {
	@EJB
	private ConsultaTablaEJB consTablaEJB;
	@EJB
	private CargoEJB cargoEJB;
	@EJB
	FacadeEJB facadeEJB;
	@EJB
	UsuarioEJB usuarioEJB;
	@ManagedProperty("#{ListavaloresController}")
	private ListavaloresController listavaloresCtr;
	private FacesContext facContex;
	private ExternalContext extContex;
	private HttpSession sesion;
	private Funciones funciones;
	private String un;
	private String codUn;
	private String cargo;
	private String txtBuscUN;
	private String tipoCargoSel;
	private String perfilSel;
	private String nombreUsuario;
	private String cedulaUsuario;
	private String dominioUsuario;
	private String txtBuscNombreUsuario;
	private String condicion;
	private String msg;
	private String usuario;
	private String displayReg;
	private String[] regionesChk;
	private FacesMessage fcMsg;
	private Hashtable unSel;
	private Hashtable cargoSel;
	private Hashtable nombreUsuarioSel;
	private Hashtable datosRegistro;
	private Collection listUN;
	private Collection listCargos;
	private Collection listNombresUsuario;
	private boolean continuar;
	private boolean activo;
	private boolean flagBtnNuevo;

	@PostConstruct
	public void init() {
		facContex = FacesContext.getCurrentInstance();
		extContex = facContex.getExternalContext();
		sesion = (HttpSession) extContex.getSession(false);
		datosRegistro = (Hashtable) sesion.getAttribute("datosRegistro");
		usuario = (String) datosRegistro.get("CODIGO_USUARIO");
		funciones = new Funciones();
		limpiar();
	}

	public void consultaUN() {
		listUN = null;
		if (txtBuscUN != null && !txtBuscUN.isEmpty()) {
			try {
				String sql = "SELECT * FROM V_UN";
				sql += " WHERE CODIGO_UNIDAD_NEGOCIO LIKE '%" + txtBuscUN.toUpperCase() + "%'";
				sql += " OR NOMBRE_UNIDAD_NEGOCIO LIKE '%" + txtBuscUN.toUpperCase() + "%'";
				listUN = consTablaEJB.consultarTabla(0, 0, null, sql);
			} catch (Exception e) {
				System.out.println("FrmcargoController|consultaUN: " + e.getMessage());
			}
		}
		// System.out.println("listUN: " + listUN);
		txtBuscUN=null;
	}

	public void actualizarUn() {
		un = unSel.get("NOMBRE_UNIDAD_NEGOCIO").toString();
		codUn = unSel.get("CODIGO_UNIDAD_NEGOCIO").toString();
		txtBuscUN= null;
		listUN=null;
		
	}

	public void consultaNombreUsuario() {
		listNombresUsuario = null;
		if (txtBuscNombreUsuario != null && !txtBuscNombreUsuario.isEmpty()) {
			try {
				String sql = "SELECT CODIGO_USUARIO,NOMBRE_USUARIO,CODIGO_CARGO,DOMINIO_USUARIO FROM V_USUARIOS";
				sql += " WHERE CODIGO_USUARIO LIKE '%" + txtBuscNombreUsuario.toUpperCase() + "%'";
				sql += " OR NOMBRE_USUARIO LIKE '%" + txtBuscNombreUsuario.toUpperCase() + "%'";
				listNombresUsuario = consTablaEJB.consultarTabla(0, 0, null, sql);
			} catch (Exception e) {
				System.out.println("FrmcargoController|listNombresUsuario: " + e.getMessage());
			}
		}
		// System.out.println("listNombresUsuario: " + listNombresUsuario);
	}

	public void actualizarUsuario() {
		nombreUsuario = nombreUsuarioSel.get("NOMBRE_USUARIO").toString();
		cedulaUsuario = nombreUsuarioSel.get("CODIGO_USUARIO").toString();
		dominioUsuario = nombreUsuarioSel.get("DOMINIO_USUARIO").toString();
		txtBuscNombreUsuario =null;
		listNombresUsuario=null;
	}

	public void buscarCargos() {
		condicion = " WHERE 0<1";
		if (cargo != null && !cargo.isEmpty()) {
			condicion += " AND (CODIGO_CARGO LIKE '%" + cargo.toUpperCase() + "%')";
		}
		if (!un.isEmpty()) {
			condicion += " AND (UPPER(NOMBRE_UNIDAD_NEGOCIO)='" + un.toUpperCase() + "')";
		}
		if (tipoCargoSel != null && !tipoCargoSel.isEmpty()) {
			condicion += " AND (CODIGO_TIPO_CARGO = '" + tipoCargoSel + "')";
		}
		if (perfilSel != null && !perfilSel.isEmpty()) {
			condicion += " AND (CODIGO_PERFIL = '" + perfilSel + "')";
		}
		if (activo) {
			condicion += " AND (CARGO_ACTIVO = '1')";
		}
		if (!nombreUsuario.isEmpty()) {
			condicion += " AND (UPPER(NOMBRE_USUARIO) LIKE '%" + nombreUsuario.toUpperCase() + "%')";
		}
		if (!cedulaUsuario.isEmpty()) {
			condicion += " AND (CODIGO_USUARIO LIKE '%" + cedulaUsuario + "%')";
		}
		if (!dominioUsuario.isEmpty()) {
			condicion += " AND (UPPER(DOMINIO_USUARIO) LIKE '%" + dominioUsuario.toUpperCase() + "%')";
		}
		Integer limite = listavaloresCtr.getLimiteRes();
		condicion += " AND ROWNUM <= " + limite;
		System.out.println("condicion: " + condicion);
		try {
			listCargos = consTablaEJB.consultarTabla(0, 0, "V_USUARIOS", condicion);
			Iterator it = listCargos.iterator();
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				ht = funciones.quitarNull(ht);
			}
		} catch (Exception e) {
			System.out.println("FrmcargoController|buscarCargos" + e.getMessage());
		}
		// System.out.println("listCargos: " + listCargos);
		txtBuscUN=null;
	}

	public void actualizar() {
		cargo = cargoSel.get("CODIGO_CARGO").toString();
		un = cargoSel.get("NOMBRE_UNIDAD_NEGOCIO").toString();
		codUn = cargoSel.get("CODIGO_UNIDAD_NEGOCIO").toString();
		tipoCargoSel = cargoSel.get("CODIGO_TIPO_CARGO").toString();
		perfilSel = cargoSel.get("CODIGO_PERFIL").toString();
		activo = cargoSel.get("CARGO_ACTIVO").equals("1");
		nombreUsuario = cargoSel.get("NOMBRE_USUARIO").toString();
		cedulaUsuario = cargoSel.get("CODIGO_USUARIO").toString();
		dominioUsuario = cargoSel.get("DOMINIO_USUARIO").toString();
		flagBtnNuevo = true;
		displayReg = "none";
		if (perfilSel.equals("5")) {
			consRegionesAnalistaDucc();
			displayReg = "block";
		}
	}

	private void consRegionesAnalistaDucc() {
		if (cargo != null && !cargo.isEmpty()) {
			Collection colRegAnal = null;
			String condicionRegAnal = " WHERE CODIGO_CARGO = '" + cargo + "' ";
			try {
				colRegAnal = consTablaEJB.consultarTabla(0, 0, "ANALISTA_REGION", condicionRegAnal);
				Iterator it = colRegAnal.iterator();
				int i = 0;
				int tamano = colRegAnal.size();
				regionesChk = new String[tamano];
				while (it.hasNext()) {
					Hashtable htRegAnal = (Hashtable) it.next();
					String codigoRegion = htRegAnal.get("CODIGO_REGION_V").toString();
					regionesChk[i] = codigoRegion;
					i++;
				}
			} catch (Exception e) {
				System.out.println("FrmcargoController|consRegionesAnalistaDucc: " + e.getMessage());
				if (e.getMessage() == null) {
					e.printStackTrace();
				}
			}
		} else {
			regionesChk = null;
		}
	}

	public void nuevo() {
		limpiar();
		flagBtnNuevo = true;
	}

	public void limpiar() {
		cargo = "";
		unSel = null;
		un = "";
		tipoCargoSel = null;
		perfilSel = null;
		nombreUsuarioSel = null;
		nombreUsuario = "";
		cedulaUsuario = "";
		dominioUsuario = "";
		activo = false;
		listCargos = null;
		flagBtnNuevo = false;
		displayReg = "none";
	}

	private String strLeng4(String str) {
		String strRet = str;
		while (strRet.length() < 4) {
			strRet = "0" + strRet;
		}
		return strRet;
	}

	public void guardar() {
		validar();
		if (continuar) {
			String tipoMsg = "";
			String codigoUnidadNegocio = codUn;
			String codigoTipoCargo = tipoCargoSel;
			String codigoCargo = cargo.isEmpty() ? strLeng4(codigoUnidadNegocio) + codigoTipoCargo : cargo;
			String codigoPerfil = perfilSel;
			Integer cargoActivo = activo ? 1 : 0;
			String codigoUsuario = cedulaUsuario;
			String estadoForma = "";
			SimpleDateFormat SDFormat = new SimpleDateFormat("DD/MM/YYYY HH:MM:SS"); // ob
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			System.out.println("fechaActual: " + fechaActual);
			Long usuarioActual = new Long(usuario);
			try {
				// Se asume que tanto el cargo como el usuario existen.
				// Se actualizan los valores del cargo menos el usuaio asociado
				Cargo cargoLocal = (Cargo) cargoEJB.findByPrimaryKey(codigoCargo);
				cargoLocal.setUsuarioActualizacion(usuarioActual);
				cargoLocal.setFechaActualizacion(fechaActual);
				cargoLocal.setActivo(cargoActivo);
				cargoLocal.setCodigoPerfilV(codigoPerfil);
				cargoLocal.setCodigoTipoCargoV(codigoTipoCargo);
				cargoLocal.setCodigoUnidadNegocio(Integer.valueOf(codigoUnidadNegocio));
				cargoEJB.actualizarCargo(cargoLocal);
				String perfilAnterior = cargoLocal.getCodigoPerfilV();
				if (perfilAnterior != null && perfilAnterior.equals("5") && !perfilAnterior.equals(codigoPerfil)) {
					String[] noRegiones = new String[0];
					facadeEJB.asignarRegionAnalista(noRegiones, codigoCargo);
					
				}
				// Inactivacion del cargo
				if (cargoLocal.getActivo().intValue() == 0) {
					try {
						// Obtenemos el usuario asociado al cargo y lo inactivamos
						Long usuarioCargo = cargoLocal.getCodigoUsuario();
						Usuario u= usuarioEJB.findByPrimaryKey(usuarioCargo);
						u.setFechaActualizacion(fechaActual);
						u.setUsuarioActualizacion(usuarioActual);
						u.setActivo(new Integer(0));
						usuarioEJB.actualizarUsuario(u);
						// Desasociamos el usuario del cargo
						cargoLocal.setCodigoUsuario(null);
						cargoEJB.actualizarCargo(cargoLocal);
						msg = "Se inactivó exitosamente el cargo, desvinculando el usuario";
						tipoMsg = "exito";
					} catch (Exception noDejaQuitarUsuario) {
						msg = "El cargo no tiene usuario asociado, por lo tanto no es necesario desactivarlo";
						tipoMsg = "error";
						// noDejaQuitarUsuario.printStackTrace();
					}
				}
				// Activacion del cargo
				if (cargoLocal.getActivo().intValue() == 1) {
					try {
						// Validamos sí el usuario a asignar(nuevo usuario) está asociado a otro cargo
						// para desvincularlo
						String miConsulta = "WHERE CODIGO_USUARIO = '" + codigoUsuario + "'" + " AND CODIGO <> '"
								+ codigoCargo + "'" + " AND CODIGO_PERFIL_V <> '" + codigoPerfil + "'";
						Collection resultados = consTablaEJB.consultarTabla(0, 0, "CARGOS", miConsulta);
						
						String desasignacion = "";
						// Usuario asociado a otro cargo
						if (resultados.size() > 0) {
							Iterator itConsulta = resultados.iterator();
							while (itConsulta.hasNext()) {
								Hashtable temp = (Hashtable) itConsulta.next();
								// Obtenemos los datos de ese cargo
								Cargo cargoAnterior = cargoEJB.findByPrimaryKey((String) temp.get("CODIGO"));
								cargoAnterior.setFechaActualizacion(fechaActual);
								cargoAnterior.setUsuarioActualizacion(usuarioActual);
								cargoAnterior.setCodigoUsuario(null);
								cargoAnterior.setActivo(new Integer(0));
								cargoEJB.actualizarCargo(cargoAnterior); // ob
								cargoAnterior = null;
								desasignacion += cargoEJB.getCargo().getCodigo() + ",";
							}
						}
						try {
							// Obtenemos el nuevo usuario asignado al cargo
							Usuario miUsuario = usuarioEJB.findByPrimaryKey(new Long(codigoUsuario));
							// Obtenemos el usuario anterior del cargo que se está modificando
							Long codUsuarioAnterior = cargoLocal.getCodigoUsuario();
							if (codUsuarioAnterior != null
									&& (codUsuarioAnterior.intValue() != (new Integer(codigoUsuario)).intValue())) {
								Usuario usuarioAnt = usuarioEJB.findByPrimaryKey(codUsuarioAnterior);
								usuarioAnt.setFechaActualizacion(fechaActual);
								usuarioAnt.setUsuarioActualizacion(usuarioActual);
								usuarioAnt.setActivo(new Integer(0));
								usuarioEJB.actualizarUsuario(usuarioAnt); // ob
								usuarioAnt = null;
							}
							miUsuario.setFechaActualizacion(fechaActual);
							miUsuario.setUsuarioActualizacion(usuarioActual);
							miUsuario.setActivo(cargoActivo);
							miUsuario.setDominioUsuario(dominioUsuario.toUpperCase());
							miUsuario.setNombre(nombreUsuario.toUpperCase());
							usuarioEJB.actualizarUsuario(miUsuario);
							cargoEJB.setCargo(cargoLocal);
							cargoEJB.establecerUsuario(new Long(codigoUsuario));
							msg = "Se actualizó el cargo y el usuario exitosamente.";
							tipoMsg = "exito";
							if (!desasignacion.equals("")) {
								msg += "\n Se desvincula el usuario de su cargo anterior: "
										+ desasignacion.substring(0, desasignacion.length() - 1);
							}
						}
						// El usuario a asignar al cargo no existe
						catch (Exception usuarioNoExiste) {
							
							try {
								// Inactivamos el usuario anterior del cargo
								Long usuarioAnterior = cargoLocal.getCodigoUsuario();
							
								if (usuarioAnterior != null) {
									Usuario usuarioAnt = usuarioEJB.findByPrimaryKey(usuarioAnterior);
									usuarioAnt.setFechaActualizacion(fechaActual);
									usuarioAnt.setUsuarioActualizacion(usuarioActual);
									usuarioAnt.setActivo(new Integer(0));
									usuarioEJB.actualizarUsuario(usuarioAnt); // ob
								
								}
								Hashtable datosUsuario = new Hashtable();
								datosUsuario.put("CEDULA", new Long(codigoUsuario));
								datosUsuario.put("ACTIVO", cargoActivo);
								datosUsuario.put("DOMINIO", dominioUsuario.toUpperCase());
								datosUsuario.put("FECHA_CREACION", fechaActual);
								datosUsuario.put("USUARIO_CREACION", usuarioActual);
								datosUsuario.put("NOMBRE", nombreUsuario.toUpperCase());
								
								usuarioEJB.create(datosUsuario);
								cargoEJB.establecerUsuario(usuarioEJB.getUsuario().getCedula());
								msg = "Se actualizó el cargo y se creó el usuario exitosamente";
								tipoMsg = "exito";
							} catch (Exception error) {
								msg = "Error al intentar crear el usuario, en la actualización del cargo: "
										+ error.getMessage();
								tipoMsg = "error";
								error.printStackTrace();
							}
						}
					} catch (javax.ejb.EJBException noExisteUsuario) {
						msg = "No se pudo realizar la transaccion " + noExisteUsuario.getMessage();
						tipoMsg = "error";
						noExisteUsuario.printStackTrace();
					}
				}
			}
			// Creacion de nuevo cargo
			catch (Exception cargoNoExiste) {
				Hashtable datosCargo = new Hashtable();
				datosCargo.put("CODIGO_CARGO", codigoCargo);
				datosCargo.put("ACTIVO", cargoActivo);
				datosCargo.put("CODIGO_PERFIL", codigoPerfil);
				datosCargo.put("CODIGO_TIPO_CARGO", codigoTipoCargo);
				datosCargo.put("CODIGO_UNIDAD_NEGOCIO", new Integer(codigoUnidadNegocio));
				datosCargo.put("FECHA_CREACION", fechaActual);
				datosCargo.put("USUARIO_CREACION", usuarioActual);
				try {
					// Validamos sí el usuario seleccionado ya existe
					Usuario usuarioLocal = usuarioEJB.findByPrimaryKey(new Long(codigoUsuario));
					String miConsulta = "WHERE CODIGO_USUARIO = '" + codigoUsuario + "'" + " AND CODIGO_PERFIL_V <> '"
							+ codigoPerfil + "'";
					// Obtenemos el cargo asociado al usuario
					Collection resultados = consTablaEJB.consultarTabla(0, 0, "CARGOS", miConsulta);
					String desasignacion = "";
					if (resultados.size() > 0) {
						Iterator itConsulta = resultados.iterator();
						while (itConsulta.hasNext()) {
							Hashtable temp = (Hashtable) itConsulta.next();
							Cargo cargoAnterior = cargoEJB.findByPrimaryKey((String) temp.get("CODIGO"));
							// Desvinculamos del cargo al usuario
							cargoAnterior.setFechaActualizacion(fechaActual);
							cargoAnterior.setUsuarioActualizacion(usuarioActual);
							cargoAnterior.setCodigoUsuario(null);
							cargoAnterior.setActivo(new Integer(0));
							cargoEJB.actualizarCargo(cargoAnterior);
							desasignacion = cargoAnterior.getCodigo() + ",";
						}
					}
					datosCargo.put("CODIGO_USUARIO", usuarioLocal.getCedula());
					cargoEJB.create(datosCargo);
					// ob CargoLocal cargoLocal = cargoHome.create(datosCargo);
					codigoCargo = new String(cargoEJB.getCargo().getCodigo());
					usuarioLocal.setFechaActualizacion(fechaActual);
					usuarioLocal.setUsuarioActualizacion(usuarioActual);
					usuarioLocal.setActivo(cargoActivo);
					usuarioLocal.setDominioUsuario(dominioUsuario.toUpperCase());
					usuarioLocal.setNombre(nombreUsuario.toUpperCase());
					usuarioEJB.actualizarUsuario(usuarioLocal); // ob
					msg = "Se creó el cargo " + cargoEJB.getCargo().getCodigo() + " y se actualizó el usuario"
							+ usuarioLocal.getCedula();
					tipoMsg = "exito";
					if (!desasignacion.equals("")) {
						msg += " .Se desvinculó el usuario de su cargo anterior: "
								+ desasignacion.substring(0, desasignacion.length() - 1);
					}
				}
				// Creación de usuario
				catch (Exception usuarioNoExiste) {
					usuarioNoExiste.printStackTrace();
					try {
						Hashtable datosUsuario = new Hashtable();
						datosUsuario.put("CEDULA", new Long(codigoUsuario));
						datosUsuario.put("ACTIVO", cargoActivo);
						datosUsuario.put("DOMINIO", dominioUsuario.toUpperCase());
						datosUsuario.put("FECHA_CREACION", fechaActual);
						datosUsuario.put("USUARIO_CREACION", usuarioActual);
						datosUsuario.put("NOMBRE", nombreUsuario.toUpperCase());
						usuarioEJB.create(datosUsuario);
						datosCargo.put("CODIGO_USUARIO", usuarioEJB.getUsuario().getCedula());
						String nombre_usuario = usuarioEJB.getUsuario().getNombre();
						cargoEJB.create(datosCargo);
						codigoCargo = new String(cargoEJB.getCargo().getCodigo());
						msg = "Se creó exitosamente el cargo " + cargoEJB.getCargo().getCodigo() + " y el usuario "
								+ nombre_usuario;
						tipoMsg = "exito";
					} catch (Exception error) {
						msg = "Error al intentar crear el usuario creando cargo: " + error.getMessage();
						tipoMsg = "error";
						error.printStackTrace();
					}
				}
			}
			if (codigoPerfil.equals("5")) {
				if (regionesChk != null) {
					try {
						facadeEJB.asignarRegionAnalista(regionesChk, codigoCargo);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			sesion.setAttribute("msg", msg);
			sesion.setAttribute("tipoMsg", tipoMsg);
			limpiar();
			PrimeFaces.current().executeScript("PF('dgwvResultado').show()");
		} else {
			fcMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
			FacesContext.getCurrentInstance().addMessage(null, fcMsg);
		}
	}

	private void validar() {
		continuar = true;
		if (un.isEmpty()) {
			continuar = false;
			msg = "Debe ingresar la unidad de negocio";
		}
		if (continuar && tipoCargoSel == null) {
			continuar = false;
			msg = "Debe seleccionar el tipo de cargo";
		}
		if (continuar && perfilSel == null) {
			continuar = false;
			msg = "Debe seleccionar el perfil";
		}
		if (continuar && nombreUsuario.isEmpty()) {
			continuar = false;
			msg = "Debe ingresar el nombre de usuario";
		}
		if (continuar && cedulaUsuario.isEmpty()) {
			continuar = false;
			msg = "Debe ingresar la cédula de usuario";
		}
		if (continuar && dominioUsuario.isEmpty()) {
			continuar = false;
			msg = "Debe ingresar el dominio de usuario";
		}
		if(continuar && tipoCargoSel.equals("N") && !perfilSel.equals("2")) {
			continuar = false;
			msg = "Verifique el tipo de cargo y perfil seleccionado";
			
		}
	}

	public void displayRegiones() {
		System.out.println("Perfil Sel: " + perfilSel);
		displayReg = "none";
		if (perfilSel != null && perfilSel.equals("5")) {
			consRegionesAnalistaDucc();
			displayReg = "block";
		}
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCargo() {
		return cargo;
	}

	public void setUnSel(Hashtable unSel) {
		this.unSel = unSel;
	}

	public Hashtable getUnSel() {
		return unSel;
	}

	public void setTxtBuscUN(String txtBuscUN) {
		this.txtBuscUN = txtBuscUN;
	}

	public String getTxtBuscUN() {
		return txtBuscUN;
	}

	public void setListUN(Collection listUN) {
		this.listUN = listUN;
	}

	public Collection getListUN() {
		return listUN;
	}

	public void setListCargos(Collection listCargos) {
		this.listCargos = listCargos;
	}

	public Collection getListCargos() {
		return listCargos;
	}

	public void setCargoSel(Hashtable cargoSel) {
		this.cargoSel = cargoSel;
	}

	public Hashtable getCargoSel() {
		return cargoSel;
	}

	public void setTipoCargoSel(String tipoCargoSel) {
		this.tipoCargoSel = tipoCargoSel;
	}

	public String getTipoCargoSel() {
		return tipoCargoSel;
	}

	public void setPerfilSel(String perfilSel) {
		this.perfilSel = perfilSel;
	}

	public String getPerfilSel() {
		return perfilSel;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setCedulaUsuario(String cedulaUsuario) {
		this.cedulaUsuario = cedulaUsuario;
	}

	public String getCedulaUsuario() {
		return cedulaUsuario;
	}

	public void setDominioUsuario(String dominioUsuario) {
		this.dominioUsuario = dominioUsuario;
	}

	public String getDominioUsuario() {
		return dominioUsuario;
	}

	public void setListNombresUsuario(Collection listNombresUsuario) {
		this.listNombresUsuario = listNombresUsuario;
	}

	public Collection getListNombresUsuario() {
		return listNombresUsuario;
	}

	public void setTxtBuscNombreUsuario(String txtBuscNombreUsuario) {
		this.txtBuscNombreUsuario = txtBuscNombreUsuario;
	}

	public String getTxtBuscNombreUsuario() {
		return txtBuscNombreUsuario;
	}

	public void setNombreUsuarioSel(Hashtable nombreUsuarioSel) {
		this.nombreUsuarioSel = nombreUsuarioSel;
	}

	public Hashtable getNombreUsuarioSel() {
		return nombreUsuarioSel;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setUn(String un) {
		this.un = un;
	}

	public String getUn() {
		return un;
	}

	public void setFlagBtnNuevo(boolean flagBtnNuevo) {
		this.flagBtnNuevo = flagBtnNuevo;
	}

	public boolean isFlagBtnNuevo() {
		return flagBtnNuevo;
	}

	public void setListavaloresCtr(ListavaloresController listavaloresCtr) {
		this.listavaloresCtr = listavaloresCtr;
	}

	public ListavaloresController getListavaloresCtr() {
		return listavaloresCtr;
	}

	public void setCodUn(String codUn) {
		this.codUn = codUn;
	}

	public String getCodUn() {
		return codUn;
	}

	public void setRegionesChk(String[] regionesChk) {
		this.regionesChk = regionesChk;
	}

	public String[] getRegionesChk() {
		return regionesChk;
	}

	public void setDisplayReg(String displayReg) {
		this.displayReg = displayReg;
	}

	public String getDisplayReg() {
		return displayReg;
	}
}
