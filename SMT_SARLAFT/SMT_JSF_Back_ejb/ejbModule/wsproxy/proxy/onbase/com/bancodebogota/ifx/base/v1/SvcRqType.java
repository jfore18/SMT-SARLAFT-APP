
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.DocumentAddRqType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.IndexedDocumentAddRqType;


/**
 * <p>Clase Java para SvcRq_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SvcRq_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}RqUID"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}AsyncRqUID" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}RevClientTrnSeq" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}CustId" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}NextDay" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}BaseEnvr" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ClientTerminalSeqId" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}TrnStatusType" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}NetworkTrnInfo" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ClientDt" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SvcRq_Type", propOrder = {
    "rqUID",
    "asyncRqUID",
    "revClientTrnSeq",
    "custId",
    "nextDay",
    "baseEnvr",
    "clientTerminalSeqId",
    "trnStatusType",
    "networkTrnInfo",
    "clientDt"
})
@XmlSeeAlso({
    IndexedDocumentAddRqType.class,
    DocumentAddRqType.class
})
public class SvcRqType {

    @XmlElement(name = "RqUID", required = true,namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String rqUID;
    @XmlElement(name = "AsyncRqUID")
    protected String asyncRqUID;
    @XmlElement(name = "RevClientTrnSeq")
    protected String revClientTrnSeq;
    @XmlElement(name = "CustId",namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected CustIdType custId;
    @XmlElement(name = "NextDay")
    protected String nextDay;
    @XmlElement(name = "BaseEnvr")
    protected BaseEnvrType baseEnvr;
    @XmlElement(name = "ClientTerminalSeqId")
    protected String clientTerminalSeqId;
    @XmlElement(name = "TrnStatusType")
    protected String trnStatusType;
    @XmlElement(name = "NetworkTrnInfo",namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected NetworkTrnInfoType networkTrnInfo;
    @XmlElement(name = "ClientDt" ,namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String clientDt;

    /**
     * 
     * 						Identificador �nico del mensaje que entrega la aplicaci�n.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRqUID() {
        return rqUID;
    }

    /**
     * Define el valor de la propiedad rqUID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRqUID(String value) {
        this.rqUID = value;
    }

    /**
     * 
     * 						Identificador de solicitud asincr�nica, enviado por un cliente para obtener una respuesta as�ncrona 
     * 						generada por un servidor, por lo general, en el caso de que la respuesta ha tomado demasiado tiempo 
     * 						para construir y ser capaz de ser enviados de manera sincr�nica.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsyncRqUID() {
        return asyncRqUID;
    }

    /**
     * Define el valor de la propiedad asyncRqUID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsyncRqUID(String value) {
        this.asyncRqUID = value;
    }

    /**
     * 
     * 						Secuencia de transaci�n del cliente para reversos.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevClientTrnSeq() {
        return revClientTrnSeq;
    }

    /**
     * Define el valor de la propiedad revClientTrnSeq.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevClientTrnSeq(String value) {
        this.revClientTrnSeq = value;
    }

    /**
     * 
     * 						En este campo se parametriza la informaci�n del usuario que autentica el servicio.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link CustIdType }
     *     
     */
    public CustIdType getCustId() {
        return custId;
    }

    /**
     * Define el valor de la propiedad custId.
     * 
     * @param value
     *     allowed object is
     *     {@link CustIdType }
     *     
     */
    public void setCustId(CustIdType value) {
        this.custId = value;
    }

    /**
     * 
     * 						El proveedor del pago indica que �ste debe realizarse normalmente para el pr�ximo d�a.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextDay() {
        return nextDay;
    }

    /**
     * Define el valor de la propiedad nextDay.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextDay(String value) {
        this.nextDay = value;
    }

    /**
     * 
     * 						En este campo se parametriza la informaci�n del ambiente que origina la invocaci�n.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link BaseEnvrType }
     *     
     */
    public BaseEnvrType getBaseEnvr() {
        return baseEnvr;
    }

    /**
     * Define el valor de la propiedad baseEnvr.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseEnvrType }
     *     
     */
    public void setBaseEnvr(BaseEnvrType value) {
        this.baseEnvr = value;
    }

    /**
     * 
     * 						 Identificador de secuencia generado por la terminal del cliente 
     * 						 en un ambiente de oficina/CallCenter/cajero.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientTerminalSeqId() {
        return clientTerminalSeqId;
    }

    /**
     * Define el valor de la propiedad clientTerminalSeqId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientTerminalSeqId(String value) {
        this.clientTerminalSeqId = value;
    }

    /**
     * 
     * 						Tipo de estado de la transacci�n.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrnStatusType() {
        return trnStatusType;
    }

    /**
     * Define el valor de la propiedad trnStatusType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrnStatusType(String value) {
        this.trnStatusType = value;
    }

    /**
     * 
     * 						En este campo se parametriza la informaci�n de la transacci�n en la Red sobre el procesamiento de la red, 
     * 						es decir, propietario, ubicaci�n, c�digo de banco y el n�mero de referencia asignado por la red durante 
     * 						el procesamiento de la transacci�n.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link NetworkTrnInfoType }
     *     
     */
    public NetworkTrnInfoType getNetworkTrnInfo() {
        return networkTrnInfo;
    }

    /**
     * Define el valor de la propiedad networkTrnInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkTrnInfoType }
     *     
     */
    public void setNetworkTrnInfo(NetworkTrnInfoType value) {
        this.networkTrnInfo = value;
    }

    /**
     * 
     * 						Tiempo de acuerdo con el cliente, se puede comparar con el tiempo del servidor
     * 						para determinar si hay una discrepancia.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientDt() {
        return clientDt;
    }

    /**
     * Define el valor de la propiedad clientDt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientDt(String value) {
        this.clientDt = value;
    }

}
