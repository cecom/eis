package com.geewhiz.eis.commandline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.geewhiz.eis.executer.CreateEnvironmentExecuter;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Provides;

@Parameters(commandNames = "create-environment",
            separators = "=",
            commandDescription = "Create a environment Node.")
public class CreateEnvironmentCommandModule extends DbCommandModule {

	@Parameter(names = { "--name", "-n" },
	           required = true,
	           description = "The Name of the environment.")
	protected String name;

	@Parameter(names = { "--basedOn", "-b" },
	           required = false,
	           description = "On which environment is this environment based on?")
	protected String basedOn;

	@Parameter(names = { "--description", "-d" },
	           required = false,
	           description = "The description of the environment.")
	protected String description;

	@Parameter(names = { "--force", "-f" },
	           required = false,
	           description = "If the node allready exists, it will be recreated with the given arguments")
	protected Boolean force = false;

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuters(GraphManipulator graphManipulator, NodeFinderFactory nodeFinderFactory) {
		List<EisExecuter> executers = new ArrayList<EisExecuter>();
		executers.add(new CreateEnvironmentExecuter(graphManipulator, nodeFinderFactory, name, basedOn, description, force));

		return executers;
	}

}
