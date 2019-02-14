package it.polito.dp2.rest.rns.z3;

import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;

public class Z3 {
	private Z3Model model;
	
	
	public Z3(String sourceNodeId, String destinationNodeId) throws UnsatisfiableException {
		this.model = new Z3Model(sourceNodeId, destinationNodeId);
	}
}
