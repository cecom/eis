package com.geewhiz.eis.commandline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.geewhiz.eis.executer.CreateDeploymentExecuter;
import com.geewhiz.eis.executer.EisExecuter;
import com.geewhiz.eis.node.finder.NodeFinderFactory;
import com.geewhiz.eis.node.manipulator.GraphManipulator;
import com.google.inject.Provides;

@Parameters(commandNames = "create-deployment",
            separators = "=",
            commandDescription = "Create a deployment Node.")
public class CreateDeploymentCommandModule extends DbCommandModule {

	@Parameter(names = { "--name", "-n" },
	           required = true,
	           description = "The Name of the deployment.")
	protected String name;

	@Parameter(names = { "--successor", "-s" },
	           required = false,
	           description = "Who is the successor of the deployment")
	protected String successorDeployment;

	@Parameter(names = { "--commit", "-c" },
	           required = false,
	           description = "The SHA1 of the deployment.")
	protected String commit;

	@Parameter(names = { "--tag", "-t" },
	           required = false,
	           description = "The tag of the deployment.")
	protected String tag;

	@Parameter(names = { "--artifactUrl", "-u" },
	           required = false,
	           description = "The artifact url for getting the deployment.")
	protected String artifactUrl;

	@Parameter(names = { "--buildId", "-b" },
	           required = false,
	           description = "The artifact url for getting the deployment.")
	protected String buildId;

	@Parameter(names = { "--force", "-f" },
	           required = false,
	           description = "If the node allready exists, it will be recreated with the given arguments")
	protected Boolean force = false;

	@Provides
	@Named(EIS_COMMAND)
	protected List<EisExecuter> createEisExecuters(GraphManipulator graphManipulator, NodeFinderFactory nodeFinderFactory) {
		List<EisExecuter> executers = new ArrayList<EisExecuter>();
		executers.add(new CreateDeploymentExecuter(graphManipulator, nodeFinderFactory, name, successorDeployment, commit, tag, artifactUrl, buildId, force));

		return executers;
	}

}
