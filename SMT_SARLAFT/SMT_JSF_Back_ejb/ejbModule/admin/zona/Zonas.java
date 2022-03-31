package admin.zona;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
              @NamedQuery(name = "Zonas.findAll", query = "select o from Zonas o"),
              @NamedQuery(name = "Zonas.findByPrimaryKey",
                          query = "select o from Zonas o where o.codigo = :codigo and o.codigoRegionV = :codigoR")
    })
@IdClass(ZonasPK.class)
public class Zonas implements Serializable {
    private static final long serialVersionUID = -6753515579128509297L;
    @Id
    @Column(nullable = false)
    private Integer codigo;
    @Id
    @Column(name = "CODIGO_REGION_V", nullable = false, length = 3)
    private String codigoRegionV;
    @Column(name = "FECHA_CREACION")
    private Timestamp fechaCreacion;
    @Column(name = "NOMBRE_CORTO", length = 3)
    private String nombreCorto;
    @Column(name = "NOMBRE_LARGO", length = 30)
    private String nombreLargo;
    @Column(name = "USUARIO_CREACION")
    private Long usuarioCreacion;
    @Column(name = "FECHA_ACTUALIZACION")
    private Timestamp fechaActualizacion;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;
    @Column(name = "ACTIVA")
    private Long zActiva;
    

    public Zonas() {
    }

    public Zonas(Integer codigo, String codigoRegionV, Timestamp fechaCreacion, String nombreCorto, String nombreLargo,
                 Long usuarioCreacion, Long usuarioActualizacion, Timestamp fechaActualizacion, Long activa) {
        this.codigo = codigo;
        this.codigoRegionV = codigoRegionV;
        this.fechaCreacion = fechaCreacion;
        this.nombreCorto = nombreCorto;
        this.nombreLargo = nombreLargo;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioActualizacion=usuarioActualizacion;
        this.fechaActualizacion=fechaActualizacion;
        this.zActiva=activa;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCodigoRegionV() {
        return codigoRegionV;
    }

    public void setCodigoRegionV(String codigoRegionV) {
        this.codigoRegionV = codigoRegionV;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getNombreLargo() {
        return nombreLargo;
    }

    public void setNombreLargo(String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

	public Timestamp getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Timestamp fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Long getUsuarioActualizacion() {
		return usuarioActualizacion;
	}

	public void setUsuarioActualizacion(Long usuarioActualizacion) {
		this.usuarioActualizacion = usuarioActualizacion;
	}

	public Long getzActiva() {
		return zActiva;
	}

	public void setzActiva(Long zActiva) {
		this.zActiva = zActiva;
	}
}
