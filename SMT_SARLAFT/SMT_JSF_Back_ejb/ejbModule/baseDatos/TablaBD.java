package baseDatos;

import java.io.Serializable;

import java.sql.*;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Calendar;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Collection;
import java.util.Vector;
import java.util.ArrayList;

import java.sql.Connection;

public class TablaBD {
    public static String separador = ",";

    private Connection sesion;
    private String nombre;
    private StringBuffer sqlQuery = new StringBuffer();
    private StringBuffer sqlQueryPK = new StringBuffer();
    private StringBuffer sqlWherePK = new StringBuffer();
    private StringBuffer sqlInsert = new StringBuffer();
    private StringBuffer sqlUpdate = new StringBuffer();
    private StringBuffer sqlDelete = new StringBuffer();
    private PreparedStatement sentenciaSQL;
    private ResultSetMetaData metaData;
    private ResultSet registros;
    private TreeMap columnas = new TreeMap();


    public TablaBD(String pNombre, String pLlaves, Connection pSesion) throws SQLException, Exception {
        System.out.println("TablaBD|: ... ");
        StringBuffer txtValues = new StringBuffer();
        boolean tienePK = false;

        ColumnaBD columnaTabla;
        Iterator listaColumnas;

        if (pNombre == null || pSesion == null || pSesion.isClosed()) {
            throw new ExcepcionBD("El nombre de la tabla no puede ser nulo.");
        }

        if (pSesion == null || pSesion.isClosed()) {
            throw new ExcepcionBD("La sesión de base de datos no existe o ya está cerrada.");
        }

        if (pLlaves == null) {
            throw new ExcepcionBD("La llave primaria no puede ser nula.");
        }

        this.nombre = pNombre.toUpperCase();
        this.sesion = pSesion;
        pLlaves = pLlaves.toUpperCase();

        this.sqlQuery.append(" SELECT *");
        this.sqlQuery.append(" FROM " + this.nombre);
        this.sqlQuery.append(" WHERE ROWNUM < 2");

        this.sqlQueryPK.append(" SELECT ");

        this.sqlInsert.append(" INSERT INTO " + this.nombre);
        this.sqlInsert.append(" ( ");

        this.sqlUpdate.append(" UPDATE " + this.nombre + " SET ");

        this.sqlDelete.append(" DELETE FROM " + this.nombre);

        this.sqlWherePK.append(" WHERE ");

        System.out.println("TablaBD|: this.sqlQuery = " + this.sqlQuery.toString());


        this.sentenciaSQL = this.sesion.prepareStatement(sqlQuery.toString());
        Statement st = this.sesion.createStatement();
        this.registros = st.executeQuery(sqlQuery.toString());
        this.metaData = this.registros.getMetaData();

        for (int i = 1; i <= this.metaData.getColumnCount(); i++) {
            columnaTabla = new ColumnaBD();

            columnaTabla.nombre = this.metaData.getColumnName(i).toUpperCase();
            columnaTabla.nombreTipo = this.metaData.getColumnTypeName(i).toUpperCase();
            columnaTabla.tipo = this.metaData.getColumnType(i);
            columnaTabla.claseJava = this.metaData.getColumnClassName(i);
            columnaTabla.longitud = this.metaData.getColumnDisplaySize(i);
            columnaTabla.enteros = this.metaData.getPrecision(i);
            columnaTabla.decimales = this.metaData.getScale(i);
            columnaTabla.permiteNulo = (this.metaData.isNullable(i) == 1);
            columnaTabla.llavePrimaria = this.identificarPK(pLlaves, columnaTabla.nombre);
            columnaTabla.valor = null;

            this.columnas.put(columnaTabla.nombre, columnaTabla);
        }

        listaColumnas = this.columnas.values().iterator();

        while (listaColumnas.hasNext()) {

            columnaTabla = (ColumnaBD) listaColumnas.next();

            this.sqlInsert.append(" " + columnaTabla.nombre + ",");
            txtValues.append(" ?,");

            this.sqlUpdate.append(" " + columnaTabla.nombre + " = ?,");

            if (columnaTabla.llavePrimaria) {

                tienePK = true;
                this.sqlQueryPK.append(" " + columnaTabla.nombre + ",");
                this.sqlWherePK.append(" " + columnaTabla.nombre + " = ? AND");
            }
        }

        if (!tienePK) {
            throw new ExcepcionBD("No se definió llave primaria para la tabla.");
        }

        this.sqlQueryPK.deleteCharAt(this.sqlQueryPK.length() - 1);
        this.sqlInsert.deleteCharAt(this.sqlInsert.length() - 1);
        txtValues.deleteCharAt(txtValues.length() - 1);
        this.sqlUpdate.deleteCharAt(this.sqlUpdate.length() - 1);
        this.sqlWherePK.delete(this.sqlWherePK.length() - 4, this.sqlWherePK.length());

        this.sqlInsert.append(" )");
        this.sqlInsert.append(" VALUES ( ");
        this.sqlInsert.append(txtValues);
        this.sqlInsert.append(")");

        // this.sqlUpdate.append("\n");
        // this.sqlDelete.append("\n");
        this.sqlQueryPK.append(" FROM " + this.nombre + " ");

        if (tienePK) {
            this.sqlUpdate.append(this.sqlWherePK);
            this.sqlDelete.append(this.sqlWherePK);
            this.sqlQueryPK.append(this.sqlWherePK);
        }

        System.out.println("TablaBD|: this.sqlQueryPK = " + this.sqlQueryPK.toString());

        System.out.println("TablaBD|: this.sqlInsert = " + this.sqlInsert.toString());

        System.out.println("TablaBD|: this.sqlDelete = " + this.sqlDelete.toString());

        System.out.println("TablaBD|: this.sqlUpdate = " + this.sqlUpdate.toString());

        boolean altero =
            this.sentenciaSQL.execute("ALTER SESSION SET NLS_DATE_FORMAT='" + CatalogoBD.patronFormatoFecha + "'");
        /*
        Modificación para establecer el lenguaje y el tipo de sort de la base de datos.(Milena)
      */

        boolean alteroLenguaje = this.sentenciaSQL.execute("ALTER SESSION SET NLS_LANGUAGE = 'AMERICAN'");
        boolean alteroSort = this.sentenciaSQL.execute("ALTER SESSION SET NLS_SORT = 'BINARY'");

        System.out.println("TablaBD|: altero = " + altero);
        System.out.println("TablaBD|: alteroLenguaje = " + alteroLenguaje);
        System.out.println("TablaBD|: alteroSort = " + alteroSort);

        System.out.println("TablaBD|: this.sentenciaSQL = " + this.sentenciaSQL.toString());

        System.out.println("this.sentenciaSQL: " + this.sentenciaSQL + " this.registros: " + this.registros);

        this.registros.close();
        this.sentenciaSQL.close();

    }

