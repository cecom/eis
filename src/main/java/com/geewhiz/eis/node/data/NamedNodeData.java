package com.geewhiz.eis.node.data;

import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;

import java.util.Map;

import com.geewhiz.eis.constants.IndexName;

public abstract class NamedNodeData extends PkNodeData {

	private String name;

	public NamedNodeData(String name) {
		super();
		this.name = name;
	}

	public NamedNodeData(String pk, String name) {
		super(pk);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Map<String, String> getAttributeValues() {
		Map<String, String> attributeValues = super.getAttributeValues();

		attributeValues.put(NAME, getName());

		return attributeValues;
	}

	@Override
	public IndexName getIndexName() {
		return IndexName.valueOf(getLabel().name() + "_INDEX");
	}

	@Override
	public String getIndexValue() {
		return name;
	}

	@Override
	public boolean usesIndex() {
		return true;
	}
}
