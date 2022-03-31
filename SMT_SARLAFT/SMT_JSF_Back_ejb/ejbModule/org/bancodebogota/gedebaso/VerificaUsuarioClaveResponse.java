
package org.bancodebogota.gedebaso;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="verifica_usuario_claveResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "verificaUsuarioClaveResult" })
@XmlRootElement(name = "verifica_usuario_claveResponse")
public class VerificaUsuarioClaveResponse {

    @XmlElement(name = "verifica_usuario_claveResult")
    protected String verificaUsuarioClaveResult;

    /**
     * Gets the value of the verificaUsuarioClaveResult property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVerificaUsuarioClaveResult() {
        return verificaUsuarioClaveResult;
    }

    /**
     * Sets the value of the verificaUsuarioClaveResult property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVerificaUsuarioClaveResult(String value) {
        this.verificaUsuarioClaveResult = value;
    }

}
