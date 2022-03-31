
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.DocumentAddRsType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.IndexedDocumentAddRsType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event.KeywordsAddRsType;


/**
 * 
 * 				Define los campos obligatorios y opcionales de cualquier mensaje de 
 * 				respuesta enviado por un servicio con adopción	del estandar IFX.
 * 			
 * 
 * <p>Clase Java para SvcRs_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SvcRs_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}RqUID"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}AsyncRqUID" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}RevClientTrnSeq" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}Status"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}CustId" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}NextDay" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ServerTerminalSeqId" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}NetworkTrnInfo" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}ServerDt" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SvcRs_Type", propOrder = {
    "rqUID",
    "asyncRqUID",
    "revClientTrnSeq",
    "status",
    "custId",
    "nextDay",
    "serverTerminalSeqId",
    "networkTrnInfo",
    "serverDt"
})
@XmlSeeAlso({
    DocumentAddRsType.class,
    KeywordsAddRsType.class,
    IndexedDocumentAddRsType.class
})
public class SvcRsType {

    @XmlElement(name = "RqUID", required = true)
    protected String rqUID;
    @XmlElement(name = "AsyncRqUID")
    protected String asyncRqUID;
    @XmlElement(name = "RevClientTrnSeq")
    protected String revClientTrnSeq;
    @XmlElement(name = "Status", required = true)
    protected StatusType status;
    @XmlElement(name = "CustId")
    protected CustIdType custId;
    @XmlElement(name = "NextDay")
    protected String nextDay;
    @XmlElement(name = "ServerTerminalSeqId")
    protected String serverTerminalSeqId;
    @XmlElement(name = "NetworkTrnInfo")
    protected NetworkTrnInfoType networkTrnInfo;
    @XmlElement(name = "ServerDt")
    protected String serverDt;

    /**
     * 
     * 						Identificador único del mensaje que entrega la aplicación consumidora.
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
     * 						Identificador de solicitud asincrónica, enviado por un cliente para obtener una respuesta asíncrona 
     * 						generada por un servidor, por lo general, en el caso de que la respuesta ha tomado demasiado tiempo 
     * 						para construir y ser capaz de ser enviados de manera sincrónica.
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
     * 						Secuencia de transación del cliente para reversos.
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
     * 						En este campo se parametriza la información del estado de la respuesta de la transacción.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Define el valor de la propiedad status.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

    /**
     * 
     * 						En este campo se parametriza la información del usuario que autentica el servicio.
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
     * 						El proveedor del pago indica que éste debe realizarse normalmente para el próximo día.
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
     * 						Identifiador secuencia termianl generado por el servidor (CSP) en un ambiente ATM o POS.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerTerminalSeqId() {
        return serverTerminalSeqId;
    }

    /**
     * Define el valor de la propiedad serverTerminalSeqId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerTerminalSeqId(String value) {
        this.serverTerminalSeqId = value;
    }

    /**
     * 
     * 						En este campo se parametriza la información de la transacción en la Red sobre el procesamiento de la red, 
     * 						es decir, propietario, ubicación, código de banco y el número de referencia asignado por la red durante 
     * 						el procesamiento de la transacción.
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
     * 						Fecha del servidor.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerDt() {
        return serverDt;
    }

    /**
     * Define el valor de la propiedad serverDt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerDt(String value) {
        this.serverDt = value;
    }

}
