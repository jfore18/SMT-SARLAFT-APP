
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.BinaryType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.ChksumType;


/**
 * <p>Clase Java para FileInfo_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="FileInfo_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}Identification" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}FileName" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}FilePath" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}FileType" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}FileData" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}FileStatus" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}Chksum" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileInfo_Type", propOrder = {
    "identification",
    "fileName",
    "filePath",
    "fileType",
    "fileData",
    "fileStatus",
    "chksum"
})
public class FileInfoType {

    @XmlElement(name = "Identification")
    protected String identification;
    @XmlElement(name = "FileName")
    protected String fileName;
    @XmlElement(name = "FilePath",namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected String filePath;
    @XmlElement(name = "FileType", namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String fileType;
    @XmlElement(name = "FileData", namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected BinaryType fileData;
    @XmlElement(name = "FileStatus")
    protected String fileStatus;
    @XmlElement(name = "Chksum", namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected ChksumType chksum;

    /**
     * Obtiene el valor de la propiedad identification.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * Define el valor de la propiedad identification.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentification(String value) {
        this.identification = value;
    }

    /**
     * Obtiene el valor de la propiedad fileName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Define el valor de la propiedad fileName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Obtiene el valor de la propiedad filePath.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Define el valor de la propiedad filePath.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilePath(String value) {
        this.filePath = value;
    }

    /**
     * Obtiene el valor de la propiedad fileType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Define el valor de la propiedad fileType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileType(String value) {
        this.fileType = value;
    }

    /**
     * Obtiene el valor de la propiedad fileData.
     * 
     * @return
     *     possible object is
     *     {@link BinaryType }
     *     
     */
    public BinaryType getFileData() {
        return fileData;
    }

    /**
     * Define el valor de la propiedad fileData.
     * 
     * @param value
     *     allowed object is
     *     {@link BinaryType }
     *     
     */
    public void setFileData(BinaryType value) {
        this.fileData = value;
    }

    /**
     * Obtiene el valor de la propiedad fileStatus.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileStatus() {
        return fileStatus;
    }

    /**
     * Define el valor de la propiedad fileStatus.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileStatus(String value) {
        this.fileStatus = value;
    }

    /**
     * Obtiene el valor de la propiedad chksum.
     * 
     * @return
     *     possible object is
     *     {@link ChksumType }
     *     
     */
    public ChksumType getChksum() {
        return chksum;
    }

    /**
     * Define el valor de la propiedad chksum.
     * 
     * @param value
     *     allowed object is
     *     {@link ChksumType }
     *     
     */
    public void setChksum(ChksumType value) {
        this.chksum = value;
    }

}
