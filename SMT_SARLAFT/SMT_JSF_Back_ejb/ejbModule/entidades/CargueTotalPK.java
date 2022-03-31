package entidades;

import java.io.Serializable;

import java.util.Date;

public class CargueTotalPK implements Serializable {
    private Integer codigoArchivo;
    private Date fechaProceso;
    private Integer secuenciaTransaccion;

    public CargueTotalPK() {
    }

    public CargueTotalPK(Integer codigoArchivo, Date fechaProceso, Integer secuenciaTransaccion) {
        this.codigoArchivo = codigoArchivo;
        this.fechaProceso = fechaProceso;
        this.secuenciaTransaccion = secuenciaTransaccion;
    }

    public boolean equals(Object other) {
        if (other instanceof CargueTotalPK) {
            final CargueTotalPK otherCargueTotalPK = (CargueTotalPK) other;
            final boolean areEqual = true;
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Integer getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(Integer codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public Integer getSecuenciaTransaccion() {
        return secuenciaTransaccion;
    }

    public void setSecuenciaTransaccion(Integer secuenciaTransaccion) {
        this.secuenciaTransaccion = secuenciaTransaccion;
    }
}
