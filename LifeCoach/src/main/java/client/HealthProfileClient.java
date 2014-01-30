package client;

import healthProfile.HealthProfileServiceInterface;
import healthProfile.model.HealthProfile;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class HealthProfileClient {
	private Service service;
	private HealthProfileServiceInterface serviceInterface;

	public HealthProfileClient(URL url, QName serviceQName) {
		service = Service.create(url, serviceQName);
		serviceInterface = service.getPort(HealthProfileServiceInterface.class);
	}

	public HealthProfile readPersonHealthProfile(int personId,
			String profileType) {
		HealthProfile profile = serviceInterface.readPersonHealthProfile(
				personId, profileType);
		return profile;
	}
}
