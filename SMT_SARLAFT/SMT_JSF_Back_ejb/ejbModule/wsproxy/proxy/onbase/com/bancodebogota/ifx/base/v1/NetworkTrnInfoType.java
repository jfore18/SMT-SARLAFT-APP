
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para NetworkTrnInfo_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="NetworkTrnInfo_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}NetworkOwner" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}TerminalId" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}TerminalType" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}BankId" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}Desc" minOccurs="0"/>
 *           &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}Name" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkTrnInfo_Type", propOrder = {
    "networkOwner",
    "terminalId",
    "terminalType",
    "bankId",
    "desc",
    "name"
})
public class NetworkTrnInfoType {

    @XmlElement(name = "NetworkOwner",namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String networkOwner;
    @XmlElement(name = "TerminalId")
    protected String terminalId;
    @XmlElement(name = "TerminalType")
    protected String terminalType;
    @XmlElement(name = "BankId",namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String bankId;
    @XmlElement(name = "Desc")
    protected String desc;
    @XmlElement(name = "Name")
    protected String name;

    /**
     * 
     * 						En este campo se parametriza el propietario de Red, el nombre de la persona u organización 
     * 						que posee la red de origen, los valores definidos son ATM, POS, ACH, FedNet, SWIFT, Rama, 
     * 						CallCenter, Otros.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkOwner() {
        return networkOwner;
    }

    /**
     * Define el valor de la propiedad networkOwner.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkOwner(String value) {
        this.networkOwner = value;
    }

    /**
     * 
     * 						En este campo se parametriza el identificador de terminal, tal como el código de terminal o
     * 						número de terminal ATM.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * Define el valor de la propiedad terminalId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalId(String value) {
        this.terminalId = value;
    }

    /**
     * 
     * 						En este campo se parametriza el tipo de terminal, valores definidos AdminTerm, ATM, POS, 
     * 						CustomerDevice, ECR, DialCash, TravelerCheckDispenser, FuelPump, ScripTerm, CouponTerm, 
     * 						TicketTerm, POBTerm, Teller, Utility, Vending, Payment, VRU.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalType() {
        return terminalType;
    }

    /**
     * Define el valor de la propiedad terminalType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalType(String value) {
        this.terminalType = value;
    }

    /**
     * 
     * 						En este campo se parametriza el código bancario el cual viene de CRM.
     * 	    				Indica cual banco dentro del holding esta asociado a la cuenta.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * Define el valor de la propiedad bankId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankId(String value) {
        this.bankId = value;
    }

    /**
     * 
     * 							En este campo se parametriza la descripción.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Define el valor de la propiedad desc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * 
     * 							En este campo se parametriza el nombre.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
