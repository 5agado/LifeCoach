package adapters;

import healthProfile.HealthProfileAdapter;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Measure;

import org.junit.Test;

import util.XMLAdapter;

public class TestXMLAdapter {
	@Test
	public void readHealthMeasures() {
		HealthProfileAdapter reader = new HealthProfileAdapter("src/main/resources/Blood.xml");
		List<Measure> list = reader.readMeasures();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Measure.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			for (Measure m : list){
				marshaller.marshal(m, System.out);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void checkIfUpToDate() {
		HealthProfileAdapter reader = new HealthProfileAdapter("src/main/resources/Blood.xml");
		System.out.println(reader.isCurrentSourceUpToDate());
	}
	
	@Test
	public void setUpToDate() {
		HealthProfileAdapter reader = new HealthProfileAdapter("src/main/resources/Blood.xml");
		reader.setUpToDate();
	}
}
