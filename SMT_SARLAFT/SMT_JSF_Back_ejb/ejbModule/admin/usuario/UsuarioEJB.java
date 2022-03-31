package admin.usuario;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

import javax.naming.NamingException;

@Local
public interface UsuarioEJB {
    Usuario findByPrimaryKey(Long primaryKey) throws Exception;

    void setUsuario(Usuario usuario);

    Usuario getUsuario();

    Long create(Hashtable pDatos);

    void establecerPwd(String newPassword);

    void actualizarUsuario(Usuario usuario);

    void guardarUsuario(Usuario usuario);

    Collection buscarUsuario(String dominioNT, String usuarioNT) throws Exception;
}
