package com.geewhiz.eis.node.manipulator;

import static com.geewhiz.eis.attributes.GeneralAttributes.CREATED;
import static com.geewhiz.eis.attributes.GeneralAttributes.LABEL;
import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import com.geewhiz.eis.node.data.PkNodeData;
import com.geewhiz.eis.node.data.container.RelationData;
import com.geewhiz.eis.utils.DateUtils;

public abstract class AbstractGraphManipulator implements GraphManipulator {

	private GraphDatabaseService db;

	protected AbstractGraphManipulator(GraphDatabaseService db) {
		this.db = db;
	}

	public GraphDatabaseService getDb() {
		return db;
	}

	@Override
	public PkNodeData createNode(PkNodeData data) {
		Transaction transaction = getDb().beginTx();

		Node node = getDb().createNode();

		System.out.println(String.format("[%s] == Node [%s] [id=%s] created. Values [%s] ", DateUtils.getCurrentTime(), data.getLabel().name(), node.getId(), data.getAttributeValues().toString()));

		node.setProperty(LABEL, data.getLabel().name());
		node.setProperty(CREATED, DateUtils.getCurrentTime());

		if (data.usesIndex()) {
			Index<Node> nodeIndex = getDb().index().forNodes(data.getIndexName().name());
			nodeIndex.add(node, NAME, data.getIndexValue());
		}

		for (Map.Entry<String, String> entry : data.getAttributeValues().entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			node.setProperty(entry.getKey(), entry.getValue());
		}

		data.setId(node.getId());

		createRelationshipsForData(data);

		transaction.success();
		transaction.finish();

		return data;
	}

	@Override
	public void deleteNode(PkNodeData data) {
		Node node = getDb().getNodeById(data.getId());

		Transaction transaction = getDb().beginTx();

		for (Relationship relation : node.getRelationships()) {
			System.out.println("Deleting rel: " + relation.getProperty("NAME"));
			relation.delete();
		}
		node.delete();

		transaction.success();
		transaction.finish();
	}

	@Override
	public void createRelationship(PkNodeData start, RelationshipType type, PkNodeData end) {
		createRelationship(start, type, end, Collections.<String, String> emptyMap());
	}

	@Override
	public void createRelationship(PkNodeData start, RelationshipType type, PkNodeData end, Map<String, String> attributeValues) {
		Node startNode = getDb().getNodeById(start.getId());
		Node endNode = getDb().getNodeById(end.getId());

		Relationship relation = startNode.createRelationshipTo(endNode, type);
		relation.setProperty(CREATED, DateUtils.getCurrentTime());

		for (Map.Entry<String, String> entry : attributeValues.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			relation.setProperty(entry.getKey(), entry.getValue());
		}

		System.out.println(String.format("[%s] ==== Relation %s [id=%s] created. Values [%s] ", DateUtils.getCurrentTime(), type, relation.getId(), attributeValues.toString()));
	}

	@Override
	public void createRelationships(List<RelationData> relations) {
		for (RelationData relation : relations) {
			createRelationship(relation.getStartPkNodeData(), relation.getRelationshipType(), relation.getEndPkNodeData(), relation.getRelationAttributeValues());
		}
	}

	@Override
	public void createRelationshipsForData(PkNodeData data) {
		createRelationships(data.getRelations());
	}

}
