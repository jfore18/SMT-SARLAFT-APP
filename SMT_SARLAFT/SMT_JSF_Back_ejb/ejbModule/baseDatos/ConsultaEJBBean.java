package baseDatos;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.sql.DataSource;

@Stateless(name = "ConsultaEJB", mappedName = "SMT_JSF")
public class ConsultaEJBBean implements ConsultaEJB {
    @Resource
    private SessionContext ctx;
    private Consulta consulta;
    private Connection sesionBD;
    private Usuario usuario;

    public ConsultaEJBBean() {
    }

    @Override
    public Consulta realizarConsulta(String pUsuario, String pCodigoConsulta) {
        try {
            this.establecerConexion();
            traerDatosUsuario(pUsuario);
            traerDatosConsulta(pCodigoConsulta);
            System.out.println("ConsultaEJB|realizarConsulta|consulta: " + consulta);
            return this.consulta;
        } catch (SQLException sqlError) {
            System.out.println("ConsultaEJB|realizarConsulta|SQLException: ");
            sqlError.printStackTrace();
            throw new EJBException("ConsultaBean|realizarConsultaSQLException" + sqlError.getMessage());
        } catch (Exception error) {
            System.out.println("ConsultaEJB|realizarConsulta|Exception: ");
            error.printStackTrace();
            throw new EJBException("ConsultaBean|realizarConsultaException" + error.getMessage());
        } finally {
            System.out.println("ConsultaEJB|realizarConsulta|finally: ");
            this.cerrarConexion();
        }
    }

    public Consulta realizarConsultaTipoCargo(String cargo, String pCodigoConsulta) {
        try {
            this.establecerConexion();
            traerDatosUsuario(cargo);
            traerDatosConsultaTipoCargo(pCodigoConsulta);
            return this.consulta;
        } catch (SQLException sqlError) {
            throw new EJBException("ConsultaBean|realizarConsultaSQLException" + sqlError.getMessage());
        } catch (Exception error) {
            throw new EJBException("ConsultaBean|realizarConsultaException" + error.getMessage());
        } finally {
            this.cerrarConexion();
        }
    }

