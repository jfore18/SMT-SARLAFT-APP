package transaccion;

import javax.ejb.EntityContext;

import java.sql.Date;

import java.util.*;

import java.sql.*;

import javax.ejb.FinderException;

import javax.sql.*;

import javax.naming.InitialContext;

import baseDatos.*;

import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.transaction.UserTransaction;

@Stateless(name = "ComentarioEJB", mappedName = "SARLAFT-EJB-ComentarioEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class ComentarioEJBBean implements ComentarioEJB {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;
    private TablaBD miTabla;
    transient private Connection sesionBD;
    private EntityContext context;
    static final String PERFIL_ANALISTA = "5";

    private Comentario comentario;

    public Integer create(Hashtable pDatos) {
        this.comentario = new Comentario();
        this.comentario.setId(traerIdComentario()); //PENDIENTE VERIFICAR ***
        this.comentario.setCodigoArchivo((Integer) pDatos.get("CODIGO_ARCHIVO"));
        this.comentario.setFechaProceso((Date) pDatos.get("FECHA_PROCESO"));
        this.comentario.setIdTransaccion((Integer) pDatos.get("ID_TRANSACCION"));
        this.comentario.setComentario((String) pDatos.get("COMENTARIO"));
        this.comentario.setUsuarioCreacion((Long) pDatos.get("USUARIO_CREACION"));
        this.comentario.setFechaCreacion((Timestamp) pDatos.get("FECHA_CREACION"));

        String codigoPerfil = (String) pDatos.get("PERFIL");
        String colNoComent = (codigoPerfil.equals(PERFIL_ANALISTA) ? "NO_COMENTARIOS_DUCC" : "NO_COMENTARIOS");

        String actualizarTr =
            "UPDATE TRANSACCIONES_CLIENTE SET " + colNoComent + " = " + colNoComent + " + 1 " + "WHERE " +
            " CODIGO_ARCHIVO = " + this.comentario.getCodigoArchivo() + " AND FECHA_PROCESO = TO_DATE('" +
            CatalogoBD.formatoFecha.format(this.comentario.getFechaProceso()) + "','YYYY/MM/DD')" + " AND ID = " +
            this.comentario.getIdTransaccion();

        //System.out.println("sentencia Comentario" + actualizarTr);
        try {
            this.establecerConexion();
            Statement stmtActualizarTr = sesionBD.createStatement();
            stmtActualizarTr.executeUpdate(actualizarTr);
            stmtActualizarTr.close();
        } catch (Exception ex) {
            throw new EJBException("ComentarioBean|ejbCreate: " + ex.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("ComentarioEJB|ejbCreate: " + e.getMessage());
            }
        }
        this.guardarComentario(this.comentario);
        return this.comentario.getId();
    }

    public void guardarComentario(Comentario comentario) {
        if (sessionContext != null) {
            UserTransaction transaction = sessionContext.getUserTransaction();
            try {
                transaction.begin();
                entityManager.persist(comentario);
                transaction.commit();
            } catch (Exception e) {
                if (transaction == null) {
                    throw new EJBException("ComentarioEJB|guardarComentario: transacción null ");
                }
                comentario = null;
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new EJBException("ComentarioEJB|guardarComentario: " + ex.getMessage());
                }
            }
        } else {
            throw new EJBException("ComentarioEJB|guardarComentario: sessionContext null");
        }
    }

    public void setEntityContext(EntityContext ctx) {
        this.context = ctx;
    }

    public void unsetEntityContext() {
        this.context = null;
    }

    public Comentario getComentario() {
        return this.comentario;
    }

    public void establecerConexion() throws Exception {
        this.cerrarConexion();

        DataSource ds = null;
        this.sesionBD = null;

        InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(CatalogoBD.urlDataSource);
        this.sesionBD = ds.getConnection();
    }

    public void cerrarConexion() throws SQLException {
        if (this.sesionBD != null && !this.sesionBD.isClosed()) {
            this.sesionBD.close();
            this.sesionBD = null;
        }
    }


    private Integer traerIdComentario() {
        Integer id = null;
        try {
            this.establecerConexion();
            miTabla = new TablaBD("COMENTARIOS", "ID", this.sesionBD);
            id = miTabla.traerIDSecuencia("SEQ_COMENTARIO");
        } catch (Exception error) {
            System.out.println("ComentarioEJB|traerIdComentario (1): " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("ComentarioEJB|traerIdComentario (2): " + e.getMessage());
            }
        }
        return id;
    }

    @Override
    public ComentarioEJB findByPrimaryKey(Integer primaryKey) throws FinderException {
        // TODO Implement this method
        return null;
    }

    @Override
    public Collection findAll() throws FinderException {
        // TODO Implement this method
        return Collections.emptySet();
    }
}
