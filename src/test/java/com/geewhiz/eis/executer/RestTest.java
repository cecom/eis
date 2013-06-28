package com.geewhiz.eis.executer;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.rest.graphdb.RestGraphDatabase;

import com.geewhiz.eis.TestDataCreator;
import com.geewhiz.eis.node.finder.embedded.EmbeddedNodeFinderModule;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.geewhiz.eis.node.manipulator.rest.RestGraphManipulator;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

public class RestTest {
	private static Injector injector;

	@Inject
	private GraphDatabaseService db;

	@Inject
	private TestDataCreator testDataCreator;

	@Inject
	private GraphManipulator graphManipulator;

	private static class RestTestModule extends AbstractModule {
		@Provides
		@Singleton
		private GraphDatabaseService createTestDatabase() {
			return new RestGraphDatabase("http://localhost:7474/db/data");
		}

		@Override
		protected void configure() {
			install(new EmbeddedNodeFinderModule());

			bind(GraphManipulator.class).to(RestGraphManipulator.class).asEagerSingleton();
			bind(TestDataCreator.class);
		}
	}

	@BeforeClass
	public static void setup() {
		injector = Guice.createInjector(new RestTestModule());
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
		testDataCreator.testTheData();
	}
}
