package baseDatos;

public class Usuario {
    private String codigoUsuario;
    private String codigoZona;
    private String codigoRegion;
    private String nombreRegion;
    private String nombreZona;
    private String codigoPerfil;
    private String nombrePerfil;
    private String nombreUsuario;
    private String codigoTipoCargo;
    private String nombreTipoCargo;
    private String nombreCortoRegion;
    private String nombreCortoZona;
    private String codigoUnidadNegocio;
    private String nombreUnidadNegocio;
    private String codigoCargo;
    private String isMegabanco;

    public Usuario() {
        codigoUsuario = "";
        codigoZona = "";
        codigoRegion = "";
        nombreZona = "";
        nombreRegion = "";
        codigoPerfil = "";
        nombrePerfil = "";
        nombreUsuario = "";
        codigoTipoCargo = "";
        nombreTipoCargo = "";
        nombreCortoRegion = "";
        nombreCortoZona = "";
        codigoUnidadNegocio = "";
        nombreUnidadNegocio = "";
        isMegabanco = "";
    }


    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String pCodigoUsuario) {
        codigoUsuario = pCodigoUsuario;
    }

    public String getCodigoZona() {
        return codigoZona;
    }

    public void setCodigoZona(String pCodigoZona) {
        codigoZona = pCodigoZona;
    }

    public String getCodigoRegion() {
        return codigoRegion;
    }

    public void setCodigoRegion(String pCodigoRegion) {
        codigoRegion = pCodigoRegion;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String pNombreRegion) {
        nombreRegion = pNombreRegion;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String pNombreZona) {
        nombreZona = pNombreZona;
    }

    public String getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(String pCodigoPerfil) {
        codigoPerfil = pCodigoPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String pNombrePerfil) {
        nombrePerfil = pNombrePerfil;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String pNombreUsuario) {
        nombreUsuario = pNombreUsuario;
    }

    public String getCodigoTipoCargo() {
        return codigoTipoCargo;
    }

    public void setCodigoTipoCargo(String pCodigoTipoCargo) {
        codigoTipoCargo = pCodigoTipoCargo;
    }

    public String getNombreTipoCargo() {
        return nombreTipoCargo;
    }

    public void setNombreTipoCargo(String pNombreTipoCargo) {
        nombreTipoCargo = pNombreTipoCargo;
    }

    public String getNombreCortoRegion() {
        return nombreCortoRegion;
    }

    public void setNombreCortoRegion(String pNombreCortoRegion) {
        nombreCortoRegion = pNombreCortoRegion;
    }

    public String getNombreCortoZona() {
        return nombreCortoZona;
    }

    public void setNombreCortoZona(String pNombreCortoZona) {
        nombreCortoZona = pNombreCortoZona;
    }

    public String getCodigoUnidadNegocio() {
        return codigoUnidadNegocio;
    }

    public void setCodigoUnidadNegocio(String pCodigoUnidadNegocio) {
        codigoUnidadNegocio = pCodigoUnidadNegocio;
    }

    public String getNombreUnidadNegocio() {
        return nombreUnidadNegocio;
    }

    public void setNombreUnidadNegocio(String pNombreUnidadNegocio) {
        nombreUnidadNegocio = pNombreUnidadNegocio;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String newCodigoCargo) {
        codigoCargo = newCodigoCargo;
    }

    public String getIsMegabanco() {
        return isMegabanco;
    }

    public void setIsMegabanco(String newIsMegabanco) {
        isMegabanco = newIsMegabanco;
    }
}
