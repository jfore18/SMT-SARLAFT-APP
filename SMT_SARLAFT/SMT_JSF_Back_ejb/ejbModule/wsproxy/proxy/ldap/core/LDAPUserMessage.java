package wsproxy.proxy.ldap.core;

public class LDAPUserMessage {
    private String proceso;
    private String user;

    public LDAPUserMessage() {
    }


    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getProceso() {
        return proceso;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
