package com.geewhiz.eis.commandline;

import java.util.Collections;
import java.util.List;

import javax.inject.Named;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.executer.GetPropertyValueExecuter;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.google.inject.Provides;

@Parameters(commandNames = "get-value",
            separators = "=",
            commandDescription = "Gets the value for the given arguments.")
public class GetValueCommandModule extends DbCommandModule {

	@Parameter(names = { "--property", "-p" },
	           required = true,
	           description = "For which property do you want the value.")
	protected String property;

	@Parameter(names = { "--deployment", "-d" },
	           required = true,
	           description = "For which deployment do you want the value.")
	protected String deployment;

	@Parameter(names = { "--environment", "-e" },
	           required = true,
	           description = "For which environment do you want the value.")
	protected String environment;

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuter(NodeFinderFactory factory) {
		return Collections
		        .<EisExecuter> singletonList(new GetPropertyValueExecuter(factory, property, deployment, environment));
	}

}
