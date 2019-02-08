//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.08 alle 05:59:35 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per VehicleTypeType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="VehicleTypeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CAR"/&gt;
 *     &lt;enumeration value="CARAVAN"/&gt;
 *     &lt;enumeration value="SHUTTLE"/&gt;
 *     &lt;enumeration value="TRUCK"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VehicleTypeType")
@XmlEnum
public enum VehicleTypeType {

    CAR,
    CARAVAN,
    SHUTTLE,
    TRUCK;

    public String value() {
        return name();
    }

    public static VehicleTypeType fromValue(String v) {
        return valueOf(v);
    }

}
