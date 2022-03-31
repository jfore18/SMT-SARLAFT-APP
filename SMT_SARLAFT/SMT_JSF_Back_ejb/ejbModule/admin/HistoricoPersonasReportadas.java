package admin;

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
              @NamedQuery(name = "HistoricoPersonasReportadas.findAll",
                          query = "select o from HistoricoPersonasReportadas o") })
@Table(name = "HISTORICO_PERSONAS_REPORTADAS")
public class HistoricoPersonasReportadas implements Serializable {
    private static final long serialVersionUID = 8830034855367628848L;
    @Column(nullable = false, length = 12)
    private String accion;
    @Column(length = 20)
    private String alias;
    @Column(name = "APELLIDOS_RAZON_SOCIAL", length = 60)
    private String apellidosRazonSocial;
    @Column(name = "CODIGO_MOTIVO_V", length = 3)
    private String codigoMotivoV;
    @Column(length = 100)
    private String comentario;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INGRESO")
    private Date fechaIngreso;
    @Id
    @Column(nullable = false)
    private Long id;
    @Column(name = "ID_REPORTE")
    private Integer idReporte;
    @Column(name = "NOMBRES_RAZON_COMERCIAL", length = 60)
    private String nombresRazonComercial;
    @Column(name = "NUMERO_IDENTIFICACION", length = 11)
    private String numeroIdentificacion;
    @Column(length = 10)
    private String ros;
    @Column(name = "TIPO_IDENTIFICACION", length = 3)
    private String tipoIdentificacion;
    @Column(name = "TIPO_REPORTE", length = 1)
    private String tipoReporte;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;

    public HistoricoPersonasReportadas() {
    }

    public HistoricoPersonasReportadas(String accion, String alias, String apellidosRazonSocial, String codigoMotivoV,
                                       String comentario, Date fechaActualizacion, Date fechaIngreso, Long id,
                                       Integer idReporte, String nombresRazonComercial, String numeroIdentificacion,
                                       String ros, String tipoIdentificacion, String tipoReporte,
                                       Long usuarioActualizacion) {
        this.accion = accion;
        this.alias = alias;
        this.apellidosRazonSocial = apellidosRazonSocial;
        this.codigoMotivoV = codigoMotivoV;
        this.comentario = comentario;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaIngreso = fechaIngreso;
        this.id = id;
        this.idReporte = idReporte;
        this.nombresRazonComercial = nombresRazonComercial;
        this.numeroIdentificacion = numeroIdentificacion;
        this.ros = ros;
        this.tipoIdentificacion = tipoIdentificacion;
        this.tipoReporte = tipoReporte;
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getApellidosRazonSocial() {
        return apellidosRazonSocial;
    }

    public void setApellidosRazonSocial(String apellidosRazonSocial) {
        this.apellidosRazonSocial = apellidosRazonSocial;
    }

    public String getCodigoMotivoV() {
        return codigoMotivoV;
    }

    public void setCodigoMotivoV(String codigoMotivoV) {
        this.codigoMotivoV = codigoMotivoV;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public String getNombresRazonComercial() {
        return nombresRazonComercial;
    }

    public void setNombresRazonComercial(String nombresRazonComercial) {
        this.nombresRazonComercial = nombresRazonComercial;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getRos() {
        return ros;
    }

    public void setRos(String ros) {
        this.ros = ros;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public Long getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Long usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }
}
