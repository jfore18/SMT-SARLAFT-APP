package admin.autenticacion;

import java.util.Collection;

public interface Autenticacion {
    public void validadarUsuarioEnLdap(String dominio, String usuario) throws AutenticacionException;

    public Collection<String> getDominios() throws AutenticacionException, ConsultaDominiosException;

    public void autenticar(String username, String password, String dominio) throws AutenticacionException;
}
