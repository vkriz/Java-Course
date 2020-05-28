package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
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
 * shell naredbu za ispis sadržaja datoteke.
 * 
 * @author Valentina Križ
 *
 */
public class CatShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String fileName = "";
		
		try {
			fileName = Util.getNextArgument(arguments, true, 0);
		} catch(IllegalArgumentException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(fileName.isBlank()) {
			env.writeln("Command takes one or two arguments.");
			return ShellStatus.CONTINUE;
		}
		
		String charsetString = "";
		try {
			charsetString = Util.getNextArgument(arguments, false, fileName.length());
		} catch(IllegalArgumentException ex) {
			env.writeln("Unexpected \" sign.");
			return ShellStatus.CONTINUE;
		}
		

		fileName = Util.removeQuotes(fileName);
		
		Path path = Paths.get(fileName);
		File file = path.toFile();
		
		if(file.isFile() && file.canRead()) {
			// default charset UTF_8
			Charset charset = StandardCharsets.UTF_8;
			
			// ako je zadan i charset promijeni ga
			if(!charsetString.isBlank()) {
				try {
					charset = Charset.forName(charsetString);
				} catch (IllegalCharsetNameException ex) {
					env.writeln("Illegal charset name " + charsetString + ".");
					return ShellStatus.CONTINUE;
				}
			}
			
			// čitaj file sa zadanim charsetom i ispisuj
			try {
				List<String> allLines = Files.readAllLines(path, charset);
				for(String line : allLines) {
					env.writeln(line);
				}
			} catch (IOException e) {
				env.writeln("Error reading file.");
			}
			
		} else {
			env.writeln("Cannot open file " + fileName + ".");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "cat";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("The cat command opens given file and writes its content to console.");
		description.add("Command takes one or two arguments.");
		description.add("The first argument is path to some file and is mandatory.");
		description.add("The second argument is charset name that should be used to interpret chars from bytes.");
		description.add("If not provided, a default platform charset should be used.");
		
		return description;
	}

}