    public void insertarDatos() throws SQLException, Exception {
        System.out.println("===> TablaBD|insertarDatos... ");
        int indiceColumna = 0;

        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columna;

        this.sentenciaSQL = this.sesion.prepareStatement(sqlInsert.toString());

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();
            indiceColumna++;

            if (columna.valor == null) {
                this.sentenciaSQL.setNull(indiceColumna, columna.tipo);
                continue;
            }

            switch (columna.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                this.sentenciaSQL.setString(indiceColumna, (String) columna.valor);
                break;

            case Types.NUMERIC:
                if (columna.decimales == 0) {
                    try { /*IMAM*/
                        this.sentenciaSQL.setInt(indiceColumna, ((Integer) (columna.valor)).intValue());
                    } catch (Exception noCabe) { /*IMAM*/
                        this.sentenciaSQL.setLong(indiceColumna, ((Long) (columna.valor)).longValue()); /*IMAM*/
                    } /*IMAM*/
                } else {
                    this.sentenciaSQL.setBigDecimal(indiceColumna, (BigDecimal) columna.valor);
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                this.sentenciaSQL.setTimestamp(indiceColumna, (Timestamp) columna.valor);
                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }
        }
        System.out.println("===> TablaBD|insertarDatos: sentenciaSQL: " + this.sentenciaSQL.toString());
        int reUpd = this.sentenciaSQL.executeUpdate();
        System.out.println("===> TablaBD|insertarDatos: reUpd: " + reUpd);
        if (reUpd == 0) {
            this.sentenciaSQL.close();
            throw new ExcepcionBD("No se insertó ningún registro.");
        } else {
            this.sentenciaSQL.close();
        }
    }

