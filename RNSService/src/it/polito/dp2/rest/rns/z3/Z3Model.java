package it.polito.dp2.rest.rns.z3;

import java.util.ArrayList;
import java.util.List;

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
 * # - z = connection between x_curr -> x_conn                                    #
 * # - y_dangerousMaterial{i}_{j} = dangerous material 'i' is in the place 'j'    #                                                      #
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
	
	public Z3Model(String sourceNodeId, String destinationNodeId, String materialId) throws UnsatisfiableException {
		if(Neo4jInteractions.getInstance().getActualCapacityOfPlace(sourceNodeId) < 1)
			throw(new UnsatisfiableException("Node " + sourceNodeId + " has no more room for another vehicle"));
		
		System.out.println("############# INITIALIZATION OF Z3 MODEL #############");
		ctx = new Context();
		mkOptimize = ctx.mkOptimize();
		this.foundEnd = false;
		BoolExpr x_src = ctx.mkBoolConst(sourceNodeId);
		BoolExpr x_dst = ctx.mkBoolConst(destinationNodeId);
		
		// Add the fact that x_src and x_dst have to be one
		mkOptimize.Add(x_src, ctx.mkBool(true));
		mkOptimize.Add(x_dst, ctx.mkBool(true));
		
		this.defineConstraintsForNodes(sourceNodeId, materialId, destinationNodeId, null);
	}

	/**
	 * Function used to convert boolean values to integer ones
	 * @param value = value to be converted
	 * @return corresponding integer expression
	 */
	private IntExpr bool_to_int(BoolExpr value) {
		IntExpr integer = ctx.mkIntConst("integer_" + value);
		// value -> (integer == 1)
		// !value -> (integer == 0)
		mkOptimize.Add((ctx.mkImplies(value, ctx.mkEq(integer, ctx.mkInt(1)))));
		mkOptimize.Add((ctx.mkImplies(ctx.mkNot(value), ctx.mkEq(integer, ctx.mkInt(0)))));
		return integer;
	}
	
	public void defineConstraintsForNodes(String source, String materialId, String destination, List<String> tabuList) {
		DangerousMaterialImpl material = new DangerousMaterialImpl(materialId, Neo4jInteractions.getInstance().getIncompatibleMaterialsGivenId(materialId));
		System.out.println("Current place: " + source);
		SimplePlaceReaderType current = Neo4jInteractions.getInstance().getPlace(source);
		
		if(current == null) {
			System.out.println("Couldn't retrieve node with id: " + source);
			return;
		}
		
		// Retrieve materials and actual capacity
		int actualCapacity = Neo4jInteractions.getInstance().getActualCapacityOfPlace(current.getId());
		List<String> materials = Neo4jInteractions.getInstance().getMaterialsInPlaceGivenId(current.getId());
		
		/*// Check on capacity and materials
		if(actualCapacity < 1) {
			System.out.println("Place " + current.getId() + " has no more room.");
			return;
		}*/
		
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
			foundEnd = true;
			return;
		}
		
		if(tabuList == null) tabuList = new ArrayList<>();
		
		// Capacity constraint
		mkOptimize.Add(ctx.mkImplies(
				ctx.mkLe(ctx.mkInt(actualCapacity), ctx.mkInt(1)), // if capacity < 1
				ctx.mkEq(ctx.mkBoolConst(current.getId()), ctx.mkBool(false)))); // this node has to be false
		
		// Connections
		ArithExpr leftSide = ctx.mkInt(0);
		for(String id : current.getConnectedPlaceId()) {
			if(!tabuList.contains(id)) {
				ctx.mkAdd(leftSide, bool_to_int(ctx.mkBoolConst(id)));
			}
		}
		mkOptimize.Add(ctx.mkImplies(ctx.mkBoolConst(current.getId()), ctx.mkEq(leftSide, ctx.mkInt(1))));
		
		// Add in order to not loop back
		tabuList.add(current.getId());
		
		// Recur
		for(String id : current.getConnectedPlaceId())
			if(!tabuList.contains(id))
				this.defineConstraintsForNodes(id, materialId, destination, tabuList);
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
