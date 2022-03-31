
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
 *         &lt;element name="TestServicioResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "testServicioResult" })
@XmlRootElement(name = "TestServicioResponse")
public class TestServicioResponse {

    @XmlElement(name = "TestServicioResult")
    protected String testServicioResult;

    /**
     * Gets the value of the testServicioResult property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTestServicioResult() {
        return testServicioResult;
    }

    /**
     * Sets the value of the testServicioResult property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTestServicioResult(String value) {
        this.testServicioResult = value;
    }

}
