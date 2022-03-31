package wsproxy.proxy.ldap.core;

import java.util.ArrayList;
import java.util.Collection;

public class LDAPDominioMessage {
    private String proceso;
    private String mensaje;
    private Collection<String> dominios;

    public LDAPDominioMessage() {
    }


    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getProceso() {
        return proceso;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Collection<String> getDominios() {
        return dominios;
    }

    public void addDominio(String dominio) {
        if (dominios == null)
            dominios = new ArrayList();
        dominios.add(dominio);
    }
}
