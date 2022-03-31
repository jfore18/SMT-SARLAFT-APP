package transaccion;

import java.io.Serializable;

import java.math.BigDecimal;

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
@NamedQueries({
              @NamedQuery(name = "ComentarioCargo.findAll", query = "select o from ComentarioCargo o"),
              @NamedQuery(name = "ComentarioCargo.findByPrimaryKey",
                          query =
                          "select o from ComentarioCargo o where o.codigoCargo = :codigoCargo and o.idComentario = :idComentario")
    })
@Table(name = "COMENTARIO_CARGO")
@IdClass(ComentarioCargoEJBPK.class)
public class ComentarioCargo implements Serializable {
    private static final long serialVersionUID = 3382858185101373118L;
    @Id
    @Column(name = "CODIGO_CARGO", nullable = false)
    private String codigoCargo;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_REVISION")
    private Date fechaRevision;
    @Id
    @Column(name = "ID_COMENTARIO", nullable = false)
    private Integer idComentario;
    @Column(name = "USUARIO_REVISION")
    private Long usuarioRevision;

    public ComentarioCargo() {
    }

    public ComentarioCargo(String codigoCargo, Date fechaRevision, Integer idComentario, Long usuarioRevision) {
        this.codigoCargo = codigoCargo;
        this.fechaRevision = fechaRevision;
        this.idComentario = idComentario;
        this.usuarioRevision = usuarioRevision;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public Integer getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public Long getUsuarioRevision() {
        return usuarioRevision;
    }

    public void setUsuarioRevision(Long usuarioRevision) {
        this.usuarioRevision = usuarioRevision;
    }
}
