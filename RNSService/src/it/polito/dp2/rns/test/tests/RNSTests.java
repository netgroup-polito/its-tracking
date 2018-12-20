package it.polito.dp2.rns.test.tests;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import it.polito.dp2.rest.rns.jaxb.ComplexPlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rns.test.client.RNSPlaceClient;

public class RNSTests {
	
	@Test
	public void createComplexPlace() {
		RNSPlaceClient client = new RNSPlaceClient();
		ObjectFactory factory = new ObjectFactory();
		ComplexPlaceReaderType place1 = factory.createComplexPlaceReaderType();
		
		place1.setComplexPlaceName("Complex Place 0");
		place1.setId("x1");
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
		place1.setId("x1");
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
}
