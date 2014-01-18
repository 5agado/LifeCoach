package model;

import org.junit.Test;

import util.Serializer;

public class TestSerializer {
	@Test
	public void generateXSD() {
		Serializer.generateXSD(Goal.class, Measure.class, MeasureDefinition.class, Person.class);
	}
}