    public void traerDatosConsulta(String pCodigoConsulta) throws Exception, SQLException {
        System.out.println("ConsultaEJB|traerDatosConsulta|");
        consulta = new Consulta();
        String sentencia =
            "SELECT TABLA, COLUMNA, ATRIBUTO, CONECTOR, CONECTOR_FINAL, ALIAS, GROUP_BY " + " FROM VISIBILIDAD " +
            " WHERE ID_CONSULTA = " + pCodigoConsulta + " AND TIPO_CARGO IS NULL AND ( CODIGO_PERFIL_V = '" +
            usuario.getCodigoPerfil() + "' OR " + " CODIGO_PERFIL_V = '*' ) " + " ORDER BY ORDEN ASC";

        String[] seleccion = new String[CatalogoBD.numeroColumnasConsulta];
        String[] tabla = new String[CatalogoBD.numeroColumnasConsulta];
        String[] condicion = new String[CatalogoBD.numeroColumnasConsulta];
        String[] conector = new String[CatalogoBD.numeroColumnasConsulta];
        String[] conectorFinal = new String[CatalogoBD.numeroColumnasConsulta];
        String[] alias = new String[CatalogoBD.numeroColumnasConsulta];
        String[] agrupacion = new String[CatalogoBD.numeroColumnasConsulta];

        Statement st = sesionBD.createStatement();
        ResultSet resultados = st.executeQuery(sentencia);
        int indice = 0;

        while (resultados.next()) {
            try {
                tabla[indice] = resultados.getString("TABLA");
            } catch (Exception e) {
                tabla[indice] = "";
            }
            try {
                if (resultados.getString("GROUP_BY").equalsIgnoreCase("1")) {
                    agrupacion[indice] = resultados.getString("COLUMNA");
                }
            } catch (Exception e) {
                agrupacion[indice] = "";
            }
            try {
                if (resultados.getString("ALIAS") != null) {
                    seleccion[indice] = resultados.getString("COLUMNA") + " ";
                    alias[indice] = resultados.getString("ALIAS");
                } else {
                    seleccion[indice] = resultados.getString("COLUMNA") + " ";
                    alias[indice] = null;
                }
            } catch (Exception e) {
                seleccion[indice] = "";
                alias[indice] = "";
            }
            try {
                condicion[indice] = resultados.getString("ATRIBUTO");
                conector[indice] = resultados.getString("CONECTOR");
                conectorFinal[indice] = resultados.getString("CONECTOR_FINAL");
            } catch (Exception e) {
                condicion[indice] = "";
                conector[indice] = "";
                conectorFinal[indice] = "";
            }

            indice++;
        }
        int i = 0;
        boolean agrupacionInicial = true;

        for (i = 0; i < indice; i++) {
            if (agrupacion[i] != null && !agrupacion[i].equals("")) {
                if (agrupacionInicial) {
                    consulta.setAgrupacion(" GROUP BY " + agrupacion[i]);
                    agrupacionInicial = false;
                } else {
                    consulta.setAgrupacion(consulta.getAgrupacion() + ", " + agrupacion[i]);
                }
            }
            if (i == 0) {
                if (condicion[i] != null) {
                    consulta.setCondicion(conector[i] + " " + seleccion[i] + procesarCondicion(condicion[i]) +
                                          (conectorFinal[i] != null ? conectorFinal[i] : " "));
                }
                consulta.setSeleccion("SELECT " + seleccion[i] + " \"" + alias[i] + "\"");
                consulta.setTabla("FROM " + tabla[i]);
                continue;
            }
            if (alias[i] != null && !alias[i].equals("")) {
                consulta.setSeleccion(consulta.getSeleccion() + ", " + seleccion[i] + " \"" + alias[i] + "\"");
            }
            if (condicion[i] != null && !condicion[i].equals("")) {
                consulta.setCondicion(consulta.getCondicion() + " " + conector[i] + " " + seleccion[i] + " " +
                                      procesarCondicion(condicion[i]) +
                                      (conectorFinal[i] != null ? conectorFinal[i] : " "));
            }
            if (tabla[i] != null && !tabla[i].equals("")) { //&& !(tabla[i].trim()).equals(tabla[i-1].trim() )){
                boolean repetido = false;
                for (int entero = 0; entero < i; entero++) {
                    if (tabla[entero].equalsIgnoreCase(tabla[i])) {
                        repetido = true;
                    }
                }
                if (!repetido) {
                    consulta.setTabla(consulta.getTabla() + ", " + tabla[i]);
                }
            }
        }
        if (i > 0) {
            consulta.setAlias(alias);
        }
        if (resultados != null) {
            resultados.close();
        }
        if (st != null) {
            st.close();
        }
        System.out.println("ConsultaEJB|traerDatosUsuario|consulta = " + consulta);
    }

