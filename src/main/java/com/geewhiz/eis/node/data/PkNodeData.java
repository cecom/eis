package com.geewhiz.eis.node.data;

import static com.geewhiz.eis.attributes.GeneralAttributes.PK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.constants.Label;
import com.geewhiz.eis.node.data.container.RelationData;

public abstract class PkNodeData {

	private Long id;
	private String pk;
	private UserNodeData createdBy;

	public PkNodeData() {
		this.pk = UUID.randomUUID().toString();
	}

	public PkNodeData(String pk) {
		this.pk = pk;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPk() {
		return pk;
	}

	public UserNodeData getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserNodeData createdBy) {
		this.createdBy = createdBy;
	}

	public abstract Label getLabel();

	public abstract boolean usesIndex();

	public abstract IndexName getIndexName();

	public abstract String getIndexValue();

	public Map<String, String> getAttributeValues() {
		Map<String, String> attributeValues = new HashMap<String, String>();

		attributeValues.put(PK, getPk());

		return attributeValues;
	}

	public List<RelationData> getRelations() {
		List<RelationData> relationData = new ArrayList<RelationData>();

		// relationData.add(new RelationData(this, Relation.CREATED_BY, createdBy));

		return relationData;
	}

}
