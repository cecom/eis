package com.geewhiz.eis.commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.neo4j.graphdb.GraphDatabaseService;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.geewhiz.eis.executer.DeleteAllExecuter;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.executer.ImportExecutor;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Provides;

@Parameters(commandNames = "import", separators = "=", commandDescription = "Import an File to EIS")
public class ImportCommandModule extends DbCommandModule {

	@Parameter(names = { "--file" },
	           required = true,
	           description = "The File which you want to import.",
	           converter = FileConverter.class)
	protected File file;

	@Parameter(names = { "--force" }, required = true, description = "Delete the current content, before importing.")
	protected Boolean force;

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuters(GraphDatabaseService db, GraphManipulator graphManipulator) {
		List<EisExecuter> executers = new ArrayList<EisExecuter>();
		if (force) {
			executers.add(new DeleteAllExecuter(graphManipulator));
		}
		executers.add(new ImportExecutor(db, file));

		return executers;
	}

}
