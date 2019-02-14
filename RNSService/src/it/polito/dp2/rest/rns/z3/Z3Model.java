package it.polito.dp2.rest.rns.z3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;

import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;

/**
 * Z3 class that can load the model of the graph, and while loading it
 * it prunes the nodes where the vehicle can't be due to capacity constraints
 * 
 * @author eugeniogallea
 *
 */
public class Z3Model {
	private List<String> alreadyVisitedNodes;
	private Optimize mkOptimize;
	private Context ctx;
	
	public Z3Model(String sourceNodeId, String destinationNodeId) throws UnsatisfiableException {
		if(Neo4jInteractions.getInstance().getActualCapacityOfPlace(sourceNodeId) < 1)
			throw(new UnsatisfiableException("Node " + sourceNodeId + " has no more room for another vehicle"));
	
		this.alreadyVisitedNodes = new ArrayList<>();
		BoolExpr currentNodeBool = ctx.mkBoolConst(sourceNodeId);
		this.recurGraph(sourceNodeId, destinationNodeId, currentNodeBool);
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
	 * @param previousNodeBool = preceding node, to which we want to create a connection
	 */
	private void recurGraph(String sourceNodeId, String destinationNodeId, BoolExpr previousNodeBool) {
		// Recursion end condition
		if(sourceNodeId.equals(destinationNodeId)) {
			return;
		}
		
		if(this.alreadyVisitedNodes.contains(sourceNodeId)) return;
		
		// Retrieve the current node, with connected places (with enough capacity)
		SimplePlaceReaderType currentPlace = Neo4jInteractions.getInstance().getPlace(sourceNodeId);
		if(currentPlace == null) return;
		
		// Create boolean expressions for the place and connections
		List<BoolExpr> relations = new LinkedList<>();
		for(String id : currentPlace.getConnectedPlaceId()) { 
			BoolExpr connectedNodeBool = ctx.mkBoolConst(id);
			BoolExpr relationBool = ctx.mkBoolConst(sourceNodeId + "_" + id);
			
			// If the previous node and the connected one's boolean expressions are
			// set to one, then the relation is taken
			mkOptimize.Add(ctx.mkImplies(ctx.mkAnd(previousNodeBool, connectedNodeBool), relationBool));
			relations.add(relationBool);

			this.recurGraph(id, destinationNodeId, connectedNodeBool);
		}
		
		// Arithmetic expression to select only one path
		ArithExpr leftSide = null;
		for(BoolExpr xi : relations) {
			leftSide = ctx.mkAdd(leftSide, bool_to_int(xi));
		}
		// x0 + ... + xn = 1
		mkOptimize.Add(ctx.mkEq(leftSide, ctx.mkInt(1)));
		
	}

}