    public void consultarDatos() throws SQLException, Exception {
        int indiceColumna = 0;

        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columna;

        this.sqlQuery = new StringBuffer();
        this.sqlQuery.append(" SELECT *");
        this.sqlQuery.append(" FROM " + this.nombre + " ");
        this.sqlQuery.append(this.sqlWherePK);

        this.sentenciaSQL = this.sesion.prepareStatement(sqlQuery.toString());

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();

            if (!columna.llavePrimaria) {
                continue;
            }

            indiceColumna++;

            if (columna.valor == null) {
                continue;
            }

            switch (columna.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                this.sentenciaSQL.setString(indiceColumna, (String) columna.valor);
                break;

            case Types.NUMERIC:
                if (columna.decimales == 0) {
                    try { /*IMAM*/
                        this.sentenciaSQL.setInt(indiceColumna, ((Integer) (columna.valor)).intValue());
                    } catch (Exception e) { /*IMAM*/
                        this.sentenciaSQL.setLong(indiceColumna, ((Long) (columna.valor)).longValue()); /*IMAM*/
                    } /*IMAM*/
                } else {
                    this.sentenciaSQL.setBigDecimal(indiceColumna, (BigDecimal) columna.valor);
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                this.sentenciaSQL.setTimestamp(indiceColumna, (Timestamp) columna.valor);
                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }
        }

        this.registros = this.sentenciaSQL.executeQuery();

        if (this.registros.next()) {
            listaColumnas = this.columnas.values().iterator();

            while (listaColumnas.hasNext()) {
                columna = (ColumnaBD) listaColumnas.next();

                switch (columna.tipo) {
                case Types.VARCHAR:
                case Types.CHAR:
                    columna.valor = this.registros.getString(columna.nombre);
                    break;

                case Types.NUMERIC:
                    if (columna.decimales == 0) {
                        try { /*IMAM*/
                            columna.valor = new Integer(this.registros.getInt(columna.nombre));
                        } catch (Exception e) { /*IMAM*/
                            columna.valor = new Long(this.registros.getLong(columna.nombre)); /*IMAM*/
                        } /* IMAM */
                    } else {
                        columna.valor = this.registros.getBigDecimal(columna.nombre);
                    }

                    break;

                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    columna.valor = this.registros.getTimestamp(columna.nombre);
                    break;

                default:
                    throw new ExcepcionBD("Tipo de dato no soportado.");
                }

                if (this.registros.wasNull()) {
                    columna.valor = null;
                }
            }

            this.registros.close();
            this.sentenciaSQL.close();
        } else {
            this.registros.close();
            this.sentenciaSQL.close();

            throw new ExcepcionBD("Registro no encontrado.");
        }
    }

    public void actualizarDatos() throws SQLException, Exception {
        System.out.println("==> TablaBD|actualizarDatos(): ");
        int indiceColumna = 0;

        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columna;
        System.out.println("==> TablaBD|actualizarDatos()|prepareStatement: ");
        this.sentenciaSQL = this.sesion.prepareStatement(sqlUpdate.toString());

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();
            indiceColumna++;

            if (columna.valor == null) {
                this.sentenciaSQL.setNull(indiceColumna, columna.tipo);
                continue;
            }

            switch (columna.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                this.sentenciaSQL.setString(indiceColumna, (String) columna.valor);
                break;

            case Types.NUMERIC:
                if (columna.decimales == 0) {
                    try { /*IMAM*/
                        this.sentenciaSQL.setInt(indiceColumna, ((Integer) (columna.valor)).intValue());
                    } catch (Exception e) { /*IMAM*/
                        this.sentenciaSQL.setLong(indiceColumna, ((Long) (columna.valor)).longValue()); /*IMAM*/
                    } /*IMAM*/
                } else {
                    this.sentenciaSQL.setBigDecimal(indiceColumna, (BigDecimal) columna.valor);
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                this.sentenciaSQL.setTimestamp(indiceColumna, (Timestamp) columna.valor);
                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }


        }
        System.out.println("==> TablaBD|actualizarDatos()|listaColumnas ");
        listaColumnas = this.columnas.values().iterator();

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();

            if (!columna.llavePrimaria) {
                continue;
            }

            indiceColumna++;

            if (columna.valor == null) {
                continue;
            }

            switch (columna.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                this.sentenciaSQL.setString(indiceColumna, (String) columna.valor);
                break;

            case Types.NUMERIC:
                if (columna.decimales == 0) {
                    try {
                        this.sentenciaSQL.setInt(indiceColumna, ((Integer) (columna.valor)).intValue());
                    } catch (Exception e) {
                        this.sentenciaSQL.setLong(indiceColumna, ((Long) (columna.valor)).longValue());
                    }
                } else {
                    this.sentenciaSQL.setBigDecimal(indiceColumna, (BigDecimal) columna.valor);
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                this.sentenciaSQL.setTimestamp(indiceColumna, (Timestamp) columna.valor);
                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }
        }
        System.out.println("==> TablaBD|actualizarDatos: " + this.sentenciaSQL.toString());
        if (this.sentenciaSQL.executeUpdate() == 0) {
            this.sentenciaSQL.close();
            throw new ExcepcionBD("No se actualizó ningún registro.");
        } else {
            this.sentenciaSQL.close();
        }
    }

