package admin;

import baseDatos.CatalogoBD;
import baseDatos.TablaBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;

import javax.sql.DataSource;

@Stateless(name = "PreguntasEJB", mappedName = "SARLAFT-EJB-PreguntasEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class PreguntasEJBBean implements PreguntasEJB {
    private TablaBD miTabla;
    transient private Connection sesionBD;

    public void PreguntasEJBBean() {
    }

    public void adicionarPregunta(Hashtable pDatos) throws SQLException {
        try {
            this.establecerConexion();
            this.miTabla = new TablaBD("PREGUNTAS_REP", "ID", this.sesionBD);
            Integer id = this.miTabla.traerIDSecuencia("SEQ_PREGUNTAS_REP");
            this.miTabla.asignarValorColumna("ID", id);
            this.miTabla.asignarValorColumna("DESCRIPCION", (String) pDatos.get("DESCRIPCION"));
            this.miTabla.asignarValorColumna("VIGENTE_DESDE", (Timestamp) pDatos.get("VIGENTE_DESDE"));
            this.miTabla.asignarValorColumna("VIGENTE_HASTA", (Timestamp) pDatos.get("VIGENTE_HASTA"));
            this.miTabla.asignarValorColumna("CODIGO_PERFIL_V", (String) pDatos.get("CODIGO_PERFIL_V"));
            this.miTabla.asignarValorColumna("TIPO_PREGUNTA_V", (String) pDatos.get("TIPO_PREGUNTA_V"));
            this.miTabla.asignarValorColumna("USUARIO_ACTUALIZACION", (Long) pDatos.get("USUARIO_ACTUALIZACION"));
            this.miTabla.asignarValorColumna("FECHA_ACTUALIZACION", (Timestamp) pDatos.get("FECHA_ACTUALIZACION"));
            this.miTabla.insertarDatos();
        } catch (Exception e) {
            throw new EJBException("PreguntasEJB|adicionarPregunta: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void actualizarPregunta(Hashtable pDatos) throws SQLException {

        try {
            this.establecerConexion();
            this.miTabla = new TablaBD("PREGUNTAS_REP", "ID", this.sesionBD);

            this.miTabla.asignarValorColumna("ID", (Integer) pDatos.get("ID"));
            this.miTabla.asignarValorColumna("DESCRIPCION", (String) pDatos.get("DESCRIPCION"));

            if (this.miTabla.buscarPK()) {

                this.miTabla.consultarDatos();

                this.miTabla.asignarValorColumna("VIGENTE_DESDE", (Timestamp) pDatos.get("VIGENTE_DESDE"));
                this.miTabla.asignarValorColumna("VIGENTE_HASTA", (Timestamp) pDatos.get("VIGENTE_HASTA"));
                this.miTabla.asignarValorColumna("CODIGO_PERFIL_V", (String) pDatos.get("CODIGO_PERFIL_V"));
                this.miTabla.asignarValorColumna("TIPO_PREGUNTA_V", (String) pDatos.get("TIPO_PREGUNTA_V"));
                this.miTabla.asignarValorColumna("DESCRIPCION", (String) pDatos.get("DESCRIPCION"));
                this.miTabla.asignarValorColumna("USUARIO_ACTUALIZACION", (Long) pDatos.get("USUARIO_ACTUALIZACION"));
                this.miTabla.asignarValorColumna("FECHA_ACTUALIZACION", (Timestamp) pDatos.get("FECHA_ACTUALIZACION"));

                this.miTabla.actualizarDatos();
            } else {
                throw new EJBException("PreguntasEJB|actualizarPregunta: No se puede actualizar la pregunta, porque no existe");
            }
        } catch (Exception e) {
            throw new EJBException("PreguntasEJB|actualizarPregunta: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public Hashtable consultarTiposPregunta() throws SQLException {
        try {
            Hashtable resultados = new Hashtable();
            this.establecerConexion();
            String consulta =
                "SELECT TIPO_PREGUNTA, DESCRIPCION_TIPO_PREGUNTA " + " FROM V_PREGUNTAS " +
                " GROUP BY TIPO_PREGUNTA, DESCRIPCION_TIPO_PREGUNTA";
            Statement st = this.sesionBD.createStatement();
            ResultSet rsResultados = st.executeQuery(consulta);
            while (rsResultados.next()) {
                resultados.put(rsResultados.getString("TIPO_PREGUNTA"),
                               rsResultados.getString("DESCRIPCION_TIPO_PREGUNTA"));
            }
            rsResultados.close();
            st.close();

            return resultados;
        } catch (Exception e) {
            throw new EJBException("PreguntasEBJ|consultarTiposPreguntas: " + e.getMessage());
        } finally {
            this.cerrarConexion();
        }
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
