
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Binary_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Binary_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ContentType" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}BinLength" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}BinData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Binary_Type", propOrder = {
    "contentType",
    "binLength",
    "binData"
})
public class BinaryType {

    @XmlElement(name = "ContentType",namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String contentType;
    @XmlElement(name = "BinLength")
    protected Long binLength;
    @XmlElement(name = "BinData",namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected byte[] binData;

    /**
     * Obtiene el valor de la propiedad contentType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Define el valor de la propiedad contentType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentType(String value) {
        this.contentType = value;
    }

    /**
     * Obtiene el valor de la propiedad binLength.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBinLength() {
        return binLength;
    }

    /**
     * Define el valor de la propiedad binLength.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBinLength(Long value) {
        this.binLength = value;
    }

    /**
     * Obtiene el valor de la propiedad binData.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBinData() {
        return binData;
    }

    /**
     * Define el valor de la propiedad binData.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBinData(byte[] value) {
        this.binData = value;
    }

}
