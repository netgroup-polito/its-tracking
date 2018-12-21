package it.polito.dp2.rns.test.tests;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import it.polito.dp2.rest.rns.jaxb.ComplexPlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleStateType;
import it.polito.dp2.rest.rns.jaxb.VehicleTypeType;
import it.polito.dp2.rns.test.client.RNSPlaceClient;
import it.polito.dp2.rns.test.client.RNSVehicleClient;

public class RNSTests {
	
	@Test
	public void createComplexPlace() {
		RNSPlaceClient client = new RNSPlaceClient();
		ObjectFactory factory = new ObjectFactory();
		ComplexPlaceReaderType place1 = factory.createComplexPlaceReaderType();
		
		place1.setComplexPlaceName("Complex Place 0");
		place1.setTotalCapacity(new BigInteger("100"));
		place1.getSimplePlaceId().add(new BigInteger("100"));
		
		String place1Id = client.createComplexPlace(place1);
		
		place1.setId(place1Id);
		
		ComplexPlaceReaderType place1bis = client.getComplexPlace(place1Id);
		
		assertNotNull("Place with id " + place1Id + " can't be null", place1bis);
		
		System.out.println("*********************************");
		System.out.println("Id -> Before: " + place1.getId() + " --- After: " + place1bis.getId());
		System.out.println("Complex Places -> Before: " + place1.getComplexPlaceId().size() + " --- After: " + place1bis.getComplexPlaceId().size());
		System.out.println("Tot Capacity -> Before: " + place1.getTotalCapacity() + " --- After: " + place1bis.getTotalCapacity());
		System.out.println("Simple Places -> Before: " + place1.getSimplePlaceId().size() + " --- After: " + place1bis.getSimplePlaceId().size());
		System.out.println("*********************************");
		
		assertEquals("Create complex place failed!", place1.getId(), place1bis.getId());
		assertEquals("Create complex place failed!", place1.getComplexPlaceId().size(), place1bis.getComplexPlaceId().size());
		assertEquals("Create complex place failed!", place1.getTotalCapacity(), place1bis.getTotalCapacity());
		assertEquals("Create complex place failed!", place1.getSimplePlaceId().size(), place1bis.getSimplePlaceId().size());
	}
	
	@Test
	public void createSimplePlace() {
		RNSPlaceClient client = new RNSPlaceClient();
		ObjectFactory factory = new ObjectFactory();
		SimplePlaceReaderType place1 = factory.createSimplePlaceReaderType();
		
		place1.setSimplePlaceName("Simple Place 0");
		place1.setCapacity(new BigInteger("10"));
		
		String place1Id = client.createSimplePlace(place1);
		
		place1.setId(place1Id);
		
		SimplePlaceReaderType place1bis = client.getSimplePlace(place1Id);
		
		assertNotNull("Place with id " + place1Id + " can't be null", place1bis);
		
		System.out.println("*********************************");
		System.out.println("Id -> Before: " + place1.getId() + " --- After: " + place1bis.getId());
		System.out.println("Capacity -> Before: " + place1.getCapacity() + " --- After: " + place1bis.getCapacity());
		System.out.println("*********************************");
		
		assertEquals("Create complex place failed!", place1.getId(), place1bis.getId());
		assertEquals("Create complex place failed!", place1.getCapacity(), place1bis.getCapacity());
	}
	
	@Test
	public void createVehicle() throws DatatypeConfigurationException {
		RNSVehicleClient client = new RNSVehicleClient();
		ObjectFactory factory = new ObjectFactory();
		
		VehicleReaderType vehicle = factory.createVehicleReaderType();
		SimplePlaceReaderType origin = new SimplePlaceReaderType();
		SimplePlaceReaderType destination = new SimplePlaceReaderType();
		SimplePlaceReaderType position = new SimplePlaceReaderType();
		VehicleStateType state = VehicleStateType.IN_TRANSIT;
		VehicleTypeType type = VehicleTypeType.CAR;
		
		origin.setId("o1");		
		origin.setSimplePlaceName("Simple Place 0");
		origin.setCapacity(new BigInteger("10"));
		destination.setId("d1");
		destination.setSimplePlaceName("Simple Place 1");
		destination.setCapacity(new BigInteger("6"));
		position.setId("p1");
		position.setSimplePlaceName("Simple Place 2");
		position.setCapacity(new BigInteger("8"));
		
		GregorianCalendar calendarActual = new GregorianCalendar(2018, 6, 28);
	    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
	    XMLGregorianCalendar XMLGregorianCalendar = datatypeFactory
	      .newXMLGregorianCalendar(calendarActual);
	    
	    vehicle.setVehicleName("car1");
		vehicle.setDestination(destination);
		vehicle.setOrigin(origin);
		vehicle.setPosition(position);
		vehicle.setEntryTime(XMLGregorianCalendar);
		vehicle.setState(state);
		vehicle.setType(type);
		
		String vehicleId = client.createVehicle(vehicle);
		
		vehicle.setId(vehicleId);
		
		VehicleReaderType vehicleCopy = client.getVehicle(vehicleId);
		
		assertNotNull("Vehicle with id " + vehicleId + " can't be null", vehicleCopy);
		
		System.out.println("*********************************");
		System.out.println("Id -> Before: " + vehicle.getId() + " --- After: " + vehicleCopy.getId());
		System.out.println("Name -> Before: " + vehicle.getVehicleName() + " --- After: " + vehicleCopy.getVehicleName());
		System.out.println("Dest -> Before: " + vehicle.getDestination().getId() + " --- After: " + vehicleCopy.getDestination().getId());
		System.out.println("Origin -> Before: " + vehicle.getOrigin().getId() + " --- After: " + vehicleCopy.getOrigin().getId());
		System.out.println("Position -> Before: " + vehicle.getPosition().getId() + " --- After: " + vehicleCopy.getPosition().getId());
		System.out.println("Entry time -> Before: " + vehicle.getEntryTime() + " --- After: " + vehicleCopy.getEntryTime());
		System.out.println("State -> Before: " + vehicle.getState() + " --- After: " + vehicleCopy.getState());
		System.out.println("Type -> Before: " + vehicle.getType() + " --- After: " + vehicleCopy.getType());
		System.out.println("*********************************");
		
		assertEquals("Create vehicle failed!", vehicle.getId(), vehicleCopy.getId());
		assertEquals("Create vehicle failed!", vehicle.getVehicleName(), vehicleCopy.getVehicleName());
		assertEquals("Create vehicle failed!", vehicle.getDestination().getId(), vehicleCopy.getDestination().getId());
		assertEquals("Create vehicle failed!", vehicle.getOrigin().getId(), vehicleCopy.getOrigin().getId());
		assertEquals("Create vehicle failed!", vehicle.getPosition().getId(), vehicleCopy.getPosition().getId());
		assertEquals("Create vehicle failed!", vehicle.getEntryTime(), vehicleCopy.getEntryTime());
		assertEquals("Create vehicle failed!", vehicle.getState(), vehicleCopy.getState());
		assertEquals("Create vehicle failed!", vehicle.getType(), vehicleCopy.getType());
	}
}
