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

@Entity(name = "HistoricoTransaccion")
@NamedQueries({ @NamedQuery(name = "HistoricoTransaccion.findAll", query = "select o from HistoricoTransaccion o") })
@Table(name = "HISTORICO_ESTADOS_TR")
public class HistoricoTransaccion implements Serializable {
    private static final long serialVersionUID = 3863812969036987040L;
    @Column(name = "CODIGO_ARCHIVO", nullable = false)
    private Integer codigoArchivo;
    @Column(name = "CODIGO_CARGO", length = 6)
    private String codigoCargo;
    @Column(name = "CODIGO_ESTADO_V", length = 3)
    private String codigoEstadoV;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PROCESO", nullable = false)
    private Date fechaProceso;
    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(name = "ID_TRANSACCION", nullable = false)
    private Integer idTransaccion;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;

    public HistoricoTransaccion() {
    }

    public HistoricoTransaccion(Integer codigoArchivo, String codigoCargo, String codigoEstadoV,
                                Date fechaActualizacion, Date fechaProceso, Integer id, Integer idTransaccion,
                                Long usuarioActualizacion) {
        this.codigoArchivo = codigoArchivo;
        this.codigoCargo = codigoCargo;
        this.codigoEstadoV = codigoEstadoV;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaProceso = fechaProceso;
        this.id = id;
        this.idTransaccion = idTransaccion;
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public Integer getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(Integer codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String getCodigoEstadoV() {
        return codigoEstadoV;
    }

    public void setCodigoEstadoV(String codigoEstadoV) {
        this.codigoEstadoV = codigoEstadoV;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Long usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }
}
