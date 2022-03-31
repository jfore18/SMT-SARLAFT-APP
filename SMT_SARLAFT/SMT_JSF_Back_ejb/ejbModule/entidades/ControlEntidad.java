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
@NamedQueries({
              @NamedQuery(name = "ControlEntidad.findAll", query = "select o from ControlEntidad o"),
              @NamedQuery(name = "ControlEntidad.updateFechaProeso",
                          query = "update ControlEntidad set fechaProceso = :fecha ")
    })
@Table(name = "CONTROL_ENTIDAD")
public class ControlEntidad implements Serializable {
    private static final long serialVersionUID = -8154528331376700607L;
    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PROCESO", nullable = false)
    private Date fechaProceso;
    @Column(name = "NOMBRE_ENTIDAD", length = 20)
    private String nombreEntidad;

    public ControlEntidad() {
    }

    public ControlEntidad(Date fechaProceso, String nombreEntidad) {
        this.fechaProceso = fechaProceso;
        this.nombreEntidad = nombreEntidad;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }
}
