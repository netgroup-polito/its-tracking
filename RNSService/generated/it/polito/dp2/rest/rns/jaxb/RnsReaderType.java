//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.06 alle 01:02:38 PM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RnsReaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RnsReaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="connection" type="{http://www.example.org/RnsInfo}ConnectionReaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="gate" type="{http://www.example.org/RnsInfo}GateReaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="parkingArea" type="{http://www.example.org/RnsInfo}ParkingAreaReaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="place" type="{http://www.example.org/RnsInfo}ComplexPlaceReaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="roadSegment" type="{http://www.example.org/RnsInfo}RoadSegmentReaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="vehicle" type="{http://www.example.org/RnsInfo}VehicleReaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RnsReaderType", propOrder = {
    "connection",
    "gate",
    "parkingArea",
    "place",
    "roadSegment",
    "vehicle"
})
public class RnsReaderType {

    protected List<ConnectionReaderType> connection;
    protected List<GateReaderType> gate;
    protected List<ParkingAreaReaderType> parkingArea;
    protected List<ComplexPlaceReaderType> place;
    protected List<RoadSegmentReaderType> roadSegment;
    protected List<VehicleReaderType> vehicle;

    /**
     * Gets the value of the connection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionReaderType }
     * 
     * 
     */
    public List<ConnectionReaderType> getConnection() {
        if (connection == null) {
            connection = new ArrayList<ConnectionReaderType>();
        }
        return this.connection;
    }

    /**
     * Gets the value of the gate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GateReaderType }
     * 
     * 
     */
    public List<GateReaderType> getGate() {
        if (gate == null) {
            gate = new ArrayList<GateReaderType>();
        }
        return this.gate;
    }

    /**
     * Gets the value of the parkingArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parkingArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParkingArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParkingAreaReaderType }
     * 
     * 
     */
    public List<ParkingAreaReaderType> getParkingArea() {
        if (parkingArea == null) {
            parkingArea = new ArrayList<ParkingAreaReaderType>();
        }
        return this.parkingArea;
    }

    /**
     * Gets the value of the place property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the place property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComplexPlaceReaderType }
     * 
     * 
     */
    public List<ComplexPlaceReaderType> getPlace() {
        if (place == null) {
            place = new ArrayList<ComplexPlaceReaderType>();
        }
        return this.place;
    }

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
     * Gets the value of the vehicle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vehicle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVehicle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VehicleReaderType }
     * 
     * 
     */
    public List<VehicleReaderType> getVehicle() {
        if (vehicle == null) {
            vehicle = new ArrayList<VehicleReaderType>();
        }
        return this.vehicle;
    }

}
