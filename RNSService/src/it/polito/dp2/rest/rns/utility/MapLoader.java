package it.polito.dp2.rest.rns.utility;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.GateType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.jaxb.ServiceType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.math.BigInteger;

public class MapLoader {
	private static final Neo4jInteractions neo4j = Neo4jInteractions.getInstance();
	public static void loadMap() {
		try {

			File fXmlFile = new File(Constants.MapXMLPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
					
			NodeList nList = doc.getElementsByTagName("gate");
			loadGates(nList);
			
			nList = doc.getElementsByTagName("road");
			loadRoads(nList);
			
			nList = doc.getElementsByTagName("roadSegment");
			loadRoadSegments(nList);
			
			nList = doc.getElementsByTagName("parkingArea");
			loadParkingAreas(nList);
			
		} catch (Exception e) {
			e.printStackTrace();
	    }
	}
	
	private static void loadParkingAreas(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				ParkingAreaReaderType park = (new ObjectFactory()).createParkingAreaReaderType();
				NodeList list = nNode.getChildNodes();
				Element eElement = (Element) nNode;
				
				park.setId(eElement.getAttribute("id"));
				
				for(int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					
					if(node.getNodeName().equals("name")) park.setSimplePlaceName(node.getTextContent());
					if(node.getNodeName().equals("capacity")) park.setCapacity(new BigInteger(node.getTextContent()));
					if(node.getNodeName().equals("connectedPlace")) park.getConnectedPlaceId().add(node.getTextContent());
					if(node.getNodeName().equals("service")) { 
						ServiceType service = (new ObjectFactory()).createServiceType();
						service.setName(node.getTextContent());
						park.getService().add(service);
					}
				}
				
				neo4j.createNode(park);
			}
		}
	}

	private static void loadRoadSegments(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				RoadSegmentReaderType roadSegment = (new ObjectFactory()).createRoadSegmentReaderType();
				NodeList list = nNode.getChildNodes();
				Element eElement = (Element) nNode;
				
				roadSegment.setId(eElement.getAttribute("id"));
				
				for(int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					
					if(node.getNodeName().equals("name")) roadSegment.setName((node.getTextContent()));
					if(node.getNodeName().equals("capacity")) roadSegment.setCapacity(new BigInteger(node.getTextContent()));
					if(node.getNodeName().equals("connectedPlace")) roadSegment.getConnectedPlaceId().add(node.getTextContent());
				}
				
				neo4j.createNode(roadSegment);
			}
		}
		
	}

	private static void loadRoads(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList list = nNode.getChildNodes();
				loadRoadSegments(list);
			}
		}
	}

	private static void loadGates(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			GateReaderType gate = (new ObjectFactory()).createGateReaderType();
			Node nNode = nList.item(temp);
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList list = nNode.getChildNodes();
				Element eElement = (Element) nNode;
				
				gate.setId(eElement.getAttribute("id"));
				
				for(int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					
					if(node.getNodeName().equals("simplePlaceName")) gate.setSimplePlaceName(node.getTextContent());
					if(node.getNodeName().equals("capacity")) gate.setCapacity(new BigInteger(node.getTextContent()));
					if(node.getNodeName().equals("connectedPlace")) gate.getConnectedPlaceId().add(node.getTextContent());
					if(node.getNodeName().equals("type")) gate.setType(GateType.fromValue(node.getTextContent()));
				}
				
				neo4j.createNode(gate);
			}
		}
	}
}
