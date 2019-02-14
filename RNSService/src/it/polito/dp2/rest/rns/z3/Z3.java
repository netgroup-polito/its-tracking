package it.polito.dp2.rest.rns.z3;

import com.microsoft.z3.Context;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Optimize.Handle;

public class Z3 {
	private Optimize mkOptimize;
	private Z3Model model;
	
	
	public Z3(String sourceNodeId, String destinationNodeId) throws UnsatisfiableException {
		this.model = new Z3Model(sourceNodeId, destinationNodeId);
	}
}
