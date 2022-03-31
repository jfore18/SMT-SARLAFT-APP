package admin.autenticacion;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import wsproxy.proxy.ldap.core.LDAPException;
import wsproxy.proxy.ldap.core.Ldap;


@Stateless(name = "AutenticacionEJB", mappedName = "SARLAFT-EJB-AutenticacionEJB")
@TransactionManagement(TransactionManagementType.BEAN)
public class AutenticacionEJBBean implements AutenticacionEJB {
    @Resource
    SessionContext _context;

    private String url_ldap = null;
    private Ldap ldap = null;

    public AutenticacionEJBBean() {
    }

    @Override
    public void validadarUsuarioEnLdap(String dominio, String usuario) {
    }

    @Override
    public Collection<String> getDominios() throws ConsultaDominiosException, AutenticacionException {
        getLDAP();
        try {
            //  System.out.println("==> AutenticacionEJBBean --> getDominios --> ...");
            return ldap.getDominios(Ldap.WINNT_MODE);
        } catch (LDAPException e) {
            throw new ConsultaDominiosException(e.getMessage());
        }
    }

    @Override
    public void autenticar(String username, String password, String dominio) throws AutenticacionException {
        //System.out.println("AutenticacionEJBBean|autenticar|" + username + "|" + password + "|" + dominio);
        getLDAP();
        try {
            ldap.autenticarUsuario(dominio, username, password);
        } catch (LDAPException e) {
        
            //e.printStackTrace();
            throw new AutenticacionException(e.getMessage());
        }
    }

    private void getLDAP() throws AutenticacionException {
        try {
            ldap = new Ldap(url_ldap);
        } catch (LDAPException e) {
            throw new AutenticacionException(e.getMessage());
        }
    }

    public void ejbCreate() {
        url_ldap = AutenticacionDelegate.urlldap;
    }
}
