package baseDatos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;

import javax.ejb.Stateless;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.naming.InitialContext;

import javax.sql.DataSource;

@Stateless(name = "ConsultaTablaEJB", mappedName = "SMT_JSF")
@TransactionManagement(TransactionManagementType.BEAN)
public class ConsultaTablaEJBBean implements ConsultaTablaEJB {
    @Resource
    SessionContext ctx;
    private Connection sesionBD;
    private PreparedStatement sentenciaSQL;
    private ResultSet registros;
    private Integer numeroRegistros = new Integer(0);

    public ConsultaTablaEJBBean() {

    }

    public void setSessionContext(SessionContext context) {
        try {
            this.ctx = context;
        } catch (Exception error) {
            throw new EJBException("[SSC|Vista] \n" + error.getMessage());
        }
    }

    public Collection consultarTabla(int pRegistrosConsulta, int pPosicionInicial, String pTabla, String pCondicion,
                                     boolean usarColumnas) throws Exception {
        Collection listaRegistros = new ArrayList();
        StringBuffer txtSql = new StringBuffer();
        try {
            if (pTabla == null) {
                //throw new Exception("[CV|Tabla]: El nombre de la Tabla no puede ser nulo");
                txtSql.append(pCondicion);
            } else {
                txtSql.append(" SELECT * \n");
                txtSql.append(" FROM  " + pTabla + "\n");
                if (pCondicion != null) {
                    txtSql.append(pCondicion);
                }
            }

            this.establecerConexion();

            boolean limitarRegistros = (pRegistrosConsulta != 0) ? true : false;

            int numeroRegistro = 0;

            this.sentenciaSQL =
                this.sesionBD.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT='" + CatalogoBD.patronFormatoFecha +
                                               "'");

            boolean altero = this.sentenciaSQL.execute();
            this.sentenciaSQL.close();
            /*
            Modificación para establecer el lenguaje y el tipo de sort de la base de datos.(Milena)
          */
            this.establecerConexion();
            this.sentenciaSQL = this.sesionBD.prepareStatement("ALTER SESSION SET NLS_LANGUAGE = 'AMERICAN'");
            this.sentenciaSQL.execute();
            //boolean alteroLenguaje = this.sentenciaSQL.execute("ALTER SESSION SET NLS_LANGUAGE = 'AMERICAN'");
            this.sentenciaSQL.close();
            this.establecerConexion();
            this.sentenciaSQL = this.sesionBD.prepareStatement("ALTER SESSION SET NLS_SORT = 'BINARY'");
            this.sentenciaSQL.execute();
            //boolean alteroSort = this.sentenciaSQL.execute("ALTER SESSION SET NLS_SORT = 'BINARY'");
            this.sentenciaSQL.close();
            this.establecerConexion();
            this.sentenciaSQL = this.sesionBD.prepareStatement("ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.'");
            this.sentenciaSQL.execute();
            //boolean alteroFormatoNumerico = this.sentenciaSQL.execute("ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.'");
            this.sentenciaSQL.close();


            //this.sentenciaSQL = this.sesionBD.prepareStatement(txtSql.toString(),ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("consulta: " + txtSql.toString());
            this.establecerConexion();

            this.sentenciaSQL =
                this.sesionBD.prepareStatement(txtSql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE,
                                               ResultSet.CONCUR_READ_ONLY);

            if (this.sentenciaSQL == null) {
                throw new Exception("[CV|Tabla]: La sintaxis SQL puede estar incorrecta. \n " + txtSql);
            }


            this.registros = this.sentenciaSQL.executeQuery();

            if (this.registros == null) {
                throw new Exception("[CV|Tabla]: No se retornaron registros en la consulta. \n " + txtSql);
            }

            registros.last();

            int totalRegistros = registros.getRow();

            if (totalRegistros < pPosicionInicial) {
                registros.close();
                sentenciaSQL.close();
                this.cerrarConexion();
                return null;

            }

            this.numeroRegistros = new Integer(totalRegistros);

            if (pPosicionInicial == 0) {
                registros.beforeFirst();
            } else {
                registros.absolute(pPosicionInicial);
            }

            ResultSetMetaData metaData = registros.getMetaData();


            while (this.registros.next()) {
                if (limitarRegistros) {
                    if (numeroRegistro == pRegistrosConsulta) {
                        break;
                    }

                    numeroRegistro++;
                }

                Hashtable datosRegistro = new Hashtable();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String dato = this.registros.getString(metaData.getColumnName(i));

                    if (dato == null) {
                        dato = "null";
                    }
                    if (usarColumnas) {
                        datosRegistro.put(metaData.getColumnName(i).toUpperCase(), dato);
                    } else {
                        datosRegistro.put(new Integer(i), dato);
                    }
                }

                listaRegistros.add(datosRegistro);
            }

