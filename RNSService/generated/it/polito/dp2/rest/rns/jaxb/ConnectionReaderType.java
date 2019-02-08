//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.08 alle 05:59:35 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ConnectionReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ConnectionReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RnsInfo}IdentifiedEntityReaderType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="from" type="{http://www.example.org/RnsInfo}SimplePlaceReaderType"/&gt;
 *         &lt;element name="to" type="{http://www.example.org/RnsInfo}SimplePlaceReaderType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
     * Recupera il valore della proprietxE0 from.
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
     * Imposta il valore della proprietxE0 from.
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
     * Recupera il valore della proprietxE0 to.
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
     * Imposta il valore della proprietxE0 to.
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
