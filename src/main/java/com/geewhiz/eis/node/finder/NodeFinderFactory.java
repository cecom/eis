package com.geewhiz.eis.node.finder;

public interface NodeFinderFactory {

	DeploymentNodeFinder createDeploymentNodeFinder();

	EnvironmentNodeFinder createEnvironmentNodeFinder();

	PropertyNodeFinder createPropertyNodeFinder();

	PropertyValueNodeFinder createPropertyValueNodeFinder();

	UserNodeFinder createUserNodeFinder();

}