    public void borrarDatos() throws SQLException, Exception {
        int indiceColumna = 0;

        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columna;

        this.sentenciaSQL = this.sesion.prepareStatement(sqlDelete.toString());

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();

            if (!columna.llavePrimaria) {
                continue;
            }

            indiceColumna++;

            if (columna.valor == null) {
                continue;
            }

            switch (columna.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                this.sentenciaSQL.setString(indiceColumna, (String) columna.valor);
                break;

            case Types.NUMERIC:
                if (columna.decimales == 0) {
                    try { /*IMAM*/
                        this.sentenciaSQL.setInt(indiceColumna, ((Integer) (columna.valor)).intValue());
                    } catch (Exception e) { /*IMAM*/
                        this.sentenciaSQL.setLong(indiceColumna, ((Long) (columna.valor)).longValue()); /*IMAM*/
                    } /*IMAM*/
                } else {
                    this.sentenciaSQL.setBigDecimal(indiceColumna, (BigDecimal) columna.valor);
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                this.sentenciaSQL.setTimestamp(indiceColumna, (Timestamp) columna.valor);
                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }
        }


        this.sentenciaSQL.executeUpdate();
        this.sentenciaSQL.close();
    }


    public boolean buscarPK() throws SQLException, Exception {
        int indiceColumna = 0;
        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columna;
        this.sentenciaSQL = this.sesion.prepareStatement(sqlQueryPK.toString());
        if (this.sentenciaSQL == null) {
            throw new ExcepcionBD("La sintaxis SQL puede estar errónea. \n " + sqlQueryPK);
        }

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();
            if (!columna.llavePrimaria) {
                continue;
            }
            indiceColumna++;
            if (columna.valor == null) {
                continue;
            }
            switch (columna.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                this.sentenciaSQL.setString(indiceColumna, (String) columna.valor);
                break;

            case Types.NUMERIC:
                if (columna.decimales == 0) {
                    try { /*IMAM*/
                        this.sentenciaSQL.setInt(indiceColumna, ((Integer) (columna.valor)).intValue());
                    } catch (Exception e) { /*IMAM*/
                        this.sentenciaSQL.setLong(indiceColumna, ((Long) (columna.valor)).longValue());
                    }
                } else {
                    this.sentenciaSQL.setBigDecimal(indiceColumna, (BigDecimal) columna.valor);
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                this.sentenciaSQL.setTimestamp(indiceColumna, (Timestamp) columna.valor);
                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }
        }

        registros = this.sentenciaSQL.executeQuery();
        if (registros.next()) {
            registros.close();
            this.sentenciaSQL.close();
            return true;
        } else {
            registros.close();
            this.sentenciaSQL.close();
            return false;
        }
    }


    public Collection consultarPkCompuesta(int pRegistrosConsulta, String pCondicion) throws SQLException, Exception {

        ArrayList listaPKs = new ArrayList();
        Vector llavesPK = new Vector();

        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columna;

        StringBuffer txtSelect = new StringBuffer(" SELECT ");

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();

            if (columna.llavePrimaria) {
                txtSelect.append(" " + columna.nombre + ",");
                llavesPK.add(columna.nombre);
            }
        }

