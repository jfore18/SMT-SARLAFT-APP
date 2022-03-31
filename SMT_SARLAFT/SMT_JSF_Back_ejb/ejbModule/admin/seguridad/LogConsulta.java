package admin.seguridad;

import java.io.Serializable;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "LogConsulta.findAll", query = "select o from LogConsulta o") })
@Table(name = "LOG_CONSULTAS")
public class LogConsulta implements Serializable {
    private static final long serialVersionUID = -5175080211200426749L;
    @Column(length = 200)
    private String canal;
    @Id
    @Column(nullable = false)
    private Long consecutivo;
    @Column(name = "DESCRIPCION_RECHAZO", length = 20)
    private String descripcionRechazo;
    @Column(name = "DOMINIO_RED", nullable = false, length = 16)
    private String dominioRed;
    /* @Column(nullable = false)
    private String enviado; */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_EJECUCION", nullable = false)
    private Calendar fechaEjecucion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ENVIO")
    private Calendar fechaEnvio;
    @Column(name = "NOMBRE_PC", nullable = false, length = 50)
    private String nombrePc;
    @Column(name = "NUMERO_ID")
    private Long numeroId;
    @Column(name = "NUMERO_PRODUCTO", length = 16)
    private String numeroProducto;
    @Column(nullable = false, length = 4000)
    private String query;
    @Column(name = "RESULTADO_TX", nullable = false)
    private Integer resultadoTx;
    @Column(name = "TIPO_BUSQUEDA", nullable = false)
    private Integer tipoBusqueda;
    @Column(name = "TIPO_ID")
    private String tipoId;
    @Column(name = "TIPO_PRODUCTO", length = 6)
    private String tipoProducto;
    @Column(name = "TIPO_TRANSACCION", nullable = false)
    private Integer tipoTransaccion;
    @Column(nullable = false)
    private Long usuario;
    @Column(name = "USUARIO_NT", nullable = false, length = 15)
    private String usuarioNt;

    public LogConsulta() {
    }

    public LogConsulta(String canal, Long consecutivo, String descripcionRechazo, String dominioRed, /* String enviado,*/
                       Calendar fechaEjecucion, Calendar fechaEnvio, String nombrePc, Long numeroId,
                       String numeroProducto, String query, Integer resultadoTx, Integer tipoBusqueda, String tipoId,
                       String tipoProducto, Integer tipoTransaccion, Long usuario, String usuarioNt) {
        this.canal = canal;
        this.consecutivo = consecutivo;
        this.descripcionRechazo = descripcionRechazo;
        this.dominioRed = dominioRed;
        this.fechaEjecucion = fechaEjecucion;
        this.fechaEnvio = fechaEnvio;
        this.nombrePc = nombrePc;
        this.numeroId = numeroId;
        this.numeroProducto = numeroProducto;
        this.query = query;
        this.resultadoTx = resultadoTx;
        this.tipoBusqueda = tipoBusqueda;
        this.tipoId = tipoId;
        this.tipoProducto = tipoProducto;
        this.tipoTransaccion = tipoTransaccion;
        this.usuario = usuario;
        this.usuarioNt = usuarioNt;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public Long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getDescripcionRechazo() {
        return descripcionRechazo;
    }

    public void setDescripcionRechazo(String descripcionRechazo) {
        this.descripcionRechazo = descripcionRechazo;
    }

    public String getDominioRed() {
        return dominioRed;
    }

    public void setDominioRed(String dominioRed) {
        this.dominioRed = dominioRed;
    }

    public Calendar getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Calendar fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public Calendar getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Calendar fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getNombrePc() {
        return nombrePc;
    }

    public void setNombrePc(String nombrePc) {
        this.nombrePc = nombrePc;
    }

    public Long getNumeroId() {
        return numeroId;
    }

    public void setNumeroId(Long numeroId) {
        this.numeroId = numeroId;
    }

    public String getNumeroProducto() {
        return numeroProducto;
    }

    public void setNumeroProducto(String numeroProducto) {
        this.numeroProducto = numeroProducto;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getResultadoTx() {
        return resultadoTx;
    }

    public void setResultadoTx(Integer resultadoTx) {
        this.resultadoTx = resultadoTx;
    }

    public Integer getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Integer tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Integer getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(Integer tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public String getUsuarioNt() {
        return usuarioNt;
    }

    public void setUsuarioNt(String usuarioNt) {
        this.usuarioNt = usuarioNt;
    }
}
