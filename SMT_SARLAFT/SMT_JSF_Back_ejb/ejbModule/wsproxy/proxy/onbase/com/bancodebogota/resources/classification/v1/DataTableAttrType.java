
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DataTableAttr_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DataTableAttr_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/support/v1/}Name" minOccurs="0"/>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DataTable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataTableAttr_Type", propOrder = {
    "name",
    "dataTable"
})
public class DataTableAttrType {

    @XmlElement(name = "Name", namespace = "urn://bancodebogota.com/resources/support/v1/")
    protected String name;
    @XmlElement(name = "DataTable")
    protected List<DataTableType> dataTable;

    /**
     * Obtiene el valor de la propiedad name.
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

    /**
     * Gets the value of the dataTable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataTable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataTable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataTableType }
     * 
     * 
     */
    public List<DataTableType> getDataTable() {
        if (dataTable == null) {
            dataTable = new ArrayList<DataTableType>();
        }
        return this.dataTable;
    }

}
