//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.04 at 05:55:32 PM CET 
//


package it.polito.RNS.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VehicleTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VehicleTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CAR"/>
 *     &lt;enumeration value="CARAVAN"/>
 *     &lt;enumeration value="SHUTTLE"/>
 *     &lt;enumeration value="TRUCK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
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
