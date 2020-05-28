package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * Razred implementira sučelje ShellCommand, a predstavlja
 * shell naredbu za izradu novog direktorija.
 * 
 * @author Valentina Križ
 *
 */
public class MkdirShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String folderName = "";
		
		try {
			folderName = Util.getNextArgument(arguments, true, 0);
		} catch(IllegalArgumentException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(folderName.isBlank() || !arguments.substring(folderName.length()).isBlank()) {
			env.writeln("Command takes one argument.");
			return ShellStatus.CONTINUE;
		}
		
		folderName = Util.removeQuotes(folderName);
		
		Path path = Paths.get(folderName);
		
		if(Files.exists(path) && Files.isDirectory(path)) {
			env.writeln("Directory already exists.");
			return ShellStatus.CONTINUE;
		} else {
			 File directory = new File(path.toString());
			 directory.mkdir();
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "mkdir";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("The mkdir command takes a single argument: directory name, and creates the appropriate directory structure.");
		
		return description;
	}

}
