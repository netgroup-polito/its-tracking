//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.12 at 12:22:10 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConnectionReaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConnectionReaderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType">
 *       &lt;sequence>
 *         &lt;element name="from" type="{http://www.example.org/RnsInfo}SimplePlaceReaderType"/>
 *         &lt;element name="to" type="{http://www.example.org/RnsInfo}SimplePlaceReaderType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConnectionReaderType", propOrder = {
    "from",
    "to"
})
public class ConnectionReaderType
    extends IdentifiedEntityReaderType
{

    @XmlElement(required = true)
    protected SimplePlaceReaderType from;
    @XmlElement(required = true)
    protected SimplePlaceReaderType to;

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link SimplePlaceReaderType }
     *     
     */
    public SimplePlaceReaderType getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimplePlaceReaderType }
     *     
     */
    public void setFrom(SimplePlaceReaderType value) {
        this.from = value;
    }

    /**
     * Gets the value of the to property.
     * 
     * @return
     *     possible object is
     *     {@link SimplePlaceReaderType }
     *     
     */
    public SimplePlaceReaderType getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimplePlaceReaderType }
     *     
     */
    public void setTo(SimplePlaceReaderType value) {
        this.to = value;
    }

}
