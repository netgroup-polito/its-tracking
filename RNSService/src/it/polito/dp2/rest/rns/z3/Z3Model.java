package it.polito.dp2.rest.rns.z3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Status;

import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import it.polito.dp2.rest.rns.resources.RNSCore;
import it.polito.dp2.rest.rns.utility.DangerousMaterialImpl;

/**
 * Z3 class that can load the model of the graph, and while loading it
 * it prunes the nodes where the vehicle can't be due to capacity constraints.
 * 
 * ################################################################################
 * #                                                                              #
 * #                                  LEGEND                                      #
 * #                                                                              #
 * ################################################################################
 * #                                                                              # 
 * # - x_curr = current node in which we are during recursion                     #
 * # - x_conn = connected node, which we have to visit in the next level of       #
 * #         recursion                                                            #
 * # - z = connection between x_curr -> x_conn                                    #s                                                    #
 * #                                                                              #
 * ################################################################################
 * 
 * @author eugeniogallea
 *
 */
public class Z3Model {
	private boolean foundEnd;
	private Optimize mkOptimize;
	private Context ctx;
	
	private Map<String, List<String>> connections = new HashMap<>();
	private Map<String, BoolExpr> connectionsBool = new HashMap<>();
	private Map<String, BoolExpr> nodes = new HashMap<>();
	private Map<String, ArithExpr> nodesCapacity = new HashMap<>();
	
	private String sourceNodeId = "";
	
	public Z3Model(String sourceNodeId, String destinationNodeId, List<String> materialId) throws UnsatisfiableException {
		if(Neo4jInteractions.getInstance().getActualCapacityOfPlace(sourceNodeId) < 1)
			throw(new UnsatisfiableException("Node " + sourceNodeId + " has no more room for another vehicle"));
		
		ctx = new Context();
		mkOptimize = ctx.mkOptimize();
		this.foundEnd = false;
		
		//System.out.println("Source: " + sourceNodeId + " --- Destination: " + destinationNodeId);
		//System.out.println("############# INITIALIZATION OF Z3 MODEL #############");
		this.sourceNodeId = sourceNodeId;
		this.createBooleanExpressions(sourceNodeId, materialId, destinationNodeId, null, null, null, null);
		//System.out.println("#############  DEFINITION OF CONSTRAINTS #############");
		this.defineHardConstraints(sourceNodeId, destinationNodeId);
		this.defineSoftConstraints();
	}
	
	/**
	 * Function to define soft constraints on the 
	 * average time.
	 */
	private void defineSoftConstraints() {
		for(Entry<String, BoolExpr> node : this.nodes.entrySet()) {
			SimplePlaceReaderType place = Neo4jInteractions.getInstance().getPlace(node.getKey());
			mkOptimize.AssertSoft(ctx.mkNot(node.getValue()), place.getAvgTimeSpent().intValue(), "latency");
		}
	}

	/**
	 * Function to define hard constraints for the optimizer
	 * @param source = where we start
	 * @param destination = where we want to go
	 */
	private void defineHardConstraints(String source, String destination) {
		for(Entry<String, BoolExpr> node : this.nodes.entrySet()) {
			ArithExpr capacity = this.nodesCapacity.get(node.getKey());
			mkOptimize.Add(ctx.mkImplies(ctx.mkLt(capacity, ctx.mkInt(0)), ctx.mkNot(node.getValue())));
			
			if(source.equals(node.getKey())) {
				
				mkOptimize.Add(ctx.mkEq(node.getValue(), ctx.mkBool(true)));
				
			} else if(destination.equals(node.getKey())) {
				
				List<BoolExpr> conns = new ArrayList<>();
				for(String conn : this.connections.get(node.getKey())) {
					conns.add(this.connectionsBool.get(conn));
				}
				
				mkOptimize.Add(ctx.mkEq(node.getValue(), ctx.mkBool(true)));
				mkOptimize.Add(ctx.mkImplies(node.getValue(), ctx.mkAtLeast(conns.stream().toArray(BoolExpr[]::new), 1)));
				mkOptimize.Add(ctx.mkImplies(node.getValue(), ctx.mkAtMost(conns.stream().toArray(BoolExpr[]::new), 1)));
				
			} else {
				
				List<BoolExpr> conns = new ArrayList<>();
				for(String conn : this.connections.get(node.getKey())) {
					conns.add(this.connectionsBool.get(conn));
				}
				
				mkOptimize.Add(ctx.mkImplies(node.getValue(), ctx.mkOr(conns.stream().toArray(BoolExpr[]::new))));
				
			}
		}
	}

	/**
	 * Function used to convert boolean values to integer ones
	 * @param value = value to be converted
	 * @return corresponding integer expression
	 */
	@SuppressWarnings("unused")
	private IntExpr bool_to_int(BoolExpr value) {
		IntExpr integer = ctx.mkIntConst("integer_" + value);
		// value -> (integer == 1)
		// !value -> (integer == 0)
		mkOptimize.Add((ctx.mkImplies(value, ctx.mkEq(integer, ctx.mkInt(1)))));
		mkOptimize.Add((ctx.mkImplies(ctx.mkNot(value), ctx.mkEq(integer, ctx.mkInt(0)))));
		return integer;
	}
	
