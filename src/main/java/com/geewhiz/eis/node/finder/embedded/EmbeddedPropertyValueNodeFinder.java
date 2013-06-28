package com.geewhiz.eis.node.finder.embedded;

import static com.geewhiz.eis.constants.Relation.APPLIES_TO;
import static com.geewhiz.eis.constants.Relation.BASED_ON;
import static com.geewhiz.eis.constants.Relation.EFFECTIVE_SINCE;
import static com.geewhiz.eis.constants.Relation.OVERWRITES;
import static com.geewhiz.eis.constants.Relation.SUCCESSOR;
import static com.geewhiz.eis.constants.Relation.VALUE_OF;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;

import com.geewhiz.eis.constants.IndexName;
import com.geewhiz.eis.constants.TemporalScope;
import com.geewhiz.eis.node.data.PropertyNodeData;
import com.geewhiz.eis.node.data.PropertyValueNodeData;
import com.geewhiz.eis.node.data.embedded.EmbeddedPropertyValueNodeData;
import com.geewhiz.eis.node.finder.PropertyValueNodeFinder;
import com.geewhiz.eis.utils.QueryBuilder;

public class EmbeddedPropertyValueNodeFinder implements PropertyValueNodeFinder {

	private GraphDatabaseService db;
	private ExecutionEngine executionEngine;

	@Inject
	public EmbeddedPropertyValueNodeFinder(GraphDatabaseService db) {
		this.db = db;
		this.executionEngine = new ExecutionEngine(db);
	}

	@Override
	public PropertyValueNodeData find(String deployment, String property, String environment) {
		Node node = findNode(deployment, property, environment);
		return new EmbeddedPropertyValueNodeData(node);
	}

	@Override
	public PropertyValueNodeData findLatestIfExists(String property, String environment) {
		Node node = findLatestNodeIfExists(property, environment);
		return new EmbeddedPropertyValueNodeData(node);
	}

	public Node findNode(String deployment, String property, String environment) {

		String query = getQuery(property);

		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("deployment", deployment);
		queryParams.put("property", property);
		queryParams.put("environment", environment);

		ExecutionResult result = executionEngine.execute(query, queryParams);
		return (Node) IteratorUtil.single(result).get("v");
	}

	private Node findLatestNodeIfExists(String property, String environment) {
		String query = getLatestPropertyValueQuery();

		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("property", property);
		queryParams.put("environment", environment);

		ExecutionResult result = executionEngine.execute(query, queryParams);
		Map<String, Object> resultMap = IteratorUtil.singleOrNull(result);
		return resultMap != null ? (Node) resultMap.get("v") : null;
	}

	private String getQuery(String property) {
		EmbeddedPropertyNodeFinder propertyNodeFinder = new EmbeddedPropertyNodeFinder(db);
		PropertyNodeData propertyData = propertyNodeFinder.find(property);

		if (propertyData.getTimeSpan() == TemporalScope.LATEST) {
			return getLatestPropertyValueQuery();
		}

		return getDeploymentDependentValueQuery();
	}

	private String getDeploymentDependentValueQuery() {
		Map<String, String> replaceStrings = new HashMap<String, String>();
		replaceStrings.put("propertyIndex", IndexName.PROPERTY_INDEX.name());
		replaceStrings.put("valueOf", VALUE_OF.name());
		replaceStrings.put("successor", SUCCESSOR.name());
		replaceStrings.put("effectiveSince", EFFECTIVE_SINCE.name());
		replaceStrings.put("appliesTo", APPLIES_TO.name());
		replaceStrings.put("basedOn", BASED_ON.name());

		StringBuffer query = new StringBuffer();
		query.append(" START  p=node:@propertyIndex@(name={property})");
		query.append(" MATCH (p)<-[:@valueOf@]-(v)-[:@appliesTo@]->()<-[:@basedOn@*0..]-(e)");
		query.append(" WHERE e.name={environment}");
		query.append(" WITH  v");
		query.append(" MATCH p=(d)-[:@successor@*0..]->()<-[:@effectiveSince@]-(v)");
		query.append(" WHERE d.name={deployment}");
		query.append(" RETURN v");
		query.append(" ORDER BY LENGTH(p)");
		query.append(" LIMIT 1");

		return QueryBuilder.replaceEntriesInQuery(query.toString(), replaceStrings);
	}

	private String getLatestPropertyValueQuery() {
		Map<String, String> replaceStrings = new HashMap<String, String>();
		replaceStrings.put("propertyIndex", IndexName.PROPERTY_INDEX.name());
		replaceStrings.put("valueOf", VALUE_OF.name());
		replaceStrings.put("appliesTo", APPLIES_TO.name());
		replaceStrings.put("basedOn", BASED_ON.name());
		replaceStrings.put("overwrites", OVERWRITES.name());

		StringBuffer query = new StringBuffer();
		query.append(" START p=node:@propertyIndex@(name={property})");
		query.append(" MATCH path=(p)<-[:@valueOf@]-(v)-[:@appliesTo@]->()<-[:@basedOn@*0..]-(e)");
		query.append(" WHERE e.name={environment}");
		query.append("   AND NOT((e)-[:@basedOn@*0..]->()<-[:@appliesTo@]-()-[:@overwrites@]->(v))");
		query.append(" RETURN v");
		query.append(" ORDER BY LENGTH(path)");
		query.append(" LIMIT 1");

		return QueryBuilder.replaceEntriesInQuery(query.toString(), replaceStrings);
	}

}
