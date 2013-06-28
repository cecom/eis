package com.geewhiz.eis;

import static com.geewhiz.eis.attributes.PropertyValueAttributes.VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.geewhiz.eis.constants.Relation;
import com.geewhiz.eis.constants.TemporalScope;
import com.geewhiz.eis.node.data.DeploymentNodeData;
import com.geewhiz.eis.node.data.EnvironmentNodeData;
import com.geewhiz.eis.node.data.PropertyNodeData;
import com.geewhiz.eis.node.data.PropertyValueNodeData;
import com.geewhiz.eis.node.finder.DeploymentNodeFinder;
import com.geewhiz.eis.node.finder.EnvironmentNodeFinder;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.finder.PropertyNodeFinder;
import com.geewhiz.eis.node.finder.embedded.EmbeddedDeploymentNodeFinder;
import com.geewhiz.eis.node.finder.embedded.EmbeddedEnvironmentNodeFinder;
import com.geewhiz.eis.node.finder.embedded.EmbeddedPropertyNodeFinder;
import com.geewhiz.eis.node.finder.embedded.EmbeddedPropertyValueNodeFinder;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.geewhiz.eis.utils.PropertyValueResolver;

public class TestDataCreator {

	private GraphDatabaseService db;
	private GraphManipulator graphManipulator;
	private NodeFinderFactory nodeFinderFactory;

	@Inject
	public TestDataCreator(GraphDatabaseService db, GraphManipulator graphManipulator, NodeFinderFactory nodeFinderFactory) {
		this.db = db;
		this.graphManipulator = graphManipulator;
		this.nodeFinderFactory = nodeFinderFactory;
	}

	public void createTestData() {
		Transaction transaction = db.beginTx();

		importDeployments();
		importProperties();
		importEnvironments();
		importPropertyValues();

		transaction.success();
		transaction.finish();

	}

	public void testTheData() {
		EmbeddedDeploymentNodeFinder deploymentNodeFinder = new EmbeddedDeploymentNodeFinder(db);
		EmbeddedPropertyNodeFinder propertyNodeFinder = new EmbeddedPropertyNodeFinder(db);
		EmbeddedEnvironmentNodeFinder environmentNodeFinder = new EmbeddedEnvironmentNodeFinder(db);
		EmbeddedPropertyValueNodeFinder propertyValueNodeFinder = new EmbeddedPropertyValueNodeFinder(db);

		// deployment test stuff
		Node d5Node = deploymentNodeFinder.findNode("2.3.1");
		assertNotNull(d5Node);

		Node d4Node = d5Node.getSingleRelationship(Relation.SUCCESSOR, Direction.OUTGOING).getEndNode();
		assertEquals(d4Node, deploymentNodeFinder.findNode("2.3.0"));

		// property test stuff
		Node p4Node = propertyNodeFinder.findNode("db.url");
		assertNotNull(p4Node);
		// assertEquals(3, IteratorUtil.count(p4Node.getRelationships(Direction.OUTGOING, CONTAINS)));

		// environment test stuff
		Node e4Node = environmentNodeFinder.findNode("dev1");
		assertNotNull(e4Node);
		Node e2Node = e4Node.getSingleRelationship(Relation.BASED_ON, Direction.OUTGOING).getEndNode();
		assertEquals(e2Node, environmentNodeFinder.findNode("dev"));
		Node e1Node = e2Node.getSingleRelationship(Relation.BASED_ON, Direction.OUTGOING).getEndNode();
		assertEquals(e1Node, environmentNodeFinder.findNode("ALL_ENVIRONMENTS"));

		// property value test stuff
		Node pv1Node = propertyValueNodeFinder.findNode("2.2.0", "log.level", "ALL_ENVIRONMENTS");
		assertNotNull(pv1Node);
		assertEquals("debug", pv1Node.getProperty(VALUE));

		pv1Node = propertyValueNodeFinder.findNode("2.4.0", "log.level", "ALL_ENVIRONMENTS");
		assertNotNull(pv1Node);
		assertEquals("debug", pv1Node.getProperty(VALUE));

		pv1Node = propertyValueNodeFinder.findNode("2.4.0", "log.level", "dev2");
		assertNotNull(pv1Node);
		assertEquals("info", pv1Node.getProperty(VALUE));

		Node pv2Node = propertyValueNodeFinder.findNode("2.4.0", "log.level", "dev1");
		assertNotNull(pv2Node);
		assertEquals("debug", pv2Node.getProperty(VALUE));

		Node pv5Node = propertyValueNodeFinder.findNode("2.2.0", "db.port", "ALL_ENVIRONMENTS");
		assertNotNull(pv5Node);
		assertEquals("1523", pv5Node.getProperty(VALUE));

		pv5Node = propertyValueNodeFinder.findNode("2.3.0", "db.port", "dev");
		assertNotNull(pv5Node);
		assertEquals("1522", pv5Node.getProperty(VALUE));

		pv5Node = propertyValueNodeFinder.findNode("2.4.0", "db.port", "dev1");
		assertNotNull(pv5Node);
		assertEquals("1522", pv5Node.getProperty(VALUE));

		Node pv8Node = propertyValueNodeFinder.findNode("2.2.0", "db.url", "dev1");
		assertNotNull(pv8Node);
		assertEquals("jdbc:oracle:thin:@%{db.host}:%{db.port}:%{db.sid}", pv8Node.getProperty(VALUE));

		PropertyValueResolver resolver = new PropertyValueResolver(nodeFinderFactory);

		String resolvedPv8Value = resolver.resolve("2.2.0", "db.url", "dev1");
		assertEquals("jdbc:oracle:thin:@geewhiz.de:1522:xe", resolvedPv8Value);
		assertNotNull(resolvedPv8Value);

		resolvedPv8Value = resolver.resolve("2.3.0", "db.url", "dev1");
		assertEquals("jdbc:oracle:thin:@geewhiz.de:1522:xe", resolvedPv8Value);
		assertNotNull(resolvedPv8Value);

		resolvedPv8Value = resolver.resolve("2.3.0", "db.url", "dev2");
		assertEquals("jdbc:oracle:thin:@geewhiz.de:1524:xe", resolvedPv8Value);
		assertNotNull(resolvedPv8Value);

		resolvedPv8Value = resolver.resolve("2.4.0", "db.url", "dev1");
		assertEquals("jdbc:oracle:thin:@geewhiz.de:1522:xe", resolvedPv8Value);
		assertNotNull(resolvedPv8Value);

		resolvedPv8Value = resolver.resolve("2.4.0", "db.url", "dev2");
		assertNotNull(resolvedPv8Value);
		assertEquals("jdbc:oracle:thin:@geewhiz.de:1524:xe", resolvedPv8Value);

	}

