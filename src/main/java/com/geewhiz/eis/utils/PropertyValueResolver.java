package com.geewhiz.eis.utils;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

import com.geewhiz.eis.node.data.PropertyValueNodeData;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.finder.PropertyValueNodeFinder;

public class PropertyValueResolver {

	public static final String BEGIN_TOKEN = "%{";
	public static final String END_TOKEN = "}";

	private PropertyValueNodeFinder propertyValueNodefinder;

	public PropertyValueResolver(NodeFinderFactory finderFactory) {
		this.propertyValueNodefinder = finderFactory.createPropertyValueNodeFinder();
	}

	public String resolve(final String deployment, final String property, final String environment) {
		PropertyValueNodeData propertyValueData = getPropertyValue(deployment, property, environment);
		String propertyValue = propertyValueData.getValue();

		StrSubstitutor substitutor = new StrSubstitutor(new StrLookup<String>() {
			@Override
			public String lookup(String key) {
				return getPropertyValue(deployment, key, environment).getValue();
			};
		});

		substitutor.setVariablePrefix(BEGIN_TOKEN);
		substitutor.setVariableSuffix(END_TOKEN);

		return substitutor.replace(propertyValue);
	}

	private PropertyValueNodeData getPropertyValue(String deployment, String property, String environment) {
		PropertyValueNodeData propertyValue = propertyValueNodefinder.find(deployment, property, environment);
		return propertyValue;
	}

}
