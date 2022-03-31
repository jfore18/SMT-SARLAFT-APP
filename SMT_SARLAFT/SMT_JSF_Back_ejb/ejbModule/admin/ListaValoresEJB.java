package admin;

import java.sql.SQLException;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface ListaValoresEJB {
    boolean crearValor(Hashtable pDatos) throws SQLException;

    Collection listarValores(String pTipoDato) throws SQLException;

    void actualizarValor(Hashtable pDatos) throws SQLException;
}
