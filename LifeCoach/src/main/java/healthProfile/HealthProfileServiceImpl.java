package healthProfile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.Serializer;
import util.XMLAdapter;
import client.ProfileClient;
import model.Measure;

@WebService(endpointInterface = "healthProfile.HealthProfileServiceInterface", serviceName = "HealthProfileService")
public class HealthProfileServiceImpl implements HealthProfileServiceInterface {
	private String profileType;
	private ProfileClient client = new ProfileClient("http://localhost:8080/SDE_Final_Project/rest");
	private HealthProfileAdapter adapter;
	
	public HealthProfileServiceImpl() {
		client.setDebugEnabled(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HealthProfile readPersonHealthProfile(int personId, String profileType) {
		this.profileType = profileType;
		
		//TODO expose adapter as a service
		//TODO define good way for the source name to be passed + personId
		adapter = new HealthProfileAdapter("src/main/resources/BloodProfile_01.xml");
		
		if (!adapter.isCurrentSourceUpToDate()){
			updateRemoteMeasures(personId);
		}
		
		List<Measure> stdMeasures = readRemoteProfile(personId);
		
		List<HealthMeasure> healthMeasures = computeAndGetMeasuresFrom(stdMeasures);
		
		HealthProfile profile = new HealthProfile();
		profile.setMeasures(healthMeasures);
		
		String suggestions = getSuggestionForProfile(profile);
		
		profile.setSuggestions(suggestions);
		
		return profile;
	}
	
	//TODO tmp usage of personId
	private void updateRemoteMeasures(int personId){
		//Measure without measureDef id
		List<Measure> newMeasures = adapter.readMeasures();
		for (Measure m : newMeasures){
			//TODO check better option like Joda
			Date date = Calendar.getInstance().getTime();
			m.setTimestamp(date);
			client.createMeasure(m, personId);
		}
		adapter.setUpToDate();
	}
	
	private List<Measure> readRemoteProfile(int personId){
		String remoteProfile = client.readProfile(personId, profileType);
		InputStream profileIO = new ByteArrayInputStream(remoteProfile.getBytes(StandardCharsets.UTF_8));
		XMLAdapter profileAdapter = new XMLAdapter(profileIO);
		NodeList nodeList = profileAdapter.readNodeList("/measures/measure");
		List<Measure> stdMeasures = new ArrayList<Measure>();
		for (int i=0; i<nodeList.getLength(); i++){
			Node currentNode = nodeList.item(i);	
			Measure measure = (Measure) Serializer.unmarshal(Measure.class, currentNode);
			stdMeasures.add(measure);
		}
		return stdMeasures;
	}
	
	private List<HealthMeasure> computeAndGetMeasuresFrom(List<Measure> remoteMeasureList){
		List<HealthMeasure> measures = new ArrayList<HealthMeasure>();
		for (Measure m : remoteMeasureList){
			HealthMeasure hm = new HealthMeasure();
			hm.setValue(m.getValue());
			hm.setMeasureName(m.getMeasureDefinition().getMeasureName());
			hm.setRefLevel(adapter.readReferenceLevelAsString(m.getMeasureDefinition().getMeasureName()));
			computeAndSetWarningLevelFor(hm);
			measures.add(hm);
		}
		
		return measures;
	}
	
	private void computeAndSetWarningLevelFor(HealthMeasure measure){
		final String WARNING_LOW = "*";
		final String WARNING_MEDIUM = "**";
		final String WARNING_HIGH = "***";
		String[] minMax = measure.getRefLevel().split("-");
		double min = Double.valueOf(minMax[0]);
		double max = Double.valueOf(minMax[1]);
		double range = max - min;
		double diff = 0;
		double value = Double.valueOf(measure.getValue());
		if (value < min){
			diff = Math.abs(min - value);
		}
		else if (value > max){
			diff = Math.abs(value - max);
		} 
		
		if (diff == 0){
			measure.setWarning("");
		}
		else if (diff < (range/2)){
			measure.setWarning(WARNING_LOW);
		}
		else if (diff < (range)){
			measure.setWarning(WARNING_MEDIUM);
		}
		else if (diff > (range)){
			measure.setWarning(WARNING_HIGH);
		}
	}
	
	private String getSuggestionForProfile (HealthProfile profile){
		//TODO repeated
		final String WARNING_LOW = "*";
		final String WARNING_MEDIUM = "**";
		final String WARNING_HIGH = "***";
		StringBuilder suggestions = new StringBuilder();
		
		int profileIlness = 0;
		List<HealthMeasure> measures = profile.getMeasures();
		for (HealthMeasure m : measures){
			String wLevel = m.getWarning();
			switch (wLevel) {
			case WARNING_LOW:
				profileIlness += 1;
				suggestions.append(adapter.readAdviceFor(m.getMeasureName()));
				break;
			case WARNING_MEDIUM:
				profileIlness += 2;
				suggestions.append(adapter.readAdviceFor(m.getMeasureName()));
				break;
			case WARNING_HIGH:
				profileIlness += 4;
				suggestions.append(adapter.readAdviceFor(m.getMeasureName()));
				break;
			default:
				break;
			}
		}
		
		profileIlness = profileIlness/measures.size();
		suggestions.append("Profile illness: " + profileIlness);
		return suggestions.toString();
	}
}
