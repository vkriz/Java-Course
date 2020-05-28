package hr.fer.zemris.java.hw06.shell.commands;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred implementira sučelje ShellCommand, a predstavlja
 * shell naredbu za ispis vrijednosti i promjenu vrijednosti
 * specijalnog znaka.
 * 
 * @author Valentina Križ
 *
 */
public class SymbolShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] argArray = arguments.trim().split("\\s+");
		
		if(argArray.length < 1 || argArray.length > 2) {
			env.writeln("Command takes one or two arguments.");
			return ShellStatus.CONTINUE;
		}
		
		if(argArray.length == 2) {
			if(argArray[1].length() > 1) {
				env.writeln("Special symbols must be 1 character long.");
				return ShellStatus.CONTINUE;
			} 
			
			if(argArray[0].equals("PROMPTSYMBOL")) {
				env.writeln("Symbol for PROMPTSYMBOL changed from \'" + env.getPromptSymbol() + "\' to \'" + argArray[1].charAt(0) + "\'.");
				env.setPromptSymbol(argArray[1].charAt(0));
			} else if(argArray[0].equals("MORELINESSYMBOL")) {
				env.writeln("Symbol for MORELINESSYMBOL changed from \'" + env.getMorelinesSymbol() + "\' to \'" + argArray[1].charAt(0) + "\'.");
				env.setMorelinesSymbol(argArray[1].charAt(0));
			} else if(argArray[0].equals("MULTILINESYMBOL")) {
				env.writeln("Symbol for MULTILINESYMBOL changed from \'" + env.getMultilineSymbol() + "\' to \'" + argArray[1].charAt(0) + "\'.");
				env.setMultilineSymbol(argArray[1].charAt(0));
			} else {
				env.writeln("Invalid special symbol name.");
			}
			
		} else {
			if(argArray[0].equals("PROMPTSYMBOL")) {
				env.writeln("Symbol for PROMPTSYMBOL is \'" + env.getPromptSymbol() + "\'.");
			} else if(argArray[0].equals("MORELINESSYMBOL")) {
				env.writeln("Symbol for MORELINESSYMBOL is \'" + env.getMorelinesSymbol() + "\'.");
			} else if(argArray[0].equals("MULTILINESYMBOL")) {
				env.writeln("Symbol for MULTILINESYMBOL is \'" + env.getMultilineSymbol() + "\'.");
			} else {
				env.writeln("Invalid special symbol name.");
			}
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("The mkdir command takes one or two arguments.");
		description.add("The first argument is the name of a special symbol.");
		description.add("The second argument is a new value of that symbol.");
		description.add("If second argument is not provided, command prints the value of selected symbol.");
		
		return description;
	}

}
