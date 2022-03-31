package entidades;

import java.io.Serializable;

import java.util.Date;

public class TransaccionesClientePK implements Serializable {
    private Integer codigoArchivo;
    private Date fechaProceso;
    private Integer id;

    public TransaccionesClientePK() {
    }

    public TransaccionesClientePK(Integer codigoArchivo, Date fechaProceso, Integer id) {
        this.codigoArchivo = codigoArchivo;
        this.fechaProceso = fechaProceso;
        this.id = id;
    }

    public boolean equals(Object other) {
        if (other instanceof TransaccionesClientePK) {
            final TransaccionesClientePK otherTransaccionesClientePK = (TransaccionesClientePK) other;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
