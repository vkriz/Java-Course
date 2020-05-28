package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
 * shell naredbu za kopiranje datoteke.
 * 
 * @author Valentina Križ
 *
 */
public class CopyShellCommand implements ShellCommand {
	/**
	 * Pomoćna metoda za upisivanje linija teksta u datoteku.
	 * 
	 * @param newFilePath putanja do datoteke
	 * @param allLines lista linija teksta
	 * @param env instanca sučelja Environment koji se koristi za ispis
	 * @throws IOException
	 */
	private void writeAllLines(Path newFilePath, List<String> allLines, Environment env) throws IOException {
		BufferedWriter bw = Files.newBufferedWriter(newFilePath, StandardCharsets.UTF_8);
		
		for(String line : allLines) {
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
		env.writeln("Done.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String fileName = "";
		String directoryName = "";
		
		try {
			fileName = Util.getNextArgument(arguments, true, 0);
			directoryName = Util.getNextArgument(arguments, true, fileName.length());
		} catch(IllegalArgumentException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(fileName.isBlank() || directoryName.isBlank()) {
			env.writeln("Command expects two arguments.");
			return ShellStatus.CONTINUE;
		}
		
		File file = new File(fileName);
		if(file.isDirectory()) {
			env.writeln("Directory copying is not supported.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			
			Path newFilePath = Paths.get(directoryName + "/" + fileName);
			
			if(Files.exists(newFilePath)) {
				env.writeln("The file " + newFilePath + " already exist. Do you want to overwrite existing file? Write yes or no.");
				String answer = env.readLine().trim();
				if(answer.equals("yes")) {
					env.writeln("Overwriting...");
					writeAllLines(newFilePath, allLines, env);
					
				} else if(answer.equals("no")) {
					env.writeln("Try again with different path.");
				} else {
					env.writeln("Invalid response.");
				}
				return ShellStatus.CONTINUE;
			}
		
			writeAllLines(newFilePath, allLines, env);
			
		} catch (IOException e) {
			env.writeln("Error reading file.");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "copy";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("The copy command expects two arguments: source file name and destination file name.");
		description.add("If destination file exists, user is asked if it is allowed to overwrite it.");
		description.add("Command works only with files (no directories).");
		description.add("If the second argument is directory, the original file is coppied into that directory using the original file name.");

		return description;
	}

}
