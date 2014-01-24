package util;

import healthProfile.HealthProfileServicePublisher;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private final static Logger LOGGER = Logger.getLogger(Serializer.class.getName());
	
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
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
		}
		
	}
	
	public static <T> String marshalAsString(T obj) {
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			StringWriter sw = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(obj, sw);
			return sw.toString();
		} catch (JAXBException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
			return "";
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> objClass, String obj) {
		try {
			JAXBContext context = JAXBContext.newInstance(objClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader reader = new StringReader(obj);
			T res = (T) unmarshaller.unmarshal(reader);
			return res;
		} catch (JAXBException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
			return (T) new Object();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> objClass, Node node) {
		try {
			JAXBContext context = JAXBContext.newInstance(objClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			T res = (T) unmarshaller.unmarshal(node);
			return res;
		} catch (JAXBException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
			return (T) new Object();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> unmarshalWrapper(Class<T> objClass, String obj) {
		try {
			JAXBContext context = JAXBContext.newInstance(objClass, ListWrapper.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader reader = new StringReader(obj);
			StreamSource source = new StreamSource(reader);
			ListWrapper<T> wrapper = (ListWrapper<T>) unmarshaller.unmarshal(source, ListWrapper.class).getValue();
			return wrapper.getItems();
		} catch (JAXBException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
			return (List<T>) new ListWrapper<T>();
		}
    }
	
	public static <T> void generateXSD(Class<?>... objClasses) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(objClasses);
			SchemaOutputResolver sor = new MySchemaOutputResolver();
			jaxbContext.generateSchema(sor);
		} catch (JAXBException | IOException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
		}
	}
	
	//Helper class for output schema generation
	private static class MySchemaOutputResolver extends SchemaOutputResolver {
		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
	        File file = new File(suggestedFileName);
	        StreamResult result = new StreamResult(file);
	        result.setSystemId(file.toURI().toURL().toString());
	        return result;
	    }

	}
}
