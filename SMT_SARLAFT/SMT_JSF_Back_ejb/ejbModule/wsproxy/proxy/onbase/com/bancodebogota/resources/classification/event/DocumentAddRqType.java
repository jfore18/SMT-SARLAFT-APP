
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.SvcRqType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.DocKeywordsType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.DocTypeRefType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.FileInfoType;


/**
 * <p>Clase Java para DocumentAddRq_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DocumentAddRq_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://bancodebogota.com/ifx/base/v1/}SvcRq_Type">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/ifx/base/v1/}CreatedDt" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocKeywords" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}FileInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocTypeRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentAddRq_Type", propOrder = {
    "createdDt",
    "docKeywords",
    "fileInfo",
    "docTypeRef"
})
public class DocumentAddRqType
    extends SvcRqType
{

    @XmlElement(name = "CreatedDt", namespace = "urn://bancodebogota.com/ifx/base/v1/")
    protected String createdDt;
    @XmlElement(name = "DocKeywords", namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected List<DocKeywordsType> docKeywords;
    @XmlElement(name = "FileInfo", namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected List<FileInfoType> fileInfo;
    @XmlElement(name = "DocTypeRef", namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected DocTypeRefType docTypeRef;

    /**
     * Obtiene el valor de la propiedad createdDt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedDt() {
        return createdDt;
    }

    /**
     * Define el valor de la propiedad createdDt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedDt(String value) {
        this.createdDt = value;
    }

    /**
     * Gets the value of the docKeywords property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docKeywords property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocKeywords().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocKeywordsType }
     * 
     * 
     */
    public List<DocKeywordsType> getDocKeywords() {
        if (docKeywords == null) {
            docKeywords = new ArrayList<DocKeywordsType>();
        }
        return this.docKeywords;
    }

    /**
     * Gets the value of the fileInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fileInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFileInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FileInfoType }
     * 
     * 
     */
    public List<FileInfoType> getFileInfo() {
        if (fileInfo == null) {
            fileInfo = new ArrayList<FileInfoType>();
        }
        return this.fileInfo;
    }

    /**
     * Obtiene el valor de la propiedad docTypeRef.
     * 
     * @return
     *     possible object is
     *     {@link DocTypeRefType }
     *     
     */
    public DocTypeRefType getDocTypeRef() {
        return docTypeRef;
    }

    /**
     * Define el valor de la propiedad docTypeRef.
     * 
     * @param value
     *     allowed object is
     *     {@link DocTypeRefType }
     *     
     */
    public void setDocTypeRef(DocTypeRefType value) {
        this.docTypeRef = value;
    }

    public void setDocKeywords(List<DocKeywordsType> docKeywords) {
        this.docKeywords = docKeywords;
    }

    public void setFileInfo(List<FileInfoType> fileInfo) {
        this.fileInfo = fileInfo;
    }
}
