package com.geewhiz.eis.executer;

import com.geewhiz.eis.constants.TemporalScope;
import com.geewhiz.eis.node.data.PropertyNodeData;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.finder.PropertyNodeFinder;
import com.geewhiz.eis.node.manipulator.GraphManipulator;

public class CreatePropertyExecuter implements EisExecuter {

	private GraphManipulator graphManipulator;
	private NodeFinderFactory nodeFinderFactory;
	private String name;
	private String wasIntroduced;
	private String description;
	private TemporalScope temporalScope;
	private Boolean force;

	public CreatePropertyExecuter(GraphManipulator graphManipulator,
	                              NodeFinderFactory nodeFinderFactory,
	                              String name,
	                              String wasIntroduced,
	                              String temporalScope,
	                              String description,
	                              Boolean force) {
		this.graphManipulator = graphManipulator;
		this.nodeFinderFactory = nodeFinderFactory;
		this.name = name;
		this.wasIntroduced = wasIntroduced;
		this.temporalScope = TemporalScope.valueOf(temporalScope);
		this.description = description;
		this.force = force;
	}

	@Override
	public void run() {
		checkForExistence();

		PropertyNodeData data = createNodeData();
		graphManipulator.createNode(data);
	}

	private void checkForExistence() {
		PropertyNodeFinder finder = nodeFinderFactory.createPropertyNodeFinder();

		PropertyNodeData data = finder.findIfExist(name);

		if (data != null && !force) {
			throw new RuntimeException("Node [" + name + "] allready exist's and no force option specified.");
		} else if (data != null) {
			graphManipulator.deleteNode(data);
		}
	}

	private PropertyNodeData createNodeData() {
		PropertyNodeData data = new PropertyNodeData(name);

		data.setDescription(description);
		data.setTimeSpan(temporalScope);
		if (wasIntroduced != null) {
			data.setWasIntroduced(nodeFinderFactory.createDeploymentNodeFinder().find(wasIntroduced));
		}
		return data;
	}
}
