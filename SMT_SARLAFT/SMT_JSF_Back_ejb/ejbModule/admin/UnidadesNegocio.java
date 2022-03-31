package admin;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * To create ID generator sequence "UNIDADES_NEGOCIO_ID_SEQ_GEN":
 * CREATE SEQUENCE "UNIDADES_NEGOCIO_ID_SEQ_GEN" INCREMENT BY 50 START WITH 50;
 */
@Entity
@NamedQueries({
              @NamedQuery(name = "UnidadesNegocio.findAll", query = "select o from UnidadesNegocio o"),
              @NamedQuery(name = "UnidadesNegocio.findByPrimaryKey",
                          query = "select o from UnidadesNegocio o where o.codigo = :codigo")
    })
@Table(name = "UNIDADES_NEGOCIO")
// @ SequenceGenerator(name = "UnidadesNegocio_Id_Seq_Gen", sequenceName = "UNIDADES_NEGOCIO_ID_SEQ_GEN",
//                   allocationSize = 50, initialValue = 50)
public class UnidadesNegocio implements Serializable {
    private static final long serialVersionUID = 1617293252291506274L;
    private Integer activa;
    private Integer ceo;
    @Id
    @Column(nullable = false)
    // @ GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UnidadesNegocio_Id_Seq_Gen")
    private Integer codigo;
    @Column(name = "CODIGO_REGION_V", length = 3)
    private String codigoRegionV;
    @Column(name = "CODIGO_ZONA")
    private Integer codigoZona;
    @Column(length = 40)
    private String descripcion;
    @Column(name = "FECHA_ACTUALIZACION")
    private Timestamp fechaActualizacion;
    @Column(name = "IS_MEGABANCO")
    private Integer isMegabanco;
    @Column(name = "PLAZA_CRITICA")
    private Integer plazaCritica;
    @Column(name = "USUARIO_ACTUALIZACION")
    private Long usuarioActualizacion;

    public UnidadesNegocio() {
    }

    public UnidadesNegocio(Integer activa, Integer ceo, Integer codigo, String codigoRegionV, Integer codigoZona,
                           String descripcion, Timestamp fechaActualizacion, Integer isMegabanco, Integer plazaCritica,
                           Long usuarioActualizacion) {
        this.activa = activa;
        this.ceo = ceo;
        this.codigo = codigo;
        this.codigoRegionV = codigoRegionV;
        this.codigoZona = codigoZona;
        this.descripcion = descripcion;
        this.fechaActualizacion = fechaActualizacion;
        this.isMegabanco = isMegabanco;
        this.plazaCritica = plazaCritica;
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public Integer getActiva() {
        return activa;
    }

    public void setActiva(Integer activa) {
        this.activa = activa;
    }

    public Integer getCeo() {
        return ceo;
    }

    public void setCeo(Integer ceo) {
        this.ceo = ceo;
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

    public Integer getCodigoZona() {
        return codigoZona;
    }

    public void setCodigoZona(Integer codigoZona) {
        this.codigoZona = codigoZona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getIsMegabanco() {
        return isMegabanco;
    }

    public void setIsMegabanco(Integer isMegabanco) {
        this.isMegabanco = isMegabanco;
    }

    public Integer getPlazaCritica() {
        return plazaCritica;
    }

    public void setPlazaCritica(Integer plazaCritica) {
        this.plazaCritica = plazaCritica;
    }

    public Long getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Long usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }
}