    public void traerDatosConsultaTipoCargo(String pCodigoConsulta) throws Exception, SQLException {
        consulta = new Consulta();
        String sentencia =
            "SELECT TABLA, COLUMNA, ATRIBUTO, CONECTOR, CONECTOR_FINAL, ALIAS, GROUP_BY " + " FROM VISIBILIDAD " +
            " WHERE ID_CONSULTA = " + pCodigoConsulta + " AND ((CODIGO_PERFIL_V = '" + usuario.getCodigoPerfil() +
            "' AND TIPO_CARGO='" + usuario.getCodigoTipoCargo() + "')" + "  OR CODIGO_PERFIL_V = '*' ) " +
            " ORDER BY ORDEN ASC";
        System.out.println("Esta es la sentencia de la consulta especial :" + sentencia);
        String[] seleccion = new String[CatalogoBD.numeroColumnasConsulta];
        String[] tabla = new String[CatalogoBD.numeroColumnasConsulta];
        String[] condicion = new String[CatalogoBD.numeroColumnasConsulta];
        String[] conector = new String[CatalogoBD.numeroColumnasConsulta];
        String[] conectorFinal = new String[CatalogoBD.numeroColumnasConsulta];
        String[] alias = new String[CatalogoBD.numeroColumnasConsulta];
        String[] agrupacion = new String[CatalogoBD.numeroColumnasConsulta];

        //this.establecerConexion();

        Statement st = sesionBD.createStatement();
        ResultSet resultados = st.executeQuery(sentencia);
        int indice = 0;

        while (resultados.next()) {
            try {
                tabla[indice] = resultados.getString("TABLA");
            } catch (Exception e) {
                tabla[indice] = "";
            }
            try {
                if (resultados.getString("GROUP_BY").equalsIgnoreCase("1")) {
                    agrupacion[indice] = resultados.getString("COLUMNA");
                }
            } catch (Exception e) {
                agrupacion[indice] = "";
            }
            try {
                if (resultados.getString("ALIAS") != null) {
                    seleccion[indice] = resultados.getString("COLUMNA") + " ";
                    alias[indice] = resultados.getString("ALIAS");
                } else {
                    seleccion[indice] = resultados.getString("COLUMNA") + " ";
                    alias[indice] = null;
                }
            } catch (Exception e) {
                seleccion[indice] = "";
                alias[indice] = "";
            }
            try {
                condicion[indice] = resultados.getString("ATRIBUTO");
                conector[indice] = resultados.getString("CONECTOR");
                conectorFinal[indice] = resultados.getString("CONECTOR_FINAL");
            } catch (Exception e) {
                condicion[indice] = "";
                conector[indice] = "";
                conectorFinal[indice] = "";
            }

            indice++;
        }
        //    resultados.close();
        //st.close();

        int i = 0;
        boolean agrupacionInicial = true;

        for (i = 0; i < indice; i++) {
            if (agrupacion[i] != null && !agrupacion[i].equals("")) {
                if (agrupacionInicial) {
                    consulta.setAgrupacion(" GROUP BY " + agrupacion[i]);
                    agrupacionInicial = false;
                } else {
                    consulta.setAgrupacion(consulta.getAgrupacion() + ", " + agrupacion[i]);
                }
            }
            if (i == 0) {
                if (condicion[i] != null) {
                    consulta.setCondicion(conector[i] + " " + seleccion[i] + procesarCondicion(condicion[i]) +
                                          (conectorFinal[i] != null ? conectorFinal[i] : " "));
                }
                consulta.setSeleccion("SELECT " + seleccion[i] + " \"" + alias[i] + "\"");
                consulta.setTabla("FROM " + tabla[i]);
                continue;
            }
            if (alias[i] != null && !alias[i].equals("")) {
                consulta.setSeleccion(consulta.getSeleccion() + ", " + seleccion[i] + " \"" + alias[i] + "\"");
            }
            if (condicion[i] != null && !condicion[i].equals("")) {
                consulta.setCondicion(consulta.getCondicion() + " " + conector[i] + " " + seleccion[i] + " " +
                                      procesarCondicion(condicion[i]) +
                                      (conectorFinal[i] != null ? conectorFinal[i] : " "));
            }
            if (tabla[i] != null && !tabla[i].equals("")) { //&& !(tabla[i].trim()).equals(tabla[i-1].trim() )){
                boolean repetido = false;
                for (int entero = 0; entero < i; entero++) {
                    if (tabla[entero].equalsIgnoreCase(tabla[i])) {
                        repetido = true;
                    }
                }
                if (!repetido) {
                    consulta.setTabla(consulta.getTabla() + ", " + tabla[i]);
                }
            }
        }
        if (i > 0) {
            consulta.setAlias(alias);
        }
        if (resultados != null) {
            resultados.close();
        }
        if (st != null) {
            st.close();
        }
        //this.cerrarConexion();
    }

