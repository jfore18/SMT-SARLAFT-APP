package admin;

import java.io.Serializable;

import java.sql.Timestamp;

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
              @NamedQuery(name = "Cargo.findAll", query = "select o from Cargo o"),
              @NamedQuery(name = "Cargo.findByPrimaryKey", query = "select o from Cargo o where o.codigo = :codigo")
    })
@Table(name = "CARGOS")
public class Cargo implements Serializable {
    private static final long serialVersionUID = -3156923909628720936L;
    private Integer activo;
    @Id
    @Column(nullable = false, length = 6)
    private String codigo;
    @Column(name = "CODIGO_PERFIL_V", length = 3)
    private String codigoPerfilV;
    @Column(name = "CODIGO_TIPO_CARGO_V", nullable = false, length = 3)
    private String codigoTipoCargoV;
    @Column(name = "CODIGO_UNIDAD_NEGOCIO", nullable = false)
    private Integer codigoUnidadNegocio;
    @Column(name = "CODIGO_USUARIO")
    private Long codigoUsuario;
    @Column(name = "FECHA_ACTUALIZACION")
    private Timestamp fechaActualizacion;
    @Column(name = "FECHA_CREACION")
    private Timestamp fechaCreacion;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;
    @Column(name = "USUARIO_CREACION")
    private Long usuarioCreacion;

    public Cargo() {
    }

    public Cargo(Integer activo, String codigo, String codigoPerfilV, String codigoTipoCargoV,
                 Integer codigoUnidadNegocio, Long codigoUsuario, Timestamp fechaActualizacion, Timestamp fechaCreacion,
                 Long usuarioActualizacion, Long usuarioCreacion) {
        this.activo = activo;
        this.codigo = codigo;
        this.codigoPerfilV = codigoPerfilV;
        this.codigoTipoCargoV = codigoTipoCargoV;
        this.codigoUnidadNegocio = codigoUnidadNegocio;
        this.codigoUsuario = codigoUsuario;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioActualizacion = usuarioActualizacion;
        this.usuarioCreacion = usuarioCreacion;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoPerfilV() {
        return codigoPerfilV;
    }

    public void setCodigoPerfilV(String codigoPerfilV) {
        this.codigoPerfilV = codigoPerfilV;
    }

    public String getCodigoTipoCargoV() {
        return codigoTipoCargoV;
    }

    public void setCodigoTipoCargoV(String codigoTipoCargoV) {
        this.codigoTipoCargoV = codigoTipoCargoV;
    }

    public Integer getCodigoUnidadNegocio() {
        return codigoUnidadNegocio;
    }

    public void setCodigoUnidadNegocio(Integer codigoUnidadNegocio) {
        this.codigoUnidadNegocio = codigoUnidadNegocio;
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

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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
}
