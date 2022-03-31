
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.SvcRqType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.DocDataType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.DocIndexInfoType;


/**
 * <p>Clase Java para IndexedDocumentAddRq_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="IndexedDocumentAddRq_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://bancodebogota.com/ifx/base/v1/}SvcRq_Type">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocData"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocIndexInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndexedDocumentAddRq_Type", propOrder = {
    "docData",
    "docIndexInfo"
})
public class IndexedDocumentAddRqType
    extends SvcRqType
{

    @XmlElement(name = "DocData", namespace = "urn://bancodebogota.com/resources/classification/v1/", required = true)
    protected DocDataType docData;
    @XmlElement(name = "DocIndexInfo", namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected List<DocIndexInfoType> docIndexInfo;

    /**
     * Obtiene el valor de la propiedad docData.
     * 
     * @return
     *     possible object is
     *     {@link DocDataType }
     *     
     */
    public DocDataType getDocData() {
        return docData;
    }

    /**
     * Define el valor de la propiedad docData.
     * 
     * @param value
     *     allowed object is
     *     {@link DocDataType }
     *     
     */
    public void setDocData(DocDataType value) {
        this.docData = value;
    }

    /**
     * Gets the value of the docIndexInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docIndexInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocIndexInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocIndexInfoType }
     * 
     * 
     */
    public List<DocIndexInfoType> getDocIndexInfo() {
        if (docIndexInfo == null) {
            docIndexInfo = new ArrayList<DocIndexInfoType>();
        }
        return this.docIndexInfo;
    }

}
