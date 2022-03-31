
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DocTypeRef_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DocTypeRef_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocTypeId" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocTypeName" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocTypeRef_Type", propOrder = {
    "docTypeId",
    "docTypeName"
})
public class DocTypeRefType {

    @XmlElement(name = "DocTypeId")
    protected String docTypeId;
    @XmlElement(name = "DocTypeName",namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected String docTypeName;

    /**
     * Obtiene el valor de la propiedad docTypeId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocTypeId() {
        return docTypeId;
    }

    /**
     * Define el valor de la propiedad docTypeId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocTypeId(String value) {
        this.docTypeId = value;
    }

    /**
     * Obtiene el valor de la propiedad docTypeName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocTypeName() {
        return docTypeName;
    }

    /**
     * Define el valor de la propiedad docTypeName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocTypeName(String value) {
        this.docTypeName = value;
    }

}
