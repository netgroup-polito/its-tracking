package it.polito.dp2.rest.rns.resources;
// This validator performs JAXB unmarshalling with validation
// against the schema
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

@Provider
@Consumes({"application/xml","text/xml"})
public class RnsValidator implements MessageBodyReader<JAXBElement<?>> {
	final String jaxbPackage = "it.polito.dp2.rest.rns.jaxb";
	Unmarshaller unmarshaller;
	Logger logger;

	public RnsValidator() {
		logger = Logger.getLogger(RnsValidator.class.getName());

		try {				
			InputStream schemaStream = RnsValidator.class.getResourceAsStream("/xsd/RnsInfo.xsd");
			if (schemaStream == null) {
				logger.log(Level.SEVERE, "xml schema file Not found.");
				throw new IOException();
			}
            JAXBContext jc = JAXBContext.newInstance( jaxbPackage );
            unmarshaller = jc.createUnmarshaller();
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new StreamSource(schemaStream));
            unmarshaller.setSchema(schema);
            
            logger.log(Level.INFO, "TelDirectoryProvider initialized successfully");
		} catch (SAXException | JAXBException | IOException se) {
			logger.log(Level.SEVERE, "Error parsing xml directory file. Service will not work properly.", se);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return JAXBElement.class.equals(type) || jaxbPackage.equals(type.getPackage().getName());
	}

	@Override
	public JAXBElement<?> readFrom(Class<JAXBElement<?>> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		try {
			return (JAXBElement<?>) unmarshaller.unmarshal(entityStream);
		} catch (JAXBException ex) {
			logger.log(Level.WARNING, "Request body validation error.", ex);
			BadRequestException bre = new BadRequestException("Request body validation error.");
			Response response = Response.fromResponse(bre.getResponse()).build();
			throw new BadRequestException("Request body validation error", response);
		}
	}

}
