package model;

import lifeCoach.LifeCoachLogic;
import lifeCoach.model.LifeCoachMeasure;
import lifeCoach.model.LifeCoachReport;
import lifeCoach.model.LifeCoachReportStatistics;
import healthProfile.model.HealthMeasure;
import healthProfile.model.HealthProfile;
import healthProfile.model.HealthProfileSuggestions;

import org.junit.Test;

import util.Serializer;

public class TestSerializer {
	@Test
	public void generateXSD() {
		Serializer.generateXSD(Goal.class, Measure.class,
				MeasureDefinition.class, Person.class, ListWrapper.class, HealthProfile.class, 
				HealthProfileSuggestions.class, HealthMeasure.class, LifeCoachMeasure.class,
				LifeCoachReport.class, LifeCoachReportStatistics.class);
	}
}
