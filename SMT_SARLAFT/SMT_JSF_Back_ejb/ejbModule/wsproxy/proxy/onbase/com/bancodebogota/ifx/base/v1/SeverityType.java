
package wsproxy.proxy.onbase.com.bancodebogota.ifx.base.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Severity_Type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="Severity_Type">
 *   &lt;restriction base="{urn://bancodebogota.com/ifx/base/v1/}ClosedEnum_Type">
 *     &lt;enumeration value="Error"/>
 *     &lt;enumeration value="Warn"/>
 *     &lt;enumeration value="Info"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Severity_Type")
@XmlEnum
public enum SeverityType {

    @XmlEnumValue("Error")
    ERROR("Error"),
    @XmlEnumValue("Warn")
    WARN("Warn"),
    @XmlEnumValue("Info")
    INFO("Info");
    private final String value;

    SeverityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SeverityType fromValue(String v) {
        for (SeverityType c : SeverityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
