package admin.usuario;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
              @NamedQuery(name = "usuario.findAll", query = "select o from Usuario o"),
              @NamedQuery(name = "Usuario.findByPrimaryKey", query = "select o from Usuario o where o.cedula = :cedula")
    })
public class Usuario implements Serializable {
    private static final long serialVersionUID = 486735362604848990L;
    private Integer activo;
    @Id
    @Column(nullable = false)
    private Long cedula;
    @Column(name = "DOMINIO_USUARIO", length = 30)
    private String dominioUsuario;
    @Column(name = "FECHA_ACTUALIZACION")
    private Timestamp fechaActualizacion;
    @Column(name = "FECHA_CREACION")
    private Timestamp fechaCreacion;
    @Column(length = 40)
    private String nombre;
    @Column(length = 30)
    private String password;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;
    @Column(name = "USUARIO_CREACION")
    private Long usuarioCreacion;

    public Usuario() {
    }

    public Usuario(Integer activo, Long cedula, String dominioUsuario, Timestamp fechaActualizacion,
                   Timestamp fechaCreacion, String nombre, String password, Long usuarioActualizacion,
                   Long usuarioCreacion) {
        this.activo = activo;
        this.cedula = cedula;
        this.dominioUsuario = dominioUsuario;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCreacion = fechaCreacion;
        this.nombre = nombre;
        this.password = password;
        this.usuarioActualizacion = usuarioActualizacion;
        this.usuarioCreacion = usuarioCreacion;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
    }

    public String getDominioUsuario() {
        return dominioUsuario;
    }

    public void setDominioUsuario(String dominioUsuario) {
        this.dominioUsuario = dominioUsuario;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
