package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * Instance razreda MyShell oponašaju rad shell-a operacijskog sustava.
 * Omogućene su jednostavnije naredbe kao što su cat, mkdir, copy, ls, itd.
 * Pri pokretanju programa korisniku se ispisuje poruka "Welcome to MyShell v 1.0"
 * nakon čega se čeka na unos naredbi od strane korisnika.
 * Rad programa se prekida naredbom "exit".
 * 
 * @author Valentina Križ
 *
 */
public class MyShell {
	private static Environment environment;
	private static ShellStatus status;
	
	public MyShell() {
		environment = new ShellEnvironment();
	}
	
	/**
	 * Pomoćna metoda za čitanje naredbe koju je korisnik unesao.
	 * U slučaju čitanja naredbe koja se proteže kroz više redaka,
	 * vraća čitavu naredbu bez prelazaka u novi red.
	 * 
	 * @return naredba u obliku stringa
	 */
	private static String readCommand() {
		StringBuilder lines = new StringBuilder();
		String line = environment.readLine();
		
		if(!line.isEmpty()) {
			if(line.charAt(line.length() - 1) == environment.getMorelinesSymbol()) {
				lines.append(line.substring(0, line.length() - 1));
				
				String nextLine = environment.readLine();
				while(nextLine.charAt(0) == environment.getMultilineSymbol() && nextLine.length() > 1) {
					lines.append(" ");
					
					if(nextLine.charAt(nextLine.length() - 1) == environment.getMorelinesSymbol()) {
						lines.append(nextLine.substring(1, nextLine.length() - 1));
					} else {
						lines.append(nextLine.substring(1));
						break;
					}
					nextLine = environment.readLine();
				}
				
				return lines.toString();
			} else {
				return line;
			}
		}
		return "";
	}
	
	/**
	 * Metoda vraća ime naredbe predane u obliku stringa.
	 * 
	 * @param command
	 * @return ime naredbe
	 */
	private static String getCommandName(String command) {
		String name = "";
		if(command != null && !command.isBlank()) {
			name = command.trim().split("\\s+")[0];
		}
		
		return name;
	}
	
	/**
	 * Metoda vraća argumente naredbe predane u obliku stringa.
	 * 
	 * @param command
	 * @return argumenti naredbe
	 */
	private static String getCommandArguments(String command) {
		command = command.trim();
		
		for(int i = 0, len = command.length(); i < len; ++i) {
			if(Character.isWhitespace(command.charAt(i))) {
				return command.substring(i).trim();
			}
		}
		
		return "";
	}
	
	/**
	 * Razred koji služi za komunikaciju s korisnikom, a 
	 * implementira sučelje Environment.
	 * 
	 * @author Valentina Križ
	 *
	 */
	public static class ShellEnvironment implements Environment {
		private char promptSymbol;
		private char multilineSymbol;
		private char morelinesSymbol;
		private SortedMap<String, ShellCommand> commands;
		private Scanner sc;
		
		private ShellEnvironment() {
			promptSymbol = '>';
			multilineSymbol = '|';
			morelinesSymbol = '\\';
			commands = new TreeMap<>();
			sc = new Scanner(System.in);
			
			commands.put("exit", new ExitShellCommand());
			commands.put("ls", new LsShellCommand());
			commands.put("cat", new CatShellCommand());
			commands.put("copy", new CopyShellCommand());
			commands.put("charsets", new CharsetsShellCommand());
			commands.put("help", new HelpShellCommand());
			commands.put("hexdump", new HexdumpShellCommand());
			commands.put("mkdir", new MkdirShellCommand());
			commands.put("symbol", new SymbolShellCommand());
			commands.put("tree", new TreeShellCommand());
		}

		@Override
		public String readLine() throws ShellIOException {
			return sc.hasNextLine() ? sc.nextLine().toString() : "";
		}

		@Override
		public void write(String text) throws ShellIOException {
			System.out.print(text);
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			System.out.println(text);
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return Collections.unmodifiableSortedMap(commands);
		}

		@Override
		public Character getMultilineSymbol() {
			return multilineSymbol;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			if(symbol != null) {
				multilineSymbol = symbol;
			}
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			if(symbol != null) {
				promptSymbol = symbol;
			}
		}

		@Override
		public Character getMorelinesSymbol() {
			return morelinesSymbol;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			if(symbol != null) {
				morelinesSymbol = symbol;
			}
		}
	}
	
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		environment = new ShellEnvironment();
		status = ShellStatus.CONTINUE;
		
		environment.writeln("Welcome to MyShell v 1.0");
		
		while(status.equals(ShellStatus.CONTINUE)) {
			environment.write(environment.getPromptSymbol() + " ");
			
			String stringCommand = readCommand();
			String commandName = getCommandName(stringCommand);
			String arguments = getCommandArguments(stringCommand);
						
			ShellCommand command = environment.commands().get(commandName);
			
			if(command != null) {
				status = command.executeCommand(environment, arguments);
			} else {
				environment.writeln("Unsupported command!");
			}
			
		}
	}
}
