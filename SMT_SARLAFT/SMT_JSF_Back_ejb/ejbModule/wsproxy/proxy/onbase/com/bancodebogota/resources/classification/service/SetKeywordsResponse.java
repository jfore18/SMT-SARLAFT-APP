
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.KeywordsAddRsType;


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
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/event/}KeywordsAddRs"/>
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
    "keywordsAddRs"
})
@XmlRootElement(name = "setKeywordsResponse")
public class SetKeywordsResponse {

    @XmlElement(name = "KeywordsAddRs", namespace = "urn://bancodebogota.com/resources/classification/event/", required = true)
    protected KeywordsAddRsType keywordsAddRs;

    /**
     * Obtiene el valor de la propiedad keywordsAddRs.
     * 
     * @return
     *     possible object is
     *     {@link KeywordsAddRsType }
     *     
     */
    public KeywordsAddRsType getKeywordsAddRs() {
        return keywordsAddRs;
    }

    /**
     * Define el valor de la propiedad keywordsAddRs.
     * 
     * @param value
     *     allowed object is
     *     {@link KeywordsAddRsType }
     *     
     */
    public void setKeywordsAddRs(KeywordsAddRsType value) {
        this.keywordsAddRs = value;
    }

}
