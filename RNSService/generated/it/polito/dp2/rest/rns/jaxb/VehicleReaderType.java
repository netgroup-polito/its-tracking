//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.08 at 12:13:05 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for VehicleReaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VehicleReaderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destination" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="position" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entryTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="state" type="{http://www.example.org/RnsInfo}VehicleStateType"/>
 *         &lt;element name="type" type="{http://www.example.org/RnsInfo}VehicleTypeType"/>
 *         &lt;element name="material" type="{http://www.example.org/RnsInfo}DangerousMaterialType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehicleReaderType", propOrder = {
    "name",
    "destination",
    "origin",
    "position",
    "entryTime",
    "state",
    "type",
    "material"
})
public class VehicleReaderType
    extends IdentifiedEntityReaderType
{

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String destination;
    @XmlElement(required = true)
    protected String origin;
    @XmlElement(required = true)
    protected String position;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar entryTime;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected VehicleStateType state;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected VehicleTypeType type;
    protected List<DangerousMaterialType> material;

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
     * Gets the value of the destination property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestination(String value) {
        this.destination = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigin(String value) {
        this.origin = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

    /**
     * Gets the value of the entryTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEntryTime() {
        return entryTime;
    }

    /**
     * Sets the value of the entryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEntryTime(XMLGregorianCalendar value) {
        this.entryTime = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link VehicleStateType }
     *     
     */
    public VehicleStateType getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleStateType }
     *     
     */
    public void setState(VehicleStateType value) {
        this.state = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link VehicleTypeType }
     *     
     */
    public VehicleTypeType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleTypeType }
     *     
     */
    public void setType(VehicleTypeType value) {
        this.type = value;
    }

    /**
     * Gets the value of the material property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the material property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMaterial().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DangerousMaterialType }
     * 
     * 
     */
    public List<DangerousMaterialType> getMaterial() {
        if (material == null) {
            material = new ArrayList<DangerousMaterialType>();
        }
        return this.material;
    }

}
