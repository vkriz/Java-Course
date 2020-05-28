package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Sučelje za potrebe implementacije naredbi u MyShell razredu.
 * 
 * @author Valentina Križ
 *
 */
public interface ShellCommand {
	/**
	 * Metoda za izvršavanje naredbe.
	 * 
	 * @param env instanca sučelja Environment koje se koristi
	 * @param arguments argumenti naredbe
	 * @return status MyShell-a nakon izvođenja naredbe
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Metoda za dohvaćanje imena naredbe.
	 * 
	 * @return ime naredbe
	 */
	String getCommandName();
	
	/**
	 * Metoda za dohvaćanje opisa naredbe.
	 * 
	 * @return opis naredbe
	 */
	List<String> getCommandDescription();
}
