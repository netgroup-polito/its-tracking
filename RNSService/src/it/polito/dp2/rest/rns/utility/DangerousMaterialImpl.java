package it.polito.dp2.rest.rns.utility;

import java.util.ArrayList;
import java.util.List;

import it.polito.dp2.rest.rns.jaxb.DangerousMaterialType;

/**
 * Implementation of the JAXB generated class for dangerous material.
 * @author eugeniogallea
 */
public class DangerousMaterialImpl extends DangerousMaterialType {

	public DangerousMaterialImpl() { }
	
	public DangerousMaterialImpl(String id, List<String> incompatibleMaterials) {
		this.id = id;
		this.incompatibleMaterial = new ArrayList<>();
		incompatibleMaterials.stream().forEach((material) -> {
			String s = material.replace("\"", "");
			this.incompatibleMaterial.add(s);
		});
	}
	
	public DangerousMaterialImpl(String id, String[] incompatibleMaterials) {
		this.id = id;
		this.incompatibleMaterial = new ArrayList<>();
		for(String material : incompatibleMaterials) {
			String s = material.replace("\"", "");
			this.incompatibleMaterial.add(s);
		}
	}
	
	public boolean isCompatibleWith(String material) {
		//System.out.println("Checking compatibility between " + this.id + " --- " + material + " = " + this.incompatibleMaterial.contains(material));
		return !this.incompatibleMaterial.contains(material);
	}

}
