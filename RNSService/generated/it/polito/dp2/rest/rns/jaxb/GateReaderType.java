//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.06 alle 01:02:38 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per GateReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="GateReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RnsInfo}SimplePlaceReaderType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.example.org/RnsInfo}GateType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GateReaderType", propOrder = {
    "type"
})
public class GateReaderType
    extends SimplePlaceReaderType
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected GateType type;

    /**
     * Recupera il valore della proprietxE0 type.
     * 
     * @return
     *     possible object is
     *     {@link GateType }
     *     
     */
    public GateType getType() {
        return type;
    }

    /**
     * Imposta il valore della proprietxE0 type.
     * 
     * @param value
     *     allowed object is
     *     {@link GateType }
     *     
     */
    public void setType(GateType value) {
        this.type = value;
    }

}
