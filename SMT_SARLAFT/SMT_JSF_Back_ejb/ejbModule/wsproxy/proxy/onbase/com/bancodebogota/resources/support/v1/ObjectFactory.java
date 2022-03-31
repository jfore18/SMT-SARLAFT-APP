
package wsproxy.proxy.onbase.com.bancodebogota.resources.support.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bancodebogota.resources.support.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DocIndexTypeCode_QNAME = new QName("urn://bancodebogota.com/resources/support/v1/", "DocIndexTypeCode");
    private final static QName _Value_QNAME = new QName("urn://bancodebogota.com/resources/support/v1/", "Value");
    private final static QName _Name_QNAME = new QName("urn://bancodebogota.com/resources/support/v1/", "Name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bancodebogota.resources.support.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/support/v1/", name = "DocIndexTypeCode")
    public JAXBElement<String> createDocIndexTypeCode(String value) {
        return new JAXBElement<String>(_DocIndexTypeCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/support/v1/", name = "Value")
    public JAXBElement<String> createValue(String value) {
        return new JAXBElement<String>(_Value_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/support/v1/", name = "Name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

}
