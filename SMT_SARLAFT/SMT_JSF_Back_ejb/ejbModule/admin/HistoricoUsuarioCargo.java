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
@NamedQueries({ @NamedQuery(name = "HistoricoUsuarioCargo.findAll", query = "select o from HistoricoUsuarioCargo o") })
@Table(name = "HISTORICO_USUARIO_CARGO")
public class HistoricoUsuarioCargo implements Serializable {
    private static final long serialVersionUID = 4799000843082176699L;
    private Integer activo;
    @Column(name = "CODIGO_CARGO", length = 6)
    private String codigoCargo;
    @Column(name = "CODIGO_USUARIO")
    private Long codigoUsuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Id
    @Column(nullable = false)
    private Long id;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;

    public HistoricoUsuarioCargo() {
    }

    public HistoricoUsuarioCargo(Integer activo, String codigoCargo, Long codigoUsuario, Date fechaActualizacion,
                                 Long id, Long usuarioActualizacion) {
        this.activo = activo;
        this.codigoCargo = codigoCargo;
        this.codigoUsuario = codigoUsuario;
        this.fechaActualizacion = fechaActualizacion;
        this.id = id;
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Long usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }
}
