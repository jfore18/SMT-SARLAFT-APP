
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.KeywordsAddRqType;


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
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/event/}KeywordsAddRq"/>
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
    "keywordsAddRq"
})
@XmlRootElement(name = "setKeywordsRequest")
public class SetKeywordsRequest {

    @XmlElement(name = "KeywordsAddRq", namespace = "urn://bancodebogota.com/resources/classification/event/", required = true)
    protected KeywordsAddRqType keywordsAddRq;

    /**
     * Obtiene el valor de la propiedad keywordsAddRq.
     * 
     * @return
     *     possible object is
     *     {@link KeywordsAddRqType }
     *     
     */
    public KeywordsAddRqType getKeywordsAddRq() {
        return keywordsAddRq;
    }

    /**
     * Define el valor de la propiedad keywordsAddRq.
     * 
     * @param value
     *     allowed object is
     *     {@link KeywordsAddRqType }
     *     
     */
    public void setKeywordsAddRq(KeywordsAddRqType value) {
        this.keywordsAddRq = value;
    }

}
