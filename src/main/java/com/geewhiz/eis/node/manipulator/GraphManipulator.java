package com.geewhiz.eis.node.manipulator;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.RelationshipType;

import com.geewhiz.eis.node.data.PkNodeData;
import com.geewhiz.eis.node.data.container.RelationData;

public interface GraphManipulator {

	PkNodeData createNode(PkNodeData data);

	void deleteAll();

	void deleteNode(PkNodeData data);

	void createRelationship(PkNodeData start, RelationshipType type, PkNodeData end);

	void createRelationship(PkNodeData start, RelationshipType type, PkNodeData end, Map<String, String> attributeValues);

	void createRelationships(List<RelationData> relations);

	void createRelationshipsForData(PkNodeData data);

}
