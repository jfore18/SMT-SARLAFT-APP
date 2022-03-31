package admin.usuario;

import admin.Contextos;

//import com.bea.core.repackaged.springframework.transaction.annotation.Transactional;

import baseDatos.ConsultaTablaEJB;

import java.security.MessageDigest;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.Hashtable;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.NamingException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;

import javax.transaction.UserTransaction;

import sun.misc.BASE64Encoder;

@Stateless(name = "UsuarioEJB", mappedName = "SMT_JSF")
@TransactionManagement(TransactionManagementType.BEAN)
public class UsuarioEJBBean implements UsuarioEJB {
    @Resource
    SessionContext sessionContext;
    @EJB
    ConsultaTablaEJB consultaTablaEJB;
    @EJB
    UsuarioEJB usuarioEJB;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;

    private Usuario usuario;

    public UsuarioEJBBean() {
    }

    public Long create(Hashtable pDatos) {

        Long usuario = (Long) pDatos.get("USUARIO_CREACION");
        if (usuario == null) {
            throw new EJBException("UsuarioEJB|Crear: usuario creacion nulo");
        }
        try {
            Usuario usuarioLocal = usuarioEJB.findByPrimaryKey(new Long(usuario.longValue()));
            if (usuarioLocal == null) {
                throw new EJBException("UsuarioEJB|Crear: usuario creacion no existe");
            }
        } catch (Exception e) {
            throw new EJBException("UsuarioEJB|Crear " + e.getMessage());
        }
        this.usuario = new Usuario();
        this.usuario.setCedula((Long) pDatos.get("CEDULA"));
        this.usuario.setActivo((Integer) pDatos.get("ACTIVO"));
        this.usuario.setDominioUsuario((String) pDatos.get("DOMINIO"));
        this.usuario.setFechaCreacion((Timestamp) pDatos.get("FECHA_CREACION"));
        this.usuario.setUsuarioCreacion(usuario);
        this.usuario.setNombre((String) pDatos.get("NOMBRE"));

        if (pDatos.containsKey("PASSWORD")) {
            this.establecerPwd((String) pDatos.get("PASSWORD"));
        }

        this.guardarUsuario(this.usuario);

        return this.usuario.getCedula();
    }

    // @ TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void guardarUsuario(Usuario usuario) {
        UserTransaction transaction = sessionContext.getUserTransaction();
        try {
            transaction.begin();
            entityManager.persist(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction == null) {
                throw new EJBException("UsuarioEJB|guardarUsuario: transacción null ");
            }
            usuario = null;
            // e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new EJBException("UsuarioEJB|guardarUsuario: " + ex.getMessage());
            }
        }
    }

    public void actualizarUsuario(Usuario usuario) {
        UserTransaction transaction = sessionContext.getUserTransaction();
        try {
            transaction.begin();
            entityManager.merge(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction == null) {
                throw new EJBException("UsuarioEJB|actualizarUsuario: transacción null ");
            }
            usuario = null;
            // e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new EJBException("UsuarioEJB|actualizarUsuario: " + ex.getMessage());
            }
        }
    }

    public Usuario findByPrimaryKey(Long cedula) throws Exception {
    	Query query;
        query = entityManager.createNamedQuery("Usuario.findByPrimaryKey", Usuario.class);
        query.setParameter("cedula", cedula);
        Usuario usuario = (Usuario) query.getSingleResult();
        this.usuario = usuario;
        return usuario;
    }

    private UsuarioEJB getUsuarioEJB() throws NamingException {
        Contextos ctx = new Contextos();
        UsuarioEJB usuarioEJB = (UsuarioEJB) ctx.getContext("UsuarioEJB");
        return usuarioEJB;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void establecerPwd(String newPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(newPassword.getBytes());

            byte[] raw = md.digest();

            BASE64Encoder encoder = new BASE64Encoder();
            this.usuario.setPassword(encoder.encode(raw));
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    @Override
    public Collection buscarUsuario(String dominioNT, String usuarioNT) throws Exception {
        String consulta = "WHERE DOMINIO_USUARIO = '" + dominioNT.toUpperCase() + "\\" + usuarioNT.toUpperCase() + "' AND CARGO_ACTIVO='1'";
        Collection resultados = consultaTablaEJB.consultarTabla(0, 0, "V_USUARIOS", consulta);
        return resultados;
    }
}
