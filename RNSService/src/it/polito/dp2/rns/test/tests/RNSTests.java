package it.polito.dp2.rns.test.tests;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import it.polito.dp2.rest.rns.jaxb.ComplexPlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rns.test.client.RNSPlaceClient;

public class RNSTests {
	
	@Test
	public void createComplexPlace() {
		RNSPlaceClient client = new RNSPlaceClient();
		ObjectFactory factory = new ObjectFactory();
		ComplexPlaceReaderType place1 = factory.createComplexPlaceReaderType();
		
		place1.setComplexPlaceName("Complex Place 0");
		place1.setId("0");
		place1.setTotalCapacity(new BigInteger("100"));
		
		String place1Id = client.createComplexPlace(place1);
		
		place1.setId(place1Id);
		
		ComplexPlaceReaderType place1bis = client.getComplexPlace(place1Id);
		
		assertNotNull("Place with id: " + place1Id + " can't be null", place1bis);
		assertEquals("Create complex place failed!", place1, place1bis);
	}
}
