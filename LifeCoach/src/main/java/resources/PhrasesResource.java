package resources;

import java.io.IOException;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.Serializer;
import util.XMLParser;
import client.QuotesClient;

@Path("/phrases/")
public class PhrasesResource {
	private QuotesClient quotesClient = new QuotesClient();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("quote")
	public String readQuote() {
		String quote = quotesClient.getRandomQuote();
		return quote;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("motivational")
	public String readMotivationalPhrase(
			@QueryParam("personName") String personName) {
		Random r = new Random();
		XMLParser parser = null;
		try {
			parser = new XMLParser(
					"src/main/resources/motivationalPhrases.xml");
		} catch (IOException | SAXException | ParserConfigurationException e) {
			System.out.println("No motivational" + e.getMessage());
			return "";
		}
		NodeList nodeList = parser.readNodeList("/phrases/phrase");
		Node randomNode = nodeList.item(r.nextInt(nodeList.getLength()));
		XMLPhrase xmlphrase = Serializer.unmarshal(XMLPhrase.class, randomNode);
		if (xmlphrase == null) {
			return "";
		}

		String phrase = xmlphrase.getContent();

		if (personName == null || personName.isEmpty()) {
			return phrase;
		}

		return personName + ", " + phrase.substring(0, 1).toLowerCase()
				+ phrase.substring(1);
	}

	@XmlRootElement(name = "phrase")
	protected static class XMLPhrase {
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
