package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}
