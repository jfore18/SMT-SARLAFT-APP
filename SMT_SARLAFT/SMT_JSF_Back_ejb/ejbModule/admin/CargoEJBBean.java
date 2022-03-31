package admin;

import admin.usuario.Usuario;
import admin.usuario.UsuarioEJB;

import baseDatos.ConsultaTablaEJB;

//import com.bea.core.repackaged.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import javax.transaction.UserTransaction;

import presentacion.FacadeEJB;

@Stateless(name = "CargoEJB", mappedName = "SARLAFT-EJB-CargoEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class CargoEJBBean implements CargoEJB {

    @Resource
    SessionContext sessionContext;
    @EJB
    UsuarioEJB usuarioEJB;
    @EJB
    FacadeEJB facadeEJB;
    @EJB
    ConsultaTablaEJB consTablaEJB;
    private EntityContext entityContext;
    private Cargo cargo;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;

    public String create(Hashtable pDatos) {
        Long usuario = (Long) pDatos.get("USUARIO_CREACION");
        if (usuario == null) {
            throw new EJBException("CargoEJB|Crear: usuario creacion nulo");
        }
        try {
            usuarioEJB.findByPrimaryKey(new Long(usuario.longValue()));
            if (usuarioEJB.getUsuario() == null) {
                throw new EJBException("CargoEJB|Crear: usuario creacion no existe");
            }
        } catch (Exception e) {
            throw new EJBException("CargoEJB|Crear " + e.getMessage());
        }
        String codigoCargo = (String) pDatos.get("CODIGO_CARGO");
        System.out.println("codigoCargo: " + codigoCargo);
        String consultaConsecutivo =
            "SELECT MAX(SUBSTR(CODIGO, 6,1)) + 1 \"ID\" FROM CARGOS " + " WHERE CODIGO LIKE '" + codigoCargo + "%'";
        System.out.println("consultaConsecutivo: " + consultaConsecutivo);
        try {
            String sufijo = "";
            Collection resultados = consTablaEJB.consultarTabla(0, 0, null, consultaConsecutivo);
            System.out.println("resultados: " + resultados);
            if (resultados.size() == 0) {
                codigoCargo += "1";
            } else {
                Iterator it = resultados.iterator();
                while (it.hasNext()) {
                    Hashtable tablaTemp = (Hashtable) it.next();
                    sufijo = (String) tablaTemp.get("ID");
                    sufijo = sufijo.equals("null") ? "1" : sufijo;
                    //System.out.println("sufijo: " + sufijo);
                    if (Integer.parseInt(sufijo) > 9) {
                        throw new Exception("CargoEJB|No se puede crear el cargo, no hay mas códigos disponibles");
                    }
                }
                codigoCargo += sufijo;
            }

        } catch (Exception ex) {
            System.out.println("CargoEJB|CrearCargo: " + ex.getMessage());
            codigoCargo += "1";
        }
        System.out.println("codigoCargo: " + codigoCargo);
        this.cargo = new Cargo();
        this.cargo.setActivo((Integer) pDatos.get("ACTIVO"));
        this.cargo.setCodigo(codigoCargo);
        this.cargo.setCodigoPerfilV((String) pDatos.get("CODIGO_PERFIL"));
        this.cargo.setCodigoTipoCargoV((String) pDatos.get("CODIGO_TIPO_CARGO"));
        this.cargo.setCodigoUnidadNegocio((Integer) pDatos.get("CODIGO_UNIDAD_NEGOCIO"));
        this.cargo.setCodigoUsuario((Long) pDatos.get("CODIGO_USUARIO"));
        this.cargo.setFechaCreacion((Timestamp) pDatos.get("FECHA_CREACION"));
        this.cargo.setUsuarioCreacion(usuario);
        System.out.println("codigoCargo*: " + this.cargo.getCodigo());
        guardarCargo(this.cargo);
        return this.cargo.getCodigo();
    }

    // @ TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void guardarCargo(Cargo cargo) {
        UserTransaction transaction = sessionContext.getUserTransaction();
        try {
            transaction.begin();
            entityManager.persist(cargo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction == null) {
                throw new EJBException("CargoEJB|guardarCargo: transacción null ");
            }
            cargo = null;
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new EJBException("CargoEJB|guardarCargo: " + ex.getMessage());
            }
        }
    }

    @Override
    public void actualizarCargo(Cargo cargo) {
        UserTransaction transaction = sessionContext.getUserTransaction();
        try {
            transaction.begin();
            entityManager.merge(cargo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction == null) {
                throw new EJBException("CargoEJB|actualizarCargo: transacción null ");
            }
            cargo = null;
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new EJBException("CargoEJB|actualizarCargo: " + ex.getMessage());
            }
        }
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return this.cargo;
    }

    @Override
    public void establecerUsuario(Long pCedula) throws Exception {
        try {
            usuarioEJB.findByPrimaryKey(pCedula);
            if (usuarioEJB.getUsuario() == null) {
                throw new EJBException("CargoEJB|establecerUsuario: usuario no existe");
            }
            this.cargo.setCodigoUsuario(pCedula);
            this.actualizarCargo(this.cargo);
            /* Activar flag de confirmacion de filtros del cargo actual */
            facadeEJB.marcarFiltrosCambioUsuario(this.cargo.getCodigo());
        } catch (Exception e) {
            throw new EJBException("CargoEJB|establecerUsuario:" + e.getMessage());
        }
    }

    @Override
    public Cargo findByPrimaryKey(String primaryKey) throws FinderException {
        Query query;
        query = entityManager.createNamedQuery("Cargo.findByPrimaryKey", Cargo.class);
        query.setParameter("codigo", primaryKey);
        Cargo cargo = (Cargo) query.getSingleResult();
        this.cargo = cargo;
        return cargo;
    }

    @Override
    public Collection findAll() throws FinderException {
        // TODO Implement this method
        return Collections.emptySet();
    }
}
