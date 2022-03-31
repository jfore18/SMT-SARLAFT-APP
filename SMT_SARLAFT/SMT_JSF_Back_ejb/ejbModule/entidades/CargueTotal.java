package entidades;

import java.io.Serializable;

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
@NamedQueries({ @NamedQuery(name = "CargueTotal.findAll", query = "select o from CargueTotal o") })
@Table(name = "CARGUE_TOTAL")
@IdClass(CargueTotalPK.class)
public class CargueTotal implements Serializable {
    private static final long serialVersionUID = 8333014890136143084L;
    @Id
    @Column(name = "CODIGO_ARCHIVO", nullable = false)
    private Integer codigoArchivo;
    @Column(name = "CODIGO_MENSAJE")
    private Integer codigoMensaje;
    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PROCESO", nullable = false)
    private Date fechaProceso;
    @Column(length = 2000)
    private String registro;
    @Id
    @Column(name = "SECUENCIA_TRANSACCION", nullable = false)
    private Integer secuenciaTransaccion;

    public CargueTotal() {
    }

    public CargueTotal(Integer codigoArchivo, Integer codigoMensaje, Date fechaProceso, String registro,
                       Integer secuenciaTransaccion) {
        this.codigoArchivo = codigoArchivo;
        this.codigoMensaje = codigoMensaje;
        this.fechaProceso = fechaProceso;
        this.registro = registro;
        this.secuenciaTransaccion = secuenciaTransaccion;
    }

    public Integer getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(Integer codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public Integer getCodigoMensaje() {
        return codigoMensaje;
    }

    public void setCodigoMensaje(Integer codigoMensaje) {
        this.codigoMensaje = codigoMensaje;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Integer getSecuenciaTransaccion() {
        return secuenciaTransaccion;
    }

    public void setSecuenciaTransaccion(Integer secuenciaTransaccion) {
        this.secuenciaTransaccion = secuenciaTransaccion;
    }
}
