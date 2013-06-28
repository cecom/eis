package com.geewhiz.eis.node.data.embedded;

import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;
import static com.geewhiz.eis.attributes.GeneralAttributes.PK;
import static com.geewhiz.eis.attributes.PropertyAttributes.DESCRIPTION;
import static com.geewhiz.eis.attributes.PropertyAttributes.TEMPORAL_SCOPE;
import static com.geewhiz.eis.constants.Relation.WAS_INTRODUCED;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.geewhiz.eis.constants.TemporalScope;
import com.geewhiz.eis.node.data.PropertyNodeData;

public class EmbeddedPropertyNodeData extends PropertyNodeData {

	public EmbeddedPropertyNodeData(Node node) {
		super((String) node.getProperty(PK), (String) node.getProperty(NAME));

		setId(node.getId());
		setDescription(node.hasProperty(DESCRIPTION) ? (String) node.getProperty(DESCRIPTION) : null);
		setTimeSpan(TemporalScope.valueOf((String) node.getProperty(TEMPORAL_SCOPE)));

		Relationship relationship = node.getSingleRelationship(WAS_INTRODUCED, Direction.OUTGOING);
		if (relationship != null) {
			Node wasIntroducedNode = relationship.getEndNode();
			setWasIntroduced(new EmbeddedDeploymentNodeData(wasIntroducedNode));
		}

	}

}
