package admin;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * To create ID generator sequence "CRITERIOS_INUSUALIDAD_ID_SEQ_GEN":
 * CREATE SEQUENCE "CRITERIOS_INUSUALIDAD_ID_SEQ_GEN" INCREMENT BY 50 START WITH 50;
 */
@Entity
@NamedQueries({
              @NamedQuery(name = "CriteriosInusualidad.findAll", query = "select o from CriteriosInusualidad o"),
              @NamedQuery(name = "CriteriosInusualidad.findByPrimaryKey",
                          query = "select o from CriteriosInusualidad o where o.id = :id")
    })
@Table(name = "CRITERIOS_INUSUALIDAD")
public class CriteriosInusualidad implements Serializable {
    private static final long serialVersionUID = -8327266625462944550L;
    private Long activo;
    @Column(name = "CODIGO_PRODUCTO_V", length = 3)
    private String codigoProductoV;
    @Column(length = 30)
    private String descripcion;
    @Column(name = "DESCRIPCION_P1", length = 20)
    private String descripcionP1;
    @Column(name = "DESCRIPCION_P2", length = 20)
    private String descripcionP2;
    @Column(name = "DESCRIPCION_P3", length = 20)
    private String descripcionP3;
    @Column(name = "DESCRIPCION_P4", length = 20)
    private String descripcionP4;
    @Column(name = "DESCRIPCION_P5", length = 20)
    private String descripcionP5;
    @Column(name = "FECHA_CREACION")
    private Timestamp fechaCreacion;
    @Column(name = "FECHA_DESACTIVACION")
    private Timestamp fechaDesactivacion;
    @Column(length = 30)
    private String funcion;
    @Id
    @Column(nullable = false)
    private Long id;
    @Column(name = "LISTA_VALOR_P1", length = 20)
    private String listaValorP1;
    @Column(name = "LISTA_VALOR_P2", length = 20)
    private String listaValorP2;
    @Column(name = "LISTA_VALOR_P3", length = 20)
    private String listaValorP3;
    @Column(name = "LISTA_VALOR_P4", length = 20)
    private String listaValorP4;
    @Column(name = "LISTA_VALOR_P5", length = 20)
    private String listaValorP5;
    @Column(length = 100)
    private String mensaje;
    @Column(name = "PROCESAR_POR_GRUPOS")
    private Long procesarPorGrupos;
    @Column(name = "USUARIO_CREACION")
    private Long usuarioCreacion;
    @Column(name = "USUARIO_DESACTIVACION")
    private Long usuarioDesactivacion;
    @Column(name = "VALOR_P1", length = 50)
    private String valorP1;
    @Column(name = "VALOR_P2", length = 50)
    private String valorP2;
    @Column(name = "VALOR_P3", length = 50)
    private String valorP3;
    @Column(name = "VALOR_P4", length = 50)
    private String valorP4;
    @Column(name = "VALOR_P5", length = 50)
    private String valorP5;

    public CriteriosInusualidad() {
    }

    public CriteriosInusualidad(Long activo, String codigoProductoV, String descripcion, String descripcionP1,
                                String descripcionP2, String descripcionP3, String descripcionP4, String descripcionP5,
                                Timestamp fechaCreacion, Timestamp fechaDesactivacion, String funcion, Long id,
                                String listaValorP1, String listaValorP2, String listaValorP3, String listaValorP4,
                                String listaValorP5, String mensaje, Long procesarPorGrupos, Long usuarioCreacion,
                                Long usuarioDesactivacion, String valorP1, String valorP2, String valorP3,
                                String valorP4, String valorP5) {
        this.activo = activo;
        this.codigoProductoV = codigoProductoV;
        this.descripcion = descripcion;
        this.descripcionP1 = descripcionP1;
        this.descripcionP2 = descripcionP2;
        this.descripcionP3 = descripcionP3;
        this.descripcionP4 = descripcionP4;
        this.descripcionP5 = descripcionP5;
        this.fechaCreacion = fechaCreacion;
        this.fechaDesactivacion = fechaDesactivacion;
        this.funcion = funcion;
        this.id = id;
        this.listaValorP1 = listaValorP1;
        this.listaValorP2 = listaValorP2;
        this.listaValorP3 = listaValorP3;
        this.listaValorP4 = listaValorP4;
        this.listaValorP5 = listaValorP5;
        this.mensaje = mensaje;
        this.procesarPorGrupos = procesarPorGrupos;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioDesactivacion = usuarioDesactivacion;
        this.valorP1 = valorP1;
        this.valorP2 = valorP2;
        this.valorP3 = valorP3;
        this.valorP4 = valorP4;
        this.valorP5 = valorP5;
    }

