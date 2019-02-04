//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.01 alle 02:45:02 PM CET 
//


package it.polito.dp2.RNS.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RoadSegmentReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RoadSegmentReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RnsInfo}SimplePlaceReaderType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="road" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="roadName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoadSegmentReaderType", propOrder = {
    "name",
    "road"
})
public class RoadSegmentReaderType
    extends SimplePlaceReaderType
{

    @XmlElement(required = true)
    protected java.lang.String name;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected String road;
    @XmlAttribute(name = "roadName")
    protected java.lang.String roadName;

    /**
     * Recupera il valore della proprietxE0 name.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Imposta il valore della proprietxE0 name.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setName(java.lang.String value) {
        this.name = value;
    }

    /**
     * Recupera il valore della proprietxE0 road.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoad() {
        return road;
    }

    /**
     * Imposta il valore della proprietxE0 road.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoad(String value) {
        this.road = value;
    }

    /**
     * Recupera il valore della proprietxE0 roadName.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getRoadName() {
        return roadName;
    }

    /**
     * Imposta il valore della proprietxE0 roadName.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setRoadName(java.lang.String value) {
        this.roadName = value;
    }

}
