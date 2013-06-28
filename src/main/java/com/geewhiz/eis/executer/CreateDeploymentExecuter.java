package com.geewhiz.eis.executer;

import com.geewhiz.eis.node.data.DeploymentNodeData;
import com.geewhiz.eis.node.finder.DeploymentNodeFinder;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.manipulator.GraphManipulator;

public class CreateDeploymentExecuter implements EisExecuter {

	private GraphManipulator graphManipulator;
	private NodeFinderFactory nodeFinderFactory;
	private Boolean force;

	private String name;
	private String successorDeployment;
	private String commit;
	private String tag;
	private String artifactUrl;
	private String buildId;

	public CreateDeploymentExecuter(GraphManipulator graphManipulator, NodeFinderFactory nodeFinderFactory,
	                                String name, String successorDeployment, String commit, String tag, String artifactUrl, String buildId, Boolean force) {
		this.graphManipulator = graphManipulator;
		this.nodeFinderFactory = nodeFinderFactory;
		this.name = name;
		this.successorDeployment = successorDeployment;
		this.commit = commit;
		this.tag = tag;
		this.artifactUrl = artifactUrl;
		this.buildId = buildId;
		this.force = force;
	}

	@Override
	public void run() {
		checkForExistence();

		DeploymentNodeData data = createDeploymentNodeData();
		graphManipulator.createNode(data);
	}

	private void checkForExistence() {
		DeploymentNodeFinder finder = nodeFinderFactory.createDeploymentNodeFinder();

		DeploymentNodeData data = finder.findIfExist(name);

		if (data != null && !force) {
			throw new RuntimeException("Node [" + name + "] allready exist's and no force option specified.");
		} else if (data != null) {
			graphManipulator.deleteNode(data);
		}
	}

	private DeploymentNodeData createDeploymentNodeData() {
		DeploymentNodeData data = new DeploymentNodeData(name);

		data.setCommit(commit);
		data.setArtifactUrl(artifactUrl);
		data.setBuildId(buildId);
		data.setTag(tag);
		if (successorDeployment != null) {
			data.setSuccessor(nodeFinderFactory.createDeploymentNodeFinder().find(successorDeployment));
		}
		return data;
	}
}
