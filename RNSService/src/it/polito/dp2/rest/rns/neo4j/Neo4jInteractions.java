package it.polito.dp2.rest.rns.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.Value;

import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.GateType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleStateType;
import it.polito.dp2.rest.rns.jaxb.VehicleTypeType;
import it.polito.dp2.rest.rns.utility.Constants;
import it.polito.dp2.rest.rns.utility.DateConverter;
import it.polito.dp2.rest.rns.utility.IdTranslator;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * This class is responsible to give access to all method necessary to interact 
 * with Neo4j.
 * @author dp2
 *
 */
public class Neo4jInteractions implements AutoCloseable {
	private Driver driver;
	private static Neo4jInteractions instance = null;
	
	private Neo4jInteractions(String uri, String username, String password){
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
	}
	
	public static Neo4jInteractions getInstance() {
		if(instance == null) {
			instance = new Neo4jInteractions(
					Constants.Neo4jURL, 
					Constants.Neo4jUsername, 
					Constants.Neo4jPassword);
		}
		
		return instance;
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}
	
	@Override
	protected void finalize() throws Throwable {
		driver.close();
		super.finalize();
	}
	
	/**
	 * Function to create the new node into the db
	 * @param element = the new element to be loaded
	 * @return the corresponding id
	 */
	public String createNode(Object element) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().createStatement(element);
            String nodeId = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run(query);
                    
