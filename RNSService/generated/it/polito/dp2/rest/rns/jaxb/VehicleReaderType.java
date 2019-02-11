//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.12 alle 12:01:17 AM CET 
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
 * <p>Classe Java per VehicleReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="VehicleReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="destination" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="origin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="position" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="entryTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="state" type="{http://www.example.org/RnsInfo}VehicleStateType"/&gt;
 *         &lt;element name="type" type="{http://www.example.org/RnsInfo}VehicleTypeType"/&gt;
 *         &lt;element name="material" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
    protected List<String> material;

    /**
     * Recupera il valore della proprietxE0 name.
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
     * Imposta il valore della proprietxE0 name.
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
     * Recupera il valore della proprietxE0 destination.
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
     * Imposta il valore della proprietxE0 destination.
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
     * Recupera il valore della proprietxE0 origin.
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
     * Imposta il valore della proprietxE0 origin.
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
     * Recupera il valore della proprietxE0 position.
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
     * Imposta il valore della proprietxE0 position.
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
     * Recupera il valore della proprietxE0 entryTime.
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
     * Imposta il valore della proprietxE0 entryTime.
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
     * Recupera il valore della proprietxE0 state.
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
     * Imposta il valore della proprietxE0 state.
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
     * Recupera il valore della proprietxE0 type.
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
     * Imposta il valore della proprietxE0 type.
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
     * {@link String }
     * 
     * 
     */
    public List<String> getMaterial() {
        if (material == null) {
            material = new ArrayList<String>();
        }
        return this.material;
    }

}
