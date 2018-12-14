//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.12 at 07:14:48 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ComplexPlaceReaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ComplexPlaceReaderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType">
 *       &lt;sequence>
 *         &lt;element name="complexPlaceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="totalCapacity" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="simplePlaceId" type="{http://www.example.org/RnsInfo}SimplePlaceReaderType" maxOccurs="unbounded"/>
 *         &lt;element name="complexPlaceId" type="{http://www.example.org/RnsInfo}ComplexPlaceReaderType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexPlaceReaderType", propOrder = {
    "complexPlaceName",
    "totalCapacity",
    "simplePlaceId",
    "complexPlaceId"
})
public class ComplexPlaceReaderType
    extends IdentifiedEntityReaderType
{

    @XmlElement(required = true)
    protected String complexPlaceName;
    @XmlElement(required = true)
    protected BigInteger totalCapacity;
    @XmlElement(required = true)
    protected List<SimplePlaceReaderType> simplePlaceId;
    protected List<ComplexPlaceReaderType> complexPlaceId;

    /**
     * Gets the value of the complexPlaceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplexPlaceName() {
        return complexPlaceName;
    }

    /**
     * Sets the value of the complexPlaceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplexPlaceName(String value) {
        this.complexPlaceName = value;
    }

    /**
     * Gets the value of the totalCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalCapacity() {
        return totalCapacity;
    }

    /**
     * Sets the value of the totalCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalCapacity(BigInteger value) {
        this.totalCapacity = value;
    }

    /**
     * Gets the value of the simplePlaceId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the simplePlaceId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSimplePlaceId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SimplePlaceReaderType }
     * 
     * 
     */
    public List<SimplePlaceReaderType> getSimplePlaceId() {
        if (simplePlaceId == null) {
            simplePlaceId = new ArrayList<SimplePlaceReaderType>();
        }
        return this.simplePlaceId;
    }

    /**
     * Gets the value of the complexPlaceId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complexPlaceId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplexPlaceId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComplexPlaceReaderType }
     * 
     * 
     */
    public List<ComplexPlaceReaderType> getComplexPlaceId() {
        if (complexPlaceId == null) {
            complexPlaceId = new ArrayList<ComplexPlaceReaderType>();
        }
        return this.complexPlaceId;
    }

}
