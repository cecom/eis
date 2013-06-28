package com.geewhiz.eis.commandline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.beust.jcommander.Parameters;
import com.geewhiz.eis.executer.DeleteAllExecuter;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Provides;

@Parameters(commandNames = "delete-all",
            separators = "=",
            commandDescription = "Delete the db.")
public class DeleteAllCommandModule extends DbCommandModule {

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuters(GraphManipulator graphManipulator) {
		List<EisExecuter> executers = new ArrayList<EisExecuter>();
		executers.add(new DeleteAllExecuter(graphManipulator));

		return executers;
	}

}
