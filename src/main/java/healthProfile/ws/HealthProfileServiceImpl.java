package healthProfile.ws;

import healthProfile.controller.HealthProfileServiceHelper;
import healthProfile.model.HealthProfile;

import javax.jws.WebService;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project",
		endpointInterface = "healthProfile.ws.HealthProfileServiceInterface", 
		serviceName = "HealthProfileService")
public class HealthProfileServiceImpl implements HealthProfileServiceInterface {
	HealthProfileServiceHelper helper;

	public HealthProfileServiceImpl() {
		helper = new HealthProfileServiceHelper();
	}

	@Override
	public HealthProfile readPersonHealthProfile(int personId,
			String profileType) {
		HealthProfile profile = helper.readPersonHealthProfile(personId,
				profileType);
		if (profile == null) {
			return new HealthProfile();
		}
		return profile;
	}
}
