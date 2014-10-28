package client;

import org.junit.Test;

public class TestRestClient {
	@Test
	public void testQuotesClient() {
		QuotesClient client = new QuotesClient();
		String res = client.getRandomQuote();
		System.out.println(res);
	}
}
