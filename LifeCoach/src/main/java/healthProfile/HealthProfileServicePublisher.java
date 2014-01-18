package healthProfile;

import javax.xml.ws.Endpoint;

public class HealthProfileServicePublisher {
	public static String SERVER_URL = "http://localhost";
	public static String PORT = "8081";
	public static String BASE_URL = "/ws/healthProfile";
	
	public static String getEndpointURL() {
		return SERVER_URL + ":" + PORT + BASE_URL;
	}
 
	public static void main(String[] args) {
		String endpointUrl = getEndpointURL();
		System.out.println("Starting HealthProfile Service...");
		System.out.println("--> Published at = " + endpointUrl);
		Endpoint.publish(endpointUrl, new HealthProfileServiceImpl());
    }
}
