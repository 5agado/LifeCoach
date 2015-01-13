package healthProfile;

import healthProfile.ws.HealthProfileServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import resources.ws.GoalResourceImpl;
import resources.ws.MeasureDefinitionResourceImp;
import resources.ws.MeasureResourceImpl;
import resources.ws.PersonResourceImpl;
import resources.ws.ProfileResourceImpl;

public class HealthProfileServicePublisher {
	private final static Logger LOGGER = Logger
			.getLogger(HealthProfileServicePublisher.class.getName());
	public static String SERVER_URL = "http://localhost";
	public static String PORT = "5031";
	public static String BASE_URL = "/ws/healthProfile";

	public static String getEndpointURL() {
		return SERVER_URL + ":" + PORT + BASE_URL;
	}

	public static void main(String[] args) {
		String endpointUrl = getEndpointURL();
		LOGGER.log(Level.INFO, "Starting HealthProfile Service");
		LOGGER.log(Level.INFO, "Published at = " + endpointUrl);
		Endpoint.publish(endpointUrl, new HealthProfileServiceImpl());
		Endpoint.publish(endpointUrl + "/person", new PersonResourceImpl());
		Endpoint.publish(endpointUrl + "/measureDefinition", new MeasureDefinitionResourceImp());
		Endpoint.publish(endpointUrl + "/measure", new MeasureResourceImpl());
		Endpoint.publish(endpointUrl + "/goal", new GoalResourceImpl());
		Endpoint.publish(endpointUrl + "/profile", new ProfileResourceImpl());
	}
}
