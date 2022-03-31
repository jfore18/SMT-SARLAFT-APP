package transaccion;

import java.io.Serializable;

public class ComentarioCargoEJBPK implements Serializable {
    public String codigoCargo;
    public Integer idComentario;

    public ComentarioCargoEJBPK() {
    }

    public ComentarioCargoEJBPK(Integer idComentario, String codigoCargo) {
        this.idComentario = idComentario;
        this.codigoCargo = codigoCargo;
    }

    public boolean equals(Object other) {
        if (other instanceof ComentarioCargoEJBPK) {
            final ComentarioCargoEJBPK otherComentarioCargoEJBPK = (ComentarioCargoEJBPK) other;

            // The following assignment statement is auto-maintained and may be overwritten!
            boolean areEqual =
                (otherComentarioCargoEJBPK.idComentario.equals(idComentario) &&
                 otherComentarioCargoEJBPK.codigoCargo.equals(codigoCargo));

            return areEqual;
        }

        return false;
    }

    public String getCodigoCargo() {
        return this.codigoCargo;
    }

    public Integer getIdComentario() {
        return this.idComentario;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public int hashCode() {
        // Add custom hashCode() impl here
        return super.hashCode();
    }
}
