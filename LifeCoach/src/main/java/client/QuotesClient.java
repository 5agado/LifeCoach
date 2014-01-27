package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import util.XMLParser;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/*
 * STANDS4 APIs
 * http://www.abbreviations.com/api.php
 * 
 */
public class QuotesClient extends RESTClient {
	private final static Logger LOGGER = Logger.getLogger(QuotesClient.class
			.getName());
	private final static String STANDS4_URL = "http://www.stands4.com/services/v2/quotes.php";
	private final static String USER_ID_PARAM_NAME = "uid";
	private final static String USER_ID = "3163";
	private final static String TOKEN_ID_PARAM_NAME = "tokenid";
	private final static String TOKEN_ID = "bWAk5ZTzdcuYzYEC";
	private final static String SEARCH_TYPE_PARAM_NAME = "searchtype";
	
	private final static String THEY_SAID_SO_URL = "http://api.theysaidso.com/qod.xml";
	
	private final static String IHEART_URL = "http://www.iheartquotes.com/api/v1/random?format=xml";
	
	private final static String XML_QUOTE_ELEMENT = "quote";
	private final static String XML_AUTHOR_ELEMENT = "author";	
	
	public QuotesClient() {
		super("");
	}
	
	//Sample: http://www.stands4.com/services/v2/quotes.php?
	//uid=1001&tokenid=tk324324324&searchtype=RANDOM
	public String getRandomQuote() {
		final String SEARCH_TYPE = "RANDOM";
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl(); 
		queryParams.add(USER_ID_PARAM_NAME, USER_ID);
		queryParams.add(TOKEN_ID_PARAM_NAME, TOKEN_ID);
		queryParams.add(SEARCH_TYPE_PARAM_NAME, SEARCH_TYPE);
		ClientResponse response = executeGET(STANDS4_URL, queryParams);
		if (response.getStatus() == 200) {
			String quote = readQuote(response);
			return quote;
		}	
		
		response = executeGET(THEY_SAID_SO_URL, new MultivaluedMapImpl());
		if (response.getStatus() == 200) {
			String quote = readQuote(response);
			return quote;
		}	
		
		response = executeGET(IHEART_URL, new MultivaluedMapImpl());
		if (response.getStatus() == 200) {
			String quote = readQuote(response);
			return quote;
		}	
		
		return "";		
	}
	
	private String readQuote(ClientResponse response) {
		String res = response.getEntity(String.class);
		String quote = extractElement(res, XML_QUOTE_ELEMENT);
		String author = extractElement(res, XML_AUTHOR_ELEMENT);
		return "\"" + quote + "\" " + author;
	}
	
	private String extractElement(String response, String elementName) {
		InputStream input = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
		XMLParser adapter;
		try {
			adapter = new XMLParser(input);
			String value = adapter.getAttributeValue(".//" + elementName);
			return value;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOGGER.log(Level.WARNING, "XMLAdapter init error", e);
			return "";
		}
	}

}
