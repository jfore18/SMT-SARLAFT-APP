
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AdditionalStatus_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AdditionalStatus_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}StatusCode" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ServerStatusCode" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}Severity" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}StatusDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalStatus_Type", propOrder = {
    "statusCode",
    "serverStatusCode",
    "severity",
    "statusDesc"
})
public class AdditionalStatusType {

    @XmlElement(name = "StatusCode")
    protected String statusCode;
    @XmlElement(name = "ServerStatusCode")
    protected String serverStatusCode;
    @XmlElement(name = "Severity")
    @XmlSchemaType(name = "string")
    protected SeverityType severity;
    @XmlElement(name = "StatusDesc")
    protected String statusDesc;

    /**
     * 
     * 						En este campo se parametriza el código de estatus.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Define el valor de la propiedad statusCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

    /**
     * 
     * 						En este campo se parametriza el código de estatus del servidor.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerStatusCode() {
        return serverStatusCode;
    }

    /**
     * Define el valor de la propiedad serverStatusCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerStatusCode(String value) {
        this.serverStatusCode = value;
    }

    /**
     * 
     * 						En este campo se parametriza la severidad.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link SeverityType }
     *     
     */
    public SeverityType getSeverity() {
        return severity;
    }

    /**
     * Define el valor de la propiedad severity.
     * 
     * @param value
     *     allowed object is
     *     {@link SeverityType }
     *     
     */
    public void setSeverity(SeverityType value) {
        this.severity = value;
    }

    /**
     * 
     * 						En este campo se parametriza la descripción del estatus.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * Define el valor de la propiedad statusDesc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusDesc(String value) {
        this.statusDesc = value;
    }

}
