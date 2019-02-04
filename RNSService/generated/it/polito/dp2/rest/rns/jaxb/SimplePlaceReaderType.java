//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.04 alle 07:00:42 PM CET 
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
 * <p>Classe Java per SimplePlaceReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SimplePlaceReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="simplePlaceName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="capacity" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="connectedPlaceId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="containerPlaceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="avgTimeSpent" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimplePlaceReaderType", propOrder = {
    "simplePlaceName",
    "capacity",
    "connectedPlaceId",
    "containerPlaceId",
    "avgTimeSpent"
})
@XmlSeeAlso({
    ParkingAreaReaderType.class,
    GateReaderType.class,
    RoadSegmentReaderType.class
})
public class SimplePlaceReaderType
    extends IdentifiedEntityReaderType
{

    @XmlElement(required = true)
    protected String simplePlaceName;
    @XmlElement(required = true)
    protected BigInteger capacity;
    protected List<String> connectedPlaceId;
    protected String containerPlaceId;
    protected BigInteger avgTimeSpent;

    /**
     * Recupera il valore della proprietxE0 simplePlaceName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimplePlaceName() {
        return simplePlaceName;
    }

    /**
     * Imposta il valore della proprietxE0 simplePlaceName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimplePlaceName(String value) {
        this.simplePlaceName = value;
    }

    /**
     * Recupera il valore della proprietxE0 capacity.
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
     * Imposta il valore della proprietxE0 capacity.
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
     * Recupera il valore della proprietxE0 containerPlaceId.
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
     * Imposta il valore della proprietxE0 containerPlaceId.
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
     * Recupera il valore della proprietxE0 avgTimeSpent.
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
     * Imposta il valore della proprietxE0 avgTimeSpent.
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
