package com.geewhiz.eis;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestTest {

	public static void main(String[] args) {
		RestTest test = new RestTest("http://localhost:7474/db/data");
		test.testServer();
		try {
			URI foo = test.createNode();
			test.addProperty(foo, "name", "foo");

			URI bar = test.createNode();
			test.addProperty(bar, "name", "bar");

			URI relationshipUri = addRelationship(foo, bar, "knows", "{ \"from\" : \"1976\", \"until\" : \"1986\" }");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static URI addRelationship(URI startNode, URI endNode, String relationshipType, String jsonAttributes) throws URISyntaxException {
		URI fromUri = new URI(startNode.toString() + "/relationships");
		String relationshipJson = "{ \"to\" : \"" + endNode.toString() + "\" , \"type\" : \"" + relationshipType + "\"}";

		WebResource resource = Client.create().resource(fromUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
		        .type(MediaType.APPLICATION_JSON)
		        .entity(relationshipJson)
		        .post(ClientResponse.class);

		final URI location = response.getLocation();
		System.out.println(String.format("POST to [%s], status code [%d], location header [%s]", fromUri, response.getStatus(), location.toString()));

		response.close();
		return location;
	}

	private String serverUrl;

	public RestTest(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	private void addProperty(URI node, String property, String propertyValue) throws URISyntaxException {
		URI propertyUri = new URI(String.format("%s/properties/%s", node.toString(), property));

		WebResource resource = Client.create().resource(propertyUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
		        .type(MediaType.APPLICATION_JSON)
		        .entity("\"" + propertyValue + "\"")
		        .put(ClientResponse.class);

		System.out.println(String.format("PUT to [%s], status code [%d]", propertyUri, response.getStatus()));
		response.close();
	}

	public void testServer() {
		WebResource resource = Client.create().resource(serverUrl);

		ClientResponse response = null;
		try {
			response = resource.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Server not reachable!");
			}
		}
		finally {
			response.close();
		}
	}

	private URI createNode() throws URISyntaxException {
		final String nodeEntryPointUri = serverUrl + "/node";

		WebResource resource = Client.create().resource(nodeEntryPointUri);

		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
		        .type(MediaType.APPLICATION_JSON)
		        .entity("{}")
		        .post(ClientResponse.class);
		final URI nodeUri = response.getLocation();
		System.out.println(String.format("POST to [%s], status code [%d], location header [%s]", nodeEntryPointUri, response.getStatus(), nodeUri.toString()));
		response.close();

		return nodeUri;
	}
}
