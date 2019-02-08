//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.08 at 12:13:05 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SimplePlaceReaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimplePlaceReaderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="capacity" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="connectedPlaceId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="containerPlaceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="avgTimeSpent" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimplePlaceReaderType", propOrder = {
    "name",
    "capacity",
    "connectedPlaceId",
    "containerPlaceId",
    "avgTimeSpent"
})
@XmlSeeAlso({
    GateReaderType.class,
    ParkingAreaReaderType.class,
    RoadSegmentReaderType.class
})
public class SimplePlaceReaderType
    extends IdentifiedEntityReaderType
{

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected BigInteger capacity;
    protected List<String> connectedPlaceId;
    protected String containerPlaceId;
    protected BigInteger avgTimeSpent;

    /**
     * Gets the value of the name property.
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
     * Sets the value of the name property.
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
     * Gets the value of the capacity property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCapacity() {
        return capacity;
    }

    /**
     * Sets the value of the capacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCapacity(BigInteger value) {
        this.capacity = value;
    }

    /**
     * Gets the value of the connectedPlaceId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connectedPlaceId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectedPlaceId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getConnectedPlaceId() {
        if (connectedPlaceId == null) {
            connectedPlaceId = new ArrayList<String>();
        }
        return this.connectedPlaceId;
    }

    /**
     * Gets the value of the containerPlaceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContainerPlaceId() {
        return containerPlaceId;
    }

    /**
     * Sets the value of the containerPlaceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContainerPlaceId(String value) {
        this.containerPlaceId = value;
    }

    /**
     * Gets the value of the avgTimeSpent property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAvgTimeSpent() {
        return avgTimeSpent;
    }

    /**
     * Sets the value of the avgTimeSpent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAvgTimeSpent(BigInteger value) {
        this.avgTimeSpent = value;
    }

}
