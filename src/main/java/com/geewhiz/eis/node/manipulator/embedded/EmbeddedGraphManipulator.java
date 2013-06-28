package com.geewhiz.eis.node.manipulator.embedded;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import com.geewhiz.eis.node.manipulator.AbstractGraphManipulator;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Inject;

public class EmbeddedGraphManipulator extends AbstractGraphManipulator implements GraphManipulator {

	private ExecutionEngine executionEngine;

	@Inject
	public EmbeddedGraphManipulator(GraphDatabaseService db) {
		super(db);
		this.executionEngine = new ExecutionEngine(db);
	}

	@Override
	public void deleteAll() {
		Transaction transaction = getDb().beginTx();

		String query = "START n = node(*) MATCH n-[r?]-() DELETE n, r";

		executionEngine.execute(query);

		transaction.success();
		transaction.finish();

	}

}
