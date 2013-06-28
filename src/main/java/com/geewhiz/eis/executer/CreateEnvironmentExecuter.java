package com.geewhiz.eis.executer;

import com.geewhiz.eis.node.data.EnvironmentNodeData;
import com.geewhiz.eis.node.finder.EnvironmentNodeFinder;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.manipulator.GraphManipulator;

public class CreateEnvironmentExecuter implements EisExecuter {

	private GraphManipulator graphManipulator;
	private NodeFinderFactory nodeFinderFactory;
	private Boolean force;

	private String name;
	private String basedOn;
	private String description;

	public CreateEnvironmentExecuter(GraphManipulator graphManipulator, NodeFinderFactory nodeFinderFactory,
	                                 String name, String basedOn, String description, Boolean force) {
		this.graphManipulator = graphManipulator;
		this.nodeFinderFactory = nodeFinderFactory;
		this.name = name;
		this.basedOn = basedOn;
		this.description = description;
		this.force = force;
	}

	@Override
	public void run() {
		checkForExistence();

		EnvironmentNodeData data = createNodeData();
		graphManipulator.createNode(data);
	}

	private void checkForExistence() {
		EnvironmentNodeFinder finder = nodeFinderFactory.createEnvironmentNodeFinder();

		EnvironmentNodeData data = finder.findIfExist(name);

		if (data != null && !force) {
			throw new RuntimeException("Node [" + name + "] allready exist's and no force option specified.");
		} else if (data != null) {
			graphManipulator.deleteNode(data);
		}
	}

	private EnvironmentNodeData createNodeData() {
		EnvironmentNodeData data = new EnvironmentNodeData(name);

		data.setDescription(description);
		if (basedOn != null) {
			data.setBasedOn(nodeFinderFactory.createEnvironmentNodeFinder().find(basedOn));
		}
		return data;
	}
}
