package com.geewhiz.eis.commandline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.executer.SetPropertyValueExecuter;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Provides;

@Parameters(commandNames = "set-value",
            separators = "=",
            commandDescription = "Set a value for the given environment, property and deployment.")
public class SetValueCommandModule extends DbCommandModule {

	@Parameter(names = { "--property", "-p" },
	           required = true,
	           description = "For which property do you want to set the value.")
	protected String property;

	@Parameter(names = { "--deployment", "-d" },
	           required = true,
	           description = "For which deployment do you want to set the value.")
	protected String deployment;

	@Parameter(names = { "--environment", "-e" },
	           required = true,
	           description = "For which environment do you want to set the value.")
	protected String environment;

	@Parameter(names = { "--value", "-v" },
	           required = true,
	           description = "The value. ")
	protected String value;

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuters(GraphManipulator graphManipulator, NodeFinderFactory nodeFinderFactory) {
		List<EisExecuter> executers = new ArrayList<EisExecuter>();
		executers.add(new SetPropertyValueExecuter(graphManipulator, nodeFinderFactory, property, deployment, environment, value));

		return executers;
	}

}