    public Long getActivo() {
        return activo;
    }

    public void setActivo(Long activo) {
        this.activo = activo;
    }

    public String getCodigoProductoV() {
        return codigoProductoV;
    }

    public void setCodigoProductoV(String codigoProductoV) {
        this.codigoProductoV = codigoProductoV;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcionP1() {
        return descripcionP1;
    }

    public void setDescripcionP1(String descripcionP1) {
        this.descripcionP1 = descripcionP1;
    }

    public String getDescripcionP2() {
        return descripcionP2;
    }

    public void setDescripcionP2(String descripcionP2) {
        this.descripcionP2 = descripcionP2;
    }

    public String getDescripcionP3() {
        return descripcionP3;
    }

    public void setDescripcionP3(String descripcionP3) {
        this.descripcionP3 = descripcionP3;
    }

    public String getDescripcionP4() {
        return descripcionP4;
    }

    public void setDescripcionP4(String descripcionP4) {
        this.descripcionP4 = descripcionP4;
    }

    public String getDescripcionP5() {
        return descripcionP5;
    }

    public void setDescripcionP5(String descripcionP5) {
        this.descripcionP5 = descripcionP5;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaDesactivacion() {
        return fechaDesactivacion;
    }

    public void setFechaDesactivacion(Timestamp fechaDesactivacion) {
        this.fechaDesactivacion = fechaDesactivacion;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getListaValorP1() {
        return listaValorP1;
    }

    public void setListaValorP1(String listaValorP1) {
        this.listaValorP1 = listaValorP1;
    }

    public String getListaValorP2() {
        return listaValorP2;
    }

    public void setListaValorP2(String listaValorP2) {
        this.listaValorP2 = listaValorP2;
    }

    public String getListaValorP3() {
        return listaValorP3;
    }

    public void setListaValorP3(String listaValorP3) {
        this.listaValorP3 = listaValorP3;
    }

    public String getListaValorP4() {
        return listaValorP4;
    }

    public void setListaValorP4(String listaValorP4) {
        this.listaValorP4 = listaValorP4;
    }

    public String getListaValorP5() {
        return listaValorP5;
    }

    public void setListaValorP5(String listaValorP5) {
        this.listaValorP5 = listaValorP5;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getProcesarPorGrupos() {
        return procesarPorGrupos;
    }

    public void setProcesarPorGrupos(Long procesarPorGrupos) {
        this.procesarPorGrupos = procesarPorGrupos;
    }

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Long getUsuarioDesactivacion() {
        return usuarioDesactivacion;
    }

    public void setUsuarioDesactivacion(Long usuarioDesactivacion) {
        this.usuarioDesactivacion = usuarioDesactivacion;
    }

    public String getValorP1() {
        return valorP1;
    }

    public void setValorP1(String valorP1) {
        this.valorP1 = valorP1;
    }

    public String getValorP2() {
        return valorP2;
    }

    public void setValorP2(String valorP2) {
        this.valorP2 = valorP2;
    }

    public String getValorP3() {
        return valorP3;
    }

    public void setValorP3(String valorP3) {
        this.valorP3 = valorP3;
    }

    public String getValorP4() {
        return valorP4;
    }

    public void setValorP4(String valorP4) {
        this.valorP4 = valorP4;
    }

    public String getValorP5() {
        return valorP5;
    }

    public void setValorP5(String valorP5) {
        this.valorP5 = valorP5;
    }
}
