package entidades;

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
@NamedQueries({ @NamedQuery(name = "LogProcesos.findAll", query = "select o from LogProcesos o") })
@Table(name = "LOG_PROCESOS")
public class LogProcesos implements Serializable {
    private static final long serialVersionUID = 8882383749955070706L;
    @Column(name = "CODIGO_MENSAJE")
    private Integer codigoMensaje;
    @Column(name = "CODIGO_PROCESO", nullable = false, length = 3)
    private String codigoProceso;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_HORA_FIN")
    private Date fechaHoraFin;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_HORA_INICIO", nullable = false)
    private Date fechaHoraInicio;
    @Id
    @Column(name = "ID_PROCESO", nullable = false)
    private Long idProceso;
    @Column(name = "REGISTROS_PROCESADOS")
    private Long registrosProcesados;
    private Long usuario;

    public LogProcesos() {
    }

    public LogProcesos(Integer codigoMensaje, String codigoProceso, Date fechaHoraFin, Date fechaHoraInicio,
                       Long idProceso, Long registrosProcesados, Long usuario) {
        this.codigoMensaje = codigoMensaje;
        this.codigoProceso = codigoProceso;
        this.fechaHoraFin = fechaHoraFin;
        this.fechaHoraInicio = fechaHoraInicio;
        this.idProceso = idProceso;
        this.registrosProcesados = registrosProcesados;
        this.usuario = usuario;
    }

    public Integer getCodigoMensaje() {
        return codigoMensaje;
    }

    public void setCodigoMensaje(Integer codigoMensaje) {
        this.codigoMensaje = codigoMensaje;
    }

    public String getCodigoProceso() {
        return codigoProceso;
    }

    public void setCodigoProceso(String codigoProceso) {
        this.codigoProceso = codigoProceso;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public Long getRegistrosProcesados() {
        return registrosProcesados;
    }

    public void setRegistrosProcesados(Long registrosProcesados) {
        this.registrosProcesados = registrosProcesados;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
