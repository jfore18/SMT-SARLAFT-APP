
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1.SvcRsType;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1.DocKeywordsType;


/**
 * <p>Clase Java para KeywordsAddRs_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="KeywordsAddRs_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://bancodebogota.com/ifx/base/v1/}SvcRs_Type">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocHandle" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DocKeywords" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KeywordsAddRs_Type", propOrder = {
    "docHandle",
    "docKeywords"
})
public class KeywordsAddRsType
    extends SvcRsType
{

    @XmlElement(name = "DocHandle", namespace = "urn://bancodebogota.com/resources/classification/v1/", type = Long.class)
    protected List<Long> docHandle;
    @XmlElement(name = "DocKeywords", namespace = "urn://bancodebogota.com/resources/classification/v1/")
    protected List<DocKeywordsType> docKeywords;

    /**
     * Gets the value of the docHandle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docHandle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocHandle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getDocHandle() {
        if (docHandle == null) {
            docHandle = new ArrayList<Long>();
        }
        return this.docHandle;
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

}
