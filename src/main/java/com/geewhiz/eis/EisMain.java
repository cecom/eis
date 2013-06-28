package com.geewhiz.eis;

import java.util.List;
import java.util.ServiceLoader;

import com.beust.jcommander.JCommander;
import com.geewhiz.eis.commandline.CommandModule;
import com.geewhiz.eis.executer.EisExecuter;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class EisMain {
	JCommander commander;

	public EisMain() {
		commander = new JCommander();
	}

	public static void main(String[] args) {
		EisMain eisMain = new EisMain();
		eisMain.run(args);
	}

	public void run(String[] args) {
		for (CommandModule command : ServiceLoader.load(CommandModule.class)) {
			commander.addCommand(command);
		}

		commander.parse(args);

		CommandModule command = getCommandOrExitWithUsage();

		for (EisExecuter executer : getExecuters(command)) {
			executer.run();
		}
	}

	private CommandModule getCommandOrExitWithUsage() {
		String parsedCommand = commander.getParsedCommand();
		if (parsedCommand == null) {
			commander.usage();
			System.exit(1);
		}

		JCommander command = commander.getCommands().get(parsedCommand);
		// workaround, but there is actually no other way to get the command object
		return (CommandModule) command.getObjects().get(0);
	}

	private List<EisExecuter> getExecuters(CommandModule command) {
		Injector injector = Guice.createInjector(command);
		return injector.getInstance(CommandModule.EIS_KEY);
	}

}
