package transaccion;

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
@NamedQueries({ @NamedQuery(name = "DetalleAnalisisRep.findAll", query = "select o from DetalleAnalisisRep o") })
@Table(name = "DETALLE_ANALISIS_REP")
@IdClass(DetalleAnalisisRepPK.class)
public class DetalleAnalisisRep implements Serializable {
    private static final long serialVersionUID = 5837666442810912191L;
    @Column(name = "CODIGO_ESTADO_REPORTE_V", length = 3)
    private String codigoEstadoReporteV;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTA")
    private Date fechaActa;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Id
    @Column(name = "ID_REPORTE", nullable = false)
    private Long idReporte;
    @Column(name = "JUSTIFICACION_FINAL", length = 500)
    private String justificacionFinal;
    @Id
    @Column(name = "NO_ACTA", nullable = false)
    private Integer noActa;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;

    public DetalleAnalisisRep() {
    }

    public DetalleAnalisisRep(String codigoEstadoReporteV, Date fechaActa, Date fechaActualizacion, Long idReporte,
                              String justificacionFinal, Integer noActa, Long usuarioActualizacion) {
        this.codigoEstadoReporteV = codigoEstadoReporteV;
        this.fechaActa = fechaActa;
        this.fechaActualizacion = fechaActualizacion;
        this.idReporte = idReporte;
        this.justificacionFinal = justificacionFinal;
        this.noActa = noActa;
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public String getCodigoEstadoReporteV() {
        return codigoEstadoReporteV;
    }

    public void setCodigoEstadoReporteV(String codigoEstadoReporteV) {
        this.codigoEstadoReporteV = codigoEstadoReporteV;
    }

    public Date getFechaActa() {
        return fechaActa;
    }

    public void setFechaActa(Date fechaActa) {
        this.fechaActa = fechaActa;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public String getJustificacionFinal() {
        return justificacionFinal;
    }

    public void setJustificacionFinal(String justificacionFinal) {
        this.justificacionFinal = justificacionFinal;
    }

    public Integer getNoActa() {
        return noActa;
    }

    public void setNoActa(Integer noActa) {
        this.noActa = noActa;
    }

    public Long getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Long usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }
}
