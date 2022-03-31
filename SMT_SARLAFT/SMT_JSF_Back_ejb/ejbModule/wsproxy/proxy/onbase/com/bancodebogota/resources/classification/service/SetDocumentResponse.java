
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.DocumentAddRsType;


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
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/event/}DocumentAddRs"/>
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
    "documentAddRs"
})
@XmlRootElement(name = "setDocumentResponse")
public class SetDocumentResponse {

    @XmlElement(name = "DocumentAddRs", namespace = "urn://bancodebogota.com/resources/classification/event/", required = true)
    protected DocumentAddRsType documentAddRs;

    /**
     * Obtiene el valor de la propiedad documentAddRs.
     * 
     * @return
     *     possible object is
     *     {@link DocumentAddRsType }
     *     
     */
    public DocumentAddRsType getDocumentAddRs() {
        return documentAddRs;
    }

    /**
     * Define el valor de la propiedad documentAddRs.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentAddRsType }
     *     
     */
    public void setDocumentAddRs(DocumentAddRsType value) {
        this.documentAddRs = value;
    }

}
