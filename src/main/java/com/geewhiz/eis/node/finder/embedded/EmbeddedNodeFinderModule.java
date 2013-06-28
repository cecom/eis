package com.geewhiz.eis.node.finder.embedded;

import com.geewhiz.eis.node.finder.DeploymentNodeFinder;
import com.geewhiz.eis.node.finder.EnvironmentNodeFinder;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.finder.PropertyNodeFinder;
import com.geewhiz.eis.node.finder.PropertyValueNodeFinder;
import com.geewhiz.eis.node.finder.UserNodeFinder;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class EmbeddedNodeFinderModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
		        .implement(DeploymentNodeFinder.class, EmbeddedDeploymentNodeFinder.class)
		        .implement(EnvironmentNodeFinder.class, EmbeddedEnvironmentNodeFinder.class)
		        .implement(PropertyNodeFinder.class, EmbeddedPropertyNodeFinder.class)
		        .implement(PropertyValueNodeFinder.class, EmbeddedPropertyValueNodeFinder.class)
		        .implement(UserNodeFinder.class, EmbeddedUserNodeFinder.class)
		        .build(NodeFinderFactory.class));
	}
}
