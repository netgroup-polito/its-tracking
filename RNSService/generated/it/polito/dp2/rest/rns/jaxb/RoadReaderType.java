//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.05 alle 07:08:59 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RoadReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RoadReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="roadSegment" type="{http://www.example.org/RnsInfo}RoadSegmentReaderType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoadReaderType", propOrder = {
    "roadSegment"
})
public class RoadReaderType
    extends IdentifiedEntityReaderType
{

    @XmlElement(required = true)
    protected List<RoadSegmentReaderType> roadSegment;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the roadSegment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roadSegment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoadSegment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoadSegmentReaderType }
     * 
     * 
     */
    public List<RoadSegmentReaderType> getRoadSegment() {
        if (roadSegment == null) {
            roadSegment = new ArrayList<RoadSegmentReaderType>();
        }
        return this.roadSegment;
    }

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

}
