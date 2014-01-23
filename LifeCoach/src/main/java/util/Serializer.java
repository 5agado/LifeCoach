package util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import model.ListWrapper;

import org.w3c.dom.Node;

public class Serializer {
	private Serializer() {
		throw new AssertionError();
	}
	
	public static <T> void marshal(T obj, File out) {
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(obj, out);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static <T> String marshalString(T obj) {
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			StringWriter sw = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(obj, sw);
			return sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static <T> T unmarshal(Class<T> objClass, Reader reader) {
		try {
			JAXBContext context = JAXBContext.newInstance(objClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			T res = (T) unmarshaller.unmarshal(reader);
			return res;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T unmarshal(Class<T> objClass, Node node) {
		try {
			JAXBContext context = JAXBContext.newInstance(objClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			T res = (T) unmarshaller.unmarshal(node);
			return res;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> List<T> unmarshalWrapper(Class<T> objClass, Reader reader) {
		try {
			JAXBContext context = JAXBContext.newInstance(objClass, ListWrapper.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StreamSource source = new StreamSource(reader);
			ListWrapper<T> wrapper = (ListWrapper<T>) unmarshaller.unmarshal(source, ListWrapper.class).getValue();
			return wrapper.getItems();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	
	public static <T> void generateXSD(Class<?>... objClasses) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(objClasses);
			SchemaOutputResolver sor = new MySchemaOutputResolver();
			jaxbContext.generateSchema(sor);
			sor.createOutput("src/main/resources", "tmp.xsd");
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class MySchemaOutputResolver extends SchemaOutputResolver {
		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
	        File file = new File(suggestedFileName);
	        StreamResult result = new StreamResult(file);
	        result.setSystemId(file.toURI().toURL().toString());
	        return result;
	    }

	}
}
