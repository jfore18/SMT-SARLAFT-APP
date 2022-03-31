package admin.seguridad;

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
              @NamedQuery(name = "logingreso.findAll", query = "select o from LogIngreso o"),
              @NamedQuery(name = "logingreso.findByPrimaryKey",
                          query = "select o from LogIngreso o where o.usuario = :IdUsuario"),
              @NamedQuery(name = "logingreso.findById", query = "select o from LogIngreso o where o.id = :id")
    })
@Table(name = "LOG_INGRESO")
public class LogIngreso implements Serializable {
    private static final long serialVersionUID = -5934003549913842236L;
    // @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_LOGIN")
    private Timestamp fechaLogin;
    // @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_LOGOFF")
    private Timestamp fechaLogoff;
    @Column(nullable = false)
    private Long id;
    @Id
    private Long usuario;

    public LogIngreso() {
    }

    public LogIngreso(Long usuario) {
        this.usuario = usuario;
    }

    public LogIngreso(Timestamp fechaLogin, Timestamp fechaLogoff, Long id, Long usuario) {
        this.fechaLogin = fechaLogin;
        this.fechaLogoff = fechaLogoff;
        this.id = id;
        this.usuario = usuario;
    }

    public Timestamp getFechaLogin() {
        return fechaLogin;
    }

    public void setFechaLogin(Timestamp fechaLogin) {
        this.fechaLogin = fechaLogin;
    }

    public Timestamp getFechaLogoff() {
        return fechaLogoff;
    }

    public void setFechaLogoff(Timestamp fechaLogoff) {
        this.fechaLogoff = fechaLogoff;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
