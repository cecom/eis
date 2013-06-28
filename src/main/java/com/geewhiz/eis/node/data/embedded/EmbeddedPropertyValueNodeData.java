package com.geewhiz.eis.node.data.embedded;

import static com.geewhiz.eis.attributes.GeneralAttributes.PK;
import static com.geewhiz.eis.attributes.PropertyValueAttributes.VALUE;
import static com.geewhiz.eis.constants.Relation.APPLIES_TO;
import static com.geewhiz.eis.constants.Relation.EFFECTIVE_SINCE;
import static com.geewhiz.eis.constants.Relation.VALUE_OF;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

import com.geewhiz.eis.constants.TemporalScope;
import com.geewhiz.eis.node.data.PropertyNodeData;
import com.geewhiz.eis.node.data.PropertyValueNodeData;

public class EmbeddedPropertyValueNodeData extends PropertyValueNodeData {

	public EmbeddedPropertyValueNodeData(Node node) {
		super((String) node.getProperty(PK));

		Node appliesToNode = node.getSingleRelationship(APPLIES_TO, Direction.OUTGOING).getEndNode();
		Node valueOfNode = node.getSingleRelationship(VALUE_OF, Direction.OUTGOING).getEndNode();

		PropertyNodeData propertyNodeData = new EmbeddedPropertyNodeData(valueOfNode);

		setId(node.getId());
		setValue((String) node.getProperty(VALUE));
		setAppliesTo(new EmbeddedEnvironmentNodeData(appliesToNode));
		setValueOf(propertyNodeData);

		if (TemporalScope.DEPLOYMENT_DEPENDENT == propertyNodeData.getTimeSpan()) {
			Node effectiveSinceNode = node.getSingleRelationship(EFFECTIVE_SINCE, Direction.OUTGOING).getEndNode();
			setEffectiveSince(new EmbeddedDeploymentNodeData(effectiveSinceNode));
		}
	}

}
