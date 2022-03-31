package transaccion;

import admin.usuario.Usuario;

import java.util.Collection;
import java.util.Collections;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import java.sql.Timestamp;

import javax.ejb.FinderException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Hashtable;

import java.sql.*;

import javax.sql.*;

import baseDatos.CatalogoBD;

//import com.rsa.cryptoj.c.ob;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "ComentarioCargoEJB", mappedName = "SARLAFT-EJB-ComentarioCargoEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class ComentarioCargoEJBBean implements ComentarioCargoEJB {
    private EntityContext context;
    private ComentarioCargoEJBPK pk;
    transient private Connection sesionBD;

    @PersistenceContext(unitName = "Administracion")
    private EntityManager em;

    private ComentarioCargo comentarioCargo;

    public ComentarioCargoEJBPK create(Hashtable pDatos) {
        this.comentarioCargo = new ComentarioCargo();
        this.comentarioCargo.setCodigoCargo((String) pDatos.get("CODIGO_CARGO"));
        this.comentarioCargo.setIdComentario((Integer) pDatos.get("ID_COMENTARIO"));
        this.comentarioCargo.setFechaRevision(null);
        this.comentarioCargo.setUsuarioRevision(null);
        this.pk.codigoCargo = this.comentarioCargo.getCodigoCargo();
        this.pk.idComentario = this.comentarioCargo.getIdComentario();
        return pk;
    }

    public void setComentarioCargo(ComentarioCargo comentarioCargo) {
        this.comentarioCargo = comentarioCargo;
    }

    public ComentarioCargo getComentarioCargo() {
        return this.comentarioCargo;
    }

    public void setEntityContext(EntityContext ctx) {
        this.context = ctx;
    }

    public void unsetEntityContext() {
        this.context = null;
    }

    /**
     * @Métodos de Contexto y conexión a Base de Datos
     */

    private void establecerConexion() throws Exception {
        this.cerrarConexion();

        DataSource ds = null;
        this.sesionBD = null;

        InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(CatalogoBD.urlDataSource);
        this.sesionBD = ds.getConnection();

    }

    private void cerrarConexion() throws SQLException {
        if (this.sesionBD != null && !this.sesionBD.isClosed()) {
            this.sesionBD.close();
            this.sesionBD = null;
        }
    }

    @Override
    public ComentarioCargo findByPrimaryKey(ComentarioCargoEJBPK primaryKey) throws FinderException {
        Query query;
        query = em.createNamedQuery("ComentarioCargo.findByPrimaryKey", ComentarioCargo.class);
        query.setParameter("codigoCargo", primaryKey.getCodigoCargo());
        query.setParameter("idComentario", primaryKey.getIdComentario());

        return (ComentarioCargo) query.getSingleResult();
    }

    @Override
    public Collection findAll() throws FinderException {
        // TODO Implement this method
        return Collections.emptySet();
    }
}
