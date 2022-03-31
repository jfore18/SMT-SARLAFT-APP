
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Chksum_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Chksum_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ChksumType" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ChksumValue" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Chksum_Type", propOrder = {
    "chksumType",
    "chksumValue"
})
public class ChksumType {

    @XmlElement(name = "ChksumType")
    protected String chksumType;
    @XmlElement(name = "ChksumValue")
    protected BigDecimal chksumValue;

    /**
     * Obtiene el valor de la propiedad chksumType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChksumType() {
        return chksumType;
    }

    /**
     * Define el valor de la propiedad chksumType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChksumType(String value) {
        this.chksumType = value;
    }

    /**
     * Obtiene el valor de la propiedad chksumValue.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getChksumValue() {
        return chksumValue;
    }

    /**
     * Define el valor de la propiedad chksumValue.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setChksumValue(BigDecimal value) {
        this.chksumValue = value;
    }

}
