package adapters;

import static org.junit.Assert.assertNotNull;
import healthProfile.HealthProfileAdapter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Measure;

import org.junit.Test;

import util.XMLAdapter;

public class TestHealthProfileAdapter {
	HealthProfileAdapter adapter;
	
	public TestHealthProfileAdapter() throws IOException {
		try {
			adapter = new HealthProfileAdapter("blood", 2);
		} catch (IOException e) {
			System.out.println("No such profile type or person ID" + e.getLocalizedMessage());
			assertNotNull("No such profile type or person ID", null);
		}
	}
	
	@Test
	public void readHealthMeasures() {
		List<Measure> list = adapter.readMeasures();
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
	public void readLevels() {
		String level = adapter.readReferenceLevelAsString("leucociti");
		System.out.println(level);
	}
	
	@Test
	public void checkIfUpToDate() {
		System.out.println(adapter.isCurrentSourceUpToDate());
	}
	
	@Test
	public void setUpToDate() {
		adapter.setUpToDate();
	}
}
