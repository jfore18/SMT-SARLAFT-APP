
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para LogicGate_Type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="LogicGate_Type">
 *   &lt;restriction base="{urn://bancodebogota.com/ifx/base/v1/}ClosedEnum_Type">
 *     &lt;enumeration value="AND"/>
 *     &lt;enumeration value="OR"/>
 *     &lt;enumeration value="NOT"/>
 *     &lt;enumeration value="XOR"/>
 *     &lt;enumeration value="NAND"/>
 *     &lt;enumeration value="NOR"/>
 *     &lt;enumeration value="XNOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LogicGate_Type")
@XmlEnum
public enum LogicGateType {

    AND,
    OR,
    NOT,
    XOR,
    NAND,
    NOR,
    XNOR;

    public String value() {
        return name();
    }

    public static LogicGateType fromValue(String v) {
        return valueOf(v);
    }

}
