package baseDatos;

import java.util.Collection;
import java.util.Vector;

import javax.ejb.Local;
import javax.ejb.Remote;

@Remote
public interface ConsultaTablaEJB {
    Collection consultarTabla(int pRegistrosConsulta, int pPosicionInicial, String pTabla, String pCondicion,
                              boolean usarColumnas) throws Exception;

    Integer getNumeroRegistros();

    String ejecutarProcedure(String pProcedure, String pParametros, int[] pArrayRetorno) throws java.sql.SQLException;

    Collection consultarTabla(int pRegistrosConsulta, int pPosicionInicial, String pTabla,
                              String pCondicion) throws Exception;

    String ejecutarProcedure(String pProcedure, Vector pParametros, int[] pArrayRetorno) throws java.sql.SQLException;
}
