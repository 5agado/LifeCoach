package client;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

/*
 * STANDS4 APIs
 * http://www.abbreviations.com/api.php
 * 
 */
public class QuotesClient extends RESTClient {
	private final static String STANDS4_URL = "http://www.stands4.com/services/v2/quotes.php";
	private final static String USER_ID_PARAM_NAME = "uid";
	private final static String USER_ID = "3163";
	private final static String TOKEN_ID_PARAM_NAME = "tokenid";
	private final static String TOKEN_ID = "bWAk5ZTzdcuYzYEC";
	private final static String SEARCH_TYPE_PARAM_NAME = "searchtype";
	
	private final static String THEY_SAID_SO_URL = "http://api.theysaidso.com/qod.xml";
	
	private final static String IHEART_URL = "http://www.iheartquotes.com/api/v1/random?format=json";
	
	public QuotesClient() {
		//TODO add possibility of other sources
		super(STANDS4_URL);
	}
	
	//Sample: http://www.stands4.com/services/v2/quotes.php?
	//uid=1001&tokenid=tk324324324&searchtype=RANDOM
	public String getRandomQuote() {
		final String SEARCH_TYPE = "RANDOM";
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl(); 
		queryParams.add(USER_ID_PARAM_NAME, USER_ID);
		queryParams.add(TOKEN_ID_PARAM_NAME, TOKEN_ID);
		queryParams.add(SEARCH_TYPE_PARAM_NAME, SEARCH_TYPE);
		String res = executeGET("", queryParams);
		return res;
	}

}
