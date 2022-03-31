package admin;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.rmi.RemoteException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import javax.ejb.FinderException;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;

import javax.sql.DataSource;

@Stateless(name = "HistoricoUsuarioCargoEJB", mappedName = "SARLAFT-EJB-HistoricoUsuarioCargoEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class HistoricoUsuarioCargoEJBBean implements HistoricoUsuarioCargoEJB {
    private EntityContext context;
    private Connection sesionBD;
    private TablaBD miTabla;
    private HistoricoUsuarioCargo historicoUsuarioCargo;

    public Long create(Hashtable pDatos) {
        Long codUsuario = (Long) pDatos.get("CODIGO_USUARIO");
        String codCargo = (String) pDatos.get("CODIGO_CARGO");
        Integer act = (Integer) pDatos.get("ACTIVO");
        Long usuarioAct = (Long) pDatos.get("USUARIO_ACTUALIZACION");
        Timestamp fechaAct = (Timestamp) pDatos.get("FECHA_ACTUALIZACION");
        this.historicoUsuarioCargo = new HistoricoUsuarioCargo();
        this.historicoUsuarioCargo.setActivo(act);
        this.historicoUsuarioCargo.setCodigoCargo(codCargo);
        this.historicoUsuarioCargo.setCodigoUsuario(codUsuario);
        this.historicoUsuarioCargo.setFechaActualizacion(fechaAct);
        this.historicoUsuarioCargo.setUsuarioActualizacion(usuarioAct);
        this.historicoUsuarioCargo.setId(this.buscarId());
        return this.historicoUsuarioCargo.getId();
    }

    public void setEntityContext(EntityContext ctx) {
        this.context = ctx;
    }

    public void unsetEntityContext() {
        this.context = null;
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


    private Long buscarId() {
        Long id = null;
        try {
            this.establecerConexion();
            miTabla = new TablaBD("HISTORICO_USUARIO_CARGO", "ID", this.sesionBD);
            Integer tId = miTabla.traerIDSecuencia("SEQ_HISTORICO_USUARIO_CARGO");
            id = new Long(tId.longValue());
        } catch (Exception error) {
            System.out.println("HistoricoUsuarioCargoEJB|buscarId (1): " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("HistoricoUsuarioCargoEJB|buscarId (2): " + e.getMessage());
            }
        }
        return id;
    }

    @Override
    public HistoricoUsuarioCargo findByPrimaryKey(Long primaryKey) throws FinderException {
        // TODO Implement this method
        return null;
    }

    @Override
    public Collection findAll() throws FinderException {
        // TODO Implement this method
        return Collections.emptySet();
    }

    @Override
    public void setHistoricoUsuarioCargo(HistoricoUsuarioCargo historicoUsuarioCargo) {
        this.historicoUsuarioCargo = historicoUsuarioCargo;
    }

    @Override
    public HistoricoUsuarioCargo getHistoricoUsuarioCargo() {
        return this.historicoUsuarioCargo;
    }
}
