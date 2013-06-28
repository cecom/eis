package com.geewhiz.eis.node.finder.embedded;

import javax.inject.Inject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.node.data.PropertyNodeData;
import com.geewhiz.eis.node.data.embedded.EmbeddedPropertyNodeData;
import com.geewhiz.eis.node.finder.PropertyNodeFinder;

public class EmbeddedPropertyNodeFinder extends EmbeddedNamedNodeFinder<PropertyNodeData> implements PropertyNodeFinder {

	@Inject
	public EmbeddedPropertyNodeFinder(GraphDatabaseService db) {
		super(db);
	}

	@Override
	protected IndexName getNodeIndex() {
		return IndexName.PROPERTY_INDEX;
	}

	@Override
	protected PropertyNodeData createDataForNode(Node node) {
		return new EmbeddedPropertyNodeData(node);
	}
}