	private void importDeployments() {

		DeploymentNodeData d1 = new DeploymentNodeData("d1", "ALL_DEPLOYMENTS", null, getARandomSha1(), "http://..../tar.gz", "BuildId 0815", "Tag v0815");
		DeploymentNodeData d2 = new DeploymentNodeData("d2", "2.1.0", d1, getARandomSha1(), "http://..../tar.gz", "BuildId 0815", "Tag v0815");
		DeploymentNodeData d3 = new DeploymentNodeData("d3", "2.2.0", d2, getARandomSha1(), "http://..../tar.gz", "BuildId 0815", "Tag v0815");
		DeploymentNodeData d4 = new DeploymentNodeData("d4", "2.3.0", d3, getARandomSha1(), "http://..../tar.gz", "BuildId 0815", "Tag v0815");
		DeploymentNodeData d5 = new DeploymentNodeData("d5", "2.3.1", d4, getARandomSha1(), "http://..../tar.gz", "BuildId 0815", "Tag v0815");
		DeploymentNodeData d6 = new DeploymentNodeData("d6", "2.3.2", d5, getARandomSha1(), "http://..../tar.gz", "BuildId 0815", "Tag v0815");
		DeploymentNodeData d7 = new DeploymentNodeData("d7", "2.4.0", d4, getARandomSha1(), "http://..../tar.gz", "BuildId 0815", "Tag v0815");

		graphManipulator.createNode(d1);
		graphManipulator.createNode(d2);
		graphManipulator.createNode(d3);
		graphManipulator.createNode(d4);
		graphManipulator.createNode(d5);
		graphManipulator.createNode(d6);
		graphManipulator.createNode(d7);
	}

	private void importProperties() {
		DeploymentNodeFinder finder = nodeFinderFactory.createDeploymentNodeFinder();

		DeploymentNodeData d220 = finder.find("2.2.0");

		PropertyNodeData p1 = new PropertyNodeData("p1", "db.port", d220, TemporalScope.LATEST, "description 0815");
		PropertyNodeData p2 = new PropertyNodeData("p2", "db.host", d220, TemporalScope.LATEST, "description 0815");
		PropertyNodeData p3 = new PropertyNodeData("p3", "db.sid", d220, TemporalScope.LATEST, "description 0815");
		PropertyNodeData p4 = new PropertyNodeData("p4", "log.level", d220, TemporalScope.DEPLOYMENT_DEPENDENT, "description 0815");
		PropertyNodeData p5 = new PropertyNodeData("p5", "db.url", d220, TemporalScope.LATEST, "description 0815");

		graphManipulator.createNode(p1);
		graphManipulator.createNode(p2);
		graphManipulator.createNode(p3);
		graphManipulator.createNode(p4);
		graphManipulator.createNode(p5);
	}

