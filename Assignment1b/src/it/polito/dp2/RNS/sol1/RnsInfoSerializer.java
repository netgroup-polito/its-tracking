package it.polito.dp2.RNS.sol1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.polito.dp2.RNS.RnsReader;
import it.polito.dp2.RNS.RnsReaderException;
import it.polito.dp2.RNS.RnsReaderFactory;
import it.polito.dp2.RNS.sol1.jaxb.ObjectFactory;
import it.polito.dp2.RNS.sol1.jaxb.RnsReaderType;

/**
 * This class is used to generate randomic values for places to start with.
 * It is base on Assignment1b RnsInfo implementation and adapted to the particular
 * schema developed for the project
 * @author eugeniogallea
 */
public class RnsInfoSerializer {
	private RnsReader monitor;
	private DateFormat dateFormat;
	private RnsReaderType rnsReader;
	
	public RnsInfoSerializer() throws RnsReaderException {
		RnsReaderFactory factory = RnsReaderFactory.newInstance();
		this.monitor = factory.newRnsReader();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		this.rnsReader = (new ObjectFactory()).createRnsReaderType();
	}
	
	public RnsInfoSerializer(RnsReader monitor) {
		super();
		this.monitor = monitor;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		this.rnsReader = (new ObjectFactory()).createRnsReaderType();
	}

	
}
