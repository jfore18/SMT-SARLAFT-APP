
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.IndexedDocumentAddRqType;


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
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/event/}IndexedDocumentAddRq"/>
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
    "indexedDocumentAddRq"
})
@XmlRootElement(name = "setIndexedDocumentRequest")
public class SetIndexedDocumentRequest {

    @XmlElement(name = "IndexedDocumentAddRq", namespace = "urn://bancodebogota.com/resources/classification/event/", required = true)
    protected IndexedDocumentAddRqType indexedDocumentAddRq;

    /**
     * Obtiene el valor de la propiedad indexedDocumentAddRq.
     * 
     * @return
     *     possible object is
     *     {@link IndexedDocumentAddRqType }
     *     
     */
    public IndexedDocumentAddRqType getIndexedDocumentAddRq() {
        return indexedDocumentAddRq;
    }

    /**
     * Define el valor de la propiedad indexedDocumentAddRq.
     * 
     * @param value
     *     allowed object is
     *     {@link IndexedDocumentAddRqType }
     *     
     */
    public void setIndexedDocumentAddRq(IndexedDocumentAddRqType value) {
        this.indexedDocumentAddRq = value;
    }

}
