
package wsproxy.proxy.onbase.com.bancodebogota.resources.classification.event;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bancodebogota.resources.classification.event package. 
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

    private final static QName _KeywordsAddRq_QNAME = new QName("urn://bancodebogota.com/resources/classification/event/", "KeywordsAddRq");
    private final static QName _KeywordsAddRs_QNAME = new QName("urn://bancodebogota.com/resources/classification/event/", "KeywordsAddRs");
    private final static QName _DocumentAddRs_QNAME = new QName("urn://bancodebogota.com/resources/classification/event/", "DocumentAddRs");
    private final static QName _DocumentAddRq_QNAME = new QName("urn://bancodebogota.com/resources/classification/event/", "DocumentAddRq");
    private final static QName _IndexedDocumentAddRs_QNAME = new QName("urn://bancodebogota.com/resources/classification/event/", "IndexedDocumentAddRs");
    private final static QName _IndexedDocumentAddRq_QNAME = new QName("urn://bancodebogota.com/resources/classification/event/", "IndexedDocumentAddRq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bancodebogota.resources.classification.event
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IndexedDocumentAddRqType }
     * 
     */
    public IndexedDocumentAddRqType createIndexedDocumentAddRqType() {
        return new IndexedDocumentAddRqType();
    }

    /**
     * Create an instance of {@link DocumentAddRqType }
     * 
     */
    public DocumentAddRqType createDocumentAddRqType() {
        return new DocumentAddRqType();
    }

    /**
     * Create an instance of {@link DocumentAddRsType }
     * 
     */
    public DocumentAddRsType createDocumentAddRsType() {
        return new DocumentAddRsType();
    }

    /**
     * Create an instance of {@link KeywordsAddRsType }
     * 
     */
    public KeywordsAddRsType createKeywordsAddRsType() {
        return new KeywordsAddRsType();
    }

    /**
     * Create an instance of {@link KeywordsAddRqType }
     * 
     */
    public KeywordsAddRqType createKeywordsAddRqType() {
        return new KeywordsAddRqType();
    }

    /**
     * Create an instance of {@link IndexedDocumentAddRsType }
     * 
     */
    public IndexedDocumentAddRsType createIndexedDocumentAddRsType() {
        return new IndexedDocumentAddRsType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeywordsAddRqType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/classification/event/", name = "KeywordsAddRq")
    public JAXBElement<KeywordsAddRqType> createKeywordsAddRq(KeywordsAddRqType value) {
        return new JAXBElement<KeywordsAddRqType>(_KeywordsAddRq_QNAME, KeywordsAddRqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeywordsAddRsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/classification/event/", name = "KeywordsAddRs")
    public JAXBElement<KeywordsAddRsType> createKeywordsAddRs(KeywordsAddRsType value) {
        return new JAXBElement<KeywordsAddRsType>(_KeywordsAddRs_QNAME, KeywordsAddRsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentAddRsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/classification/event/", name = "DocumentAddRs")
    public JAXBElement<DocumentAddRsType> createDocumentAddRs(DocumentAddRsType value) {
        return new JAXBElement<DocumentAddRsType>(_DocumentAddRs_QNAME, DocumentAddRsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentAddRqType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/classification/event/", name = "DocumentAddRq")
    public JAXBElement<DocumentAddRqType> createDocumentAddRq(DocumentAddRqType value) {
        return new JAXBElement<DocumentAddRqType>(_DocumentAddRq_QNAME, DocumentAddRqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndexedDocumentAddRsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/classification/event/", name = "IndexedDocumentAddRs")
    public JAXBElement<IndexedDocumentAddRsType> createIndexedDocumentAddRs(IndexedDocumentAddRsType value) {
        return new JAXBElement<IndexedDocumentAddRsType>(_IndexedDocumentAddRs_QNAME, IndexedDocumentAddRsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndexedDocumentAddRqType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://bancodebogota.com/resources/classification/event/", name = "IndexedDocumentAddRq")
    public JAXBElement<IndexedDocumentAddRqType> createIndexedDocumentAddRq(IndexedDocumentAddRqType value) {
        return new JAXBElement<IndexedDocumentAddRqType>(_IndexedDocumentAddRq_QNAME, IndexedDocumentAddRqType.class, null, value);
    }

}
