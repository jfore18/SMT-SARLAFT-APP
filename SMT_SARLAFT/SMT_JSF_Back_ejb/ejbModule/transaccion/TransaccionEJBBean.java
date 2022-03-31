package transaccion;

import java.util.*;

import javax.ejb.SessionContext;

import java.util.Hashtable;

import baseDatos.*;

import javax.ejb.EJBException;

import java.sql.*;

import javax.sql.*;

import javax.naming.InitialContext;

import java.sql.SQLException;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name = "TransaccionEJB", mappedName = "SARLAFT-EJB-TransaccionEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class TransaccionEJBBean implements TransaccionEJB {

    transient private Connection sesionBD;

    public void ejbCreate() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
        try {
            this.cerrarConexion();
        } catch (Exception ex) {

        }
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
    }

    /*public void actualizarCampo(Hashtable pDatos) throws SQLException {
    try{

      Integer idTransaccion = (Integer) pDatos.get("ID");
      Integer codigoArchivo = (Integer) pDatos.get("CODIGO_ARCHIVO");
      Timestamp fechaProceso = (Timestamp) pDatos.get("FECHA_PROCESO");

      //this.establecerConexion();
      this.sesionBD = (Connection)pDatos.get("sesionBD");
      if( sesionBD == null || sesionBD.isClosed()){
        this.establecerConexion();
      }
      pDatos.remove("sesionBD");

      TablaBD miTabla = new TablaBD("TRANSACCIONES_CLIENTE", "ID,FECHA_PROCESO,CODIGO_ARCHIVO", sesionBD);

      miTabla.asignarValorColumna("ID", idTransaccion);
      miTabla.asignarValorColumna("CODIGO_ARCHIVO", codigoArchivo);
      miTabla.asignarValorColumna("FECHA_PROCESO", fechaProceso);
      miTabla.consultarDatos();

      Enumeration llaves = pDatos.keys();

      while ( llaves.hasMoreElements() ){
        String t = (String) llaves.nextElement();
        miTabla.asignarValorColumna(t, pDatos.get(t));
      }
      miTabla.actualizarDatos();

    } catch ( SQLException sqlError){
      throw new EJBException ( "TransaccionEJB|actualizarCampo: " + sqlError.getMessage());
    } catch ( Exception error){
      throw new EJBException ( "TransaccionEJB|actualizarCampo: " + error.getMessage());
    } /*finally{
      this.cerrarConexion();
    }*/
    /*  }*/
    public void actualizarCampo(Hashtable pDatos) throws SQLException {
        System.out.println("==> TransaccionEJBBean|actualizarCampo... ");
        PreparedStatement actSQL = null;
        try {

            Integer idTransaccion = (Integer) pDatos.get("ID");
            Integer codigoArchivo = (Integer) pDatos.get("CODIGO_ARCHIVO");
            Timestamp fechaProceso = (Timestamp) pDatos.get("FECHA_PROCESO");

            this.establecerConexion();
            String actStr = "UPDATE TRANSACCIONES_CLIENTE ";
            String finStr = " WHERE CODIGO_ARCHIVO = ? " + " AND FECHA_PROCESO = ? " + " AND ID = ? ";
            String temp = "";
            Enumeration llaves = pDatos.keys();

            while (llaves.hasMoreElements()) {
                String t = (String) llaves.nextElement();
                if (!t.equalsIgnoreCase("ID") && !t.equalsIgnoreCase("CODIGO_ARCHIVO") &&
                    !t.equalsIgnoreCase("FECHA_PROCESO")) {
                    temp += t + " = ? ,";
                }

            }
            //System.out.println(actStr + "SET " + temp.substring(0, temp.length() - 1) + finStr);
            actSQL = this.sesionBD.prepareStatement(actStr + "SET " + temp.substring(0, temp.length() - 1) + finStr);

            int posicion = 1;

            llaves = pDatos.keys();
            while (llaves.hasMoreElements()) {
                String t = (String) llaves.nextElement();
                if (!t.equalsIgnoreCase("ID") && !t.equalsIgnoreCase("CODIGO_ARCHIVO") &&
                    !t.equalsIgnoreCase("FECHA_PROCESO")) {
                    Object objeto = pDatos.get(t);
                    if (objeto instanceof Timestamp) {
                        actSQL.setTimestamp(posicion, (Timestamp) objeto);
                    } else {
                        actSQL.setString(posicion, objeto.toString());
                    }

                    posicion++;
                }
            }
            actSQL.setInt(posicion, codigoArchivo);
            actSQL.setTimestamp(posicion + 1, fechaProceso);
            actSQL.setInt(posicion + 2, idTransaccion);
            System.out.println("==> TransaccionEJBBean|actualizarCampo: " + actSQL.toString());
            actSQL.execute();
            actSQL.close();
        } catch (SQLException sqlError) {
            throw new EJBException("TransaccionEJB|actualizarCampo: " + sqlError.getMessage());
        } catch (Exception error) {
            throw new EJBException("TransaccionEJB|actualizarCampo: " + error.getMessage());
        } finally {
            if (actSQL != null) {
                actSQL.close();
            }
            this.cerrarConexion();
        }
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

}
