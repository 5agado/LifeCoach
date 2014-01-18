package client;

import java.io.File;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RESTClient {
	private final URI serverUri;
	private WebResource webResource;
	private Boolean debugRequests = false;
	
	public RESTClient(String requestUrl) {
		ClientConfig config = new DefaultClientConfig();
	    Client client = Client.create(config);
	    serverUri = getBaseURI(requestUrl);
	    this.webResource = client.resource(serverUri);
	}
	
	protected String executeGET(String getPath, MultivaluedMap<String, String> queryParams){
		if (debugRequests){
			System.out.println("Executing GET to: " + serverUri.toString() + getPath + "?" + queryParams.toString());
		}
		
		ClientResponse response = webResource.queryParams(queryParams).path(getPath).accept(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
		String res = "Failed : HTTP error code : " + response.getStatus();

		if (response.getStatus() == 200) {
			res = response.getEntity(String.class);
		}

		return res;
	}
	
	protected String executePOST(String postPath){
		if (debugRequests){
			System.out.println("Executing POST to: " + serverUri.toString() + postPath);
		}
		
		ClientResponse response = webResource.path(postPath).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class);
		
		String res = "Failed : HTTP error code : " + response.getStatus();
		
		if (response.getStatus() == 200) {
			res = response.getEntity(String.class);
		}

		return res;
	}
	
	protected String executePOSTWithRequestEntity(String postPath, Object reqEntity){
		if (debugRequests){
			System.out.println("Executing POST to: " + serverUri.toString() + postPath);
			System.out.println("Request entity: " + reqEntity.toString());
		}
		
		//TODO here only xml_type??
		ClientResponse response = webResource.path(postPath).type(MediaType.APPLICATION_XML_TYPE)
                .post(ClientResponse.class, reqEntity);
		
		String res = "Failed : HTTP error code : " + response.getStatus();
		
		if (response.getStatus() == 200) {
			res = response.getEntity(String.class);
		}

		return res;
	}
	
	protected String executePUT(String putPath){
		if (debugRequests){
			System.out.println("Executing PUT to: " + serverUri.toString() + putPath);
		}
		
		ClientResponse response = webResource.path(putPath).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .put(ClientResponse.class);		
		
		String res = "Failed : HTTP error code : " + response.getStatus();
		
		if (response.getStatus() == 200) {
			res = response.getEntity(String.class);
		}

		return res;
	}
	
	protected String executeDELETE(String deletePath){
		if (debugRequests){
			System.out.println("Executing DELETE to: " + serverUri.toString() + deletePath);
		}
		ClientResponse response = webResource.path(deletePath).delete(ClientResponse.class);
		String res = "Failed : HTTP error code : " + response.getStatus();
		
		if (response.getStatus() == 200) {
			res = response.getEntity(String.class);
		}

		return res;
	}
	
	public void setDebugEnabled (Boolean debug){
		debugRequests = debug;
	}
	
	private static URI getBaseURI(String requestUrl) {
	    return UriBuilder.fromUri(requestUrl).build();
	}

}
