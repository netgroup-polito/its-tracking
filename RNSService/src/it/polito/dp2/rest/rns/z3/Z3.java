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
	
	// TODO: retrieve from the model the path
	public List<String> findPath() {
		System.out.println("############# MODEL #############");
		List<String> result = this.parsModel(this.model.evaluateModel());
		
		for(String s : result) System.out.println(s);
		
		return result;
	}
	
	private List<String> parsModel(Model result) {
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
