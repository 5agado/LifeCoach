package client.test;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import util.Serializer;
import client.RESTClient;

public class TestRESTClient {
	private final static Logger LOGGER = Logger
			.getLogger(TestRESTClient.class.getName());
	private static final String SERVICE_URL = "http://localhost:8080/SDE_Final_Project/rest/"; 
			//"http://localhost:5030/";
	private static Scanner scan;
	
	public static void main(String[] args) {
		RESTClient client = new RESTClient(SERVICE_URL);
		
		LOGGER.log(Level.INFO, "Starting REST services testing via RESTClient\n"
				+ "\n [kill the process to exit]");
		
		scan = new Scanner(System.in);
		while (true){
			String method;
			boolean validMethod = false;
			
			do {
				System.out.println("Insert method type: ");
				method = scan.nextLine().trim();
				if (method.equals("POST") || method.equals("PUT") || 
						method.equals("GET") || method.equals("DELETE")){
					validMethod = true;
				}
			} while (!validMethod);
			
			System.out.println("\nInsert endpoint: ");
		    String endpoint = scan.nextLine();
		    ClientResponse response = null;
		    String entity = "";
			String nextLine = "";
		    
			switch (method) {
			case "GET":
				response = client.executeGET(endpoint, new MultivaluedMapImpl());
				break;
			case "POST":
				System.out.println("\nInsert body entity followed by the <DONE> keyword: ");
				while (!(nextLine = scan.nextLine()).trim().equals("DONE")){
					entity = entity + nextLine;
				}
				response = client.executePOST(endpoint, entity);
				break;
			case "PUT":
				System.out.println("\nInsert body entity followed by the <DONE> keyword: ");
				while (!(nextLine = scan.nextLine()).trim().equals("DONE")){
					entity = entity + nextLine;
				}
				response = client.executePUT(endpoint, entity);
				break;
			case "DELETE":
				response = client.executeDELETE(endpoint);
				break;
			default:
				LOGGER.log(Level.SEVERE, "Invalid method");
				break;
			}
		    
			System.out.println("\nRESPONSE:");
			if (response.getStatus() != 200) {
				System.out.println("Http error code = " + response.getStatus());
			}
			else {
				System.out.println("Http status = 200");
				System.out.println(response.getEntity(String.class));
			}
			
			System.out.println("\n");
		}
	}
}
