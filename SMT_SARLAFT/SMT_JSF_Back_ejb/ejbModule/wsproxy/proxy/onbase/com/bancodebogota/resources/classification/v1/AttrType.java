
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Attr_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Attr_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DataTextAttr" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DataTableAttr" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Attr_Type", propOrder = {
    "dataTextAttr",
    "dataTableAttr"
})
public class AttrType {

    @XmlElement(name = "DataTextAttr")
    protected List<DataTextAttrType> dataTextAttr;
    @XmlElement(name = "DataTableAttr")
    protected List<DataTableAttrType> dataTableAttr;

    /**
     * Gets the value of the dataTextAttr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataTextAttr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataTextAttr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataTextAttrType }
     * 
     * 
     */
    public List<DataTextAttrType> getDataTextAttr() {
        if (dataTextAttr == null) {
            dataTextAttr = new ArrayList<DataTextAttrType>();
        }
        return this.dataTextAttr;
    }

    /**
     * Gets the value of the dataTableAttr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataTableAttr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataTableAttr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataTableAttrType }
     * 
     * 
     */
    public List<DataTableAttrType> getDataTableAttr() {
        if (dataTableAttr == null) {
            dataTableAttr = new ArrayList<DataTableAttrType>();
        }
        return this.dataTableAttr;
    }

}