                    return String.valueOf(result.single().get( 0 ));
                }
            } );
            
            session.close();
            
            return nodeId;
        }
	}

	/**
	 * Function to connect two nodes given their ids
	 * @param node1 = identifier of source node
	 * @param node2 = identifier of destination node
	 * @param label = label of the relation
	 * @return a string representing the id of the newly created relation
	 */
	public String connectNodes(String node1, String node2, String label) {
		String query = StatementBuilder.getInstance().connectStatement(
				IdTranslator.getInstance().getIdTranslation(node1), 
				IdTranslator.getInstance().getIdTranslation(node2), 
				label);
		//System.out.println("Connect nodes: " + node1 + " --- " + node2 + " --- " + label);
		try ( Session session = driver.session() )
        {
            String relationshipId = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                	//System.err.println("Executing query: " + query);
                    tx.run(query);
                    //System.out.println("[NEO4J] " + result.single());
                    return ""; //String.valueOf(result.single().get( 0 ));
                }
            } );
            
            session.close();
            
            return relationshipId;
        }
	}
	
	/**
	 * Function to delete a specific node from the neo4j database
	 * @param nodeId = id of the node to be deleted
	 * @param type = type of the node to be deleted
	 */
	public void deleteNode(String nodeId, String type) {
		String query = StatementBuilder.getInstance().deleteByTypeAndIdStatement(
				IdTranslator.getInstance().getIdTranslation(nodeId),  
				type);

		try ( Session session = driver.session() )
        {
            session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                		tx.run(query);
                		return "";
                }
            } );
            
            session.close();
        }
	}
	
	/**
	 * Function to retrieve the assigned id of the node loaded in neo4j
	 * of a certain type
	 * @param id = id client side of the node
	 * @param type = type of the node
	 * @return the corresponding neo4j id
	 */
	public String getNeo4jNodeId(String id, String type) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getIdStatementByTypeAndId(id, type);
            String result = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
					StatementResult result = tx.run(query);
					
					return String.valueOf(result.single().get(0));
                }
            } );
            
            session.close();
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}
	
	/**
	 * Function to retrieve from Neo4j all the gates
	 * @return list of the gates currently in the db
	 */
	public synchronized List<GateReaderType> getGates() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeAndConnection("Gate");
            List<GateReaderType> result = session.writeTransaction( new TransactionWork<List<GateReaderType>>()
            {
                @Override
                public List<GateReaderType> execute( Transaction tx )
                {
                		Map<String, GateReaderType> map = new HashMap<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						GateReaderType gate = (new ObjectFactory()).createGateReaderType();
						
						if(!map.containsKey((String) r.get(0).asMap().get("id"))) {
							gate.setName((String) r.get(0).asMap().get("name"));
							gate.setId((String) r.get(0).asMap().get("id"));
							gate.setType(GateType.fromValue((String) r.get(0).asMap().get("type")));
							gate.setCapacity(new BigInteger(Long.toString((Long) r.get(0).asMap().get("capacity"))));
							gate.getConnectedPlaceId().add(IdTranslator.getInstance().fromNeo4jId(
									String.valueOf((int) r.get(1).asInt())
								));
							gate.setAvgTimeSpent((new BigInteger(Long.toString((Long) r.get(0).asMap().get("avgTimeSpent")))));
							
							map.put(
								gate.getId(),
								gate
							);
						} else {
							map.get((String) r.get(0).asMap().get("id"))
								.getConnectedPlaceId()
								.add(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(1).asInt())
									));
						}
					}
					
					return map.values().stream().collect(Collectors.toList());
                }
            } );
            
            session.close();
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve a particular vehicle from the system
	 * @param vehicleId = id of the vehicle to be retrieved
	 * @return the desired vehicle
	 */
	public synchronized VehicleReaderType getVehicle(String vehicleId) {
		List<VehicleReaderType> vehicles = this.getVehicles();
		
		for(VehicleReaderType vehicle : vehicles) {
			if(vehicle.getId().equals(vehicleId)) return vehicle;
		}
		
		return null;
	}

	/**
	 * Function to retrieve all the nodes that are of type vehicle
	 * stored in neo4j database
	 * @return the list of vehicles in the system
	 */
	public List<VehicleReaderType> getVehicles() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeNoConnection("Vehicle");
            List<VehicleReaderType> result = session.writeTransaction( new TransactionWork<List<VehicleReaderType>>()
            {
                @Override
                public List<VehicleReaderType> execute( Transaction tx )
                {
                		List<VehicleReaderType> list = new LinkedList<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						VehicleReaderType vehicle = (new ObjectFactory()).createVehicleReaderType();
						
						for( Value entry : r.values()) {
							//System.out.println("[NEO4J] " + (String) entry.asMap().get("type"));
							vehicle.setName((String) entry.asMap().get("name"));
							vehicle.setId((String) entry.asMap().get("id"));
							vehicle.setType(VehicleTypeType.fromValue((String) entry.asMap().get("type")));
							vehicle.setOrigin((String) entry.asMap().get("origin"));
							vehicle.setDestination((String) entry.asMap().get("destination"));
							vehicle.setPosition((String) entry.asMap().get("position"));
							vehicle.setState(VehicleStateType.fromValue((String) entry.asMap().get("state")));
							try {
								vehicle.setEntryTime(DateConverter.convertFromString((String) entry.asMap().get("entryTime"), "yyyy-MM-dd'T'HH:mm:ss"));
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (DatatypeConfigurationException e) {
								e.printStackTrace();
							}
						}
						
						list.add(vehicle);
					}
					return list;
                }
            } );
            
            session.close();
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve all road segments stored in the system
	 * @return the list of road segments
	 */
	public List<RoadSegmentReaderType> getRoadSegments() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeAndConnectionAndContainer("RoadSegment");
            List<RoadSegmentReaderType> result = session.writeTransaction( new TransactionWork<List<RoadSegmentReaderType>>()
            {
                @Override
                public List<RoadSegmentReaderType> execute( Transaction tx )
                {
            		Map<String, RoadSegmentReaderType> map = new HashMap<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						RoadSegmentReaderType road = (new ObjectFactory()).createRoadSegmentReaderType();
						
						if(!map.containsKey((String) r.get(0).asMap().get("id"))) {
							road.setName((String) r.get(0).asMap().get("name"));
							road.setId((String) r.get(0).asMap().get("id"));
							road.setCapacity(new BigInteger(Long.toString((Long) r.get(0).asMap().get("capacity"))));
							road.setAvgTimeSpent(new BigInteger(Long.toString((Long) r.get(0).asMap().get("avgTimeSpent"))));
							road.getConnectedPlaceId().add(IdTranslator.getInstance().fromNeo4jId(
									String.valueOf((int) r.get(1).asInt())
								));
							
							if(r.get(2) != null) // Check if it has a container
								road.setContainerPlaceId(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(2).asInt())
									));
							
							map.put(
									road.getId(),
									road
							);
						} else {
							//System.out.println("******************* " + (String) r.get(0).asMap().get("id"));
							if(!map.get(
									(String) r.get(0).asMap().get("id"))
								.getConnectedPlaceId()
								.contains(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(1).asInt())))
								&& !((String) r.get(0).asMap().get("id")).equals(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(1).asInt())))
							){
								map.get((String) r.get(0).asMap().get("id"))
									.getConnectedPlaceId()
									.add(IdTranslator.getInstance().fromNeo4jId(
											String.valueOf((int) r.get(1).asInt())
										));
							}
						}
					}
					
					return map.values().stream().collect(Collectors.toList());
                }
            } );
            
            session.close();
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve all the parking areas stored into the system
	 * @return the list of parking areas
	 */
	public List<ParkingAreaReaderType> getParkingAreas() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeAndConnection("ParkingArea");
            List<ParkingAreaReaderType> result = session.writeTransaction( new TransactionWork<List<ParkingAreaReaderType>>()
            {
                @Override
                public List<ParkingAreaReaderType> execute( Transaction tx )
                {
                		Map<String, ParkingAreaReaderType> map = new HashMap<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						ParkingAreaReaderType park = (new ObjectFactory()).createParkingAreaReaderType();
						
						if(!map.containsKey((String) r.get(0).asMap().get("id"))) {
							park.setName((String) r.get(0).asMap().get("name"));
							park.setId((String) r.get(0).asMap().get("id"));
							park.setCapacity(new BigInteger(Long.toString((Long) r.get(0).asMap().get("capacity"))));
							park.setAvgTimeSpent(new BigInteger(Long.toString((Long) r.get(0).asMap().get("avgTimeSpent"))));
							park.getConnectedPlaceId().add(
									IdTranslator.getInstance().fromNeo4jId(
											String.valueOf((int) r.get(1).asInt())
										));
							//System.out.println("Services for " + park.getId() + " " + r.get(0).asMap().get("service"));
							String[] servicesString = r.get(0).asMap().get("service")
														.toString()
														.replace("[", "")
														.replace("]", "")
														.replace(",", "")
														.split(" ");
							for(String s : servicesString) {
								if(!s.equals("")) {
									park.getService().add(s);
								}
							}
						
							map.put(
									park.getId(),
									park
							);
						} else {
							map.get((String) r.get(0).asMap().get("id"))
								.getConnectedPlaceId()
								.add(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(1).asInt())
									));
						}
					}
					
					return map.values().stream().collect(Collectors.toList());
                }
            } );
            
            session.close();
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to update the position of a vehicle in the system
	 * @param vehicle = the vehicle whose position has to be updated
	 */
	public void updatePositionVehicle(VehicleReaderType vehicle, String oldPosition) {
		try ( Session session = driver.session() )
        {
			//System.out.println("*******************************");
			//System.out.println("Vehicle id: " + vehicle.getId() + " --- Neo4jId: " + IdTranslator.getInstance().getIdTranslation(vehicle.getId()));
			final String queryDelete = StatementBuilder.getInstance()
					.deleteRelation(
							IdTranslator.getInstance().getIdTranslation(vehicle.getId()), 
							IdTranslator.getInstance().getIdTranslation(oldPosition), 
							"isLocatedIn");
			final String queryConnect = StatementBuilder.getInstance()
					.connectStatement(
							IdTranslator.getInstance().getIdTranslation(vehicle.getId()), 
							IdTranslator.getInstance().getIdTranslation(vehicle.getPosition()), 
							"isLocatedIn");
			final String queryUpdate = StatementBuilder.getInstance()
					.updatePositionVehicle(
							IdTranslator.getInstance().getIdTranslation(vehicle.getId()), 
							vehicle.getPosition(),
							vehicle.getState().name()
					);
			//System.out.println("Query delete: " + queryDelete);
			//System.out.println("Query connect: " + queryConnect);
			//System.out.println("Query update: " + queryUpdate);
            session.writeTransaction( new TransactionWork<Boolean>()
            {
                @Override
                public Boolean execute( Transaction tx )
                {
					tx.run(queryDelete);
					
					return true;
                }
            } );
            
            session.writeTransaction( new TransactionWork<Boolean>()
            {
                @Override
                public Boolean execute( Transaction tx )
                {
					tx.run(queryConnect);
					
					return true;
                }
            } );
            
            session.writeTransaction( new TransactionWork<Boolean>()
            {
                @Override
                public Boolean execute( Transaction tx )
                {
					tx.run(queryUpdate);
					
					return true;
                }
            } );
            
            session.close();
            
        } catch(Exception e) {
        		e.printStackTrace();
        }
	}

	/**
	 * Function to retrieve the basic informations about a specific place
	 * @param sourceNodeId = id of the node to be considered
	 * @return information about the desired node
	 */
	public SimplePlaceReaderType getPlace(String sourceNodeId) {
		if(sourceNodeId == null || sourceNodeId.equals("")) return null;
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getNodeById(
									IdTranslator.getInstance().getIdTranslation(sourceNodeId)
								);
			SimplePlaceReaderType result = session.writeTransaction( new TransactionWork<SimplePlaceReaderType>()
            {
                @Override
                public SimplePlaceReaderType execute( Transaction tx )
                {
					StatementResult result = tx.run(query);
					SimplePlaceReaderType place = null;
					SimplePlaceReaderType place1 = (new ObjectFactory()).createSimplePlaceReaderType();
					
					for(Record r : result.list()) {
						if(r != null) {
							if(place == null) {
								place = (new ObjectFactory()).createSimplePlaceReaderType();
								
								place.setName((String) r.get(0).asMap().get("name"));
								place.setId((String) r.get(0).asMap().get("id"));
								place.setCapacity(new BigInteger(Long.toString((Long) r.get(0).asMap().get("capacity"))));
								place.setAvgTimeSpent(new BigInteger(Long.toString((Long) r.get(0).asMap().get("avgTimeSpent"))));
							} else {
								place1.setName((String) r.get(1).asMap().get("name"));
								place1.setId((String) r.get(1).asMap().get("id"));
								place1.setCapacity(new BigInteger(Long.toString((Long) r.get(1).asMap().get("capacity"))));
								place1.setAvgTimeSpent(new BigInteger(Long.toString((Long) r.get(1).asMap().get("avgTimeSpent"))));
							
								if(getActualCapacityOfPlace(place1.getId()) >= 1) {
									place.getConnectedPlaceId().add(place1.getId());
								}
							}
						}
					}
					
					/*System.out.println("#########################");
					System.out.println("Place: " + place.getId());
					for(String s : place.getConnectedPlaceId()) System.out.println("++++ Connection --> " + s);*/
					
					return place;
                }
            } );
            
			session.close();
			
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve from Neo4j, given the state of the system,
	 * the actual capacity o a place. It is derived subtracting from the
	 * nominal capacity the number of vehicles actually in that place.
	 * @param position = position whose capacity we want to be retrieved
	 * @return the actual capacity
	 */
	public int getActualCapacityOfPlace(String position) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getActualCapacityStatementById(
										IdTranslator.getInstance().getIdTranslation(position));
			int result = session.writeTransaction( new TransactionWork<Integer>()
            {
                @Override
                public Integer execute( Transaction tx )
                {
					StatementResult result = tx.run(query);
					Record record = result.next();
					/*System.out.println("Actual capacity of node " + position + " = " + (Integer.parseInt(String.valueOf(record.get(0))) - 
							Integer.parseInt(String.valueOf(record.get(1)))));*/
					return 	Integer.parseInt(String.valueOf(record.get(0)));
                }
            } );
            
			session.close();
			
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return 0;
	}
	
	/**
	 * Function to retrieve all the incompatible material with the one
	 * whose id is given as parameter.
	 * @param materialId = material we want to find the incompatible materials
	 * @return list containing all the incompatible materials with the given one
	 */
	public List<String> getIncompatibleMaterialsGivenId(String materialId) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getIncompatibleMaterialsStatementById(
										IdTranslator.getInstance()
										.getIdTranslation(materialId)
								);
			List<String> result = session.writeTransaction( new TransactionWork<List<String>>()
            {
                @Override
                public List<String> execute( Transaction tx )
                {
                		List<String> incompatibleMaterials = new ArrayList<>();
					StatementResult result = tx.run(query);
					if(result != null) {
						for(Record r : result.list())
							incompatibleMaterials.add(String.valueOf(r.get(0)));
                	}				
					return incompatibleMaterials;
                }
            } );
            
			session.close();
			
            return result;
        } catch(Exception e) {
    		e.printStackTrace();
        }
		
		return null;
	}
	
	/**
	 * Function to retrieve all the materials carries by vehicles in a certain
	 * place specified as parameter
	 * @param placeId = id of the place in question
	 * @return list containing all the materials in the place
	 */
	public List<String> getMaterialsInPlaceGivenId(String placeId) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getMaterialInPlaceStatementById(
										IdTranslator.getInstance()
										.getIdTranslation(placeId)
								);
			List<String> result = session.writeTransaction( new TransactionWork<List<String>>()
            {
                @Override
                public List<String> execute( Transaction tx )
                {
                		List<String> materials = new ArrayList<>();
					StatementResult result = tx.run(query);

					for(Record r : result.list())
						materials.add(String.valueOf(r.get(0)));
					
					return materials;
                }
            } );
            
			session.close();
			
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve the assigned Neo4j label of a specific node
	 * given its id.
	 * @param id = the id of the node
	 * @return the corresponding label
	 */
	public String getLabelOfNode(String id) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getLabelOfNodeById(
										IdTranslator.getInstance()
										.getIdTranslation(id)
								);
			String result = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
					StatementResult result = tx.run(query);
					//System.out.println(result.single().get(0).get(0));
					return String.valueOf(result.single().get(0).get(0)).replace("\"", "");
                }
            } );
            
			session.close();
			
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to decrease the capacity of a place in the
	 * database
	 * @param idPlace = id of the desired place
	 */
	public void decreaseCapacityOfNodeGivenId(String idPlace) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getDecreaseStatementById(
										IdTranslator.getInstance()
										.getIdTranslation(idPlace)
								);
			session.writeTransaction( new TransactionWork<Boolean>()
            {
                @Override
                public Boolean execute( Transaction tx )
                {
					tx.run(query);
					return true;
                }
            } );
            
			session.close();
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
	}
	
	/**
	 * Function to increase the capacity of a node in the
	 * system
	 * @param idPlace = id of the place in question
	 */
	public void increaseCapacityOfNodeGivenId(String idPlace) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getIncreaseStatementById(
										IdTranslator.getInstance()
										.getIdTranslation(idPlace)
								);
			session.writeTransaction( new TransactionWork<Boolean>()
            {
                @Override
                public Boolean execute( Transaction tx )
                {
					tx.run(query);
					return true;
                }
            } );
            
			session.close();
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
	}

	/**
	 * Function to retrieve from the database all dangerous materials
	 * @return a list of dangerous materials
	 */
	public List<it.polito.dp2.rest.rns.jaxb.DangerousMaterialType> getDangerousMaterials() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getStatementByTypeAndConnection("DangerousMaterial", "isIncompatibleWith");
			
			List<it.polito.dp2.rest.rns.jaxb.DangerousMaterialType> result 
				= session.writeTransaction( new TransactionWork<List<it.polito.dp2.rest.rns.jaxb.DangerousMaterialType>>()
            {
                @Override
                public List<it.polito.dp2.rest.rns.jaxb.DangerousMaterialType> execute( Transaction tx )
                {
					StatementResult result = tx.run(query);
					Map<String, List<String>> materialsMap = new HashMap<>();
					List<it.polito.dp2.rest.rns.jaxb.DangerousMaterialType> materials = new ArrayList<>();
					
					for(Record r : result.list()) {
						String id = (String) r.get(0).asMap().get("id");
						String idNeo = String.valueOf(r.get(1).asInt());
						String idInc = IdTranslator.getInstance().fromNeo4jId(idNeo);
						
						if(!materialsMap.containsKey(id)) {
							materialsMap.put(id, new ArrayList<>());
						}
						if(!id.equals(idInc))
							materialsMap.get(id).add(idInc);
					}
					
					for(Entry<String, List<String>> entry : materialsMap.entrySet()) {
						it.polito.dp2.rest.rns.jaxb.DangerousMaterialType material = (new ObjectFactory()).createDangerousMaterialType();
						material.setId(entry.getKey());
						
						for(String s : entry.getValue()) material.getIncompatibleMaterial().add(s);
						
						materials.add(material);
					}
					
					return materials;
                }
            } );
            
			session.close();
			
			return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		return null;
	}

	/**
	 * Function to update the average time spent in a place
	 * @param duration = duration to be added
	 * @param counter = counter by which divide
	 */
	public void updateAvgTimeSpentPlace(String id, long duration, int counter) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance()
								.getUpdateAvgTimeStatementById(
										IdTranslator.getInstance().getIdTranslation(id),
										duration,
										counter
								);
			
			session.writeTransaction( new TransactionWork<Boolean>()
            {
                @Override
                public Boolean execute( Transaction tx )
                {
                	tx.run(query);
					
					return true;
                }
            } );
            
			session.close();
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
	}
}