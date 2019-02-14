package it.polito.dp2.rest.rns.z3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Optimize.Handle;

import it.polito.dp2.rest.rns.graph.Graph;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;

/**
 * 
 * @author eugeniogallea
 *
 */
public class Z3Model {
	private static Graph graph = Graph.getInstance();
	private List<String> alreadyVisitedNodes;
	private Handle handle;
	private Optimize mkOptimize;
	private Context ctx;
	private boolean pathPossible;
	
	public Z3Model(String sourceNodeId, String destinationNodeId) throws UnsatisfiableException {
		if(!graph.hasEnoughCapacity(sourceNodeId))
			throw(new UnsatisfiableException("Node " + sourceNodeId + " has no more room for another vehicle"));
	
		this.alreadyVisitedNodes = new ArrayList<>();
		this.pathPossible = false;
		
		BoolExpr currentNodeBool = ctx.mkBoolConst(sourceNodeId);
		this.recurGraph(sourceNodeId, destinationNodeId, currentNodeBool);
	}
	
	// used to convert boolean values to integer
	private IntExpr bool_to_int(BoolExpr value) {
		IntExpr integer = ctx.mkIntConst("integer_" + value);
		// value -> (integer == 1)
		// !value -> (integer == 0)
		mkOptimize.Add((ctx.mkImplies(value, ctx.mkEq(integer, ctx.mkInt(1)))));
		mkOptimize.Add((ctx.mkImplies(ctx.mkNot(value), ctx.mkEq(integer, ctx.mkInt(0)))));
		return integer;
	}
	
	private void recurGraph(String sourceNodeId, String destinationNodeId, BoolExpr previousNodeBool) {
		// Recursion end condition
		if(sourceNodeId.equals(destinationNodeId)) {
			this.pathPossible = true;
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
