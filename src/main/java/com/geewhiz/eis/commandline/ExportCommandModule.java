package com.geewhiz.eis.commandline;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.inject.Named;

import org.neo4j.graphdb.GraphDatabaseService;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.executer.ExportExecuter;
import com.google.inject.Provides;

@Parameters(commandNames = "export", separators = "=", commandDescription = "Export the EIS DB to a file")
public class ExportCommandModule extends DbCommandModule {

	@Parameter(names = { "--file" },
	           required = true,
	           description = "The File where to export EIS.",
	           converter = FileConverter.class)
	protected File file;

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuter(GraphDatabaseService db) {
		return Collections
		        .<EisExecuter> singletonList(new ExportExecuter(db, file));
	}
}
