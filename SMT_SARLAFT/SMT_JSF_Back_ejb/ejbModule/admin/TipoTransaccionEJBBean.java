package admin;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;

import javax.sql.DataSource;

@Stateless(name = "TipoTransaccionEJB", mappedName = "SARLAFT-EJB-TipoTransaccionEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class TipoTransaccionEJBBean implements TipoTransaccionEJB {
    private TablaBD miTabla;
    transient private Connection sesionBD;

    public void crear(Hashtable pDatos) throws SQLException {
        try {
            String codigoProducto = (String) pDatos.get("CODIGO_PRODUCTO_V");
            String codigoTransaccion = (String) pDatos.get("CODIGO_TRANSACCION");
            String descripcion = (String) pDatos.get("DESCRIPCION");
            boolean mayorRiesgo = ((Boolean) pDatos.get("MAYOR_RIESGO")).booleanValue();
            String naturaleza = (String) pDatos.get("NATURALEZA");

            this.establecerConexion();
            miTabla = new TablaBD("TIPOS_TRANSACCION", "CODIGO_PRODUCTO_V,CODIGO_TRANSACCION", this.sesionBD);

            miTabla.asignarValorColumna("CODIGO_PRODUCTO_V", codigoProducto);
            miTabla.asignarValorColumna("CODIGO_TRANSACCION", codigoTransaccion);
            try {
                boolean existe = miTabla.buscarPK();
                if (existe) {
                    throw new Exception("No se puede crear el tipo de transacción, ya existe");
                }
            } catch (Exception noExiste) {
            }
            miTabla.asignarValorColumna("DESCRIPCION", descripcion);
            miTabla.asignarValorColumna("MAYOR_RIESGO", mayorRiesgo);
            miTabla.asignarValorColumna("NATURALEZA", naturaleza);

            miTabla.insertarDatos();
        } catch (Exception e) {
            throw new EJBException("TipoTransaccionEJB|crear : " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void actualizar(Hashtable pDatos) throws SQLException {
        try {
            String codigoProducto = (String) pDatos.get("CODIGO_PRODUCTO_V");
            String codigoTransaccion = (String) pDatos.get("CODIGO_TRANSACCION");
            String descripcion = (String) pDatos.get("DESCRIPCION");
            boolean mayorRiesgo = ((Boolean) pDatos.get("MAYOR_RIESGO")).booleanValue();
            String naturaleza = (String) pDatos.get("NATURALEZA");

            this.establecerConexion();
            miTabla = new TablaBD("TIPOS_TRANSACCION", "CODIGO_PRODUCTO_V,CODIGO_TRANSACCION", this.sesionBD);

            miTabla.asignarValorColumna("CODIGO_PRODUCTO_V", codigoProducto);
            miTabla.asignarValorColumna("CODIGO_TRANSACCION", codigoTransaccion);
            try {
                boolean existe = miTabla.buscarPK();
                if (existe) {
                    miTabla.asignarValorColumna("DESCRIPCION", descripcion);
                    miTabla.asignarValorColumna("MAYOR_RIESGO", mayorRiesgo);
                    miTabla.asignarValorColumna("NATURALEZA", naturaleza);

                    miTabla.actualizarDatos();
                } else {
                    throw new EJBException("TipoTransaccionEJB|actualizar: El tipo de transaccion no existe");
                }
            } catch (Exception noExiste) {
                throw new EJBException("TipoTransaccionEJB|actualizar(1): El tipo de transaccion no existe" +
                                       noExiste.getMessage());
            }

        } catch (Exception e) {
            throw new EJBException("TipoTransaccionEJB|actualizar : " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
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
}
