package transaccion;

import java.io.Serializable;

public class DetalleAnalisisRepPK implements Serializable {
    private Long idReporte;
    private Integer noActa;

    public DetalleAnalisisRepPK() {
    }

    public DetalleAnalisisRepPK(Long idReporte, Integer noActa) {
        this.idReporte = idReporte;
        this.noActa = noActa;
    }

    public boolean equals(Object other) {
        if (other instanceof DetalleAnalisisRepPK) {
            final DetalleAnalisisRepPK otherDetalleAnalisisRepPK = (DetalleAnalisisRepPK) other;
            final boolean areEqual = true;
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public Integer getNoActa() {
        return noActa;
    }

    public void setNoActa(Integer noActa) {
        this.noActa = noActa;
    }
}
