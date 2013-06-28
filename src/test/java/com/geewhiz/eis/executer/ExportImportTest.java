package com.geewhiz.eis.executer;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.geewhiz.eis.TestDataCreator;
import com.geewhiz.eis.node.finder.embedded.EmbeddedNodeFinderModule;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.geewhiz.eis.node.manipulator.embedded.EmbeddedGraphManipulator;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

public class ExportImportTest {
	File exportFile = new File("target/export/export.xml");
	File dbFolder = new File("target/export/db");

	private static Injector injector;

	@Inject
	private GraphDatabaseService db;

	@Inject
	private TestDataCreator testDataCreator;

	@Inject
	private GraphManipulator graphManipulator;

	private static class TestModule extends AbstractModule {
		@Provides
		@Singleton
		private GraphDatabaseService createTestDatabase() {
			return new TestGraphDatabaseFactory().newImpermanentDatabase();
			// return new TestGraphDatabaseFactory().newEmbeddedDatabase("../neo4j-community-1.9.1/data/graph.db");
		}

		@Override
		protected void configure() {
			install(new EmbeddedNodeFinderModule());

			bind(GraphManipulator.class).to(EmbeddedGraphManipulator.class).asEagerSingleton();
			bind(TestDataCreator.class);
		}
	}

	@BeforeClass
	public static void setup() {
		injector = Guice.createInjector(new TestModule());
	}

	@Before
	public void injectDependencies() {
		injector.injectMembers(this);
	}

	@Test
	public void export() {
		DeleteAllExecuter deleteAllExecuter = new DeleteAllExecuter(graphManipulator);
		deleteAllExecuter.run();

		testDataCreator.createTestData();

		ExportExecuter exportExecuter = new ExportExecuter(db, exportFile);
		exportExecuter.run();

		Assert.assertTrue(exportFile.exists());

		deleteAllExecuter = new DeleteAllExecuter(graphManipulator);
		deleteAllExecuter.run();

		ImportExecutor importExecuter = new ImportExecutor(db, exportFile);
		importExecuter.run();

		testDataCreator.testTheData();
	}
}
