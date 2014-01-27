package healthProfile;

import healthProfile.model.HealthProfile;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface HealthProfileServiceInterface {
	@WebMethod(operationName="readPersonHealthProfile")
    public HealthProfile readPersonHealthProfile(@WebParam(name="personId") int personId, @WebParam(name="profileType") String profileType);
}