	private void importEnvironments() {

		EnvironmentNodeData e1 = new EnvironmentNodeData("e1", "ALL_ENVIRONMENTS", null, "description 0815");
		EnvironmentNodeData e2 = new EnvironmentNodeData("e2", "dev", e1, "description 0815");
		EnvironmentNodeData e3 = new EnvironmentNodeData("e3", "fsys", e1, "description 0815");
		EnvironmentNodeData e4 = new EnvironmentNodeData("e4", "dev1", e2, "description 0815");
		EnvironmentNodeData e5 = new EnvironmentNodeData("e5", "dev2", e2, "description 0815");
		EnvironmentNodeData e6 = new EnvironmentNodeData("e6", "fsys1", e3, "description 0815");
		EnvironmentNodeData e7 = new EnvironmentNodeData("e7", "fsys2", e3, "description 0815");

		graphManipulator.createNode(e1);
		graphManipulator.createNode(e2);
		graphManipulator.createNode(e3);
		graphManipulator.createNode(e4);
		graphManipulator.createNode(e5);
		graphManipulator.createNode(e6);
		graphManipulator.createNode(e7);
	}

	private void importPropertyValues() {
		DeploymentNodeFinder deploymentFinder = nodeFinderFactory.createDeploymentNodeFinder();
		PropertyNodeFinder propertyFinder = nodeFinderFactory.createPropertyNodeFinder();
		EnvironmentNodeFinder environmentFinder = nodeFinderFactory.createEnvironmentNodeFinder();

		PropertyValueNodeData pv1 = new PropertyValueNodeData("pv1", deploymentFinder.find("2.2.0"), propertyFinder.find("log.level"), environmentFinder.find("ALL_ENVIRONMENTS"), "debug");
		PropertyValueNodeData pv2 = new PropertyValueNodeData("pv2", deploymentFinder.find("2.3.0"), propertyFinder.find("log.level"), environmentFinder.find("dev2"), "info");
		PropertyValueNodeData pv3 = new PropertyValueNodeData("pv3", deploymentFinder.find("2.2.0"), propertyFinder.find("db.port"), environmentFinder.find("ALL_ENVIRONMENTS"), "1521");
		PropertyValueNodeData pv4 = new PropertyValueNodeData("pv4", deploymentFinder.find("2.3.0"), propertyFinder.find("db.port"), environmentFinder.find("dev"), "1522");
		PropertyValueNodeData pv5 = new PropertyValueNodeData("pv5", deploymentFinder.find("2.4.0"), propertyFinder.find("db.port"), environmentFinder.find("ALL_ENVIRONMENTS"), "1523");
		PropertyValueNodeData pv6 = new PropertyValueNodeData("pv6", deploymentFinder.find("2.4.0"), propertyFinder.find("db.port"), environmentFinder.find("dev2"), "1524");
		PropertyValueNodeData pv7 = new PropertyValueNodeData("pv7", deploymentFinder.find("2.2.0"), propertyFinder.find("db.host"), environmentFinder.find("ALL_ENVIRONMENTS"), "geewhiz.de");
		PropertyValueNodeData pv8 = new PropertyValueNodeData("pv8", deploymentFinder.find("2.2.0"), propertyFinder.find("db.sid"), environmentFinder.find("ALL_ENVIRONMENTS"), "xe");
		PropertyValueNodeData pv9 = new PropertyValueNodeData("pv9", deploymentFinder.find("2.2.0"), propertyFinder.find("db.url"), environmentFinder.find("ALL_ENVIRONMENTS"), "jdbc:oracle:thin:@"
		        + PropertyValueResolver.BEGIN_TOKEN + "db.host" + PropertyValueResolver.END_TOKEN
		        + ":" + PropertyValueResolver.BEGIN_TOKEN + "db.port" + PropertyValueResolver.END_TOKEN
		        + ":" + PropertyValueResolver.BEGIN_TOKEN + "db.sid" + PropertyValueResolver.END_TOKEN);

		graphManipulator.createNode(pv1);
		graphManipulator.createNode(pv2);
		graphManipulator.createNode(pv3);
		graphManipulator.createNode(pv4);
		graphManipulator.createNode(pv5);
		graphManipulator.createNode(pv6);
		graphManipulator.createNode(pv7);
		graphManipulator.createNode(pv8);
		graphManipulator.createNode(pv9);
	}

	private String getARandomSha1() {
		SecureRandom prng;
		try {
			prng = SecureRandom.getInstance("SHA1PRNG");
			String randomNum = new Integer(prng.nextInt()).toString();

			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] sha1ByteArray = sha.digest(randomNum.getBytes());

			String result = hexEncode(sha1ByteArray);
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private String hexEncode(byte[] aInput) {
		StringBuilder result = new StringBuilder();
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		for (int idx = 0; idx < aInput.length; ++idx) {
			byte b = aInput[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}
}
