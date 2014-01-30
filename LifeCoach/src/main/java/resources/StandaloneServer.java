package resources;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

@Path("/rest/")
public class StandaloneServer {
	private final static Logger LOGGER = Logger
			.getLogger(StandaloneServer.class.getName());
	private static final String PROTOCOL = "http://";
	private static final String PORT = ":5030/";

	public static void main(String[] args) throws IOException {
		try {
			String hostname;
			hostname = "localhost";
			String baseUrl = PROTOCOL + hostname + PORT;

			ResourceConfig rc = new PackagesResourceConfig("resources",
					"lifeCoach", "org.codehaus.jackson.jaxrs");
			HttpServer server = HttpServerFactory.create(baseUrl, rc);
			server.start();
			LOGGER.log(Level.INFO, "Server is listening on: " + baseUrl
					+ "\n [kill the process to exit]");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Server init error", e);
			System.exit(1);
		}
	}
}
