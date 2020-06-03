package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Program učitava datoteku za zadanim imenom
 * i pomoću SmartScriptEngine razreda izvršava
 * program u toj datoteci.
 * 
 * @author Valentina Križ
 *
 */
public class SmartScriptEngineDemo {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * Metoda očekuje jedan parametar komandne linije
	 * koji predstavlja ime datoteke koju treba parsirati.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Program prima jedan parametar koji predstavlja ime datoteke.");
			return;
		}
		
		String documentBody;
		try {
			documentBody = readFromDisk(args[0]);
		} catch (IOException e) {
			System.out.println("Greška pri čitanju datoteke");
			return;
		}
		
		Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();

	}

	/**
	 * Pomoćna metoda za čitanje datoteke
	 * s diska računala.
	 * 
	 * @param string ime datoteke
	 * @return tekst datoteke
	 * @throws IOException 
	 */
	private static String readFromDisk(String fileName) throws IOException {
		return new String(
				 Files.readAllBytes(Paths.get(fileName)),
				 StandardCharsets.UTF_8
				);
	}
}