	/**
	 * Function to setup the boolean expressions for the map.
	 * @param source = source where we are at the moment
	 * @param materialId = material carried by the vehicle
	 * @param destination = where the vehicle wants to go
	 * @param tabuList = already visited places in the path, no need to bother setting them up
	 * @param z_prev = connection where we came from
	 * @param y_prev = previous node where we were
	 * @param prevId = id of the previous node where we came from
	 */
	public void createBooleanExpressions(String source, List<String> materialIds, String destination, List<String> tabuList, BoolExpr z_prev, BoolExpr y_prev, String prevId) {
		ArrayList<DangerousMaterialImpl> materialsTransported = new ArrayList<>();
		for(String materialId : materialIds) {		
			if(materialId != null && !materialId.equals("")) {
				materialsTransported.add(new DangerousMaterialImpl(
												materialId, 
												Neo4jInteractions.getInstance().getIncompatibleMaterialsGivenId(materialId)
										));
			}
		}
		
		SimplePlaceReaderType current = Neo4jInteractions.getInstance().getPlace(source);
		if(current == null) return;
		
		// Retrieve materials and actual capacity
		int actualCapacity = Neo4jInteractions.getInstance().getActualCapacityOfPlace(current.getId());
		List<String> materials = RNSCore.getInstance().getMaterialsInPlaceGivenId(current.getId());
		
		ArithExpr leftSide = ctx.mkSub(ctx.mkInt(actualCapacity), ctx.mkInt(1));
		this.nodesCapacity.put(current.getId(), leftSide);
		
		// Check on capacity and materials
		/*if(actualCapacity < 1) {
			//System.out.println("Place " + current.getId() + " has no more room.");
			return;
		}*/
		
		if( !materialsTransported.isEmpty() && materials != null) {
			for( DangerousMaterialImpl material : materialsTransported) {
				for(String m : materials) {
					String mat = m.replace("\"", "");
					//System.out.println("Comparing " + mat + " --- " + material.getId());
					if(!material.isCompatibleWith(mat)) {
						/*System.out.println(
								"Node " + current.getId() + 
								" contains material " + mat + 
								" that is not compatible with " + material.getId());*/
						return;
					}
				}
			}
		}
		
		// Check on end of recursion
		if(source.equals(destination)) {
			//System.out.println("!!!! Found end: " + source + " !!!!");
			
			// Current node
			BoolExpr y_curr = ctx.mkBoolConst("y_" + current.getId());
			
			this.nodes.put(current.getId(), y_curr);
			if(!connections.containsKey(current.getId())) this.connections.put(current.getId(), new ArrayList<String>());
			this.connections.get(current.getId()).add("z_" + prevId + "_" + current.getId());
			this.connectionsBool.put("z_" + prevId + "_" + current.getId(), ctx.mkAnd(y_prev, y_curr));
			
			foundEnd = true;
			return;
		}
		
		// Check if it is a gate or a parking area, if it's not the end
		// it can be neither a gate nor a parking area
		String label = Neo4jInteractions.getInstance().getLabelOfNode(current.getId());
		//System.out.println(current.getId() + ": " + label);
		if(!current.getId().equals(this.sourceNodeId) && (label.equals("Gate") || label.equals("ParkingArea"))) {
			//System.out.println("Place " + current.getId() + " is not viable as node because it's a " + label + ".");
			return;
		}
		
		if(tabuList == null) tabuList = new ArrayList<>();
		// Add in order to not loop back
		tabuList.add(current.getId());
		
		// Current node
		BoolExpr y_curr = ctx.mkBoolConst("y_" + current.getId());
		if(!this.nodes.containsKey(current.getId()))
			this.nodes.put(current.getId(), y_curr);
		
		if(prevId != null) {
			this.nodes.put(current.getId(), y_curr);
			if(!connections.containsKey(current.getId())) this.connections.put(current.getId(), new ArrayList<String>());
			this.connections.get(current.getId()).add("z_" + prevId + "_" + current.getId());
			this.connectionsBool.put("z_" + prevId + "_" + current.getId(), ctx.mkAnd(y_prev, y_curr));
		}
		
		// Outgoing connections
		for(String id : current.getConnectedPlaceId()) {
			if(!tabuList.contains(id)) {
				//System.out.println("+++++ Visiting next: " + id);
				this.createBooleanExpressions(id, materialIds, destination, tabuList, ctx.mkBoolConst("z_" + current.getId() + "_" + id), y_curr, source);
			}
		}		
	}
	
	/**
	 * Function to launch the evaluation of the conditions
	 * @return a status object to access the result
	 */
	private Status evaluateFeasibility() {
		// We perform a check only if in the traverse of the graph
		// we actually found at least once the destination
		//System.out.println("Fesibility of the problem: " + mkOptimize.Check() + " --- " + foundEnd);
		return (this.foundEnd) ? this.mkOptimize.Check() : null;
	}
	
	/**
	 * Function to evaluate the model
	 * @return the model that Z3 has found
	 */
	public Model evaluateModel() {
		return (this.evaluateFeasibility() == Status.SATISFIABLE) ?
				mkOptimize.getModel() : // We can find a model
				null; // no model could be found
	}

}
