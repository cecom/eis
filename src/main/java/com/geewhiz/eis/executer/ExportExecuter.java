package com.geewhiz.eis.executer;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;

import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class ExportExecuter implements EisExecuter {

	private final File exportToFile;
	private final GraphDatabaseService db;

	public ExportExecuter(GraphDatabaseService db, File exportToFile) {
		if (exportToFile == null) {
			throw new IllegalArgumentException("exportToFile can't be null.");
		}

		this.exportToFile = exportToFile;
		this.db = db;
	}

	@Override
	public void run() {
		if (!exportToFile.exists()) {
			exportToFile.getParentFile().mkdirs();
		} else {
			exportToFile.delete();
		}

		try {
			Neo4jGraph graph = new Neo4jGraph(db);

			new GraphMLWriter(graph).outputGraph(exportToFile.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
