package it.polito.dp2.rest.rns.z3;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.z3.Model;

import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;

public class Z3 {
	private Z3Model model;
	
	public Z3(String sourceNodeId, String destinationNodeId, String materialId) throws UnsatisfiableException {
		this.model = new Z3Model(sourceNodeId, destinationNodeId, materialId);
	}
	
	/**
	 * Function to retrieve a path from the model
	 * we have stored in the db.
	 * @return the list of ids included in the path or an empty list otherwise
	 */
	public List<String> findPath() {
		System.out.println("############# MODEL #############");
		Model result = this.model.evaluateModel();
		List<String> resultList = new ArrayList<String>();
		
		// If we have found nothing we don't do anything
		if(result != null)
		 resultList = this.parseModel(result);
		
		return resultList;
	}
	
	/**
	 * Function used to parse the string of the model and return
	 * a list containing the ids of the places we have to go through.
	 * @param result = the model obtained from the optimizer
	 * @return the list of id of the viable nodes
	 */
	private List<String> parseModel(Model result) {
		List<String> list = new ArrayList<String>();
		String[] s1 = result.toString().replace("(", "").replace(")", "").split("define-fun");
		for(String s : s1) s.replace("\n", " ");
		for(String s : s1) {
			if(s.contains("true")) {
				String[] s2 = s.split(" ");
				for(String ss : s2) {
					if(ss.startsWith("y")) {
						String[] s3 = ss.split("_");
						list.add(s3[1]);
					}
				}
			}
		}
		
		return list;
	}
}
