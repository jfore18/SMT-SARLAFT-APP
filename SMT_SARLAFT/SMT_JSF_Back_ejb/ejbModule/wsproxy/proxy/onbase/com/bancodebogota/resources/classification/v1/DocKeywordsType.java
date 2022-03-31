
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DocKeywords_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DocKeywords_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}KeyId" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}Operator" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}KeyName" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}KeyValue" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}Condition" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}KeyDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocKeywords_Type", propOrder = {
    "keyId",
    "operator",
    "keyName",
    "keyValue",
    "condition",
    "keyDataType"
})
public class DocKeywordsType {

    @XmlElement(name = "KeyId")
    protected String keyId;
    @XmlElement(name = "Operator", namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String operator;
    @XmlElement(name = "KeyName",namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected String keyName;
    @XmlElement(name = "KeyValue",namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected String keyValue;
    @XmlElement(name = "Condition")
    @XmlSchemaType(name = "string")
    protected LogicGateType condition;
    @XmlElement(name = "KeyDataType")
    protected String keyDataType;

    /**
     * Obtiene el valor de la propiedad keyId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyId() {
        return keyId;
    }

    /**
     * Define el valor de la propiedad keyId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyId(String value) {
        this.keyId = value;
    }

    /**
     * Obtiene el valor de la propiedad operator.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Define el valor de la propiedad operator.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperator(String value) {
        this.operator = value;
    }

    /**
     * Obtiene el valor de la propiedad keyName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * Define el valor de la propiedad keyName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyName(String value) {
        this.keyName = value;
    }

    /**
     * Obtiene el valor de la propiedad keyValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyValue() {
        return keyValue;
    }

    /**
     * Define el valor de la propiedad keyValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyValue(String value) {
        this.keyValue = value;
    }

    /**
     * Obtiene el valor de la propiedad condition.
     * 
     * @return
     *     possible object is
     *     {@link LogicGateType }
     *     
     */
    public LogicGateType getCondition() {
        return condition;
    }

    /**
     * Define el valor de la propiedad condition.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicGateType }
     *     
     */
    public void setCondition(LogicGateType value) {
        this.condition = value;
    }

    /**
     * Obtiene el valor de la propiedad keyDataType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyDataType() {
        return keyDataType;
    }

    /**
     * Define el valor de la propiedad keyDataType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyDataType(String value) {
        this.keyDataType = value;
    }

}
