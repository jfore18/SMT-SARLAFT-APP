
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.SvcRsType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.FileInfoType;


/**
 * <p>Clase Java para IndexedDocumentAddRs_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="IndexedDocumentAddRs_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://bancodebogota.com/ifx/base/v1/}SvcRs_Type">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}FileInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndexedDocumentAddRs_Type", propOrder = {
    "fileInfo"
})
public class IndexedDocumentAddRsType
    extends SvcRsType
{

    @XmlElement(name = "FileInfo", namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected FileInfoType fileInfo;

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
