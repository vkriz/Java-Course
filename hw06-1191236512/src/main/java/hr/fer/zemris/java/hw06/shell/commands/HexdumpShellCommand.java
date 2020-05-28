package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
 * shell naredbu za ispis sadržaja datoteke u heksadecimalnom obliku.
 * 
 * @author Valentina Križ
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	
	private String format(int counter, byte[] buffer, int length) throws UnsupportedEncodingException {
		 StringBuilder sb = new StringBuilder();
		 sb.append(String.format("%1$07X", counter));
		 sb.append(": ");
		 
		 char[] chars = new char[16];
		 for(int i = 0; i < 16; ++i) {
			 sb.append(i < length ? String.format("%02X", buffer[i]) : "  ");
			 sb.append(i == 7 ? "|" : " ");
			 
			 if(i < length) {
				 chars[i] = (buffer[i] < 32 || buffer[i] > 127) ? '.' : (char)buffer[i];
			 }
		 }
		 
		 sb.append("| ");
		 sb.append(chars);
		 return sb.toString();
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String fileName = "";
		
		try {
			fileName = Util.getNextArgument(arguments, true, 0);
		} catch(IllegalArgumentException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(fileName.isBlank() || !arguments.substring(fileName.length()).isBlank()) {
			env.writeln("Command takes one argument.");
			return ShellStatus.CONTINUE;
		}
		
		fileName = Util.removeQuotes(fileName);
		Path path = Paths.get(fileName);
		
		try (InputStream is = Files.newInputStream(path)) {
			 byte[] buffer = new byte[16];
			 int cnt = 0;
			 while(true) {
				 int r = is.read(buffer);
				 if(r < 1) break;
				 env.writeln(format(cnt, buffer, r));
				 ++cnt;
			 }
		} catch(IOException ex) {
			env.writeln("Error reading file " + fileName + "."); 
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
	
		description.add("The hexdump command expects a single argument: file name, and produces hex-output.");
		
		return description;
	}

}
