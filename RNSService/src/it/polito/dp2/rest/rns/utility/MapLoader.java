package it.polito.dp2.rest.rns.utility;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.GateType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.jaxb.ServiceType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {
	private static final Neo4jInteractions neo4j = Neo4jInteractions.getInstance();
	private static final IdTranslator id2neo4j = IdTranslator.getInstance();
	private static List<GateReaderType> gates = new ArrayList<>();
	private static List<RoadReaderType> roads = new ArrayList<>();
	private static List<RoadSegmentReaderType> roadSegments = new ArrayList<>();
	private static List<ParkingAreaReaderType> parkings = new ArrayList<>();
	
	public static void loadMap() {
		try {

			File fXmlFile = new File(Constants.MapXMLPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
					
			// Load all the nodes
			NodeList nListGates = doc.getElementsByTagName("gate");
			loadGates(nListGates);
			
			NodeList nListRoads = doc.getElementsByTagName("road");
			loadRoads(nListRoads);
			
			NodeList nListRoadSegments = doc.getElementsByTagName("roadSegment");
			loadRoadSegments(nListRoadSegments);
			
			NodeList nListParkingAreas = doc.getElementsByTagName("parkingArea");
			loadParkingAreas(nListParkingAreas);
			
			// Establish links
			connectGates(gates);
			connectRoadSegments(roadSegments);
			connectParkingAreas(parkings);
			
		} catch (Exception e) {
			e.printStackTrace();
	    }
	}
	
	/**
	 * Function to connect parking areas to whatever they're connected to
	 * @param parkingList = list of parking areas that have to be connected
	 */
	private static void connectParkingAreas(List<ParkingAreaReaderType> parkingList) {
		for(ParkingAreaReaderType park : parkingList) {
			for(String id : park.getConnectedPlaceId())
				if(id != null)
					neo4j.connectNodes(park.getId(), id, "isConnectedTo");
			
			String containerId = park.getContainerPlaceId();
			if(containerId != null)
				neo4j.connectNodes(park.getId(), containerId, "isContainedInto");
		}
		
	}

	/**
	 * Function to connect road segment to whatever they're connected to
	 * @param roadSegmentList = list of road segments that have to be connected
	 */
	private static void connectRoadSegments(List<RoadSegmentReaderType> roadSegmentList) {
		for(RoadSegmentReaderType roadSegment : roadSegmentList) {
			for(String id : roadSegment.getConnectedPlaceId()) {
				if(id != null)
					neo4j.connectNodes(roadSegment.getId(), id, "isConnectedTo");
			}
			
			String containerId = roadSegment.getContainerPlaceId();
			if(containerId != null)
				neo4j.connectNodes(roadSegment.getId(), containerId, "isContainedInto");
		}
		
	}

	/**
	 * Function to connect in neo4j all the gates previously
	 * loaded into the db
	 * @param gateList = list of gates that has to be connected
	 */
	private static void connectGates(List<GateReaderType> gateList) {
		for(GateReaderType gate : gateList) {
			for(String id : gate.getConnectedPlaceId()) {
				if(id != null)
					neo4j.connectNodes(gate.getId(), id, "isConnectedTo");
			}
			
			String containerId = gate.getContainerPlaceId();
			if(containerId != null)
				neo4j.connectNodes(gate.getId(), containerId, "isContainedInto");
		}
	}

	/**
	 * Function to load parking areas into neo4j db
	 * @param nList = list of xml read nodes known to be parking
	 * areas
	 */
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
				
				String parkId = neo4j.createNode(park);
				id2neo4j.addIdTranslation(park.getId(), parkId);
				parkings.add(park);
			}
		}
	}

	/**
	 * Function to load road segments into the neo4j database
	 * @param nList = list of xml read nodes known to be road segments
	 */
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
					if(node.getNodeName().equals("containerPlaceId")) roadSegment.setContainerPlaceId(node.getTextContent());
				}
				
				String roadSegmentId = neo4j.createNode(roadSegment);
				id2neo4j.addIdTranslation(roadSegment.getId(), roadSegmentId);
				roadSegments.add(roadSegment);
			}
		}
		
	}

	/**
	 * Function to load roads into the database
	 * @param nList = list of nodes know to be roads
	 */
	private static void loadRoads(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				RoadReaderType road = (new ObjectFactory()).createRoadReaderType();
				road.setId(((Element)nNode).getAttribute("id"));
				road.setName(((Element)nNode).getAttribute("name"));
				String roadId = neo4j.createNode(road);
				id2neo4j.addIdTranslation(road.getId(), roadId);
				roads.add(road);
				
				NodeList list = nNode.getChildNodes();
				loadRoadSegments(list);
			}
		}
	}

	/**
	 * Function to load a list of nodes, know to be gates, into
	 * neo4j database
	 * @param nList = the list of gates extracted from xml
	 */
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
				
				String gateId = neo4j.createNode(gate);
				id2neo4j.addIdTranslation(gate.getId(), gateId);
				gates.add(gate);
			}
		}
	}
}
