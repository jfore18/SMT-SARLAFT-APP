
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AsyncRsInfo_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AsyncRsInfo_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}AvailDt" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ExpDt" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AsyncRsInfo_Type", propOrder = {
    "availDt",
    "expDt"
})
public class AsyncRsInfoType {

    @XmlElement(name = "AvailDt")
    protected String availDt;
    @XmlElement(name = "ExpDt")
    protected String expDt;

    /**
     * 
     * 						En este campo se parametriza la fecha en la que los fondos de la cuenta son disponibles.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvailDt() {
        return availDt;
    }

    /**
     * Define el valor de la propiedad availDt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvailDt(String value) {
        this.availDt = value;
    }

    /**
     * 
     * 						En este campo se parametriza la fecha de vencimiento.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpDt() {
        return expDt;
    }

    /**
     * Define el valor de la propiedad expDt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpDt(String value) {
        this.expDt = value;
    }

}
