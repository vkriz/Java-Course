package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred implementira sučelje ShellCommand, a predstavlja
 * shell naredbu za ispis charseta.
 * 
 * @author Valentina Križ
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Map<String, Charset> charsets = Charset.availableCharsets(); 
		
		Iterator<Charset> iterator = charsets.values().iterator(); 

		while (iterator.hasNext()) { 
			Charset current = (Charset)iterator.next(); 
			env.writeln(current.displayName()); 
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("The charsets command takes no arguments and lists names of supported charsets for your Java platform.");
		
		return description;
	}

}
