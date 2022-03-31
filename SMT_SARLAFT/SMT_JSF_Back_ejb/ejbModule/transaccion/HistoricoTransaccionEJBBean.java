package transaccion;

import java.util.Collection;
import java.util.Collections;

import javax.ejb.EntityContext;

import java.sql.*;

import javax.ejb.FinderException;

import javax.sql.*;

import javax.naming.InitialContext;

import java.util.Hashtable;

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

import admin.HistoricoUsuarioCargo;

@Stateless(name = "HistoricoTransaccionEJB", mappedName = "SARLAFT-EJB-HistoricoTransaccionEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class HistoricoTransaccionEJBBean implements HistoricoTransaccionEJB {
    private EntityContext context;
    private Connection sesionBD;
    private TablaBD miTabla;
    private HistoricoTransaccion historicoTransaccion;
    
    

    @Override
    public Integer create(Hashtable pDatos) {
        this.historicoTransaccion = new HistoricoTransaccion();
        this.historicoTransaccion.setId(traerIdReporte());
        this.historicoTransaccion.setCodigoArchivo((Integer) pDatos.get("CODIGO_ARCHIVO"));
        this.historicoTransaccion.setCodigoCargo((String) pDatos.get("CODIGO_CARGO"));
        this.historicoTransaccion.setCodigoEstadoV((String) pDatos.get("CODIGO_ESTADO"));
        this.historicoTransaccion.setFechaActualizacion((Date) pDatos.get("FECHA_ACTUALIZACION"));
        this.historicoTransaccion.setFechaProceso((Date) pDatos.get("FECHA_PROCESO"));
        this.historicoTransaccion.setIdTransaccion((Integer) pDatos.get("ID_TRANSACCION"));
        this.historicoTransaccion.setUsuarioActualizacion((Long) pDatos.get("USUARIO_ACTUALIZACION"));
        return this.historicoTransaccion.getId();
    }

  

    @Override
    public HistoricoTransaccionEJB findByPrimaryKey(Integer primaryKey) throws FinderException {
        // TODO Implement this method
        return null;
    }

    @Override
    public Collection findAll() throws FinderException {
        // TODO Implement this method
        return Collections.emptySet();
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

    private Integer traerIdReporte() {
        Integer idReporte = null;
        try {
            this.establecerConexion();
            miTabla = new TablaBD("HISTORICO_ESTADOS_TR", "ID", this.sesionBD);
            idReporte = miTabla.traerIDSecuencia("SEQ_HISTORICO_ESTADO");
        } catch (Exception error) {
            System.out.println("HistoricoTxnEJB|traerIdReporte1: " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("HistoricoTxnEJB|traerIdReporte2: " + e.getMessage());
            }
        }
        return idReporte;
    }


}
