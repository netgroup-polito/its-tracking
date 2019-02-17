package it.polito.dp2.rest.rns.z3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Status;

import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
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
	
	private Map<String, List<BoolExpr>> incomingConnections = new HashMap<>();
	private Map<String, List<BoolExpr>> outgoingConnections = new HashMap<>();
	private Map<String, BoolExpr> nodes = new HashMap<>();
	
	public Z3Model(String sourceNodeId, String destinationNodeId, String materialId) throws UnsatisfiableException {
		if(Neo4jInteractions.getInstance().getActualCapacityOfPlace(sourceNodeId) < 1)
			throw(new UnsatisfiableException("Node " + sourceNodeId + " has no more room for another vehicle"));
		
		ctx = new Context();
		mkOptimize = ctx.mkOptimize();
		this.foundEnd = false;
		
		System.out.println("############# INITIALIZATION OF Z3 MODEL #############");
		this.createBooleanExpressions(sourceNodeId, materialId, destinationNodeId, null, null);
		System.out.println("#############  DEFINITION OF CONSTRAINTS #############");
		this.defineHardConstraints(sourceNodeId, destinationNodeId);
	}

	private void defineHardConstraints(String source, String destination) {
		for(Entry<String, BoolExpr> node : this.nodes.entrySet()) {
			System.out.println("\n+ Definition of constraints for node " + node.getKey());
			if(node.getKey().contentEquals(source)) {
				System.out.println("++ Source node. Only outgoing connection constraints.");
				List<BoolExpr> outgoings = this.outgoingConnections.get(node.getKey());
				
				mkOptimize.Add(ctx.mkEq(ctx.mkAnd(node.getValue(), ctx.mkBool(true)), ctx.mkBool(true)));
				mkOptimize.Add(ctx.mkImplies(node.getValue(), ctx.mkAtLeast(outgoings.stream().toArray(BoolExpr[]::new), 1)));
				mkOptimize.Add(ctx.mkImplies(node.getValue(), ctx.mkAtMost(outgoings.stream().toArray(BoolExpr[]::new), 1)));
				
			} else if(node.getKey().contentEquals(destination)) {
				System.out.println("++ Destination node. Only incoming connection constraints.");
				List<BoolExpr> incomings = this.incomingConnections.get(node.getKey());
				
				mkOptimize.Add(ctx.mkEq(ctx.mkAnd(node.getValue(), ctx.mkBool(true)), ctx.mkBool(true)));
				mkOptimize.Add(ctx.mkImplies(node.getValue(), ctx.mkAtLeast(incomings.stream().toArray(BoolExpr[]::new), 1)));
				mkOptimize.Add(ctx.mkImplies(node.getValue(), ctx.mkAtMost(incomings.stream().toArray(BoolExpr[]::new), 1)));
				
			} else {
				BoolExpr[] incomings = this.incomingConnections.get(node.getKey()).stream().toArray(BoolExpr[]::new);
				int incomingsSize = this.incomingConnections.get(node.getKey()).size();
				BoolExpr[] outgoings = this.outgoingConnections.get(node.getKey()).stream().toArray(BoolExpr[]::new);
				int outgoingsSize = this.outgoingConnections.get(node.getKey()).size();
				
				/*
				 * Incoming connections. I have to declare that:
				 * 1) if z_incoming = true -> y_curr = true;
				 * 2) z_incoming_i + ... + z_incoming_n = 1; (so if one connection incoming is taken the others have to be false)
				 */
				System.out.println("+++ Incoming relations for node " + node.getKey());
				int i = 0;
				for(BoolExpr z : incomings) {
					List<BoolExpr> tmp = new ArrayList<>();
					for(int j = 0; j < incomingsSize; j++) {
						if(j != i) {
							tmp.add(incomings[j]);
						}
					}
					
					mkOptimize.Add(ctx.mkImplies(z, ctx.mkEq(ctx.mkOr(tmp.stream().toArray(BoolExpr[]::new)), ctx.mkBool(false))));
					mkOptimize.Add(ctx.mkImplies(z, node.getValue()));
					i++;
				}
				
				/*
				 * Outgoing connections. I have to declare that:
				 * 1) z_outgoing_i + ... + z_outgoing_n = 1; (so if one connection outgoing is taken the others have to be false)
				 */
				System.out.println("+++ Outgoing relations for node " + node.getKey());
				i = 0;
				for(BoolExpr z : outgoings) {
					List<BoolExpr> tmp = new ArrayList<>();
					for(int j = 0; j < outgoingsSize; j++) {
						if(j != i) {
							tmp.add(outgoings[j]);
						}
					}
					
					mkOptimize.Add(ctx.mkImplies(z, ctx.mkEq(ctx.mkOr(tmp.stream().toArray(BoolExpr[]::new)), ctx.mkBool(false))));
					i++;
				}
				if(outgoingsSize == 0) {
					mkOptimize.Add(ctx.mkEq(node.getValue(), ctx.mkBool(false)));
				} else {
					//mkOptimize.Add(node.getValue(), ctx.mkAtLeast(outgoings, 1));
					//mkOptimize.Add(node.getValue(), ctx.mkAtMost(outgoings, 1));
				}
				/**
				 * In this case this is neither source or destination, so if it 
				 * has an incoming relation it has to have an outgoing relation
				 * set to true as well
				 */
				mkOptimize.Add(ctx.mkImplies(
						node.getValue(),
						ctx.mkEq(
								ctx.mkAnd(ctx.mkOr(incomings), ctx.mkOr(outgoings)), 
								ctx.mkBool(true))
						));
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
	
	public void createBooleanExpressions(String source, String materialId, String destination, List<String> tabuList, BoolExpr z_prev) {
		DangerousMaterialImpl material = new DangerousMaterialImpl(
											materialId, 
											Neo4jInteractions.getInstance().getIncompatibleMaterialsGivenId(materialId)
									);
		//System.out.println("Current place: " + source);
		SimplePlaceReaderType current = Neo4jInteractions.getInstance().getPlace(source);
		
		if(current == null) {
			System.out.println("Couldn't retrieve node with id: " + source);
			return;
		}
		
		// Retrieve materials and actual capacity
		int actualCapacity = Neo4jInteractions.getInstance().getActualCapacityOfPlace(current.getId());
		List<String> materials = Neo4jInteractions.getInstance().getMaterialsInPlaceGivenId(current.getId());
		
		// Check on capacity and materials
		if(actualCapacity < 1) {
			System.out.println("Place " + current.getId() + " has no more room.");
			return;
		}
		
		for(String mat : materials) {
			if(!material.isCompatibleWith(mat)) {
				System.out.println(
						"Node " + current.getId() + 
						" contains material " + mat + 
						" that is not compatible with " + materialId);
				return;
			}
		}
		
		// Check on end of recursion
		if(source.equals(destination)) {
			System.out.println("!!!! Found end: " + source + " !!!!");
			
			// Current node
			BoolExpr y_curr = ctx.mkBoolConst("y_" + current.getId());
			this.nodes.put(current.getId(), y_curr);
			
			// Incoming connections
			if(!this.incomingConnections.containsKey(current.getId())) {
				this.incomingConnections.put(current.getId(), new ArrayList<>());
			}
			
			if(z_prev != null) this.incomingConnections.get(current.getId()).add(z_prev);
			
			foundEnd = true;
			return;
		}
		
		if(tabuList == null) tabuList = new ArrayList<>();
		// Add in order to not loop back
		tabuList.add(current.getId());
		
		// Current node
		BoolExpr y_curr = ctx.mkBoolConst("y_" + current.getId());
		if(!this.nodes.containsKey(current.getId()))
			this.nodes.put(current.getId(), y_curr);
		
		// Outgoing connections
		List<BoolExpr> connections = new ArrayList<>();
		for(String id : current.getConnectedPlaceId()) {
			if(!tabuList.contains(id)) {
				connections.add(ctx.mkBoolConst("z_" + current.getId() + "_" + id));
			}
		}
		if(!this.outgoingConnections.containsKey(current.getId()))
			this.outgoingConnections.put(current.getId(), connections);
		
		// Incoming connections
		if(!this.incomingConnections.containsKey(current.getId())) {
			this.incomingConnections.put(current.getId(), new ArrayList<>());
		}
		if(z_prev != null) this.incomingConnections.get(current.getId()).add(z_prev);
		
		// Recur
		for(String id : current.getConnectedPlaceId())
			if(!tabuList.contains(id))
				this.createBooleanExpressions(id, materialId, destination, tabuList, ctx.mkBoolConst("z_" + current.getId() + "_" + id));
	}
	
	/**
	 * Function to launch the evaluation of the conditions
	 * @return a status object to access the result
	 */
	private Status evaluateFeasibility() {
		// We perform a check only if in the traverse of the graph
		// we actually found at least once the destination
		System.out.println("Fesibility of the problem: " + mkOptimize.Check());
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
