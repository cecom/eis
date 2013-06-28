package com.geewhiz.eis.node.manipulator.rest;

import java.util.Collections;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;

import com.geewhiz.eis.node.manipulator.AbstractGraphManipulator;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Inject;

public class RestGraphManipulator extends AbstractGraphManipulator implements GraphManipulator {

	private RestCypherQueryEngine restQueryEngine;

	@Inject
	public RestGraphManipulator(GraphDatabaseService db) {
		super(db);
		this.restQueryEngine = new RestCypherQueryEngine(((RestGraphDatabase) db).getRestAPI());
	}

	@Override
	public void deleteAll() {
		Transaction transaction = getDb().beginTx();

		String query = "START n = node(*) MATCH n-[r?]-() DELETE n, r";

		restQueryEngine.query(query, Collections.<String, Object> emptyMap());

		transaction.success();
		transaction.finish();

	}

}
