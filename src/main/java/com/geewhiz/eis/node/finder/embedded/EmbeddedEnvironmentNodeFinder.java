package com.geewhiz.eis.node.finder.embedded;

import javax.inject.Inject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.node.data.EnvironmentNodeData;
import com.geewhiz.eis.node.data.embedded.EmbeddedEnvironmentNodeData;
import com.geewhiz.eis.node.finder.EnvironmentNodeFinder;

public class EmbeddedEnvironmentNodeFinder extends EmbeddedNamedNodeFinder<EnvironmentNodeData> implements EnvironmentNodeFinder {

	@Inject
	public EmbeddedEnvironmentNodeFinder(GraphDatabaseService db) {
		super(db);
	}

	@Override
	protected IndexName getNodeIndex() {
		return IndexName.ENVIRONMENT_INDEX;
	}

	@Override
	protected EnvironmentNodeData createDataForNode(Node node) {
		return new EmbeddedEnvironmentNodeData(node);
	}

}
