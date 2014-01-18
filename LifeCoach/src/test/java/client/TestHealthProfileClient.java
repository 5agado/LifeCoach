package client;

import healthProfile.HealthProfile;

import java.net.MalformedURLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.junit.Test;

public class TestHealthProfileClient {
	private static final String SERVICE_URL = "http://localhost:8081/ws/healthProfile?wsdl";
	private static final String SERVICE_URI = "http://healthProfile/";
	private static final String SERVICE_NAME = "HealthProfileService";
	// http://test.lifeparticipation.org/introsde2013/SERVER_PORT/
	
	@Test
	public void testHealthProfileClient() {
		HealthProfileClient client = null;
		try {
			QName qName = new QName(SERVICE_URI, SERVICE_NAME);
			java.net.URL url = new java.net.URL(SERVICE_URL);
			client = new HealthProfileClient(url, qName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Starting server testing via HealthProfileClient\n");
		System.out.print("\ngetAndOutputPerson: ");
		HealthProfile profile = client.readPersonHealthProfile(1, "blood");
		if (profile == null){
			System.out.println("null");
		}
		else {
			outputHealthProfile(profile);
		}
	}
	
	private static void outputHealthProfile(HealthProfile profile) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(HealthProfile.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(profile, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
