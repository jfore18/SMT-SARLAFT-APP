
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.IndexedDocumentAddRsType;


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
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/event/}IndexedDocumentAddRs"/>
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
    "indexedDocumentAddRs"
})
@XmlRootElement(name = "setIndexedDocumentResponse")
public class SetIndexedDocumentResponse {

    @XmlElement(name = "IndexedDocumentAddRs", namespace = "urn://bancodebogota.com/resources/classification/event/", required = true)
    protected IndexedDocumentAddRsType indexedDocumentAddRs;

    /**
     * Obtiene el valor de la propiedad indexedDocumentAddRs.
     * 
     * @return
     *     possible object is
     *     {@link IndexedDocumentAddRsType }
     *     
     */
    public IndexedDocumentAddRsType getIndexedDocumentAddRs() {
        return indexedDocumentAddRs;
    }

    /**
     * Define el valor de la propiedad indexedDocumentAddRs.
     * 
     * @param value
     *     allowed object is
     *     {@link IndexedDocumentAddRsType }
     *     
     */
    public void setIndexedDocumentAddRs(IndexedDocumentAddRsType value) {
        this.indexedDocumentAddRs = value;
    }

}
