package com.geewhiz.eis.node.finder.embedded;

import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.node.data.NamedNodeData;
import com.geewhiz.eis.node.finder.NodeFinder;

public abstract class EmbeddedNamedNodeFinder<T extends NamedNodeData> implements NodeFinder<T> {

	private GraphDatabaseService db;

	public EmbeddedNamedNodeFinder(GraphDatabaseService db) {
		this.db = db;
	}

	public T find(String name) {
		Node node = findNode(name);
		return createDataForNode(node);
	}

	public T find(T data) {
		Node node = findNode(data.getName());
		return createDataForNode(node);
	}

	public T findIfExist(String name) {
		Node node = findNodeIfExist(name);
		return node != null ? createDataForNode(node) : null;
	}

	public T findIfExist(T data) {
		Node node = findNodeIfExist(data.getName());
		return node != null ? createDataForNode(node) : null;
	}

	protected abstract IndexName getNodeIndex();

	protected abstract T createDataForNode(Node node);

	public Node findNode(String name) {
		Node node = findNodeIfExist(name);

		if (node == null) {
			throw new IllegalArgumentException("Node [" + name + "] not found in index [" + getNodeIndex() + "].");
		}

		return node;
	}

	public Node findNodeIfExist(String name) {
		Index<Node> nodeIndex = db.index().forNodes(getNodeIndex().name());

		Node node = nodeIndex.get(NAME, name).getSingle();

		return node;
	}

}