            this.registros.close();
            //this.sentenciaSQL.close();
        } catch (Exception error) {
            throw new Exception(error.getMessage());
        } finally {
            try {
                if (this.sentenciaSQL != null) {
                    this.sentenciaSQL.close();
                }
                this.cerrarConexion();
            } catch (Exception error) {
                throw new Exception("[CV|Tabla]: \n" + error.getMessage());
            }
        }

        return listaRegistros;
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

    public Integer getNumeroRegistros() {
        return numeroRegistros;
    }

    public String ejecutarProcedure(String pProcedure, String pParametros, int[] pArrayRetorno) throws SQLException {
        String retorno = "";
        CallableStatement cs = null;
        try {
            this.establecerConexion();

            String llamado = "{call " + pProcedure + pParametros + "}";
            System.out.println("Llamado : " + llamado);
            System.out.println("Llamado procedimiento: " + pProcedure + pParametros);
            cs = sesionBD.prepareCall(llamado);
            if (pArrayRetorno != null) {
                for (int i = 0; i < pArrayRetorno.length; i++) {
                    cs.registerOutParameter(i + 1, pArrayRetorno[i]);
                }
            }
            cs.execute();

            Vector vectorRespuestas = new Vector();
            if (pArrayRetorno != null) {
                for (int i = 0; i < pArrayRetorno.length; i++) {
                    try {
                        if (pArrayRetorno[i] == Types.NUMERIC) {
                            vectorRespuestas.add(new Integer(cs.getInt(i + 1)));
                        }
                        if (pArrayRetorno[i] == Types.VARCHAR) {
                            vectorRespuestas.add(cs.getString(i + 1));
                        }
                        if (pArrayRetorno[i] == Types.DATE) {
                            vectorRespuestas.add(cs.getDate(i + 1));
                        }
                    } catch (Exception exc) {
                        vectorRespuestas.add("Error al traer retorno: " + i);
                        continue;
                    }
                }
            }

            retorno = vectorRespuestas.toString();

        } catch (Exception error) {
            error.printStackTrace(); //ob
            retorno = error.getMessage();
        } finally {
            cs.close();
            this.cerrarConexion();
            return retorno;
        }
    }

    @Override
    public Collection consultarTabla(int pRegistrosConsulta, int pPosicionInicial, String pTabla,
                                     String pCondicion) throws Exception {
        boolean usarColumnas = true;
        Collection listaRegistros = new ArrayList();
        StringBuffer txtSql = new StringBuffer();
        try {
            if (pTabla == null) {
                txtSql.append(pCondicion);
            } else {
                txtSql.append(" SELECT * \n");
                txtSql.append(" FROM  " + pTabla + "\n");
                if (pCondicion != null) {
                    txtSql.append(pCondicion);
                }
            }
            this.establecerConexion();
            boolean limitarRegistros = (pRegistrosConsulta != 0) ? true : false;
            int numeroRegistro = 0;
            this.sentenciaSQL =
                this.sesionBD.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT='" + CatalogoBD.patronFormatoFecha +
                                               "'");

            boolean altero = this.sentenciaSQL.execute();
            boolean alteroLenguaje = this.sentenciaSQL.execute("ALTER SESSION SET NLS_LANGUAGE = 'AMERICAN'");
            boolean alteroSort = this.sentenciaSQL.execute("ALTER SESSION SET NLS_SORT = 'BINARY'");
            boolean alteroFormatoNumerico =
                this.sentenciaSQL.execute("ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.'");

            this.sentenciaSQL.close();
            System.out.println("consulta: " + txtSql.toString());
            this.sentenciaSQL =
                this.sesionBD.prepareStatement(txtSql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE,
                                               ResultSet.CONCUR_READ_ONLY);
            if (this.sentenciaSQL == null) {
                throw new Exception("[CV|Tabla]: La sintaxis SQL puede estar incorrecta. \n " + txtSql);
            }
            this.registros = this.sentenciaSQL.executeQuery();
            if (this.registros == null) {
                throw new Exception("[CV|Tabla]: No se retornaron registros en la consulta. \n " + txtSql);
            }
            registros.last();
            int totalRegistros = registros.getRow();
            if (totalRegistros < pPosicionInicial) {
                registros.close();
                sentenciaSQL.close();
                this.cerrarConexion();
                return null;
            }
            this.numeroRegistros = new Integer(totalRegistros);
            if (pPosicionInicial == 0) {
                registros.beforeFirst();
            } else {
                registros.absolute(pPosicionInicial);
            }
            ResultSetMetaData metaData = registros.getMetaData();
            while (this.registros.next()) {
                if (limitarRegistros) {
                    if (numeroRegistro == pRegistrosConsulta) {
                        break;
                    }
                    numeroRegistro++;
                }
                Hashtable datosRegistro = new Hashtable();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String dato = this.registros.getString(metaData.getColumnName(i));
                    if (dato == null) {
                        dato = "null";
                    }
                    if (usarColumnas) {
                        datosRegistro.put(metaData.getColumnName(i).toUpperCase(), dato);
                    } else {
                        datosRegistro.put(new Integer(i), dato);
                    }
                }
                listaRegistros.add(datosRegistro);
            }
            this.registros.close();
            this.sentenciaSQL.close();
        } catch (Exception error) {
            throw new Exception(error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception error) {
                throw new Exception("[CV|Tabla]: \n" + error.getMessage());
            }
        }
        return listaRegistros;
    }
    // Para llamar este metodo es necesario que pProcedure se pase de la siguiente manera:
    // procedure_name.llamar(?)

    public String ejecutarProcedure(String pProcedure, Vector pParametros, int[] pArrayRetorno) throws SQLException {
        String retorno = "";
        CallableStatement cs = null;
        try {
            this.establecerConexion();

            String llamado = "{call " + pProcedure + "}";
            cs = sesionBD.prepareCall(llamado);

            for (int i = 0; i < pParametros.size(); i++) {

                Object elemento = pParametros.get(i);
                if (elemento instanceof String) {
                    cs.setString(i + 1, (String) elemento);

                }
                if (elemento instanceof Integer) {
                    cs.setInt(i + 1, ((Integer) elemento).intValue());

                }
                if (elemento instanceof Long) {
                    cs.setLong(i + 1, ((Long) elemento).longValue());

                }
            }

            if (pArrayRetorno != null) {
                for (int i = 0; i < pArrayRetorno.length; i++) {
                    cs.registerOutParameter(i + 1, pArrayRetorno[i]);
                }
            }
            cs.execute();

            Vector vectorRespuestas = new Vector();
            if (pArrayRetorno != null) {
                for (int i = 0; i < pArrayRetorno.length; i++) {
                    try {
                        if (pArrayRetorno[i] == Types.NUMERIC) {
                            vectorRespuestas.add(new Integer(cs.getInt(i + 1)));
                        }
                        if (pArrayRetorno[i] == Types.VARCHAR) {
                            vectorRespuestas.add(cs.getString(i + 1));
                        }
                        if (pArrayRetorno[i] == Types.DATE) {
                            vectorRespuestas.add(cs.getDate(i + 1));
                        }
                    } catch (Exception exc) {
                        vectorRespuestas.add("Error al traer retorno: " + i);
                        continue;
                    }
                }
            }

            retorno = vectorRespuestas.toString();

        } catch (Exception error) {
            retorno = error.getMessage();
        } finally {
            cs.close();
            this.cerrarConexion();
            return retorno;
        }

    }

}
