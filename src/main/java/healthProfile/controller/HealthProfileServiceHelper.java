package healthProfile.controller;

import healthProfile.model.HealthMeasure;
import healthProfile.model.HealthProfile;
import healthProfile.model.HealthProfileSuggestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import model.Measure;
import model.Person;

import org.xml.sax.SAXException;

import client.ResourcesClient;

public class HealthProfileServiceHelper {
	private final static String WARNING_ZERO = "";
	private final static String WARNING_LOW = "*";
	private final static String WARNING_MEDIUM = "**";
	private final static String WARNING_HIGH = "***";
	private final static String CONTACT_DOCTOR_MSG = "Si consiglia di rivolgersi al "
			+ "proprio medico di famiglia al più presto";
	private final static double ILLNESS_WARNING_LOW = 1.0;
	private final static double ILLNESS_WARNING_MEDIUM = 2.0;
	private final static double ILLNESS_WARNING_HIGH = 4.0;
	private final static Logger LOGGER = Logger
			.getLogger(HealthProfileServiceHelper.class.getName());

	private ResourcesClient client = new ResourcesClient();
	private HealthProfileAdapter adapter;

	public HealthProfileServiceHelper() {
	}

	public HealthProfile readPersonHealthProfile(int personId,
			String profileType) {
		HealthProfile profile = new HealthProfile();

		if (profileType == null || profileType.isEmpty()) {
			return null;
		}

		Person person = client.readPerson(personId);
		if (person == null) {
			return null;
		}

		profile.setPerson(person);
		profile.setTimestamp(new Date());

		try {
			adapter = new HealthProfileAdapter(profileType, personId);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "No such profile type or person ID",
					e.getLocalizedMessage());
			return profile;
		} catch (SAXException e) {
			LOGGER.log(Level.WARNING, "SAXException", e);
			return profile;
		} catch (ParserConfigurationException e) {
			LOGGER.log(Level.WARNING, "ParserConfigurationException", e);
			return profile;
		}

		if (!adapter.isCurrentSourceUpToDate()) {
			updateRemoteMeasures(personId);
		}

		List<Measure> remoteMeasures = client.readProfileMeasures(personId,
				profileType);

		if (remoteMeasures == null || remoteMeasures.isEmpty()) {
			return profile;
		}

		List<HealthMeasure> healthMeasures = computeAndGetMeasuresFrom(remoteMeasures);

		profile.setMeasures(healthMeasures);

		HealthProfileSuggestions suggestions = getSuggestionForProfile(profile);

		profile.setSuggestions(suggestions);

		return profile;
	}

	private void updateRemoteMeasures(int personId) {
		// Measure without measureDef id
		List<Measure> newMeasures = adapter.readMeasures();
		for (Measure m : newMeasures) {
			Date date = Calendar.getInstance().getTime();
			m.setTimestamp(date);
			client.createMeasure(m, personId);
		}
		adapter.setUpToDate();
	}

	private List<HealthMeasure> computeAndGetMeasuresFrom(
			List<Measure> remoteMeasureList) {
		List<HealthMeasure> measures = new ArrayList<HealthMeasure>();
		for (Measure m : remoteMeasureList) {
			HealthMeasure hm = new HealthMeasure();
			hm.setValue(m.getValue());
			hm.setMeasureName(m.getMeasureDefinition().getMeasureName());
			hm.setRefLevel(adapter.readReferenceLevelFor(m
					.getMeasureDefinition().getMeasureName()));
			computeAndSetWarningLevelFor(hm);
			measures.add(hm);
		}

		return measures;
	}

	private void computeAndSetWarningLevelFor(HealthMeasure measure) {
		if (measure.getRefLevel().isEmpty()) {
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
		double measureValue = Double.valueOf(measure.getValue());
		if (measureValue < min) {
			diff = Math.abs(min - measureValue);
		} else if (measureValue > max) {
			diff = Math.abs(measureValue - max);
		}

		if (diff == 0) {
			measure.setWarning("");
		} else if (diff < (range / 2)) {
			measure.setWarning(WARNING_LOW);
		} else if (diff < (range)) {
			measure.setWarning(WARNING_MEDIUM);
		} else if (diff > (range)) {
			measure.setWarning(WARNING_HIGH);
		} else {
			LOGGER.log(Level.INFO, "Missed warning level option");
		}
	}

	private HealthProfileSuggestions getSuggestionForProfile(
			HealthProfile profile) {
		HealthProfileSuggestions suggestions = new HealthProfileSuggestions();
		List<String> advice = new ArrayList<String>();

		int numWL = 0, numWM = 0, numWH = 0;
		double profileIllness = 0.0;
		int numMeasures = 0;
		List<HealthMeasure> measures = profile.getMeasures();
		for (HealthMeasure m : measures) {
			String wLevel = m.getWarning();
			if (wLevel == null) {
				continue;
			}
			numMeasures++;
			switch (wLevel) {
			case WARNING_ZERO:
				break;
			case WARNING_LOW:
				profileIllness += ILLNESS_WARNING_LOW;
				advice.add(adapter.readAdviceFor(m.getMeasureName()));
				numWL++;
				break;
			case WARNING_MEDIUM:
				profileIllness += ILLNESS_WARNING_MEDIUM;
				advice.add(adapter.readAdviceFor(m.getMeasureName()));
				numWM++;
				break;
			case WARNING_HIGH:
				profileIllness += ILLNESS_WARNING_HIGH;
				advice.add(adapter.readAdviceFor(m.getMeasureName()));
				numWH++;
				break;
			default:
				LOGGER.log(Level.INFO, "Undefined Warning level for suggestion");
				break;
			}
		}

		profileIllness = (profileIllness / (numMeasures * ILLNESS_WARNING_HIGH)) * 100.0;
		suggestions.setNumMeasures(measures.size());
		suggestions.setNumDetailedMeasures(numMeasures);
		suggestions.setLowW(numWL);
		suggestions.setMediumW(numWM);
		suggestions.setHighW(numWH);
		suggestions
				.setIllnessLevel(String.format("%.2f", profileIllness) + "%");

		if (profileIllness > 80.0) {
			advice.add(CONTACT_DOCTOR_MSG);
		}

		suggestions.setAdvice(advice);
		return suggestions;
	}
}
