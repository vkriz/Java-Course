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
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartScriptEngineDemo {
	public static void main(String[] args) {
		String documentBody;
		try {
			documentBody = readFromDisk("fibonaccih.smscr");
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
