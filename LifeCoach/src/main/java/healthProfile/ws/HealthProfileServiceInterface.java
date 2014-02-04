package healthProfile.ws;

import healthProfile.model.HealthProfile;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface HealthProfileServiceInterface {
	@WebMethod(operationName = "readPersonHealthProfile")
	public HealthProfile readPersonHealthProfile(int personId,
			String profileType);
}