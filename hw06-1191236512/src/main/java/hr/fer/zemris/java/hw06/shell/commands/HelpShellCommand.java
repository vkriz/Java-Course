package hr.fer.zemris.java.hw06.shell.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred implementira sučelje ShellCommand, a predstavlja
 * shell naredbu za pomoć u korištenju shell-a.
 * 
 * @author Valentina Križ
 *
 */
public class HelpShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		SortedMap<String, ShellCommand> commands = env.commands();
		
		// ako nema argumenata ispisuje imena svih naredbi
		if(arguments.isBlank()) {
			for(String commandName : commands.keySet()) {
				env.writeln(commandName);
			}
			
		} else {
			// inače ispisuje ime i opis odabrane naredbe
			String commandName = arguments.split("\\s+")[0];
			ShellCommand command = commands.get(commandName);
			
			if(command != null) {
				env.writeln(commandName);
				for(String line : command.getCommandDescription()) {
					env.writeln(line);
				}
			} else {
				env.writeln("Unknown command " + commandName + ".");
			}
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("If started with no arguments, the help command lists names of all supported commands.");
		description.add("If started with single argument, it prints name and the description of selected command (or prints appropriate error message if no such command exists).");
		
		return description;
	}

}
