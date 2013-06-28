package com.geewhiz.eis.commandline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.geewhiz.eis.executer.CreatePropertyExecuter;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Provides;

@Parameters(commandNames = "create-property",
            separators = "=",
            commandDescription = "Create a property Node.")
public class CreatePropertyCommandModule extends DbCommandModule {

	@Parameter(names = { "--name", "-n" },
	           required = true,
	           description = "The Name of the environment.")
	protected String name;

	@Parameter(names = { "--wasIntroduced", "-i" },
	           required = true,
	           description = "Since when was the property introduced?")
	protected String wasIntroduced;

	@Parameter(names = { "--temporalScope", "-t" },
	           required = true,
	           description = "The TemporalScope. Values are: LATEST and DEPLOYMENT_DEPENDENT")
	protected String temporalScope;

	@Parameter(names = { "--description", "-d" },
	           required = false,
	           description = "The description of the property.")
	protected String description;

	@Parameter(names = { "--force", "-f" },
	           required = false,
	           description = "If the node allready exists, it will be recreated with the given arguments")
	protected Boolean force = false;

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuters(GraphManipulator graphManipulator, NodeFinderFactory nodeFinderFactory) {
		List<EisExecuter> executers = new ArrayList<EisExecuter>();
		executers.add(new CreatePropertyExecuter(graphManipulator, nodeFinderFactory, name, wasIntroduced, temporalScope, description, force));

		return executers;
	}

}
