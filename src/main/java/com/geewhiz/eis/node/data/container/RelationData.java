package com.geewhiz.eis.node.data.container;

import java.util.Collections;
import java.util.Map;

import org.neo4j.graphdb.RelationshipType;

import com.geewhiz.eis.node.data.PkNodeData;

public class RelationData {

	private PkNodeData startPkNodeData;
	private RelationshipType relationshipType;
	private PkNodeData endPkNodeData;
	private Map<String, String> relationAttributeValues;

	public RelationData(PkNodeData startPkNodeData, RelationshipType relationshipType, PkNodeData endPkNodeData) {
		this(startPkNodeData, relationshipType, endPkNodeData, Collections.<String, String> emptyMap());
	}

	public RelationData(PkNodeData startPkNodeData, RelationshipType relationshipType, PkNodeData endPkNodeData, Map<String, String> relationAttributeValues) {
		this.startPkNodeData = startPkNodeData;
		this.relationshipType = relationshipType;
		this.endPkNodeData = endPkNodeData;
		this.relationAttributeValues = relationAttributeValues;
	}

	public PkNodeData getStartPkNodeData() {
		return startPkNodeData;
	}

	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	public PkNodeData getEndPkNodeData() {
		return endPkNodeData;
	}

	public Map<String, String> getRelationAttributeValues() {
		return relationAttributeValues;
	}

}
