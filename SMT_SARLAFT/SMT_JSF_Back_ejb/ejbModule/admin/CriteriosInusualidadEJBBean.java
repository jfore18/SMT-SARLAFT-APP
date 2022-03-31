package admin;

import admin.usuario.UsuarioEJB;

import baseDatos.ConsultaTablaEJB;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import java.sql.Timestamp;

import javax.ejb.EJBException;

import java.util.Hashtable;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.*;

import baseDatos.TablaBD;

import java.sql.Connection;

import javax.annotation.Resource;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;

import javax.transaction.UserTransaction;

import presentacion.FacadeEJB;

@Stateless(name = "CriteriosInusualidadEJB", mappedName = "SARLAFT-EJB-CriteriosInusualidadEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class CriteriosInusualidadEJBBean implements CriteriosInusualidadEJB {
	@Resource
	SessionContext sessionContext;
	@PersistenceContext(unitName = "Administracion")
	private EntityManager entityManager;
	private CriteriosInusualidad criteriosInusualidad;
	private EntityContext context;
	private TablaBD miTabla;
	transient private Connection sesionBD;

	public Long create(Hashtable pDatos) {
		String codigoID = (String) pDatos.get("ID");
		Long id = null;
		id = new Long(Long.parseLong(codigoID));
		if (codigoID == null) {
			throw new EJBException("CriteriosInusualidadEJB|Crear: Criterio nulo");
		}

		try {
			findByPrimaryKey(id);
			throw new EJBException("Id Criterio existente. Por favor rectificar");

		} catch (NoResultException e) {

			String codigoProducto = (String) pDatos.get("CODIGO_PRODUCTO_V");
			String descripcion = (String) pDatos.get("DESCRIPCION");
			String mensaje = (String) pDatos.get("MENSAJE");
			String funcion = (String) pDatos.get("FUNCION");
			Long activo = (Long) pDatos.get("ACTIVO");
			Long usuarioDesactivacion = (Long) pDatos.get("USUARIO_DESACTIVACION");
			Timestamp fechaDesactivacion = (Timestamp) pDatos.get("FECHA_DESACTIVACION");
			String descripcionP1 = (String) pDatos.get("DESCRIPCION_P1");
			String valorP1 = (String) pDatos.get("VALOR_P1");
			String listaValorP1 = (String) pDatos.get("LISTA_VALOR_P1");
			String descripcionP2 = (String) pDatos.get("DESCRIPCION_P2");
			String valorP2 = (String) pDatos.get("VALOR_P2");
			String listaValorP2 = (String) pDatos.get("LISTA_VALOR_P2");
			String descripcionP3 = (String) pDatos.get("DESCRIPCION_P3");
			String valorP3 = (String) pDatos.get("VALOR_P3");
			String listaValorP3 = (String) pDatos.get("LISTA_VALOR_P3");
			String descripcionP4 = (String) pDatos.get("DESCRIPCION_P4");
			String valorP4 = (String) pDatos.get("VALOR_P4");
			String listaValorP4 = (String) pDatos.get("LISTA_VALOR_P4");
			String descripcionP5 = (String) pDatos.get("DESCRIPCION_P5");
			String valorP5 = (String) pDatos.get("VALOR_P5");
			String listaValorP5 = (String) pDatos.get("LISTA_VALOR_P5");
			Long usuarioCreacion = (Long) pDatos.get("USUARIO_CREACION");
			Timestamp fechaCreacion = (Timestamp) pDatos.get("FECHA_CREACION");
			Long procesarPorGrupos = (Long) pDatos.get("PROCESAR_POR_GRUPOS");

			this.criteriosInusualidad = new CriteriosInusualidad();
			this.criteriosInusualidad.setCodigoProductoV(codigoProducto);
			this.criteriosInusualidad.setDescripcion(descripcion);
			this.criteriosInusualidad.setMensaje(mensaje);
			this.criteriosInusualidad.setFuncion(funcion);
			this.criteriosInusualidad.setActivo(activo);
			this.criteriosInusualidad.setUsuarioDesactivacion(usuarioDesactivacion);
			this.criteriosInusualidad.setFechaDesactivacion(fechaDesactivacion);
			this.criteriosInusualidad.setDescripcionP1(descripcionP1);
			this.criteriosInusualidad.setValorP1(valorP1);
			this.criteriosInusualidad.setListaValorP1(listaValorP1);
			this.criteriosInusualidad.setDescripcionP2(descripcionP2);
			this.criteriosInusualidad.setValorP2(valorP2);
			this.criteriosInusualidad.setListaValorP2(listaValorP2);
			this.criteriosInusualidad.setDescripcionP3(descripcionP3);
			this.criteriosInusualidad.setValorP3(valorP3);
			this.criteriosInusualidad.setListaValorP3(listaValorP3);
			this.criteriosInusualidad.setDescripcionP4(descripcionP4);
			this.criteriosInusualidad.setValorP4(valorP4);
			this.criteriosInusualidad.setListaValorP4(listaValorP4);
			this.criteriosInusualidad.setDescripcionP5(descripcionP5);
			this.criteriosInusualidad.setValorP5(valorP5);
			this.criteriosInusualidad.setListaValorP5(listaValorP5);
			this.criteriosInusualidad.setUsuarioCreacion(usuarioCreacion);
			this.criteriosInusualidad.setFechaCreacion(fechaCreacion);
			this.criteriosInusualidad.setProcesarPorGrupos(procesarPorGrupos);
			this.criteriosInusualidad.setId(id);
			this.guardarCriteriosInusualidad(this.criteriosInusualidad);

		} catch (FinderException e) {
			throw new EJBException("CriteriosInusualidadEJB|Crear: Criterio ");
		}
		return this.criteriosInusualidad.getId();

	}

	public void guardarCriteriosInusualidad(CriteriosInusualidad criteriosInusualidad) {
		UserTransaction transaction = sessionContext.getUserTransaction();
		try {
			transaction.begin();
			entityManager.persist(criteriosInusualidad);
			transaction.commit();
		} catch (Exception e) {
			if (transaction == null) {
				throw new EJBException("CriteriosInusualidadEJB|guardarCriteriosInusualidad: transacción null ");
			}
			criteriosInusualidad = null;
			try {
				transaction.rollback();
			} catch (Exception ex) {
				throw new EJBException("CriteriosInusualidadEJB|guardarCriteriosInusualidad: " + ex.getMessage());
			}
		}
	}

	public void actualizarCriteriosInusualidad(CriteriosInusualidad criteriosInusualidad) {
		UserTransaction transaction = sessionContext.getUserTransaction();
		try {
			transaction.begin();
			entityManager.merge(criteriosInusualidad);
			transaction.commit();
		} catch (Exception e) {
			if (transaction == null) {
				throw new EJBException("CriteriosInusualidadEJB|actualizarCriteriosInusualidad: transacción null ");
			}
			criteriosInusualidad = null;
			try {
				transaction.rollback();
			} catch (Exception ex) {
				throw new EJBException("CriteriosInusualidadEJB|actualizarCriteriosInusualidad: " + ex.getMessage());
			}
		}
	}

	public CriteriosInusualidad findByPrimaryKey(Long primaryKey) throws FinderException, NoResultException {
		System.out.println("findByPrimaryKey:... " + primaryKey);
		Query query;
		query = entityManager.createNamedQuery("CriteriosInusualidad.findByPrimaryKey", CriteriosInusualidad.class);
		query.setParameter("id", primaryKey);
		CriteriosInusualidad criteriosInusualidad = (CriteriosInusualidad) query.getSingleResult();
		this.criteriosInusualidad = criteriosInusualidad;
		System.out.println("criteriosInusualidad: " + criteriosInusualidad);
		return criteriosInusualidad;
	}

	public CriteriosInusualidad getCriteriosInusualidad() {
		return this.criteriosInusualidad;
	}
}
