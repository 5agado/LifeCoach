package healthProfile;

import healthProfile.model.HealthMeasure;
import healthProfile.model.HealthProfile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import model.Measure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.Serializer;
import util.XMLAdapter;
import client.ProfileClient;

public class HealthProfileServiceHelper {
	private final static String WARNING_LOW = "*";
	private final static String WARNING_MEDIUM = "**";
	private final static String WARNING_HIGH = "***";
	private final static Logger LOGGER = Logger.getLogger(HealthProfileServiceHelper.class.getName());
	
	private ProfileClient client = new ProfileClient("http://localhost:8080/SDE_Final_Project/rest");
	private HealthProfileAdapter adapter;
	
	public HealthProfileServiceHelper() {
		client.setDebugEnabled(true);
	}
	
	public HealthProfile readPersonHealthProfile(int personId, String profileType) {		
		HealthProfile profile = new HealthProfile();
		
		//TODO expose adapter as a service
		try {
			adapter = new HealthProfileAdapter(profileType, personId);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "No such profile type or person ID", e);
			return profile;
		} catch (SAXException e) {
			LOGGER.log(Level.WARNING, "SAXException", e);
			return profile;
		} catch (ParserConfigurationException e) {
			LOGGER.log(Level.WARNING, "ParserConfigurationException", e);
			return profile;
		}
		
		if (!adapter.isCurrentSourceUpToDate()){
			updateRemoteMeasures(personId);
		}
		
		//TODO management of wrong response (null)
		List<Measure> remoteMeasures = client.readProfile(personId, profileType);
		
		List<HealthMeasure> healthMeasures = computeAndGetMeasuresFrom(remoteMeasures);
		
		if (healthMeasures.isEmpty()){
			return profile;
		}
		
		profile.setMeasures(healthMeasures);
		
		String suggestions = getSuggestionForProfile(profile);
		
		profile.setSuggestions(suggestions);
		
		profile.setTimestamp(new Date());
		
		return profile;
	}
	
	private void updateRemoteMeasures(int personId){
		//Measure without measureDef id
		List<Measure> newMeasures = adapter.readMeasures();
		for (Measure m : newMeasures){
			Date date = Calendar.getInstance().getTime();
			m.setTimestamp(date);
			client.createMeasure(m, personId);
		}
		adapter.setUpToDate();
	}
	
	private List<HealthMeasure> computeAndGetMeasuresFrom(List<Measure> remoteMeasureList){
		List<HealthMeasure> measures = new ArrayList<HealthMeasure>();
		for (Measure m : remoteMeasureList){
			HealthMeasure hm = new HealthMeasure();
			hm.setValue(m.getValue());
			hm.setMeasureName(m.getMeasureDefinition().getMeasureName());
			hm.setRefLevel(adapter.readReferenceLevelFor(m.getMeasureDefinition().getMeasureName()));
			computeAndSetWarningLevelFor(hm);
			measures.add(hm);
		}
		
		return measures;
	}
	
	//TODO perfectionate the following methods
	private void computeAndSetWarningLevelFor(HealthMeasure measure){		
		if (measure.getRefLevel() == null){
			measure.setWarning(null);
			return;
		}
		
		String[] minMax = measure.getRefLevel().split("-");
		double min;
		double max;
		try {
			min = Double.valueOf(minMax[0]);
			max = Double.valueOf(minMax[1]);
		} catch (NumberFormatException e) {
			LOGGER.log(Level.WARNING, e.getLocalizedMessage());
			measure.setWarning(null);
			return;
		}
		
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
		StringBuilder suggestions = new StringBuilder();
		
		int profileIlness = 0;
		int numMeasures = 0;
		List<HealthMeasure> measures = profile.getMeasures();
		for (HealthMeasure m : measures){
			String wLevel = m.getWarning();
			if (wLevel == null){
				continue;
			}
			numMeasures++;
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
		
		profileIlness = profileIlness/numMeasures;
		suggestions.append("Profile illness: " + profileIlness);
		return suggestions.toString();
	}
}
