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

	public ClientResponse executeGET(String getPath,
			MultivaluedMap<String, String> queryParams) {
		if (queryParams == null) {
			queryParams = new MultivaluedMapImpl();
		}

		LOGGER.log(Level.FINE, "Executing GET to: " + serverUri.toString()
				+ getPath + "?" + queryParams.toString());

		ClientResponse response = webResource.queryParams(queryParams)
				.path(getPath).accept(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		return response;
	}

	public ClientResponse executePOST(String postPath, Object reqEntity) {
		LOGGER.log(Level.FINE, "Executing POST to: " + serverUri.toString()
				+ postPath);
		LOGGER.log(Level.FINE, "Request entity: " + reqEntity.toString());

		ClientResponse response = webResource.path(postPath)
				.type(MediaType.APPLICATION_XML_TYPE)
				.post(ClientResponse.class, reqEntity);

		return response;
	}

	public ClientResponse executePUT(String putPath, Object reqEntity) {
		LOGGER.log(Level.FINE, "Executing PUT to: " + serverUri.toString()
				+ putPath);
		LOGGER.log(Level.FINE, "Request entity: " + reqEntity.toString());

		ClientResponse response = webResource.path(putPath)
				.type(MediaType.APPLICATION_XML_TYPE)
				.put(ClientResponse.class, reqEntity);

		return response;
	}

	public ClientResponse executeDELETE(String deletePath) {
		LOGGER.log(Level.FINE, "Executing DELETE to: " + serverUri.toString()
				+ deletePath);
		ClientResponse response = webResource.path(deletePath).delete(
				ClientResponse.class);

		return response;
	}

	protected <T> T extractEntity(ClientResponse response, Class<T> objClass) {
		if (response.getStatus() != 200) {
			LOGGER.log(Level.WARNING, "Http error code = " + response.getStatus());
			return null;
		}

		String res = response.getEntity(String.class);
		T entity = Serializer.unmarshal(objClass, res);
		return entity;
	}

	protected <T> List<T> extractEntityWrapper(ClientResponse response,
			Class<T> objClass) {
		if (response.getStatus() != 200) {
			LOGGER.log(Level.WARNING, "Http error code = " + response.getStatus());
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
