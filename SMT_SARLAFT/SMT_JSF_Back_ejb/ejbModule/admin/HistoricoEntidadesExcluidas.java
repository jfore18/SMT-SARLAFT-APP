package admin;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
              @NamedQuery(name = "HistoricoEntidadesExcluidas.findAll",
                          query = "select o from HistoricoEntidadesExcluidas o"),
              @NamedQuery(name = "HistoricoEntidadesExcluidas.findByPrimaryKey",
                          query = "select o from HistoricoEntidadesExcluidas o where o.id = :id")
    })
@Table(name = "HISTORICO_ENTIDADES_EXCLUIDAS")
public class HistoricoEntidadesExcluidas implements Serializable {
    private static final long serialVersionUID = 2027025897684452742L;
    @Column(nullable = false, length = 12)
    private String accion;
    @Column(name = "FECHA_ACTUALIZACION")
    private Timestamp fechaActualizacion;
    @Id
    @Column(nullable = false)
    private Long id;
    @Column(name = "NUMERO_IDENTIFICACION", length = 11)
    private String numeroIdentificacion;
    @Column(name = "TIPO_IDENTIFICACION", length = 3)
    private String tipoIdentificacion;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;

    public HistoricoEntidadesExcluidas() {
    }

    public HistoricoEntidadesExcluidas(String accion, Timestamp fechaActualizacion, Long id,
                                       String numeroIdentificacion, String tipoIdentificacion,
                                       Long usuarioActualizacion) {
        this.accion = accion;
        this.fechaActualizacion = fechaActualizacion;
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Long getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Long usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }
}
