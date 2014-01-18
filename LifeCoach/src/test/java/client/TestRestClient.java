package client;

import org.junit.Test;

public class TestRestClient {
	@Test
	public void testStand4Client() {
		Stands4Client client = new Stands4Client();
		String res = client.getRandomQuote();
		System.out.println(res);
	}
}
