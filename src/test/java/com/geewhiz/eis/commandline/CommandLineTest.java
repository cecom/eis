package com.geewhiz.eis.commandline;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.geewhiz.eis.commandline.ExportCommandModule;
import com.geewhiz.eis.commandline.ImportCommandModule;

public class CommandLineTest {

	@Test
	public void exportTest() {
		JCommander jc = new JCommander();

		ExportCommandModule command = new ExportCommandModule();

		jc.addCommand("export", command);

		jc.parse("export", "--db=tempDb", "--file=anExportFile.xml");

		Assert.assertEquals(command.db, new File("tempDb"));
		Assert.assertEquals(command.file, new File("anExportFile.xml"));
	}

	@Test
	public void importTest() {
		JCommander jc = new JCommander();

		ImportCommandModule command = new ImportCommandModule();

		jc.addCommand("import", command);

		jc.parse("import", "--db=tempDb", "--file=anExportFile.xml", "--force");

		Assert.assertEquals(command.db, new File("tempDb"));
		Assert.assertEquals(command.file, new File("anExportFile.xml"));
		Assert.assertTrue(command.force);
	}

}
