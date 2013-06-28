package com.geewhiz.eis.commandline;

import java.io.File;

import javax.inject.Singleton;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.rest.graphdb.RestGraphDatabase;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.geewhiz.eis.node.finder.embedded.EmbeddedNodeFinderModule;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.geewhiz.eis.node.manipulator.embedded.EmbeddedGraphManipulator;
import com.geewhiz.eis.node.manipulator.rest.RestGraphManipulator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public abstract class DbCommandModule extends AbstractModule implements CommandModule {

	@Parameter(names = "--help", help = true)
	private boolean help;

	@Parameter(names = { "--db" },
	           required = false,
	           description = "If you want to use direct access to the EIS Database for export/import purposes. Make sure the database is down.",
	           converter = FileConverter.class)
	protected File db;

	@Parameter(names = { "--url" },
	           required = false,
	           description = "If you want to use the rest api.")
	protected String url;

	@Provides
	@Singleton
	private GraphDatabaseService createGraphDatabase() {
		if (db != null) {
			return new GraphDatabaseFactory().newEmbeddedDatabase(db.getAbsolutePath());
		} else if (url != null) {
			return new RestGraphDatabase(url);
		} else {
			throw new RuntimeException("You have to specify --db or --url.");
		}
	}

	@Override
	protected void configure() {
		install(new EmbeddedNodeFinderModule());

		if (db != null) {
			bind(GraphManipulator.class).to(EmbeddedGraphManipulator.class);
		} else if (url != null) {
			bind(GraphManipulator.class).to(RestGraphManipulator.class);
		} else {
			throw new RuntimeException("You have to specify --db or --url.");
		}
	}

}
