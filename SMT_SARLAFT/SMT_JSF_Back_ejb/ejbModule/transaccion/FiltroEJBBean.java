package transaccion;

import admin.Cargo;
import admin.CriteriosInusualidad;

import java.util.Collection;
import java.util.Collections;

import javax.ejb.EJBException;

import java.util.Date;

import java.sql.*;

import javax.ejb.FinderException;

import javax.sql.*;

import javax.naming.InitialContext;

import java.util.Hashtable;

import baseDatos.*;

import java.util.Calendar;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import javax.servlet.http.HttpServletRequest;

import javax.transaction.UserTransaction;

@Stateless(name = "FiltroEJB", mappedName = "SARLAFT-EJB-FiltroEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class FiltroEJBBean implements FiltroEJB {
    @EJB
    private ConsultaTablaEJB consTablaEJB;
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Administracion")
    private EntityManager entityManager;
    private Filtro filtro;
    private TablaBD miTabla;
    transient private Connection sesionBD;

    public Integer create(Hashtable pDatos) {
        String cargo = (String) pDatos.get("CODIGO_CARGO");
        if (cargo != null) {
            try {
                this.establecerConexion();
                TablaBD miTabla = new TablaBD("CARGOS", "CODIGO", this.sesionBD);
                miTabla.asignarValorColumna("CODIGO", cargo);
                if (!miTabla.buscarPK()) {
                    throw new EJBException("FiltroEJB|Create: Cargo no existe");
                }
            } catch (Exception e) {
                throw new EJBException("FiltroEJB|Create: Cargo " + e.getMessage());
            } finally {
                try {
                    this.cerrarConexion();
                } catch (Exception error) {
                    throw new EJBException("FiltroEJB|Create: No se cerró la conexion " + error.getMessage());
                }
            }

        } else {
            throw new EJBException("FiltroEJB|Create: Cargo nulo");
        }

        String producto = (String) pDatos.get("CODIGO_PRODUCTO");
        if (producto != null) {
            try {
                this.establecerConexion();
                TablaBD miTabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", this.sesionBD);
                miTabla.asignarValorColumna("TIPO_DATO", new Integer(2));
                miTabla.asignarValorColumna("CODIGO", producto);
                if (!miTabla.buscarPK()) {
                    throw new EJBException("FiltroEJB|Create: Producto no existe");
                }
            } catch (Exception e) {
                throw new EJBException("FiltroEJB|Create: Producto " + e.getMessage());
            } finally {
                try {
                    this.cerrarConexion();
                } catch (Exception error) {
                    throw new EJBException("FiltroEJB|Create: No se cerró la conexion " + error.getMessage());
                }
            }
        }

        String condicion = (String) pDatos.get("CONDICION");
        if (condicion == null) {
            throw new EJBException("FiltroEJB|Create: La condición del filtro no puede ser nula");
        }

        String ti = (String) pDatos.get("TIPO_IDENTIFICACION");
        if (ti != null) {
            try {
                this.establecerConexion();
                TablaBD miTabla = new TablaBD("LISTA_VALORES", "TIPO_DATO,CODIGO", this.sesionBD);
                miTabla.asignarValorColumna("TIPO_DATO", new Integer(17));
                miTabla.asignarValorColumna("CODIGO", ti);
                if (!miTabla.buscarPK()) {
                    throw new EJBException("FiltroEJB|Create: Tipo Documento no existe");
                }
            } catch (Exception e) {
                throw new EJBException("FiltroEJB|Create: TI " + e.getMessage());
            } finally {
                try {
                    this.cerrarConexion();
                } catch (Exception error) {
                    throw new EJBException("FiltroEJB|Create: No se cerró la conexion " + error.getMessage());
                }
            }
        }
        this.filtro = new Filtro();
        this.filtro.setId(this.traerIdFiltro());
        this.filtro.setCodigoCargo(cargo);
        this.filtro.setCodigoProducto(producto);
        this.filtro.setCondicion(condicion);
        this.filtro.setFechaCreacion((java.util.Date) pDatos.get("FECHA_CREACION"));
        this.filtro.setJustificacion((String) pDatos.get("JUSTIFICACION"));
        this.filtro.setNumeroIdentificacion((String) pDatos.get("NUMERO_IDENTIFICACION"));
        this.filtro.setNumeroNegocio((String) pDatos.get("NUMERO_NEGOCIO"));
        this.filtro.setTipoIdentificacion(ti);
        this.filtro.setUsuarioCreacion((Long) pDatos.get("USUARIO_CREACION"));
        this.filtro.setUsuarioConfirmacion((Long) pDatos.get("USUARIO_CONFIRMACION"));
        this.filtro.setVigenteDesde((java.util.Date) pDatos.get("VIGENTE_DESDE"));
        this.filtro.setVigenteHasta((java.util.Date) pDatos.get("VIGENTE_HASTA"));
        this.filtro.setEstadoFiltro(pDatos.get("ESTADO") != null ? (String) pDatos.get("ESTADO") : "0");
        return this.guardarFiltro(this.filtro);
    }

    public int guardarFiltro(Filtro filtro) {
        int reIns = 0;
        PreparedStatement sentenciaSQL;
        String sqlInsert =
            "INSERT INTO FILTROS (ID, CODIGO_CARGO, CODIGO_PRODUCTO, CONDICION, ESTADO_FILTRO, \n" +
            "FECHA_CREACION, JUSTIFICACION, NUMERO_IDENTIFICACION, NUMERO_NEGOCIO, TIPO_IDENTIFICACION, \n" +
            "USUARIO_CONFIRMACION, USUARIO_CREACION, VIGENTE_DESDE, VIGENTE_HASTA) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            this.establecerConexion();
        } catch (Exception e) {
            throw new EJBException("FiltroEJB|guardarFiltro: No se creó la conexion " + e.getMessage());
        }
        try {
            System.out.println("sqlInsert: " + sqlInsert);
            sentenciaSQL = this.sesionBD.prepareStatement(sqlInsert);
            System.out.println("sentenciaSQL: " + sentenciaSQL);
            sentenciaSQL.setInt(1, filtro.getId());
            sentenciaSQL.setString(2, filtro.getCodigoCargo());
            sentenciaSQL.setString(3, filtro.getCodigoProducto());
            sentenciaSQL.setString(4, filtro.getCondicion());
            sentenciaSQL.setString(5, filtro.getEstadoFiltro());
            sentenciaSQL.setDate(6,
                                 filtro.getFechaCreacion() != null ?
                                 new java.sql.Date(filtro.getFechaCreacion().getTime()) : null);
            sentenciaSQL.setString(7, filtro.getJustificacion());
            sentenciaSQL.setString(8, filtro.getNumeroIdentificacion());
            sentenciaSQL.setString(9, filtro.getNumeroNegocio());
            sentenciaSQL.setString(10, filtro.getTipoIdentificacion());
            sentenciaSQL.setLong(11, filtro.getUsuarioConfirmacion());
            sentenciaSQL.setLong(12, filtro.getUsuarioCreacion());
            sentenciaSQL.setDate(13,
                                 filtro.getVigenteDesde() != null ?
                                 new java.sql.Date(filtro.getVigenteDesde().getTime()) : null);
            sentenciaSQL.setDate(14,
                                 filtro.getVigenteHasta() != null ?
                                 new java.sql.Date(filtro.getVigenteHasta().getTime()) : null);
            reIns = sentenciaSQL.executeUpdate();
            System.out.println("reIns: " + reIns);
            sentenciaSQL.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|guardarFiltro: No se pudo insertar el filtro, " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|guardarFiltro: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception error) {
                throw new EJBException("FiltroEJB|guardarFiltro: No se cerró la conexion " + error.getMessage());
            }
        }
        return reIns;
    }

    public void actualizarFiltro(Filtro filtro) {
        int reUpd = 0;
        PreparedStatement sentenciaSQL;
        String sqlUpdate = "UPDATE FILTROS SET VIGENTE_HASTA = ?, ESTADO_FILTRO = ? WHERE ID = ?";
        try {
            this.establecerConexion();
        } catch (Exception e) {
            throw new EJBException("FiltroEJB|actualizarFiltro: No se creó la conexion " + e.getMessage());
        }
        try {
            System.out.println("sqlUpdate: " + sqlUpdate);
            sentenciaSQL = this.sesionBD.prepareStatement(sqlUpdate);
            sentenciaSQL.setDate(1,
                                 filtro.getVigenteHasta() != null ?
                                 new java.sql.Date(filtro.getVigenteHasta().getTime()) : null);
            sentenciaSQL.setString(2, filtro.getEstadoFiltro());
            sentenciaSQL.setInt(3, filtro.getId());
            reUpd = sentenciaSQL.executeUpdate();
            System.out.println("reUpd: " + reUpd);
            sentenciaSQL.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|actualizarFiltro: No se pudo actualizar el filtro, " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|actualizarFiltro: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception error) {
                throw new EJBException("FiltroEJB|actualizarFiltro: No se cerró la conexion " + error.getMessage());
            }
        }
    }
    

    public void aprobarFiltro(Filtro filtro) {
        int reUpd = 0;
        PreparedStatement sentenciaSQL;
        String sqlUpdate =
            "UPDATE FILTROS SET VIGENTE_DESDE = ?, ESTADO_FILTRO = ?, CONCEPTO_SUPERVISOR = ?, USUARIO_SUPERVISOR = ?, FECHA_SUPERVISOR = ? WHERE ID = ?";
        try {
            this.establecerConexion();
        } catch (Exception e) {
            throw new EJBException("FiltroEJB|actualizarFiltro: No se creó la conexion " + e.getMessage());
        }
        try {
            System.out.println("sqlUpdate: " + sqlUpdate);
            sentenciaSQL = this.sesionBD.prepareStatement(sqlUpdate);
            sentenciaSQL.setDate(1,
                                 filtro.getVigenteDesde() != null ?
                                 new java.sql.Date(filtro.getVigenteDesde().getTime()) : null);
            sentenciaSQL.setString(2, filtro.getEstadoFiltro());
            sentenciaSQL.setString(3, filtro.getConceptoSupervisor());
            sentenciaSQL.setLong(4, filtro.getUsuarioSupervisor());
            sentenciaSQL.setDate(5,
                                 filtro.getFechaSupervisor() != null ?
                                 new java.sql.Date(filtro.getFechaSupervisor().getTime()) : null);
            sentenciaSQL.setInt(6, filtro.getId());
            reUpd = sentenciaSQL.executeUpdate();
            System.out.println("reUpd: " + reUpd);
            sentenciaSQL.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|aprobarFiltro: No se pudo actualizar el filtro, " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|aprobarFiltro: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception error) {
                throw new EJBException("FiltroEJB|aprobarFiltro: No se cerró la conexion " + error.getMessage());
            }
        }
    }

    public void rechazarFiltro(Filtro filtro) {
        int reUpd = 0;
        PreparedStatement sentenciaSQL;
        String sqlUpdate =
            "UPDATE FILTROS SET ESTADO_FILTRO = ?, CONCEPTO_SUPERVISOR = ?, USUARIO_SUPERVISOR = ?, FECHA_SUPERVISOR = ? WHERE ID = ?";
        try {
            this.establecerConexion();
        } catch (Exception e) {
            throw new EJBException("FiltroEJB|actualizarFiltro: No se creó la conexion " + e.getMessage());
        }
        try {
            System.out.println("sqlUpdate: " + sqlUpdate);
            sentenciaSQL = this.sesionBD.prepareStatement(sqlUpdate);
            sentenciaSQL.setString(1, filtro.getEstadoFiltro());
            sentenciaSQL.setString(2, filtro.getConceptoSupervisor());
            sentenciaSQL.setLong(3, filtro.getUsuarioSupervisor());
            sentenciaSQL.setDate(4,
                                 filtro.getFechaSupervisor() != null ?
                                 new java.sql.Date(filtro.getFechaSupervisor().getTime()) : null);
            sentenciaSQL.setInt(5, filtro.getId());
            reUpd = sentenciaSQL.executeUpdate();
            System.out.println("reUpd: " + reUpd);
            sentenciaSQL.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|rechazarFiltro: No se pudo actualizar el filtro, " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|rechazarFiltro: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception error) {
                throw new EJBException("FiltroEJB|rechazarFiltro: No se cerró la conexion " + error.getMessage());
            }
        }
    }

    public Filtro getFiltro() {
        return this.filtro;
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

    private Integer traerIdFiltro() {
        Integer idFiltro = null;
        try {
            this.establecerConexion();
            miTabla = new TablaBD("FILTROS", "ID", this.sesionBD);
            idFiltro = miTabla.traerIDSecuencia("SEQ_FILTROS");
        } catch (Exception error) {
            System.out.println("FiltroEJB|traerIdFiltro1: " + error.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception e) {
                System.out.println("FiltroEJB|traerIdFiltro2: " + e.getMessage());
            }
        }
        return idFiltro;
    }

    @Override
    public Filtro findByPrimaryKey(Integer primaryKey) throws Exception {
        Query query;
        query = entityManager.createNamedQuery("Filtro.findByPrimaryKey", Filtro.class);
        query.setParameter("id", primaryKey);
        return (Filtro) query.getSingleResult();
    }

    @Override
    public Collection findAll() throws FinderException {
        // TODO Implement this method
        return Collections.emptySet();
    }

    public Collection filtrosCliente(Hashtable cliente, Hashtable datosRegistro) {
        String consulta = null;
        String ti = (String) cliente.get("TIPO_IDENTIFICACION");
        String ni = (String) cliente.get("NUMERO_IDENTIFICACION");
        String un = (String) cliente.get("OFICINA");
        String codCargo = (String) datosRegistro.get("CODIGO_CARGO");
        String codPerfil = (String) datosRegistro.get("CODIGO_PERFIL");
        String codTipoCargo = (String) datosRegistro.get("CODIGO_TIPO_CARGO");
        if (codPerfil.equals("2")) {
            consulta =
                "SELECT TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION," +
                "CODIGO_PRODUCTO, NUMERO_NEGOCIO, NOMBRE,tipo_producto FROM(" +
                "SELECT t.tipo_identificacion TIPO_IDENTIFICACION," + "t.numero_identificacion NUMERO_IDENTIFICACION," +
                "a.codigo_producto_v CODIGO_PRODUCTO, " + " p.nombre_corto tipo_producto," +
                "t.numero_negocio NUMERO_NEGOCIO," + " NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre" +
                " FROM transacciones_cliente t,clientes c,archivos a, lista_valores p" +
                " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                " AND c.numero_identificacion(+)  = t.numero_identificacion" +
                " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " + " AND t.codigo_oficina            = " +
                un + " UNION ALL" + " SELECT t.tipo_identificacion TIPO_IDENTIFICACION," +
                " t.numero_identificacion NUMERO_IDENTIFICACION," + " a.codigo_producto_v CODIGO_PRODUCTO," +
                " p.nombre_corto tipo_producto," + " t.numero_negocio NUMERO_NEGOCIO," +
                " NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre " +
                " FROM siscla_his.transacciones_cliente t,clientes c,archivos a, lista_valores p" +
                " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                " AND c.numero_identificacion(+)  = t.numero_identificacion" +
                " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " + " AND t.codigo_oficina            = " +
                un + ")";
        } else {
            consulta =
                "SELECT TIPO_IDENTIFICACION,NUMERO_IDENTIFICACION," +
                " CODIGO_PRODUCTO, NUMERO_NEGOCIO,NOMBRE,tipo_producto FROM(" +
                " SELECT t.tipo_identificacion,t.numero_identificacion " +
                " ,a.codigo_producto_v codigo_producto,p.nombre_corto tipo_producto " +
                " ,t.numero_negocio,NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre " +
                " FROM transacciones_cliente t, clientes c, archivos a " +
                ",lista_valores p, unidades_negocio un, analista_region ar " +
                " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                " AND c.numero_identificacion(+)  = t.numero_identificacion " +
                " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " +
                " AND t.codigo_oficina            = un.codigo " +
                " AND un.codigo_region_v          = ar.codigo_region_v " + " AND ar.codigo_cargo             = '" +
                codCargo + "'" + " UNION ALL" + " SELECT t.tipo_identificacion,t.numero_identificacion " +
                " ,a.codigo_producto_v codigo_producto,p.nombre_corto tipo_producto " +
                " ,t.numero_negocio,NVL(c.nombre_razon_social,'NO HALLADO EN CRM') nombre " +
                " FROM siscla_his.transacciones_cliente t, clientes c, archivos a " +
                ",lista_valores p, unidades_negocio un, analista_region ar " +
                " WHERE c.tipo_identificacion(+)  = t.tipo_identificacion " +
                " AND c.numero_identificacion(+)  = t.numero_identificacion " +
                " AND a.codigo                    = t.codigo_archivo " + " AND p.tipo_dato                 = '2' " +
                " AND p.codigo                    = a.codigo_producto_v " + " AND t.tipo_identificacion       = '" +
                ti + "' " + " AND t.numero_identificacion     = '" + ni + "' " +
                " AND t.codigo_oficina            = un.codigo " +
                " AND un.codigo_region_v          = ar.codigo_region_v " + " AND ar.codigo_cargo             =  '" +
                codCargo + "'" + ")";
        }
        consulta +=
            " GROUP BY TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION,CODIGO_PRODUCTO,NUMERO_NEGOCIO,NOMBRE,tipo_producto";
        Collection resultados = null;
        try {
            resultados = consTablaEJB.consultarTabla(0, 0, null, consulta);
        } catch (Exception e) {
            System.out.println("FiltroEJB|filtrosCliente");
        }
        insertarLogConsulta(cliente, datosRegistro, consulta);
        return resultados;
    }

    public void insertarLogConsulta(Hashtable cliente, Hashtable datosRegistro, String consulta) {
        String ti = (String) cliente.get("TIPO_IDENTIFICACION");
        String ni = (String) cliente.get("NUMERO_IDENTIFICACION");
        String un = (String) cliente.get("OFICINA");
        String codUsuario = (String) datosRegistro.get("CODIGO_USUARIO");
        String dominioNt = (String) datosRegistro.get("DOMINIO_NT");
        String usuarioNt = (String) datosRegistro.get("USUARIO_NT");
        String nombrePc = (String) datosRegistro.get("NOMBRE_PC");
        String canal = (String) datosRegistro.get("CANAL");
        Hashtable datosLogConsulta = new Hashtable();
        datosLogConsulta.put("USUARIO", new Long(codUsuario));
        datosLogConsulta.put("CANAL", canal);
        datosLogConsulta.put("NOMBREPC", nombrePc);
        datosLogConsulta.put("FECHAEJECUCION", Calendar.getInstance());
        datosLogConsulta.put("QUERY", consulta);
        datosLogConsulta.put("DOMINIORED", dominioNt);
        datosLogConsulta.put("TIPOID", new String(ti));
        datosLogConsulta.put("NUMEROID", new String(ni));
        datosLogConsulta.put("TIPOPRODUCTO", new String(" "));
        datosLogConsulta.put("NUMEROPRODUCTO", new String(" "));
        datosLogConsulta.put("TIPOBUSQUEDA", new Integer(2));
        datosLogConsulta.put("TIPOTRANSACCION", new Integer(1));
        datosLogConsulta.put("USUARIONT", usuarioNt);

    }


	public void actualizarFiltro2(Filtro filtro) {
		int reUpd = 0;
        PreparedStatement sentenciaSQL;
        String sqlUpdate = "UPDATE FILTROS SET CONFIRMAR = ?, FECHA_CONFIRMACION = ?,USUARIO_CONFIRMACION= ? WHERE ID = ?";
        try {
            this.establecerConexion();
        } catch (Exception e) {
            throw new EJBException("FiltroEJB|actualizarFiltro: No se creó la conexion " + e.getMessage());
        }
        try {
            System.out.println("sqlUpdate: " + sqlUpdate);
            sentenciaSQL = this.sesionBD.prepareStatement(sqlUpdate);
            sentenciaSQL.setInt(1,filtro.getConfirmar());
            sentenciaSQL.setDate(2,                       
                    new java.sql.Date(filtro.getFechaConfirmacion().getTime()));
            sentenciaSQL.setLong(3, filtro.getUsuarioConfirmacion());
            sentenciaSQL.setInt(4, filtro.getId());
            reUpd = sentenciaSQL.executeUpdate();
            System.out.println("reUpd: " + reUpd);
            sentenciaSQL.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|actualizarFiltro: No se pudo actualizar el filtro, " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException("FiltroEJB|actualizarFiltro: " + e.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (Exception error) {
                throw new EJBException("FiltroEJB|actualizarFiltro: No se cerró la conexion " + error.getMessage());
            }
        }
		
	}
}
