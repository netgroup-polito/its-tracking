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
			this.incompatibleMaterial.add(material);
		});
	}
	
	public DangerousMaterialImpl(String id, String[] incompatibleMaterials) {
		this.id = id;
		this.incompatibleMaterial = new ArrayList<>();
		for(String material : incompatibleMaterials) {
			this.incompatibleMaterial.add(material);
		}
	}
	
	public boolean isCompatibleWith(String material) {
		return this.incompatibleMaterial.contains(material);
	}

}
