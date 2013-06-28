package com.geewhiz.eis.executer;

import com.geewhiz.eis.node.data.PropertyValueNodeData;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.manipulator.GraphManipulator;

public class SetPropertyValueExecuter implements EisExecuter {

	private GraphManipulator graphManipulator;
	private NodeFinderFactory nodeFinderFactory;

	private String deployment;
	private String property;
	private String environment;
	private String value;

	public SetPropertyValueExecuter(GraphManipulator graphManipulator,
	                                NodeFinderFactory nodeFinderFactory,
	                                String property,
	                                String deployment,
	                                String environment,
	                                String value) {
		this.graphManipulator = graphManipulator;
		this.nodeFinderFactory = nodeFinderFactory;
		this.property = property;
		this.deployment = deployment;
		this.environment = environment;
		this.value = value;
	}

	@Override
	public void run() {
		PropertyValueNodeData data = createNodeData();
		graphManipulator.createNode(data);
	}

	private PropertyValueNodeData createNodeData() {
		PropertyValueNodeData data = new PropertyValueNodeData();

		data.setValue(value);
		data.setAppliesTo(nodeFinderFactory.createEnvironmentNodeFinder().find(environment));
		data.setValueOf(nodeFinderFactory.createPropertyNodeFinder().find(property));

		if (deployment != null) {
			data.setEffectiveSince(nodeFinderFactory.createDeploymentNodeFinder().find(deployment));
		}
		return data;
	}
}
