package baseDatos;

public class Consulta {
    private String seleccion;
    private String condicion;
    private String orden;
    private String agrupacion;
    private String tabla;
    private String[] alias;

    public Consulta() {
        seleccion = "";
        condicion = "";
        orden = "";
        agrupacion = "";
        tabla = "";
        alias = null;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String pSeleccion) {
        seleccion = pSeleccion;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String pCondicion) {
        condicion = pCondicion;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String pOrden) {
        orden = pOrden;
    }

    public String getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(String pAgrupacion) {
        agrupacion = pAgrupacion;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String pTabla) {
        tabla = pTabla;
    }

    public String[] getAlias() {
        return alias;
    }

    public void setAlias(String[] pAlias) {
        alias = pAlias;
    }
}
