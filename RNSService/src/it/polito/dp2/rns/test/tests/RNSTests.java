package it.polito.dp2.rns.test.tests;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.GateType;
import it.polito.dp2.rest.rns.jaxb.Gates;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.Places;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleStateType;
import it.polito.dp2.rest.rns.jaxb.VehicleTypeType;
import it.polito.dp2.rns.test.client.RNSVehicleClient;

public class RNSTests {
	
	@Test
	public void createVehicle() throws DatatypeConfigurationException {
		RNSVehicleClient client = new RNSVehicleClient();
		ObjectFactory factory = new ObjectFactory();
		
		// Setting vehicle properties
		VehicleReaderType vehicle = factory.createVehicleReaderType();
		VehicleStateType state = VehicleStateType.IN_TRANSIT;
		VehicleTypeType type = VehicleTypeType.CAR;
		
		GregorianCalendar calendarActual = new GregorianCalendar(2018, 6, 28);
	    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
	    XMLGregorianCalendar XMLGregorianCalendar = datatypeFactory
	      .newXMLGregorianCalendar(calendarActual);
	    
	    // Getting two gates
	    Gates gates = client.getGates();
	    String origin = "";
	    String destination = "";
	    
	    // Select two gates with capacity >= 1 as origin and destination
	    for(GateReaderType gate : gates.getGate()) {
	    	System.out.println("Gate " + gate.getId() + " --- Capacity = " + gate.getCapacity().intValue());
	    	if(origin.equals("")) {
	    		if(
    				gate.getCapacity().intValue() >= 1 && 
    				(gate.getType().equals(GateType.IN) || gate.getType().equals(GateType.INOUT))
				) {
	    			origin = gate.getId();
	    		}
	    	} else {
				if(gate.getId() != origin && destination.equals("")) {
					if(
						gate.getCapacity().intValue() >= 1 && 
						(gate.getType().equals(GateType.OUT) || gate.getType().equals(GateType.INOUT)) &&
						!origin.equals(gate.getId())
					) {
						destination = gate.getId();
		    		}
				}
	    	}
	    }
	    
	    // Set up everything in the vehicle
	    vehicle.setId("car01");
	    vehicle.setName("car01");
		vehicle.setDestination(destination);
		vehicle.setOrigin(origin);
		vehicle.setPosition(origin);
		vehicle.setEntryTime(XMLGregorianCalendar);
		vehicle.setState(state);
		vehicle.setType(type);
		
		// POST the vehicle
		Places places = client.createVehicle(vehicle);
		
		// Retrieve the same vehicle
		VehicleReaderType vehicleCopy = client.getVehicle(vehicle.getId());
		
		// Checks
		
		if(!origin.equals("") && !destination.equals("")) { // Vehicle was created
			assertNotNull("Vehicle with id " + vehicle.getId() + " can't be null", vehicleCopy);
			
			System.out.println("*********************************");
			System.out.println("Id -> Before: " + vehicle.getId() + " --- After: " + vehicleCopy.getId());
			System.out.println("Name -> Before: " + vehicle.getName() + " --- After: " + vehicleCopy.getName());
			System.out.println("Dest -> Before: " + vehicle.getDestination() + " --- After: " + vehicleCopy.getDestination());
			System.out.println("Origin -> Before: " + vehicle.getOrigin() + " --- After: " + vehicleCopy.getOrigin());
			System.out.println("Position -> Before: " + vehicle.getPosition() + " --- After: " + vehicleCopy.getPosition());
			System.out.println("Entry time -> Before: " + vehicle.getEntryTime() + " --- After: " + vehicleCopy.getEntryTime());
			System.out.println("State -> Before: " + vehicle.getState() + " --- After: " + vehicleCopy.getState());
			System.out.println("Type -> Before: " + vehicle.getType() + " --- After: " + vehicleCopy.getType());
			System.out.println("*********************************");
			
			assertEquals("Create vehicle failed!", vehicle.getId(), vehicleCopy.getId());
			assertEquals("Create vehicle failed!", vehicle.getName(), vehicleCopy.getName());
			assertEquals("Create vehicle failed!", vehicle.getDestination(), vehicleCopy.getDestination());
			assertEquals("Create vehicle failed!", vehicle.getOrigin(), vehicleCopy.getOrigin());
			assertEquals("Create vehicle failed!", vehicle.getPosition(), vehicleCopy.getPosition());
			assertEquals("Create vehicle failed!", vehicle.getEntryTime(), vehicleCopy.getEntryTime());
			assertEquals("Create vehicle failed!", vehicle.getState(), vehicleCopy.getState());
			assertEquals("Create vehicle failed!", vehicle.getType(), vehicleCopy.getType());
			
		} else { // vehicle wasn't created
			
			assertEquals("Vehicle created even if it was not possible.", places, null);
			System.out.println("Vehicle hasn't been created, because there was no space in origin or destination.");
			
		}
		
		System.out.println("******************************************************************");
		System.out.println("CREATE VEHICLE tests PASSED!");
		System.out.println("******************************************************************");
	}
	
