package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * Razred implementira sučelje ShellCommand, a predstavlja
 * shell naredbu za prikaz strukture direktorija.
 * 
 * @author Valentina Križ
 *
 */
public class TreeShellCommand implements ShellCommand {
	/**
	 * Pomoćna rekurzivna metoda za ispis strukture direktorija.
	 * 
	 * @param directoryPath putanja do direktorija
	 * @param numSpaces broj razmaka koji je potrebno dodati ispred imena
	 * @return string sa strukturom direktorija
	 */
	private String listDirectoryFiles(Path directoryPath, int numSpaces) {
		StringBuilder sb = new StringBuilder();
		File[] children = directoryPath.toFile().listFiles();
		for(File file : children) {
			sb.append(" ".repeat(numSpaces));
			sb.append(file.toPath().getFileName().toString());
			sb.append("\n");
			if(file.isDirectory()) {
				sb.append(listDirectoryFiles(file.toPath(), numSpaces + 2));
			}
		}
		
		return sb.toString();
	}

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
		
		File folder = new File(folderName);
		
		if(folder.isDirectory()) {
			env.writeln(folder.toPath().getFileName().toString());
			env.write(listDirectoryFiles(folder.toPath(), 2));
		} else {
			env.writeln("Invalid directory name.");
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "tree";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("The tree command expects a single argument: directory name and prints a tree.");
		
		return description;
	}

}
