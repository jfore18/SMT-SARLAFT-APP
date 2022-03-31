package entidades;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "TransaccionesCliente.findAll", query = "select o from TransaccionesCliente o") })
@Table(name = "TRANSACCIONES_CLIENTE")
@IdClass(TransaccionesClientePK.class)
public class TransaccionesCliente implements Serializable {
    private static final long serialVersionUID = 2792785324003756750L;
    private Integer chequeada;
    @Column(name = "CODIGO_ACTIVIDAD_ECONOMICA", length = 5)
    private String codigoActividadEconomica;
    @Id
    @Column(name = "CODIGO_ARCHIVO", nullable = false)
    private Integer codigoArchivo;
    @Column(name = "CODIGO_OFICINA")
    private Integer codigoOficina;
    @Column(name = "CODIGO_OFICINA_ORIGEN")
    private Integer codigoOficinaOrigen;
    @Column(name = "CODIGO_TRANSACCION", length = 8)
    private String codigoTransaccion;
    @Column(name = "ESTADO_DUCC", length = 1)
    private String estadoDucc;
    @Column(name = "ESTADO_OFICINA", length = 1)
    private String estadoOficina;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PROCESO", nullable = false)
    private Date fechaProceso;
    @Column(name = "FILTRO_DUCC")
    private Integer filtroDucc;
    @Column(name = "FILTRO_OFICINA")
    private Integer filtroOficina;
    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(name = "MAYOR_RIESGO")
    private Integer mayorRiesgo;
    @Column(name = "NOMBRE_CLIENTE", length = 40)
    private String nombreCliente;
    @Column(name = "NO_COMENTARIOS")
    private Integer noComentarios;
    @Column(name = "NO_COMENTARIOS_DUCC")
    private Integer noComentariosDucc;
    @Column(name = "NO_CRITERIOS")
    private Integer noCriterios;
    private Integer nueva;
    @Column(name = "NUMERO_IDENTIFICACION", length = 11)
    private String numeroIdentificacion;
    @Column(name = "NUMERO_NEGOCIO", length = 20)
    private String numeroNegocio;
    @Column(name = "PROCESADA_CRITERIOS")
    private Integer procesadaCriterios;
    @Column(name = "PROCESADA_FILTROS")
    private Integer procesadaFiltros;
    @Column(name = "PROCESADA_PITUFEO")
    private Integer procesadaPitufeo;
    @Column(name = "TIPO_IDENTIFICACION", length = 3)
    private String tipoIdentificacion;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;
    @Column(name = "USUARIO_CREACION")
    private Long usuarioCreacion;
    @Column(name = "VALOR_TRANSACCION")
    private Long valorTransaccion;

    public TransaccionesCliente() {
    }

    public TransaccionesCliente(Integer chequeada, String codigoActividadEconomica, Integer codigoArchivo,
                                Integer codigoOficina, Integer codigoOficinaOrigen, String codigoTransaccion,
                                String estadoDucc, String estadoOficina, Date fecha, Date fechaActualizacion,
                                Date fechaProceso, Integer filtroDucc, Integer filtroOficina, Integer id,
                                Integer mayorRiesgo, Integer noComentarios, Integer noComentariosDucc,
                                Integer noCriterios, String nombreCliente, Integer nueva, String numeroIdentificacion,
                                String numeroNegocio, Integer procesadaCriterios, Integer procesadaFiltros,
                                Integer procesadaPitufeo, String tipoIdentificacion, Long usuarioActualizacion,
                                Long usuarioCreacion, Long valorTransaccion) {
        this.chequeada = chequeada;
        this.codigoActividadEconomica = codigoActividadEconomica;
        this.codigoArchivo = codigoArchivo;
        this.codigoOficina = codigoOficina;
        this.codigoOficinaOrigen = codigoOficinaOrigen;
        this.codigoTransaccion = codigoTransaccion;
        this.estadoDucc = estadoDucc;
        this.estadoOficina = estadoOficina;
        this.fecha = fecha;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaProceso = fechaProceso;
        this.filtroDucc = filtroDucc;
        this.filtroOficina = filtroOficina;
        this.id = id;
        this.mayorRiesgo = mayorRiesgo;
        this.noComentarios = noComentarios;
        this.noComentariosDucc = noComentariosDucc;
        this.noCriterios = noCriterios;
        this.nombreCliente = nombreCliente;
        this.nueva = nueva;
        this.numeroIdentificacion = numeroIdentificacion;
        this.numeroNegocio = numeroNegocio;
        this.procesadaCriterios = procesadaCriterios;
        this.procesadaFiltros = procesadaFiltros;
        this.procesadaPitufeo = procesadaPitufeo;
        this.tipoIdentificacion = tipoIdentificacion;
        this.usuarioActualizacion = usuarioActualizacion;
        this.usuarioCreacion = usuarioCreacion;
        this.valorTransaccion = valorTransaccion;
    }

