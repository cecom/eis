package com.geewhiz.eis.commandline;

import java.util.List;

import com.geewhiz.eis.executer.EisExecuter;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public interface CommandModule extends Module {

	public static String EIS_COMMAND = "eisCommand";

	public static TypeLiteral<List<EisExecuter>> EXECUTER_LIST_TYPE = new TypeLiteral<List<EisExecuter>>() {
	};

	public static Key<List<EisExecuter>> EIS_KEY = Key.get(EXECUTER_LIST_TYPE, Names.named(EIS_COMMAND));

}
