
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CustId_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="CustId_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}SPName" minOccurs="0"/>
 *         &lt;sequence>
 *           &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}CustPermId" minOccurs="0"/>
 *           &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}CustLoginId" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}CustType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustId_Type", propOrder = {
    "spName",
    "custPermId",
    "custLoginId",
    "custType"
})
public class CustIdType {

    @XmlElement(name = "SPName")
    protected String spName;
    @XmlElement(name = "CustPermId")
    protected String custPermId;
    @XmlElement(name = "CustLoginId",namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String custLoginId;
    @XmlElement(name = "CustType")
    protected String custType;

    /**
     * 
     * 						En este campo se parametriza el nombre del proveedor de servicio.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPName() {
        return spName;
    }

    /**
     * Define el valor de la propiedad spName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPName(String value) {
        this.spName = value;
    }

    /**
     * 
     * 							En este campo se parametriza ID permanente del cliente, usado como una 
     * 	    					llave de base de datos para identificar de forma única un intercambio
     * 	    					financiero o cliente CSP. No puede ser modificado por el cliente.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustPermId() {
        return custPermId;
    }

    /**
     * Define el valor de la propiedad custPermId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustPermId(String value) {
        this.custPermId = value;
    }

    /**
     * 
     * 							En este campo se parametriza el ID inicio sesión del cliente.
     * 							Usuario STA Campo de tres partes, el tercero  muestra el ID 
     * 							del usuario que realizó la modificación.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustLoginId() {
        return custLoginId;
    }

    /**
     * Define el valor de la propiedad custLoginId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustLoginId(String value) {
        this.custLoginId = value;
    }

    /**
     * 
     * 						En este campo se parametriza el tipo de cliente.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustType() {
        return custType;
    }

    /**
     * Define el valor de la propiedad custType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustType(String value) {
        this.custType = value;
    }

}
