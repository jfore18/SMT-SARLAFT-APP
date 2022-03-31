
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.SvcRsType;


/**
 * <p>Clase Java para DocumentAddRs_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DocumentAddRs_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://bancodebogota.com/ifx/base/v1/}SvcRs_Type">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocHandle" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentAddRs_Type", propOrder = {
    "docHandle"
})
public class DocumentAddRsType
    extends SvcRsType
{

    @XmlElement(name = "DocHandle", namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected Long docHandle;

    /**
     * Obtiene el valor de la propiedad docHandle.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDocHandle() {
        return docHandle;
    }

    /**
     * Define el valor de la propiedad docHandle.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDocHandle(Long value) {
        this.docHandle = value;
    }

}
