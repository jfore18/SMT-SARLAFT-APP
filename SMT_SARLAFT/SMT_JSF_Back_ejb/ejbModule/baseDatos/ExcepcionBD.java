package baseDatos;

public class ExcepcionBD extends Exception {
    public ExcepcionBD() {
        super();
    }

    public ExcepcionBD(String pMensaje) {
        super(pMensaje);
    }

}
