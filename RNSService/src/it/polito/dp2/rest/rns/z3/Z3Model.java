package it.polito.dp2.rest.rns.z3;

import java.util.ArrayList;
import java.util.LinkedList;
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
 * # - y_dangerousMaterial{i}_dangerousMaterial{j} = dangerous material 'i' is    #
 * #         compatible with dangerous material 'j', means it is true             #
 * #                                                                              #
 * ################################################################################
 * 
 * @author eugeniogallea
 *
 */
public class Z3Model {
	private List<String> alreadyVisitedNodes;
	private List<String> incompatibleMaterials;
	private boolean foundEnd;
	private Optimize mkOptimize;
	private Context ctx;
	
	public Z3Model(String sourceNodeId, String destinationNodeId, String materialId) throws UnsatisfiableException {
		if(Neo4jInteractions.getInstance().getActualCapacityOfPlace(sourceNodeId) < 1)
			throw(new UnsatisfiableException("Node " + sourceNodeId + " has no more room for another vehicle"));
		this.incompatibleMaterials = Neo4jInteractions.getInstance().getIncompatibleMaterialsGivenId(materialId); 
		this.foundEnd = false;
		this.alreadyVisitedNodes = new ArrayList<>();
		BoolExpr x_curr = ctx.mkBoolConst(sourceNodeId);
		this.recurGraph(sourceNodeId, destinationNodeId, materialId, x_curr);
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
	
	/**
	 * Function used to recur onto the graph to load all nodes with their corresponding
	 * boolean expression.
	 * @param sourceNodeId = current node
	 * @param destinationNodeId = final node, to which we want to arrive (end of recursion)
	 * @param x_curr = preceding node, to which we want to create a connection
	 */
	private void recurGraph(String sourceNodeId, String destinationNodeId, String materialId, BoolExpr x_curr) {
		// Recursion end condition
		if(sourceNodeId.equals(destinationNodeId)) {
			foundEnd = true;
			return;
		}
		
		if(this.alreadyVisitedNodes.contains(sourceNodeId)) return;
		
		// Retrieve the current node, with connected places (with enough capacity)
		SimplePlaceReaderType currentPlace = Neo4jInteractions.getInstance().getPlace(sourceNodeId);
		if(currentPlace == null) return;
		
		// Create boolean expressions for the materials in the place
		List<BoolExpr> materials = new LinkedList<>();
		List<String> materialsInPlaceList = Neo4jInteractions.getInstance().getMaterialsInPlaceGivenId(currentPlace.getId());
		
		// TODO: materials constraints
		
		// Create boolean expressions for the place and connections
		List<BoolExpr> relations = new LinkedList<>();
		for(String id : currentPlace.getConnectedPlaceId()) { 
			BoolExpr x_conn = ctx.mkBoolConst(id);
			BoolExpr z = ctx.mkBoolConst(sourceNodeId + "_" + id);
			
			// If the previous node and the connected one's boolean expressions are
			// set to one, then the relation is taken
			mkOptimize.Add(ctx.mkImplies(ctx.mkAnd(x_curr, x_conn), z));
			relations.add(z);

			this.recurGraph(id, destinationNodeId, materialId, x_conn);
		}
		
		// Arithmetic expression to select only one path
		ArithExpr leftSide = null;
		for(BoolExpr xi : relations) {
			leftSide = ctx.mkAdd(leftSide, bool_to_int(xi));
		}
		// x0 + ... + xn = 1
		mkOptimize.Add(ctx.mkEq(leftSide, ctx.mkInt(1)));
		
	}
	
	/**
	 * Function to launch the evaluation of the conditions
	 * @return a status object to access the result
	 */
	private Status evaluateFeasibility() {
		// We perform a check only if in the traverse of the graph
		// we actually found at least once the destination
		return (this.foundEnd) ? this.mkOptimize.Check() : null;
	}
	
	public Model evaluateModel() {
		return (this.evaluateFeasibility() == Status.SATISFIABLE) ?
				mkOptimize.getModel() : // We can find a model
				null; // no model could be found
	}

}
