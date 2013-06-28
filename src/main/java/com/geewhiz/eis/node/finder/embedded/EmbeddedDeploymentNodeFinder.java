package com.geewhiz.eis.node.finder.embedded;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.node.data.DeploymentNodeData;
import com.geewhiz.eis.node.data.embedded.EmbeddedDeploymentNodeData;
import com.geewhiz.eis.node.finder.DeploymentNodeFinder;
import com.google.inject.Inject;

public class EmbeddedDeploymentNodeFinder extends EmbeddedNamedNodeFinder<DeploymentNodeData> implements DeploymentNodeFinder {

	@Inject
	public EmbeddedDeploymentNodeFinder(GraphDatabaseService db) {
		super(db);
	}

	@Override
	protected IndexName getNodeIndex() {
		return IndexName.DEPLOYMENT_INDEX;
	}

	@Override
	protected DeploymentNodeData createDataForNode(Node node) {
		return new EmbeddedDeploymentNodeData(node);
	}
}
