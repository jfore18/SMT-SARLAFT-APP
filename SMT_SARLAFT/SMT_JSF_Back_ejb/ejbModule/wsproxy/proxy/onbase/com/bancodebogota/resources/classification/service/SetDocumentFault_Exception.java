
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "setDocumentFault", targetNamespace = "urn://bancodebogota.com/resources/classification/service/")
public class SetDocumentFault_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private SetDocumentFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public SetDocumentFault_Exception(String message, SetDocumentFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public SetDocumentFault_Exception(String message, SetDocumentFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.bancodebogota.resources.classification.service.SetDocumentFault
     */
    public SetDocumentFault getFaultInfo() {
        return faultInfo;
    }

}
