package healthProfile;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

public class HealthProfileServicePublisher {
	private final static Logger LOGGER = Logger.getLogger(HealthProfileServicePublisher.class.getName());
	public static String SERVER_URL = "http://localhost";
	public static String PORT = "8081";
	public static String BASE_URL = "/ws/healthProfile";
	
	public static String getEndpointURL() {
		return SERVER_URL + ":" + PORT + BASE_URL;
	}
 
	public static void main(String[] args) {
		String endpointUrl = getEndpointURL();
		LOGGER.log(Level.INFO, "Starting HealthProfile Service");
		LOGGER.log(Level.INFO, "Published at = " + endpointUrl);
		Endpoint.publish(endpointUrl, new HealthProfileServiceImpl());
    }
}