        txtSelect.deleteCharAt(txtSelect.length() - 1);
        txtSelect.append(" FROM " + this.nombre);

        if (pCondicion != null) {
            txtSelect.append(" " + pCondicion);
        }

        boolean limitarRegistros = (pRegistrosConsulta != 0) ? true : false;

        int numeroRegistro = 0;

        this.sentenciaSQL = this.sesion.prepareStatement(txtSelect.toString());

        if (this.sentenciaSQL == null) {
            throw new ExcepcionBD("La sintaxis SQL puede estar errónea. \n " + txtSelect);
        }

        this.registros = this.sentenciaSQL.executeQuery();

        if (this.registros == null) {
            throw new ExcepcionBD("No se retornaron resgistros en la consulta. \n " + txtSelect);
        }

        while (registros.next()) {
            if (limitarRegistros) {
                if (numeroRegistro == pRegistrosConsulta) {
                    break;
                }

                numeroRegistro++;
            }

            Iterator columnasPK = llavesPK.iterator();
            Hashtable registroPK = new Hashtable();

            while (columnasPK.hasNext()) {
                String nombrePK = columnasPK.next().toString();

                columna = (ColumnaBD) this.columnas.get(nombrePK);

                switch (columna.tipo) {
                case Types.VARCHAR:
                case Types.CHAR:
                    String valorString = new String();
                    valorString = this.registros.getString(columna.nombre);
                    registroPK.put(nombrePK, valorString);
                    break;

                case Types.NUMERIC:
                    if (columna.decimales == 0) {
                        try { /*IMAM*/
                            Integer valorEntero = new Integer(0);
                            valorEntero = new Integer(this.registros.getInt(columna.nombre));
                            registroPK.put(nombrePK, valorEntero);
                        } catch (Exception e) {
                            Long valorEnteroGrande = new Long(0);
                            valorEnteroGrande = new Long(this.registros.getLong(columna.nombre));
                            registroPK.put(nombrePK, valorEnteroGrande);
                        }
                    } else {
                        BigDecimal valorDecimal = new BigDecimal(0);
                        valorDecimal = this.registros.getBigDecimal(columna.nombre);
                        registroPK.put(nombrePK, valorDecimal);
                    }

                    break;

                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    Timestamp valorFecha = new Timestamp(System.currentTimeMillis());
                    valorFecha = this.registros.getTimestamp(columna.nombre);
                    registroPK.put(nombrePK, valorFecha);
                    break;

                default:
                    throw new ExcepcionBD("Tipo de dato no soportado.");
                }

            }

            listaPKs.add(registroPK);
        }

        registros.close();
        this.sentenciaSQL.close();

