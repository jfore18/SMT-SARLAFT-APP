package admin.zona;

import java.io.Serializable;

public class ZonasPK implements Serializable {
    private Integer codigo;
    private String codigoRegionV;

    public ZonasPK() {
    }

    public ZonasPK(Integer codigo, String codigoRegionV) {
        this.codigo = codigo;
        this.codigoRegionV = codigoRegionV;
    }

    public boolean equals(Object other) {
        if (other instanceof ZonasPK) {
            final ZonasPK otherZonasPK = (ZonasPK) other;
            final boolean areEqual = true;
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
}
