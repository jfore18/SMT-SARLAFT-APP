
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				En este campo se parametriza la información del estado de la respuesta de la transacción.
 * 			
 * 
 * <p>Clase Java para Status_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Status_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}StatusCode" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ServerStatusCode" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}Severity" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}StatusDesc" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ServerStatusDesc" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}AdditionalStatus" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}AsyncRsInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Status_Type", propOrder = {
    "statusCode",
    "serverStatusCode",
    "severity",
    "statusDesc",
    "serverStatusDesc",
    "additionalStatus",
    "asyncRsInfo"
})
public class StatusType {

    @XmlElement(name = "StatusCode")
    protected String statusCode;
    @XmlElement(name = "ServerStatusCode")
    protected String serverStatusCode;
    @XmlElement(name = "Severity")
    @XmlSchemaType(name = "string")
    protected SeverityType severity;
    @XmlElement(name = "StatusDesc")
    protected String statusDesc;
    @XmlElement(name = "ServerStatusDesc")
    protected String serverStatusDesc;
    @XmlElement(name = "AdditionalStatus")
    protected List<AdditionalStatusType> additionalStatus;
    @XmlElement(name = "AsyncRsInfo")
    protected AsyncRsInfoType asyncRsInfo;

    /**
     * 
     * 						En este campo estan paramentrizados todos los posibles estados en 
     * 	    				los cuales se puede encontrar una cuenta perteneciente a la entidad.
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
     * 						En este campo se parametriza el código de estado del servidor,
     * 	    				Si el valor es mayor a 0; la consulta fue exitosa. 
     * 	    				Si el valor es igual a 0 la consulta no retornó datos pero la comunicación fue exitosa.
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
     * 						En este campo se parametriza la severidad de la transacción. 
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
     * 						En este campo se parametriza la descripción del estado.
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

    /**
     * 
     * 						En este campo se parametriza la descripción estado del servidor.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerStatusDesc() {
        return serverStatusDesc;
    }

    /**
     * Define el valor de la propiedad serverStatusDesc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerStatusDesc(String value) {
        this.serverStatusDesc = value;
    }

    /**
     * 
     * 						En este campo se parametriza el estado adicional, el "StatusCode" debe contener
     * 						el código de respuesta primario, si el número de cuenta, fecha son inválidos el 
     * 						"StatusCode" podría contener uno de los dos errores y éste agregado puede 
     * 						contener el otro.
     * 					Gets the value of the additionalStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalStatusType }
     * 
     * 
     */
    public List<AdditionalStatusType> getAdditionalStatus() {
        if (additionalStatus == null) {
            additionalStatus = new ArrayList<AdditionalStatusType>();
        }
        return this.additionalStatus;
    }

    /**
     * 
     * 						En este campo se parametriza la información de respuesta asíncrona.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link AsyncRsInfoType }
     *     
     */
    public AsyncRsInfoType getAsyncRsInfo() {
        return asyncRsInfo;
    }

    /**
     * Define el valor de la propiedad asyncRsInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link AsyncRsInfoType }
     *     
     */
    public void setAsyncRsInfo(AsyncRsInfoType value) {
        this.asyncRsInfo = value;
    }

}