    private void traerDatosUsuario(String pUsuario) throws Exception, SQLException {
        System.out.println("ConsultaEJB|traerDatosUsuario|");
        usuario = new Usuario();
        String consulta = " SELECT * FROM V_USUARIOS WHERE CODIGO_CARGO = '" + pUsuario + "'";
        Statement stConsulta = this.sesionBD.createStatement();
        ResultSet resultados = stConsulta.executeQuery(consulta);
        while (resultados.next()) {
            usuario.setCodigoCargo(resultados.getString("CODIGO_CARGO"));
            if (resultados.getString("CODIGO_PERFIL") != null) {
                usuario.setCodigoPerfil(resultados.getString("CODIGO_PERFIL"));
                usuario.setNombrePerfil(resultados.getString("NOMBRE_PERFIL"));
            }
            if (resultados.getString("CODIGO_REGION") != null) {
                usuario.setCodigoRegion(resultados.getString("CODIGO_REGION"));
                usuario.setNombreRegion(resultados.getString("NOMBRE_REGION"));
                usuario.setNombreCortoRegion(resultados.getString("NOMBRE_CORTO_REGION"));
            }
            usuario.setCodigoTipoCargo(resultados.getString("CODIGO_TIPO_CARGO"));
            usuario.setCodigoUsuario(resultados.getString("CODIGO_USUARIO"));
            if (resultados.getString("CODIGO_ZONA") != null) {
                usuario.setCodigoZona(resultados.getString("CODIGO_ZONA"));
                usuario.setNombreZona(resultados.getString("NOMBRE_ZONA"));
                usuario.setNombreCortoZona(resultados.getString("NOMBRE_CORTO_ZONA"));
            }
            usuario.setNombreTipoCargo(resultados.getString("NOMBRE_TIPO_CARGO"));
            usuario.setNombreUsuario(resultados.getString("NOMBRE_USUARIO"));
            usuario.setCodigoUnidadNegocio(resultados.getString("CODIGO_UNIDAD_NEGOCIO"));
            usuario.setNombreUnidadNegocio(resultados.getString("NOMBRE_UNIDAD_NEGOCIO"));
            usuario.setIsMegabanco(resultados.getString("IS_MEGABANCO"));
        }

        if (resultados != null) {
            resultados.close();
        }
        if (stConsulta != null) {
            stConsulta.close();
        }
        System.out.println("ConsultaEJB|traerDatosUsuario|usuario = " + usuario);
    }

    private String procesarCondicion(String pCondicion) throws SQLException {
        if (pCondicion.equalsIgnoreCase("IGUAL USUARIO")) {
            return " = '" + usuario.getCodigoUsuario() + "' ";
        }
        if (pCondicion.equalsIgnoreCase("IGUAL CARGO")) {
            return " = '" + usuario.getCodigoCargo() + "' ";
        }
        if (pCondicion.equalsIgnoreCase("IGUAL ZONA")) {
            return " = '" + usuario.getCodigoZona() + "' ";
        }
        if (pCondicion.equalsIgnoreCase("IGUAL REGION")) {
            return " = '" + usuario.getCodigoRegion() + "' ";
        }
        if (pCondicion.equalsIgnoreCase("IGUAL UN")) {
            return " = '" + usuario.getCodigoUnidadNegocio() + "' ";
        }
        if (pCondicion.equalsIgnoreCase("IGUAL PERFIL")) {
            return " = '" + usuario.getCodigoPerfil() + "' ";
        }
        if (pCondicion.equalsIgnoreCase("ANALIZA")) {
            return " IN (" + traerRegionesAnalista() + ")";
        }
        if (pCondicion.equalsIgnoreCase("IGUAL BANCO")) {
            return " = " + usuario.getIsMegabanco() + " ";
        }
        return pCondicion;
    }

    public String traerRegionesAnalista() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            //establecerConexion();
            String regiones = "";
            String consulta =
                "SELECT CODIGO_REGION_V " + " FROM ANALISTA_REGION " + " WHERE CODIGO_CARGO = '" +
                usuario.getCodigoCargo() + "'";
            st = sesionBD.createStatement();
            rs = st.executeQuery(consulta);
            while (rs.next()) {
                regiones += (",'" + rs.getString("CODIGO_REGION_V") + "'");
            }
            if (regiones.trim().length() > 0) {
                regiones = regiones.substring(1);
            }

            //      rs.close();
            //st.close();

            return regiones;
        } catch (Exception e) {
            throw new EJBException("ConsultaEJB|traerRegiones: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
                st.close();
            }
            //cerrarConexion();
        }
    }

    public void establecerConexion() throws NamingException, SQLException, Exception {
        this.cerrarConexion();

        DataSource ds = null;
        this.sesionBD = null;

        InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(CatalogoBD.urlDataSource);
        this.sesionBD = ds.getConnection();
    }

    public void cerrarConexion() {
        try {
            if (this.sesionBD != null && !this.sesionBD.isClosed()) {
                this.sesionBD.close();
                this.sesionBD = null;
            }
        } catch (Exception error) {
        }
    }


}
