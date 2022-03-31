package admin;

import baseDatos.CatalogoBD;
import baseDatos.ConsultaTablaEJB;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;

import javax.naming.NamingException;

import javax.sql.DataSource;

@Stateless(name = "ListaValoresEJB", mappedName = "SARLAFT-EJB-ListaValoresEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class ListaValoresEJBBean implements ListaValoresEJB {
    private TablaBD miTabla;
    transient private Connection sesionBD;

    public ListaValoresEJBBean() {
    }

    @Override
    public boolean crearValor(Hashtable pDatos) throws SQLException {
        boolean exito = false;
        try {
            this.establecerConexion();
            this.miTabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", this.sesionBD);
            this.miTabla.asignarValorColumna("TIPO_DATO", (Integer) pDatos.get("TIPO_DATO"));
            this.miTabla.asignarValorColumna("CODIGO", (String) pDatos.get("CODIGO"));
            this.miTabla.asignarValorColumna("NOMBRE_CORTO", (String) pDatos.get("NOMBRE_CORTO"));
            this.miTabla.asignarValorColumna("NOMBRE_LARGO", (String) pDatos.get("NOMBRE_LARGO"));
            this.miTabla.asignarValorColumna("APLICA_GERENTE", (String) pDatos.get("APLICA_GERENTE"));
            this.miTabla.asignarValorColumna("ACTIVO", (Integer) pDatos.get("ACTIVO"));
            this.miTabla.asignarValorColumna("FECHA_ACTUALIZACION", (Timestamp) pDatos.get("FECHA_ACTUALIZACION"));
            this.miTabla.asignarValorColumna("USUARIO_ACTUALIZACION", (Long) pDatos.get("USUARIO_ACTUALIZACION"));
            this.miTabla.insertarDatos();
            exito = true;
        } catch (Exception error) {
            throw new EJBException("ListaValoresEJB|CrearValor: " + error.getMessage());
        } finally {
            this.cerrarConexion();
        }
        return exito;
    }

    @Override
    public Collection listarValores(String pTipoDato) throws SQLException {
        try {
            this.establecerConexion();
            ConsultaTablaEJB consulta = this.getConsultaTablaEJB();
            return consulta.consultarTabla(0, 0, "LISTA_VALORES",
                                           "WHERE TIPO_DATO = " + pTipoDato + " ORDER BY CODIGO");
        } catch (Exception error) {
            throw new EJBException("ListaValoresEJB|listarValores: " + error.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void actualizarValor(Hashtable pDatos) throws SQLException {
        try {
            this.establecerConexion();
            this.miTabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", this.sesionBD);
            this.miTabla.asignarValorColumna("TIPO_DATO", (Integer) pDatos.get("TIPO_DATO"));
            this.miTabla.asignarValorColumna("CODIGO", (String) pDatos.get("CODIGO"));

            if (this.miTabla.buscarPK()) {
                this.miTabla.consultarDatos();
                this.miTabla.asignarValorColumna("NOMBRE_CORTO", (String) pDatos.get("NOMBRE_CORTO"));
                this.miTabla.asignarValorColumna("NOMBRE_LARGO", (String) pDatos.get("NOMBRE_LARGO"));
                this.miTabla.asignarValorColumna("APLICA_GERENTE", (String) pDatos.get("APLICA_GERENTE"));
                this.miTabla.asignarValorColumna("ACTIVO", (Integer) pDatos.get("ACTIVO"));
                this.miTabla.asignarValorColumna("FECHA_ACTUALIZACION", (Timestamp) pDatos.get("FECHA_ACTUALIZACION"));
                this.miTabla.asignarValorColumna("USUARIO_ACTUALIZACION", (Long) pDatos.get("USUARIO_ACTUALIZACION"));


                this.miTabla.actualizarDatos();
            } else {
                throw new EJBException("ListaValoresEJB|actualizarValor: No se puede actualizar el dato, porque no existe");
            }
        } catch (Exception error) {
            throw new EJBException("ListaValoresEJB|ActualizarValor: " + error.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    private ConsultaTablaEJB getConsultaTablaEJB() throws NamingException {
        Contextos ctx = new Contextos();
        return (ConsultaTablaEJB) ctx.getContext("ConsultaTablaEJB");
    }

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
