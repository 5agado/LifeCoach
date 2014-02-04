
package healthProfile.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "readPersonHealthProfileResponse", namespace = "http://ws.healthProfile/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readPersonHealthProfileResponse", namespace = "http://ws.healthProfile/")
public class ReadPersonHealthProfileResponse {

    @XmlElement(name = "return", namespace = "")
    private healthProfile.model.HealthProfile _return;

    /**
     * 
     * @return
     *     returns HealthProfile
     */
    public healthProfile.model.HealthProfile getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(healthProfile.model.HealthProfile _return) {
        this._return = _return;
    }

}
