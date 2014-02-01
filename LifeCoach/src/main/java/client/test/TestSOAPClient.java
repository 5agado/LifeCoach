package client.test;

import healthProfile.HealthProfileServicePublisher;
import healthProfile.model.HealthProfile;

import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import util.Serializer;
import client.HealthProfileClient;

public class TestSOAPClient {
	private final static Logger LOGGER = Logger
			.getLogger(TestSOAPClient.class.getName());
	private static final String SERVICE_URL = "http://localhost:5031/ws/healthProfile?wsdl";
	private static final String SERVICE_URI = "http://healthProfile/";
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
		
		LOGGER.log(Level.INFO, "Starting HealthProfile Service testing via HealthProfileClient\n"
				+ "\n [kill the process to exit]");
		
		scan = new Scanner(System.in);
		while (true){
			System.out.println("Testing readPersonHealthProfile");
			int personId = -1;
			
			do {
				System.out.println("Insert personId: ");
				String id = scan.nextLine();
				try {
					personId = Integer.valueOf(id);
				} catch (Exception e) {
					personId = -1;
				}
			} while (personId == -1);
		    
		    System.out.println("Insert profileType: ");
		    String profileType = scan.nextLine();
		    
		    String response = Serializer.marshalAsString(client.readPersonHealthProfile(personId, profileType));
		    System.out.println(response);
		}
	}
}
