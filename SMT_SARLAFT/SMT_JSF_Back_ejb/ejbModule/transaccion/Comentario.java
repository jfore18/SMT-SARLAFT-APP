package transaccion;

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
@NamedQueries({ @NamedQuery(name = "Comentario.findAll", query = "select o from Comentario o") })
@Table(name = "COMENTARIOS")
public class Comentario implements Serializable {
    private static final long serialVersionUID = 5469234390736209371L;
    @Column(name = "CODIGO_ARCHIVO")
    private Integer codigoArchivo;
    @Column(nullable = false, length = 280)
    private String comentario;
    @Column(name = "FECHA_CREACION")
    private Timestamp fechaCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PROCESO")
    private Date fechaProceso;
    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(name = "ID_TRANSACCION")
    private Integer idTransaccion;
    @Column(name = "USUARIO_CREACION")
    private Long usuarioCreacion;

    public Comentario() {
    }

    public Comentario(Integer codigoArchivo, String comentario, Timestamp fechaCreacion, Date fechaProceso, Integer id,
                      Integer idTransaccion, Long usuarioCreacion) {
        this.codigoArchivo = codigoArchivo;
        this.comentario = comentario;
        this.fechaCreacion = fechaCreacion;
        this.fechaProceso = fechaProceso;
        this.id = id;
        this.idTransaccion = idTransaccion;
        this.usuarioCreacion = usuarioCreacion;
    }

    public Integer getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(Integer codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }
}
