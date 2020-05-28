package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Razred služi za testiranje leksera i parsera.
 * Ime datoteke koje se želi parsirati se šalje preko argumenta
 * komandne linije.
 * 
 * @author Valentina Križ
 *
 */
public class SmartScriptTester {
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args ime datoteke koju treba parsirati
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Program prima točno jedan parametar - put do datoteke.");
			return;
		}
		String docBody;
		try {
			docBody = new String(
					 Files.readAllBytes(Paths.get(args[0])),
					 StandardCharsets.UTF_8
					);
		} catch (IOException e1) {
			System.out.println("Greška pri čitanju datoteke.");
			return;
		}
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.out.println(e);
			System.exit(-1);
		} catch(Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
		System.out.println(originalDocumentBody);
		
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		boolean same = document.equals(document2); // ==> "same" must be true
		String originalDocumentBody2 = document2.toString();
		System.out.println(originalDocumentBody2);
		System.out.println("\n" + same);
	}
}
