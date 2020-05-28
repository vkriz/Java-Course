package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * Razred implementira sučelje ShellCommand, a predstavlja
 * shell naredbu za ispis sadržaja direktorija.
 * 
 * @author Valentina Križ
 *
 */
public class LsShellCommand implements ShellCommand {

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
			File[] children = folder.listFiles();
			for(File file : children) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Path path = file.toPath();
				BasicFileAttributeView faView = Files.getFileAttributeView(
				path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
				);
				
				BasicFileAttributes attributes;
				try {
					attributes = faView.readAttributes();
					FileTime fileTime = attributes.creationTime();
					String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

					StringBuilder sb = new StringBuilder();
					
					sb.append(file.isDirectory() ? 'd' : '-');
					sb.append(file.canRead() ? 'r' : '-');
					sb.append(file.canWrite() ? 'w' : '-');
					sb.append(file.canExecute() ? 'x' : '-');
					sb.append(" ");
					
					String fileLength = Long.toString(file.length());
					sb.append(" ".repeat(10 - fileLength.length()) + fileLength);
				
					sb.append(" ");
					sb.append(formattedDateTime);
					
					sb.append(" ");
					sb.append(file.getName());
					
					env.writeln(sb.toString());
				} catch (IOException e) {
					env.writeln("Error.");
				}
			}
		} else {
			env.writeln("Invalid directory name.");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "ls";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new LinkedList<String>();
		
		description.add("Command ls takes a single argument – directory – and writes a directory listing.");
		description.add("The output consists of 4 columns.");
		description.add("First column indicates if current object is directory (d), readable (r), writable (w) and executable (x).");
		description.add("Second column contains object size in bytes.");
		description.add("Follows file creation date/time and finally file name.");
		
		return description;
	}

}
