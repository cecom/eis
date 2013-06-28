package com.geewhiz.eis.node.finder.embedded;

import javax.inject.Inject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.node.data.UserNodeData;
import com.geewhiz.eis.node.data.embedded.EmbeddedUserNodeData;
import com.geewhiz.eis.node.finder.UserNodeFinder;

public class EmbeddedUserNodeFinder extends EmbeddedNamedNodeFinder<UserNodeData> implements UserNodeFinder {

	@Inject
	public EmbeddedUserNodeFinder(GraphDatabaseService db) {
		super(db);
	}

	@Override
	protected IndexName getNodeIndex() {
		return IndexName.USER_INDEX;
	}

	@Override
	protected UserNodeData createDataForNode(Node node) {
		return new EmbeddedUserNodeData(node);
	}

}
