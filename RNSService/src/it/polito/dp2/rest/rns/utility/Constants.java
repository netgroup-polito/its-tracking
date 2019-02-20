package it.polito.dp2.rest.rns.utility;

public interface Constants {
	public static final String Neo4jURL = "bolt://localhost:7687";
	public static final String Neo4jUsername = "neo4j";
	public static final String Neo4jPassword = "password";
	public static final String MapXMLPath = "xml/map.xml";
	
	public static final String dangerousMaterial1 = "dangerousMaterial1";
	public static final String dangerousMaterial2 = "dangerousMaterial2";
	public static final String dangerousMaterial3 = "dangerousMaterial3";
	public static final String dangerousMaterial4 = "dangerousMaterial4";
	public static final String dangerousMaterial5 = "dangerousMaterial5";
	public static final String dangerousMaterial6 = "dangerousMaterial6";
	public static final String dangerousMaterial7 = "dangerousMaterial7";
	
	public static final String[] notCompatibleWith1 = {"dangerousMaterial3", "dangerousMaterial5", "dangerousMaterial6", "dangerousMaterial7"};
	public static final String[] notCompatibleWith2 = {"dangerousMaterial1", "dangerousMaterial3", "dangerousMaterial5", "dangerousMaterial6"};
	public static final String[] notCompatibleWith3 = {"dangerousMaterial2", "dangerousMaterial4"};
	public static final String[] notCompatibleWith4 = {"dangerousMaterial2", "dangerousMaterial6", "dangerousMaterial5"};

	//public static Map<String, Integer> countVehiclePlace = new HashMap<>();
}
