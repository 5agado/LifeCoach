package healthProfile;

import healthProfile.model.HealthProfile;

import javax.jws.WebService;

@WebService(endpointInterface = "healthProfile.HealthProfileServiceInterface", serviceName = "HealthProfileService")
public class HealthProfileServiceImpl implements HealthProfileServiceInterface {
	HealthProfileServiceHelper helper;
	
	public HealthProfileServiceImpl() {
		helper = new HealthProfileServiceHelper();
	}

	@Override
	public HealthProfile readPersonHealthProfile(int personId, String profileType) {
		
		//TODO check person and profile existence
		
		HealthProfile profile = helper.readPersonHealthProfile(personId, profileType);
		return profile;
	}
}
