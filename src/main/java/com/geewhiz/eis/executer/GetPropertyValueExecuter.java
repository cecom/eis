package com.geewhiz.eis.executer;

import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.utils.PropertyValueResolver;

public class GetPropertyValueExecuter implements EisExecuter {

	private NodeFinderFactory finderFactory;
	private String property;
	private String environment;
	private String deployment;

	public GetPropertyValueExecuter(NodeFinderFactory finderFactory, String property, String deployment, String environment) {
		this.finderFactory = finderFactory;
		this.property = property;
		this.deployment = deployment;
		this.environment = environment;
	}

	@Override
	public void run() {
		PropertyValueResolver resolver = new PropertyValueResolver(finderFactory);

		String value = resolver.resolve(deployment, property, environment);

		System.out.println(value);
	}
}