        if (listaPKs.size() > 0) {
            return listaPKs;
        } else {
            throw new ExcepcionBD("No se encontraron registros.");
        }
    }


    public Collection consultarPkSimple(int pRegistrosConsulta, String pCondicion) throws SQLException, Exception {

        this.limpiarColumnasConsulta();

        ArrayList listaPKs = new ArrayList();

        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columnaPk = new ColumnaBD();

        StringBuffer txtSelect = new StringBuffer(" SELECT ");

        int totalPk = 0;

        while (listaColumnas.hasNext()) {
            ColumnaBD columna = (ColumnaBD) listaColumnas.next();

            if (columna.llavePrimaria) {
                txtSelect.append(" " + columna.nombre + ",");
                columnaPk = columna;
                totalPk++;
            }
        }

        if (totalPk > 1) {
            throw new ExcepcionBD("La tabla " + this.nombre + " no tiene llave primaria simple");
        }

        txtSelect.deleteCharAt(txtSelect.length() - 1);
        txtSelect.append(" FROM " + this.nombre);

        if (pCondicion != null) {
            txtSelect.append(" " + pCondicion);
        }

        boolean limitarRegistros = (pRegistrosConsulta != 0) ? true : false;

        int numeroRegistro = 0;

        this.sentenciaSQL = this.sesion.prepareStatement(txtSelect.toString());

        if (this.sentenciaSQL == null) {
            throw new ExcepcionBD("La sintaxis SQL puede estar errónea. \n " + txtSelect);
        }

        this.registros = this.sentenciaSQL.executeQuery();

        if (this.registros == null) {
            throw new ExcepcionBD("No se retornaron resgistros en la consulta. \n " + txtSelect);
        }


        while (registros.next()) {
            if (limitarRegistros) {
                if (numeroRegistro == pRegistrosConsulta) {
                    break;
                }

                numeroRegistro++;
            }

            switch (columnaPk.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                String valorString = new String();
                valorString = this.registros.getString(columnaPk.nombre);
                listaPKs.add(valorString);
                break;

            case Types.NUMERIC:
                if (columnaPk.decimales == 0) {
                    try { /*IMAM*/
                        Integer valorEntero = new Integer(0);
                        valorEntero = new Integer(this.registros.getInt(columnaPk.nombre));
                        listaPKs.add(valorEntero);
                    } catch (Exception e) {
                        Long valorEntero = new Long(0);
                        valorEntero = new Long(this.registros.getLong(columnaPk.nombre));
                        listaPKs.add(valorEntero);
                    }
                } else {
                    BigDecimal valorDecimal = new BigDecimal(0);
                    valorDecimal = this.registros.getBigDecimal(columnaPk.nombre);
                    listaPKs.add(valorDecimal);
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                Timestamp valorFecha = new Timestamp(System.currentTimeMillis());
                valorFecha = this.registros.getTimestamp(columnaPk.nombre);
                listaPKs.add(valorFecha);
                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }
        }

        this.registros.close();
        this.sentenciaSQL.close();

        if (listaPKs.size() > 0) {
            return listaPKs;
        } else {
            throw new ExcepcionBD("No se encontraron registros.");
        }
    }


    public void asignarValorColumna(String pColumna, Object pValor) throws ExcepcionBD {
        System.out.println("TablaBD|asignarValorColumna...");
        ColumnaBD columna;
        System.out.println("this.columnas: " + this.columnas);
        columna = (ColumnaBD) this.columnas.get(pColumna);
        System.out.println("columna: " + columna);
        if (columna == null) {
            throw new ExcepcionBD("La columna " + pColumna + " no existe en la tabla " + this.nombre + ".");
        } else {
            System.out.println("pValor: " + pValor);
            System.out.println("columna.valor: " + columna.valor);
            if (pValor == null) {
                columna.valor = null;
                return;
            }

            String claseJava = pValor.getClass().getName();
            System.out.println("columna.tipo: " + columna.tipo);
            switch (columna.tipo) {
            case Types.VARCHAR:
            case Types.CHAR:
                if (!claseJava.equals("java.lang.String")) {
                    throw new ExcepcionBD("Tipo de dato incompatible con la columna " + columna.nombre +
                                          ". Debe ser java.lang.String.");
                }

                break;

            case Types.NUMERIC:
                if (!claseJava.equals("java.lang.Integer") && !claseJava.equals("java.lang.Long") &&
                    !claseJava.equals("java.math.BigDecimal")) {
                    throw new ExcepcionBD("Tipo de dato incompatible con la columna " + columna.nombre +
                                          ". Debe ser \n" +
                                          "java.lang.Integer, java.lang.Long o java.math.BigDecimal.");
                }

                break;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                if (!claseJava.equals("java.sql.Timestamp")) {
                    throw new ExcepcionBD("Tipo de dato incompatible con la columna " + columna.nombre +
                                          ". Debe ser java.sql.Timestamp.");
                }

                break;

            default:
                throw new ExcepcionBD("Tipo de dato no soportado.");
            }

            columna.valor = pValor;
        }

    }

    public void asignarValorColumna(String pColumna, boolean pValor) throws ExcepcionBD {
        ColumnaBD columna;

        columna = (ColumnaBD) this.columnas.get(pColumna);

        if (columna == null) {
            throw new ExcepcionBD("La columna " + pColumna + " no existe en la tabla " + this.nombre + ".");
        }

        if (columna.tipo != Types.NUMERIC) {
            throw new ExcepcionBD("La columna " + pColumna + " de la tabla " + this.nombre +
                                  " no permite valores booleanos.");
        }

        columna.valor = (pValor) ? new Integer(1) : new Integer(0);
    }

    public Object traerValorColumna(String pColumna) throws ExcepcionBD {
        ColumnaBD columna;

        columna = (ColumnaBD) this.columnas.get(pColumna);

        if (columna == null) {
            throw new ExcepcionBD("La columna " + pColumna + " no existe en la tabla " + this.nombre + ".");
        } else {
            return columna.valor;
        }

    }

    public boolean traerValorBoleanoColumna(String pColumna) throws ExcepcionBD {
        ColumnaBD columna;

        columna = (ColumnaBD) this.columnas.get(pColumna);

        if (columna == null) {
            throw new ExcepcionBD("La columna " + pColumna + " no existe en la tabla " + this.nombre + ".");
        }

        if (columna.tipo != Types.NUMERIC) {
            throw new ExcepcionBD("La columna " + pColumna + " de la tabla " + this.nombre +
                                  " no permite valores booleanos.");
        }

        int valorColumna = ((Integer) columna.valor).intValue();

        if (valorColumna != 0 && valorColumna != 1) {
            throw new ExcepcionBD("El valor de la columna " + pColumna + " de la tabla " + this.nombre +
                                  " no corresponde a un valor booleano ( false = 0, true = 1).");
        }

        return (valorColumna == 1);
    }

    public void limpiarColumnasConsulta() {
        Iterator listaColumnas = this.columnas.values().iterator();
        ColumnaBD columna;

        while (listaColumnas.hasNext()) {
            columna = (ColumnaBD) listaColumnas.next();
            columna.indagable = false;
            columna.condicionConsulta = null;
        }
    }


    public void activarColumnaConsulta(String pColumna, String pCondicion) throws ExcepcionBD {
        ColumnaBD columna;

        columna = (ColumnaBD) this.columnas.get(pColumna);

        if (columna == null) {
            throw new ExcepcionBD("La columna " + pColumna + " no existe en la tabla " + this.nombre + ".");
        } else {
            columna.condicionConsulta = pCondicion;
            columna.indagable = true;
        }
    }

    public String getNombre() {
        return nombre;
    }


    public Integer traerIDSecuencia(String pSecuencia) throws SQLException, Exception {

        this.sqlQuery = new StringBuffer();
        this.sqlQuery.append(" SELECT " + pSecuencia.trim() + ".NEXTVAL ");
        this.sqlQuery.append(" FROM DUAL ");

        this.sentenciaSQL = this.sesion.prepareStatement(sqlQuery.toString());

        this.registros = this.sentenciaSQL.executeQuery();

        if (registros.next()) {
            Integer id = new Integer(registros.getInt(1));

            registros.close();
            this.sentenciaSQL.close();
            return id;
        } else {
            registros.close();
            this.sentenciaSQL.close();
            throw new ExcepcionBD("La secuencia " + pSecuencia + " no existe.");
        }
    }

    public void setSesion(Connection pSesion) {
        sesion = pSesion;
    }

    public boolean identificarPK(String listaPk, String nombreColumna) {
        listaPk += separador;

        int posInicial = 0;
        int posFinal = listaPk.indexOf(separador, posInicial);

        do {
            String columnaPk = listaPk.substring(posInicial, posFinal);

            if (columnaPk.trim().equalsIgnoreCase(nombreColumna.trim())) {
                return true;
            }

            posInicial = (posFinal + separador.length());
            posFinal = listaPk.indexOf(separador, posInicial);
        } while ((posFinal != -1) && (posInicial < listaPk.length()));

        return false;
    }
}


class ColumnaBD {
    public String nombre;
    public String nombreTipo;
    public int tipo;
    public String claseJava;
    public int longitud;
    public int enteros;
    public int decimales;
    public boolean permiteNulo;
    public boolean llavePrimaria;
    public boolean indagable;
    public String condicionConsulta;
    public Object valor;

    public ColumnaBD() {
        this.valor = null;
    }

    public String toString() {
        return "[" + this.nombre + "," + this.nombreTipo + "," + this.tipo + "," + this.claseJava + "," +
               this.longitud + "," + this.enteros + "," + this.decimales + "," + this.permiteNulo + "," +
               this.llavePrimaria + "," + this.indagable + "," + this.condicionConsulta + "," + this.valor + "]";
    }
}


