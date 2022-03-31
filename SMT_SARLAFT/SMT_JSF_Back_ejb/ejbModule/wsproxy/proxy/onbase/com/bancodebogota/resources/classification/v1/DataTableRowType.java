
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DataTableRow_Type complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DataTableRow_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn://bancodebogota.com/resources/classification/v1/}DataTableCell" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataTableRow_Type", propOrder = {
    "dataTableCell"
})
public class DataTableRowType {

    @XmlElement(name = "DataTableCell")
    protected List<DataTableCellType> dataTableCell;

    /**
     * Gets the value of the dataTableCell property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataTableCell property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataTableCell().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataTableCellType }
     * 
     * 
     */
    public List<DataTableCellType> getDataTableCell() {
        if (dataTableCell == null) {
            dataTableCell = new ArrayList<DataTableCellType>();
        }
        return this.dataTableCell;
    }

}
