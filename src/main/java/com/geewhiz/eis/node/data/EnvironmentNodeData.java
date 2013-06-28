package com.geewhiz.eis.node.data;

import static com.geewhiz.eis.attributes.EnvironmentAttributes.DESCRIPTION;

import java.util.List;
import java.util.Map;

import com.geewhiz.eis.constants.Label;
import com.geewhiz.eis.constants.Relation;
import com.geewhiz.eis.node.data.container.RelationData;

public class EnvironmentNodeData extends NamedNodeData {

	private String description;
	private EnvironmentNodeData basedOn;

	public EnvironmentNodeData(String name) {
		super(name);
	}

	public EnvironmentNodeData(String pk, String name) {
		super(pk, name);
	}

	public EnvironmentNodeData(String pk, String name, EnvironmentNodeData basedOn, String description) {
		super(pk, name);
		this.basedOn = basedOn;
		this.description = description;
	}

	public EnvironmentNodeData getBasedOn() {
		return basedOn;
	}

	public void setBasedOn(EnvironmentNodeData basedOn) {
		this.basedOn = basedOn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Map<String, String> getAttributeValues() {
		Map<String, String> attributeValues = super.getAttributeValues();

		attributeValues.put(DESCRIPTION, getDescription());

		return attributeValues;
	}

	@Override
	public List<RelationData> getRelations() {
		List<RelationData> relationData = super.getRelations();

		if (basedOn != null) {
			relationData.add(new RelationData(this, Relation.BASED_ON, basedOn));
		}

		return relationData;
	}

	@Override
	public Label getLabel() {
		return Label.ENVIRONMENT;
	}

}
