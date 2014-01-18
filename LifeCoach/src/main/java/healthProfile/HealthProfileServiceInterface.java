package healthProfile;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import model.Measure;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface HealthProfileServiceInterface {
	@WebMethod(operationName="readPersonHealthProfile")
	//TODO generalize for profile type
    public HealthProfile readPersonHealthProfile(@WebParam(name="personId") int personId, String profileType);
}