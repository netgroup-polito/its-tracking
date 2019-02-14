package it.polito.dp2.rest.rns.z3;

import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;

public class Z3 {
	private Z3Model model;
	
	public Z3(String sourceNodeId, String destinationNodeId, String materialId) throws UnsatisfiableException {
		this.model = new Z3Model(sourceNodeId, destinationNodeId, materialId);
	}
	
	// TODO: retrieve from the model the path
	public void findPath() {
		System.out.println(this.model.evaluateModel());
	}
}
