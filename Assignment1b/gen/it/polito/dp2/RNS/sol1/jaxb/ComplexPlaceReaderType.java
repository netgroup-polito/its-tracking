//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.01 alle 02:45:02 PM CET 
//


package it.polito.dp2.RNS.sol1.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ComplexPlaceReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ComplexPlaceReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="complexPlaceName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="totalCapacity" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="simplePlaceId" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/&gt;
 *         &lt;element name="complexPlaceId" type="{http://www.example.org/RnsInfo}ComplexPlaceReaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
    protected List<BigInteger> simplePlaceId;
    protected List<ComplexPlaceReaderType> complexPlaceId;

    /**
     * Recupera il valore della proprietxE0 complexPlaceName.
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
     * Imposta il valore della proprietxE0 complexPlaceName.
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
     * Recupera il valore della proprietxE0 totalCapacity.
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
     * Imposta il valore della proprietxE0 totalCapacity.
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
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getSimplePlaceId() {
        if (simplePlaceId == null) {
            simplePlaceId = new ArrayList<BigInteger>();
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
