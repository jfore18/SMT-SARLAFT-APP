package transaccion;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
              @NamedQuery(name = "Filtro.findAll", query = "select o from Filtro o"),
              @NamedQuery(name = "Filtro.findByPrimaryKey", query = "select o from Filtro o where o.id = :id")
    })
@Table(name = "FILTROS")
public class Filtro implements Serializable {
    private static final long serialVersionUID = -133574284289476044L;
    @Column(name = "CODIGO_CARGO", length = 6)
    private String codigoCargo;
    @Column(name = "CODIGO_PRODUCTO", length = 3)
    private String codigoProducto;
    @Column(name = "CONCEPTO_SUPERVISOR", length = 300)
    private String conceptoSupervisor;
    @Column(length = 50)
    private String condicion;
    private Integer confirmar;
    @Column(name = "ESTADO_FILTRO", nullable = false, length = 1)
    private String estadoFiltro;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CONFIRMACION")
    private Date fechaConfirmacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_SUPERVISOR")
    private Date fechaSupervisor;
    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(length = 300)
    private String justificacion;
    @Column(name = "NUMERO_IDENTIFICACION", length = 11)
    private String numeroIdentificacion;
    @Column(name = "NUMERO_NEGOCIO", length = 20)
    private String numeroNegocio;
    @Column(name = "TIPO_IDENTIFICACION", length = 3)
    private String tipoIdentificacion;
    @Column(name = "USUARIO_CONFIRMACION")
    private Long usuarioConfirmacion;
    @Column(name = "USUARIO_CREACION")
    private Long usuarioCreacion;
    @Column(name = "USUARIO_SUPERVISOR")
    private Long usuarioSupervisor;
    @Temporal(TemporalType.DATE)
    @Column(name = "VIGENTE_DESDE")
    private Date vigenteDesde;
    @Temporal(TemporalType.DATE)
    @Column(name = "VIGENTE_HASTA")
    private Date vigenteHasta;

    public Filtro() {
    }

    public Filtro(String codigoCargo, String codigoProducto, String conceptoSupervisor, String condicion,
                  Integer confirmar, String estadoFiltro, Date fechaConfirmacion, Date fechaCreacion,
                  Date fechaSupervisor, Integer id, String justificacion, String numeroIdentificacion,
                  String numeroNegocio, String tipoIdentificacion, Long usuarioConfirmacion, Long usuarioCreacion,
                  Long usuarioSupervisor, Date vigenteDesde, Date vigenteHasta) {
        this.codigoCargo = codigoCargo;
        this.codigoProducto = codigoProducto;
        this.conceptoSupervisor = conceptoSupervisor;
        this.condicion = condicion;
        this.confirmar = confirmar;
        this.estadoFiltro = estadoFiltro;
        this.fechaConfirmacion = fechaConfirmacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaSupervisor = fechaSupervisor;
        this.id = id;
        this.justificacion = justificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.numeroNegocio = numeroNegocio;
        this.tipoIdentificacion = tipoIdentificacion;
        this.usuarioConfirmacion = usuarioConfirmacion;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioSupervisor = usuarioSupervisor;
        this.vigenteDesde = vigenteDesde;
        this.vigenteHasta = vigenteHasta;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getConceptoSupervisor() {
        return conceptoSupervisor;
    }

    public void setConceptoSupervisor(String conceptoSupervisor) {
        this.conceptoSupervisor = conceptoSupervisor;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Integer getConfirmar() {
        return confirmar;
    }

    public void setConfirmar(Integer confirmar) {
        this.confirmar = confirmar;
    }

    public String getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(String estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public Date getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(Date fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaSupervisor() {
        return fechaSupervisor;
    }

    public void setFechaSupervisor(Date fechaSupervisor) {
        this.fechaSupervisor = fechaSupervisor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
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

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Long getUsuarioConfirmacion() {
        return usuarioConfirmacion;
    }

    public void setUsuarioConfirmacion(Long usuarioConfirmacion) {
        this.usuarioConfirmacion = usuarioConfirmacion;
    }

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Long getUsuarioSupervisor() {
        return usuarioSupervisor;
    }

    public void setUsuarioSupervisor(Long usuarioSupervisor) {
        this.usuarioSupervisor = usuarioSupervisor;
    }

    public Date getVigenteDesde() {
        return vigenteDesde;
    }

    public void setVigenteDesde(Date vigenteDesde) {
        this.vigenteDesde = vigenteDesde;
    }

    public Date getVigenteHasta() {
        return vigenteHasta;
    }

    public void setVigenteHasta(Date vigenteHasta) {
        this.vigenteHasta = vigenteHasta;
    }
}