	@Test
	public void createVehicleWrongCapacityGate() throws DatatypeConfigurationException {
		RNSVehicleClient client = new RNSVehicleClient();
		ObjectFactory factory = new ObjectFactory();
		
		// Setting vehicle properties
		VehicleReaderType vehicle = factory.createVehicleReaderType();
		VehicleStateType state = VehicleStateType.IN_TRANSIT;
		VehicleTypeType type = VehicleTypeType.CAR;
		
		GregorianCalendar calendarActual = new GregorianCalendar(2018, 6, 28);
	    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
	    XMLGregorianCalendar XMLGregorianCalendar = datatypeFactory
	      .newXMLGregorianCalendar(calendarActual);
	    
	    // Getting two gates
	    Gates gates = client.getGates();
	    String origin = "";
	    String destination = "";
	    
	    // Select two gates with capacity >= 1 as origin and destination
	    for(GateReaderType gate : gates.getGate()) {
	    	System.out.println("Gate " + gate.getId() + " --- Capacity = " + gate.getCapacity().intValue());
	    	if(origin.equals("")) {
	    		if(gate.getCapacity().intValue() == 0 && (gate.getType().equals(GateType.IN) || gate.getType().equals(GateType.INOUT))) {
	    			origin = gate.getId();
	    		}
	    	} else {
				if(gate.getId() != origin && destination.equals("")) {
					if(gate.getCapacity().intValue() >= 1 && (gate.getType().equals(GateType.OUT) || gate.getType().equals(GateType.INOUT))) {
		    			destination = gate.getId();
		    		}
				}
	    	}
	    }
	    
	    if(!origin.equals("")) {
		    // Set up everything in the vehicle
		    vehicle.setId("car02");
		    vehicle.setName("car02");
			vehicle.setDestination(destination);
			vehicle.setOrigin(origin);
			vehicle.setPosition(origin);
			vehicle.setEntryTime(XMLGregorianCalendar);
			vehicle.setState(state);
			vehicle.setType(type);
			
			// POST the vehicle
			Places places = client.createVehicle(vehicle);
			
			assertNull("We should not have retrieved a path because origin has capacity 0.", places);
	    } else {
	    	System.out.println("******************************************************************");
			System.out.println("COULD NOT TEST, BECAUSE IT WASN'T POSSIBLE TO GET ORIGIN WITH CAPACITY 0");
			System.out.println("******************************************************************");
	    }
		
		System.out.println("******************************************************************");
		System.out.println("CREATE WRONG VEHICLE ORIGIN tests PASSED!");
		System.out.println("******************************************************************");
	}
	
	@Test
	public void createVehicleWrongVehicleId() throws DatatypeConfigurationException {
		RNSVehicleClient client = new RNSVehicleClient();
		ObjectFactory factory = new ObjectFactory();
		
		// Setting vehicle properties
		VehicleReaderType vehicle = factory.createVehicleReaderType();
		VehicleStateType state = VehicleStateType.IN_TRANSIT;
		VehicleTypeType type = VehicleTypeType.CAR;
		
		GregorianCalendar calendarActual = new GregorianCalendar(2018, 6, 28);
	    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
	    XMLGregorianCalendar XMLGregorianCalendar = datatypeFactory
	      .newXMLGregorianCalendar(calendarActual);
	    
	    // Getting two gates
	    Gates gates = client.getGates();
	    String origin = "";
	    String destination = "";
	    
	    // Select two gates with capacity >= 1 as origin and destination
	    for(GateReaderType gate : gates.getGate()) {
	    	System.out.println("Gate " + gate.getId() + " --- Capacity = " + gate.getCapacity().intValue());
	    	if(origin.equals("")) {
	    		if(gate.getCapacity().intValue() >= 0 && (gate.getType().equals(GateType.IN) || gate.getType().equals(GateType.INOUT))) {
	    			origin = gate.getId();
	    		}
	    	} else {
				if(gate.getId() != origin && destination.equals("")) {
					if(gate.getCapacity().intValue() >= 1 && (gate.getType().equals(GateType.OUT) || gate.getType().equals(GateType.INOUT))) {
		    			destination = gate.getId();
		    		}
				}
	    	}
	    }
	    
	    // Set up everything in the vehicle
	    vehicle.setId("car01");
	    vehicle.setName("car01");
		vehicle.setDestination(destination);
		vehicle.setOrigin(origin);
		vehicle.setPosition(origin);
		vehicle.setEntryTime(XMLGregorianCalendar);
		vehicle.setState(state);
		vehicle.setType(type);
		
		// POST the vehicle
		Places places = client.createVehicle(vehicle);
		
		assertNull("We should not have retrieved a path because we already loaded a vehicle with id " + vehicle.getId(), places);
		
		System.out.println("******************************************************************");
		System.out.println("CREATE WRONG VEHICLE ID tests PASSED!");
		System.out.println("******************************************************************");
	}
}