    public Integer getChequeada() {
        return chequeada;
    }

    public void setChequeada(Integer chequeada) {
        this.chequeada = chequeada;
    }

    public String getCodigoActividadEconomica() {
        return codigoActividadEconomica;
    }

    public void setCodigoActividadEconomica(String codigoActividadEconomica) {
        this.codigoActividadEconomica = codigoActividadEconomica;
    }

    public Integer getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(Integer codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public Integer getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(Integer codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public Integer getCodigoOficinaOrigen() {
        return codigoOficinaOrigen;
    }

    public void setCodigoOficinaOrigen(Integer codigoOficinaOrigen) {
        this.codigoOficinaOrigen = codigoOficinaOrigen;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public String getEstadoDucc() {
        return estadoDucc;
    }

    public void setEstadoDucc(String estadoDucc) {
        this.estadoDucc = estadoDucc;
    }

    public String getEstadoOficina() {
        return estadoOficina;
    }

    public void setEstadoOficina(String estadoOficina) {
        this.estadoOficina = estadoOficina;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public Integer getFiltroDucc() {
        return filtroDucc;
    }

    public void setFiltroDucc(Integer filtroDucc) {
        this.filtroDucc = filtroDucc;
    }

    public Integer getFiltroOficina() {
        return filtroOficina;
    }

    public void setFiltroOficina(Integer filtroOficina) {
        this.filtroOficina = filtroOficina;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMayorRiesgo() {
        return mayorRiesgo;
    }

    public void setMayorRiesgo(Integer mayorRiesgo) {
        this.mayorRiesgo = mayorRiesgo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Integer getNoComentarios() {
        return noComentarios;
    }

    public void setNoComentarios(Integer noComentarios) {
        this.noComentarios = noComentarios;
    }

    public Integer getNoComentariosDucc() {
        return noComentariosDucc;
    }

    public void setNoComentariosDucc(Integer noComentariosDucc) {
        this.noComentariosDucc = noComentariosDucc;
    }

    public Integer getNoCriterios() {
        return noCriterios;
    }

    public void setNoCriterios(Integer noCriterios) {
        this.noCriterios = noCriterios;
    }

    public Integer getNueva() {
        return nueva;
    }

    public void setNueva(Integer nueva) {
        this.nueva = nueva;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNumeroNegocio() {
        return numeroNegocio;
    }

    public void setNumeroNegocio(String numeroNegocio) {
        this.numeroNegocio = numeroNegocio;
    }

    public Integer getProcesadaCriterios() {
        return procesadaCriterios;
    }

    public void setProcesadaCriterios(Integer procesadaCriterios) {
        this.procesadaCriterios = procesadaCriterios;
    }

    public Integer getProcesadaFiltros() {
        return procesadaFiltros;
    }

    public void setProcesadaFiltros(Integer procesadaFiltros) {
        this.procesadaFiltros = procesadaFiltros;
    }

    public Integer getProcesadaPitufeo() {
        return procesadaPitufeo;
    }

    public void setProcesadaPitufeo(Integer procesadaPitufeo) {
        this.procesadaPitufeo = procesadaPitufeo;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Long getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Long usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Long getValorTransaccion() {
        return valorTransaccion;
    }

    public void setValorTransaccion(Long valorTransaccion) {
        this.valorTransaccion = valorTransaccion;
    }
}
