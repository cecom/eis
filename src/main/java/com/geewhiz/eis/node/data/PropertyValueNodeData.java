package com.geewhiz.eis.node.data;

import static com.geewhiz.eis.attributes.PropertyValueAttributes.VALUE;

import java.util.List;
import java.util.Map;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.constants.Label;
import com.geewhiz.eis.constants.Relation;
import com.geewhiz.eis.node.data.container.RelationData;

public class PropertyValueNodeData extends PkNodeData {

	private DeploymentNodeData effectiveSince;
	private EnvironmentNodeData appliesTo;
	private PropertyNodeData valueOf;
	private String value;

	public PropertyValueNodeData() {
		super();
	}

	public PropertyValueNodeData(String pk) {
		super(pk);
	}

	public PropertyValueNodeData(String pk, DeploymentNodeData effectiveSince, PropertyNodeData valueOf, EnvironmentNodeData appliesTo, String value) {
		super(pk);
		this.effectiveSince = effectiveSince;
		this.appliesTo = appliesTo;
		this.valueOf = valueOf;
		this.value = value;
	}

	public DeploymentNodeData getEffectiveSince() {
		return effectiveSince;
	}

	public void setEffectiveSince(DeploymentNodeData effectiveSince) {
		this.effectiveSince = effectiveSince;
	}

	public EnvironmentNodeData getAppliesTo() {
		return appliesTo;
	}

	public void setAppliesTo(EnvironmentNodeData appliesTo) {
		this.appliesTo = appliesTo;
	}

	public PropertyNodeData getValueOf() {
		return valueOf;
	}

	public void setValueOf(PropertyNodeData ValueOf) {
		this.valueOf = ValueOf;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Map<String, String> getAttributeValues() {
		Map<String, String> attributeValues = super.getAttributeValues();

		attributeValues.put(VALUE, getValue());

		return attributeValues;
	}

	@Override
	public List<RelationData> getRelations() {
		List<RelationData> relationData = super.getRelations();

		relationData.add(new RelationData(this, Relation.EFFECTIVE_SINCE, effectiveSince));
		relationData.add(new RelationData(this, Relation.APPLIES_TO, appliesTo));
		relationData.add(new RelationData(this, Relation.VALUE_OF, valueOf));

		return relationData;
	}

	@Override
	public Label getLabel() {
		return Label.PROPERTY_VALUE;
	}

	@Override
	public boolean usesIndex() {
		return false;
	}

	@Override
	public IndexName getIndexName() {
		throw new IllegalAccessError("we aren't using an index");
	}

	@Override
	public String getIndexValue() {
		throw new IllegalAccessError("we aren't using an index");
	}

}
