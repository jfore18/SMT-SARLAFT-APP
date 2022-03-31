
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.DocumentAddRqType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/event/}DocumentAddRq"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "documentAddRq"
})
@XmlRootElement(name = "setDocumentRequest")
public class SetDocumentRequest {

    @XmlElement(name = "DocumentAddRq", namespace = "urn://bancodebogota.com/resources/classification/event/", required = true)
    protected DocumentAddRqType documentAddRq;

    /**
     * Obtiene el valor de la propiedad documentAddRq.
     * 
     * @return
     *     possible object is
     *     {@link DocumentAddRqType }
     *     
     */
    public DocumentAddRqType getDocumentAddRq() {
        return documentAddRq;
    }

    /**
     * Define el valor de la propiedad documentAddRq.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentAddRqType }
     *     
     */
    public void setDocumentAddRq(DocumentAddRqType value) {
        this.documentAddRq = value;
    }

}
