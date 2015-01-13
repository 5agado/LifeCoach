package healthProfile.ws;

import healthProfile.model.HealthProfile;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project")
@SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL)
public interface HealthProfileServiceInterface {
	@WebMethod(operationName = "readPersonHealthProfile")
	public HealthProfile readPersonHealthProfile(int personId,
			String profileType);
}