package com.geewhiz.eis.node.data.embedded;

import static com.geewhiz.eis.attributes.DeploymentAttributes.ARTIFACT_URL;
import static com.geewhiz.eis.attributes.DeploymentAttributes.BUILD_ID;
import static com.geewhiz.eis.attributes.DeploymentAttributes.COMMIT;
import static com.geewhiz.eis.attributes.DeploymentAttributes.TAG;
import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;
import static com.geewhiz.eis.attributes.GeneralAttributes.PK;
import static com.geewhiz.eis.constants.Relation.SUCCESSOR;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.geewhiz.eis.node.data.DeploymentNodeData;

public class EmbeddedDeploymentNodeData extends DeploymentNodeData {

	public EmbeddedDeploymentNodeData(Node node) {
		super((String) node.getProperty(PK), (String) node.getProperty(NAME));

		setId(node.getId());
		setTag(node.hasProperty(TAG) ? (String) node.getProperty(TAG) : null);
		setCommit(node.hasProperty(COMMIT) ? (String) node.getProperty(COMMIT) : null);
		setBuildId(node.hasProperty(BUILD_ID) ? (String) node.getProperty(BUILD_ID) : null);
		setArtifactUrl(node.hasProperty(ARTIFACT_URL) ? (String) node.getProperty(ARTIFACT_URL) : null);

		Relationship relationship = node.getSingleRelationship(SUCCESSOR, Direction.OUTGOING);

		if (relationship != null) {
			Node successor = relationship.getEndNode();
			setSuccessor(new EmbeddedDeploymentNodeData(successor));
		}

	}
}
