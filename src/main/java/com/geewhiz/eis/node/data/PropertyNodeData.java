package com.geewhiz.eis.node.data;

import static com.geewhiz.eis.attributes.PropertyAttributes.DESCRIPTION;
import static com.geewhiz.eis.attributes.PropertyAttributes.TEMPORAL_SCOPE;

import java.util.List;
import java.util.Map;

import com.geewhiz.eis.constants.Label;
import com.geewhiz.eis.constants.Relation;
import com.geewhiz.eis.constants.TemporalScope;
import com.geewhiz.eis.node.data.container.RelationData;

public class PropertyNodeData extends NamedNodeData {

	private String description;
	private TemporalScope timeSpan;
	private DeploymentNodeData wasIntroduced;

	public PropertyNodeData(String name) {
		super(name);
	}

	public PropertyNodeData(String pk, String name) {
		super(pk, name);
	}

	public PropertyNodeData(String pk, String name, DeploymentNodeData wasIntroduced, TemporalScope timeSpan, String description) {
		super(pk, name);
		this.wasIntroduced = wasIntroduced;
		this.timeSpan = timeSpan;
		this.description = description;
	}

	public void setWasIntroduced(DeploymentNodeData deploymentNodeData) {
		this.wasIntroduced = deploymentNodeData;
	}

	public DeploymentNodeData getWasIntroduced() {
		return wasIntroduced;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TemporalScope getTimeSpan() {
		return timeSpan;
	}

	public String getTimeSpanAsString() {
		return timeSpan.name();
	}

	public void setTimeSpan(TemporalScope timeSpan) {
		this.timeSpan = timeSpan;
	}

	@Override
	public Map<String, String> getAttributeValues() {
		Map<String, String> attributeValues = super.getAttributeValues();

		attributeValues.put(DESCRIPTION, getDescription());
		attributeValues.put(TEMPORAL_SCOPE, getTimeSpanAsString());

		return attributeValues;
	}

	@Override
	public List<RelationData> getRelations() {
		List<RelationData> relationData = super.getRelations();

		relationData.add(new RelationData(this, Relation.WAS_INTRODUCED, wasIntroduced));

		return relationData;
	}

	@Override
	public Label getLabel() {
		return Label.PROPERTY;
	}
}
