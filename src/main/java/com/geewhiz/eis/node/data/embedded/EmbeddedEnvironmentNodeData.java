package com.geewhiz.eis.node.data.embedded;

import static com.geewhiz.eis.attributes.EnvironmentAttributes.DESCRIPTION;
import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;
import static com.geewhiz.eis.attributes.GeneralAttributes.PK;
import static com.geewhiz.eis.constants.Relation.BASED_ON;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.geewhiz.eis.node.data.EnvironmentNodeData;

public class EmbeddedEnvironmentNodeData extends EnvironmentNodeData {

	public EmbeddedEnvironmentNodeData(Node node) {
		super((String) node.getProperty(PK), (String) node.getProperty(NAME));

		setId(node.getId());
		setDescription(node.hasProperty(DESCRIPTION) ? (String) node.getProperty(DESCRIPTION) : null);

		Relationship relationship = node.getSingleRelationship(BASED_ON, Direction.OUTGOING);
		if (relationship != null) {
			Node basedOnNode = relationship.getEndNode();
			setBasedOn(new EmbeddedEnvironmentNodeData(basedOnNode));
		}

	}
}
