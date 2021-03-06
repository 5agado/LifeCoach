package client.test;

import healthProfile.model.HealthProfile;

import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import util.Serializer;
import client.HealthProfileClient;

public class TestSOAPClient {
	private final static Logger LOGGER = Logger.getLogger(TestSOAPClient.class
			.getName());
	private static final String SERVICE_URL = "http://localhost:5031/ws/healthProfile?wsdl";
	private static final String SERVICE_URI = "http://ws.healthProfile/";
	private static final String SERVICE_NAME = "HealthProfileService";
	private static Scanner scan;

	public static void main(String[] args) {
		HealthProfileClient client = null;
		try {
			QName qName = new QName(SERVICE_URI, SERVICE_NAME);
			java.net.URL url = new java.net.URL(SERVICE_URL);
			client = new HealthProfileClient(url, qName);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, "Malformed URL", e);
			System.exit(1);
		}

		LOGGER.log(Level.INFO,
				"Starting HealthProfile Service testing via HealthProfileClient\n"
						+ "\n [kill the process to exit]");

		scan = new Scanner(System.in);
		while (true) {
			System.out.println("\nTesting readPersonHealthProfile");
			int personId = -1;

			do {
				System.out.print("Insert personId: ");
				String id = scan.nextLine();
				try {
					personId = Integer.valueOf(id);
				} catch (Exception e) {
					personId = -1;
				}
			} while (personId == -1);

			System.out.print("Insert profileType: ");
			String profileType = scan.nextLine();

			System.out.println("\nRESPONSE: ");
			HealthProfile hp = client.readPersonHealthProfile(personId,
					profileType);
			String response = hp == null ? "No profile found" : Serializer
					.marshalAsString(hp);
			System.out.println(response);
		}
	}
}
