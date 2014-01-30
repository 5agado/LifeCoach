package client;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import util.Serializer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RESTClient {
	private final static Logger LOGGER = Logger.getLogger(RESTClient.class
			.getName());
	private URI serverUri;
	private WebResource webResource;

	public RESTClient(String requestUrl) {
		configureClient(requestUrl);
	}

	private void configureClient(String requestUrl) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		serverUri = getBaseURI(requestUrl);
		this.webResource = client.resource(serverUri);
	}

	protected ClientResponse executeGET(String getPath,
			MultivaluedMap<String, String> queryParams) {
		if (queryParams == null) {
			queryParams = new MultivaluedMapImpl();
		}
		
		LOGGER.log(Level.INFO, "Executing GET to: " + serverUri.toString()
			+ getPath + "?" + queryParams.toString());

		ClientResponse response = webResource.queryParams(queryParams)
				.path(getPath).accept(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		return response;
	}

	protected ClientResponse executePOST(String postPath) {
		LOGGER.log(Level.INFO, "Executing POST to: " + serverUri.toString()
				+ postPath);

		ClientResponse response = webResource.path(postPath)
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.post(ClientResponse.class);

		return response;
	}

	protected ClientResponse executePOSTWithRequestEntity(String postPath,
			Object reqEntity) {
		LOGGER.log(Level.INFO, "Executing POST to: " + serverUri.toString()
					+ postPath);
		LOGGER.log(Level.INFO, "Request entity: " + reqEntity.toString());
			System.out.println();

		ClientResponse response = webResource.path(postPath)
				.type(MediaType.APPLICATION_XML_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, reqEntity);

		return response;
	}

	protected ClientResponse executePUT(String putPath) {
		LOGGER.log(Level.INFO, "Executing PUT to: " + serverUri.toString()
					+ putPath);			

		ClientResponse response = webResource.path(putPath)
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.put(ClientResponse.class);

		return response;
	}

	protected ClientResponse executeDELETE(String deletePath) {
		LOGGER.log(Level.INFO, "Executing DELETE to: " + serverUri.toString()
				+ deletePath);
		ClientResponse response = webResource.path(deletePath).delete(
				ClientResponse.class);

		return response;
	}

	protected <T> T extractEntity(ClientResponse response, Class<T> objClass) {
		if (response.getStatus() != 200) {
			LOGGER.log(Level.INFO, "Http error code = " + response.getStatus());
			return null;
		}

		String res = response.getEntity(String.class);
		T entity = Serializer.unmarshal(objClass, res);
		return entity;
	}

	protected <T> List<T> extractEntityWrapper(ClientResponse response,
			Class<T> objClass) {
		if (response.getStatus() != 200) {
			LOGGER.log(Level.INFO, "Http error code = " + response.getStatus());
			return null;
		}

		String res = response.getEntity(String.class);
		List<T> entityWrapper = Serializer.unmarshalWrapper(objClass, res);
		return entityWrapper;
	}

	private static URI getBaseURI(String requestUrl) {
		return UriBuilder.fromUri(requestUrl).build();
	}

}
