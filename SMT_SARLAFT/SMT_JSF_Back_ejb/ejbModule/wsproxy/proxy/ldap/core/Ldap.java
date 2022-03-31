package wsproxy.proxy.ldap.core;

import java.util.Collection;

import wsproxy.proxy.ldap.parser.LdapParser;

import org.bancodebogota.gedebaso.Service;
import org.bancodebogota.gedebaso.ServiceSoap;

public class Ldap {
    public static final String WINNT_MODE = "WinNT";
    public static final String LDAP_MODE = "LDAP";
    private ServiceSoap ldapPort = null;

    public Ldap(String URL) throws LDAPException {
        try {
            Service service = new Service();
            ldapPort = service.getServiceSoap();

        } catch (Exception e) {
            System.out.println("Error Ldap");
            e.printStackTrace();
            throw new LDAPException(e.getMessage());
        }
    }

    public Collection<String> getDominios(String modo) throws LDAPException {
        String respuesta = null;
        try {
            respuesta = ldapPort.listaDominios(modo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LDAPException(e.getMessage());
        }
        if (respuesta == null)
            throw new LDAPException("No se obtubo respuesta del servicio web de LDAP");
        LDAPDominioMessage mensaje = LdapParser.parseDominios(respuesta);
        if (!mensaje.getProceso().equalsIgnoreCase("0"))
            throw new LDAPException(mensaje.getMensaje());
        return mensaje.getDominios();
    }

    public void autenticarUsuario(String dominio, String usuario, String password) throws LDAPException {
        String respuesta = null;
        try {
            if (dominio.equalsIgnoreCase("banbogota")) {
                respuesta = ldapPort.verificaUsuarioClave(dominio, "DC=bancodebogota,DC=net", usuario, password);
            } else {
                String dc = dominio.replace("_", "");
                String letrasDominio = dc.substring(0, 2);
                respuesta =
                    ldapPort.verificaUsuarioClave(dominio, "DC=" + letrasDominio + ",DC=bancodebogota,DC=net", usuario,
                                                  password);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LDAPException(e.getMessage());
        }
        if (respuesta == null)
            throw new LDAPException("No se obtuvo respuesta del servicio web de LDAP");

        LDAPUserMessage mensaje = LdapParser.parseUser(respuesta);
        if (!mensaje.getProceso().equalsIgnoreCase("0")) {
            System.out.println("Error Ldap2");
            throw new LDAPException(mensaje.getUser());
        }
    }

    public String getNombreUsuario(String dominio, String usuario) throws LDAPException {
        String respuesta = null;
        try {
            if (dominio.equalsIgnoreCase("banbogota")) {
                respuesta =
                    ldapPort.verificaExistenciaUsuarioxNombreDominio(dominio, "DC=bancodebogota,DC=net", usuario);
            } else {
                String dc = dominio.replace("_", "");
                String letrasDominio = dc.substring(0, 2);
                respuesta =
                    ldapPort.verificaExistenciaUsuarioxNombreDominio(dominio,
                                                                     "DC=" + letrasDominio + ",DC=bancodebogota,DC=net",
                                                                     usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LDAPException(e.getMessage());
        }
        if (respuesta == null)
            throw new LDAPException("No se obtubo respuesta del servicio web de LDAP");

        LDAPUserMessage mensaje = LdapParser.parseUser(respuesta);
        if (!mensaje.getProceso().equalsIgnoreCase("0"))
            throw new LDAPException(mensaje.getUser().trim().replaceAll("'", " ").replaceAll("\"", ""));
        return mensaje.getUser();
    }

    public static void main(String[] args) throws LDAPException {
        Ldap ldap = new Ldap("http://forcis01/wsautenticacion/service.asmx");
        ldap.autenticarUsuario("BANCODEBOGOTA", "MZULUAG", "1Marzo2011");
        System.out.println("resultado" + ldap.getDominios("winnt"));
    }
}
