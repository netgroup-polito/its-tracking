package it.polito.dp2.rest.rns.utility;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import it.polito.dp2.rest.rns.jaxb.DangerousMaterialType;
import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.GateType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that offers methods to load a map from an xml file.
 * @author eugeniogallea
 */
public class MapLoader {
	private static final Neo4jInteractions neo4j = Neo4jInteractions.getInstance();
	private static final IdTranslator id2neo4j = IdTranslator.getInstance();
	private static List<GateReaderType> gates = new ArrayList<>();
	private static List<RoadReaderType> roads = new ArrayList<>();
	private static List<RoadSegmentReaderType> roadSegments = new ArrayList<>();
	private static List<ParkingAreaReaderType> parkings = new ArrayList<>();
	private static List<DangerousMaterialType> dangerousMaterials = new ArrayList<>();
	
	public static void loadMap() {
		try {

			File fXmlFile = new File(Constants.MapXMLPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
					
			// Load all the nodes
			//System.out.println("###### GATES ######");
			NodeList nListGates = doc.getElementsByTagName("gate");
			loadGates(nListGates);
			
			//System.out.println("###### ROADS ######");
			NodeList nListRoads = doc.getElementsByTagName("road");
			loadRoads(nListRoads);
			
			//System.out.println("###### ROAD SEGMENTS ######");
			NodeList nListRoadSegments = doc.getElementsByTagName("roadSegment");
			loadRoadSegments(nListRoadSegments);
			
			//System.out.println("###### PARKING AREAS ######");
			NodeList nListParkingAreas = doc.getElementsByTagName("parkingArea");
			loadParkingAreas(nListParkingAreas);
			
			//System.out.println("###### DANGEROUS MATERIAL ######");
			NodeList nListDangerousMaterials = doc.getElementsByTagName("dangerousMaterial");
			loadDangerousMaterials(nListDangerousMaterials);
			
			// Establish links
			connectGates(gates);
			connectRoadSegments(roadSegments);
			connectParkingAreas(parkings);
			connectDangerousMaterials(dangerousMaterials);
		} catch (Exception e) {
			e.printStackTrace();
	    }
	}

	private static void connectDangerousMaterials(List<DangerousMaterialType> materialList) {
		materialList.stream().forEach((material) -> {
			String materialId = material.getId();
			
			material.getIncompatibleMaterial().stream().forEach((id) -> {
				Neo4jInteractions.getInstance()
						.connectNodes(
								materialId, 
								id, 
								"isIncompatibleWith"
						);
			});
		});
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
					
					if(node.getNodeName().equals("name")) park.setName(node.getTextContent());
					if(node.getNodeName().equals("capacity")) park.setCapacity(new BigInteger(node.getTextContent()));
					if(node.getNodeName().equals("connectedPlace")) park.getConnectedPlaceId().add(node.getTextContent());
					if(node.getNodeName().equals("service")) {
						park.getService().add(node.getTextContent());
					}
					if(node.getNodeName().equals("avgTimeSpent")) { 
						//System.err.println("Node: " + eElement.getAttribute("id") + " avgTimeSpent: " + node.getTextContent());
						park.setAvgTimeSpent(new BigInteger(node.getTextContent()));
					}
				}
				/*System.out.println("*************************************");
				System.out.println("Loading parking area: " + park.getId());*/
				String parkId = neo4j.createNode(park);
				id2neo4j.addIdTranslation(park.getId(), parkId);
				parkings.add(park);
				Constants.countVehiclePlace.put(park.getId(), 0);
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
				
				//System.out.println("List of children: " + list.getLength());
				
				for(int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					
					if(node.getNodeName().equals("name")) roadSegment.setName((node.getTextContent()));
					if(node.getNodeName().equals("capacity")) roadSegment.setCapacity(new BigInteger(node.getTextContent()));
					if(node.getNodeName().equals("connectedPlace")) roadSegment.getConnectedPlaceId().add(node.getTextContent());
					if(node.getNodeName().equals("containerPlaceId")) roadSegment.setContainerPlaceId(node.getTextContent());
					if(node.getNodeName().equals("avgTimeSpent")) roadSegment.setAvgTimeSpent(new BigInteger(node.getTextContent()));
				}
				/*System.out.println("*************************************");
				System.out.println("Loading road segment: " + roadSegment.getId());*/
				String roadSegmentId = neo4j.createNode(roadSegment);
				id2neo4j.addIdTranslation(roadSegment.getId(), roadSegmentId);
				roadSegments.add(roadSegment);
				Constants.countVehiclePlace.put(roadSegment.getId(), 0);
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
				/*System.out.println("*************************************");
				System.out.println("Loading road: " + road.getId());*/
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
					
					if(node.getNodeName().equals("name")) gate.setName(node.getTextContent());
					if(node.getNodeName().equals("capacity")) gate.setCapacity(new BigInteger(node.getTextContent()));
					if(node.getNodeName().equals("connectedPlace")) gate.getConnectedPlaceId().add(node.getTextContent());
					if(node.getNodeName().equals("type")) gate.setType(GateType.fromValue(node.getTextContent()));
					if(node.getNodeName().equals("avgTimeSpent")) gate.setAvgTimeSpent(new BigInteger(node.getTextContent()));
				}
				/*System.out.println("*************************************");
				System.out.println("Loading gate: " + gate.getId());*/
				String gateId = neo4j.createNode(gate);
				id2neo4j.addIdTranslation(gate.getId(), gateId);
				gates.add(gate);
				Constants.countVehiclePlace.put(gate.getId(), 0);
			}
		}
	}
	
	private static void loadDangerousMaterials(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			DangerousMaterialType material = (new ObjectFactory()).createDangerousMaterialType();
			Node nNode = nList.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList list = nNode.getChildNodes();
				Element eElement = (Element) nNode;
				
				material.setId(eElement.getAttribute("id"));
				/*System.out.println("++++++++++++++++++++++++++++++++");
				System.out.println("MATERIAL: " + material.getId());*/
				for(int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					
					if(!isStringNullOrWhiteSpace(node.getTextContent())) {
						//System.out.println("Incompatible Material: " + node.getTextContent());
						material.getIncompatibleMaterial().add(node.getTextContent());
					}
				}
				
				String dangerousMaterialId = neo4j.createNode(material);
				id2neo4j.addIdTranslation(material.getId(), dangerousMaterialId);
				dangerousMaterials.add(material);
			}
		}
		
	}
	
	public static boolean isStringNullOrWhiteSpace(String value) {
	    if (value == null) {
	        return true;
	    }

	    for (int i = 0; i < value.length(); i++) {
	        if (!Character.isWhitespace(value.charAt(i))) {
	            return false;
	        }
	    }

	    return true;
	}
	
	public static boolean areCompatible(String material1, String material2) {
		int index = -1;
		for(DangerousMaterialType material : dangerousMaterials) {
			if(!material.getId().equals(material1)) {
				index++;
			}
		}
		
		return dangerousMaterials.get(index).getIncompatibleMaterial().contains(material2);
	}
}
