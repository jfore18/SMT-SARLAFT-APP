
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DocIndexInfo_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DocIndexInfo_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/support/v1/}DocIndexTypeCode" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocIndexMetaData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}FileInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocIndexInfo_Type", propOrder = {
    "docIndexTypeCode",
    "docIndexMetaData",
    "fileInfo"
})
public class DocIndexInfoType {

    @XmlElement(name = "DocIndexTypeCode", namespace = "urn://bancodebogota.com/resources/support/v1/")
    protected String docIndexTypeCode;
    @XmlElement(name = "DocIndexMetaData")
    protected List<DocIndexMetaDataType> docIndexMetaData;
    @XmlElement(name = "FileInfo")
    protected FileInfoType fileInfo;

    /**
     * Obtiene el valor de la propiedad docIndexTypeCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocIndexTypeCode() {
        return docIndexTypeCode;
    }

    /**
     * Define el valor de la propiedad docIndexTypeCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocIndexTypeCode(String value) {
        this.docIndexTypeCode = value;
    }

    /**
     * Gets the value of the docIndexMetaData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docIndexMetaData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocIndexMetaData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocIndexMetaDataType }
     * 
     * 
     */
    public List<DocIndexMetaDataType> getDocIndexMetaData() {
        if (docIndexMetaData == null) {
            docIndexMetaData = new ArrayList<DocIndexMetaDataType>();
        }
        return this.docIndexMetaData;
    }

    /**
     * Obtiene el valor de la propiedad fileInfo.
     * 
     * @return
     *     possible object is
     *     {@link FileInfoType }
     *     
     */
    public FileInfoType getFileInfo() {
        return fileInfo;
    }

    /**
     * Define el valor de la propiedad fileInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link FileInfoType }
     *     
     */
    public void setFileInfo(FileInfoType value) {
        this.fileInfo = value;
    }

}
