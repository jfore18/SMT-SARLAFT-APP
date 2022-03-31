package baseDatos;

import javax.ejb.Local;
import javax.ejb.Remote;

@Remote
public interface ConsultaEJB {
    Consulta realizarConsulta(String pUsuario, String pCodigoConsulta);
    Consulta realizarConsultaTipoCargo(String pUsuario, String pCodigoConsulta);
}
