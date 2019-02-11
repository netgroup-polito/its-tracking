//
// Questo file xe8 stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0-b170531.0717 
// Vedere <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Qualsiasi modifica a questo file andrxe0 persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.02.12 alle 12:01:17 AM CET 
//


package it.polito.dp2.rest.rns.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polito.dp2.rest.rns.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Rns_QNAME = new QName("http://www.example.org/RnsInfo", "rns");
    private final static QName _Connection_QNAME = new QName("http://www.example.org/RnsInfo", "connection");
    private final static QName _ComplexPlace_QNAME = new QName("http://www.example.org/RnsInfo", "complexPlace");
    private final static QName _Place_QNAME = new QName("http://www.example.org/RnsInfo", "place");
    private final static QName _ParkingArea_QNAME = new QName("http://www.example.org/RnsInfo", "parkingArea");
    private final static QName _Gate_QNAME = new QName("http://www.example.org/RnsInfo", "gate");
    private final static QName _Road_QNAME = new QName("http://www.example.org/RnsInfo", "road");
    private final static QName _RoadSegment_QNAME = new QName("http://www.example.org/RnsInfo", "roadSegment");
    private final static QName _Vehicle_QNAME = new QName("http://www.example.org/RnsInfo", "vehicle");
    private final static QName _DangerousMaterial_QNAME = new QName("http://www.example.org/RnsInfo", "dangerousMaterial");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polito.dp2.rest.rns.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RnsReaderType }
     * 
     */
    public RnsReaderType createRnsReaderType() {
        return new RnsReaderType();
    }

    /**
     * Create an instance of {@link ConnectionReaderType }
     * 
     */
    public ConnectionReaderType createConnectionReaderType() {
        return new ConnectionReaderType();
    }

    /**
     * Create an instance of {@link ComplexPlaceReaderType }
     * 
     */
    public ComplexPlaceReaderType createComplexPlaceReaderType() {
        return new ComplexPlaceReaderType();
    }

    /**
     * Create an instance of {@link SimplePlaceReaderType }
     * 
     */
    public SimplePlaceReaderType createSimplePlaceReaderType() {
        return new SimplePlaceReaderType();
    }

    /**
     * Create an instance of {@link ParkingAreaReaderType }
     * 
     */
    public ParkingAreaReaderType createParkingAreaReaderType() {
        return new ParkingAreaReaderType();
    }

    /**
     * Create an instance of {@link GateReaderType }
     * 
     */
    public GateReaderType createGateReaderType() {
        return new GateReaderType();
    }

    /**
     * Create an instance of {@link RoadReaderType }
     * 
     */
    public RoadReaderType createRoadReaderType() {
        return new RoadReaderType();
    }

    /**
     * Create an instance of {@link RoadSegmentReaderType }
     * 
     */
    public RoadSegmentReaderType createRoadSegmentReaderType() {
        return new RoadSegmentReaderType();
    }

    /**
     * Create an instance of {@link VehicleReaderType }
     * 
     */
    public VehicleReaderType createVehicleReaderType() {
        return new VehicleReaderType();
    }

    /**
     * Create an instance of {@link DangerousMaterialType }
     * 
     */
    public DangerousMaterialType createDangerousMaterialType() {
        return new DangerousMaterialType();
    }

    /**
     * Create an instance of {@link Gates }
     * 
     */
    public Gates createGates() {
        return new Gates();
    }

    /**
     * Create an instance of {@link Vehicles }
     * 
     */
    public Vehicles createVehicles() {
        return new Vehicles();
    }

    /**
     * Create an instance of {@link ParkingAreas }
     * 
     */
    public ParkingAreas createParkingAreas() {
        return new ParkingAreas();
    }

    /**
     * Create an instance of {@link Roads }
     * 
     */
    public Roads createRoads() {
        return new Roads();
    }

    /**
     * Create an instance of {@link RoadSegments }
     * 
     */
    public RoadSegments createRoadSegments() {
        return new RoadSegments();
    }

    /**
     * Create an instance of {@link DangerousMaterials }
     * 
     */
    public DangerousMaterials createDangerousMaterials() {
        return new DangerousMaterials();
    }

    /**
     * Create an instance of {@link IdentifiedEntityReaderType }
     * 
     */
    public IdentifiedEntityReaderType createIdentifiedEntityReaderType() {
        return new IdentifiedEntityReaderType();
    }

    /**
     * Create an instance of {@link ServiceType }
     * 
     */
    public ServiceType createServiceType() {
        return new ServiceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RnsReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RnsReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "rns")
    public JAXBElement<RnsReaderType> createRns(RnsReaderType value) {
        return new JAXBElement<RnsReaderType>(_Rns_QNAME, RnsReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConnectionReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConnectionReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "connection")
    public JAXBElement<ConnectionReaderType> createConnection(ConnectionReaderType value) {
        return new JAXBElement<ConnectionReaderType>(_Connection_QNAME, ConnectionReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComplexPlaceReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ComplexPlaceReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "complexPlace")
    public JAXBElement<ComplexPlaceReaderType> createComplexPlace(ComplexPlaceReaderType value) {
        return new JAXBElement<ComplexPlaceReaderType>(_ComplexPlace_QNAME, ComplexPlaceReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimplePlaceReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SimplePlaceReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "place")
    public JAXBElement<SimplePlaceReaderType> createPlace(SimplePlaceReaderType value) {
        return new JAXBElement<SimplePlaceReaderType>(_Place_QNAME, SimplePlaceReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParkingAreaReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ParkingAreaReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "parkingArea")
    public JAXBElement<ParkingAreaReaderType> createParkingArea(ParkingAreaReaderType value) {
        return new JAXBElement<ParkingAreaReaderType>(_ParkingArea_QNAME, ParkingAreaReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GateReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GateReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "gate")
    public JAXBElement<GateReaderType> createGate(GateReaderType value) {
        return new JAXBElement<GateReaderType>(_Gate_QNAME, GateReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoadReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RoadReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "road")
    public JAXBElement<RoadReaderType> createRoad(RoadReaderType value) {
        return new JAXBElement<RoadReaderType>(_Road_QNAME, RoadReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoadSegmentReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RoadSegmentReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "roadSegment")
    public JAXBElement<RoadSegmentReaderType> createRoadSegment(RoadSegmentReaderType value) {
        return new JAXBElement<RoadSegmentReaderType>(_RoadSegment_QNAME, RoadSegmentReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VehicleReaderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link VehicleReaderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "vehicle")
    public JAXBElement<VehicleReaderType> createVehicle(VehicleReaderType value) {
        return new JAXBElement<VehicleReaderType>(_Vehicle_QNAME, VehicleReaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DangerousMaterialType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DangerousMaterialType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/RnsInfo", name = "dangerousMaterial")
    public JAXBElement<DangerousMaterialType> createDangerousMaterial(DangerousMaterialType value) {
        return new JAXBElement<DangerousMaterialType>(_DangerousMaterial_QNAME, DangerousMaterialType.class, null, value);
    }

}
