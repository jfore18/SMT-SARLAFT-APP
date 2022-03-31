package admin.autenticacion;

import java.util.Collection;

import javax.ejb.CreateException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AutenticacionDelegate {

    private AutenticacionEJB autenticacion = null;
    public static String urlldap = null;

    public AutenticacionDelegate() throws AutenticacionException {
        String NOMBRE_METODO = ".AutenticacionDelegate";
        try {
            autenticacion = getAutenticacionLocalHome();
            System.out.println("==> AutenticacionDelegate --> autenticacion --> " + autenticacion);
        } catch (NamingException e) {
            e.printStackTrace();
            throw new AutenticacionException("Error al localizar recurso");
        }
    }

    private static AutenticacionEJB getAutenticacionLocalHome() throws NamingException {
        final InitialContext context = new InitialContext();
        urlldap = "http://forcis01/wsautenticacion/service.asmx";
        //(String) ( new javax.naming.InitialContext().lookup("java:comp/env/urlldap"));
        System.out.println("==> AutenticacionDelegate --> urlldap --> " + urlldap);
        return (AutenticacionEJB) context.lookup("SARLAFT-EJB-AutenticacionEJB#admin.autenticacion.AutenticacionEJB");
    }

    public Collection<String> getDominios() throws AutenticacionException, ConsultaDominiosException {
        System.out.println("==> AutenticacionDelegate --> getDominios --> ...");
        return autenticacion.getDominios();
    }

    public void autenticar(String username, String password, String dominio) throws AutenticacionException {
        System.out.println("==> AutenticacionDelegate --> autenticar --> ...");
        autenticacion.autenticar(username, password, dominio);
    }
}
