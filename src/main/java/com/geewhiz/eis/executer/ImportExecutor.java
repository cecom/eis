package com.geewhiz.eis.executer;

import static com.geewhiz.eis.attributes.GeneralAttributes.LABEL;
import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;

import com.geewhiz.eis.constants.IndexName;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

public class ImportExecutor implements EisExecuter {

	private File importFromFile;
	private GraphDatabaseService db;

	public ImportExecutor(GraphDatabaseService db, File importFromFile) {
		if (importFromFile == null) {
			throw new IllegalArgumentException("import file can't be null");
		}
		if (!importFromFile.exists()) {
			throw new IllegalArgumentException("import file [" + importFromFile.getAbsolutePath() + "] does not exist");
		}

		this.db = db;
		this.importFromFile = importFromFile;
	}

	@Override
	public void run() {
		try {
			InputStream s = new FileInputStream(importFromFile);

			Neo4jGraph graph = new Neo4jGraph(db);

			new GraphMLReader(graph).inputGraph(s);

			updateIndexes();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void updateIndexes() {
		Transaction transaction = db.beginTx();

		for (Node node : GlobalGraphOperations.at(db).getAllNodes()) {
			if (!node.hasProperty(NAME)) {
				continue;
			}

			String name = (String) node.getProperty(NAME);
			String label = (String) node.getProperty(LABEL);

			IndexName index = IndexName.valueOf(label + "_INDEX");
			Index<Node> nodeIndex = db.index().forNodes(index.name());
			nodeIndex.add(node, NAME, name);
		}

		transaction.success();
		transaction.finish();
	}
}
